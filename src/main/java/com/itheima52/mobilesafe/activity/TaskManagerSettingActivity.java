package com.itheima52.mobilesafe.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.itheima52.mobilesafe.R;
import com.itheima52.mobilesafe.service.KillProcessService;
import com.itheima52.mobilesafe.utils.SharedPreferencesUtils;
import com.itheima52.mobilesafe.utils.SystemInfoUtils;


/**
 * 任务管理器的设置界面
 */
public class TaskManagerSettingActivity extends Activity {

    private CheckBox cb_status;
    private CheckBox cb_status_kill_process;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUI();
    }

    private void initUI() {
        setContentView(R.layout.activity_task_manager_setting);
        cb_status = (CheckBox) findViewById(R.id.cb_status);

        //设置是否选中
        cb_status.setChecked(SharedPreferencesUtils.getBoolean(this,"is_show_system",false));


        cb_status.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                SharedPreferencesUtils.saveBoolean(TaskManagerSettingActivity.this,"is_show_system",isChecked);
            }
        });


        //定时清理进程
        cb_status_kill_process = (CheckBox) findViewById(R.id.cb_status_kill_process);

        final Intent intent = new Intent(this,KillProcessService.class);

        cb_status_kill_process.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    startService(intent);
                }else {
                    stopService(intent);
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(SystemInfoUtils.isServiceRunning(this,"com.itheima52.mobilesafe.service.KillProcessService")){
            cb_status_kill_process.setChecked(true);
        }else {
            cb_status_kill_process.setChecked(false);
        }

    }
}
