package com.example.iweibo.weibo_imgcache;

import java.lang.Thread.State;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import com.example.iweibo.util.CallBackManager;
import com.example.iweibo.util.WeiboApplication;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

/**
 * 
 * @author Administrator
 *
 */
public class LazyImageLoader {
	private ImageManager imageManager = new ImageManager(WeiboApplication.context);
	private BlockingQueue<String> queue = new ArrayBlockingQueue<String>(50);
	private DownLoadImageThread dthread = new DownLoadImageThread();
	private CallBackManager callBackManager = new CallBackManager();
	private static final int MESSAGE_ID = 1;
	private static final String EXTRA_IMG_URL = "extra_img_url";
	private static final String EXTRA_IMG = "extra_img";
    /**
     * 【核心方法1】
     * @param url
     * @param imageLoaderCallBack
     * @return
     */
	public Bitmap get(String url, ImageLoaderCallBack imageLoaderCallBack) {
		Bitmap bitmap = null;
		System.out.println("get 方法执行");
		if (imageManager.contains(url)) {// 如果存在加载
			bitmap = imageManager.getFromCache(url);
			System.out.println("存在返回bitmap");
			return bitmap;
		} else {// 不存在 开启线程从Internet上面获取
			System.out.println("不存在开启线程获取");
			callBackManager.put(url, imageLoaderCallBack);
			StartDownThread(url);
		}
		return bitmap;
	}

	private void StartDownThread(String url) {
		PutUrlToQuene(url);
		State state = dthread.getState();
		// 线程开始执行
		if (state == State.NEW) {
			dthread.start();
		}
		// 线程执行结束
		if (state == State.TERMINATED) {
			dthread = new DownLoadImageThread();
			dthread.start();
		}
	}

	private void PutUrlToQuene(String url) {
		if (!queue.contains(url)) {
			try {
				queue.put(url);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case MESSAGE_ID:
				Bundle bundle = msg.getData();
				String url = bundle.getString(EXTRA_IMG_URL);
				Bitmap bitmap = bundle.getParcelable(EXTRA_IMG);
				callBackManager.callBack(url, bitmap);
				break;
			default:
				break;
			}

		};

	};

	// 开启线程从Internet上面获取 类
	private class DownLoadImageThread extends Thread {

		private boolean isRun = true;

		public void ShutDown() {
			isRun = false;
		}

		public void run() {
			try {
				while (isRun) {
					String url = queue.poll();// 从队列中取出一个对象并移除
					if (url == null) {
						break;
					}
					// 开始正式从网上下载
					Bitmap bitmap = imageManager.safeGet(url);
					Message message = handler.obtainMessage(MESSAGE_ID);
					Bundle bundle = message.getData();
					bundle.putSerializable(EXTRA_IMG_URL, url);
					bundle.putParcelable(EXTRA_IMG, bitmap);
					handler.sendMessage(message);
				}
			} catch (Exception e) {
				System.out.println("线程出错了！！！！！！！！！！");
			} finally {
				ShutDown();
			}
		}

	}

}
