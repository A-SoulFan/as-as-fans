package com.example.asasfans.ui.main.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.text.method.DigitsKeyListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.chaychan.viewlib.PowerfulEditText;
import com.example.asasfans.R;
import com.example.asasfans.ui.main.AdvancedSearchActivity;
import com.example.asasfans.ui.main.adapter.PubdateVideoAdapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author akarinini
 * @description:
 * @date :2022/4/9 16:11
 */
public class AdvancedSearchOptionFragment extends Fragment {
    private PowerfulEditText tag_edittext;
    private ImageButton tag_add;
    private ImageView tag_reset;

    private TextView start_time_pick;
    private TextView end_time_pick;
    private ImageView time_reset;

    private PowerfulEditText uid_edittext;
    private ImageView uid_add;
    private ImageView uid_reset;

    private TimePickerView startTimePicker;
    private TimePickerView endTimePicker;

    private RadioButton tag_is_and;

    private String initTime = "-/-/-";

    public static AdvancedSearchOptionFragment newInstance() {

        Bundle args = new Bundle();

        AdvancedSearchOptionFragment fragment = new AdvancedSearchOptionFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_advanced_search_option, container, false);
        init(view);
        initOnclickListener();
        timePickerReset();
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void init(View view){
        tag_edittext = view.findViewById(R.id.tag_edittext);
        tag_add = view.findViewById(R.id.tag_add);
        tag_reset = view.findViewById(R.id.tag_reset);


        start_time_pick = view.findViewById(R.id.start_time_pick);
        end_time_pick = view.findViewById(R.id.end_time_pick);
        time_reset = view.findViewById(R.id.time_reset);

        uid_edittext = view.findViewById(R.id.uid_edittext);
        uid_add = view.findViewById(R.id.uid_add);
        uid_reset = view.findViewById(R.id.uid_reset);

        tag_is_and = view.findViewById(R.id.tag_is_and);

        if (AdvancedSearchActivity.tagQArray.getType().equals("AND")) {
            tag_is_and.setChecked(true);
        }

        startTimePicker = new TimePickerBuilder(getActivity(), new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                start_time_pick.setText(getTime(date));
                Log.i("onTimeSelect", String.valueOf(date.getTime()/1000));
                AdvancedSearchActivity.pubdateQ.set(0, String.valueOf(date.getTime()/1000));
                AdvancedSearchActivity.apiConfig.setQ(AdvancedSearchActivity.getQ());
                try {
                    compareStartEndTime();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        })
                .setType(new boolean[]{true, true, true, false, false, false})
                .setCancelText("取消")
                .setSubmitText("确定")
                .setContentTextSize(17)
                .setTitleSize(20)
                .setTitleText("开始日期")
                .setOutSideCancelable(true)
                .isCyclic(false)
                .setTitleColor(0xFF576691)
                .setSubmitColor(0xFF576691)
                .setCancelColor(0xFF576691)
                .build();
        endTimePicker = new TimePickerBuilder(getActivity(), new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                end_time_pick.setText(getTime(date));
                Log.i("onTimeSelect", String.valueOf(date.getTime()/1000));
                AdvancedSearchActivity.pubdateQ.set(1, String.valueOf(date.getTime()/1000));
                AdvancedSearchActivity.apiConfig.setQ(AdvancedSearchActivity.getQ());
                try {
                    compareStartEndTime();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        })
                .setType(new boolean[]{true, true, true, false, false, false})
                .setCancelText("取消")
                .setSubmitText("确定")
                .setContentTextSize(17)
                .setTitleSize(20)
                .setTitleText("结束日期")
                .setOutSideCancelable(true)
                .isCyclic(false)
                .setTitleColor(0xFF576691)
                .setSubmitColor(0xFF576691)
                .setCancelColor(0xFF576691)
                .build();

    }

    private void compareStartEndTime() throws ParseException {
        if (!start_time_pick.getText().toString().equals(initTime) && !end_time_pick.getText().toString().equals(initTime)) {
            long startTime = dateToStamp(start_time_pick.getText().toString());
            long endTime = dateToStamp(end_time_pick.getText().toString());
            Log.i("startTime", String.valueOf(startTime));
            Log.i("endTime", String.valueOf(endTime));
            if (startTime > endTime) {
                Toast.makeText(getActivity(), "开始时间大于结束时间，已重置", Toast.LENGTH_SHORT).show();
                start_time_pick.setText(initTime);
                end_time_pick.setText(initTime);
                AdvancedSearchActivity.initPubdateQ();
            }
        }
    }

