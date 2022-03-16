package com.example.asasfans.ui.main.fragment;

import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.asasfans.R;
import com.example.asasfans.data.ImageDataBean;
import com.example.asasfans.ui.main.adapter.ImageAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.scwang.smart.refresh.footer.BallPulseFooter;
import com.scwang.smart.refresh.header.BezierRadarHeader;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import java.io.IOException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.example.asasfans.ui.main.fragment.BiliVideoFragment.GET_DATA_SUCCESS;
import static com.example.asasfans.ui.main.fragment.BiliVideoFragment.NETWORK_ERROR;

/**
 * @author: akari
 * @date: 2022/3/8
 * @description 图片加载页面
 */
public class ImageFanArtFragment extends Fragment {
    public static int divider = 15;
    private ImageAdapter imageAdapter;
    private ImageUrl imageUrl;
    private View view;
    private List<ImageDataBean> imageRecyclerViewData = new ArrayList<>();
    private RecyclerView recyclerView;
    private RefreshLayout refreshLayout;
    private String[] order = {"最新发布", "B站热门"};
    private String[] date = {"无榜单", "日榜", "周榜", "月榜"};
    private String[] tag = {"全部TAG", "A-SOUL", "向晚AvA", "贝拉Bella", "珈乐Carol", "嘉然Diana",
                        "乃琳Queen", "贝贝珈", "乃贝", "嘉晚饭", "琳狼", "珈特琳", "果丹皮", "琳嘉女孩"};
    private enum sort{
        pubdateImage(3), biliHotImage(4);
        private int value = 3;
        sort(int value) {
            this.value = value;
        }
        public int value() {
            return this.value;
        }
    }
    private enum part{
        allTag(0), asoul(1712619), ava(9221368), bella(195579), carol(17872743),
        diana(17520266), queen(17839311), bbj(18207897), nb(18843054), jwf(17895874),
        ll(21134102), jtl(18579605), gdp(1058727), ljnh(20064249);
        private int value = 0;
        part(int value) {
            this.value = value;
        }
        public int value() {
            return this.value;
        }
    }

    private final ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
    private boolean isFirstOrder = true;
    private boolean isFirstDate = true;
    private boolean isFirstTag = true;


    public static ImageFanArtFragment newInstance() {

        Bundle args = new Bundle();

        ImageFanArtFragment fragment = new ImageFanArtFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_fan_art_image, container, false);
        initImageAdapter();
        initSpinner();

        imageUrl = new ImageUrl();
        refreshLayout = (RefreshLayout)view.findViewById(R.id.fan_art_image_refreshLayout);
        refreshLayout.setRefreshHeader(new BezierRadarHeader(getActivity()));
        refreshLayout.setDragRate(1f);
        refreshLayout.setEnableAutoLoadMore(true);
        refreshLayout.setHeaderTriggerRate((float) 0.3);
        refreshLayout.setRefreshFooter(new BallPulseFooter(getActivity()));
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                imageRecyclerViewData.clear();
                imageAdapter.notifyDataSetChanged();
                cachedThreadPool.execute(networkTask.setParam(imageUrl.getUrl()));
                refreshLayout.finishRefresh();
            }
        });
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                imageUrl.pageSelfAdd();
                cachedThreadPool.execute(networkTask.setParam(imageUrl.getUrl()));
                refreshLayout.finishLoadMore(100/*,false*/);
            }
        });

        imageRecyclerViewData.clear();
        imageAdapter.notifyDataSetChanged();
        cachedThreadPool.execute(networkTask.setParam(imageUrl.getUrl()));
