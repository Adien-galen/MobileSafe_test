package com.itheima52.mobilesafe.activity;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.itheima52.mobilesafe.R;
import com.itheima52.mobilesafe.db.dao.AntivirusDao;
import com.itheima52.mobilesafe.utils.MD5Utils;

import java.util.List;

public class AntivirusActivity extends Activity {

    // 扫描开始
    protected static final int BEGING = 1;
    // 扫描中
    protected static final int SCANING = 2;
    // 扫描结束
    protected static final int FINISH = 3;
    private Message message;
    private TextView tv_init_virus;
    private ProgressBar pb;
    private ImageView iv_scanning;
    private LinearLayout ll_content;
    private ScrollView scrollView;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case BEGING:
                    tv_init_virus.setText("初始化四核引擎");
                    break;

                case SCANING:
                    // 病毒扫描中：
                    TextView child = new TextView(AntivirusActivity.this);
                    ScanInfo scanInfo = (ScanInfo) msg.obj;
                    // 如果为true表示有病毒
                    if(scanInfo.desc){
                        child.setTextColor(Color.RED);
                        child.setText(scanInfo.appName+": 有病毒！");
                    }else {
                        child.setTextColor(Color.BLACK);
                        child.setText(scanInfo.appName+": 扫描安全");
                    }

                    ll_content.addView(child,0);
                    //自动滚动
                    scrollView.post(new Runnable() {
                        @Override
                        public void run() {

                            //一直往下面进行滚动
                            scrollView.fullScroll(scrollView.FOCUS_DOWN);
                        }
                    });
                    System.out.println(scanInfo.appName + "扫描安全");
                    break;
                case FINISH:
                    // 当扫描结束的时候。停止动画
                    iv_scanning.clearAnimation();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initUI();
        initData();
    }

    private void initData() {
        new Thread(){
            @Override
            public void run() {
                message = Message.obtain();
                message.what = BEGING;

                PackageManager packageManager = getPackageManager();
                // 获取到所有安装的应用程序
                List<PackageInfo> installedPackages = packageManager.getInstalledPackages(0);
                // 返回手机上面安装了多少个应用程序
                int size = installedPackages.size();
                // 设置进度条的最大值
                pb.setMax(size);

                int progress = 0;

                for (PackageInfo packageInfo:installedPackages) {
                    ScanInfo scanInfo = new ScanInfo();

                    // 获取到当前手机上面的app的名字
                    String appName = packageInfo.applicationInfo.loadLabel(packageManager).toString();
                    scanInfo.appName = appName;

                    String packageName = packageInfo.applicationInfo.packageName;
                    scanInfo.packageName = packageName;

                    // 首先需要获取到每个应用程序的目录
                    String sourceDir = packageInfo.applicationInfo.sourceDir;
                    // 获取到文件的md5
                    String md5 = MD5Utils.getFileMd5(sourceDir);
                    // 判断当前的文件是否是病毒数据库里面
                    String desc = AntivirusDao.checkFileVirus(md5);

                    System.out.println("-------------------------");

                    System.out.println(appName);

                    System.out.println(md5);
                    //					My Application1
//                    05-29 11:41:33.509 17234-17432/com.itheima52.mobilesafe I/System.out: 39f20dd2ca2ec9a6d72f08b5808b7b33



//					B7DA3864FD19C0B2390C9719E812E649
                    // 如果当前的描述信息等于null说明没有病毒
                    if(desc==null){
                        scanInfo.desc = false;
                    }else {
                        scanInfo.desc = true;
                    }

                    progress++;

                    SystemClock.sleep(5);

                    pb.setProgress(progress);
                    message =Message.obtain();
                    message.what = SCANING;
                    message.obj = scanInfo;
                    handler.sendMessage(message);
                }
                message =Message.obtain();
                message.what = FINISH;
                handler.sendMessage(message);
            };
        }.start();
    }

    static class ScanInfo{
        boolean desc;
        String appName;
        String packageName;
    }

    private void initUI() {
        setContentView(R.layout.activity_antivirus);
        iv_scanning = (ImageView) findViewById(R.id.iv_scanning);
        tv_init_virus = (TextView) findViewById(R.id.tv_init_virus);
        pb = (ProgressBar) findViewById(R.id.progressBar1);
        ll_content = (LinearLayout) findViewById(R.id.ll_content);
        scrollView = (ScrollView) findViewById(R.id.scrollView);

        /**
         * 第一个参数表示开始的角度 第二个参数表示结束的角度 第三个参数表示参照自己 初始化旋转动画
         */
        RotateAnimation animation = new RotateAnimation(0,360, Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        //设置动画的时间
        animation.setDuration(5000);
        //设置动画无限循环
        animation.setRepeatCount(Animation.INFINITE);

        iv_scanning.startAnimation(animation);
    }

}
