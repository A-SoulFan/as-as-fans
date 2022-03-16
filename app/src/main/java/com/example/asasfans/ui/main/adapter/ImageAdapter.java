package com.example.asasfans.ui.main.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.asasfans.R;
import com.example.asasfans.data.ImageDataBean;
import com.example.asasfans.ui.main.fragment.ImageFanArtFragment;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: akari
 * @date: 2022/3/8
 * @description $
 */
public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {
    private Context context;
    private int pageSize;
    private List<ImageDataBean> imageDataBean = new ArrayList<>();

    public ImageAdapter(Context context, int pageSize, List<ImageDataBean> imageDataBean){
        this.context = context;
        this.pageSize = pageSize;
        this.imageDataBean = imageDataBean;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.image_recyclerview, parent,false);
        ImageViewHolder imageViewHolder = new ImageViewHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("bilibili://following/detail/" + imageDataBean.get(imageViewHolder.getBindingAdapterPosition()).getDy_id());
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                context.startActivity(intent);
            }
        });
        return imageViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
//        Animation fadeIn = AnimationUtils.loadAnimation(context, R.anim.fadein);
//        holder.fan_art_image.startAnimation(fadeIn);
//        holder.fan_art_author.startAnimation(fadeIn);

        ImageLoader.getInstance().displayImage(imageDataBean.get(position).getFace() + "@64w_64h_1e_1c.jpg", holder.fan_art_author);
        holder.fan_art_author_name.setText(imageDataBean.get(position).getName());
        double widthImage, heightImage;
        if (imageDataBean.get(position).getPic_url().get(0).getImg_width() >= 480){
            widthImage = 480;
            heightImage = widthImage*imageDataBean.get(position).getPic_url().get(0).getImg_height()/imageDataBean.get(position).getPic_url().get(0).getImg_width();

        }else {
            widthImage = imageDataBean.get(position).getPic_url().get(0).getImg_width();
            heightImage = imageDataBean.get(position).getPic_url().get(0).getImg_height();
        }
        ImageLoader.getInstance().displayImage(imageDataBean.get(position).getPic_url().get(0).getImg_src() + "@"+(int)widthImage+"w_"+(int)heightImage+"h_1e_1c.jpg", holder.fan_art_image);
        holder.fan_art_image_num.setText(String.valueOf(imageDataBean.get(position).getPic_url().size()));
        ViewGroup.LayoutParams layoutParams = holder.fan_art_image.getLayoutParams();

        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;         // 屏幕宽度（像素）
        int height = dm.heightPixels;       // 屏幕高度（像素）
        float density = dm.density;         // 屏幕密度（0.75 / 1.0 / 1.5）
        int densityDpi = dm.densityDpi;     // 屏幕密度dpi（120 / 160 / 240）
        // 屏幕宽度算法:屏幕宽度（像素）/屏幕密度
        int screenWidth = width;  // 屏幕宽度(dp)
//        screenWidth = 600;
        Log.i("widthdp", String.valueOf(screenWidth));
        Log.i("dm.density", String.valueOf(density));

        layoutParams.width = (int) ((screenWidth - ImageFanArtFragment.divider*density)/2);//(imageDataBean.get(position).getPic_url().get(0).getImg_width()/density);
        layoutParams.height = (int) ((imageDataBean.get(position).getPic_url().get(0).getImg_height() /imageDataBean.get(position).getPic_url().get(0).getImg_width())*layoutParams.width);
        holder.fan_art_image.setLayoutParams(layoutParams);
        Log.i("onBindViewHolder", String.valueOf(position));
    }

    @Override
    public int getItemCount() {
        return imageDataBean.size();
    }
    @Override
    public int getItemViewType(int position) {
        return position;
    }


    public class ImageViewHolder extends RecyclerView.ViewHolder{
        private ImageView fan_art_image;
        private ImageView fan_art_author;
        private TextView fan_art_author_name;
        private TextView fan_art_image_num;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            fan_art_image = itemView.findViewById(R.id.fan_art_image);
            fan_art_author = itemView.findViewById(R.id.fan_art_author);
            fan_art_author_name = itemView.findViewById(R.id.fan_art_author_name);
            fan_art_image_num = itemView.findViewById(R.id.fan_art_image_num);
        }
    }
}
