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
 * ʵ��͸����Բ�ǵĶԻ�����ʽ(���������öԻ������ʽ��ʾ���û�)
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
    	 * UI����ĶԻ�����ʾ��ʽ(LoginActivity�ٴ�ʹ��)
    	 */
    	View digView = View.inflate(this,R.layout.dialog,null);//���ڱ�Activity����Ҫ��ȡ����layoutҳ��ʱ��inflate����������findViewById
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
