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
 * @description webFragment?????????WebView????????????URL???????????????BottomPagerAdapter
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
        //???????????????
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
//        //?????????????????????
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
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);//??????js??????????????????????????????window.open()????????????false
        webSettings.setJavaScriptEnabled(true);//????????????JavaScript????????????????????????false?????????true???????????????????????????XSS??????
        webSettings.setSupportZoom(true);//???????????????????????????true
        webSettings.setUseWideViewPort(true);//?????????????????????????????????????????????????????????
        webSettings.setLoadWithOverviewMode(true);//???setUseWideViewPort(true)?????????????????????????????????
        webSettings.setAppCacheEnabled(true);//??????????????????
        webSettings.setDomStorageEnabled(true);//????????????DOM??????
        webSettings.setLoadsImagesAutomatically(true); // ????????????
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
//        webView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
//        webSettings.setMixedContentMode(WebSettings.);


        //???????????????????????????????????????????????????
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

                        //????????????????????????????????????videocache?????????
                        //1.1??????okHttpClient
                        OkHttpClient okHttpClient = new OkHttpClient();

                        //1.2??????Request??????
                        Request okRequest = new Request.Builder().url(proxyUrl).build();

                        //2.???Request???????????????call??????
                        Call call = okHttpClient.newCall(okRequest);

                        //3.??????????????????
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
                        Toast.makeText(getActivity(), "??????????????????app", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(getActivity(), "?????????????????????????????????", Toast.LENGTH_SHORT).show();
                }
//                if (url.startsWith("http")) {
//
////                    downloadBySystem(url, contentDisposition, mimetype);
////                    // ??????
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
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {//?????????????????????
//            NotificationChannel channel = new NotificationChannel(channelId,
//                    "listen",
//                    NotificationManager.IMPORTANCE_DEFAULT);
//            channel.enableLights(false); //???????????????icon???????????????
//            channel.setLightColor(Color.RED); //???????????????
//            channel.setSound(null, null);//???????????????????????????
//            channel.setShowBadge(false); //??????????????????????????????????????????????????????
//            notificationManager.createNotificationChannel(channel);
//        }
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(getActivity(), channelId)
//                .setSmallIcon(R.mipmap.ic_launcher)//????????????????????????????????????logo
//                .setCategory(CATEGORY_MESSAGE)
//                .setDefaults(DEFAULT_ALL)
//                .setOngoing(true);
//        //????????????????????????activity
//        Intent intent = new Intent(getActivity(), TestActivity.class);
//        PendingIntent pendingIntent = PendingIntent.getActivity(getActivity(), 0, intent,
//                PendingIntent.FLAG_UPDATE_CURRENT);
//        builder.setAutoCancel(false);//??????????????????
//        builder.setSound(null);//???????????????????????????
//        builder.setPriority(PRIORITY_MAX);//?????????????????????
//        builder.setVibrate(null);//????????????
//        builder.setContentIntent(pendingIntent);//??????????????????activity?????????
//        builder.setOnlyAlertOnce(false);
//        RemoteViews remoteViews = initNotifyView();
//        builder.setContent(remoteViews);//????????????view??????
//        builder.setCustomBigContentView(remoteViews);//????????????view??????
//        Notification notification = builder.build();
//        notification.flags |= FLAG_ONGOING_EVENT;
//        notification.flags |= Notification.FLAG_NO_CLEAR;//?????????????????? ???????????????
//        notification.sound = null;//???????????????????????????
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
//        remoteView.setTextViewText(R.id.widget_title, "????????????");
//        remoteView.setTextViewText(R.id.widget_artist, "???????????????");
//
//        Intent prv = new Intent(getActivity(), XMPlayerReceiver.class);//???????????????
//        prv.setAction(PLAY_PRE);
//        PendingIntent intent_prev = PendingIntent.getBroadcast(getActivity(), 1, prv,
//                PendingIntent.FLAG_UPDATE_CURRENT);
//        remoteView.setOnClickPendingIntent(R.id.widget_prev, intent_prev);
//
//
//        Intent next = new Intent(getActivity(), XMPlayerReceiver.class);//???????????????
//        next.setAction(PLAY_NEXT);
//        PendingIntent intent_next = PendingIntent.getBroadcast(getActivity(), 2, next,
//                PendingIntent.FLAG_UPDATE_CURRENT);
//        remoteView.setOnClickPendingIntent(R.id.widget_next, intent_next);
//
//
//        Intent startpause = new Intent(getActivity(), XMPlayerReceiver.class);//??????
//        startpause.setAction(PLAY_PAUSE);
//        PendingIntent intent_pause = PendingIntent.getBroadcast(getActivity(), 3, startpause,
//                PendingIntent.FLAG_UPDATE_CURRENT);
//        remoteView.setOnClickPendingIntent(R.id.widget_play, intent_pause);
//
//        Intent startplay = new Intent(getActivity(), XMPlayerReceiver.class);//??????
//        startplay.setAction(PLAY_PLAY);
//        PendingIntent intent_play = PendingIntent.getBroadcast(getActivity(), 4, startplay,
//                PendingIntent.FLAG_UPDATE_CURRENT);
//        remoteView.setOnClickPendingIntent(R.id.widget_play, intent_play);
//        return remoteView;
//    }
//
    public void clickPreviousSong(){
        if (webView == null){
            Toast.makeText(getActivity(), "????????????????????????", Toast.LENGTH_SHORT).show();
        }else {
            webView.loadUrl("javascript:document.getElementsByClassName(\"prevButton playButtons\")[0].click();");
        }
    }
    public void clickOtherInfoButton(){
        if (webView == null){
            Toast.makeText(getActivity(), "????????????????????????", Toast.LENGTH_SHORT).show();
        }else {
            webView.loadUrl("javascript:document.getElementsByClassName(\"detailsButton otherButtons\")[0].click();");
        }
    }
    public void clickPlaySong(){
        if (webView == null){
            Toast.makeText(getActivity(), "????????????????????????", Toast.LENGTH_SHORT).show();
        }else {
            webView.loadUrl("javascript:document.getElementsByClassName(\"playButton playButtons\")[0].click();");
        }
    }
    public void clickNextSong(){
        if (webView == null){
            Toast.makeText(getActivity(), "????????????????????????", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(getActivity(), "????????????????????????", Toast.LENGTH_SHORT).show();
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
        // ??????????????????
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        // ????????????????????????????????????????????????????????????????????????????????????
        request.allowScanningByMediaScanner();
        // ?????????????????????????????????????????????????????????????????????
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        // ??????????????????????????????????????????????????????????????????
//        request.setTitle("This is title");
        // ????????????????????????
//        request.setDescription("This is description");
        // ??????????????????????????????
        request.setAllowedOverMetered(false);
        // ??????????????????????????????????????????
        request.setVisibleInDownloadsUi(false);
        // ?????????????????????
        request.setAllowedOverRoaming(true);
        // ???????????????????????????
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);
        // ?????????????????????????????????????????????
        String fileName  = URLUtil.guessFileName(url, contentDisposition, mimeType);
//        log.debug("fileName:{}", fileName);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName);
//        ????????????????????????????????????????????????
//        request.setDestinationUri()
//        request.setDestinationInExternalFilesDir()
        final DownloadManager downloadManager = (DownloadManager) getActivity().getSystemService(DOWNLOAD_SERVICE);
        // ????????????????????????
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
