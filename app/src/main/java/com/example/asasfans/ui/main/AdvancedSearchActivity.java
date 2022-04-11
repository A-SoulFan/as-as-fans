package com.example.asasfans.ui.main;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.asasfans.AsApplication;
import com.example.asasfans.R;
import com.example.asasfans.ui.main.fragment.AdvancedSearchOptionFragment;
import com.example.asasfans.ui.main.fragment.BiliVideoFragment;
import com.example.asasfans.util.ApiConfig;
import com.example.asasfans.util.QConstructor;
import com.google.android.material.appbar.AppBarLayout;

import org.angmarch.views.NiceSpinner;
import org.angmarch.views.OnSpinnerItemSelectedListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author akarinini
 * @description:
 * @date :2022/4/9 15:29
 */
public class AdvancedSearchActivity extends AppCompatActivity {
    private List<String> order = Arrays.asList("热门", "播放量", "新发布");
    private List<String> copyright = Arrays.asList("全类型", "自制", "转载");
    private List<String> tname = Arrays.asList("全分区", "动画", "音乐", "舞蹈", "游戏", "生活", "美食", "鬼畜", "其他");
    private List<String> viewNum = Arrays.asList("全播放", "<1万", "1-10万", ">10万");

    private NiceSpinner orderSpinner;
    private NiceSpinner copyrightSpinner;
    private NiceSpinner tnameSpinner;
    private NiceSpinner viewNumSpinner;

    private ImageView search;
    public static TextView editText;

    private Boolean isFirstOrder = true;
    private Boolean isFirstCopyright = true;
    private Boolean isFirstTname = true;
    private Boolean isFirstViewNum = true;

    private Boolean isSearchFragment = false;

    public static ApiConfig apiConfig;
    public static QConstructor.QArray tagQArray;
    public static QConstructor.QArray viewQArray;
    public static QConstructor.QArray pubdateQArray;
    public static QConstructor.QArray midQArray;
//    public static QConstructor qConstructor;
//
//    public static List<QConstructor.QArray> ss = new ArrayList<>();
    public static List<String> tagQ;
    public static List<String> viewQ;
    public static List<String> pubdateQ;
    public static List<String> midQ;

    private ConstraintLayout constraintLayout;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advanced_search);
        AppBarLayout appBarLayout = findViewById(R.id.appBar);
        appBarLayout.setPadding(0, AsApplication.Companion.getStatusBarHeight(),0,0);
        constraintLayout = findViewById(R.id.top_layout);
        initSpinner();

        editText = findViewById(R.id.edit_text);
        search = findViewById(R.id.search);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();;
        transaction.replace(R.id.frameLayout, AdvancedSearchOptionFragment.newInstance());
        transaction.commit();

        apiConfig = new ApiConfig();
        tagQ = new ArrayList<>();
        viewQ = new ArrayList<>();
        pubdateQ = new ArrayList<>();
        initPubdateQ();
        midQ = new ArrayList<>();

        tagQArray = new QConstructor.QArray("tag", tagQ, "OR");
        viewQArray = new QConstructor.QArray("view", viewQ, "BETWEEN");
        pubdateQArray = new QConstructor.QArray("pubdate", pubdateQ, "BETWEEN");
        midQArray = new QConstructor.QArray("mid", midQ, "OR");

        apiConfig.setQ(getQ());

