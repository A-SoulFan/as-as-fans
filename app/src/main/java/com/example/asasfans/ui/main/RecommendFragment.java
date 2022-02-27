package com.example.asasfans.ui.main;

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
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.asasfans.R;
import com.example.asasfans.TestActivity;
import com.example.asasfans.ui.video.PubdateVideoAdapter;
import com.example.asasfans.ui.video.PubdateVideoBean;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author akarinini
 * @description 历史推荐Fragment，属于SectionsPagerAdapter
 */

public class RecommendFragment extends Fragment {
    private static final String PubdateVideoUrl = "http://124.223.8.236:5200/AsoulMostViewVedio?page=";
    private static int page = 1;
    public static final int GET_DATA_SUCCESS = 1;
    public static final int NETWORK_ERROR = 2;
    public static final int SERVER_ERROR = 3;
    private List<List<String>> VideosBvid = new ArrayList<>();


    public static RecommendFragment newInstance() {
        RecommendFragment fragment = new RecommendFragment();
//        Bundle bundle = new Bundle();
//        bundle.putInt(ARG_SECTION_NUMBER, index);
//        fragment.setArguments(bundle);
        return fragment;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pubdate_video, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerviewPubdate);
        SwipeRefreshLayout mSwipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayoutPubdate);
//        mSwipeRefreshLayout.setEnabled(false);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //模拟网络请求需要3000毫秒，请求完成，设置setRefreshing 为false
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                }, 300);
            }
        });

        Gson gson =new Gson();
        PubdateVideoBean pubdateVideoBean = gson.fromJson(TestActivity.getMost(), PubdateVideoBean.class);

        VideosBvid = pubdateVideoBean.getData().getResult();

//        VideoBean.DataBean.ResultBean mResultBean : mVideoBean.getData().getResult()


        final PubdateVideoAdapter pubdateVideoAdapter = new PubdateVideoAdapter(getActivity(), pubdateVideoBean.getData().getNumResults(), VideosBvid);
        pubdateVideoAdapter.setHasStableIds(true);
        recyclerView.setAdapter(pubdateVideoAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL));

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(!recyclerView.canScrollVertically(1)){
                    Toast.makeText(getActivity(), "加载下一页", Toast.LENGTH_SHORT).show();
                    Handler handler = new Handler() {
                        @Override
                        public void handleMessage(Message msg) {
                            super.handleMessage(msg);
                            Bundle data = msg.getData();
                            String val = data.getString("pubdateVideoBean");
                            Log.i("pubdateVideoBean", "请求结果为-->" + val);
                            Gson gson =new Gson();
                            switch (msg.what){
                                case GET_DATA_SUCCESS:
                                    PubdateVideoBean hPubdateVideoBean = gson.fromJson(val, PubdateVideoBean.class);
                                    List<List<String>> hVideosBvid = new ArrayList<>();
                                    hVideosBvid = hPubdateVideoBean.getData().getResult();
                                    int PastSize = VideosBvid.size();
                                    VideosBvid.addAll(hVideosBvid);
                                    Log.i("VideosBvid", VideosBvid.toString());
                                    pubdateVideoAdapter.notifyItemRangeChanged(PastSize, hVideosBvid.size());
                                    break;
                                case NETWORK_ERROR:

                                    break;
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
                            page++;
                            OkHttpClient client = new OkHttpClient.Builder().readTimeout(5, TimeUnit.SECONDS).build();
                            Request request = new Request.Builder().url(PubdateVideoUrl + page)
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
                                page--;
                                handler.sendEmptyMessage(NETWORK_ERROR);
                            }
                            msg.setData(data);
                            handler.sendMessage(msg);
                        }
                    };

                    new Thread(networkTask).start();
                }
                if(!recyclerView.canScrollVertically(-1)){

//                    Toast.makeText(getActivity(), "到顶了", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });

        return view;
    }
}
