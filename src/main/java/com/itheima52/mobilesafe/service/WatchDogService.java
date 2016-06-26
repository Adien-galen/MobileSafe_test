package com.itheima52.mobilesafe.service;

import android.app.ActivityManager;
import android.app.ActivityManager.*;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.os.SystemClock;

import com.itheima52.mobilesafe.activity.EnterPwdActivity;
import com.itheima52.mobilesafe.bean.AppInfo;
import com.itheima52.mobilesafe.db.dao.AppLockDao;

import java.util.List;

public class WatchDogService extends Service {

    private AppLockDao dao;
    private ActivityManager activityManager;
    private List<String> appLockInfos;
    private WatchDogReceiver receiver;
    private boolean flag = false;

    //临时停止保护的包名
    private String tempStopProtectPackageName;

    public WatchDogService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private class WatchDogReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("com.itheima52.mobilesafe.stopprotect")) {
                //获取到停止保护的对象
                tempStopProtectPackageName = intent.getStringExtra("packageName");
            }else if(intent.getAction().equals(Intent.ACTION_SCREEN_OFF)){
                tempStopProtectPackageName = null;
                // 让狗休息
                flag = false;
            }else if(intent.getAction().equals(Intent.ACTION_SCREEN_ON)){
                //让狗继续干活
                if(flag==false){
                    startWatchDog();
                }
            }

        }
    }

    @Override
    public void onCreate() {
        super.onCreate();

        dao = new AppLockDao(this);
        appLockInfos = dao.findAll();

        //注册广播接受者
        receiver = new WatchDogReceiver();

        IntentFilter filter = new IntentFilter();
        //停止保护
        filter.addAction("com.itheima52.mobilesafe.stopprotect");

        //注册一个锁屏的广播
        /**
         * 当屏幕锁住的时候。狗就休息
         * 屏幕解锁的时候。让狗活过来
         */
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_SCREEN_ON);

        registerReceiver(receiver, filter);

        //获取到进程管理器
        activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        startWatchDog();
    }

    private void startWatchDog() {

        new Thread() {
            @Override
            public void run() {
                flag = true;
                while (flag) {
                    //由于这个狗一直在后台运行。为了避免程序阻塞。
                    //获取到当前正在运行的任务栈
                    List<RunningTaskInfo> runningTasks = activityManager.getRunningTasks(1);
                    //获取到最上面的进程
                    RunningTaskInfo taskInfo = runningTasks.get(0);
                    //获取到最顶端应用程序的包名
                    String packageName = taskInfo.topActivity.getPackageName();
//                    System.out.println(packageName);
                    //让狗休息一会
                    SystemClock.sleep(30);
                    //直接从数据库里面查找当前的数据
                    if (appLockInfos.contains(packageName)) {
                        //说明需要临时取消保护
                        //是因为用户输入了正确的密码
                        if (packageName.equals(tempStopProtectPackageName)) {

                        } else {
                            Intent intent = new Intent(WatchDogService.this, EnterPwdActivity.class);

                            //注意：如果是在服务里面往activity界面跳的话。需要设置flag
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            //停止保护的对象
                            intent.putExtra("packageName", packageName);
                            startActivity(intent);
                        }
                    }
                }

            }
        }.start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        flag = false;
        unregisterReceiver(receiver);
        receiver = null;
    }
}
