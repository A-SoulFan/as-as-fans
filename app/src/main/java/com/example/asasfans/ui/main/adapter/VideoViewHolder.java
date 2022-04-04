package com.example.asasfans.ui.main.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.asasfans.R;

/**
 * @author akarinini
 * @description video_recyclerview.xml的RecyclerView.ViewHolder
 */

public class VideoViewHolder extends RecyclerView.ViewHolder {
    ImageView imageView;
    TextView videoTitle;
    TextView videoDuration;
    TextView videoAuthor;
    TextView videoView;
    TextView videoLike;
    TextView videoTname;
    ImageView videoMore;
    ConstraintLayout videoLayout;

    public VideoViewHolder(@NonNull View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.videoPic);
        videoTitle = itemView.findViewById(R.id.videoTitle);
        videoDuration = itemView.findViewById(R.id.videoDuration);
        videoAuthor = itemView.findViewById(R.id.videoAuthor);
        videoView = itemView.findViewById(R.id.videoView);
        videoLike = itemView.findViewById(R.id.videoLike);
        videoTname = itemView.findViewById(R.id.videoTname);
        videoMore = itemView.findViewById(R.id.video_more);
        videoLayout = itemView.findViewById(R.id.videoLayout);
    }
}
