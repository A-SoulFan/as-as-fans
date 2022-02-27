package com.example.asasfans.ui.video;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.asasfans.R;
import com.example.asasfans.ui.customcomponent.MyImageView;

/**
 * @author akarinini
 * @description video_recyclerview.xml的RecyclerView.ViewHolder
 */

public class VideoViewHolder extends RecyclerView.ViewHolder {
    final MyImageView myImageView;
    TextView videoTitle;
    TextView videoDuration;
    TextView videoAuthor;
    TextView videoView;
    TextView videoLike;
    TextView videoTname;

    public VideoViewHolder(@NonNull View itemView) {
        super(itemView);
        myImageView = (MyImageView) itemView.findViewById(R.id.videoPic);
        videoTitle = itemView.findViewById(R.id.videoTitle);
        videoDuration = itemView.findViewById(R.id.videoDuration);
        videoAuthor = itemView.findViewById(R.id.videoAuthor);
        videoView = itemView.findViewById(R.id.videoView);
        videoLike = itemView.findViewById(R.id.videoLike);
        videoTname = itemView.findViewById(R.id.videoTname);
    }


}
