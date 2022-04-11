package com.example.asasfans.ui.main.fragment;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chaychan.viewlib.PowerfulEditText;
import com.example.asasfans.R;
import com.example.asasfans.data.DBOpenHelper;
import com.example.asasfans.ui.customcomponent.RecyclerViewDecoration;
import com.example.asasfans.util.ApiConfig;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;
import com.scwang.smart.refresh.layout.api.RefreshLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * @author akarinini
 * @description:
 * @date :2022/4/8 14:41
 */
public class BlacklistTabsFragment extends Fragment {
    private String blacklist;
    private String table;
    private String colName;
    private RecyclerView recyclerView;
    private BlacklistTabsAdapter blacklistTabsAdapter;
    private List<String> info = new ArrayList<>();
    private RefreshLayout refreshLayout;
    private FloatingActionButton fabAdd;
    private DialogPlus dialog;
    private View dialogView;

    public static BlacklistTabsFragment newInstance(String blacklist, String table, String colName) {

        Bundle args = new Bundle();
        args.putString("blacklist", blacklist);
        args.putString("table", table);
        args.putString("colName", colName);
        BlacklistTabsFragment fragment = new BlacklistTabsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    void initDialog(){
        dialog = DialogPlus.newDialog(getActivity())
                .setContentHolder(new ViewHolder(R.layout.dialog_blacklist_copy_export))
                .setContentHeight(ViewGroup.LayoutParams.WRAP_CONTENT)
                .setContentWidth(ViewGroup.LayoutParams.MATCH_PARENT)
                .setCancelable(true)
                .setContentBackgroundResource(R.color.transparent)
                .setGravity(Gravity.CENTER)
                .create();
        dialogView = dialog.getHolderView();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        initDialog();
        recyclerView = view.findViewById(R.id.recyclerview);
        refreshLayout = view.findViewById(R.id.refreshLayout);

        fabAdd = view.findViewById(R.id.fab_add);
        fabAdd.setVisibility(View.VISIBLE);
        refreshLayout.setEnableRefresh(false);
        refreshLayout.setEnableLoadMore(false);
        info.clear();
        for (String s : blacklist.split(",")){
            info.add(s);
        }
        if (info.get(0).equals("")){
            info.clear();
        }
        blacklistTabsAdapter = new BlacklistTabsAdapter(getActivity(), info);
        recyclerView.setAdapter(blacklistTabsAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        linearLayoutManager.setInitialPrefetchItemCount(2);


        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView export_to_clip = dialogView.findViewById(R.id.export_to_clip);
                TextView copy_from_clip = dialogView.findViewById(R.id.copy_from_clip);
                TextView import_from_edittext = dialogView.findViewById(R.id.import_from_edittext);
                PowerfulEditText edittext = dialogView.findViewById(R.id.edittext);

                //点击软键盘外部，收起软键盘
                edittext.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View view, boolean hasFocus) {
                        if(!hasFocus){
                            InputMethodManager manager = ((InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE));
                            if (manager != null)
                                manager.hideSoftInputFromWindow(view.getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
                        }
                    }
                });
                export_to_clip.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        List<String> mid = new ArrayList<>();
                        List<String> tag = new ArrayList<>();
                        mid.clear();
                        tag.clear();
                        DBOpenHelper dbOpenHelper = new DBOpenHelper(getActivity(),"blackList.db",null,DBOpenHelper.DB_VERSION);
                        SQLiteDatabase sqliteDatabase = dbOpenHelper.getWritableDatabase();
                        Cursor cursor;
                        cursor = sqliteDatabase.query("blackMid",null,null,null,null,null,null);
                        if (cursor.getCount() > 0) {
                            while (cursor.moveToNext()) {
                                mid.add(cursor.getString(cursor.getColumnIndex("mid")));
                            }
                        }
                        cursor = sqliteDatabase.query("blackTag",null,null,null,null,null,null);
                        if (cursor.getCount() > 0) {
                            while (cursor.moveToNext()) {
                                tag.add(cursor.getString(cursor.getColumnIndex("tag")));
                            }
                        }
                        sqliteDatabase.close();
                        dbOpenHelper.close();
                        sqliteDatabase.close();
                        String tmp =  "tag." + ApiConfig.listToString(tag, "+") + ".tag~uid." + ApiConfig.listToString(mid, "+") + ".uid";
                        ClipboardManager cm = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                        ClipData mClipData = ClipData.newPlainText("blacklist", tmp);
                        cm.setPrimaryClip(mClipData);

                        Toast.makeText(getActivity(),"导出成功：" + tmp,Toast.LENGTH_SHORT).show();
                    }
                });
                copy_from_clip.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ClipboardManager clipboardManager = (ClipboardManager)getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                        if (null != clipboardManager) {
                            // 获取剪贴板的剪贴数据集
                            ClipData clipData = clipboardManager.getPrimaryClip();
                            if (null != clipData && clipData.getItemCount() > 0) {
                                // 从数据集中获取（粘贴）第一条文本数据
                                ClipData.Item item = clipData.getItemAt(0);
                                if (null != item) {
                                    String content = item.getText().toString();
                                    insertStringToSqlite(content);
                                }else {
                                    Toast.makeText(getActivity(),"剪贴板内容为空",Toast.LENGTH_SHORT).show();
                                }
                            }else {
                                Toast.makeText(getActivity(),"剪贴板内容为空",Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            Toast.makeText(getActivity(),"获取不到剪贴板",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                import_from_edittext.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        insertStringToSqlite(edittext.getText().toString());
                    }
                });
                dialog.show();
            }
        });
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(new RecyclerViewDecoration(8, 8));
        return view;
    }

    private void insertStringToSqlite(String s){
        String[] tags;
        String[] mids;
        if (s.startsWith("tag.") && s.endsWith(".uid") && (s.indexOf("") != -1)){
            String[] s2 = s.split("~");
            if (s2.length == 2){
                String[] tagTmp = s2[0].split("\\.");
                String[] midTmp = s2[1].split("\\.");
                if (tagTmp.length == 3 && midTmp.length == 3){
                    tags = tagTmp[1].split("\\+");
                    mids = midTmp[1].split("\\+");
                }else {
                    Toast.makeText(getActivity(),"导入的语句不符合格式要求",Toast.LENGTH_SHORT).show();
                    return;
                }
            }else {
                Toast.makeText(getActivity(),"导入的语句不符合格式要求",Toast.LENGTH_SHORT).show();
                return;
            }
        }else {
            Toast.makeText(getActivity(),"导入的语句不符合格式要求",Toast.LENGTH_SHORT).show();
            return;
        }
        DBOpenHelper dbOpenHelper = new DBOpenHelper(getActivity(),"blackList.db",null,DBOpenHelper.DB_VERSION);
        SQLiteDatabase sqliteDatabase = dbOpenHelper.getWritableDatabase();
        ContentValues valuesTag = new ContentValues();
        if (tags.length == 1 && tags[0].equals("")){

        }else {
            for (String tmp : tags) {
                valuesTag.put("tag", tmp);
                sqliteDatabase.insert("blackTag", null, valuesTag);
            }
        }
        ContentValues valuesMid = new ContentValues();
        if (mids.length == 1 && mids[0].equals("")){

        }else {
            for (String tmp : mids) {
                try {
                    valuesMid.put("mid", Integer.valueOf(tmp));
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "有UID不是纯数字，用户黑名单导入失败，TAG黑名单成功", Toast.LENGTH_SHORT).show();
                    sqliteDatabase.close();
                    dbOpenHelper.close();
                    sqliteDatabase.close();
                    return;
                }
                sqliteDatabase.insert("blackMid", null, valuesMid);
            }

        }
        Toast.makeText(getActivity(),"导入成功",Toast.LENGTH_SHORT).show();
        sqliteDatabase.close();
        dbOpenHelper.close();
        sqliteDatabase.close();
    }
    public final static boolean isNumeric(String str){
        if (str != null && !"".equals(str.trim())){
            return str.matches("^[0-9]*$");
        }
        else{
            return false;
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        blacklist = getArguments().getString("blacklist");
        table = getArguments().getString("table");
        colName = getArguments().getString("colName");
        super.onCreate(savedInstanceState);
    }

    public class BlacklistTabsAdapter extends RecyclerView.Adapter<BlacklistViewHolder> {
        Context context;
        List<String> name;

        public BlacklistTabsAdapter(Context context, List<String> name) {
            this.context = context;
            this.name = name;
        }

        @NonNull
        @Override
        public BlacklistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.blacklist_recyclerview, parent,false);
            BlacklistViewHolder blacklistViewHolder = new BlacklistViewHolder(view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
            blacklistViewHolder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DBOpenHelper dbOpenHelper = new DBOpenHelper(context,"blackList.db",null,DBOpenHelper.DB_VERSION);
                    SQLiteDatabase sqliteDatabase = dbOpenHelper.getWritableDatabase();
                    sqliteDatabase.delete(table, colName + "=?", new String[]{info.get(blacklistViewHolder.getBindingAdapterPosition())});
                    sqliteDatabase.close();
                    dbOpenHelper.close();
                    info.remove(blacklistViewHolder.getBindingAdapterPosition());
                    notifyItemRemoved(blacklistViewHolder.getBindingAdapterPosition());
                    notifyDataSetChanged();
                    Toast.makeText(context,"解除屏蔽成功",Toast.LENGTH_SHORT).show();
                }
            });
            return blacklistViewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull BlacklistViewHolder holder, int position) {
            holder.name.setText(name.get(position));
        }

        @Override
        public int getItemCount() {
            return name.size();
        }
    }

    public class BlacklistViewHolder extends RecyclerView.ViewHolder{
        private TextView name;
        private TextView delete;

        public BlacklistViewHolder(@NonNull View itemView) {
            super(itemView);
            delete = itemView.findViewById(R.id.delete);
            name = itemView.findViewById(R.id.blacklist_text);
        }
    }

}
