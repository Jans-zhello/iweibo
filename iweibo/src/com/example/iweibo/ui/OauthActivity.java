package com.example.iweibo.ui;

import com.example.iweibo.R;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
/**
    	this.setContentView(R.layout.auto_dialog);
    	View digView = View.inflate(this,R.layout.dialog,null);
    	dialog = new Dialog(this,R.style.auth_dialog);
    	dialog.setContentView(digView);
    	dialog.show();
 * 实现透明的圆角的对话框形式(即将文字用对话框的形式提示给用户)
 * @author Administrator
 *
 */
public class OauthActivity extends Activity {
    private Dialog dialog;
    protected void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	this.setContentView(R.layout.auto_dialog);
    	/**
    	 * 
    	 * UI界面的对话框显示方式(LoginActivity再次使用)
    	 */
    	View digView = View.inflate(this,R.layout.dialog,null);//当在本Activity中需要获取其他layout页面时用inflate方法其他用findViewById
    	dialog = new Dialog(this,R.style.auth_dialog);
    	dialog.setContentView(digView);
    	dialog.show();
    	Button button = (Button) digView.findViewById(R.id.btn_auto_begin);
    	button.setOnClickListener(new OnClickListener() {
			
		
			public void onClick(View arg0) {
			  Intent intent = new Intent(OauthActivity.this,WebViewActivity.class);
			  startActivity(intent);
			}
		});
    	
    	
    	
    }
}
