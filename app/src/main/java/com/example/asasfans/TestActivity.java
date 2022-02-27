package com.example.asasfans;

import androidx.fragment.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.asasfans.ui.main.BottomPagerAdapter;
import com.example.asasfans.ui.main.WebFragment;
import com.google.android.material.tabs.TabLayout;

/**
 * @author akarinini
 * @description 测试用页面，目前作为主页面
 */

public class TestActivity extends AppCompatActivity {
    /** 上次点击返回键的时间 */
    private long lastBackPressed;
    /** 两次点击的间隔时间 */
    private static final int QUIT_INTERVAL = 3000;
    private static String top30;
    private static String pubdate;
    private static String most;
    private BottomPagerAdapter bottomPagerAdapter;
    private Object mCurrentFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_main);
        bottomPagerAdapter = new BottomPagerAdapter(this, getSupportFragmentManager());
        mCurrentFragment = bottomPagerAdapter.getCurrentFragment();
        ViewPager viewPager = findViewById(R.id.view_pager_main);
        viewPager.setAdapter(bottomPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs_bottom);
        tabs.setupWithViewPager(viewPager);

        Intent mIntent =getIntent();
        Bundle mBundle =mIntent.getExtras();
        top30 = mBundle.getString("top30");
        pubdate = mBundle.getString("pubdate");
        most = mBundle.getString("most");
    }
    public static String getTop30(){
        return top30;
    }

    public static String getPubdate() {
        return pubdate;
    }

    public static String getMost(){
        return most;
    }

    /**
     * @description Override实现两次退出
     * @author akari
     * @time 2022/2/27 11:47
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        mCurrentFragment = bottomPagerAdapter.getCurrentFragment();
        Log.i("instanceof", String.valueOf(mCurrentFragment instanceof WebFragment));
        if(mCurrentFragment instanceof WebFragment){
            ((WebFragment)mCurrentFragment).onKeyDown(keyCode, event);
            return true;
        }else if (keyCode==KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            long backPressed = System.currentTimeMillis();
            if (backPressed - lastBackPressed > QUIT_INTERVAL) {
                lastBackPressed = backPressed;
                Toast.makeText(this,"再按一次退出", Toast.LENGTH_LONG).show();
            } else {
                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);

    }

    /**
     * @description Override实现两次退出
     * @author akari
     * @time 2022/2/27 11:47
     */
    @Override
    public void onBackPressed() {
        long backPressed = System.currentTimeMillis();
        super.onBackPressed();
        if (backPressed - lastBackPressed > QUIT_INTERVAL) {
            lastBackPressed = backPressed;
            Toast.makeText(this,"再按一次退出",Toast.LENGTH_LONG).show();

        } else {
            finish();
            System.exit(0);
        }
    }
}
