package com.example.iweibo.weibo_imgcache;

import com.example.iweibo.util.WeiboApplication;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

public class SimpleImgLoader {
  
	public static Bitmap bp;
	public static void showImg(String url,ImageView view){
		
		view.setTag(url);
		view.setImageBitmap(WeiboApplication.lazy.get(url, getCallBack(url,view)));
		
	}
	private static ImageLoaderCallBack getCallBack(final String url,final ImageView view){
		return new ImageLoaderCallBack() {
			@Override
			public void refresh(String url, Bitmap bitmap) {

				//使view和url和bitmap三者对应起来
				if (url.equals(view.getTag().toString())) {
					view.setImageBitmap(bitmap);
				}
			}
		};
		
	}
	public static void displayForDialog( final ImageView iView, String url, final ProgressBar pBar, final Button button,Button button2) {
		 iView.setTag(url);
		 Bitmap bitmap = WeiboApplication.lazy.get(url,getCallBack(iView,url, pBar, button,button2));
		 if (bitmap != null) {
			 bp = bitmap;
			 dismissProgressBar(iView,button,pBar,button2);
		}
		 iView.setImageBitmap(bitmap);
	}
	private static ImageLoaderCallBack getCallBack(final ImageView iView, String url, final ProgressBar pBar, final Button button,final Button button2){
		return new ImageLoaderCallBack() {
			public void refresh(String url, Bitmap bitmap) {
				dismissProgressBar(iView,button,pBar,button2);
				if (url.equals(iView.getTag().toString())) {
					iView.setImageBitmap(bitmap);
					bp = bitmap;
				}
			}
		};
		
	}
	private static void dismissProgressBar(ImageView iView,Button button,ProgressBar pBar,Button button2){
		 pBar.setVisibility(View.GONE);
		 button.setVisibility(View.VISIBLE);
		 button2.setVisibility(View.VISIBLE);
		 iView.setVisibility(View.VISIBLE);
	}
}
