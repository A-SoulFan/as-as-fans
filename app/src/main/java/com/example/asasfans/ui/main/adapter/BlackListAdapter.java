package com.example.asasfans.ui.main.adapter;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;

import com.example.asasfans.R;
import com.example.asasfans.data.DBOpenHelper;
import com.example.asasfans.data.VideoDataStoragedInMemory;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.ArrayList;
import java.util.List;

public class BlackListAdapter extends PubdateVideoAdapter{
    private DialogPlus dialog;
    private View dialogView;
    private Context context;
    private List<VideoDataStoragedInMemory> videoDataStoragedInMemoryList = new ArrayList<>();

    public BlackListAdapter(Context context, List<VideoDataStoragedInMemory> videosBvid) {
        super(context, videosBvid);
        this.context = context;
        videoDataStoragedInMemoryList = videosBvid;
        dialog = DialogPlus.newDialog(context)
                .setContentHolder(new ViewHolder(R.layout.dialog_black_list_more))
                .setContentHeight(ViewGroup.LayoutParams.WRAP_CONTENT)
                .setContentWidth(ViewGroup.LayoutParams.MATCH_PARENT)
                .setCancelable(true)
                .setGravity(Gravity.BOTTOM)
                .create();
        dialogView = dialog.getHolderView();
    }

    @NonNull
    @Override
    public VideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.video_recyclerview, parent,false);
        final VideoViewHolder videoViewHolder = new VideoViewHolder(view);
        videoViewHolder.videoMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCompatButton appCompatButton =  dialogView.findViewById(R.id.dialog_black_list);
                appCompatButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DBOpenHelper dbOpenHelper = new DBOpenHelper(context,"blackList.db",null,1);
                        SQLiteDatabase sqliteDatabase = dbOpenHelper.getWritableDatabase();
                        sqliteDatabase.delete("blackBvid", "bvid=?", new String[]{videoDataStoragedInMemoryList.get(videoViewHolder.getBindingAdapterPosition()).getBvid()});
                        sqliteDatabase.close();
                        dbOpenHelper.close();
                        videoDataStoragedInMemoryList.remove(videoViewHolder.getBindingAdapterPosition());
                        notifyItemRemoved(videoViewHolder.getBindingAdapterPosition());
                        notifyDataSetChanged();
                        Toast.makeText(context,"解除屏蔽视频成功",Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });

        return videoViewHolder;
    }
}
