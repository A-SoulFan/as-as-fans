package com.example.asasfans.ui.main.adapter;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.asasfans.R;
import com.example.asasfans.data.SingleVideo;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * @author akarinini
 * @description 设计失误，这个类实际是只给AUHotFragment用的Adapter，api不统一的代价
 */

public class VideoAdapter extends RecyclerView.Adapter<VideoViewHolder> {
    private Context mContext;
    private int PageSize;
    private List<SingleVideo> singleVideos;
    private final String PackageName = "tv.danmaku.bili";
    private ImageLoader imageLoader = ImageLoader.getInstance();

    public VideoAdapter(){

    }
    public VideoAdapter(Context context, int pageSize, List<SingleVideo> singleVideos) {
        this.mContext = context;
        this.PageSize = pageSize;
        this.singleVideos = singleVideos;
    }
    @NonNull
    @Override
    public VideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.video_recyclerview, parent,false);
        final VideoViewHolder videoViewHolder = new VideoViewHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(mContext,mResultBean.get(videoViewHolder.getBindingAdapterPosition()).getBvid(),Toast.LENGTH_SHORT).show();
                //获取剪贴板管理器：
                ClipboardManager cm = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData mClipData = ClipData.newPlainText("bvid", singleVideos.get(videoViewHolder.getBindingAdapterPosition()).getBvid());
                cm.setPrimaryClip(mClipData);

                PackageManager packageManager = mContext.getPackageManager();
                Intent intent = packageManager.getLaunchIntentForPackage(PackageName);
                if (intent != null) {
                    mContext.startActivity(intent);
                }else {
                    Toast.makeText(mContext,"没有bilibili，已复制bv号",Toast.LENGTH_SHORT).show();
                }

            }
        });
        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(mContext,singleVideos.get(videoViewHolder.getBindingAdapterPosition()).getBvid() + "已复制到剪贴板",Toast.LENGTH_SHORT).show();
                //获取剪贴板管理器：
                ClipboardManager cm = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData mClipData = ClipData.newPlainText("bvid", singleVideos.get(videoViewHolder.getBindingAdapterPosition()).getBvid());
                cm.setPrimaryClip(mClipData);
                return true;
            }
        });

        return videoViewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull VideoViewHolder holder, int position) {

//        Animation fadeIn = AnimationUtils.loadAnimation(mContext, R.anim.fadein);
//        holder.imageView.startAnimation(fadeIn);

//        holder.imageView.setImageURL(singleVideos.get(position).getPicUrl());
        imageLoader.displayImage(singleVideos.get(position).getPicUrl(), holder.imageView);
        holder.videoAuthor.setText(singleVideos.get(position).getAuthor());
        holder.videoDuration.setText(secondsToTime(singleVideos.get(position).getDuration()));
        holder.videoTitle.setText(singleVideos.get(position).getTitle());
        holder.videoLike.setText(viewNumFormat(singleVideos.get(position).getLike()) + " 点赞");
        holder.videoView.setText(viewNumFormat(singleVideos.get(position).getView()) + " 播放");
        holder.videoTname.setText(singleVideos.get(position).getTname());

    }

    @Override
    public int getItemCount() {
        return PageSize;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }


    private static String secondsToTime(int seconds){
        int h=seconds/3600;			//小时
        int m=(seconds%3600)/60;		//分钟
        int s=(seconds%3600)%60;		//秒
        if(h>0){
            return h + ":" + m +":" + s;
        }
        if(m>0){
            return m + ":" + s;
        }
        return "00:" + s;
    }

    private static String viewNumFormat(int viewNum){
        if ((viewNum - 10000) < 0){
            return viewNum + "";
        }else {
            return viewNum/10000 +"万";
        }
    }

}
