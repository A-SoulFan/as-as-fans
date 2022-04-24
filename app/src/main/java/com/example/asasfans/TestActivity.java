package com.example.asasfans;

import static com.example.asasfans.service.MusicService.CLOSE;
import static com.example.asasfans.service.MusicService.NEXT;
import static com.example.asasfans.service.MusicService.PAUSE;
import static com.example.asasfans.service.MusicService.PLAY;
import static com.example.asasfans.service.MusicService.PREV;
import static com.example.asasfans.service.MusicService.PROGRESS;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.lifecycle.Observer;
import androidx.viewpager.widget.ViewPager;

import com.chaychan.viewlib.bottombarlayout.BottomBarItem;
import com.chaychan.viewlib.bottombarlayout.BottomBarLayout;
import com.example.asasfans.data.GithubVersionBean;
import com.example.asasfans.data.TabData;
import com.example.asasfans.service.LiveDataBus;
import com.example.asasfans.service.MusicService;
import com.example.asasfans.ui.main.adapter.BottomPagerAdapter;
import com.example.asasfans.ui.main.adapter.NewBottomPagerAdapter;
import com.example.asasfans.ui.main.fragment.NewToolsFragment;
import com.example.asasfans.ui.main.fragment.WebFragment;
import com.example.asasfans.util.ACache;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import com.tencent.smtt.export.external.TbsCoreSettings;
import com.tencent.smtt.sdk.QbSdk;
import com.yy.floatserver.FloatClient;
import com.yy.floatserver.FloatHelper;
import com.yy.floatserver.IFloatPermissionCallback;


import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author akarinini
 * @description 测试用页面，目前作为主页面
 *              2022/3/03 修改底部导航栏可以动态改变标签
 *              2022/3/7 更新了图片加载方式。之前是自己写的一个根据url加载图片的imageview，
 *                       问题在于它把全部的图片放在了内存中，浏览几百个视频就oom然后crash掉了，
 *                       现在换成了开源库imageloader，它的方式是把图片下载到存储里，
 *                       遇到相同的url直接从存储中加载。
 *                       修改了预加载页面数量，全部预加载。
 *              2022/4/10 固定了页面
 */

public class TestActivity extends AppCompatActivity {
    /** 上次点击返回键的时间 */
    private long lastBackPressed;
    /** 两次点击的间隔时间 */
    private static final int QUIT_INTERVAL = 3000;

    private TabLayout tabs;
    public static ViewPager viewPager;
    public List<TabData> mFragmentList = new ArrayList<>();

    private BottomPagerAdapter bottomPagerAdapter;
    private Object mCurrentFragment;

    private SharedPreferences userInfo;
    private SharedPreferences.Editor editor;//获取Editor
    private Map<String, ?> tmp;
    private DialogPlus dialog;
    private View dialogView;

    private BottomBarLayout bottomBarLayout;

    private RotateAnimation mRotateAnimation;
    private Handler mHandler = new Handler();

    private NewBottomPagerAdapter newBottomPagerAdapter;

    public static FloatHelper floatHelper;
    /*
    权限相关
     */
    public String[] permissions = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.INTERNET,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.SYSTEM_ALERT_WINDOW,
            Manifest.permission.REORDER_TASKS,
            Manifest.permission.BIND_NOTIFICATION_LISTENER_SERVICE,
            Manifest.permission.ACCESS_NOTIFICATION_POLICY,
            Manifest.permission.BLUETOOTH,
            Manifest.permission.WAKE_LOCK
    };
    List<String> mPermissionList = new ArrayList<>();
    private static final int PERMISSION_REQUEST = 1;
    private static final int REQUEST_DIALOG_PERMISSION = 2;

    public static Context contextTestActivity;

    private MusicService.MusicBinder musicBinder;

    private MusicService musicService;

    public static Object studioFragment;

    private Intent serviceIntent;

    /**
     * 当Service中通知栏有变化时接收到消息
     */
    private LiveDataBus.BusMutableLiveData<String> activityLiveData;
    /**
     * 当在Activity中做出播放状态的改变时，通知做出相应改变
     */
    private LiveDataBus.BusMutableLiveData<String> notificationLiveData;

    //试图解决锁屏后wlan休眠的问题，无效，寄了
    private PowerManager pm;

    private PowerManager.WakeLock wl;

    // 定义WifiManager对象
    private WifiManager wifiManager;

    // 定义一个WifiLock
    private WifiManager.WifiLock wifiLock;
