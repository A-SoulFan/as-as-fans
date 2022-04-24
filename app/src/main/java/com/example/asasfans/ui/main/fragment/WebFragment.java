package com.example.asasfans.ui.main.fragment;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static android.content.Context.DOWNLOAD_SERVICE;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tencent.smtt.export.external.interfaces.WebResourceRequest;
import com.tencent.smtt.sdk.DownloadListener;
import android.webkit.URLUtil;
import com.tencent.smtt.sdk.ValueCallback;
import com.tencent.smtt.sdk.WebChromeClient;
import android.webkit.WebResourceError;

import com.tencent.smtt.export.external.interfaces.WebResourceResponse;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.asasfans.AsApplication;
import com.example.asasfans.R;
import com.example.asasfans.TestActivity;
import com.example.asasfans.service.MusicService;
import com.example.asasfans.ui.main.VideoProxyManager;
import com.example.asasfans.util.SystemUtils;



import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/**
 * @author akarinini
 * @description webFragment，使用WebView直接加载URL显示，属于BottomPagerAdapter
 */

public class WebFragment extends Fragment {
    public WebView webView;
    private ProgressBar progressBar;
    private long exitTime = 0;
    private String url = "https://asoulcnki.asia/";
    private static final int REQUEST_CODE_FILE_CHOOSER = 1;

    private ValueCallback<Uri> mUploadCallbackForLowApi;
    private ValueCallback<Uri[]> mUploadCallbackForHighApi;

    private Boolean inBottom = true;
    private String songName = "";
    private String singerName = "";
    private String currentSongTime = "";

    private WebResourceResponse webResourceResponse = null;
    private String proxyUrl;
    private InputStream is;
    public static WebFragment newInstance(String url, Boolean inBottom) {
        WebFragment fragment = new WebFragment();
        Bundle args = new Bundle();
        args.putString("WebUrl", url);
        args.putBoolean("Bottom", inBottom);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.url = getArguments().getString("WebUrl");
        this.inBottom = getArguments().getBoolean("Bottom");
        // This callback will only be called when MyFragment is at least Started.
//        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
//            @Override
//            public void handleOnBackPressed() {
//                // Handle the back button event
//                if (webView.canGoBack()) {
//                    webView.goBack();
//                }else {
//                    getActivity().onBackPressed();
//                }
//            }
//        };
//        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);

        // The callback can be enabled or disabled here or in handleOnBackPressed()
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_CODE_FILE_CHOOSER && (resultCode == RESULT_OK || resultCode == RESULT_CANCELED)) {
            afterFileChooseGoing(resultCode, data);
        }
    }

    private void afterFileChooseGoing(int resultCode, Intent data) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (mUploadCallbackForHighApi == null) {
                return;
            }
            mUploadCallbackForHighApi.onReceiveValue(WebChromeClient.FileChooserParams.parseResult(resultCode, data));
            mUploadCallbackForHighApi = null;
        } else {
            if (mUploadCallbackForLowApi == null) {
                return;
            }
            Uri result = data == null ? null : data.getData();
            mUploadCallbackForLowApi.onReceiveValue(result);
            mUploadCallbackForLowApi = null;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view;
        //兼容状态栏
        if (inBottom) {
            view = inflater.inflate(R.layout.fragment_web, container, false);
            View emptyView = view.findViewById(R.id.emptyViewWeb);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, AsApplication.Companion.getStatusBarHeight());
            emptyView.setLayoutParams(layoutParams);
        }else {
            view = inflater.inflate(R.layout.fragment_tools_web, container, false);
        }

        webView = view.findViewById(R.id.webView);
        progressBar = view.findViewById(R.id.pb);
