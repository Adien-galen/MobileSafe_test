package com.itheima52.mobilesafe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.text.InputType;
import android.view.View;
import android.view.View.*;
import android.widget.Button;
import android.widget.EditText;

import com.itheima52.mobilesafe.R;
import com.itheima52.mobilesafe.utils.UIUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

public class EnterPwdActivity extends Activity implements OnClickListener{

    @ViewInject(R.id.bt_0)
    private Button bt_0;
    @ViewInject(R.id.bt_1)
    private Button bt_1;
    @ViewInject(R.id.bt_2)
    private Button bt_2;
    @ViewInject(R.id.bt_3)
    private Button bt_3;
    @ViewInject(R.id.bt_4)
    private Button bt_4;
    @ViewInject(R.id.bt_5)
    private Button bt_5;
    @ViewInject(R.id.bt_6)
    private Button bt_6;
    @ViewInject(R.id.bt_7)
    private Button bt_7;
    @ViewInject(R.id.bt_8)
    private Button bt_8;
    @ViewInject(R.id.bt_9)
    private Button bt_9;
    @ViewInject(R.id.bt_clear_all)
    private Button bt_clear_all;
    @ViewInject(R.id.bt_delete)
    private Button bt_delete;

    @ViewInject(R.id.et_pwd)
    private static EditText et_pwd;

    @ViewInject(R.id.bt_ok)
    private Button bt_ok;

    private String packageName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUI();

    }

    private void initUI() {
        setContentView(R.layout.activity_enter_pwd);

        Intent intent = getIntent(); 
        if(intent!=null){
            packageName = intent.getStringExtra("packageName");
        }
        ViewUtils.inject(this);

        // 隐藏当前的键盘
        et_pwd.setInputType(InputType.TYPE_NULL);

        //清空
        bt_clear_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                et_pwd.setText("");
            }
        });

        //删除
        bt_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str = et_pwd.getText().toString();
                if(str.length()==0){
                    return;
                }
                et_pwd.setText(str.substring(0,str.length()-1));
            }
        });

        bt_0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str = et_pwd.getText().toString();
                et_pwd.setText(str+bt_0.getText());
                //把光标移动到文本最后
                String text = et_pwd.getText().toString();
                et_pwd.setSelection(text.length());

            }
        });

        bt_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str = et_pwd.getText().toString();
                et_pwd.setText(str+bt_1.getText());
                //把光标移动到文本最后
                String text = et_pwd.getText().toString();
                et_pwd.setSelection(text.length());
            }
        });

        bt_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str = et_pwd.getText().toString();
                et_pwd.setText(str+bt_2.getText());
                //把光标移动到文本最后
                String text = et_pwd.getText().toString();
                et_pwd.setSelection(text.length());
            }
        });

        bt_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str = et_pwd.getText().toString();
                et_pwd.setText(str+bt_3.getText());
                //把光标移动到文本最后
                String text = et_pwd.getText().toString();
                et_pwd.setSelection(text.length());
            }
        });

        bt_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str = et_pwd.getText().toString();
                et_pwd.setText(str+bt_4.getText());
                //把光标移动到文本最后
                String text = et_pwd.getText().toString();
                et_pwd.setSelection(text.length());
            }
        });

        bt_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str = et_pwd.getText().toString();
                et_pwd.setText(str+bt_5.getText());
                //把光标移动到文本最后
                String text = et_pwd.getText().toString();
                et_pwd.setSelection(text.length());
            }
        });

        bt_6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str = et_pwd.getText().toString();
                et_pwd.setText(str+bt_6.getText());
                //把光标移动到文本最后
                String text = et_pwd.getText().toString();
                et_pwd.setSelection(text.length());
            }
        });

        bt_7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str = et_pwd.getText().toString();
                et_pwd.setText(str+bt_7.getText());
                //把光标移动到文本最后
                String text = et_pwd.getText().toString();
                et_pwd.setSelection(text.length());
            }
        });

        bt_8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str = et_pwd.getText().toString();
                et_pwd.setText(str+bt_8.getText());
                //把光标移动到文本最后
                String text = et_pwd.getText().toString();
                et_pwd.setSelection(text.length());
            }
        });

        bt_9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str = et_pwd.getText().toString();
                et_pwd.setText(str+bt_9.getText());
                //把光标移动到文本最后
                String text = et_pwd.getText().toString();
                et_pwd.setSelection(text.length());
            }
        });

        //点击确认按钮
        bt_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String result = et_pwd.getText().toString();
                if("123".equals(result)){
                    System.out.println("输入密码正确");

                    Intent intent = new Intent();
                    // 发送广播。停止保护
                    intent.setAction("com.itheima52.mobilesafe.stopprotect");
                    // 跟狗说。现在停止保护短信
                    intent.putExtra("packageName",packageName);
                    sendBroadcast(intent);
                    finish();
                }else {
                    UIUtils.showToast(EnterPwdActivity.this,"密码错误");
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        // 当用户输入后退健 的时候。我们进入到桌面
        Intent intent = new Intent();
        intent.setAction("android.intent.action.MAIN");
        intent.addCategory("android.intent.category.HOME");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addCategory("android.intent.category.MONKEY");
        startActivity(intent);
    }

    public static void inputValue(Button bn){
        String str = et_pwd.getText().toString();
        et_pwd.setText(str+bn.getText());
        //把光标移动到文本最后
        String text = et_pwd.getText().toString();
        et_pwd.setSelection(text.length());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.bt_0:
                inputValue(bt_0);
                break;
        }
    }
}
