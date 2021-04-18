package z.houbin.img;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private Handler mergeHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recycler);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(3, 4, false));
        recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this, 3));

        HandlerThread handlerThread = new HandlerThread("merge-main");
        handlerThread.start();

        mergeHandler = new Handler(handlerThread.getLooper(), new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {

                ArrayList<Bitmap> bitmaps = new ArrayList<>();
                bitmaps.add(BitmapFactory.decodeResource(getResources(), R.drawable.c0));
                bitmaps.add(BitmapFactory.decodeResource(getResources(), R.drawable.c1));
                bitmaps.add(BitmapFactory.decodeResource(getResources(), R.drawable.c2));
                bitmaps.add(BitmapFactory.decodeResource(getResources(), R.drawable.c3));
                bitmaps.add(BitmapFactory.decodeResource(getResources(), R.drawable.c4));
                bitmaps.add(BitmapFactory.decodeResource(getResources(), R.drawable.c5));
                bitmaps.add(BitmapFactory.decodeResource(getResources(), R.drawable.c6));
                bitmaps.add(BitmapFactory.decodeResource(getResources(), R.drawable.c7));
                bitmaps.add(BitmapFactory.decodeResource(getResources(), R.drawable.c8));

                // 主图
                Bitmap mainBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.c0);

                ArrayList<Bitmap> mergeBitmapList = new ArrayList<>();

                List<Bitmap> mainBitmapBlockList = BitmapUtil.split(mainBitmap, 3, 3);

                for (int i = 0; i < mainBitmapBlockList.size(); i++) {
                    // 长图居中图片,显示在网格首页
                    Bitmap centerBitmap = mainBitmapBlockList.get(i);
                    // 合并后的图片
                    Bitmap mergeBitmap = BitmapUtil.mergeBitmap(bitmaps.get(i), centerBitmap, bitmaps.get(i));
                    mergeBitmapList.add(mergeBitmap);
                    // 保存文件方便查看,手动授权存储权限
                    BitmapUtil.saveBitmap(getApplicationContext(), mergeBitmap, new File("/sdcard/test/img-" + i + ".png"));
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        recyclerView.setAdapter(new ImageAdapter(getApplicationContext(), mergeBitmapList));
                    }
                });
                return false;
            }
        });


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            } else {
                mergeHandler.sendEmptyMessage(0);
            }
        } else {
            mergeHandler.sendEmptyMessage(0);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        mergeHandler.sendEmptyMessage(0);
    }
}