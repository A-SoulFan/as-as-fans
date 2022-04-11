package com.example.asasfans.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBOpenHelper extends SQLiteOpenHelper {
    public static final int DB_VERSION = 2;

    public DBOpenHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sql="create table if not exists blackBvid(bvid TEXT primary key NOT NULL UNIQUE, PicUrl TEXT, Title TEXT, Duration integer, Author TEXT, ViewNum integer, LikeNum integer, Tname TEXT)";
//        String createAuthorTable="create table blackAuthor(name TEXT primary key NOT NULL UNIQUE)";
//        sqLiteDatabase.execSQL(createAuthorTable);
        sqLiteDatabase.execSQL(sql);
        //version2 建两个新表
        sqLiteDatabase.execSQL("create table if not exists blackMid(mid integer primary key NOT NULL UNIQUE)");
        sqLiteDatabase.execSQL("create table if not exists blackTag(tag Text primary key NOT NULL UNIQUE)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        if (i < 2){
            //建两个新表
            onCreate(sqLiteDatabase);
        }
    }
}