//        RefreshLayout refreshLayout = (RefreshLayout)view.findViewById(R.id.web_refreshLayout);
//        //先关掉下拉刷新
//        refreshLayout.setEnableRefresh(false);
//        refreshLayout.setRefreshHeader(new BezierRadarHeader(getActivity()));
//        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
//            @Override
//            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
//                webView.loadUrl(url);
//                refreshLayout.finishRefresh();
//            }
//        });

        webView.getSettings().setDatabaseEnabled(true);

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);//设置js可以直接打开窗口，如window.open()，默认为false
        webSettings.setJavaScriptEnabled(true);//是否允许JavaScript脚本运行，默认为false。设置true时，会提醒可能造成XSS漏洞
        webSettings.setSupportZoom(true);//是否可以缩放，默认true
        webSettings.setUseWideViewPort(true);//设置此属性，可任意比例缩放。大视图模式
        webSettings.setLoadWithOverviewMode(true);//和setUseWideViewPort(true)一起解决网页自适应问题
        webSettings.setAppCacheEnabled(true);//是否使用缓存
        webSettings.setDomStorageEnabled(true);//开启本地DOM存储
        webSettings.setLoadsImagesAutomatically(true); // 加载图片
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
//        webView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
//        webSettings.setMixedContentMode(WebSettings.);


        //第一次加载时间会很长，添加加载动画
        webView.setWebChromeClient(new WebChromeClient(){
            @Override
            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
                mUploadCallbackForHighApi = filePathCallback;
                Intent intent = fileChooserParams.createIntent();
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                try {
                    startActivityForResult(intent, REQUEST_CODE_FILE_CHOOSER);
//                    Toast.makeText(getActivity(), "can_open_file_chooser", Toast.LENGTH_LONG).show();
                } catch (ActivityNotFoundException e) {
                    mUploadCallbackForHighApi = null;
//                    Toast.makeText(getActivity(), "R.string.cant_open_file_chooser", Toast.LENGTH_LONG).show();
                    return false;
                }
                return true;
            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if (newProgress <= 100) {
                    progressBar.setProgress(newProgress);
                }

            }
        });

        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideKeyEvent(WebView view, KeyEvent event) {
                return super.shouldOverrideKeyEvent(view, event);
            }

            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, com.tencent.smtt.export.external.interfaces.WebResourceRequest request) {
                Log.i("shouldInterceptRequest:getUrl", request.getUrl().toString());
                if (request.getUrl().toString().startsWith("https://jsxm.sharepoint.cn/sites/as-archive-cn-01/") ||
                        request.getUrl().toString().startsWith("https://as-archive-cn-01.a-soul.fans") ||
                        request.getUrl().toString().startsWith("https://as-archive-load-balance.kzmidc.workers.dev") ||
                        request.getUrl().toString().startsWith("https://cn.as-archive.studio.asf.ink/AZCN-Sharepoint")
                        || request.getUrl().toString().startsWith("https://as-archive-azcn-0001.asf.ink/AZCN-Sharepoint")){

                    webView.post(new Runnable() {
                        @Override
                        public void run() {
                            updateName();
                        }
                    });
                }
                if (request.getUrl().toString().startsWith("https://asbbs-static-01.kzmidc.workers.dev/?file=/uploads/files/1/banner_1646556711136.mp4") ||
                        request.getUrl().toString().startsWith("https://as-archive-cn-01.a-soul.fans/") ||
                        request.getUrl().toString().startsWith("https://as-archive-load-balance.kzmidc.workers.dev") ||
                        request.getUrl().toString().startsWith("https://cn.as-archive.studio.asf.ink/AZCN-Sharepoint")
                        || request.getUrl().toString().startsWith("https://as-archive-azcn-0001.asf.ink/AZCN-Sharepoint")){
                    webResourceResponse = null;
                    proxyUrl = VideoProxyManager.getInstance().getProxyUrl(request.getUrl().toString());
//                    String proxyUrl = ProxyCacheUtils.getProxyUrl(uri.toString(), null, null);
                    Log.i("proxyUrl", proxyUrl);
//                    SystemUtils.inputStreamByUrl(proxyUrl);
                    is = null;
                    if (proxyUrl.startsWith("file://")){
                        try {
                            is = SystemUtils.inputStreamByUrl(proxyUrl.replaceFirst("file://",""));
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                            webResourceResponse = null;
                        }
                        webResourceResponse = new WebResourceResponse("video/avc", "utf-8", is);
                    }else {

                        String[] tmp = proxyUrl.split("/");
//                            is = SystemUtils.inputStreamByUrl(proxyUrl);

                        //模拟音乐播放器放歌以触发videocache的缓存
                        //1.1创建okHttpClient
                        OkHttpClient okHttpClient = new OkHttpClient();

                        //1.2创建Request对象
                        Request okRequest = new Request.Builder().url(proxyUrl).build();

                        //2.把Request对象封装成call对象
                        Call call = okHttpClient.newCall(okRequest);

                        //3.发起异步请求
                        call.enqueue(new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                e.printStackTrace();
                            }
                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                InputStream inputStream = response.body().byteStream();
                                webResourceResponse = new WebResourceResponse("video/avc", "utf-8", inputStream);
                            }
                        });
                    }
