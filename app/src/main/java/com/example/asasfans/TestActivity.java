package com.example.asasfans;

import androidx.fragment.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.asasfans.data.TabData;
import com.example.asasfans.ui.main.BottomPagerAdapter;
import com.example.asasfans.ui.main.MainFragment;
import com.example.asasfans.ui.main.NullFragment;
import com.example.asasfans.ui.main.ToolsAdapter;
import com.example.asasfans.ui.main.ToolsFragment;
import com.example.asasfans.ui.main.WebFragment;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author akarinini
 * @description 测试用页面，目前作为主页面
 *              2022/3/03 修改底部导航栏可以动态改变标签
 */

public class TestActivity extends AppCompatActivity {
    /** 上次点击返回键的时间 */
    private long lastBackPressed;
    /** 两次点击的间隔时间 */
    private static final int QUIT_INTERVAL = 3000;
    private static String top30;
    private static String pubdate;
    private static String most;
    private TabLayout tabs;
    //设置成静态变量在fragment中调用改变，有内存泄漏，可能有更好的方案，但是我不知道
    private static ViewPager viewPager;
    private static List<TabData> mFragmentList = new ArrayList<>();

    private BottomPagerAdapter bottomPagerAdapter;
    private Object mCurrentFragment;

    private SharedPreferences userInfo;
    private SharedPreferences.Editor editor;//获取Editor
    private Map<String, ?> tmp;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_main);
        initTab();

        bottomPagerAdapter = new BottomPagerAdapter(this, getSupportFragmentManager(), mFragmentList);
        mCurrentFragment = bottomPagerAdapter.getCurrentFragment();
        viewPager = findViewById(R.id.view_pager_main);
        viewPager.setAdapter(bottomPagerAdapter);
        tabs = findViewById(R.id.tabs_bottom);
        tabs.setupWithViewPager(viewPager);

        Intent mIntent =getIntent();
        Bundle mBundle =mIntent.getExtras();
        top30 = mBundle.getString("top30");
        pubdate = mBundle.getString("pubdate");
        most = mBundle.getString("most");
    }

    /**
     * @description 根据存储的CheckBox状态初始化底部栏，与ToolsAdapter耦合性很高，修改时要注意
     * @param
     * @return
     * @author akari
     * @time 2022/3/5 15:02
     */
    private void initTab() {
        //这里初始化TabLayout
        mFragmentList.add(new TabData("视频", MainFragment.newInstance()));
        if (getSharedPreferences("ToolsData", MODE_PRIVATE) != null){
            userInfo = getSharedPreferences("ToolsData", MODE_PRIVATE);
            tmp = userInfo.getAll();
            for (int i = 0 ; i < tmp.size() ; i++){
                if (userInfo.getBoolean(ToolsAdapter.iconUrl.get(i), false)) {
                    switch (i) {
                        case 0:
                            mFragmentList.add(new TabData("图片", NullFragment.newInstance()));
                            break;
                        case 1:
                            mFragmentList.add(new TabData("枝网查重", WebFragment.newInstance()));
                            break;
                        case 2:
                            mFragmentList.add(new TabData("ASOUL FAN", NullFragment.newInstance()));
                            break;
                        default:
                            mFragmentList.add(new TabData("bug", NullFragment.newInstance()));
                    }
                }
            }
        }
        mFragmentList.add(new TabData("全收录", ToolsFragment.newInstance()));
    }

    public static void updateTabs(List<TabData> FragmentList){

        mFragmentList.addAll(1, FragmentList);
        if (viewPager.getAdapter() instanceof BottomPagerAdapter)
            ((BottomPagerAdapter)viewPager.getAdapter()).updateFragmentList(mFragmentList);

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

