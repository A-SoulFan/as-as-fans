package com.example.asasfans.ui.main.adapter;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.asasfans.R;
import com.example.asasfans.ui.main.fragment.BiliVideoFragment;
import com.example.asasfans.ui.main.fragment.NullFragment;

/**
 * @author akarinini
 * @description 适配器，本适配器作为Fragment嵌套，在MainFragment为一个Fragment
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{ R.string.tab_text_4, R.string.tab_text_5, R.string.tab_text_2, R.string.tab_text_3};
    private final Context mContext;

    public SectionsPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        switch (position){
//            case 0:
//                return AUHotFragment.newInstance();
            case 0:
                return BiliVideoFragment.newInstance("http://124.223.8.236:5200/AsoulRT-FanArt?page=");
            case 1:
                return BiliVideoFragment.newInstance("http://124.223.8.236:5200/AsoulRT-Cut?page=");
            case 2:
                return BiliVideoFragment.newInstance("http://124.223.8.236:5200/AsoulPudateVedio?page=");
            case 3:
                return BiliVideoFragment.newInstance("http://124.223.8.236:5200/AsoulMostViewVedio?page=");
            default:
                return NullFragment.newInstance();
        }
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
}