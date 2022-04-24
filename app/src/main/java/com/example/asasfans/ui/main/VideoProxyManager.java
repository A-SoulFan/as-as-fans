package com.example.asasfans.ui.main;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.danikula.videocache.HttpProxyCacheServer;
import com.danikula.videocache.file.FileNameGenerator;
import com.example.asasfans.TestActivity;
import com.example.asasfans.service.MusicService;

import java.util.List;

/**
 * @author akarinini
 * @description:
 * @date :2022/4/21 18:58
 */
public class VideoProxyManager {

    private HttpProxyCacheServer httpProxyCacheServer;
    private static final long DEFAULT_MAX_SIZE = 12000L * 1024 * 1024; //最大缓存容量
    public static int DEFAULT_MAX_FILE_COUNT = 5000; //最大缓存数量

    public static boolean isUseCache = true; // 全局是否使用缓存

    private VideoProxyManager() {
        init(TestActivity.contextTestActivity);
    }


    private static class VideoProxyManagerHolder {
        private static VideoProxyManager videoProxyManager = new VideoProxyManager();
    }

    public static VideoProxyManager getInstance() {
        return VideoProxyManagerHolder.videoProxyManager;
    }

    public void init(Context context) {
        httpProxyCacheServer = new HttpProxyCacheServer.Builder(context).maxCacheSize(DEFAULT_MAX_SIZE)
                .maxCacheFilesCount(DEFAULT_MAX_FILE_COUNT)
//                .fileNameGenerator(new CacheFileNameGenerator())
                .build();
    }

    /**
     * 传给播放器的url替换成代理的url
     **/

    public String getProxyUrl(String url) {
        if (TextUtils.isEmpty(url) || !isUseCache) {
            return url;
        }
        return httpProxyCacheServer.getProxyUrl(url);
    }

    public void isAlive(){
        httpProxyCacheServer.isCached("");
    }


    public void shutdown() {
        httpProxyCacheServer.shutdown();
    }

    public class CacheFileNameGenerator implements FileNameGenerator {

        private static final String TAG = "CacheFileNameGenerator";

        /**
         * @param url
         * @return
         */
        @Override
        public String generate(String url) {
            Uri uri = Uri.parse(url);
            List<String> pathSegList = uri.getPathSegments();
            String path = null;
            if (pathSegList != null && pathSegList.size() > 0) {
                path = pathSegList.get(pathSegList.size() - 1);
            } else {
                path = url;
            }
            Log.d(TAG, "generate return " + path);
            return path;
        }
    }
}