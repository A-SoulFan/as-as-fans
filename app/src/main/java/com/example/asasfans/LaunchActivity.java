package com.example.asasfans;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author akarinini
 * @description 启动页面，在启动页面里先尝试获取
 *              http://124.223.8.236:5200/AsoulRT-top30，
 *              http://124.223.8.236:5200/AsoulPudateVedio?page=1，
 *              http://124.223.8.236:5200/AsoulMostViewVedio?page=1，
 *              成功后才能进入app，实际上并没有必要这么设计，但是也无关紧要
 */

public class LaunchActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.i("APATH", getApplicationContext().getFilesDir().getAbsolutePath());
        Log.i("BPATH", String.valueOf(Environment.getExternalStorageDirectory()));
        String dirname = String.valueOf(Environment.getExternalStorageDirectory()) + "/com.example.asasfans/tmp/pic";
        File d = new File(dirname);
        d.mkdirs();
        // 现在创建目录

        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheInMemory(false) //设置下载的图片是否缓存在内存中
//                .cacheOnDisc(true)//设置下载的图片是否缓存在SD卡中
//                .showImageOnLoading(R.mipmap.load_failure)
                .showImageOnFail(R.mipmap.load_failure)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .considerExifParams(true)//是否考虑JPEG图像EXIF参数（旋转，翻转）
                .resetViewBeforeLoading(true)// 设置图片在下载前是否重置，复位
                .displayer(new RoundedBitmapDisplayer(40))
                .displayer(new FadeInBitmapDisplayer(1000))
                .build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .memoryCacheExtraOptions(480, 800) // max width, max height，即保存的每个缓存文件的最大长宽
                .threadPoolSize(3) //线程池内加载的数量
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

        setContentView(R.layout.activity_lanch);
        new Thread(networkTask).start();
    }

    final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            String val = data.getString("top30");
            Log.i("LaunchActivity", "请求结果为-->" + val);
            //跳转至 MainActivity
            Intent intent = new Intent(LaunchActivity.this, TestActivity.class);
            intent.putExtras(data);
            startActivity(intent);
            //结束当前的 Activity
            LaunchActivity.this.finish();
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
            msg.setData(data);
            handler.sendMessage(msg);
        }
    };

}