//    private static HttpProxyCacheServer proxy;

    @Override
    protected void onResume() {
        super.onResume();
        floatHelper.dismiss();
    }

    @Override
    protected void onPause() {
        super.onPause();
        ACache aCache = ACache.get(this);
        String tmpACache =  aCache.getAsString("isShowFloatingBall"); // yes or no
        if (tmpACache == null){
            floatHelper.show();
//            Toast.makeText(TestActivity.this, "悬浮球默认打开哦，可以在设置关闭", Toast.LENGTH_SHORT).show();
        }else if (tmpACache.equals("yes")){
            floatHelper.show();
        }else if (tmpACache.equals("no")){

        }
    }

    @SuppressLint("InvalidWakeLockTag")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_activity_bottom_main);
        contextTestActivity = TestActivity.this;
        initImageLoader();
        checkPermission();

        pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wl = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "POWER_MANAGER_TAG");
        wl.acquire();
        wifiManager = (WifiManager) contextTestActivity.getSystemService(Context.WIFI_SERVICE);
        wifiLock = wifiManager.createWifiLock(WifiManager.WIFI_MODE_FULL_HIGH_PERF, "WifiLocKManager");
        wifiLock.acquire();
        initFloatingBall(TestActivity.this);

//        setContentView(R.layout.activity_bottom_main);
//
//        //恢复时会第二次添加底部导航栏
//        mFragmentList.clear();
//        initTab();
//
//
//        bottomPagerAdapter = new BottomPagerAdapter(this, getSupportFragmentManager(), mFragmentList);
//        mCurrentFragment = bottomPagerAdapter.getCurrentFragment();
//        viewPager = findViewById(R.id.view_pager_main);
//        viewPager.setAdapter(bottomPagerAdapter);
//        viewPager.setOffscreenPageLimit(4);
//        tabs = findViewById(R.id.tabs_bottom);
//        tabs.setupWithViewPager(viewPager);
        bottomBarLayout = findViewById(R.id.bbl);
        newBottomPagerAdapter = new NewBottomPagerAdapter(getSupportFragmentManager());
        viewPager = findViewById(R.id.vp_content);
        viewPager.setAdapter(newBottomPagerAdapter);
        viewPager.setOffscreenPageLimit(4);
        bottomBarLayout.setViewPager(viewPager);



        bottomBarLayout.setOnItemSelectedListener(new BottomBarLayout.OnItemSelectedListener() {
            @Override
            public void onItemSelected(BottomBarItem bottomBarItem, int position) {
                switch (position){
                    case 0:
                        BottomBarItem bottomItem = bottomBarLayout.getBottomItem(0);
                        bottomItem.setIconSelectedResourceId(R.drawable.ic_new_video_pressed);//更换为原来的图标

                        cancelTabLoading(bottomItem);//停止旋转动画
                        if (bottomBarLayout.getCurrentItem() == position){
//                            bottomBarItem.setIconSelectedResourceId(R.drawable.ic_loading);//更换成加载图标
//                            bottomBarItem.setStatus(true);
//                            //播放旋转动画
//                            if (mRotateAnimation == null) {
//                                mRotateAnimation = new RotateAnimation(0, 360,
//                                        Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
//                                        0.5f);
//                                mRotateAnimation.setDuration(800);
//                                mRotateAnimation.setRepeatCount(-1);
//                            }
//                            ImageView bottomImageView = bottomBarItem.getImageView();
//                            bottomImageView.setAnimation(mRotateAnimation);
//                            bottomImageView.startAnimation(mRotateAnimation);//播放旋转动画
//
//                            //模拟数据刷新完毕
//                            mHandler.postDelayed(new Runnable() {
//                                @Override
//                                public void run() {
//                                    bottomBarItem.setIconSelectedResourceId(R.drawable.ic_video_dark);//更换成首页原来图标
//                                    bottomBarItem.setStatus(true);//刷新图标
//                                    cancelTabLoading(bottomBarItem);
//                                }
//                            },500);
                            return;
                        }
                    default:

                }
            }
        });

        //设置分割线
