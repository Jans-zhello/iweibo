package com.example.iweibo.weibo_imgcache;

import android.graphics.Bitmap;

/**
 * ����ˢ��ImageView(��Ϊ�ص�)
 * @author Administrator
 *
 */
public interface ImageLoaderCallBack {
  
	void refresh(String url,Bitmap bitmap);
}
