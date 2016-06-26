package com.itheima52.mobilesafe.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.os.SystemClock;
import android.util.Xml;

import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * ============================================================
 * <p/>
 * 版     权 ：  2016
 * <p/>
 * 作     者  :  崔桂林
 * <p/>
 * 版     本 ： 1.0
 * <p/>
 * 创 建日期 ： 2016/5/23  19:06
 * <p/>
 * 描     述 ：
 * <p/>
 * <p/>
 * 修 订 历史：
 * <p/>
 * ============================================================
 */
public class SmsUtils {

    /**
     * 备份短信的接口
     */
    public interface BackUpCallBackSms {
        public void befor(int count);

        public void onBackUpSms(int process);
    }

    public static boolean backUp(Context context, BackUpCallBackSms callback) {
        /**
         * 目的 ： 备份短信：
         *
         * 1 判断当前用户的手机上面是否有sd卡
         * 2 权限 ---
         *   使用内容观察者
         * 3 写短信(写到sd卡)
         *
         *
         */

        // 判断当前sd卡的状态
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            // 如果能进来就说明用户有SD卡
            ContentResolver contentResolver = context.getContentResolver();

            // 获取短信的路径
            Uri uri = Uri.parse("content://sms/");

            Cursor cursor = contentResolver.query(uri, new String[]{"address", "date", "type", "body"}, null, null, null);

            //获取当前一共有多少条短信
            int count = cursor.getCount();

            callback.befor(count);

            //进度条默认是0
            int process = 0;

            try {
                File file = new File(Environment.getExternalStorageDirectory(),"backup.xml");

                FileOutputStream os = new FileOutputStream(file);

                // 得到序列化器
                // 在android系统里面所有有关xml的解析都是pull解析
                XmlSerializer xmlSerializer = Xml.newSerializer();
                // 把短信序列化到sd卡然后设置编码格式
                xmlSerializer.setOutput(os,"utf-8");
                // standalone表示当前的xml是否是独立文件 ture表示文件独立。yes
                xmlSerializer.startDocument("utf-8",true);
                // 设置开始的节点 第一个参数是命名空间。第二个参数是节点的名字
                xmlSerializer.startTag(null,"smss");
                //设置smss节点上面的属性值 第二个参数是名字。第三个参数是值
                xmlSerializer.attribute(null,"size",String.valueOf(count));
                // 游标往下面进行移动
                while (cursor.moveToNext()){
                    System.out.println("------------------------------------");
                    System.out.println("address="+cursor.getString(0));
                    System.out.println("date="+cursor.getString(1));
                    System.out.println("type="+cursor.getString(2));
                    System.out.println("body"+cursor.getString(3));

                    xmlSerializer.startTag(null,"sms");

                    xmlSerializer.startTag(null,"address");

                    // 设置文本的内容
                    xmlSerializer.text(cursor.getString(0));

                    xmlSerializer.endTag(null,"address");

                    xmlSerializer.startTag(null,"date");

                    // 设置文本的内容
                    xmlSerializer.text(cursor.getString(1));

                    xmlSerializer.endTag(null,"date");

                    xmlSerializer.startTag(null,"type");

                    // 设置文本的内容
                    xmlSerializer.text(cursor.getString(2));

                    xmlSerializer.endTag(null,"type");



                    xmlSerializer.startTag(null,"body");
                    //读取短信的内容
                    /**
                     * 加密：第一个参数表示加密种子(密钥)
                     *     第二个参数表示加密的内容
                     */
                    // 设置文本的内容
                    xmlSerializer.text(Crypto.encrypt("123",cursor.getString(3)));
                    xmlSerializer.endTag(null,"body");

                    xmlSerializer.endTag(null,"sms");

                    //序列化完一条短信之后就需要++
                    process++;

                    callback.onBackUpSms(process);

                    SystemClock.sleep(200);
                }
                cursor.close();
                xmlSerializer.endTag(null,"smss");
                xmlSerializer.endDocument();
                os.flush();
                os.close();

                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }


        }

        return false;
    }
}