//        LinearLayout linearLayout = (LinearLayout) tabs.getChildAt(0);
//        linearLayout.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
//        linearLayout.setDividerDrawable(ContextCompat.getDrawable(this,
//                R.drawable.divider)); //设置分割线的样式
//        linearLayout.setDividerPadding(20); //设置分割线间隔

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        Gson gson = new Gson();
        if (bundle == null){

        } else if (bundle.getString("latestVersion").equals("")){
            Toast.makeText(TestActivity.this, "网络错误，版本号获取失败", Toast.LENGTH_SHORT).show();
        }else {
            if (bundle.getString("latestVersion").startsWith("{\"url\"")){
                GithubVersionBean githubVersionBean = gson.fromJson(bundle.getString("latestVersion"), GithubVersionBean.class);
                String versionName = githubVersionBean.getTag_name();

                String[] tmp3 = versionName.split("v");
                String[] versionCodeString = tmp3[1].split("\\.");
                int versionCode = Integer.valueOf(versionCodeString[0]) * 100 + Integer.valueOf(versionCodeString[1]) * 10 + Integer.valueOf(versionCodeString[2]) * 1;
                if (versionCode > getVersionCode(TestActivity.this)) {
                    initDialog(TestActivity.this);
                    TextView title = dialogView.findViewById(R.id.title);
                    TextView content = dialogView.findViewById(R.id.upgrade_content);
                    TextView cancel = dialogView.findViewById(R.id.close);
                    TextView confirm = dialogView.findViewById(R.id.upgrade);

                    content.setText(githubVersionBean.getBody());

                    confirm.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://app.asf.ink/"));
                            startActivity(intent);
                            dialog.dismiss();
                        }
                    });
                    cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                }
            }
        }
        FragmentStatePagerAdapter f = (FragmentStatePagerAdapter) viewPager.getAdapter();
        if (f != null) {
            //instantiateItem(pager, position) 方法会返回在position位置的fragment的引用。
            //如果该fragment 已经实例化了,再次调用instantiateItem(pager,position)的时候，该方法并不会调用
            //getItem()来再次实例化fragment，而是直接返回引用。
            studioFragment = f.instantiateItem(viewPager, 2);
        }
        //绑定服务
        serviceIntent = new Intent(contextTestActivity, MusicService.class);
//        bindService(serviceIntent, connection, BIND_AUTO_CREATE);
        startService(serviceIntent);
//        startForegroundService(serviceIntent);
        //通知栏的观察者
        notificationObserver();
        //控制通知栏
        notificationLiveData = LiveDataBus.getInstance().with("notification_control", String.class);

    }

