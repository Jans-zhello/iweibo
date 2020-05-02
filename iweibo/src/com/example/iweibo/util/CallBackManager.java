package com.example.iweibo.util;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import com.example.iweibo.weibo_imgcache.ImageLoaderCallBack;
import com.example.iweibo.weibo_parsecache.ContentParseCallBack;

import android.graphics.Bitmap;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;

public class CallBackManager {

	private ConcurrentHashMap<String,List<ImageLoaderCallBack>> concurrentHashMap;
	private ConcurrentHashMap<String,List<ContentParseCallBack>> concurrentHashMap2;
	public CallBackManager() {
		super();
	 concurrentHashMap = new ConcurrentHashMap<String, List<ImageLoaderCallBack>>();
	 concurrentHashMap2 = new ConcurrentHashMap<String, List<ContentParseCallBack>>();
	}
 /**
  * put 微博上的图片
  * @param url
  * @param imageLoaderCallBack
  */
   public void put(String url,ImageLoaderCallBack imageLoaderCallBack){
	   if (!concurrentHashMap.contains(url)) {
		concurrentHashMap.put(url, new ArrayList<ImageLoaderCallBack>());
	 }
     concurrentHashMap.get(url).add(imageLoaderCallBack);
   }
   /**
    * put 微博上的内容(高亮解析)
    * @param content
    * @param callback
    */
   public void put(String content,ContentParseCallBack callback){
	   if (!concurrentHashMap2.contains(content)) {
		   concurrentHashMap2.put(content, new ArrayList<ContentParseCallBack>());
	 }
	   concurrentHashMap2.get(content).add(callback);
   }
	public void callBack(String url,Bitmap bitmap){
		List<ImageLoaderCallBack> callbacks = concurrentHashMap.get(url);
		if (null == callbacks) {
			return;
		}
		for (ImageLoaderCallBack ilc : callbacks) {
			if (null != ilc) {
				ilc.refresh(url, bitmap);
			}
		}
		callbacks.clear();
		concurrentHashMap.remove(url);
	}
	public void callBack(String content,SpannableString ssb){
		List<ContentParseCallBack> callbacks = concurrentHashMap2.get(content);
		if (null == callbacks) {
			return;
		}
		for (ContentParseCallBack ilc : callbacks) {
			if (null != ilc) {
				ilc.refresh(content, ssb);
			}
		}
		callbacks.clear();
		concurrentHashMap2.remove(content);
	}
}