//                    is = null;
                    return webResourceResponse;
                }
//                else if (request.getUrl().toString().startsWith("https://jsxm.sharepoint.cn/sites/as-archive-cn-01")){
//                    return new WebResourceResponse("video/mp4", "utf-8", is);
//                }
                return super.shouldInterceptRequest(view, request);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, com.tencent.smtt.export.external.interfaces.WebResourceRequest request) {
//                Log.i("WebResourceRequest:getUrl", request.getUrl().toString());
//                Log.i("WebResourceRequest:getMethod", request.getMethod());
                if (request.getUrl().toString().startsWith("http")) {
                    return super.shouldOverrideUrlLoading(view, request);
                }else {
                    try {
                        Intent it = new Intent();
                        it.setAction(Intent.ACTION_VIEW);
                        it.setData(Uri.parse(request.getUrl().toString()));
                        getActivity().startActivity(it);
                    }catch (Exception e){
                        Toast.makeText(getActivity(), "没有找到对应app", Toast.LENGTH_SHORT).show();
                    }
                    return true;
                }
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                progressBar.setVisibility(View.GONE);
                if (inBottom){
//                    MusicService.updateNotificationShow(0);
                    updateName();
                }
            }

            @Override
            public void onReceivedError(WebView view, com.tencent.smtt.export.external.interfaces.WebResourceRequest request, com.tencent.smtt.export.external.interfaces.WebResourceError error) {
                super.onReceivedError(view, request, error);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    return;
                }
            }
        });

        webView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
//                downloadByBrowser(url);
                try {
                    downloadByBrowser(url);
                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "下载链接浏览器无法识别", Toast.LENGTH_SHORT).show();
                }
//                if (url.startsWith("http")) {
//
////                    downloadBySystem(url, contentDisposition, mimetype);
////                    // 使用
////                    DownloadCompleteReceiver receiver = new DownloadCompleteReceiver();
////                    IntentFilter intentFilter = new IntentFilter();
////                    intentFilter.addAction(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
////                    getActivity().registerReceiver(receiver, intentFilter);
//                }else {
//
//                }

            }
        });

//        NotificationManager notificationManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
//        String channelId = "notification";
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {//适配一下高版本
//            NotificationChannel channel = new NotificationChannel(channelId,
//                    "listen",
//                    NotificationManager.IMPORTANCE_DEFAULT);
//            channel.enableLights(false); //是否在桌面icon展示小红点
//            channel.setLightColor(Color.RED); //小红点颜色
//            channel.setSound(null, null);//关了通知默认提示音
//            channel.setShowBadge(false); //是否在久按桌面图标时显示此渠道的通知
//            notificationManager.createNotificationChannel(channel);
//        }
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(getActivity(), channelId)
//                .setSmallIcon(R.mipmap.ic_launcher)//这玩意在通知栏上显示一个logo
//                .setCategory(CATEGORY_MESSAGE)
//                .setDefaults(DEFAULT_ALL)
//                .setOngoing(true);
//        //点击通知栏跳转的activity
//        Intent intent = new Intent(getActivity(), TestActivity.class);
//        PendingIntent pendingIntent = PendingIntent.getActivity(getActivity(), 0, intent,
//                PendingIntent.FLAG_UPDATE_CURRENT);
//        builder.setAutoCancel(false);//点击不让消失
//        builder.setSound(null);//关了通知默认提示音
//        builder.setPriority(PRIORITY_MAX);//咱们通知很重要
//        builder.setVibrate(null);//关了车震
//        builder.setContentIntent(pendingIntent);//整个点击跳转activity安排上
//        builder.setOnlyAlertOnce(false);
//        RemoteViews remoteViews = initNotifyView();
//        builder.setContent(remoteViews);//把自定义view放上
//        builder.setCustomBigContentView(remoteViews);//把自定义view放上
//        Notification notification = builder.build();
//        notification.flags |= FLAG_ONGOING_EVENT;
//        notification.flags |= Notification.FLAG_NO_CLEAR;//不让手动清除 通知栏常驻
//        notification.sound = null;//关了通知默认提示音
//        notificationManager.notify(1, notification);

        webView.loadUrl(url);
//        webView.loadUrl("https://asoul.cloud/pic");
//        webView.loadUrl("https://liulanmi.com/labs/core.html");
        return view;
    }

