package com.example.iweibo.ui;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.iweibo.R;
import com.example.iweibo.adapter.UserlistAdapter;
import com.example.iweibo.bean.Task;
import com.example.iweibo.db.UserInfo;
import com.example.iweibo.dbservices.UserInfoServices;
import com.example.iweibo.logic.MainService;
import com.example.iweibo.util.SharedPreferencesUtil;
import com.example.iweibo.util.TipDialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 登录Activity
 * 
 * @author Administrator
 *
 */
public class LoginActivity extends Activity implements IweiboActivity {

	private Button button_userselect;
	private UserInfoServices services;
	private List<UserInfo> users;
	private Button button_adduser;
	private ImageView imageView;
	private TextView textView;
	private UserInfo nowuser;// 当前登录用户
	private Button btn_login;// 登录按钮
	private ProgressDialog progressDialog = null;

	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.login);
		imageView = (ImageView) this.findViewById(R.id.img_user_head);
		textView = (TextView) this.findViewById(R.id.user_text);
		button_userselect = (Button) this.findViewById(R.id.list_user);
		button_adduser = (Button) this.findViewById(R.id.btn_account);
		btn_login = (Button) this.findViewById(R.id.btn_login);
		/**
		 * 启动主服务
		 */
		Intent intent0 = new Intent(this, MainService.class);
		startService(intent0);
		MainService.addActivity(this);// 将自己的activity实例加到Mainservice中
		nowuser = SharedPreferencesUtil.getLoginUser(this);// 获取当前登录的用户
		if (nowuser != null) {
			showDialog();
			imageView.setImageDrawable(nowuser.getUserIcon());
			textView.setText(nowuser.getUserName());
			newTask();
		} else {
			init();
		}
	}

	public void init() {
		services = new UserInfoServices(this);
		users = services.getAllUsers();
		button_userselect.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				/**
				 * UI界面的对话框显示
				 */
				View view = View.inflate(LoginActivity.this, R.layout.user_listdialog, null);
				final Dialog dialog = new Dialog(LoginActivity.this, R.style.userlist_dialog);
				dialog.setContentView(view);
				dialog.show();
				ListView listView = (ListView) view.findViewById(R.id.user_listView);
				UserlistAdapter userlistAdapter = new UserlistAdapter(LoginActivity.this, users);
				listView.setAdapter(userlistAdapter);
				listView.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> arg0, View view, int arg2, long id) {
						ImageView iView = (ImageView) view.findViewById(R.id.head_img);
						TextView tView = (TextView) view.findViewById(R.id.show_username);
						imageView.setImageDrawable(iView.getDrawable());
						textView.setText(tView.getText());
						nowuser = services.getUserInfoByUserId(id + "");
						dialog.dismiss();
					}
				});
			}
		});
		button_adduser.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				Intent intent = new Intent(LoginActivity.this, OauthActivity.class);
				startActivity(intent);
			}
		});
		btn_login.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				if (users == null) {
					Intent intent = new Intent(LoginActivity.this, OauthActivity.class);
					startActivity(intent);
				}else if (users != null && (textView.getText().equals("") || textView.getText() == null)) {
				     TipDialog.showTipDialog(LoginActivity.this,"请选择用户!!!","确定");
		            }  					
				else{
					showDialog();
					SharedPreferencesUtil.saveLoginUser(LoginActivity.this, nowuser);
					newTask();
				}
			}
		});
	}

	@Override
	public void refresh(Object... objects) {
		if (progressDialog != null) {
			progressDialog.dismiss();
		}
		String s = (String) objects[0];
		if (s != null && s.equals("AccessToken过期,请重新授权")) {
			if (progressDialog == null) {
				progressDialog = new ProgressDialog(LoginActivity.this);
			}
			progressDialog.setMessage(getString(R.string.expire_in));
			progressDialog.show();
			Intent intent = new Intent(this, WebViewActivity.class);
			startActivity(intent);
		} else
			jumpTo();
	}

	public void jumpTo() {
		Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
		startActivity(intent);

	}

	public void showDialog() {
		if (progressDialog == null) {
			progressDialog = new ProgressDialog(this);
		}
		progressDialog.setMessage(getString(R.string.logining));
		progressDialog.show();
	}

	public void newTask() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("nowuser", nowuser);
		Task task = new Task(Task.WEIBO_LOGIN, map);
		MainService.newTask(task);
	}
}
