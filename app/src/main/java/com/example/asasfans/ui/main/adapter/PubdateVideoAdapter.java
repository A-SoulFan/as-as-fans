package com.example.asasfans.ui.main.adapter;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.asasfans.R;
import com.example.asasfans.data.SingleVideoBean;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.IOException;
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
    public static final int SERVER_ERROR = 3;
    private String BVID_SEARCH_URL = "https://api.bilibili.com/x/web-interface/view?bvid=";
    private int model;
    private Context mContext;
    private int PageSize;
    private List<List<String>> VideosBvid;
    private final String PackageName = "tv.danmaku.bili";
    private final ExecutorService cachedThreadPool = Executors.newCachedThreadPool();

    public PubdateVideoAdapter(Context context, int pageSize, List<List<String>> videosBvid) {
        this.mContext = context;
        this.PageSize = pageSize;
        this.VideosBvid = videosBvid;
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
                ClipData mClipData = ClipData.newPlainText("bvid", VideosBvid.get(videoViewHolder.getBindingAdapterPosition()).get(0));
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
                Toast.makeText(mContext,VideosBvid.get(videoViewHolder.getBindingAdapterPosition()).get(0) + "已复制到剪贴板",Toast.LENGTH_SHORT).show();
                //获取剪贴板管理器：
                ClipboardManager cm = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData mClipData = ClipData.newPlainText("bvid", VideosBvid.get(videoViewHolder.getBindingAdapterPosition()).get(0));
                cm.setPrimaryClip(mClipData);
                return true;
            }
        });

        return videoViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull VideoViewHolder holder, int position) {
        Animation fadeIn = AnimationUtils.loadAnimation(mContext, R.anim.fadein);
        holder.imageView.startAnimation(fadeIn);

        holder.videoTitle.setText(VideosBvid.get(position).get(0));
        Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                Bundle data = msg.getData();
                String val = data.getString("singleVideoData");
                Log.i("singleVideoData", "请求结果为-->" + val);
                Gson gson =new Gson();
                SingleVideoBean singleVideoBean = gson.fromJson(val, SingleVideoBean.class);
//                if (singleVideoBean.getCode() == 62002)

                switch (msg.what){
                    case GET_DATA_SUCCESS:
                        if (singleVideoBean.getData() != null) {
                            holder.videoTitle.setText(singleVideoBean.getData().getTitle());
//                            holder.myImageView.setImageURL(singleVideoBean.getData().getPic());
                            ImageLoader.getInstance().displayImage(singleVideoBean.getData().getPic() + "@480w_300h_1e_1c.jpg", holder.imageView);
                            holder.videoAuthor.setText(singleVideoBean.getData().getOwner().getName());
                            holder.videoDuration.setText(secondsToTime(singleVideoBean.getData().getDuration()));
                            holder.videoLike.setText(viewNumFormat(singleVideoBean.getData().getStat().getLike()) + " 点赞");
                            holder.videoView.setText(viewNumFormat(singleVideoBean.getData().getStat().getView()) + " 播放");
                            holder.videoTname.setText(singleVideoBean.getData().getTname());
                        }else {
                            holder.videoTitle.setText(val);
                        }
                        break;
                    case NETWORK_ERROR:
                        holder.videoAuthor.setText("NETWORK_ERROR");
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
                Request request = new Request.Builder().url(BVID_SEARCH_URL + VideosBvid.get(position).get(0))
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
        cachedThreadPool.execute(networkTask);

    }

    @Override
    public int getItemCount() {
        return VideosBvid.size();
    }
    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return VideosBvid.get(position).hashCode();
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