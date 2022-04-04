package com.example.asasfans.ui.main.adapter;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.example.asasfans.R;
import com.example.asasfans.data.DBOpenHelper;
import com.example.asasfans.data.SingleVideoBean;
import com.example.asasfans.data.VideoDataStoragedInMemory;
import com.example.asasfans.ui.main.fragment.BiliVideoFragment;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.OnItemClickListener;
import com.orhanobut.dialogplus.ViewHolder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author akarinini
 * @description 命名错误，这个类实际是真正的VideoAdapter，根据传入的bvid list调用BVID_SEARCH_URL api异步更新各个video item
 */

public class PubdateVideoAdapter extends RecyclerView.Adapter<VideoViewHolder> {
    public static final int GET_DATA_SUCCESS = 1;
    public static final int NETWORK_ERROR = 2;

    private String BVID_SEARCH_URL = "https://api.bilibili.com/x/web-interface/view?bvid=";
    private Context mContext;
    private final String PackageName = "tv.danmaku.bili";
    private final ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
    private List<VideoDataStoragedInMemory> videoDataStoragedInMemoryList = new ArrayList<>();
    private DialogPlus dialog;
    private View dialogView;
    DBOpenHelper dbOpenHelper;
    SQLiteDatabase db;

    public PubdateVideoAdapter(Context context, List<VideoDataStoragedInMemory> videosBvid) {
        this.mContext = context;
        this.videoDataStoragedInMemoryList = videosBvid;
        dbOpenHelper = new DBOpenHelper(context, "blackList.db", null, 1);
        db = dbOpenHelper.getWritableDatabase();
        initDialog();
    }
    public void closeSQL(){
        db.close();
        dbOpenHelper.close();
    }

