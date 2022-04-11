package com.example.asasfans.ui.main.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.asasfans.R;
import com.example.asasfans.data.TabData;

import java.util.ArrayList;
import java.util.List;

/**
 * @author akarinini
 * @description 占位用的Fragment
 */

public class NullFragment extends Fragment {
    private List<TabData> mFragmentList = new ArrayList<>();
    ViewPager viewPager;
    public static NullFragment newInstance() {
        Bundle args = new Bundle();

        NullFragment fragment = new NullFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_test, container, false);
        Button button = view.findViewById(R.id.test_button);
        viewPager = getActivity().findViewById(R.id.view_pager_main);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                mFragmentList.add(new TabData("Null" ,NullFragment.newInstance()));
//                TestActivity.updateTabs(mFragmentList);
//                mFragmentList.clear();
            }
        });
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
