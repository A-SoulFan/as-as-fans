package com.example.asasfans

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context

/**
 * 创建全局Application，方便得到全局Context。
 *
 * @ProjectName : AsAsFans
 * @Author : Yenaly Liew
 * @Time : 2022/04/04 004 09:23
 * @Description : Description...
 */
class AsApplication : Application() {
//    private var proxy: HttpProxyCacheServer? = null
//    private fun newProxy(): HttpProxyCacheServer {
//        return HttpProxyCacheServer.Builder(this)
////                .cacheDirectory(File("/storage/emulated/0/Android/data/com.example.asasfans/cache/video-cache"))
//                .build()
//    }
    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context

        fun getStatusBarHeight(): Int {
            val resourceId = context.resources.getIdentifier("status_bar_height", "dimen", "android")
            return if (resourceId > 0) {
                context.resources.getDimensionPixelSize(resourceId)
            } else 0
        }
//        fun getProxy(context: Context): HttpProxyCacheServer {
//            val app = context.applicationContext as AsApplication
//            return if (app.proxy == null) app.newProxy().also { app.proxy = it } else app.proxy!!
//        }
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }
}