//    public static void init(Context aContext) {
//        //设置点击栏目知想打开的页面
//        RxConstants.CLASSNAME = "MainActivty";
//
//        RxDownloadManager manager = RxDownloadManager.getInstance();
//        manager.init(aContext, new DownloadAdapter());
//        manager.setContext(aContext);
//        manager.setListener(new DLDownloadListener(aContext));
//        DLNormalCallback normalCallback = new DLNormalCallback();
//        if (manager.getClient() != null) {
//            manager.getClient().setCallback(normalCallback);
//        }
//        RxDownLoadCenter.getInstance(aContext).loadTask();
//    }
    /**
     * 通知栏动作观察者
     */
    private void notificationObserver() {
        activityLiveData = LiveDataBus.getInstance().with("activity_control", String.class);
        activityLiveData.observe(TestActivity.this, true, new Observer<String>() {
            @Override
            public void onChanged(String state) {

                switch (state) {
                    case PLAY:
//                        btnPlay.setIcon(getDrawable(R.mipmap.icon_pause));
//                        btnPlay.setIconTint(getColorStateList(R.color.gold_color));
//                        BLog.d(TAG,state);
//                        changeUI(musicService.getPlayPosition());
                        break;
                    case PAUSE:
                    case CLOSE:
//                        btnPlay.setIcon(getDrawable(R.mipmap.icon_play));
//                        btnPlay.setIconTint(getColorStateList(R.color.white));
//                        changeUI(musicService.getPlayPosition());
                        break;
                    case PREV:
//                        BLog.d(TAG, "上一曲");
//                        changeUI(musicService.getPlayPosition());
                        break;
                    case NEXT:
//                        BLog.d(TAG, "下一曲");
//                        changeUI(musicService.getPlayPosition());
                        break;
                    case PROGRESS:
                        //播放进度发生改变时,只改变进度，不改变其他
//                        musicProgress.setProgress(musicService.mediaPlayer.getCurrentPosition(), musicService.mediaPlayer.getDuration());
                        break;
                    default:
                        break;
                }

            }
        });
    }

