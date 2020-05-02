package com.example.iweibo.ui;

import java.net.MalformedURLException;
import java.net.URL;

import com.example.iweibo.R;
import com.example.iweibo.bean.Task;
import com.example.iweibo.db.UserInfo;
import com.example.iweibo.dbservices.UserInfoServices;
import com.example.iweibo.logic.MainService;
import com.example.iweibo.util.ImageInfoExchange;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class AccessActivity extends Activity implements IweiboActivity {
	private ProgressDialog pDialog = null;
	private UserInfoServices userInfoServices = null;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.auto_dialog);
		System.out.println("准备执行");
		init();
		Task task = new Task(Task.GET_AccessToken, null);
		MainService.newTask(task);
		MainService.addActivity(this);
	}

	public void init() {
		if (pDialog == null) {
			pDialog = new ProgressDialog(this);
		}
		pDialog.setMessage(getString(R.string.get_access_token_ing));
		pDialog.show();
		userInfoServices = new UserInfoServices(this);
	}

	public void refresh(Object... objects) {
		if (pDialog != null) {
			pDialog.dismiss();
		}
		System.out.println("执行结束");
		final Handler h = new Handler() {
			public void handleMessage(Message msg) {
				if (msg.what == 1) {
					if (pDialog == null) {
						pDialog = new ProgressDialog(AccessActivity.this);
					}
					pDialog.setMessage(getString(R.string.loading));
					pDialog.show();
					Intent intent = new Intent(AccessActivity.this, LoginActivity.class);
					startActivity(intent);
				}
			}
		};
		String string = (String) objects[0];
		final String[] splite = string.split("\t");
		System.out.println("User信息" + splite[0] + splite[1] + splite[2] + splite[3]);
		UserInfo userInfo = userInfoServices.getUserInfoByUserId(splite[1]);
		if (userInfo == null) {
			new Thread(new Runnable() {
				public void run() {
					try {
						Drawable drawable = ImageInfoExchange.UrlToDrawble(new URL(splite[3]));
						UserInfo user = new UserInfo(splite[1], splite[2], splite[0], "1", drawable);
						userInfoServices.inserUser(user);
						System.out.println("插入成功");
						h.sendEmptyMessage(1);
					} catch (MalformedURLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}).start();
		} else {
			userInfoServices.updateUserInfo(splite[1], splite[0]);
			System.out.println("更新完成");
			h.sendEmptyMessage(1);
		}
	}
}
