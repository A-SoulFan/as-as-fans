package com.example.asasfans.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class XMPlayerReceiver extends BroadcastReceiver {
    public static final String PLAY_PRE = "play_pre";
    public static final String PLAY_NEXT = "play_next";
    public static final String PLAY_PAUSE = "play_pause";
    public static final String PLAY_PLAY = "play_play";
    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals(PLAY_NEXT)){//PLAY_NEXT
            Log.e("XMPlayerReceiver", "通知栏点击了下一首");

        }
        if (intent.getAction().equals(PLAY_PRE)) {;
            Log.e("XMPlayerReceiver", "通知栏点击了上一首");
        }
        if (intent.getAction().equals(PLAY_PAUSE)) {
            Log.e("XMPlayerReceiver", "通知栏点击了暂停");
        }
        if (intent.getAction().equals(PLAY_PLAY)) {
            Log.e("XMPlayerReceiver", "通知栏点击了开始");
        }
    }
}
