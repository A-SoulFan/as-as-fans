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
 * @description ???????????????????????????????????????
 *              2022/3/03 ?????????????????????????????????????????????
 *              2022/3/7 ???????????????????????????????????????????????????????????????url???????????????imageview???
 *                       ??????????????????????????????????????????????????????????????????????????????oom??????crash?????????
 *                       ????????????????????????imageloader????????????????????????????????????????????????
 *                       ???????????????url???????????????????????????
 *                       ???????????????????????????????????????????????????
 *              2022/4/10 ???????????????
 */

public class TestActivity extends AppCompatActivity {
    /** ?????????????????????????????? */
    private long lastBackPressed;
    /** ??????????????????????????? */
    private static final int QUIT_INTERVAL = 3000;

    private TabLayout tabs;
    public static ViewPager viewPager;
    public List<TabData> mFragmentList = new ArrayList<>();

    private BottomPagerAdapter bottomPagerAdapter;
    private Object mCurrentFragment;

    private SharedPreferences userInfo;
    private SharedPreferences.Editor editor;//??????Editor
    private Map<String, ?> tmp;
    private DialogPlus dialog;
    private View dialogView;

    private BottomBarLayout bottomBarLayout;

    private RotateAnimation mRotateAnimation;
    private Handler mHandler = new Handler();

    private NewBottomPagerAdapter newBottomPagerAdapter;

    public static FloatHelper floatHelper;
    /*
    ????????????
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
     * ???Service???????????????????????????????????????
     */
    private LiveDataBus.BusMutableLiveData<String> activityLiveData;
    /**
     * ??????Activity????????????????????????????????????????????????????????????
     */
    private LiveDataBus.BusMutableLiveData<String> notificationLiveData;

    //?????????????????????wlan?????????????????????????????????
    private PowerManager pm;

    private PowerManager.WakeLock wl;

    // ??????WifiManager??????
    private WifiManager wifiManager;

    // ????????????WifiLock
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
//            Toast.makeText(TestActivity.this, "????????????????????????????????????????????????", Toast.LENGTH_SHORT).show();
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
//        //??????????????????????????????????????????
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
                        bottomItem.setIconSelectedResourceId(R.drawable.ic_new_video_pressed);//????????????????????????

                        cancelTabLoading(bottomItem);//??????????????????
                        if (bottomBarLayout.getCurrentItem() == position){
//                            bottomBarItem.setIconSelectedResourceId(R.drawable.ic_loading);//?????????????????????
//                            bottomBarItem.setStatus(true);
//                            //??????????????????
//                            if (mRotateAnimation == null) {
//                                mRotateAnimation = new RotateAnimation(0, 360,
//                                        Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
//                                        0.5f);
//                                mRotateAnimation.setDuration(800);
//                                mRotateAnimation.setRepeatCount(-1);
//                            }
//                            ImageView bottomImageView = bottomBarItem.getImageView();
//                            bottomImageView.setAnimation(mRotateAnimation);
//                            bottomImageView.startAnimation(mRotateAnimation);//??????????????????
//
//                            //????????????????????????
//                            mHandler.postDelayed(new Runnable() {
//                                @Override
//                                public void run() {
//                                    bottomBarItem.setIconSelectedResourceId(R.drawable.ic_video_dark);//???????????????????????????
//                                    bottomBarItem.setStatus(true);//????????????
//                                    cancelTabLoading(bottomBarItem);
//                                }
//                            },500);
                            return;
                        }
                    default:

                }
            }
        });

        //???????????????
//        LinearLayout linearLayout = (LinearLayout) tabs.getChildAt(0);
//        linearLayout.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
//        linearLayout.setDividerDrawable(ContextCompat.getDrawable(this,
//                R.drawable.divider)); //????????????????????????
//        linearLayout.setDividerPadding(20); //?????????????????????

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        Gson gson = new Gson();
        if (bundle == null){

        } else if (bundle.getString("latestVersion").equals("")){
            Toast.makeText(TestActivity.this, "????????????????????????????????????", Toast.LENGTH_SHORT).show();
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
            //instantiateItem(pager, position) ??????????????????position?????????fragment????????????
            //?????????fragment ??????????????????,????????????instantiateItem(pager,position)????????????????????????????????????
            //getItem()??????????????????fragment??????????????????????????????
            studioFragment = f.instantiateItem(viewPager, 2);
        }
        //????????????
        serviceIntent = new Intent(contextTestActivity, MusicService.class);
//        bindService(serviceIntent, connection, BIND_AUTO_CREATE);
        startService(serviceIntent);
//        startForegroundService(serviceIntent);
        //?????????????????????
        notificationObserver();
        //???????????????
        notificationLiveData = LiveDataBus.getInstance().with("notification_control", String.class);

    }