    void initDialog(){
        dialog = DialogPlus.newDialog(mContext)
                .setContentHolder(new ViewHolder(R.layout.dialog_video_more))
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
        View view = LayoutInflater.from(mContext).inflate(R.layout.video_recyclerview, parent,false);
        final VideoViewHolder videoViewHolder = new VideoViewHolder(view);
        videoViewHolder.videoMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCompatButton blacklistVideo =  dialogView.findViewById(R.id.dialog_black_list);
//                AppCompatButton blacklistAuthor =  dialogView.findViewById(R.id.dialog_black_list_author);
                blacklistVideo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.i("appCompatButton", "onClick");
                        try {
                            DBOpenHelper dbOpenHelper = new DBOpenHelper(mContext,"blackList.db",null,1);
                            SQLiteDatabase sqliteDatabase = dbOpenHelper.getWritableDatabase();
                            ContentValues values = new ContentValues();
                            values.put("bvid", videoDataStoragedInMemoryList.get(videoViewHolder.getBindingAdapterPosition()).getBvid());
                            values.put("PicUrl", videoDataStoragedInMemoryList.get(videoViewHolder.getBindingAdapterPosition()).getPicUrl());
                            values.put("Title", videoDataStoragedInMemoryList.get(videoViewHolder.getBindingAdapterPosition()).getTitle());
                            values.put("Duration", videoDataStoragedInMemoryList.get(videoViewHolder.getBindingAdapterPosition()).getDuration());
                            values.put("Author", videoDataStoragedInMemoryList.get(videoViewHolder.getBindingAdapterPosition()).getAuthor());
                            values.put("ViewNum", videoDataStoragedInMemoryList.get(videoViewHolder.getBindingAdapterPosition()).getView());
                            values.put("LikeNum", videoDataStoragedInMemoryList.get(videoViewHolder.getBindingAdapterPosition()).getLike());
                            values.put("Tname", videoDataStoragedInMemoryList.get(videoViewHolder.getBindingAdapterPosition()).getTname());
                            sqliteDatabase.insert("blackBvid", null, values);
                            sqliteDatabase.close();
                            dbOpenHelper.close();
                        }catch (Exception e){
                            e.printStackTrace();
                            Toast.makeText(mContext, e.toString(), Toast.LENGTH_SHORT).show();
                        }
                        videoDataStoragedInMemoryList.remove(videoViewHolder.getBindingAdapterPosition());
                        notifyItemRemoved(videoViewHolder.getBindingAdapterPosition());
                        notifyItemRangeChanged(videoViewHolder.getBindingAdapterPosition(), getItemCount());
                        Toast.makeText(mContext,"屏蔽视频成功",Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });
//                blacklistAuthor.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        try {
//                            DBOpenHelper dbOpenHelper = new DBOpenHelper(mContext,"blackList.db",null,1);
//                            SQLiteDatabase sqliteDatabase = dbOpenHelper.getWritableDatabase();
//                            ContentValues values = new ContentValues();
//                            values.put("name", videoDataStoragedInMemoryList.get(videoViewHolder.getBindingAdapterPosition()).getAuthor());
//                            sqliteDatabase.insert("blackAuthor", null, values);
//                            sqliteDatabase.close();
//                            dbOpenHelper.close();
//                        }catch (Exception e){
//                            e.printStackTrace();
//                            Toast.makeText(mContext, e.toString(), Toast.LENGTH_SHORT).show();
//                        }
//
//                        videoDataStoragedInMemoryList.remove(videoViewHolder.getBindingAdapterPosition());
//                        notifyItemRemoved(videoViewHolder.getBindingAdapterPosition());
//                        notifyDataSetChanged();
//                        Toast.makeText(mContext,"屏蔽作者成功",Toast.LENGTH_SHORT).show();
//                        dialog.dismiss();
//                    }
//                });
                dialog.show();
            }
        });

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(mContext,mResultBean.get(videoViewHolder.getBindingAdapterPosition()).getBvid(),Toast.LENGTH_SHORT).show();
                PackageManager packageManager = mContext.getPackageManager();
                Intent it = new Intent();
                Intent intent = packageManager.getLaunchIntentForPackage(PackageName);
                if (intent != null) {
                    it.setAction(Intent.ACTION_VIEW);
                    it.setData(Uri.parse("bilibili://video/" + videoDataStoragedInMemoryList.get(videoViewHolder.getBindingAdapterPosition()).getBvid()));
                    mContext.startActivity(it);
                }else {
                    Toast.makeText(mContext,"没有bilibili，已复制bv号",Toast.LENGTH_SHORT).show();
                    // TODO 通过浏览器打开链接
                    //获取剪贴板管理器：
                    ClipboardManager cm = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData mClipData = ClipData.newPlainText("bvid", videoDataStoragedInMemoryList.get(videoViewHolder.getBindingAdapterPosition()).getBvid());
                    cm.setPrimaryClip(mClipData);
                }

            }
        });
        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(mContext,videoDataStoragedInMemoryList.get(videoViewHolder.getBindingAdapterPosition()).getBvid() + "已复制到剪贴板",Toast.LENGTH_SHORT).show();
                //获取剪贴板管理器：
                ClipboardManager cm = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData mClipData = ClipData.newPlainText("bvid", videoDataStoragedInMemoryList.get(videoViewHolder.getBindingAdapterPosition()).getBvid());
                cm.setPrimaryClip(mClipData);
                return true;
            }
        });

        return videoViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull VideoViewHolder holder, @SuppressLint("RecyclerView") int position) {
//        Animation fadeIn = AnimationUtils.loadAnimation(mContext, R.anim.fadein);
//        holder.imageView.startAnimation(fadeIn);

        Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                Bundle data = msg.getData();
                String val = data.getString("singleVideoData");
//                Log.i("singleVideoData", "请求结果为-->" + val);
                Gson gson =new Gson();
                SingleVideoBean singleVideoBean = gson.fromJson(val, SingleVideoBean.class);
//                if (singleVideoBean.getCode() == 62002)
                switch (msg.what){
                    case GET_DATA_SUCCESS:
                        if ((singleVideoBean.getData() != null)) {
                            holder.videoTitle.setText(singleVideoBean.getData().getTitle());
                            videoDataStoragedInMemoryList.get(position).setTitle(singleVideoBean.getData().getTitle());
                            ImageLoader.getInstance().displayImage(singleVideoBean.getData().getPic() + "@480w_300h_1e_1c.jpg", holder.imageView);
                            videoDataStoragedInMemoryList.get(position).setPicUrl(singleVideoBean.getData().getPic() + "@480w_300h_1e_1c.jpg");
                            holder.videoAuthor.setText(singleVideoBean.getData().getOwner().getName());
                            videoDataStoragedInMemoryList.get(position).setAuthor(singleVideoBean.getData().getOwner().getName());
                            holder.videoDuration.setText(secondsToTime(singleVideoBean.getData().getDuration()));
                            videoDataStoragedInMemoryList.get(position).setDuration(singleVideoBean.getData().getDuration());
                            holder.videoLike.setText(viewNumFormat(singleVideoBean.getData().getStat().getLike()) + " 点赞");
                            videoDataStoragedInMemoryList.get(position).setLike(singleVideoBean.getData().getStat().getLike());
                            holder.videoView.setText(viewNumFormat(singleVideoBean.getData().getStat().getView()) + " 播放");
                            videoDataStoragedInMemoryList.get(position).setView(singleVideoBean.getData().getStat().getView());
                            holder.videoTname.setText(singleVideoBean.getData().getTname());
                            videoDataStoragedInMemoryList.get(position).setTname(singleVideoBean.getData().getTname());
                            videoDataStoragedInMemoryList.get(position).setFirstLoad(false);
                        }else {
                            holder.videoTitle.setText(val);
//                            videoDataStoragedInMemoryList.remove(position);
//                            notifyItemRemoved(position);
//                            notifyDataSetChanged();
//                            BiliVideoFragment.pubdateVideoAdapter.notifyItemRemoved(position);
//                            BiliVideoFragment.pubdateVideoAdapter.notifyItemRangeChanged(position, BiliVideoFragment.pubdateVideoAdapter.getItemCount());
                        }
                        break;
                    case NETWORK_ERROR:
                        holder.videoTitle.setText("NETWORK_ERROR");
                }
                // TODO
                // UI界面的更新等相关操作
            }
        };

        Runnable networkTask = new Runnable() {
            @Override
            public void run() {
                Message msg = new Message();
                Bundle data = new Bundle();
                // TODO
                // 在这里进行 http request.网络请求相关操作
                OkHttpClient client = new OkHttpClient.Builder().readTimeout(5, TimeUnit.SECONDS).build();
                Request request = new Request.Builder().url(BVID_SEARCH_URL + videoDataStoragedInMemoryList.get(position).getBvid())
                        .get().build();
                Call call = client.newCall(request);
                Response response = null;
                String tmp;
                try {
                    response = call.execute();
                    tmp = response.body().string();
                    msg.what = GET_DATA_SUCCESS;
                    data.putString("singleVideoData", tmp);

                } catch (IOException e) {
                    e.printStackTrace();
                    handler.sendEmptyMessage(NETWORK_ERROR);
                }
                msg.setData(data);
                handler.sendMessage(msg);
            }
        };

