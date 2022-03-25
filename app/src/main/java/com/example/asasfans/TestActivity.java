package com.example.asasfans;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.example.asasfans.data.GiteeVersionBean;
import com.example.asasfans.data.SingleVideoBean;
import com.example.asasfans.data.TabData;
import com.example.asasfans.ui.main.adapter.BottomPagerAdapter;
import com.example.asasfans.ui.main.fragment.ImageFanArtFragment;
import com.example.asasfans.ui.main.fragment.MainFragment;
import com.example.asasfans.ui.main.adapter.ToolsAdapter;
import com.example.asasfans.ui.main.fragment.ToolsFragment;
import com.example.asasfans.ui.main.fragment.WebFragment;
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

import java.io.File;
import java.util.ArrayList;
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
 */

public class TestActivity extends AppCompatActivity {
    /** 上次点击返回键的时间 */
    private long lastBackPressed;
    /** 两次点击的间隔时间 */
    private static final int QUIT_INTERVAL = 3000;

    private TabLayout tabs;
    private ViewPager viewPager;
    public List<TabData> mFragmentList = new ArrayList<>();

    private BottomPagerAdapter bottomPagerAdapter;
    private Object mCurrentFragment;

    private SharedPreferences userInfo;
    private SharedPreferences.Editor editor;//获取Editor
    private Map<String, ?> tmp;
    private DialogPlus dialog;
    private View dialogView;

    /*
    权限相关
     */
    public String[] permissions = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.INTERNET,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_NETWORK_STATE
    };
    List<String> mPermissionList = new ArrayList<>();
    private static final int PERMISSION_REQUEST = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initImageLoader();
        checkPermission();
        setContentView(R.layout.activity_bottom_main);

        //恢复时会第二次添加底部导航栏
        mFragmentList.clear();
        initTab();


        bottomPagerAdapter = new BottomPagerAdapter(this, getSupportFragmentManager(), mFragmentList);
        mCurrentFragment = bottomPagerAdapter.getCurrentFragment();
        viewPager = findViewById(R.id.view_pager_main);
        viewPager.setAdapter(bottomPagerAdapter);
        viewPager.setOffscreenPageLimit(4);
        tabs = findViewById(R.id.tabs_bottom);
        tabs.setupWithViewPager(viewPager);

        //设置分割线
