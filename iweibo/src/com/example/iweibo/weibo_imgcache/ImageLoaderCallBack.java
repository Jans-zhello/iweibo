package com.example.iweibo.weibo_imgcache;

import android.graphics.Bitmap;

/**
 * 负责刷新ImageView(称为回调)
 * @author Administrator
 *
 */
public interface ImageLoaderCallBack {
  
	void refresh(String url,Bitmap bitmap);
}