//    private RemoteViews initNotifyView() {
//        String packageName = getActivity().getPackageName();
//        RemoteViews remoteView = new RemoteViews(packageName, R.layout.song_player);
//        remoteView.setImageViewResource(R.id.widget_album, R.drawable.icon_asoul);
//        remoteView.setTextViewText(R.id.widget_title, "标题内容");
//        remoteView.setTextViewText(R.id.widget_artist, "小标题内容");
//
//        Intent prv = new Intent(getActivity(), XMPlayerReceiver.class);//播放上一首
//        prv.setAction(PLAY_PRE);
//        PendingIntent intent_prev = PendingIntent.getBroadcast(getActivity(), 1, prv,
//                PendingIntent.FLAG_UPDATE_CURRENT);
//        remoteView.setOnClickPendingIntent(R.id.widget_prev, intent_prev);
//
//
//        Intent next = new Intent(getActivity(), XMPlayerReceiver.class);//播放下一首
//        next.setAction(PLAY_NEXT);
//        PendingIntent intent_next = PendingIntent.getBroadcast(getActivity(), 2, next,
//                PendingIntent.FLAG_UPDATE_CURRENT);
//        remoteView.setOnClickPendingIntent(R.id.widget_next, intent_next);
//
//
//        Intent startpause = new Intent(getActivity(), XMPlayerReceiver.class);//暂停
//        startpause.setAction(PLAY_PAUSE);
//        PendingIntent intent_pause = PendingIntent.getBroadcast(getActivity(), 3, startpause,
//                PendingIntent.FLAG_UPDATE_CURRENT);
//        remoteView.setOnClickPendingIntent(R.id.widget_play, intent_pause);
//
//        Intent startplay = new Intent(getActivity(), XMPlayerReceiver.class);//播放
//        startplay.setAction(PLAY_PLAY);
//        PendingIntent intent_play = PendingIntent.getBroadcast(getActivity(), 4, startplay,
//                PendingIntent.FLAG_UPDATE_CURRENT);
//        remoteView.setOnClickPendingIntent(R.id.widget_play, intent_play);
//        return remoteView;
//    }
//
    public void clickPreviousSong(){
        if (webView == null){
            Toast.makeText(getActivity(), "还没有初始化成功", Toast.LENGTH_SHORT).show();
        }else {
            webView.loadUrl("javascript:document.getElementsByClassName(\"prevButton playButtons\")[0].click();");
        }
    }
    public void clickOtherInfoButton(){
        if (webView == null){
            Toast.makeText(getActivity(), "还没有初始化成功", Toast.LENGTH_SHORT).show();
        }else {
            webView.loadUrl("javascript:document.getElementsByClassName(\"detailsButton otherButtons\")[0].click();");
        }
    }
    public void clickPlaySong(){
        if (webView == null){
            Toast.makeText(getActivity(), "还没有初始化成功", Toast.LENGTH_SHORT).show();
        }else {
            webView.loadUrl("javascript:document.getElementsByClassName(\"playButton playButtons\")[0].click();");
        }
    }
    public void clickNextSong(){
        if (webView == null){
            Toast.makeText(getActivity(), "还没有初始化成功", Toast.LENGTH_SHORT).show();
        }else {
            webView.loadUrl("javascript:document.getElementsByClassName(\"nextButton playButtons\")[0].click();");
//            webView.loadUrl("javascript:document.getElementByXPath(\"//*[@id=\"player\"]/div[1]/div[2]/div[1]/div[2]/div[3]\").click();");
        }
    }

    public String getCurrentSongTime(){
        webView.evaluateJavascript("document.getElementsByClassName(\"currentTime\")[0].innerHTML;"
                , new ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(String value) {
//                        Log.i(SystemUtils.trimFirstAndLastChar(value, (char) 34), "getCurrentSongTime: ");
//                        singerName = SystemUtils.trimFirstAndLastChar(value, (char) 34);
//                        MusicService.updateNotificationShowName(songName, singerName);
                        currentSongTime = value;
                    }
                });
        return currentSongTime;
    }

    public void updateName(){
        webView.evaluateJavascript("document.getElementsByClassName(\"singer\")[0].innerHTML;"
                , new ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(String value) {
                        Log.i(SystemUtils.trimFirstAndLastChar(value, (char) 34), "getSingerNameOnReceiveValue: ");
                        singerName = SystemUtils.trimFirstAndLastChar(value, (char) 34);
                        MusicService.updateNotificationShowName(songName, singerName);
                    }
                });
        webView.evaluateJavascript("document.getElementsByClassName(\"songName\")[0].childNodes[0].innerHTML;"
                , new ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(String value) {
                        Log.i(SystemUtils.trimFirstAndLastChar(value, (char) 34), "getSingerNameOnReceiveValue: ");
                        songName = SystemUtils.trimFirstAndLastChar(value, (char) 34);
                        MusicService.updateNotificationShowName(songName, singerName);
                    }
                });
    }

    public void onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
            Log.i("web:onKeyDown", "canGoBack: ");
            webView.goBack();
        }
        else if ((keyCode == KeyEvent.KEYCODE_BACK) && (!webView.canGoBack()) && event.getRepeatCount() == 0) {
            Log.i("web:onKeyDown", "can not GoBack: ");
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(getActivity(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                getActivity().finish();
            }
        }
    }
    public void onKeyDownInClick(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
            webView.goBack();
        }
        else if ((keyCode == KeyEvent.KEYCODE_BACK) && (!webView.canGoBack())) {
            getActivity().finish();
        }
    }


    private void downloadByBrowser(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        intent.addCategory(Intent.CATEGORY_BROWSABLE);
        getActivity().startActivity(intent);
    }

    private void downloadBySystem(String url, String contentDisposition, String mimeType) {
        // 指定下载地址
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        // 允许媒体扫描，根据下载的文件类型被加入相册、音乐等媒体库
        request.allowScanningByMediaScanner();
        // 设置通知的显示类型，下载进行时和完成后显示通知
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        // 设置通知栏的标题，如果不设置，默认使用文件名
//        request.setTitle("This is title");
        // 设置通知栏的描述
//        request.setDescription("This is description");
        // 允许在计费流量下下载
        request.setAllowedOverMetered(false);
        // 允许该记录在下载管理界面可见
        request.setVisibleInDownloadsUi(false);
        // 允许漫游时下载
        request.setAllowedOverRoaming(true);
        // 允许下载的网路类型
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);
        // 设置下载文件保存的路径和文件名
        String fileName  = URLUtil.guessFileName(url, contentDisposition, mimeType);
