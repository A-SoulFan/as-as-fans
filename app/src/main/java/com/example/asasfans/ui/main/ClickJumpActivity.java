package com.example.asasfans.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.asasfans.R;
import com.example.asasfans.ui.main.adapter.ToolsAdapter;
import com.example.asasfans.ui.main.fragment.BlacklistFragment;
import com.example.asasfans.ui.main.fragment.ImageFanArtFragment;
import com.example.asasfans.ui.main.fragment.WebFragment;

public class ClickJumpActivity extends AppCompatActivity {
    FragmentManager fragmentManager;
    FragmentTransaction transaction;
    /** 上次点击返回键的时间 */
    private long lastBackPressed;
    /** 两次点击的间隔时间 */
    private static final int QUIT_INTERVAL = 3000;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_click_jump);

        Intent intent = getIntent();
        Bundle data = intent.getExtras();
//        Log.i("WebUrl", data.getString("WebUrl"));
        fragmentManager = getSupportFragmentManager();
        transaction = fragmentManager.beginTransaction();
        if (data.getBoolean("isBlacklist")){
            transaction.replace(R.id.click_jump, BlacklistFragment.newInstance());
        } else if (data.getString("WebUrl").equals(ToolsAdapter.iconUrl.get(0))){
            transaction.replace(R.id.click_jump, ImageFanArtFragment.newInstance());
        }else {
            transaction.replace(R.id.click_jump, WebFragment.newInstance(data.getString("WebUrl"), false));
        }
//        transaction.replace(R.id.click_jump, WebFragment.newInstance(data.getString("WebUrl")));
        transaction.commit();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.click_jump);
        Log.i("instanceof", String.valueOf(fragment instanceof WebFragment));
        //解决音量键的监听被webview劫持无法使用的问题
        if (!(keyCode==KeyEvent.KEYCODE_BACK)){
            return super.onKeyDown(keyCode, event);
        }
        if(fragment instanceof WebFragment){
            ((WebFragment)fragment).onKeyDownInClick(keyCode, event);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