//        cachedThreadPool.execute(networkTask.setParam(imageUrl.getUrl()));
//        new Thread(networkTask.setParam("https://api.asoul.cloud:8000/getPic?page=1&sort=3&part=0&rank=0&type=1")).start();
        return view;
    }

    private void initImageAdapter(){
        imageAdapter = new ImageAdapter(getActivity(), imageRecyclerViewData.size(), imageRecyclerViewData);
        recyclerView = view.findViewById(R.id.fan_art_image_recyclerview);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        RecyclerView.ItemDecoration gridItemDecoration = new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, RecyclerView parent, @NonNull RecyclerView.State state) {
                StaggeredGridLayoutManager.LayoutParams layoutParams = (StaggeredGridLayoutManager.LayoutParams) view.getLayoutParams();
                int spanIndex = layoutParams.getSpanIndex();
                int position = parent.getChildAdapterPosition(view);
                outRect.bottom = divider;
                if (position == 0 || position == 1) {
                    outRect.top = divider * 2;
                } else {
                    outRect.top = 0;
                }
                if (spanIndex % 2 == 0) {//偶数项
                    outRect.left = divider;
                    outRect.right = divider / 2;
                } else {
                    outRect.left = divider / 2;
                    outRect.right = divider;
                }
            }
        };
        recyclerView.addItemDecoration(gridItemDecoration);
        recyclerView.setAdapter(imageAdapter);
    }

    private void initSpinner() {
        //声明一个下拉列表的数组适配器
        ArrayAdapter<String> starAdapterOrder = new ArrayAdapter<String>(this.getActivity(), R.layout.item_for_custom_spinner, order);
        ArrayAdapter<String> starAdapterDate = new ArrayAdapter<String>(this.getActivity(), R.layout.item_for_custom_spinner, date);
        ArrayAdapter<String> starAdapterTag = new ArrayAdapter<String>(this.getActivity(), R.layout.item_for_custom_spinner, tag);
        //设置数组适配器的布局样式
//        starAdapter.setDropDownViewResource(R.layout.spinner_item_select);
        //从布局文件中获取名叫sp_dialog的下拉框
        Spinner order = view.findViewById(R.id.fragment_fan_art_spinner_order);
        Spinner date = view.findViewById(R.id.fragment_fan_art_spinner_date);
        Spinner tag = view.findViewById(R.id.fragment_fan_art_spinner_tag);
        //设置下拉框的标题，不设置就没有难看的标题了
//        sp.setPrompt("请选择配送方式");
        //设置下拉框的数组适配器
        order.setAdapter(starAdapterOrder);
        date.setAdapter(starAdapterDate);
        tag.setAdapter(starAdapterTag);
        //设置下拉框默认的显示第一项
        order.setSelection(-1, true);
        date.setSelection(-1, true);
        tag.setSelection(-1, true);

        //给下拉框设置选择监听器，一旦用户选中某一项，就触发监听器的onItemSelected方法
        order.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (isFirstOrder){
                    isFirstOrder = false;
                }else {
                    switch (position){
                        case 0:
                            imageUrl.setPage(1);
                            imageUrl.setSort(sort.pubdateImage.value());
                            imageUrl.setCtime(0);
                            break;
                        case 1:
                            imageUrl.setPage(1);
                            imageUrl.setSort(sort.biliHotImage.value());
                            imageUrl.setCtime(0);
                            break;
                    }
                    imageRecyclerViewData.clear();
                    imageAdapter.notifyDataSetChanged();
                    cachedThreadPool.execute(networkTask.setParam(imageUrl.getUrl()));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        date.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (isFirstDate){
                    isFirstDate = false;
                }else {
                    switch (position) {
                        case 0:
                            imageUrl.setRank(0);
                            imageUrl.setCtime(0);
                            break;
                        case 1:
                            imageUrl.setRank(1);
                            imageUrl.setCtime(System.currentTimeMillis());
                            break;
                        case 2:
                            imageUrl.setRank(2);
                            imageUrl.setSort(sort.biliHotImage.value());
                            imageUrl.setCtime(0);
                            break;
                        case 3:
                            imageUrl.setRank(3);
                            imageUrl.setSort(sort.biliHotImage.value());
                            imageUrl.setCtime(0);
                            break;
                    }
                    imageRecyclerViewData.clear();
                    imageAdapter.notifyDataSetChanged();
                    cachedThreadPool.execute(networkTask.setParam(imageUrl.getUrl()));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        tag.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (isFirstTag){
                    isFirstTag = false;
                }else {
                    switch (position) {
                        case 0:
                            imageUrl.setPart(part.allTag.value());
                            break;
                        case 1:
                            imageUrl.setPart(part.asoul.value());
                            break;
                        case 2:
                            imageUrl.setPart(part.ava.value());
                            break;
                        case 3:
                            imageUrl.setPart(part.bella.value());
                            break;
                        case 4:
                            imageUrl.setPart(part.carol.value());
                            break;
                        case 5:
                            imageUrl.setPart(part.diana.value());
                            break;
                        case 6:
                            imageUrl.setPart(part.queen.value());
                            break;
                        case 7:
                            imageUrl.setPart(part.bbj.value());
                            break;
                        case 8:
                            imageUrl.setPart(part.nb.value());
                            break;
                        case 9:
                            imageUrl.setPart(part.jwf.value());
                            break;
                        case 10:
                            imageUrl.setPart(part.ll.value());
                            break;
                        case 11:
                            imageUrl.setPart(part.jtl.value());
                            break;
                        case 12:
                            imageUrl.setPart(part.gdp.value());
                            break;
                        case 13:
                            imageUrl.setPart(part.ljnh.value());
                            break;
                    }
                    imageRecyclerViewData.clear();
                    imageAdapter.notifyDataSetChanged();
                    cachedThreadPool.execute(networkTask.setParam(imageUrl.getUrl()));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        imageRecyclerViewData.clear();
        imageAdapter.notifyDataSetChanged();
    }

    private Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            String val = data.getString("dataBean");
            Gson gson = new GsonBuilder().serializeNulls().create();
            switch (msg.what) {
                case GET_DATA_SUCCESS:
                    if (val.equals("{\"code\": 1, \"message\": \"\\u6ca1\\u6709\\u66f4\\u591a\\u6570\\u636e\"}")){
                        imageUrl.pageSelfDecrement();
                        Toast.makeText(getContext(),"后面没有了~",Toast.LENGTH_SHORT).show();
                        Log.i("GET_DATA_SUCCESS", val);
                        break;
                    }else {
                        List<ImageDataBean> imageDataBean = gson.fromJson(val, new TypeToken<List<ImageDataBean>>() {}.getType());
                        Log.i("GET_DATA_SUCCESS:ImageDataBean", val);
                        int pastSize = imageRecyclerViewData.size();
                        imageRecyclerViewData.addAll(imageDataBean);
                        if (imageRecyclerViewData.size() == 0){
                            Toast.makeText(getContext(),"什么都没有了~",Toast.LENGTH_SHORT).show();
                        }
                        imageAdapter.notifyItemRangeChanged(pastSize, imageDataBean.size());
                    }
                    break;
                case NETWORK_ERROR:
//                    refreshLayout.finishLoadMore(false);
                    break;
            }
        }
    };
    private MyRunnable networkTask = new MyRunnable() {
        String url;
        @Override
        public MyRunnable setParam(String param) {
            url = param;
            return this;
        }

        @Override
        public void run() {
            Message msg = new Message();
            Bundle data = new Bundle();
            // TODO
            // 在这里进行 http request.网络请求相关操作
            OkHttpClient client;

            client = new OkHttpClient.Builder().hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            }).sslSocketFactory(createSSLSocketFactory(), new X509TrustManager() {//忽视https网站的安全证书
                @Override
                public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {

                }

                @Override
                public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {

                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }
            }).readTimeout(5, TimeUnit.SECONDS).build();

            Request request = new Request.Builder().url(url)
                    .get().build();
            Call call = client.newCall(request);
            Response response = null;
            String tmp;
            try {
                response = call.execute();
                tmp = response.body().string();
                msg.what = GET_DATA_SUCCESS;
                data.putString("dataBean", tmp);
                Log.i("ImageDataBean", tmp);
            } catch (IOException e) {
                e.printStackTrace();
//                page--;
                handler.sendEmptyMessage(NETWORK_ERROR);
            }
            msg.setData(data);
            handler.sendMessage(msg);
        }
    };

    private SSLSocketFactory createSSLSocketFactory() {
        SSLSocketFactory ssfFactory = null;
        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, new TrustManager[]{new TrustAllCerts()}, new SecureRandom());
            ssfFactory = sc.getSocketFactory();
        } catch (Exception e) {
        }

        return ssfFactory;
    }

    public class TrustAllCerts implements X509TrustManager {
        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }
    }

    public interface MyRunnable extends Runnable {
        public MyRunnable setParam(String param);
    }

    public class ImageUrl{
        private int page;
        private int sort;
        private int part;
        private int rank;
        private long ctime;
        private String url;

        public ImageUrl(){
            this.page = 1;
            this.sort = 3;
            this.part = 0;
            this.rank = 0;
            this.ctime = 0;
        }

        public ImageUrl(int page, int sort, int part, int rank, int ctime) {
            this.page = page;
            this.sort = sort;
            this.part = part;
            this.rank = rank;
            this.ctime = ctime;
        }

        public String getUrl() {
            url = "https://api.asoul.cloud:8000/getPic?page=" + page +
                    "&sort=" + sort +
                    "&part=" + part +
                    "&rank=" + rank +
                    "&ctime=" + ctime;
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public int getPage() {
            return page;
        }

        public void setPage(int page) {
            this.page = page;
        }

        public void pageSelfAdd() {
            this.page++;
        }

        public void pageSelfDecrement() {
            this.page--;
        }

        public int getSort() {
            return sort;
        }

        public void setSort(int sort) {
            this.sort = sort;
        }

        public int getPart() {
            return part;
        }

        public void setPart(int part) {
            this.part = part;
        }

        public int getRank() {
            return rank;
        }

        public void setRank(int rank) {
            this.rank = rank;
        }

        public long getCtime() {
            return ctime;
        }

        public void setCtime(long ctime) {
            this.ctime = ctime;
        }
    }
}
