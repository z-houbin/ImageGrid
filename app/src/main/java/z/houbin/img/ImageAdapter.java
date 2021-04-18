package z.houbin.img;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {
    private final Context context;
    private final List<Bitmap> bitmaps;
    private final int imageWidth;

    public ImageAdapter(Context context, List<Bitmap> bitmaps) {
        this.context = context;
        this.bitmaps = bitmaps;

        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        imageWidth = metrics.widthPixels / 3;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ImageView imageView = new ImageView(parent.getContext());
        imageView.setLayoutParams(new LinearLayout.LayoutParams(imageWidth, imageWidth));
        return new ViewHolder(imageView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.getImageView().setImageBitmap(bitmaps.get(position));
        holder.getImageView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return bitmaps.size();
    }

    public void onItemClick(int position) {
        Intent intent = new Intent(context, PreviewActivity.class);
        intent.putExtra("IMAGE_POSITION", position);
        context.startActivity(intent);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imageView;

        public ViewHolder(ImageView view) {
            super(view);
            this.imageView = view;
            this.imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }

        public ImageView getImageView() {
            return imageView;
        }
    }

}
