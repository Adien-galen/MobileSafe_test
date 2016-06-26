package com.itheima52.mobilesafe.db.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * ============================================================
 * <p/>
 * 版     权 ：  2016
 * <p/>
 * 作     者  :  崔桂林
 * <p/>
 * 版     本 ： 1.0
 * <p/>
 * 创 建日期 ： 2016/5/31  8:51
 * <p/>
 * 描     述 ：程序所的数据库
 */
public class AppLockDao {

    private AppLockOpenHelper helper;

    public AppLockDao(Context context){
        helper = new AppLockOpenHelper(context);
    }

    /**
     * 添加到程序所里面
     *
     * @param packageName
     *            包名
     */
    public void add(String packageName){
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("packageName",packageName);
        db.insert("info", null, values);
        db.close();
    }


    /**
     * 从程序锁里面删除当前的包
     *
     * @param packageName
     */
    public void delete(String packageName){
        SQLiteDatabase db = helper.getWritableDatabase();
        db.delete("info","packageName=?",new String[]{packageName});
        db.close();
    }


    /**
     * 查询当前的包是否在程序锁里面
     * @param packageName
     * @return
     */
    public boolean find(String packageName){
        boolean result = false;
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.query("info", null, "packageName=?", new String[]{packageName}, null, null, null);
        if(cursor.moveToNext()){
            result=true;
        }
        cursor.close();
        db.close();
        return result;
    }

    /**
     * 查询全部的锁定的包名
     * @return
     */
    public List<String> findAll(){
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.query("info", new String[]{"packageName"}, null, null, null, null, null);
        List<String> packNames = new ArrayList<String>();
        while(cursor.moveToNext()){
            String packageName = cursor.getString(0);
            packNames.add(packageName);
        }
        cursor.close();
        db.close();
        return packNames;
    }
}


