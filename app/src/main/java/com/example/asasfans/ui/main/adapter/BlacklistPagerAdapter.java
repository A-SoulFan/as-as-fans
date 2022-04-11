package com.example.asasfans.ui.main.adapter;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.asasfans.data.DBOpenHelper;
import com.example.asasfans.ui.main.fragment.BlacklistTabsFragment;
import com.example.asasfans.ui.main.fragment.NullFragment;
import com.example.asasfans.util.ApiConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * @author akarinini
 * @description:
 * @date :2022/4/7 21:30
 */
public class BlacklistPagerAdapter extends FragmentPagerAdapter {
    private static final String[] TAB_TITLES = new String[]{"TAG黑名单", "作者黑名单", "视频黑名单"};
    private final Context context;

    public BlacklistPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        this.context = context;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        List<String> name;
        DBOpenHelper dbOpenHelper = new DBOpenHelper(context,"blackList.db",null,DBOpenHelper.DB_VERSION);
        SQLiteDatabase sqliteDatabase = dbOpenHelper.getReadableDatabase();
        Cursor cursor;
        switch (position){
            case 2:
                name = new ArrayList<>();
                name.clear();
                cursor = sqliteDatabase.query("blackBvid",null,null,null,null,null,null);
                if (cursor.getCount() > 0) {
                    while (cursor.moveToNext()) {
                        name.add(cursor.getString(cursor.getColumnIndex("Title")));
                    }
                }
                sqliteDatabase.close();
                dbOpenHelper.close();
                sqliteDatabase.close();
                return BlacklistTabsFragment.newInstance(ApiConfig.listToString(name, ","), "blackBvid", "Title");
            case 1:
                name = new ArrayList<>();
                name.clear();
                cursor = sqliteDatabase.query("blackMid",null,null,null,null,null,null);
                if (cursor.getCount() > 0) {
                    while (cursor.moveToNext()) {
                        name.add(cursor.getString(cursor.getColumnIndex("mid")));
                    }
                }
                sqliteDatabase.close();
                dbOpenHelper.close();
                sqliteDatabase.close();
                return BlacklistTabsFragment.newInstance(ApiConfig.listToString(name, ","), "blackMid", "mid");
            case 0:
                name = new ArrayList<>();
                name.clear();
                cursor = sqliteDatabase.query("blackTag",null,null,null,null,null,null);
                if (cursor.getCount() > 0) {
                    while (cursor.moveToNext()) {
                        name.add(cursor.getString(cursor.getColumnIndex("tag")));
                    }
                }
                sqliteDatabase.close();
                dbOpenHelper.close();
                sqliteDatabase.close();
                return BlacklistTabsFragment.newInstance(ApiConfig.listToString(name, ","), "blackTag", "tag");
            default:
                return NullFragment.newInstance();
        }
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return TAB_TITLES[position];
    }

    @Override
    public int getCount() {
        return TAB_TITLES.length;
    }
}
