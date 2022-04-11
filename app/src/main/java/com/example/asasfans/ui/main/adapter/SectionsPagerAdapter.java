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
import com.example.asasfans.util.ACache;
import com.example.asasfans.util.ApiConfig;
import com.example.asasfans.util.QConstructor;

import java.util.Arrays;

/**
 * @author akarinini
 * @description 适配器，本适配器作为Fragment嵌套，在MainFragment为一个Fragment
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{ R.string.tab_text_4, R.string.tab_text_5, R.string.tab_text_2, R.string.tab_text_3};
    private final Context mContext;
    private String hotFanArtVideoUrl = "";

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
                return BiliVideoFragment.newInstance(new ApiConfig("score", 1,
                        new QConstructor.QArray("pubdate", Arrays.asList(String.valueOf(System.currentTimeMillis()/1000 - 3 * ACache.TIME_DAY), String.valueOf(System.currentTimeMillis()/1000)), "BETWEEN").toString(), "1", "").getUrl());
            case 1:
                return BiliVideoFragment.newInstance(new ApiConfig("score", 1,
                        new QConstructor.QArray("pubdate", Arrays.asList(String.valueOf(System.currentTimeMillis()/1000 - 3 * ACache.TIME_DAY), String.valueOf(System.currentTimeMillis()/1000)), "BETWEEN").toString(), "2", "").getUrl());
            case 2:
                return BiliVideoFragment.newInstance(new ApiConfig("pubdate", 1, "", "", "").getUrl());
            case 3:
                return BiliVideoFragment.newInstance(new ApiConfig("score", 1, "", "", "").getUrl());
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