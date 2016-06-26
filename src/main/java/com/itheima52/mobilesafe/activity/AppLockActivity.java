package com.itheima52.mobilesafe.activity;


import android.os.Bundle;
import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.*;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.itheima52.mobilesafe.R;
import com.itheima52.mobilesafe.fragment.LockFragment;
import com.itheima52.mobilesafe.fragment.UnLockFragment;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

public class AppLockActivity extends FragmentActivity implements OnClickListener {

    private FrameLayout fl_content;
    private TextView tv_unlock;
    private TextView tv_lock;

    private FragmentManager fragmentManager;
    private LockFragment lockFragment;
    private UnLockFragment unLockFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUI();
    }

    private void initUI() {
        setContentView(R.layout.activity_app_lock);

        fl_content = (FrameLayout) findViewById(R.id.fl_content);

        tv_unlock = (TextView) findViewById(R.id.tv_unlock);

        tv_lock = (TextView) findViewById(R.id.tv_lock);

        tv_unlock.setOnClickListener(this);
        tv_lock.setOnClickListener(this);

        //获取到fragment的管理者,getSupportFragmentManager支持4.0以下版本
        fragmentManager = getSupportFragmentManager();
        //开启事务
        FragmentTransaction mTransaction = fragmentManager.beginTransaction();

        lockFragment = new LockFragment();
        unLockFragment = new UnLockFragment();

        /**
         * 替换界面
         * 1 需要替换的界面的id
         * 2具体指某一个fragment的对象
         *  注意：要commit
         */
        mTransaction.replace(R.id.fl_content, unLockFragment).commit();

    }

    @Override
    public void onClick(View view) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        switch (view.getId()){
            case R.id.tv_unlock:
                tv_unlock.setBackgroundResource(R.drawable.tab_left_pressed);
                tv_lock.setBackgroundResource(R.drawable.tab_right_default);
                transaction.replace(R.id.fl_content,unLockFragment);
                System.out.println("切换到lockFragment");
                break;
            case R.id.tv_lock:
                tv_unlock.setBackgroundResource(R.drawable.tab_left_default);
                tv_lock.setBackgroundResource(R.drawable.tab_right_pressed);
                transaction.replace(R.id.fl_content,lockFragment);
                System.out.println("切换到unLockFragment");
                break;
        }
        transaction.commit();
    }
}
