package com.itheima52.mobilesafe.service;

import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.IBinder;
import android.text.format.Formatter;
import android.widget.RemoteViews;

import com.itheima52.mobilesafe.R;
import com.itheima52.mobilesafe.receiver.MyAppWidget;
import com.itheima52.mobilesafe.utils.SystemInfoUtils;

import java.util.Timer;
import java.util.TimerTask;

/**
 * ============================================================
 * <p/>
 * 版     权 ：  2016
 * <p/>
 * 作     者  :  崔桂林
 * <p/>
 * 版     本 ： 1.0
 * <p/>
 * 创 建日期 ： 2016/5/29  14:38
 * <p/>
 * 描     述 ：清理桌面小控件的服务
 */
public class KillProcessWidgetService extends Service {

    private Timer timer;
    private TimerTask task;
    private AppWidgetManager appWidgetManager;
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        appWidgetManager = AppWidgetManager.getInstance(this);

        timer = new Timer();

        task = new TimerTask() {
            @Override
            public void run() {
                System.out.println("KillProcessWidgetService");
                //这个是把当前的布局文件添加进行
                /**
                 * 初始化一个远程的view
                 * Remote 远程
                 */
                RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.process_widget);

                /**
                 * 需要注意。这个里面findingviewyid这个方法
                 * 设置当前文本里面一共有多少个进程
                 */
                int processCount = SystemInfoUtils.getProcessCount(getApplicationContext());
                remoteViews.setTextViewText(R.id.process_count,"正在运行的软件:"+String.valueOf(processCount));
                //获取到当前手机上面的可用内存
                long avaiMem = SystemInfoUtils.getAvaiMem(getApplicationContext());
                remoteViews.setTextViewText(R.id.process_memory,"可用内存:"+ Formatter.formatFileSize(getApplicationContext(),avaiMem));

                Intent intent = new Intent();
                //发送一个隐式意图
                intent.setAction("com.itheima52.mobilesafe");
                PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),0,intent,0);
                //设置点击事件
                remoteViews.setOnClickPendingIntent(R.id.btn_clear,pendingIntent);
                //第一个参数表示上下文
                //第二个参数表示当前有哪一个广播进行去处理当前的桌面小控件
                ComponentName provider = new ComponentName(getApplicationContext(), MyAppWidget.class);
                appWidgetManager.updateAppWidget(provider,remoteViews);
            }
        };

        //从0开始。每隔5秒钟更新一次
        timer.schedule(task,0,5000);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
