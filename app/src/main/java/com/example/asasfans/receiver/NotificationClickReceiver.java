package com.example.asasfans.receiver;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.asasfans.TestActivity;
import com.example.asasfans.util.SystemUtils;
import com.yy.floatserver.utils.SystemHelper;


/**
 * 通知点击广播接收器  跳转到栈顶的Activity ,而不是new 一个新的Activity
 *
 * @author llw
 */
public class NotificationClickReceiver extends BroadcastReceiver {

    public static final String TAG = "NotificationClickReceiver";
    public static final int RequestCode = 10;

    @Override
    public void onReceive(Context context, Intent intent) {

        SystemUtils.setTopApp(TestActivity.contextTestActivity);
        Log.i(TAG, "NotificationClickReceiver: ");
//        BLog.d(TAG,"通知栏点击");
//
//        //获取栈顶的Activity
//        Activity currentActivity = ActivityManager.getCurrentActivity();
//        intent = new Intent(Intent.ACTION_MAIN);
//        intent.addCategory(Intent.CATEGORY_LAUNCHER);
//        intent.setClass(context, currentActivity.getClass());
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
//        context.startActivity(intent);
    }
}
