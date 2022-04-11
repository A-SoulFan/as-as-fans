package com.example.asasfans.ui.main.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.asasfans.AsApplication;
import com.example.asasfans.R;
import com.example.asasfans.ui.main.adapter.BlacklistPagerAdapter;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;

/**
 * @author akarinini
 * @description:
 * @date :2022/4/7 21:37
 */
public class BlacklistFragment extends Fragment {
    public static BlacklistFragment newInstance() {

        Bundle args = new Bundle();
        BlacklistFragment fragment = new BlacklistFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_blacklist, container, false);
        BlacklistPagerAdapter blacklistPagerAdapter = new BlacklistPagerAdapter(getActivity(), getChildFragmentManager());
        ViewPager viewPager = view.findViewById(R.id.view_pager);
        viewPager.setAdapter(blacklistPagerAdapter);
        viewPager.setOffscreenPageLimit(3);
        TabLayout tabs = view.findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

        View emptyView = view.findViewById(R.id.emptyViewMain);
        CoordinatorLayout.LayoutParams layoutParams = new CoordinatorLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, AsApplication.Companion.getStatusBarHeight());
        emptyView.setLayoutParams(layoutParams);
        AppBarLayout appBarLayout = view.findViewById(R.id.appBar);
        appBarLayout.setPadding(0,AsApplication.Companion.getStatusBarHeight(),0,0);
        return view;
    }
}
