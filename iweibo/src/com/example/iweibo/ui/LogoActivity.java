package com.example.iweibo.ui;

import com.example.iweibo.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageView;
/**
 * 全屏加载首页面，且出现logo推按由透明到不透明显示且2s跳转下一个页面
 * 
 * @author Administrator
 *
 */
public class LogoActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//取消标题
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		//取消状态栏
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.logo);
		ImageView imageView = (ImageView) this.findViewById(R.id.img_logo);
		AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
        alphaAnimation.setDuration(2000);
        alphaAnimation.setAnimationListener(new AnimationListener() {
			
			public void onAnimationStart(Animation arg0) {
				// TODO Auto-generated method stub
				
			}
			
			public void onAnimationRepeat(Animation arg0) {
				// TODO Auto-generated method stub
				
			}
			
			public void onAnimationEnd(Animation arg0) {
				Intent intent =  new Intent(LogoActivity.this,LoginActivity.class);
				startActivity(intent);
				
			}
		});
		imageView.setAnimation(alphaAnimation);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
