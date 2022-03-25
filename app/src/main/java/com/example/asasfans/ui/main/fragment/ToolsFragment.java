package com.example.asasfans.ui.main.fragment;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.asasfans.R;
import com.example.asasfans.ui.customcomponent.MyDialog;
import com.example.asasfans.ui.customcomponent.RecyclerViewDecoration;
import com.example.asasfans.ui.main.adapter.ToolsAdapter;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

/**
 * @author: akari
 * @date: 2022/3/4
 * @description 工具页面
 */
public class ToolsFragment extends Fragment {
    private DialogPlus dialog;
    private View dialogView;

    public static ToolsFragment newInstance() {
//        Bundle args = new Bundle();

        ToolsFragment fragment = new ToolsFragment();
//        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dialog = DialogPlus.newDialog(getActivity())
                .setContentHolder(new ViewHolder(R.layout.dialog_why))
                .setContentHeight(ViewGroup.LayoutParams.WRAP_CONTENT)
                .setContentWidth(ViewGroup.LayoutParams.MATCH_PARENT)
                .setCancelable(true)
                .setGravity(Gravity.TOP)
                .create();
        dialogView = dialog.getHolderView();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tools, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.tools_recyclerview);
        TextView textView = view.findViewById(R.id.tools_button);
        ImageView imageView = view.findViewById(R.id.image_why);


        ToolsAdapter toolsAdapter = new ToolsAdapter(getActivity());
        toolsAdapter.setHasStableIds(true);
        recyclerView.setAdapter(toolsAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false){
            @Override
            public boolean canScrollVertically() {
                return true;
            }
        });
        recyclerView.addItemDecoration(new RecyclerViewDecoration(8, 8));
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();
            }
        });
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toolsAdapter.setShowBox();
                toolsAdapter.notifyDataSetChanged();
                if (!toolsAdapter.isShowBox()){
                    new MyDialog(getActivity(), R.style.mdialog, new MyDialog.OncloseListener() {
                        @Override
                        public void onClick(boolean confirm) {
                            //设置重启后生效，但是自动重启速度慢，如果用户在自动重启的过程中之间点开应用会有神奇的bug
                            if (confirm){
                                Intent intent = getActivity().getPackageManager()
                                        .getLaunchIntentForPackage(getActivity().getApplication().getPackageName());
                                PendingIntent restartIntent = PendingIntent.getActivity(getActivity().getApplicationContext(), 0, intent, 0);
                                AlarmManager mgr = (AlarmManager)getActivity().getSystemService(Context.ALARM_SERVICE);
                                mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, restartIntent); // 1秒钟后重启应用
                                System.exit(0);
                            }
                        }
                    }).show();
//                    Toast.makeText(getContext(),"手动重启以变更底部栏哦",Toast.LENGTH_SHORT).show();
                    textView.setBackgroundColor(getContext().getResources().getColor(R.color.tab_text_normal));
                    textView.setTextColor(getContext().getResources().getColor(R.color.cardWhite));
                }else {
                    textView.setBackgroundColor(getContext().getResources().getColor(R.color.backgroundGray));
                    textView.setTextColor(getContext().getResources().getColor(R.color.cardBlack));
                }
            }
        });
        return view;
    }

}
