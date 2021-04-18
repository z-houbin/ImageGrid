package z.houbin.img;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.SparseArray;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class PreviewActivity extends AppCompatActivity {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);

        ImageView largeImageView = findViewById(R.id.image);

        int position = getIntent().getIntExtra("IMAGE_POSITION", 0);

        HandlerThread mergeHandlerThread = new HandlerThread("merge-preview");
        mergeHandlerThread.start();

        Handler mergeHandler = new Handler(mergeHandlerThread.getLooper());
        mergeHandler.post(new Runnable() {
            @Override
            public void run() {

                List<Bitmap> blockList = BitmapUtil.split(loadResourcesBitmap(0), 3, 3);

                Bitmap topBitmap = loadResourcesBitmap(position);
                Bitmap centerBitmap = blockList.get(position);
                Bitmap mergeBitmap = BitmapUtil.mergeBitmap(topBitmap, centerBitmap, topBitmap);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        largeImageView.setImageBitmap(mergeBitmap);
                    }
                });
            }
        });
    }

    private Bitmap loadResourcesBitmap(int postion) {
        @SuppressLint("UseSparseArrays")
        SparseArray<Integer> redIs = new SparseArray<>();
        redIs.put(0, R.drawable.c0);
        redIs.put(1, R.drawable.c1);
        redIs.put(2, R.drawable.c2);
        redIs.put(3, R.drawable.c3);
        redIs.put(4, R.drawable.c4);
        redIs.put(5, R.drawable.c5);
        redIs.put(6, R.drawable.c6);
        redIs.put(7, R.drawable.c7);
        redIs.put(8, R.drawable.c8);
        return BitmapFactory.decodeResource(getResources(), redIs.get(postion));
    }

}
