package com.example.asasfans.ui.main;

import static com.example.asasfans.TestActivity.getVersionCode;
import static com.example.asasfans.ui.main.adapter.PubdateVideoAdapter.GET_DATA_SUCCESS;
import static com.example.asasfans.ui.main.adapter.PubdateVideoAdapter.NETWORK_ERROR;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.asasfans.LaunchActivity;
import com.example.asasfans.R;
import com.example.asasfans.TestActivity;
import com.example.asasfans.data.GiteeVersionBean;
import com.google.gson.Gson;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ConfigActivity extends AppCompatActivity implements View.OnClickListener{
    private ConstraintLayout config_blacklist;
    private ConstraintLayout config_check_version;
    private ConstraintLayout config_contract_us;
    private LinearLayout config;
    private TextView config_check_version_number;
    private String latestVersion = "https://gitee.com/api/v5/repos/akarinini/as-as-fans/releases/latest";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);
        config_blacklist = findViewById(R.id.config_blacklist);
        config_check_version = findViewById(R.id.config_check_version);
        config_contract_us = findViewById(R.id.config_contract_us);
        config = findViewById(R.id.config);

        config_check_version_number = findViewById(R.id.config_check_version_number);

        config_check_version_number.setText("当前版本号:" + getVersionName(ConfigActivity.this));

        config_blacklist.setOnClickListener(this::onClick);
        config_check_version.setOnClickListener(this::onClick);
        config_contract_us.setOnClickListener(this::onClick);
        config.setOnClickListener(this::onClick);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.config_blacklist:
                Intent intentBlacklist = new Intent(ConfigActivity.this, BlackListActivity.class);
                startActivity(intentBlacklist);
                break;
            case R.id.config_check_version:
                new Thread(networkTask).start();
                break;
            case R.id.config:
                ConfigActivity.this.finish();
                break;
            case R.id.config_contract_us:
                Intent intentContractUs = new Intent();
                intentContractUs.setAction("android.intent.action.VIEW");
                Uri content_url = Uri.parse("https://gitee.com/akarinini/as-as-fans/issues");
                intentContractUs.setData(content_url);
                startActivity(intentContractUs);
                break;
        }
    }
    /**
     * get App versionName
     * @param context
     * @return
     */
    public String getVersionName(Context context){
        PackageManager packageManager=context.getPackageManager();
        PackageInfo packageInfo;
        String versionName="";
        try {
            packageInfo=packageManager.getPackageInfo(context.getPackageName(),0);
            versionName=packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionName;
    }
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            String val = data.getString("latestVersion");
            Log.i("latestVersion", "请求结果为-->" + val);
            Gson gson = new Gson();
            if (msg.what == GET_DATA_SUCCESS){
                if (val.startsWith("{\"id\":")) {
                    GiteeVersionBean giteeVersionBean = gson.fromJson(val, GiteeVersionBean.class);
                    String versionName = giteeVersionBean.getTag_name();

                    String[] tmp3 = versionName.split("v");
                    String[] versionCodeString = tmp3[1].split("\\.");
                    int versionCode = Integer.valueOf(versionCodeString[0]) * 100 + Integer.valueOf(versionCodeString[1]) * 10 + Integer.valueOf(versionCodeString[2]) * 1;
                    if (versionCode > getVersionCode(ConfigActivity.this)) {
                        DialogPlus dialog = DialogPlus.newDialog(ConfigActivity.this)
                                .setContentHolder(new ViewHolder(R.layout.my_dialog))
                                .setContentHeight(ViewGroup.LayoutParams.WRAP_CONTENT)
                                .setContentWidth(ViewGroup.LayoutParams.WRAP_CONTENT)
                                .setCancelable(true)
                                .setGravity(Gravity.CENTER)
                                .create();
                        View dialogView = dialog.getHolderView();
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
                    } else {
                        Toast.makeText(ConfigActivity.this, "已是最新版本", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(ConfigActivity.this, "403，请手动对比当前与最新版本号", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://gitee.com/akarinini/as-as-fans/releases"));
                    startActivity(intent);
                }
            }else {
                Toast.makeText(ConfigActivity.this, "网络错误，版本号获取失败", Toast.LENGTH_SHORT).show();
            }
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

            } catch (IOException e) {
                e.printStackTrace();
                data.putString("latestVersion", "");
                handler.sendEmptyMessage(NETWORK_ERROR);
            }
            msg.setData(data);
            handler.sendMessage(msg);
        }
    };
}