//        ss.add(tagQArray);
//        ss.add(viewQArray);
//        ss.add(pubdateQArray);
//        ss.add(midQArray);
//        qConstructor = new QConstructor(ss);

        constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isSearchFragment = true;
                updateFragment();
            }
        });
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isSearchFragment = true;
                updateFragment();
            }
        });
    }

    private void updateFragment(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        AdvancedSearchActivity.apiConfig.setQ(AdvancedSearchActivity.getQ());
        Log.i("editText.setOnClickListener", apiConfig.getUrl());
        transaction.replace(R.id.frameLayout, BiliVideoFragment.newInstance(apiConfig.getUrl()));
        transaction.commit();
    }

    public static void initPubdateQ(){
        long currentTimeStamp = System.currentTimeMillis()/1000;
        pubdateQ.clear();
        pubdateQ.add("0");
        pubdateQ.add(String.valueOf(currentTimeStamp));
    }

    public static String getQ(){
        return tagQArray.toString() + "~" +
                viewQArray.toString() + "~" +
                pubdateQArray.toString() + "~" +
                midQArray.toString();
    }

    private void initSpinner(){
        orderSpinner = findViewById(R.id.order);
        orderSpinner.attachDataSource(order);
        copyrightSpinner = findViewById(R.id.copyright);
        copyrightSpinner.attachDataSource(copyright);
        tnameSpinner = findViewById(R.id.tname);
        tnameSpinner.attachDataSource(tname);
        viewNumSpinner = findViewById(R.id.view_num);
        viewNumSpinner.attachDataSource(viewNum);
        orderSpinner.setOnSpinnerItemSelectedListener(new OnSpinnerItemSelectedListener() {
            @Override
            public void onItemSelected(NiceSpinner parent, View view, int position, long id) {
                if (isFirstOrder){
                    isFirstOrder = false;
                }else {

                }
                apiConfig.setPage(1);
                switch (position){
                    case 0:
                        apiConfig.setOrder("score");
                        break;
                    case 1:
                        apiConfig.setOrder("view");
                        break;
                    case 2:
                        apiConfig.setOrder("pubdate");
                        break;
                }
                Log.i("orderSpinner", apiConfig.getUrl());
                if (isSearchFragment) {
                    updateFragment();
                }
            }
        });
        copyrightSpinner.setOnSpinnerItemSelectedListener(new OnSpinnerItemSelectedListener() {
            @Override
            public void onItemSelected(NiceSpinner parent, View view, int position, long id) {
                if (isFirstCopyright){
                    isFirstCopyright = false;
                }else {

                }
                apiConfig.setPage(1);
                switch (position){
                    case 0:
                        apiConfig.setCopyright("");
                        break;
                    case 1:
                        apiConfig.setCopyright("1");
                        break;
                    case 2:
                        apiConfig.setCopyright("2");
                        break;
                }
                Log.i("copyrightSpinner", apiConfig.getUrl());
                if (isSearchFragment) {
                    updateFragment();
                }
            }
        });
        tnameSpinner.setOnSpinnerItemSelectedListener(new OnSpinnerItemSelectedListener() {
            @Override
            public void onItemSelected(NiceSpinner parent, View view, int position, long id) {
                if (isFirstTname){
                    isFirstTname = false;
                }else {

                }
                apiConfig.setPage(1);
                switch (position){
                    case 0:
                        apiConfig.setTname("");
                        break;
                    case 1:
                        apiConfig.setTname("animation");
                        break;
                    case 2:
                        apiConfig.setTname("music");
                        break;
                    case 3:
                        apiConfig.setTname("dance");
                        break;
                    case 4:
                        apiConfig.setTname("game");
                        break;
                    case 5:
                        apiConfig.setTname("live");
                        break;
                    case 6:
                        apiConfig.setTname("delicacy");
                        break;
                    case 7:
                        apiConfig.setTname("guichu");
                        break;
                    case 8:
                        apiConfig.setTname("others");
                        break;
                }
                Log.i("tnameSpinner", apiConfig.getUrl());
                if (isSearchFragment) {
                    updateFragment();
                }
            }
        });
        viewNumSpinner.setOnSpinnerItemSelectedListener(new OnSpinnerItemSelectedListener() {
            @Override
            public void onItemSelected(NiceSpinner parent, View view, int position, long id) {
                if (isFirstViewNum){
                    isFirstViewNum = false;
                }else {

                }
                switch (position){
                    case 0:
                        viewQArray.setKeywords(Arrays.asList(""));
                        break;
                    case 1:
                        viewQArray.setKeywords(Arrays.asList("0", "10000"));
                        break;
                    case 2:
                        viewQArray.setKeywords(Arrays.asList("10000", "100000"));
                        break;
                    case 3:
                        viewQArray.setKeywords(Arrays.asList("100000", "100000000"));
                        break;
                }
                apiConfig.setQ(getQ());
                Log.i("viewNumSpinner", apiConfig.getUrl());
                if (isSearchFragment) {
                    updateFragment();
                }
            }
        });
    }

    public void onTagRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.tag_is_or:
                if (checked) {
                    // Pirates are the best
                    tagQArray.setType("OR");

                    apiConfig.setQ(getQ());

                }
                Log.i("tag_is_or", apiConfig.getUrl());
                break;
            case R.id.tag_is_and:
                if (checked) {
                    // Ninjas rule
                    tagQArray.setType("AND");
                    if (tagQ.size() >= 5){
                        tagQ = tagQ.subList(0, 4);
                        Toast.makeText(AdvancedSearchActivity.this,"AND条件下，TAG最多为四个，只保留前四个",Toast.LENGTH_SHORT).show();
                    }
                    apiConfig.setQ(getQ());
                }
                Log.i("tag_is_and", apiConfig.getUrl());
                break;
        }
        updateSearchEditText();
    }

    public static void updateSearchEditText(){
        AdvancedSearchActivity.editText.setText(
                "tag:["+ApiConfig.listToString(AdvancedSearchActivity.tagQ, " "+AdvancedSearchActivity.tagQArray.getType()+" ") + "] AND UID:[" +
                ApiConfig.listToString(AdvancedSearchActivity.midQ, " "+AdvancedSearchActivity.midQArray.getType()+" ") + "]");
    }

    public void onUidRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.uid_is_or:
                if (checked) {
                    // Pirates are the best
                    midQArray.setType("OR");
                    apiConfig.setQ(getQ());
                }
                Log.i("uid_is_or", apiConfig.getUrl());
                break;
            case R.id.uid_is_and:
                if (checked) {
                    // Ninjas rule
                    midQArray.setType("AND");
                    apiConfig.setQ(getQ());
                }
                Log.i("uid_is_and", apiConfig.getUrl());
                break;
        }
        updateSearchEditText();
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.frameLayout);
        Log.i("instanceof", String.valueOf(fragment instanceof AdvancedSearchOptionFragment));
        if (!(keyCode==KeyEvent.KEYCODE_BACK)){
            return super.onKeyDown(keyCode, event);
        }
        if(fragment instanceof AdvancedSearchOptionFragment){
            return super.onKeyDown(keyCode, event);
        }else {
            isSearchFragment = false;
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();;
            transaction.replace(R.id.frameLayout, AdvancedSearchOptionFragment.newInstance());
            transaction.commit();
            return true;
        }
//        return super.onKeyDown(keyCode, event);
    }
}
