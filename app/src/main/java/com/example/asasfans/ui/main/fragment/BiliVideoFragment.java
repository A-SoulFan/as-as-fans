package com.example.asasfans.ui.main.fragment;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.asasfans.R;
import com.example.asasfans.data.AdvancedSearchDataBean;
import com.example.asasfans.data.DBOpenHelper;
import com.example.asasfans.data.VideoDataStoragedInMemory;
import com.example.asasfans.ui.customcomponent.RecyclerViewDecoration;
import com.example.asasfans.ui.main.adapter.PubdateVideoAdapter;
import com.example.asasfans.util.ApiConfig;
import com.google.gson.Gson;
import com.scwang.smart.refresh.footer.ClassicsFooter;
import com.scwang.smart.refresh.header.MaterialHeader;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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
//    private String VideoUrl;
    public static final int GET_DATA_SUCCESS = 1;
    public static final int NETWORK_ERROR = 2;
    private List<VideoDataStoragedInMemory> videoDataStoragedInMemoryList = new ArrayList<>();
    private List<AdvancedSearchDataBean.DataBean.ResultBean> resultBeans = new ArrayList<>();
    public PubdateVideoAdapter pubdateVideoAdapter;
    private RecyclerView recyclerView;
    private int page = 1;
    private RefreshLayout refreshLayout;
    private ExecutorService cachedThreadPool =  Executors.newCachedThreadPool();;
    private boolean firstOnCreateView = true;
    private ApiConfig apiConfig;
    private ImageView to_top;
    private DBOpenHelper dbOpenHelper;
    private SQLiteDatabase db;


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
//        this.VideoUrl = getArguments().getString("VideoUrl");
        Log.i("VideoUrl", getArguments().getString("VideoUrl"));
        apiConfig = new ApiConfig().fromString(getArguments().getString("VideoUrl"));
        Log.i("apiConfig.getUrl()", apiConfig.getUrl());

    }

    public void refreshing(){
        refreshLayout.autoRefreshAnimationOnly();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        Animation fadeIn = AnimationUtils.loadAnimation(getActivity(), R.anim.fadein);

        recyclerView = view.findViewById(R.id.recyclerview);
        refreshLayout = (RefreshLayout)view.findViewById(R.id.refreshLayout);
        refreshLayout.setRefreshHeader(new MaterialHeader(getActivity()).setColorSchemeResources(R.color.tab_text_normal,R.color.cardWhite,R.color.cardWhite));
        refreshLayout.setRefreshFooter(new ClassicsFooter(getActivity()));
        refreshLayout.setEnableAutoLoadMore(true);
        refreshLayout.setDisableContentWhenRefresh(false);//?????????????????????????????????????????????
        refreshLayout.setDisableContentWhenLoading(false);//?????????????????????????????????????????????
        refreshLayout.setEnableLoadMoreWhenContentNotFull(true);

        to_top = view.findViewById(R.id.to_top);

        //??????RecyclerView??????????????? addOnScrollListener()
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                //??????recyclerView????????????????????????
                LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
                //??????????????????item??????????????????  ?????????0???????????????item????????????????????? ????????????????????????????????? ????????????????????????
                int firstVisibleItemPosition = manager.findFirstVisibleItemPosition();
                // ???????????????????????????????????????????????????
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    // ??????????????????????????????
                    if (firstVisibleItemPosition == 0) {
                        //???????????????????????????????????????
                        to_top.setVisibility(View.GONE);
                    } else {
                        //?????????????????????????????????????????????????????????
                        to_top.startAnimation(fadeIn);
                        to_top.setVisibility(View.VISIBLE);
                        to_top.startAnimation(fadeIn);
                    }
                    //??????RecyclerView?????????????????????
                } else if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    //??????Recyclerview?????????????????????????????????
                    to_top.setVisibility(View.GONE);
                }
            }
        });

        to_top.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerView.smoothScrollToPosition(0);
            }
        });

        pubdateVideoAdapter = new PubdateVideoAdapter(getActivity(), resultBeans, resultBeans.size());
        recyclerView.setAdapter(pubdateVideoAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
//        linearLayoutManager.setStackFromEnd(true);//????????????????????????????????????????????????????????????
//        linearLayoutManager.setReverseLayout(true);//????????????
        linearLayoutManager.setInitialPrefetchItemCount(2);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(new RecyclerViewDecoration(12, 12));
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                apiConfig.setPage(1);
                resultBeans.clear();
                pubdateVideoAdapter.notifyDataSetChanged();
                try {
                    cachedThreadPool.execute(networkTask.setParam(apiConfig.getUrl()));
                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(getActivity(),"???????????????????????????",Toast.LENGTH_SHORT).show();
                }

                refreshLayout.finishRefresh(100/*,false*/);
            }
        });
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                apiConfig.pageSelfAdd();
                try {
                    cachedThreadPool.execute(networkTask.setParam(apiConfig.getUrl()));
                }catch (Exception e){
                    e.printStackTrace();
                    apiConfig.pageSelfDecrement();
                    Toast.makeText(getActivity(),"???????????????????????????",Toast.LENGTH_SHORT).show();
                }
                refreshLayout.finishLoadMore(100/*,false*/);
            }
        });
        if (firstOnCreateView) {
            refreshLayout.autoRefreshAnimationOnly();
            cachedThreadPool.execute(networkTask.setParam(apiConfig.getUrl()));
            firstOnCreateView = false;
        }
        return view;
    }
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            String val = data.getString("AdvancedSearchDataBean");
            Log.i("BiliVideoFragment:AdvancedSearchDataBean", "???????????????-->" + val);
            Gson gson =new Gson();
            switch (msg.what){
                case GET_DATA_SUCCESS:
                    refreshLayout.finishRefresh();
                    if (val.startsWith("{\"code\":0,\"message\":\"ok\"")) {
                        AdvancedSearchDataBean advancedSearchDataBean = gson.fromJson(val, AdvancedSearchDataBean.class);
                        List<AdvancedSearchDataBean.DataBean.ResultBean> allResultBeans = advancedSearchDataBean.getData().getResult();

//                        List<List<String>> hVideosBvid = new ArrayList<>();
//                        hVideosBvid = hPubdateVideoBean.getData().getResult();
                        int PastSize = resultBeans.size();
                        dbOpenHelper = new DBOpenHelper(getActivity(),
                                "blackList.db", null, DBOpenHelper.DB_VERSION);
                        db = dbOpenHelper.getReadableDatabase();
                        Cursor cursor = db.query("blackTag",null,null,null,null,null,null);
                        List<String> blackTag = new ArrayList<>();
                        if (cursor.getCount() > 0) {
                            while (cursor.moveToNext()) {
                                blackTag.add(cursor.getString(cursor.getColumnIndex("tag")));
                                Log.i("cursor.getCount()", cursor.getString(cursor.getColumnIndex("tag")));
                            }
                        }
                        cursor.close();
                        int addVideoCount = 0;
                        for (int i = 0; i < allResultBeans.size(); i++){
                            List currentTag = Arrays.asList(PubdateVideoAdapter.tagFormat(allResultBeans.get(i).getTag()));
                            List<String> tmpBlackTag = new ArrayList<>();
                            tmpBlackTag.addAll(blackTag);
                            tmpBlackTag.retainAll(currentTag);
                            boolean tmp = false;
                            if (tmpBlackTag.size() > 0){
                                tmp = true;
                            }
                            if (!searchInSQL(db, allResultBeans.get(i).getBvid(), "blackBvid", "bvid") &&
                                    !searchInSQL(db, String.valueOf(allResultBeans.get(i).getMid()), "blackMid", "mid") &&
                                    !tmp) {
                                resultBeans.add(allResultBeans.get(i));
                                addVideoCount++;
                            }
                        }
                        if (allResultBeans.size() == 0){
                            apiConfig.pageSelfDecrement();
                            refreshLayout.finishLoadMoreWithNoMoreData();
                            Toast.makeText(getActivity(),"?????????????????????~",Toast.LENGTH_SHORT).show();
                        }else if (addVideoCount == 0){
                            Toast.makeText(getActivity(),"?????????????????????????????????????????????",Toast.LENGTH_SHORT).show();
                        }
                        db.close();
                        dbOpenHelper.close();
                        pubdateVideoAdapter.notifyItemRangeChanged(PastSize, allResultBeans.size());
                        refreshLayout.finishLoadMore(100);
                    }else {
                        if (page > 1){

                        }
                        refreshLayout.finishLoadMoreWithNoMoreData();
                        Toast.makeText(getActivity(),"?????????????????????~",Toast.LENGTH_SHORT).show();
                    }
                    break;
                case NETWORK_ERROR:
                    //??????????????????????????????????????????????????????????????????????????????fragment????????????????????????????????????????????????handle???
                    // ????????????????????????java.io.InterruptedIOException???????????????????????????????????????????????????????????????
                    // ???????????????????????????????????????????????????????????????
                    refreshLayout.finishRefresh(100/*,false*/);
                    refreshLayout.finishLoadMore(100/*,false*/);
                    if (getActivity() != null) {
                        Toast.makeText(getActivity(), "???????????????~", Toast.LENGTH_SHORT).show();
                        break;
                    }
            }
            // TODO
            // UI??????????????????????????????
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
            // ??????????????? http request.????????????????????????
