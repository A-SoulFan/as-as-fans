package com.example.asasfans.ui.main;

import static com.example.asasfans.TestActivity.floatHelper;
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
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.webkit.WebView;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.asasfans.AsApplication;
import com.example.asasfans.R;
import com.example.asasfans.TestActivity;
import com.example.asasfans.data.GithubVersionBean;
import com.example.asasfans.util.ACache;
import com.google.gson.Gson;
import com.kyleduo.switchbutton.SwitchButton;
import com.nostra13.universalimageloader.core.ImageLoader;
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
    private ImageView config_check_version_icon;
    private ConstraintLayout config_contract_us;
    private ConstraintLayout config_clear_pic_cache;
    private ConstraintLayout config_clear_web_cache;
    private LinearLayout config;
    private TextView config_check_version_number;
    private SwitchButton config_floating_ball_switch;
    private View emptyView;
    private String latestVersion = "https://api.github.com/repos/A-SoulFan/as-as-fans/releases/latest";
    private RotateAnimation mRotateAnimation;
    private Handler mHandler = new Handler();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);
        config_blacklist = findViewById(R.id.config_blacklist);
        config_check_version = findViewById(R.id.config_check_version);
        config_check_version_icon = findViewById(R.id.config_check_version_icon);
        config_contract_us = findViewById(R.id.config_contract_us);
        config_clear_pic_cache = findViewById(R.id.config_clear_pic_cache);
        config_clear_web_cache = findViewById(R.id.config_clear_web_cache);
        config = findViewById(R.id.config);
        emptyView = findViewById(R.id.emptyViewConfig);
        config_floating_ball_switch = findViewById(R.id.config_floating_ball_switch);

        config_check_version_number = findViewById(R.id.config_check_version_number);

        config_check_version_number.setText("当前版本号:" + getVersionName(ConfigActivity.this));

        config_blacklist.setOnClickListener(this::onClick);
        config_check_version.setOnClickListener(this::onClick);
        config_contract_us.setOnClickListener(this::onClick);
        config_clear_pic_cache.setOnClickListener(this::onClick);
        config_clear_web_cache.setOnClickListener(this::onClick);
        config.setOnClickListener(this::onClick);

        ACache aCache = ACache.get(this);
        String tmpACache =  aCache.getAsString("isShowFloatingBall"); // yes or no
        String isNoLongerShowFloatingBall =  aCache.getAsString("isNoLongerShowFloatingBall"); // yes or no
        if (tmpACache == null || isNoLongerShowFloatingBall == null){
            config_floating_ball_switch.setChecked(false);
            aCache.put("isShowFloatingBall", "no");
        } else if(tmpACache.equals("yes") && isNoLongerShowFloatingBall.equals("no")){
            config_floating_ball_switch.setChecked(true);
        }else {
            config_floating_ball_switch.setChecked(false);
        }

        config_floating_ball_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                ACache aCache = ACache.get(ConfigActivity.this);
                if (b){
                    aCache.put("isShowFloatingBall", "yes");
                    if (!Settings.canDrawOverlays(ConfigActivity.this)){
                        aCache.put("isNoLongerShowFloatingBall", "no");
                        Toast.makeText(ConfigActivity.this, "需要手动开启悬浮窗权限才能使用悬浮球，回到主页可再次开启", Toast.LENGTH_SHORT).show();
                    }
                    floatHelper.show();
                }else {
                    aCache.put("isShowFloatingBall", "no");
                    floatHelper.dismiss();
                }
            }
        });
        LinearLayoutCompat.LayoutParams layoutParams = new LinearLayoutCompat.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, AsApplication.Companion.getStatusBarHeight());
        emptyView.setLayoutParams(layoutParams);

    }
    @Override
    protected void onResume() {
        super.onResume();
        floatHelper.dismiss();
    }

    @Override
    protected void onPause() {
        super.onPause();
        ACache aCache = ACache.get(this);
        String tmpACache =  aCache.getAsString("isShowFloatingBall"); // yes or no
        if (tmpACache == null){
            floatHelper.show();
//            Toast.makeText(TestActivity.this, "悬浮球默认打开哦，可以在设置关闭", Toast.LENGTH_SHORT).show();
        }else if (tmpACache.equals("yes")){
            floatHelper.show();
        }else if (tmpACache.equals("no")){

        }
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.config_blacklist:
                Bundle data = new Bundle();
                data.putBoolean("isBlacklist", true);
                Intent intentBlacklist = new Intent(ConfigActivity.this, ClickJumpActivity.class);
                intentBlacklist.putExtras(data);
                startActivity(intentBlacklist);
                break;
            case R.id.config_check_version:
                if (mRotateAnimation == null) {
                    mRotateAnimation = new RotateAnimation(0, 360,
                            Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                            0.5f);
                    mRotateAnimation.setDuration(800);
                    mRotateAnimation.setRepeatCount(-1);
                }
                config_check_version_icon.setAnimation(mRotateAnimation);
                config_check_version_icon.startAnimation(mRotateAnimation);
                new Thread(networkTask).start();
                break;
            case R.id.config:
                ConfigActivity.this.finish();
                break;
            case R.id.config_contract_us:
                Intent intentContractUs = new Intent();
                intentContractUs.setAction("android.intent.action.VIEW");
                Uri content_url = Uri.parse("https://git.asf.ink/A-SoulFan/as-as-fans/issues");
                intentContractUs.setData(content_url);
                startActivity(intentContractUs);
                break;
            case R.id.config_clear_pic_cache:
                ImageLoader.getInstance().clearDiskCache();//清除磁盘缓存
                ImageLoader.getInstance().clearMemoryCache();//清除内存缓存
                Toast.makeText(this, "清除图片缓存成功", Toast.LENGTH_SHORT).show();
                break;
            case R.id.config_clear_web_cache:
                new WebView(ConfigActivity.this).clearCache(true);
                Toast.makeText(this, "清除WEB缓存成功", Toast.LENGTH_SHORT).show();
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
                if (val.startsWith("{\"url\"")) {
                    GithubVersionBean githubVersionBean = gson.fromJson(val, GithubVersionBean.class);
                    String versionName = githubVersionBean.getTag_name();

                    String[] tmp3 = versionName.split("v");
                    String[] versionCodeString = tmp3[1].split("\\.");
                    int versionCode = Integer.valueOf(versionCodeString[0]) * 100 + Integer.valueOf(versionCodeString[1]) * 10 + Integer.valueOf(versionCodeString[2]) * 1;
                    if (versionCode > getVersionCode(ConfigActivity.this)) {
                        DialogPlus dialog = DialogPlus.newDialog(ConfigActivity.this)
                                .setContentHolder(new ViewHolder(R.layout.dialog_upgrade))
                                .setContentHeight(ViewGroup.LayoutParams.WRAP_CONTENT)
                                .setContentWidth(ViewGroup.LayoutParams.MATCH_PARENT)
                                .setCancelable(true)
                                .setContentBackgroundResource(R.color.transparent)
                                .setGravity(Gravity.CENTER)
                                .create();
                        View dialogView = dialog.getHolderView();
                        TextView title = dialogView.findViewById(R.id.title);
                        TextView content = dialogView.findViewById(R.id.upgrade_content);
                        TextView cancel = dialogView.findViewById(R.id.close);
                        TextView confirm = dialogView.findViewById(R.id.upgrade);

                        content.setText(githubVersionBean.getBody());

                        confirm.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://app.asf.ink/"));
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
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://app.asf.ink/"));
                    startActivity(intent);
                }
            }else {
                Toast.makeText(ConfigActivity.this, "网络错误，版本号获取失败", Toast.LENGTH_SHORT).show();
            }
            config_check_version_icon.clearAnimation();
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
