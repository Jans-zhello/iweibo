package com.example.iweibo.adapter;

import java.util.List;

import com.example.iweibo.R;
import com.example.iweibo.db.UserInfo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
/**
 * 
 * 绑定user列表数据源
 * @author Administrator
 *
 */
public class UserlistAdapter extends BaseAdapter {
    private Context context;
    private List<UserInfo> userinfo;	 
	public UserlistAdapter(Context context, List<UserInfo> userinfo) {
	super();
	this.context = context;
	this.userinfo = userinfo;
  }
	public int getCount() {
		return userinfo==null ? 0:userinfo.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return userinfo==null ? null:userinfo.get(position);
	}

	@Override
	public long getItemId(int position) {
		return Long.parseLong(userinfo.get(position).getUserId());
	}

	@Override
	public View getView(int position, View convertview, ViewGroup parent) {
		View v = convertview;
		if (v == null) {
			v = LayoutInflater.from(context).inflate(R.layout.userlisttemplate,null);//加载自己的布局文件
			ImageView imageView = (ImageView) v.findViewById(R.id.head_img);
			TextView textView = (TextView) v.findViewById(R.id.show_username);
			UserInfo user = userinfo.get(position);
			imageView.setImageDrawable(user.getUserIcon());
			textView.setText(user.getUserName()); 
		}
		return v;
	}

}
