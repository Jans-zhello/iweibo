package com.example.iweibo.util;

import com.example.iweibo.weibo_imgcache.LazyImageLoader;
import com.example.iweibo.weibo_parsecache.LazyContentExplain;

import android.app.Application;
import android.content.Context;

public class WeiboApplication extends Application {
    //public static LazyContentExplain cExplain;
	public static LazyImageLoader lazy;
	public static Context context;
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
	    context = this.getApplicationContext();
	    lazy = new LazyImageLoader();
	    //cExplain = new LazyContentExplain();
	}

	
}
