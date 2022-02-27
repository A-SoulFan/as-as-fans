package com.example.asasfans;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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
            OkHttpClient client = new OkHttpClient.Builder().readTimeout(5, TimeUnit.SECONDS).build();
            Request request = new Request.Builder().url("http://124.223.8.236:5200/AsoulRT-top30")
                    .get().build();
            Call call = client.newCall(request);
            Response response = null;
            try {
                response = call.execute();
                String tmp = response.body().string();
                data.putString("top30", tmp);

            } catch (IOException e) {
                e.printStackTrace();
            }
            // TODO
            // 在这里进行 http request.网络请求相关操作
            client = new OkHttpClient.Builder().readTimeout(5, TimeUnit.SECONDS).build();
            request = new Request.Builder().url("http://124.223.8.236:5200/AsoulPudateVedio?page=1")
                    .get().build();
            call = client.newCall(request);
            response = null;
            try {
                response = call.execute();
                String tmp = response.body().string();
                data.putString("pubdate", tmp);

            } catch (IOException e) {
                e.printStackTrace();
            }
            // TODO
            // 在这里进行 http request.网络请求相关操作
            client = new OkHttpClient.Builder().readTimeout(5, TimeUnit.SECONDS).build();
            request = new Request.Builder().url("http://124.223.8.236:5200/AsoulMostViewVedio?page=1")
                    .get().build();
            call = client.newCall(request);
            response = null;
            try {
                response = call.execute();
                String tmp = response.body().string();
                data.putString("most", tmp);

            } catch (IOException e) {
                e.printStackTrace();
            }
            msg.setData(data);
            handler.sendMessage(msg);
        }
    };

}
