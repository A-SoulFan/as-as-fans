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
    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context

        fun getStatusBarHeight(): Int {
            val resourceId = context.resources.getIdentifier("status_bar_height", "dimen", "android")
            return if (resourceId > 0) {
                context.resources.getDimensionPixelSize(resourceId)
            } else 0
        }
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }
}