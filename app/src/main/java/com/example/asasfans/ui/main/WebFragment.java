package com.example.asasfans.ui.main;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.asasfans.R;
import com.example.asasfans.TestActivity;

/**
 * @author akarinini
 * @description webFragment，使用WebView直接加载URL显示，属于BottomPagerAdapter
 */

public class WebFragment extends Fragment {
    private WebView webView;
    private long exitTime = 0;

    public static WebFragment newInstance() {
        WebFragment fragment = new WebFragment();

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_web, container, false);
        webView = view.findViewById(R.id.webView);
        webView.loadUrl("https://asoulcnki.asia/");
        webView.setWebViewClient(new WebViewClient());
        webView.setWebChromeClient(new WebChromeClient());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);

        return view;
    }

    public void onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
            webView.goBack();
        }
        if ((keyCode == KeyEvent.KEYCODE_BACK) && (!webView.canGoBack()) && event.getRepeatCount() == 0) {
            if ((System.currentTimeMillis() - exitTime) > 3000) {
                Toast.makeText(getActivity(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                getActivity().finish();
            }
        }
    }
}
