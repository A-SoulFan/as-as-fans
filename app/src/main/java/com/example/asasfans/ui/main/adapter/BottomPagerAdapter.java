package com.example.asasfans.ui.main.adapter;

import android.content.Context;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.asasfans.R;
import com.example.asasfans.TestActivity;
import com.example.asasfans.data.TabData;

import java.util.ArrayList;
import java.util.List;

/**
 * @author akarinini
 * @description 底部的Fragment适配器，控制{视频，枝网查重，...}等页面，
 *              2022/3/03 修改该底部导航栏可以动态改变标签
 */

public class BottomPagerAdapter extends FragmentPagerAdapter {
    private final Context mContext;
    private FragmentManager mFragmentManager;
    private List<TabData> mFragmentList = new ArrayList<>();
    private Object currentFragment;

    public BottomPagerAdapter(Context context, FragmentManager fm, List<TabData> fragmentList) {
        super(fm);
        mFragmentManager = fm;
        mContext = context;
        updateFragmentList(fragmentList);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentList.get(position).getName();
    }

    @Override
    public int getCount() {
        // Show 3 total pages.
        return mFragmentList.size();
    }

    /**
     * @description 在这里切换Fragment页面，MainFragment为视频页，WebFragment为枝网页
     * @author akari
     * @time 2022/2/27 11:00
     */
    @NonNull
    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position).getFragment();
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

    @Override
    public long getItemId(int position) {
        return mFragmentList.get(position).getFragment().hashCode();
    }

    public void updateFragmentList(List<TabData> fragmentList) {
        if (!mFragmentList.isEmpty()) {
            mFragmentList.clear();
        }
        mFragmentList.addAll(fragmentList);
        notifyDataSetChanged();

    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        Fragment instantiateItemFragment = (Fragment) super.instantiateItem(container, position);
        Fragment itemFragment = mFragmentList.get(position).getFragment();
        //如果集合中对应下标的fragment和fragmentManager中的对应下标的fragment对象一致，则直接返回该fragment
        if (instantiateItemFragment == itemFragment) {
            return instantiateItemFragment;
        } else {
            //如果集合中对应下标的fragment和fragmentManager中的对应下标的fragment对象不一致，那么就是新添加的，所以自己add进入；
            mFragmentManager.beginTransaction().add(container.getId(), itemFragment).commit();
            return itemFragment;
        }
    }


    @Override
    public int getItemPosition(@NonNull Object object) {
        //如果fragment还没有添加过，或者没有包含在里面，则返回没有找到
        if (!((Fragment) object).isAdded() || !mFragmentList.contains(object)) {
            return POSITION_NONE;
        }
        //否则就返回列表中的位置
        return mFragmentList.indexOf(object);
    }
}
