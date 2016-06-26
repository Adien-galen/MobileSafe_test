package com.itheima52.mobilesafe.fragment;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.itheima52.mobilesafe.R;
import com.itheima52.mobilesafe.bean.AppInfo;
import com.itheima52.mobilesafe.db.dao.AppLockDao;
import com.itheima52.mobilesafe.engine.AppInfos;


/**
 * ============================================================
 * <p/>
 * 版     权 ：  2016
 * <p/>
 * 作     者  :  崔桂林
 * <p/>
 * 版     本 ： 1.0
 * <p/>
 * 创 建日期 ： 2016/5/30  7:49
 * <p/>
 * 描     述 ：
 */
public class UnLockFragment extends Fragment {
    private ListView list_View;
    private TextView tv_unlock;
    private UnLockAdapter adapter;
    private List<AppInfo> appInofs;
    private List<AppInfo> unLockInfos;
    private AppLockDao dao;


	/*
     * 类似activity里面的setContentView
	 */

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.item_unlock_fragment, null);
        list_View = (ListView) view.findViewById(R.id.list_View);
        tv_unlock = (TextView) view.findViewById(R.id.tv_unlock);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        appInofs = AppInfos.getAppInfos(getActivity());
        // 获取到程序锁的dao
        dao = new AppLockDao(getActivity());
        // 初始化一个没有加锁的集合
        unLockInfos = new ArrayList<AppInfo>();

        for (AppInfo appInfo : appInofs) {
            // 判断当前的应用是否在程序所的数据里面
            if (dao.find(appInfo.getApkPackageName())) {

            } else {
                // 如果查询不到说明没有在程序锁的数据库里面
                unLockInfos.add(appInfo);
            }
        }

        adapter = new UnLockAdapter();
        list_View.setAdapter(adapter);
    }

    public class UnLockAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            tv_unlock.setText("未加锁：" + unLockInfos.size() + "个");
            return unLockInfos.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, final ViewGroup parent) {
            final View view;
            ViewHolder holder = null;
            final AppInfo appInfo;
            if (convertView == null) {
                holder = new ViewHolder();
                view = View.inflate(getActivity(), R.layout.item_unlock, null);
                holder.iv_icon = (ImageView) view.findViewById(R.id.iv_icon);
                holder.tv_name = (TextView) view.findViewById(R.id.tv_name);
                holder.iv_unlock = (ImageView) view.findViewById(R.id.iv_unlock);
                view.setTag(holder);
            } else {
                view = convertView;
                holder = (ViewHolder) view.getTag();
            }

            // 获取到当前的对象
            appInfo = unLockInfos.get(position);
            holder.iv_icon.setImageDrawable(appInfo.getIcon());
            holder.tv_name.setText(appInfo.getApkName());

            holder.iv_unlock.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 初始化一个位移动画
                    TranslateAnimation animation = new TranslateAnimation(
                            Animation.RELATIVE_TO_SELF, 0,
                            Animation.RELATIVE_TO_SELF, 1.0f,
                            Animation.RELATIVE_TO_SELF, 0,
                            Animation.RELATIVE_TO_SELF, 0);
                    // 设置动画时间
                    animation.setDuration(5000);
                    // 开始动画
                    view.startAnimation(animation);

                    new Thread() {
                        @Override
                        public void run() {
                            SystemClock.sleep(2000);

                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    // 添加到数据库里面
                                    dao.add(appInfo.getApkPackageName());
                                    // 从当前的页面移除对象
                                    unLockInfos.remove(position);
                                    // 刷新界面
                                    adapter.notifyDataSetChanged();
                                }
                            });
                        }
                    }.start();
                }
            });

            return view;
        }
    }

    static class ViewHolder {
        ImageView iv_icon;
        TextView tv_name;
        ImageView iv_unlock;
    }
}
