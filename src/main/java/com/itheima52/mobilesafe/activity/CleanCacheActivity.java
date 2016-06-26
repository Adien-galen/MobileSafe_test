package com.itheima52.mobilesafe.activity;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.pm.IPackageDataObserver;
import android.content.pm.IPackageStatsObserver;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.PackageStats;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.text.format.Formatter;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.itheima52.mobilesafe.R;
import com.itheima52.mobilesafe.utils.UIUtils;


public class CleanCacheActivity extends Activity {
    private FrameLayout fl_scan_status;
    private TextView tv_scan_status;
    private ProgressBar pb;
    private ListView lv_cacheinfos;
    private List<CacheInfo> cacheInfos;
    private PackageManager pm;
    protected static final int SCAN_FINISH = 1;
    public static final int SCANING_APP = 2;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SCANING_APP://扫描应用程序中.
                    String appName = (String) msg.obj;
                    tv_scan_status.setText("正在扫描:" + appName);
                    break;
                case SCAN_FINISH: //扫描完毕
                    UIUtils.showToast(CleanCacheActivity.this, "扫描完毕");
                    fl_scan_status.setVisibility(View.GONE);
                    if (cacheInfos.size() > 0) {
                        lv_cacheinfos.setAdapter(new CleanCacheAdapter());
                    } else {
                        UIUtils.showToast(CleanCacheActivity.this, "恭喜您,手机100分,没有任何缓存");
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clean_cache);
        pm = getPackageManager();
        fl_scan_status = (FrameLayout) findViewById(R.id.fl_scan_status);
        tv_scan_status = (TextView) findViewById(R.id.tv_scan_status);
        pb = (ProgressBar) findViewById(R.id.pb);
        lv_cacheinfos = (ListView) findViewById(R.id.lv_cacheinfos);

        sacnCache();
    }

    /**
     * 扫描缓存
     */
    private void sacnCache() {
        fl_scan_status.setVisibility(View.VISIBLE);
        cacheInfos = new ArrayList<CacheInfo>();
        new Thread() {
            @Override
            public void run() {
                // 1.扫描全部应用程序的包名
                List<PackageInfo> infos = pm.getInstalledPackages(0);
                pb.setMax(infos.size());
                int progress = 0;
                for (PackageInfo info : infos) {
                    String packName = info.packageName;
                    try {
                        Method method = PackageManager.class.getMethod("getPackageSizeInfo", String.class, IPackageDataObserver.class);
                        method.invoke(pm, packName, new MyObserver());
                        Thread.sleep(50);
                        progress++;
                        pb.setProgress(progress);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                //集合的数据就准备好了. 通知界面更新
                Message msg = Message.obtain();
                msg.what = SCAN_FINISH;
                handler.sendMessage(msg);
            }
        }.start();
    }

    private class MyObserver extends IPackageStatsObserver.Stub {
        @Override
        public void onGetStatsCompleted(PackageStats pStats, boolean succeeded) throws RemoteException {
            Message msg = Message.obtain();
            msg.what = SCANING_APP;
            try {
                String appName = pm.getPackageInfo(pStats.packageName, 0).applicationInfo.loadLabel(pm).toString();
                msg.obj = appName;
                handler.sendMessage(msg);
                if (pStats.cacheSize > 0) {
                    CacheInfo cacheInfo = new CacheInfo();
                    cacheInfo.cache = pStats.cacheSize;
                    cacheInfo.icon = pm.getPackageInfo(pStats.packageName, 0).applicationInfo.loadIcon(pm);
                    cacheInfo.appName = appName;
                    cacheInfo.packName = pStats.packageName;
                    cacheInfos.add(cacheInfo);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    class CacheInfo {
        long cache;
        String packName;
        Drawable icon;
        String appName;
    }

    public class CleanCacheAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return cacheInfos.size();
        }

        @Override
        public Object getItem(int position) {
            return cacheInfos.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View view;
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                view = View.inflate(CleanCacheActivity.this, R.layout.item_cacheinfo, null);
                holder.iv_appicon = (ImageView) view.findViewById(R.id.iv_appicon);
                holder.iv_delete_cache = (ImageView) view.findViewById(R.id.iv_delete_cache);
                holder.tv_appname = (TextView) view.findViewById(R.id.tv_appname);
                holder.tv_appcachesize = (TextView) view.findViewById(R.id.tv_appcachesize);
                view.setTag(holder);
            } else {
                view = convertView;
                holder = (ViewHolder) view.getTag();
            }

            holder.iv_appicon.setImageDrawable(cacheInfos.get(position).icon);
            holder.tv_appname.setText(cacheInfos.get(position).appName);
            holder.tv_appcachesize.setText(Formatter.formatFileSize(CleanCacheActivity.this, cacheInfos.get(position).cache));
            //点击清除
            holder.iv_delete_cache.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Method[] methods = PackageManager.class.getMethods();
                    for (Method method : methods) {
                        if ("deleteApplicationCacheFiles".equals(method.getName())) {
                            try {
                                method.invoke(pm, cacheInfos.get(position).packName, new ClearCacheObserver());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            });
            return view;
        }
    }

    static class ViewHolder {
        ImageView iv_appicon;
        ImageView iv_delete_cache;
        TextView tv_appname;
        TextView tv_appcachesize;
    }

    class ClearCacheObserver extends IPackageDataObserver.Stub {

        @Override
        public void onRemoveCompleted(String packageName, boolean succeeded) throws RemoteException {
            UIUtils.showToast(CleanCacheActivity.this, "清除状态" + succeeded);
        }
    }

    /**
     * 清理全部应用程序缓存的点击事件
     *
     * @param view
     */

    public void cleanAll(View view) {
        Method[] methods = PackageManager.class.getMethods();
        for (Method method : methods) {
            if ("freeStorageAndNotify".equals(method)) {
                try {
                    method.invoke(pm, Integer.MAX_VALUE, new ClearCacheObserver());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        sacnCache();
        return;
    }
}