//            page++;
//            ACache aCache = ACache.get(getActivity());
//            String tmpACache = aCache.getAsString(url);
//            if (tmpACache == null) {
            OkHttpClient client = new OkHttpClient.Builder().readTimeout(15, TimeUnit.SECONDS).build();
            Request request = new Request.Builder().url(url)
                    .get().build();
            Call call = client.newCall(request);
            Response response = null;
            String tmp;
            try {
                response = call.execute();
                tmp = response.body().string();
                msg.what = GET_DATA_SUCCESS;
                data.putString("AdvancedSearchDataBean", tmp);
//                    aCache.put(url, tmp, ACache.TIME_HOUR);
            } catch (IOException e) {
                e.printStackTrace();
//                page--;
                handler.sendEmptyMessage(NETWORK_ERROR);
            }
//            }else {
//                msg.what = GET_DATA_SUCCESS;
//                data.putString("AdvancedSearchDataBean", aCache.getAsString(url));
//                Log.i("ACache:AdvancedSearchDataBean", aCache.getAsString(url));
//            }
            msg.setData(data);
            handler.sendMessage(msg);
        }
    };
    public static boolean searchInSQL(SQLiteDatabase db, String str, String table, String col) {

        Cursor cursor = db.rawQuery(
                "select * from   " + table + "  where   " + col +"=? ",
                new String[] { str });
        while (cursor.moveToNext()) {
//            Log.i(" bvid:", str + "?????????????????????,return true");
            return true;
        }
//        Log.i(" bvid:", str + "????????????????????????return false");
        return false;
    }

    @Override
    public void onDetach() {
        pubdateVideoAdapter.closeSQL();
        if (db != null) {
            db.close();
        }
        if (dbOpenHelper != null) {
            dbOpenHelper.close();
        }
        cachedThreadPool.shutdownNow();
        handler.removeCallbacksAndMessages(null);
        super.onDetach();
    }
}
