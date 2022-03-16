package com.example.asasfans.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.asasfans.R;
import com.example.asasfans.ui.main.adapter.ToolsAdapter;
import com.example.asasfans.ui.main.fragment.ImageFanArtFragment;
import com.example.asasfans.ui.main.fragment.WebFragment;

public class ClickJumpActivity extends AppCompatActivity {
    FragmentManager fragmentManager;
    FragmentTransaction transaction;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_click_jump);
        Intent intent = getIntent();
        Bundle data = intent.getExtras();
        Log.i("WebUrl", data.getString("WebUrl"));
        fragmentManager = getSupportFragmentManager();
        transaction = fragmentManager.beginTransaction();
//        if (data.getString("WebUrl").equals(ToolsAdapter.iconUrl.get(0))){
//            transaction.replace(R.id.click_jump, ImageFanArtFragment.newInstance());
//        }else {
//            transaction.replace(R.id.click_jump, WebFragment.newInstance(data.getString("WebUrl")));
//        }
        transaction.replace(R.id.click_jump, WebFragment.newInstance(data.getString("WebUrl")));
        transaction.commit();
    }
}
