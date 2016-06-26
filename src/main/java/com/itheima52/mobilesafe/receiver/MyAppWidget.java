package com.itheima52.mobilesafe.receiver;

import android.appwidget.AppWidgetProvider;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.itheima52.mobilesafe.service.KillProcessService;
import com.itheima52.mobilesafe.service.KillProcessWidgetService;

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
 * 描     述 ：桌面小控件功能
 */
public class MyAppWidget extends AppWidgetProvider {

    /**
     * 第一次创建的时候才会调用当前的生命周期的方法
     *
     * 当前的广播的生命周期只有10秒钟。
     * 不能做耗时的操作
     */
    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
        System.out.println("onEnabled");
        Intent intent = new Intent(context, KillProcessWidgetService.class);
        context.startService(intent);
    }


    /**
     * 当桌面上面所有的桌面小控件都删除的时候才调用当前这个方法
     */
    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
        Intent intent = new Intent(context, KillProcessWidgetService.class);
        context.stopService(intent);
        System.out.println("onDisabled");
    }
}
