package com.example.asasfans;

import androidx.appcompat.app.AppCompatActivity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;

import com.example.asasfans.ui.main.VTBDataFragment;

/**
 * @description 点击AsAsFans Buttom打开的页面,通过这个activity打开fragment
 * @author zyxdb
 * @time 2022/3/10 22:13
 */
public class LiveDataActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_data);

        VTBDataFragment vtbDataFragment = VTBDataFragment.newInstance();
        getSupportFragmentManager().beginTransaction().replace(R.id.vtbDataFragment, vtbDataFragment).commit();


    }
}