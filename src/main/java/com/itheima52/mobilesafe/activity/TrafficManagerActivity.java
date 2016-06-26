package com.itheima52.mobilesafe.activity;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.TrafficStats;
import android.os.Bundle;
import android.app.Activity;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.itheima52.mobilesafe.R;

import java.util.List;

public class TrafficManagerActivity extends Activity {

    private List<ApplicationInfo> applications;
    private PackageManager pm;
    private ListView lv_traffic_info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUI();

    }

    private void initUI() {

        setContentView(R.layout.activity_traffic_manager);

        lv_traffic_info = (ListView) findViewById(R.id.lv_traffic_info);

        //1.获取一个包管理器。
        pm = getPackageManager();
        applications = pm.getInstalledApplications(0);
        lv_traffic_info.setAdapter(new TrafficInfoAdapter());


    }

    private class TrafficInfoAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return applications.size();
        }

        @Override
        public Object getItem(int position) {
            return applications.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view;
            final ViewHolder holder;
            if(convertView==null){
                holder = new ViewHolder();
                view = View.inflate(TrafficManagerActivity.this,R.layout.item_traffic_info,null);
                holder.iv_icon = (ImageView) view.findViewById(R.id.iv_icon);
                holder.tv_name = (TextView) view.findViewById(R.id.tv_name);
                holder.tv_triffic_size = (TextView) view.findViewById(R.id.tv_triffic_size);
                holder.tv_send_size = (TextView) view.findViewById(R.id.tv_send_size);
                holder.tv_rec_size = (TextView) view.findViewById(R.id.tv_rec_size);

                view.setTag(holder);
            }else {
                view = convertView;
                holder = (ViewHolder) view.getTag();
            }

            ApplicationInfo applicationInfo = applications.get(position);
            holder.iv_icon.setImageDrawable(applicationInfo.loadIcon(pm));
            holder.tv_name.setText(applicationInfo.loadLabel(pm));

            int uid = applicationInfo.uid;    // 获得软件uid
            //proc/uid_stat/10086
            long tx = TrafficStats.getUidTxBytes(uid);//发送的 上传的流量byte
            System.out.println("上传 "+tx);
            holder.tv_send_size.setText("上传 "+ Formatter.formatFileSize(TrafficManagerActivity.this,tx));

            long rx = TrafficStats.getUidRxBytes(uid);//下载的流量 byte
            System.out.println("下载 "+rx);   //方法返回值 -1 代表的是应用程序没有产生流量 或者操作系统不支持流量统计

            holder.tv_rec_size.setText("下载 "+ Formatter.formatFileSize(TrafficManagerActivity.this,rx));
            //总流量
            holder.tv_triffic_size.setText(Formatter.formatFileSize(TrafficManagerActivity.this,tx+rx));
            return view;
        }
    }

    static class ViewHolder{
        ImageView iv_icon;
        TextView tv_name;
        TextView tv_triffic_size;
        TextView tv_send_size;
        TextView tv_rec_size;
    }
}