//    public static HttpProxyCacheServer getProxy() {
//
////        App app = (App) context.getApplicationContext();
//        return proxy == null ? (proxy = newProxy()) : proxy;
//    }
//    private static HttpProxyCacheServer newProxy() {
//        return new HttpProxyCacheServer.Builder(contextTestActivity)
//                .maxCacheSize(6 * 1024 * 1024)       // 1 Gb for cache
//                .build();
//    }
    private ServiceConnection connection = new ServiceConnection() {

        /**
         * 连接服务
         * @param name
         * @param service
         */
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            musicBinder = (MusicService.MusicBinder) service;
            musicService = musicBinder.getService();
            Log.i("onServiceConnected", "Service与Activity已连接");

        }

        //断开服务
        @Override
        public void onServiceDisconnected(ComponentName name) {
            musicBinder = null;
        }
    };


    private void cancelTabLoading(BottomBarItem bottomItem) {
        Animation animation = bottomItem.getImageView().getAnimation();
        if (animation != null){
            animation.cancel();
        }
    }

    private void initDialog(Context context){
        dialog = DialogPlus.newDialog(context)
                .setContentHolder(new ViewHolder(R.layout.dialog_upgrade))
                .setContentHeight(ViewGroup.LayoutParams.WRAP_CONTENT)
                .setContentWidth(ViewGroup.LayoutParams.MATCH_PARENT)
                .setCancelable(true)
                .setGravity(Gravity.CENTER)
                .setContentBackgroundResource(R.color.transparent)
                .create();
        dialogView = dialog.getHolderView();
    }

    private void initX5(){
//        QbSdk.setDownloadWithoutWifi(true);
        QbSdk.initX5Environment(contextTestActivity, new QbSdk.PreInitCallback() {
            @Override
            public void onCoreInitFinished() {
                // 内核初始化完成，可能为系统内核，也可能为系统内核
            }

            /**
             * 预初始化结束
             * 由于X5内核体积较大，需要依赖网络动态下发，所以当内核不存在的时候，默认会回调false，此时将会使用系统内核代替
             * @param isX5 是否使用X5内核
             */
            @Override
            public void onViewInitFinished(boolean isX5) {

            }
        });
        HashMap map = new HashMap();
        map.put(TbsCoreSettings.TBS_SETTINGS_USE_SPEEDY_CLASSLOADER, true);
        map.put(TbsCoreSettings.TBS_SETTINGS_USE_DEXLOADER_SERVICE, true);
        QbSdk.initTbsSettings(map);
    }


    private void initImageLoader(){
//        Log.i("APATH", getApplicationContext().getFilesDir().getAbsolutePath());
//        Log.i("BPATH", getExternalCacheDir().getPath());
//        String systemPath;
        String dirname;
        try {
//            systemPath = getExternalCacheDir().getPath();
            dirname = getExternalCacheDir().getPath() + "/pic";
        }catch (Exception e){
            e.printStackTrace();
            dirname = "/storage/emulated/0/Android/data/com.example.asasfans/cache/pic";
        }

        // 现在创建目录

        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheInMemory(false) //设置下载的图片是否缓存在内存中
                .cacheOnDisk(true)//设置下载的图片是否缓存在SD卡中
//                .showImageOnLoading(R.mipmap.loading_black)
                .showImageOnFail(R.mipmap.load_failure)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .considerExifParams(true)//是否考虑JPEG图像EXIF参数（旋转，翻转）
                .resetViewBeforeLoading(true)// 设置图片在下载前是否重置，复位
                .displayer(new RoundedBitmapDisplayer(80))
                .displayer(new FadeInBitmapDisplayer(1000))
                .build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .memoryCacheExtraOptions(480, 800) // max width, max height，即保存的每个缓存文件的最大长宽
                .threadPoolSize(10) //线程池内加载的数量
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .diskCacheFileNameGenerator(new Md5FileNameGenerator()) //将保存的时候的URI名称用MD5 加密
                .memoryCache(new UsingFreqLimitedMemoryCache(2 * 1024 * 1024)) // You can pass your own memory cache implementation/你可以通过自己的内存缓存实现
                .memoryCacheSize(20 * 1024 * 1024) // 内存缓存的最大值
                .diskCacheSize(500 * 1024 * 1024)  // 50 Mb sd卡(本地)缓存的最大值
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .defaultDisplayImageOptions(options)// 由原先的discCache -> diskCache
                .diskCache(new UnlimitedDiskCache(new File(dirname)))//自定义缓存路径
                .imageDownloader(new BaseImageDownloader(this, 5 * 1000, 5 * 1000)) // connectTimeout (5 s), readTimeout (30 s)超时时间
//                .writeDebugLogs() // Remove for release app
                .build();
        ImageLoader.getInstance().init(config);//全局初始化此配置
    }

    public void initFloatingBall(Context context){
        View view =  LayoutInflater.from(contextTestActivity).inflate(R.layout.view_floating_ball, null);
        floatHelper = new FloatClient.Builder()
                .with(contextTestActivity)
                .addView(view)
                .enableDefaultPermissionDialog(false)
                .setClickTarget(TestActivity.class)
                .addPermissionCallback(new IFloatPermissionCallback() {
                    @Override
                    public void onPermissionResult(boolean b) {
                        if (!b){
                            ACache aCache = ACache.get(context);
                            String isNoLongerShowFloatingBall =  aCache.getAsString("isNoLongerShowFloatingBall"); // yes or no
                            DialogPlus dialogP = DialogPlus.newDialog(context)
                                    .setContentHolder(new ViewHolder(R.layout.my_dialog))
                                    .setContentHeight(ViewGroup.LayoutParams.WRAP_CONTENT)
                                    .setContentWidth(ViewGroup.LayoutParams.MATCH_PARENT)
                                    .setGravity(Gravity.CENTER)
                                    .setContentBackgroundResource(R.color.transparent)
                                    .create();;
                            View dialogViewP = dialogP.getHolderView();
                            TextView title = dialogViewP.findViewById(R.id.dialog_title);
                            TextView content = dialogViewP.findViewById(R.id.dialog_content);
                            TextView confirm = dialogViewP.findViewById(R.id.confirm);
                            TextView cancel = dialogViewP.findViewById(R.id.cancel);
                            title.setText("需要悬浮窗权限");
                            content.setText("通过悬浮球可以在其他应用快速回到AsAsFans，需要去开启悬浮窗权限，悬浮窗只会在其他界面显示出来哦");
                            confirm.setText("去开启");
                            cancel.setText("不再提醒");
                            cancel.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    aCache.put("isNoLongerShowFloatingBall", "yes");
                                    dialogP.dismiss();
                                }
                            });
                            confirm.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    int sdkInt = Build.VERSION.SDK_INT;
                                    if (sdkInt >= Build.VERSION_CODES.O) {//8.0以上
                                        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                                        startActivityForResult(intent, REQUEST_DIALOG_PERMISSION);
                                    } else if (sdkInt >= Build.VERSION_CODES.M) {//6.0-8.0
                                        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                                        intent.setData(Uri.parse("package:" + getPackageName()));
                                        startActivityForResult(intent, REQUEST_DIALOG_PERMISSION);
                                    } else {//4.4-6.0一下
                                        //无需处理了
                                    }
                                    dialogP.dismiss();
                                }
                            });
                            if (isNoLongerShowFloatingBall == null){
                                dialogP.show();
                            }else if (isNoLongerShowFloatingBall.equals("yes")){
                                aCache.put("isShowFloatingBall", "no");
                            }else if (isNoLongerShowFloatingBall.equals("no")){
                                dialogP.show();
                            }
                        }
                    }
                })
                .build();
        ACache aCache = ACache.get(this);
        String tmpACache =  aCache.getAsString("isShowFloatingBall"); // yes or no
        if (tmpACache == null){
            floatHelper.show();
//            Toast.makeText(TestActivity.this, "悬浮球默认打开哦，可以在设置关闭", Toast.LENGTH_SHORT).show();
        }else if (tmpACache.equals("yes")){
            floatHelper.show();
        }else if (tmpACache.equals("no")){

        }
    }


    private void initVideoDownloader(){


    }

    public static int getVersionCode(Context mContext) {
        int versionCode = 0;
        try {
                //获取软件版本号，对应AndroidManifest.xml下android:versionCode
                versionCode = mContext.getPackageManager().
                getPackageInfo(mContext.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }


    /**
     * @description Override实现两次退出
     * @author akari
     * @time 2022/2/27 11:47
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        mCurrentFragment = newBottomPagerAdapter.getCurrentFragment();

        Log.i("instanceof", String.valueOf(mCurrentFragment instanceof WebFragment));
        Log.i("keyCode", String.valueOf(keyCode));
        //解决音量键的监听被webview劫持无法使用的问题
        if (!(keyCode==KeyEvent.KEYCODE_BACK)){
            return super.onKeyDown(keyCode, event);
        }
        if(mCurrentFragment instanceof WebFragment){
            ((WebFragment)mCurrentFragment).onKeyDown(keyCode, event);
            return true;
        }else if(mCurrentFragment instanceof NewToolsFragment){
            ((NewToolsFragment) mCurrentFragment).current().onKeyDown(keyCode, event);
            return true;
        }
        else if (keyCode==KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            long backPressed = System.currentTimeMillis();
            if (backPressed - lastBackPressed > QUIT_INTERVAL) {
                lastBackPressed = backPressed;
                Toast.makeText(this,"再按一次退出", Toast.LENGTH_LONG).show();
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void checkPermission() {
        mPermissionList.clear();
        //判断哪些权限未授予
        for (int i = 0; i < permissions.length; i++) {
            if (ContextCompat.checkSelfPermission(this, permissions[i]) != PackageManager.PERMISSION_GRANTED) {
                mPermissionList.add(permissions[i]);
            }
        }
        /**
         * 判断是否为空
         */
        if (mPermissionList.isEmpty()) {//未授予的权限为空，表示都授予了
        } else {//请求权限方法
            String[] permissions = mPermissionList.toArray(new String[mPermissionList.size()]);//将List转为数组
            ActivityCompat.requestPermissions(TestActivity.this, permissions, PERMISSION_REQUEST);
        }
    }
    /**
     * 响应授权
     * 这里不管用户是否拒绝，都进入首页，不再重复申请权限
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_REQUEST:
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                break;
        }
        initX5();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case REQUEST_DIALOG_PERMISSION:
                floatHelper.show();
                ACache aCache = ACache.get(this);
                aCache.put("isShowFloatingBall", "yes");
                aCache.put("isNoLongerShowFloatingBall", "no");
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        floatHelper.release();
        musicService.closeNotification();
        stopService(serviceIntent);
        wl.release();
        wifiLock.release();
        Log.i("TestActivity", "onDestroy: ");
    }
    //实现暂停音乐



}