//    public static void init(Context aContext) {
//        //???????????????????????????????????????
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
     * ????????????????????????
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
//                        BLog.d(TAG, "?????????");
//                        changeUI(musicService.getPlayPosition());
                        break;
                    case NEXT:
//                        BLog.d(TAG, "?????????");
//                        changeUI(musicService.getPlayPosition());
                        break;
                    case PROGRESS:
                        //???????????????????????????,?????????????????????????????????
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
         * ????????????
         * @param name
         * @param service
         */
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            musicBinder = (MusicService.MusicBinder) service;
            musicService = musicBinder.getService();
            Log.i("onServiceConnected", "Service???Activity?????????");

        }

        //????????????
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
                // ????????????????????????????????????????????????????????????????????????
            }

            /**
             * ??????????????????
             * ??????X5?????????????????????????????????????????????????????????????????????????????????????????????????????????false???????????????????????????????????????
             * @param isX5 ????????????X5??????
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

        // ??????????????????

        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheInMemory(false) //?????????????????????????????????????????????
                .cacheOnDisk(true)//????????????????????????????????????SD??????
//                .showImageOnLoading(R.mipmap.loading_black)
                .showImageOnFail(R.mipmap.load_failure)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .considerExifParams(true)//????????????JPEG??????EXIF???????????????????????????
                .resetViewBeforeLoading(true)// ?????????????????????????????????????????????
                .displayer(new RoundedBitmapDisplayer(80))
                .displayer(new FadeInBitmapDisplayer(1000))
                .build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .memoryCacheExtraOptions(480, 800) // max width, max height????????????????????????????????????????????????
                .threadPoolSize(10) //???????????????????????????
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .diskCacheFileNameGenerator(new Md5FileNameGenerator()) //?????????????????????URI?????????MD5 ??????
                .memoryCache(new UsingFreqLimitedMemoryCache(2 * 1024 * 1024)) // You can pass your own memory cache implementation/??????????????????????????????????????????
                .memoryCacheSize(20 * 1024 * 1024) // ????????????????????????
                .diskCacheSize(500 * 1024 * 1024)  // 50 Mb sd???(??????)??????????????????
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .defaultDisplayImageOptions(options)// ????????????discCache -> diskCache
                .diskCache(new UnlimitedDiskCache(new File(dirname)))//?????????????????????
                .imageDownloader(new BaseImageDownloader(this, 5 * 1000, 5 * 1000)) // connectTimeout (5 s), readTimeout (30 s)????????????
//                .writeDebugLogs() // Remove for release app
                .build();
        ImageLoader.getInstance().init(config);//????????????????????????
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
                            title.setText("?????????????????????");
                            content.setText("????????????????????????????????????????????????AsAsFans?????????????????????????????????????????????????????????????????????????????????");
                            confirm.setText("?????????");
                            cancel.setText("????????????");
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
                                    if (sdkInt >= Build.VERSION_CODES.O) {//8.0??????
                                        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                                        startActivityForResult(intent, REQUEST_DIALOG_PERMISSION);
                                    } else if (sdkInt >= Build.VERSION_CODES.M) {//6.0-8.0
                                        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                                        intent.setData(Uri.parse("package:" + getPackageName()));
                                        startActivityForResult(intent, REQUEST_DIALOG_PERMISSION);
                                    } else {//4.4-6.0??????
                                        //???????????????
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
//            Toast.makeText(TestActivity.this, "????????????????????????????????????????????????", Toast.LENGTH_SHORT).show();
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
                //??????????????????????????????AndroidManifest.xml???android:versionCode
                versionCode = mContext.getPackageManager().
                getPackageInfo(mContext.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }


    /**
     * @description Override??????????????????
     * @author akari
     * @time 2022/2/27 11:47
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        mCurrentFragment = newBottomPagerAdapter.getCurrentFragment();

        Log.i("instanceof", String.valueOf(mCurrentFragment instanceof WebFragment));
        Log.i("keyCode", String.valueOf(keyCode));
        //???????????????????????????webview???????????????????????????
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
                Toast.makeText(this,"??????????????????", Toast.LENGTH_LONG).show();
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void checkPermission() {
        mPermissionList.clear();
        //???????????????????????????
        for (int i = 0; i < permissions.length; i++) {
            if (ContextCompat.checkSelfPermission(this, permissions[i]) != PackageManager.PERMISSION_GRANTED) {
                mPermissionList.add(permissions[i]);
            }
        }
        /**
         * ??????????????????
         */
        if (mPermissionList.isEmpty()) {//?????????????????????????????????????????????
        } else {//??????????????????
            String[] permissions = mPermissionList.toArray(new String[mPermissionList.size()]);//???List????????????
            ActivityCompat.requestPermissions(TestActivity.this, permissions, PERMISSION_REQUEST);
        }
    }
    /**
     * ????????????
     * ???????????????????????????????????????????????????????????????????????????
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
    //??????????????????



}

