package com.example.asasfans;

import static com.example.asasfans.ui.main.adapter.PubdateVideoAdapter.GET_DATA_SUCCESS;
import static com.example.asasfans.ui.main.adapter.PubdateVideoAdapter.NETWORK_ERROR;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.asasfans.data.DBOpenHelper;
import com.example.asasfans.util.ACache;

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
 *              2022/3/19 启动页面现在什么也不干
 */

public class LaunchActivity extends AppCompatActivity {
    private String latestVersion = "https://api.github.com/repos/A-SoulFan/as-as-fans/releases/latest";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DBOpenHelper dbOpenHelper = new DBOpenHelper(this,"blackList.db",null,1);
        dbOpenHelper.close();
        setContentView(R.layout.activity_lanch);
        new Thread(networkTask).start();
    }


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            String val = data.getString("latestVersion");
            Log.i("latestVersion", "请求结果为-->" + val);
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
            ACache aCache = ACache.get(LaunchActivity.this);
            String tmpACache =  aCache.getAsString("latestVersion");
            if (tmpACache == null) {
                // TODO
                // 在这里进行 http request.网络请求相关操作
                OkHttpClient client = new OkHttpClient.Builder().readTimeout(5, TimeUnit.SECONDS).build();
                Request request = new Request.Builder().url(latestVersion)
                        .get().build();
                Call call = client.newCall(request);
                Response response = null;
                String tmp;
                try {
                    response = call.execute();
                    tmp = response.body().string();
                    msg.what = GET_DATA_SUCCESS;
                    data.putString("latestVersion", tmp);
                    aCache.put("latestVersion", tmp, ACache.TIME_DAY);
                } catch (IOException e) {
                    e.printStackTrace();
                    data.putString("latestVersion", "");
                    handler.sendEmptyMessage(NETWORK_ERROR);
                }
            }else {
                msg.what = GET_DATA_SUCCESS;
                data.putString("latestVersion", aCache.getAsString("latestVersion"));
                Log.i("ACache", aCache.getAsString("latestVersion"));
            }
            msg.setData(data);
            handler.sendMessage(msg);
        }
    };

}
