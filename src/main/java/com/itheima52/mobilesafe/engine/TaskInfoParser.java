package com.itheima52.mobilesafe.engine;


import java.util.ArrayList;
import java.util.List;

import android.R.integer;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
import android.os.Debug.MemoryInfo;
import android.text.format.Formatter;

import com.itheima52.mobilesafe.R;
import com.itheima52.mobilesafe.bean.TaskInfo;

/**
 * ============================================================
 * <p/>
 * 版     权 ：  2016
 * <p/>
 * 作     者  :  崔桂林
 * <p/>
 * 版     本 ： 1.0
 * <p/>
 * 创 建日期 ： 2016/5/26  7:29
 * <p/>
 * 描     述 ：
 * /*
 * _ooOoo_
 * o8888888o
 * 88" . "88
 * (| -_- |)
 * O\  =  /O
 * ____/`---'\____
 * .'  \\|     |//  `.
 * /  \\|||  :  |||//  \
 * /  _||||| -:- |||||-  \
 * |   | \\\  -  /// |   |
 * | \_|  ''\---/''  |   |
 * \  .-\__  `-`  ___/-. /
 * ___`. .'  /--.--\  `. . __
 * ."" '<  `.___\_<|>_/___.'  >'"".
 * | | :  `- \`.;`\ _ /`;.`/ - ` : | |
 * \  \ `-.   \_ __\ /__ _/   .-` /  /
 * ======`-.____`-.___\_____/___.-`____.-'======
 * `=---='
 * ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
 * 佛祖保佑       永无BUG
 */
public class TaskInfoParser {

    public  static List<TaskInfo> getTaskInfos(Context context){

        List<TaskInfo> taskInfos = new ArrayList<TaskInfo>();
        //获取到进程管理器
        ActivityManager actitvityManager = (ActivityManager) context.getSystemService(context.ACTIVITY_SERVICE);
        //获取包管理器
        PackageManager packageManager = context.getPackageManager();
        //获取到手机上所有运行的进程
        List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = actitvityManager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo runningAppProcessInfo : runningAppProcesses) {
            TaskInfo taskInfo = new TaskInfo();
            //获取到进程的名称
            String processName = runningAppProcessInfo.processName;
            taskInfo.setPackageName(processName);

            try {
                // 获取到内存基本信息
                /**
                 * 这个里面一共只有一个数据
                 */
                MemoryInfo[] MemoryInfo = actitvityManager.getProcessMemoryInfo(new int[]{runningAppProcessInfo.pid});
                // Dirty弄脏
                // 获取到总共弄脏多少内存(当前应用程序占用多少内存)
                int totalPrivateDirty = MemoryInfo[0].getTotalPrivateDirty() * 1024;
                taskInfo.setMemorySize(totalPrivateDirty);

                PackageInfo packageInfo = packageManager.getPackageInfo(processName, 0);
                // /获取到图片
                Drawable icon = packageInfo.applicationInfo.loadIcon(packageManager);
                taskInfo.setIcon(icon);

                // 获取到应用的名字
                String appName = packageInfo.applicationInfo.loadLabel(packageManager).toString();
                taskInfo.setAppName(appName);
                System.out.println("-------------------");
                System.out.println("processName="+processName);
                System.out.println("appName="+appName);

                //获取到当前应用程序的标记
                int flags = packageInfo.applicationInfo.flags;
                if((flags&ApplicationInfo.FLAG_SYSTEM)!=0){
                    //系统应用
                    taskInfo.setUserApp(false);
                }else{
                    //用户程序
                    taskInfo.setUserApp(true);
                }


            } catch (Exception e) {
                e.printStackTrace();
                // 系统核心库里面有些系统没有图标。必须给一个默认的图标
                taskInfo.setAppName(processName);
                taskInfo.setIcon(context.getResources().getDrawable(R.drawable.ic_launcher));
            }
            taskInfos.add(taskInfo);
        }
        return taskInfos;
    }
}