//        log.debug("fileName:{}", fileName);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName);
//        另外可选一下方法，自定义下载路径
//        request.setDestinationUri()
//        request.setDestinationInExternalFilesDir()
        final DownloadManager downloadManager = (DownloadManager) getActivity().getSystemService(DOWNLOAD_SERVICE);
        // 添加一个下载任务
        long downloadId = downloadManager.enqueue(request);
//        log.debug("downloadId:{}", downloadId);
    }

    private class DownloadCompleteReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
//            log.verbose("onReceive. intent:{}", intent != null ? intent.toUri(0) : null);
            if (intent != null) {
                if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(intent.getAction())) {
                    long downloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
//                    log.debug("downloadId:{}", downloadId);
                    DownloadManager downloadManager = (DownloadManager) context.getSystemService(DOWNLOAD_SERVICE);
                    String type = downloadManager.getMimeTypeForDownloadedFile(downloadId);
//                    log.debug("getMimeTypeForDownloadedFile:{}", type);
                    if (TextUtils.isEmpty(type)) {
                        type = "*/*";
                    }
                    Uri uri = downloadManager.getUriForDownloadedFile(downloadId);
//                    log.debug("UriForDownloadedFile:{}", uri);
                    if (uri != null) {
                        Intent handlerIntent = new Intent(Intent.ACTION_VIEW);
                        handlerIntent.setDataAndType(uri, type);
                        context.startActivity(handlerIntent);
                    }
                }
            }
        }
    }
}