//        new Thread(networkTask).start();
        if ((videoDataStoragedInMemoryList.get(position).getFirstLoad())) {
            cachedThreadPool.execute(networkTask);
        }else {
            holder.videoTitle.setText(videoDataStoragedInMemoryList.get(position).getTitle());
            ImageLoader.getInstance().displayImage(videoDataStoragedInMemoryList.get(position).getPicUrl(), holder.imageView);
            holder.videoAuthor.setText(videoDataStoragedInMemoryList.get(position).getAuthor());
            holder.videoDuration.setText(secondsToTime(videoDataStoragedInMemoryList.get(position).getDuration()));
            holder.videoLike.setText(viewNumFormat(videoDataStoragedInMemoryList.get(position).getLike()) + " 点赞");
            holder.videoView.setText(viewNumFormat(videoDataStoragedInMemoryList.get(position).getView()) + " 播放");
            holder.videoTname.setText(videoDataStoragedInMemoryList.get(position).getTname());
//            Log.i("getFirstLoad:false", String.valueOf(position));
        }

    }


    @Override
    public int getItemCount() {
        return videoDataStoragedInMemoryList.size();
    }
    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return videoDataStoragedInMemoryList.get(position).hashCode();
    }




    /**
     * @description 视频时长为秒，更改显示
     * @param
     * @return
     * @author akari
     * @time 2022/2/27 10:39
     */
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

    /**
     * @description 更改播放量与点赞显示
     * @param
     * @return
     * @author akari
     * @time 2022/2/27 10:40
     */
    private static String viewNumFormat(int viewNum){
        if ((viewNum - 10000) < 0){
            return viewNum + "";
        }else {
            return viewNum/10000 +"万";
        }
    }

}