    private void timePickerReset(){
        if (!AdvancedSearchActivity.pubdateQ.get(0).equals("0")){
            Log.i("startTime", String.valueOf(AdvancedSearchActivity.pubdateQ.get(0)));
            Log.i("endTime", String.valueOf(AdvancedSearchActivity.pubdateQ.get(1)));
            start_time_pick.setText(PubdateVideoAdapter.stampToDate(AdvancedSearchActivity.pubdateQ.get(0)));
            end_time_pick.setText(PubdateVideoAdapter.stampToDate(AdvancedSearchActivity.pubdateQ.get(1)));
        }
    }

    @SuppressLint("SimpleDateFormat")
    public static long dateToStamp(String s) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = simpleDateFormat.parse(s);
        long ts = date.getTime();
        return ts;
    }


    public void initOnclickListener(){
        tag_edittext.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE) {
                    Log.i("---","搜索操作执行");
                    if (tag_edittext.getText().toString().equals("")){
                        Toast.makeText(getActivity(),"TAG不能为空~",Toast.LENGTH_SHORT).show();
                    }else {
                        AdvancedSearchActivity.tagQ.add(tag_edittext.getText().toString());
                        AdvancedSearchActivity.apiConfig.setQ(AdvancedSearchActivity.getQ());
                        AdvancedSearchActivity.updateSearchEditText();
                        tag_edittext.setText("");
                    }
                }
                return true;
            }
        });
        tag_edittext.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(!hasFocus){
                    InputMethodManager manager = ((InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE));
                    if (manager != null)
                        manager.hideSoftInputFromWindow(view.getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
        });

        tag_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("tag_add", tag_edittext.getText().toString());
                if (tag_edittext.getText().toString().equals("")){
                    Toast.makeText(getActivity(),"TAG不能为空~",Toast.LENGTH_SHORT).show();
                }else if (AdvancedSearchActivity.tagQArray.getType().equals("AND") && AdvancedSearchActivity.tagQ.size() >= 4){
                    Toast.makeText(getActivity(),"AND条件下，TAG最多为四个，不能继续添加了~",Toast.LENGTH_SHORT).show();
                }
                else {
                    AdvancedSearchActivity.tagQ.add(tag_edittext.getText().toString());
                    AdvancedSearchActivity.apiConfig.setQ(AdvancedSearchActivity.getQ());
                    AdvancedSearchActivity.updateSearchEditText();
                    tag_edittext.setText("");
                }
            }
        });
        tag_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AdvancedSearchActivity.tagQ.clear();
                AdvancedSearchActivity.apiConfig.setQ(AdvancedSearchActivity.getQ());
                AdvancedSearchActivity.updateSearchEditText();
                tag_edittext.setText("");
            }
        });
        start_time_pick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startTimePicker.show();
            }
        });
        end_time_pick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                endTimePicker.show();
            }
        });
        time_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                start_time_pick.setText(initTime);
                end_time_pick.setText(initTime);
                AdvancedSearchActivity.initPubdateQ();
                AdvancedSearchActivity.apiConfig.setQ(AdvancedSearchActivity.getQ());
            }
        });
        String digists = "0123456789";
        uid_edittext.setKeyListener(DigitsKeyListener.getInstance(digists));
        uid_edittext.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE) {
                    if (uid_edittext.getText().toString().equals("")){
                        Toast.makeText(getActivity(),"UID不能为空~",Toast.LENGTH_SHORT).show();
                    }else {
                        AdvancedSearchActivity.midQ.add(uid_edittext.getText().toString());
                        AdvancedSearchActivity.apiConfig.setQ(AdvancedSearchActivity.getQ());
                        AdvancedSearchActivity.updateSearchEditText();
                        uid_edittext.setText("");
                    }
                }
                return true;
            }
        });
        uid_edittext.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(!hasFocus){
                    InputMethodManager manager = ((InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE));
                    if (manager != null)
                        manager.hideSoftInputFromWindow(view.getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
        });
        uid_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("uid_add", uid_edittext.getText().toString());
                if (uid_edittext.getText().toString().equals("")){
                    Toast.makeText(getActivity(),"UID不能为空~",Toast.LENGTH_SHORT).show();
                }else {
                    AdvancedSearchActivity.midQ.add(uid_edittext.getText().toString());
                    AdvancedSearchActivity.apiConfig.setQ(AdvancedSearchActivity.getQ());
                    AdvancedSearchActivity.updateSearchEditText();
                    uid_edittext.setText("");
                }
            }
        });
        uid_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AdvancedSearchActivity.midQ.clear();
                AdvancedSearchActivity.apiConfig.setQ(AdvancedSearchActivity.getQ());
                AdvancedSearchActivity.updateSearchEditText();
                uid_edittext.setText("");
            }
        });
    }


    private String getTime(Date date) {//可根据需要自行截取数据显示
        Log.d("getTime()", "choice date millis: " + date.getTime());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }
}
