package com.example.asasfans.data;

import androidx.fragment.app.Fragment;

/**
 * @author: akari
 * @date: 2022/3/3
 * @description TabLayout的数据类
 */
public class TabData {
    Fragment fragment;
    String name;

    public TabData(String name, Fragment fragment){
        this.fragment = fragment;
        this.name = name;
    }

    public Fragment getFragment() {
        return fragment;
    }

    public String getName() {
        return name;
    }
}
