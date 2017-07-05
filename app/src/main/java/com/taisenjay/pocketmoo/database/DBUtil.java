package com.taisenjay.pocketmoo.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.taisenjay.pocketmoo.model.Movie;
import com.taisenjay.pocketmoo.model.StarSimple;

import java.util.ArrayList;
import java.util.List;

/**
 * Author : WangJian
 * Date   : 2017/7/3
 * Created by a handsome boy with love
 */

public class DBUtil {
    private static PocketDBhelper mDBhelper;
    private static SQLiteDatabase mDB;

    private static PocketDBhelper getDBhelper(Context context,int version){
        if (mDBhelper == null)
            return mDBhelper = new PocketDBhelper(context,"pocket.db",null,version);
        return mDBhelper;
    }

    public static void initDB(Context context){
        if (mDBhelper == null)
            mDBhelper = getDBhelper(context,1);
        if (mDB == null)
            mDB = mDBhelper.getWritableDatabase();
//        return mDB;
    }

    public static long insertToMyMovies(Context context, Movie movie){
        initDB(context);
        ContentValues values = new ContentValues();
        values.put("detail_url",movie.detailUrl);
        values.put("identity_code",movie.identityCode);
        values.put("publish_time",movie.publishTime);
        values.put("cover_url",movie.coverUrl);
        return mDB.insert("MyMovies",null,values);
    }

    public static List<Movie> getMyMovies(Context context, int page){
        initDB(context);
        List<Movie> movies = new ArrayList<>();
        int start = (page - 1)*10;
        int end = page * 10 - 1;
        Cursor cursor = mDB.query("MyMovies",null,null,null,
                null,null,null,start + "," + end);
        if (cursor.moveToFirst()){
            do {
                Movie movie = new Movie();
                movie.detailUrl = cursor.getString(cursor.getColumnIndex("detail_url"));
                movie.identityCode = cursor.getString(cursor.getColumnIndex("identity_code"));
                movie.publishTime = cursor.getString(cursor.getColumnIndex("publish_time"));
                movie.coverUrl = cursor.getString(cursor.getColumnIndex("cover_url"));
                movies.add(movie);
            }while (cursor.moveToNext());
        }

        return movies;
    }

    public static boolean ifCodeInMovies(Context context,String str){
        initDB(context);
        List<String> codes = new ArrayList<>();
        Cursor cursor = mDB.query("MyMovies",new String[]{"identity_code"},null,null,
                null,null,null,null);
        if (cursor.moveToFirst()){
            do {
                String name = cursor.getString(cursor.getColumnIndex("identity_code"));
                codes.add(name);
            }while (cursor.moveToNext());
        }
        return codes.contains(str);
    }

    public static int removeMyMovie(Context context,Movie movie){
        initDB(context);
        return mDB.delete("MyMovies","identity_code = ?",new String[]{movie.identityCode});
    }

    public static long insertToMyStars(Context context,StarSimple star){
        initDB(context);
        ContentValues values = new ContentValues();
        values.put("homepage",star.homepage);
        values.put("avatar",star.avatar);
        values.put("name",star.name);
        return mDB.insert("MyStars",null,values);
    }

    public static List<StarSimple> getMyStars(Context context,int page){
        initDB(context);
        List<StarSimple> stars = new ArrayList<>();
        int start = (page - 1)*10;
        int end = page * 10 - 1;
        Cursor cursor = mDB.query("MyStars",null,null,null,
                null,null,null,start + "," + end);
        if (cursor.moveToFirst()){
            do {
                StarSimple star = new StarSimple();
                star.homepage = cursor.getString(cursor.getColumnIndex("homepage"));
                star.avatar = cursor.getString(cursor.getColumnIndex("avatar"));
                star.name = cursor.getString(cursor.getColumnIndex("name"));
                stars.add(star);
            }while (cursor.moveToNext());
        }

        return stars;
    }

    public static boolean ifNameInStars(Context context,String str){
        initDB(context);
        List<String> stars = new ArrayList<>();
        Cursor cursor = mDB.query("MyStars",new String[]{"name"},null,null,
                null,null,null,null);
        if (cursor.moveToFirst()){
            do {
                String name = cursor.getString(cursor.getColumnIndex("name"));
                stars.add(name);
            }while (cursor.moveToNext());
        }
        return stars.contains(str);
    }

}
