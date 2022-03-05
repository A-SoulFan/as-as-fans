package com.example.asasfans.ui.main;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.asasfans.R;
import com.example.asasfans.TestActivity;
import com.example.asasfans.data.TabData;
import com.example.asasfans.data.ToolsData;
import com.example.asasfans.ui.customcomponent.MyImageView;
import com.example.asasfans.ui.video.VideoViewHolder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

/**
 * @author: akari
 * @date: 2022/3/4
 * @description Tools适配器
 */
public class ToolsAdapter extends RecyclerView.Adapter<ToolsAdapter.ToolsViewHolder> {
    private final int CHECK_MAX_LIMIT = 3;
    public static final List<String> iconUrl
            = Arrays.asList("https://asoul.cloud",
                            "https://asoulcnki.asia",
                            "https://asoulfan.com");
    public final List<String> iconFileName
            = Arrays.asList("icon_asoul_cloud",
                            "icon_zwcc",
                            "icon_asf");
    public final List<String> desc
            = Arrays.asList("A-SOUL Fans Art - 一个魂的二创",
                            "枝网查重",
                            "ASOUL FAN");
    public final List<String> name
            = Arrays.asList("图片",
                            "枝网查重",
                            "ASOUL FAN");
    //是否显示选框,默认false
    private boolean isShowBox = false;
    // 存储勾选框状态的map集合
    private Map<Integer, Boolean> map = new HashMap<>();
    private Context context;
    private SharedPreferences userInfo;
    private SharedPreferences.Editor editor;//获取Editor
    private Map<String, ?> tmp;


    public ToolsAdapter(Context context){
        this.context = context;
        initMapAndCheckBox();
    }

    //初始化map集合,默认为不选中
    private void initMapAndCheckBox() {
        if (context.getSharedPreferences("ToolsData", MODE_PRIVATE) == null){
            userInfo = context.getSharedPreferences("ToolsData", MODE_PRIVATE);
            editor = userInfo.edit();
            for (int i = 0; i < iconUrl.size(); i++) {
                editor.putBoolean(iconUrl.get(i), false);
                editor.commit();
                map.put(i, false);
            }
        }else {
            userInfo = context.getSharedPreferences("ToolsData", MODE_PRIVATE);
            editor = userInfo.edit();
            for (int i = 0; i < iconUrl.size(); i++) {
                map.put(i, userInfo.getBoolean(iconUrl.get(i), false));
            }
        }

    }

    @NonNull
    @Override
    public ToolsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.tools_recyclerview, parent,false);
        final ToolsViewHolder toolsViewHolder = new ToolsViewHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        return toolsViewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull ToolsViewHolder holder, int position) {
        holder.imageView.setImageResource(getResource(iconFileName.get(position)));
        holder.textView.setText(desc.get(position));
        if (isShowBox) {
            holder.checkBox.setVisibility(View.VISIBLE);
        } else {
            holder.checkBox.setVisibility(View.INVISIBLE);
        }
//        Animation animation = AnimationUtils.loadAnimation(context, R.anim.list_anim);
//        //设置checkBox显示的动画
//        if (isShowBox)
//            holder.checkBox.startAnimation(animation);
        //设置checkBox改变监听
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //用map集合保存
//                for (int i = 0; i < iconUrl.size(); i++) {
//                    System.out.println(userInfo.getBoolean(iconUrl.get(i), false));
//                }
                map.put(position, isChecked);
                editor.putBoolean(iconUrl.get(position), isChecked);
                editor.commit();
                int count = 0;
                for (int i = 0; i < iconUrl.size(); i++) {
//                    System.out.println(userInfo.getBoolean(iconUrl.get(i), false));
                    if (userInfo.getBoolean(iconUrl.get(i), false))
                        count++;
                }
//                System.out.println(count);
                if (count == CHECK_MAX_LIMIT){
                    Toast.makeText(context, "已达到选择上限", Toast.LENGTH_SHORT).show();
                    buttonView.setChecked(false);
                    map.put(position, false);
                    editor.putBoolean(iconUrl.get(position), false);
                    editor.commit();
                }
//                System.out.println("---------------------");
//                for (int i = 0; i < iconUrl.size(); i++) {
//                    System.out.println(userInfo.getBoolean(iconUrl.get(i), false));
//                }
            }
        });
        // 设置CheckBox的状态
        if (map.get(position) == null) {
            map.put(position, false);
            editor.putBoolean(iconUrl.get(position), false);
            editor.commit();
        }
        Boolean initState = userInfo.getBoolean(iconUrl.get(position), false);
        System.out.println(initState);
        holder.checkBox.setChecked(initState);

    }


    @Override
    public int getItemCount() {
        return iconUrl.size();
    }

    public int  getResource(String imageName) {
        int resId = context.getResources().getIdentifier(imageName, "drawable", context.getPackageName());
        return resId;
    }

    public boolean isShowBox() {
        return isShowBox;
    }

    public void setShowBox() {
        //取反
        isShowBox = !isShowBox;
    }
    //点击item选中CheckBox
    public void setSelectItem(int position) {
        //对当前状态取反
        if (map.get(position)) {
            map.put(position, false);
        } else {
            map.put(position, true);
        }
        notifyItemChanged(position);
    }

    //视图管理
    public class ToolsViewHolder extends RecyclerView.ViewHolder {
        private TextView textView;
        private CheckBox checkBox;
        private MyImageView imageView;

        public ToolsViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.web_desc);
            checkBox = itemView.findViewById(R.id.web_check_box);
            imageView = itemView.findViewById(R.id.web_icon);
        }
    }
}
