package com.itheima52.mobilesafe.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.itheima52.mobilesafe.R;
import com.itheima52.mobilesafe.utils.SmsUtils;
import com.itheima52.mobilesafe.utils.UIUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import net.youmi.android.listener.Interface_ActivityListener;
import net.youmi.android.offers.OffersManager;

/**
 * 高级工具
 *
 * 
 */
public class AToolsActivity extends Activity {
	@ViewInject(R.id.progressBar1)
	private ProgressBar progressBar1;

	private ProgressDialog pd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_atools);
		ViewUtils.inject(this);
	}

	/**
	 * 归属地查询
	 * 
	 * @param view
	 */
	public void numberAddressQuery(View view) {
		startActivity(new Intent(this, AddressActivity.class));
	}

	/**
	 * 程序锁
	 * @param view
	 */
	public void appLock(View view){
		startActivity(new Intent(this,AppLockActivity.class));
	}

	/**
	 * 备份短信
	 * @param view
	 */
	public void backUpsms(View view){
		//初始化一个进度条的对话框
		pd = new ProgressDialog(AToolsActivity.this);
		pd.setTitle("提示");
		pd.setMessage("稍安勿躁。正在备份。你等着吧。。");
		pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		pd.show();

		new Thread(){
			@Override
			public void run() {
				boolean result = SmsUtils.backUp(AToolsActivity.this, new SmsUtils.BackUpCallBackSms() {
					@Override
					public void befor(int count) {
						pd.setMax(count);
						progressBar1.setMax(count);
					}

					@Override
					public void onBackUpSms(int process) {
						pd.setProgress(process);
						progressBar1.setProgress(process);
					}
				});

				if(result){
					//安全弹吐司的方法
					UIUtils.showToast(AToolsActivity.this,"备份成功");
				}else{
					UIUtils.showToast(AToolsActivity.this,"备份失败");
				}
				pd.dismiss();
			}
		}.start();

	}

	/**
	 * 推荐应用（添加广告）
	 * @param view
	 */
	public void addads(View view){
		//直接打开全屏积分墙，并且监听积分墙退出的事件onDestory
		OffersManager.getInstance(this).showOffersWall(
				new Interface_ActivityListener() {

					/**
					 * 但积分墙销毁的时候，即积分墙的Activity调用了onDestory的时候回调
					 */
					@Override
					public void onActivityDestroy(Context context) {
						Toast.makeText(context, "全屏积分墙退出了", Toast.LENGTH_SHORT).show();
					}
				});
	}
}
