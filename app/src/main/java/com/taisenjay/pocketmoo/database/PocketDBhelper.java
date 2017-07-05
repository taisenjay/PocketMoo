package com.taisenjay.pocketmoo.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Author : WangJian
 * Date   : 2017/7/3
 * Created by a handsome boy with love
 */

public class PocketDBhelper extends SQLiteOpenHelper {

    private static final String CREATE_MY_MOVIES = "create table MyMovies(" +
            "id integer primary key autoincrement," +
            "detail_url text," +
            "identity_code text," +
            "publish_time text," +
            "cover_url text)";

    private static final String CREATE_MY_STARS = "create table MyStars(" +
            "id integer primary key autoincrement," +
            "homepage text," +
            "avatar text," +
            "name text)";

    private Context mContext;


    public PocketDBhelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.mContext =context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_MY_MOVIES);
        sqLiteDatabase.execSQL(CREATE_MY_STARS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
