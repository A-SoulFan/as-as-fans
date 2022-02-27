package com.example.asasfans.ui.main;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

/**
 * @author akarinini
 * @description 占位用的Fragment
 */

public class NullFragment extends Fragment {
    public static NullFragment newInstance() {

        Bundle args = new Bundle();

        NullFragment fragment = new NullFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