//        LinearLayout linearLayout = (LinearLayout) tabs.getChildAt(0);
//        linearLayout.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
//        linearLayout.setDividerDrawable(ContextCompat.getDrawable(this,
//                R.drawable.divider)); //设置分割线的样式
//        linearLayout.setDividerPadding(20); //设置分割线间隔

        Intent intent =getIntent();
        Bundle bundle =intent.getExtras();
        Gson gson = new Gson();
        if (bundle.getString("latestVersion").equals("")){
            Toast.makeText(TestActivity.this, "网络错误，版本号获取失败", Toast.LENGTH_SHORT).show();
        }else {
            if (bundle.getString("latestVersion").startsWith("{\"id\":")){
                GiteeVersionBean giteeVersionBean = gson.fromJson(bundle.getString("latestVersion"), GiteeVersionBean.class);
                String versionName = giteeVersionBean.getTag_name();

                String[] tmp3 = versionName.split("v");
                String[] versionCodeString = tmp3[1].split("\\.");
                int versionCode = Integer.valueOf(versionCodeString[0]) * 100 + Integer.valueOf(versionCodeString[1]) * 10 + Integer.valueOf(versionCodeString[2]) * 1;
                if (versionCode > getVersionCode(TestActivity.this)) {
                    initDialog(TestActivity.this);
                    TextView title = dialogView.findViewById(R.id.dialog_title);
                    TextView content = dialogView.findViewById(R.id.dialog_content);
                    TextView cancel = dialogView.findViewById(R.id.cancel);
                    TextView confirm = dialogView.findViewById(R.id.confirm);
                    title.setText("新版本提醒");
                    content.setText(giteeVersionBean.getBody());
                    confirm.setText("去下载");
                    confirm.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(giteeVersionBean.getAssets().get(0).getBrowser_download_url()));
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
    }

    private void initDialog(Context context){
        dialog = DialogPlus.newDialog(context)
                .setContentHolder(new ViewHolder(R.layout.my_dialog))
                .setContentHeight(ViewGroup.LayoutParams.WRAP_CONTENT)
                .setContentWidth(ViewGroup.LayoutParams.WRAP_CONTENT)
                .setCancelable(true)
                .setGravity(Gravity.CENTER)
                .create();
        dialogView = dialog.getHolderView();
    }

    private void initImageLoader(){
        Log.i("APATH", getApplicationContext().getFilesDir().getAbsolutePath());
        Log.i("BPATH", getExternalCacheDir().getPath());
        String dirname = getExternalCacheDir().getPath() + "/pic";

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
                .threadPoolSize(6) //线程池内加载的数量
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .diskCacheFileNameGenerator(new Md5FileNameGenerator()) //将保存的时候的URI名称用MD5 加密
                .memoryCache(new UsingFreqLimitedMemoryCache(2 * 1024 * 1024)) // You can pass your own memory cache implementation/你可以通过自己的内存缓存实现
                .memoryCacheSize(20 * 1024 * 1024) // 内存缓存的最大值
                .diskCacheSize(500 * 1024 * 1024)  // 50 Mb sd卡(本地)缓存的最大值
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .defaultDisplayImageOptions(options)// 由原先的discCache -> diskCache
                .diskCache(new UnlimitedDiskCache(new File(dirname)))//自定义缓存路径
                .imageDownloader(new BaseImageDownloader(this, 5 * 1000, 30 * 1000)) // connectTimeout (5 s), readTimeout (30 s)超时时间
//                .writeDebugLogs() // Remove for release app
                .build();
        ImageLoader.getInstance().init(config);//全局初始化此配置
    }

    /**
     * @description 根据存储的CheckBox状态初始化底部栏，与ToolsAdapter耦合性很高，修改时要注意
     * @param
     * @return
     * @author akari
     * @time 2022/3/5 15:02
     */
    private void initTab() {
        //这里初始化TabLayout
//        Log.i("initTab", String.valueOf((getSharedPreferences("ToolsData", MODE_PRIVATE)).contains(ToolsAdapter.iconUrl.get(0))));
        //判断是否为第一次加载
        if (!((getSharedPreferences("ToolsData", MODE_PRIVATE)).contains(ToolsAdapter.iconUrl.get(0)))) {
            userInfo = getSharedPreferences("ToolsData", MODE_PRIVATE);
            editor = userInfo.edit();
            for (int i = 0; i < ToolsAdapter.iconUrl.size(); i++) {
                editor.putBoolean(ToolsAdapter.iconUrl.get(i), false);
                editor.commit();
            }
        }
        mFragmentList.add(new TabData("视频", MainFragment.newInstance()));
        if (getSharedPreferences("ToolsData", MODE_PRIVATE) != null){
            userInfo = getSharedPreferences("ToolsData", MODE_PRIVATE);
            tmp = userInfo.getAll();
            if (tmp.size() < ToolsAdapter.iconUrl.size()){
                editor = userInfo.edit();
                for (int i = tmp.size(); i < ToolsAdapter.iconUrl.size(); i++) {
                    editor.putBoolean(ToolsAdapter.iconUrl.get(i), false);
                    editor.commit();
                }
            }
            Log.i("initTab", tmp.toString());
            for (int i = 0 ; i < tmp.size() ; i++){
                if (userInfo.getBoolean(ToolsAdapter.iconUrl.get(i), false)) {
                    switch (i){
                        case 0:
                            mFragmentList.add(new TabData("二创图片", ImageFanArtFragment.newInstance()));
                            break;
                        default:
                            mFragmentList.add(new TabData(ToolsAdapter.name.get(i), WebFragment.newInstance(ToolsAdapter.iconUrl.get(i))));
                            break;
                    }
                }
            }
        }
        mFragmentList.add(new TabData("工具", ToolsFragment.newInstance()));
    }

//    public static void updateTabs(List<TabData> FragmentList){
//        mFragmentList.addAll(1, FragmentList);
//        if (viewPager.getAdapter() instanceof BottomPagerAdapter)
//            ((BottomPagerAdapter)viewPager.getAdapter()).updateFragmentList(mFragmentList);
//    }

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
        mCurrentFragment = bottomPagerAdapter.getCurrentFragment();
        Log.i("instanceof", String.valueOf(mCurrentFragment instanceof WebFragment));
        Log.i("keyCode", String.valueOf(keyCode));
        //解决音量键的监听被webview劫持无法使用的问题
        if (!(keyCode==KeyEvent.KEYCODE_BACK)){
            return super.onKeyDown(keyCode, event);
        }
        if(mCurrentFragment instanceof WebFragment){
            ((WebFragment)mCurrentFragment).onKeyDown(keyCode, event);
            return true;
        }else if (keyCode==KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            long backPressed = System.currentTimeMillis();
            if (backPressed - lastBackPressed > QUIT_INTERVAL) {
                lastBackPressed = backPressed;
                Toast.makeText(this,"再按一次退出", Toast.LENGTH_LONG).show();
            } else {
                finish();
                System.exit(0);
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
    }
}

