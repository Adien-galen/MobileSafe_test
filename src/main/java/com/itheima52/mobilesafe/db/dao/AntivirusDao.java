package com.itheima52.mobilesafe.db.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * ============================================================
 * <p/>
 * 版     权 ：  2016
 * <p/>
 * 作     者  :  崔桂林
 * <p/>
 * 版     本 ： 1.0
 * <p/>
 * 创 建日期 ： 2016/5/29  19:13
 * <p/>
 * 描     述 ：
 */

public class AntivirusDao {

    /**
     * 检查当前的md5值是否在病毒数据库
     * @param md5
     * @return
     */
    public static String checkFileVirus(String md5) {
        String desc = null;
        SQLiteDatabase db = SQLiteDatabase.openDatabase("data/data/com.itheima52.mobilesafe/files/antivirus.db", null, SQLiteDatabase.OPEN_READWRITE);
        Cursor cursor = db.rawQuery("select desc from datable where md5=?",new String[]{md5});
        if(cursor.moveToNext()){
            desc = cursor.getString(0);
        }
        cursor.close();
        return desc;
    }


    /**
     * 添加病毒数据库
     * @param md5  特征码
     * @param desc 描述信息
     */

    public static void addVirus(String md5,String desc){
        SQLiteDatabase db = SQLiteDatabase.openDatabase("data/data/com.itheima52.mobilesafe/files/antivirus.db", null, SQLiteDatabase.OPEN_READWRITE);
        ContentValues values = new ContentValues();
        values.put("md5",md5);
        values.put("type",6);
        values.put("name","Android.Troj.AirAD.a");
        values.put("desc",desc);

        db.insert("datable", null, values);
        db.close();
    }

}
