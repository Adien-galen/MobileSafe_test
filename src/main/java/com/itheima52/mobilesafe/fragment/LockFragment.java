package com.itheima52.mobilesafe.fragment;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
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
import com.lidroid.xutils.DbUtils.DaoConfig;

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
public class LockFragment extends Fragment {
    private List<AppInfo> appInfos;
    private List<AppInfo> lockInfos;
    private AppLockDao dao;
    private LockAdapter adapter;
    private TextView tv_lock;
    private ListView list_view;

	/*
	 * 类似activity里面的setContentView
	 */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.item_lock_fragment, null);
        tv_lock = (TextView) view.findViewById(R.id.tv_lock);
        list_view= (ListView) view.findViewById(R.id.list_view);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        //拿到所有的应用程序
        appInfos= AppInfos.getAppInfos(getActivity());

        //初始化一个加锁的集合
        lockInfos= new ArrayList<AppInfo>();
        dao = new AppLockDao(getActivity());
        for (AppInfo appInfo:appInfos) {
            //如果能找到当前的包名说明在程序锁的数据库里面
            if(dao.find(appInfo.getApkPackageName())){
                lockInfos.add(appInfo);
            }
        }

        adapter= new LockAdapter();
        list_view.setAdapter(adapter);
    }

    public class LockAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            tv_lock.setText("已加锁"+lockInfos.size()+"个");
            return lockInfos.size();
        }

        @Override
        public Object getItem(int position) {
            return lockInfos.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final View view;
            ViewHolder holder=null;

            if(convertView==null){
                holder = new ViewHolder();
                view = View.inflate(getActivity(),R.layout.item_lock,null);
                holder.iv_icon = (ImageView) view.findViewById(R.id.iv_icon);
                holder.tv_name = (TextView) view.findViewById(R.id.tv_name);
                holder.iv_lock = (ImageView) view.findViewById(R.id.iv_lock);

                view.setTag(holder);
            }else {
                view = convertView;
                //之前写错成view.getTag()，没有赋值给holder,导致删除一个item后再刷新时会报错
                holder= (ViewHolder) view.getTag();
            }

            final AppInfo lockInfo = lockInfos.get(position);
            holder.iv_icon.setImageDrawable(lockInfo.getIcon());
            holder.tv_name.setText(lockInfo.getApkName());

            holder.iv_lock.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    TranslateAnimation animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF,0,Animation.RELATIVE_TO_SELF,-1.0f,
                            Animation.RELATIVE_TO_SELF,0,Animation.RELATIVE_TO_SELF,0);

                    animation.setDuration(2000);
                    view.startAnimation(animation);

                    new Thread(){
                        public void run() {
                            SystemClock.sleep(2000);
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    dao.delete(lockInfo.getApkPackageName());
                                    lockInfos.remove(position);
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

    static class ViewHolder{
        ImageView iv_icon;
        TextView tv_name;
        ImageView iv_lock;
    }
}
