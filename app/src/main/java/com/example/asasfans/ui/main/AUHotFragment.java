package com.example.asasfans.ui.main;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.asasfans.R;
import com.example.asasfans.TestActivity;
import com.example.asasfans.ui.customcomponent.RecyclerViewDecoration;
import com.example.asasfans.ui.video.SingleVideo;
import com.example.asasfans.ui.video.VideoAdapter;
import com.example.asasfans.ui.video.VideoBean;
import com.google.gson.Gson;
import com.scwang.smart.refresh.header.BezierRadarHeader;
import com.scwang.smart.refresh.header.FalsifyFooter;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

/**
 * @author akarinini
 * @description AU热门的Fragment，属于SectionsPagerAdapter
 *              2022/3/4 更换原生刷新header和footer为SmartRefreshLayout
 */

public class AUHotFragment extends Fragment {
    boolean hasLoad = false;

    public static AUHotFragment newInstance() {
        AUHotFragment fragment = new AUHotFragment();

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_main, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerview);
        RefreshLayout refreshLayout = (RefreshLayout)view.findViewById(R.id.refreshLayout);
        refreshLayout.setRefreshHeader(new BezierRadarHeader(getActivity()));
        refreshLayout.setRefreshFooter(new FalsifyFooter(getActivity()));
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                refreshLayout.finishRefresh(1000/*,false*/);
            }
        });


        Gson gson =new Gson();
        //解析由LaunchActivity传来的AU热门Top30视频的JSON
        //VideoBean为Gson工具根据JSON生成的数据类
        VideoBean mVideoBean = gson.fromJson(TestActivity.getTop30(), VideoBean.class);
        List<SingleVideo> singleVideos = new ArrayList<>();

//        VideoBean.DataBean.ResultBean mResultBean : mVideoBean.getData().getResult()
        //将JSON的信息提取出来格式化为singleVideos，传入VideoAdapter的构造函数
        //这里实际上属于设计失误，api的json设计问题，只有AU热门这里需要这么做，其他页面用另一种方式
        //AU热门top30这个api最开始设计是作为一个集中式访问的api，它把视频的所有信息都包含进去了
        //每个app实际上是独立的ip，不用考虑ban ip的风险，后续的api设计都是只包含视频的bvid，各个客户端根据bvid去获得视频的详细信息
        for (int i = 0; i < mVideoBean.getData().getNumResults(); i++){
            SingleVideo singleVideo = new SingleVideo();
            singleVideo.setAuthor(mVideoBean.getData().getResult().get(i).getOwner().getName());
            singleVideo.setDuration(mVideoBean.getData().getResult().get(i).getDuration());
            singleVideo.setLike(mVideoBean.getData().getResult().get(i).getStat().getLike());
            singleVideo.setPicUrl(mVideoBean.getData().getResult().get(i).getPic());
            singleVideo.setTitle(mVideoBean.getData().getResult().get(i).getTitle());
            singleVideo.setTname(mVideoBean.getData().getResult().get(i).getTname());
            singleVideo.setView(mVideoBean.getData().getResult().get(i).getStat().getView());
            singleVideo.setBvid(mVideoBean.getData().getResult().get(i).getBvid());
            singleVideos.add(i, singleVideo);
        }

        final VideoAdapter mVideoAdapter = new VideoAdapter(getActivity(), mVideoBean.getData().getNumResults(), singleVideos);
        mVideoAdapter.setHasStableIds(true);
        recyclerView.setAdapter(mVideoAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
//        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL));
        recyclerView.addItemDecoration(new RecyclerViewDecoration(12, 12));
        return view;
    }


}
