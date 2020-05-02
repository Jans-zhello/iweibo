package com.example.iweibo.ui;
import com.example.iweibo.R;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

@SuppressWarnings("deprecation")
/**
 * 实现主页面切换选项卡效果
 * @author Administrator
 *
 */
public class HomeActivity extends TabActivity {
  private static final String HOME = "home";
  private static final String AT = "at";
  private static final String MSG = "msg";
  private static final String MORE= "more";
  private TabHost tabHost;
protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_main);
	 tabHost = this.getTabHost();
	TabSpec home = tabHost.newTabSpec(HOME).setIndicator(HOME).setContent(new Intent(this,MainActivity.class));
	TabSpec at = tabHost.newTabSpec(AT).setIndicator(AT).setContent(new Intent(this,AtActivity.class));
	TabSpec msg = tabHost.newTabSpec(MSG).setIndicator(MSG).setContent(new Intent(this,MsgActivity.class));
	TabSpec more = tabHost.newTabSpec(MORE).setIndicator(MORE).setContent(new Intent(this,MoreActivity.class));
	tabHost.addTab(home);
	tabHost.addTab(at);
	tabHost.addTab(msg);
	tabHost.addTab(more);
	RadioGroup radioGroup = (RadioGroup) this.findViewById(R.id.home_radio);
	radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
		public void onCheckedChanged(RadioGroup arg0, int arg1) {//arg1即为4个面板（选项卡）buttonId
           switch (arg1) {
		case R.id.home_button:
			tabHost.setCurrentTabByTag(HOME);//设置选项卡标签，即可以跳转到对应的activity
			break;
		case R.id.at_button:
			tabHost.setCurrentTabByTag(AT);//设置选项卡标签，即可以跳转到对应的activity
			break;
		case R.id.msg_button:
			tabHost.setCurrentTabByTag(MSG);//设置选项卡标签，即可以跳转到对应的activity
			break;
		case R.id.more_button:
			tabHost.setCurrentTabByTag(MORE);//设置选项卡标签，即可以跳转到对应的activity
			break;
		default:
			break;
		}
			
		}
	});
	
	
}
}
