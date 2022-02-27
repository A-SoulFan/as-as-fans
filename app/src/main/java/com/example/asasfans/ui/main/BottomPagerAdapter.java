package com.example.asasfans.ui.main;

import android.content.Context;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.asasfans.R;

/**
 * @author akarinini
 * @description 底部的Fragment适配器，控制{视频，枝网查重，...}等页面，
 */

public class BottomPagerAdapter extends FragmentPagerAdapter {
    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.tab_bottom_1, R.string.tab_bottom_2, R.string.tab_bottom_3};
    private final Context mContext;
    private Object currentFragment;

    public BottomPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        // Show 3 total pages.
        return TAB_TITLES.length;
    }

    /**
     * @description 在这里切换Fragment页面，MainFragment为视频页，WebFragment为枝网页
     * @author akari
     * @time 2022/2/27 11:00
     */
    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return MainFragment.newInstance();
            case 1:
                return WebFragment.newInstance();
            case 2:
                return NullFragment.newInstance();
            default:
        }
        return AUHotFragment.newInstance();
    }

    /**
     * @description 获取当前视点Fragment的object
     * @param
     * @return
     * @author akari
     * @time 2022/2/27 11:00
     */
    @Override
    public void setPrimaryItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        currentFragment = object;
        super.setPrimaryItem(container, position, object);
    }

    /**
     * @description 获取当前视点的Fragment
     * @param
     * @return
     * @author akari
     * @time 2022/2/27 10:07
     */
    public Object getCurrentFragment() {
        return currentFragment;
    }
}
