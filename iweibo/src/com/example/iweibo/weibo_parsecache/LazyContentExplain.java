package com.example.iweibo.weibo_parsecache;

import java.lang.Thread.State;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.example.iweibo.util.CallBackManager;
import com.example.iweibo.util.WeiboApplication;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;

/**
 * 高亮解析微博内容 标题、用户名、表情
 * 
 * @author Administrator
 *
 */
public class LazyContentExplain {
	private static final String START = "start";
	private static final String END = "end";
	private static final String PHRASE = "phrase";
	private static final String TOPIC = "#.+?#";
	private static final String NAME = "@([\u4e00-\u9fa5A-Za-z0-9_]*)";
	private static final String URL = "http://.*";
	private static final String EMOTION = "\\[[\u4e00-\u9fa5A-Za-z0-9_]*\\]";
	private static final int MESSAGE_ID = 1;
	private static final String EXTRA_CONTENT = "extra_img_url";
	private static final String EXTRA_SPANNABLE = "extra_img";
	private EmotionManager emotionManager = new EmotionManager(WeiboApplication.context);;
	private BlockingQueue<String> queue = new ArrayBlockingQueue<String>(50);
	private DownLoadImageThread dthread = new DownLoadImageThread();
	private CallBackManager callBackManager = new CallBackManager();

	/**
	 * 获取需要解析的部分list
	 * 
	 * @param pattern
	 * @param content
	 * @return
	 */
	public List<HashMap<String, String>> getStartAndEnd(Pattern pattern, String content) {
		List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		Matcher matcher = pattern.matcher(content);
		while (matcher.find()) {
			HashMap<String, String> map = new HashMap<String, String>();
			map.put(PHRASE, matcher.group());
			map.put(START, matcher.start() + "");
			map.put(END, matcher.end() + "");
			list.add(map);
		}
		return list;
	}
    /**
     * 解析微博
     * @param content
     * @param patternstr
     * @param sBuilder
     * @param emotions
     */
	public void replace(String content, String patternstr, SpannableString sBuilder, Boolean emotions) {
		Pattern pattern = Pattern.compile(patternstr);
		List<HashMap<String, String>> list = this.getStartAndEnd(pattern, content);
		if (list != null) {
			for (HashMap<String, String> hmap : list) {
				int start = Integer.parseInt(hmap.get(START));
				int end = Integer.parseInt(hmap.get(END));
				if (emotions) {
					String phrase = hmap.get(PHRASE);
					Drawable drawable = emotionManager.getDrawableByPhrase(phrase);
					if (drawable != null) {
						drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
						ImageSpan imageSpan = new ImageSpan(drawable);
						sBuilder.setSpan(imageSpan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
					}
				}
				ForegroundColorSpan span = new ForegroundColorSpan(Color.BLUE);
				sBuilder.setSpan(span, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			}
		}

	}
   /**
    * 调用解析微博
    * @param content
    */
	public SpannableString parseContent(String content){
		SpannableString ssb = new SpannableString(content);
		replace(content, TOPIC, ssb, false);
		replace(content, NAME, ssb, false);
		replace(content, URL,ssb,false);
		replace(content, EMOTION, ssb,true);
		return ssb;
	}
	/**
	 * 返回SpannableStringBuilder使得display()方法进行
	 * 【核心方法2】
	 * @param content
	 * @param callback
	 * @return
	 */
	public SpannableStringBuilder parseContent(String content,ContentParseCallBack callback){
		SpannableStringBuilder ssb = new SpannableStringBuilder(content);
		callBackManager.put(content,callback);
		StartDownThread(content);
		return ssb;
	}
	//解析微博线程
	private class DownLoadImageThread extends Thread {

		private boolean isRun = true;

		public void ShutDown() {
			isRun = false;
		}

		public void run() {
			try {
				while (isRun) {
					String content = queue.poll();// 从队列中取出一个对象并移除
					if (content == null) {
						break;
					}
					SpannableString ssb = parseContent(content);
					Message message = handler.obtainMessage(MESSAGE_ID);
					Bundle bundle = message.getData();
					bundle.putSerializable(EXTRA_CONTENT, content);
					bundle.putCharSequence(EXTRA_SPANNABLE, ssb);
					handler.sendMessage(message);
				}
			} catch (Exception e) {
				System.out.println("线程出错了！！！！！！！！！！");
			} finally {
				ShutDown();
			}
		}

	}
	
	//Handler
	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case MESSAGE_ID:
				Bundle bundle = msg.getData();
				String content = bundle.getString(EXTRA_CONTENT);
				SpannableString ssb = (SpannableString) bundle.getCharSequence(EXTRA_SPANNABLE);
				callBackManager.callBack(content, ssb);
				break;
			default:
				break;
			}

		};

	};
	
	//启动线程
	private void StartDownThread(String content) {
		if (null != content) {
			putWeiboQuene(content);
		}
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
	//放入队列
	private void putWeiboQuene(String content) {
		if (!queue.contains(content)) {
			try {
				queue.put(content);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
	
	
	
}