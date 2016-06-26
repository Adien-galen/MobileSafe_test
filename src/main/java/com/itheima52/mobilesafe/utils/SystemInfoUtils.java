package com.itheima52.mobilesafe.utils;

import android.app.ActivityManager;
import android.app.ActivityManager.*;
import android.content.Context;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
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
 * 创 建日期 ： 2016/5/25  7:41
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
public class SystemInfoUtils {


    /**
     * 判断一个服务是否处于运行状态
     *
     * @param context
     *            上下文
     * @return
     */

    public static boolean isServiceRunning(Context context,String serviceName){
        ActivityManager activityManager = (ActivityManager) context.getSystemService(context.ACTIVITY_SERVICE);
        List<RunningServiceInfo> runningServices = activityManager.getRunningServices(200);
        for (RunningServiceInfo runningService: runningServices) {
            String serviceClassName =runningService.service.getClassName();
            if(serviceName.equals(serviceClassName)){
                return true;
            }
        }
        return false;
    }

    /**
     * 返回进程的总个数
     *
     * @param context
     * @return
     */
    public static int getProcessCount(Context context){
        // 得到进程管理者
        ActivityManager activityManager = (ActivityManager) context.getSystemService(context.ACTIVITY_SERVICE);
        // 获取到当前手机上面所有运行的进程
        List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = activityManager.getRunningAppProcesses();
        // 获取手机上面一共有多少个进程
        int size = runningAppProcesses.size();
        return size;
    }


    // 获取到剩余内存
    public static long getAvaiMem(Context context){
        ActivityManager activityManager = (ActivityManager) context.getSystemService(context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        activityManager.getMemoryInfo(memoryInfo);
        return memoryInfo.availMem;
    }


    // 获取到总内存
    public static long getTotalMem(){
        // 获取到总内存
		/*
		 * 这个地方不能直接跑到低版本的手机上面 MemTotal: 344740 kB "/proc/meminfo"
		 */
        try {
            // /proc/meminfo 配置文件的路径
            FileInputStream os = new FileInputStream(new File("/proc/meminfo"));
            BufferedReader reader = new BufferedReader(new InputStreamReader(os));
            String line = reader.readLine();
            StringBuffer sb = new StringBuffer();
            for (char c:line.toCharArray()) {
                if(c>='0'&&c<='9'){
                    sb.append(c);
                }
            }
            return Long.parseLong(sb.toString())*1024;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}
