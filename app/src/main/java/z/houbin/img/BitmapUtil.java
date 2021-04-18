package z.houbin.img;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.DisplayMetrics;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class BitmapUtil {

    /**
     * 三张图片竖直合并为一张图片
     *
     * @param firstBitmap  图片1
     * @param secondBitmap 图片2
     * @param thirdBitmap  图片3
     */
    public static Bitmap mergeBitmap(Bitmap firstBitmap, Bitmap secondBitmap, Bitmap thirdBitmap) {
        // 保证3张图片大小一致
        firstBitmap = cropImage(firstBitmap, getImageWidth(), getImageWidth());
        secondBitmap = cropImage(secondBitmap, getImageWidth(), getImageWidth());
        thirdBitmap = cropImage(thirdBitmap, getImageWidth(), getImageWidth());

        Bitmap bitmap = Bitmap.createBitmap(firstBitmap.getWidth(), firstBitmap.getHeight() * 3, firstBitmap.getConfig());

        Canvas canvas = new Canvas(bitmap);
        canvas.drawBitmap(firstBitmap, 0, 0, null);
        canvas.drawBitmap(secondBitmap, 0, firstBitmap.getHeight(), null);
        canvas.drawBitmap(thirdBitmap, 0, firstBitmap.getHeight() + secondBitmap.getHeight(), null);
        return bitmap;
    }

    /**
     * 图片剪切
     *
     * @param image  图片
     * @param width  目标宽
     * @param height 目标高
     */
    public static Bitmap cropImage(Bitmap image, float width, float height) {
        Bitmap bitmap = Bitmap.createBitmap((int) width, (int) height, image.getConfig());

        Canvas canvas = new Canvas(bitmap);
        Matrix matrix = new Matrix();
        matrix.postScale(Float.parseFloat((width / image.getWidth()) + ""), Float.parseFloat((height / image.getHeight()) + ""));
        canvas.drawBitmap(image, matrix, null);
        return bitmap;
    }

    /**
     * 图片切割
     *
     * @param bitmap 导入图片
     * @param x      x轴切割
     * @param y      y轴切割
     */
    public static List<Bitmap> split(Bitmap bitmap, int x, int y) {
        bitmap = cropImage(bitmap, getImageWidth(), getImageWidth());
        List<Bitmap> pieces = new ArrayList<Bitmap>();
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int pieceWidth = width / x;
        int pieceHeight = height / y;
        for (int i = 0; i < y; i++) {
            for (int j = 0; j < x; j++) {
                int xValue = j * pieceWidth;
                int yValue = i * pieceHeight;
                Bitmap bitmap2 = Bitmap.createBitmap(bitmap, xValue, yValue,
                        pieceWidth, pieceHeight);
                pieces.add(bitmap2);
            }
        }
        return pieces;
    }

    public static void saveBitmap(Context context, Bitmap bitmap, File file) {
        try {
            if (!Objects.requireNonNull(file.getParentFile()).exists()) {
                file.getParentFile().mkdirs();
            }
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();

            try {
                MediaStore.Images.Media.insertImage(context.getContentResolver(), file.getAbsolutePath(), file.getName(), null);
            } catch (Exception e) {
                e.printStackTrace();
            }
            // 通知图库更新
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(new File(file.getPath()))));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static float getImageWidth() {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        return (float) (metrics.widthPixels / 3);
    }
}
