package com.itheima52.mobilesafe.service;

import android.app.ActivityManager;
import android.app.ActivityManager.*;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class KillProcessService extends Service {
    public KillProcessService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
                List<RunningAppProcessInfo> runningAppProcesses = activityManager.getRunningAppProcesses();
                for (RunningAppProcessInfo runningApp:runningAppProcesses) {
                    activityManager.killBackgroundProcesses(runningApp.processName);
                }
            }
        };

        timer.schedule(task,0,4*60*60*1000);
    }
}
