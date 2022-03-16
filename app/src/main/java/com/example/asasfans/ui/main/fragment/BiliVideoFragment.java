package com.example.asasfans.ui.main.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.asasfans.R;
import com.example.asasfans.data.PubdateVideoBean;
import com.example.asasfans.data.VideoDataStoragedInMemory;
import com.example.asasfans.ui.customcomponent.RecyclerViewDecoration;
import com.example.asasfans.ui.main.adapter.PubdateVideoAdapter;
import com.google.gson.Gson;
import com.scwang.smart.refresh.footer.BallPulseFooter;
import com.scwang.smart.refresh.header.BezierRadarHeader;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

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
 * @author: akari
 * @date: 2022/3/13
 * @description $
 */
public class BiliVideoFragment extends Fragment {
    private String VideoUrl;
    public static final int GET_DATA_SUCCESS = 1;
    public static final int NETWORK_ERROR = 2;
    private List<VideoDataStoragedInMemory> videoDataStoragedInMemoryList = new ArrayList<>();
    private PubdateVideoAdapter pubdateVideoAdapter;
    private RecyclerView recyclerView;
    private int page = 1;
    private RefreshLayout refreshLayout;
    private final ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
    private boolean firstOnCreateView = true;

    public static BiliVideoFragment newInstance(String url) {

        Bundle args = new Bundle();
        args.putString("VideoUrl", url);
        BiliVideoFragment fragment = new BiliVideoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.VideoUrl = getArguments().getString("VideoUrl");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        recyclerView = view.findViewById(R.id.recyclerview);
        refreshLayout = (RefreshLayout)view.findViewById(R.id.refreshLayout);
        refreshLayout.setRefreshHeader(new BezierRadarHeader(getActivity()));
        refreshLayout.setRefreshFooter(new BallPulseFooter(getActivity()));
        refreshLayout.setEnableAutoLoadMore(true);
        pubdateVideoAdapter = new PubdateVideoAdapter(getActivity(), videoDataStoragedInMemoryList);
        recyclerView.setAdapter(pubdateVideoAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        linearLayoutManager.setInitialPrefetchItemCount(2);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(new RecyclerViewDecoration(12, 12));
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                page = 1;
                videoDataStoragedInMemoryList.clear();
                pubdateVideoAdapter.notifyDataSetChanged();
                cachedThreadPool.execute(networkTask.setParam(VideoUrl + page));
                refreshLayout.finishRefresh(100/*,false*/);
            }
        });
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                page++;
                cachedThreadPool.execute(networkTask.setParam(VideoUrl + page));
            }
        });
        if (firstOnCreateView) {
            cachedThreadPool.execute(networkTask.setParam(VideoUrl + page));
            firstOnCreateView = false;
        }
        return view;
    }
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            String val = data.getString("pubdateVideoBean");
            Log.i("BiliVideoFragment:pubdateVideoBean", "请求结果为-->" + val);
            Gson gson =new Gson();
            switch (msg.what){
                case GET_DATA_SUCCESS:
                    if (val.startsWith("{\"code\": 0, \"message\": \"0\"")) {
                        PubdateVideoBean hPubdateVideoBean = gson.fromJson(val, PubdateVideoBean.class);
                        List<List<String>> hVideosBvid = new ArrayList<>();
                        hVideosBvid = hPubdateVideoBean.getData().getResult();
                        int PastSize = videoDataStoragedInMemoryList.size();
                        for (int i = 0; i < hVideosBvid.size(); i++){
                            videoDataStoragedInMemoryList.add(new VideoDataStoragedInMemory("", "", 0, "", 0, 0, "", hVideosBvid.get(i).get(0), true));
                        }
//                        videoDataStoragedInMemoryList.addAll(hVideosBvid);
                        Log.i("BiliVideoFragment:VideosBvid", videoDataStoragedInMemoryList.toString());
                        pubdateVideoAdapter.notifyItemRangeChanged(PastSize, hVideosBvid.size());
                        refreshLayout.finishLoadMore(100);
                    }else {
                        if (page > 1){
                            page--;
                        }
                        refreshLayout.finishLoadMoreWithNoMoreData();
                        Toast.makeText(getContext(),"后面没有了~",Toast.LENGTH_SHORT).show();
                    }
                    break;
                case NETWORK_ERROR:
                    Toast.makeText(getContext(),"网络消失了~",Toast.LENGTH_SHORT).show();
                    break;
            }
            // TODO
            // UI界面的更新等相关操作
        }
    };

    private ImageFanArtFragment.MyRunnable networkTask = new ImageFanArtFragment.MyRunnable() {
        String url;
        @Override
        public ImageFanArtFragment.MyRunnable setParam(String param) {
            url = param;
            return this;
        }

        @Override
        public void run() {
            Message msg = new Message();
            Bundle data = new Bundle();
            // TODO
            // 在这里进行 http request.网络请求相关操作
//            page++;

            OkHttpClient client = new OkHttpClient.Builder().readTimeout(5, TimeUnit.SECONDS).build();
            Request request = new Request.Builder().url(url)
                    .get().build();
            Call call = client.newCall(request);
            Response response = null;
            String tmp;
            try {
                response = call.execute();
                tmp = response.body().string();
                msg.what = GET_DATA_SUCCESS;
                data.putString("pubdateVideoBean", tmp);

            } catch (IOException e) {
                e.printStackTrace();
//                page--;
                handler.sendEmptyMessage(NETWORK_ERROR);
            }
            msg.setData(data);
            handler.sendMessage(msg);
        }
    };
}
