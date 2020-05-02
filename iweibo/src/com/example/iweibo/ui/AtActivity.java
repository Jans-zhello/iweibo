package com.example.iweibo.ui;

import java.util.regex.Pattern;

import com.example.iweibo.R;
import com.example.iweibo.bean.Task;
import com.example.iweibo.logic.MainService;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.SpannableString;
import android.widget.TextView;

public class AtActivity extends Activity implements IweiboActivity {

	private ProgressDialog pDialog;
	private TextView textView;
	private String string = "#转需#//@冰冰字幕组:《#最后的岗哨 第一季#》第二集下载地址:http://stareyou.com [微笑][色][抓狂]";
	private SpannableString spannableString = new SpannableString(string);
	//EmotionManager emotionManager;

	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.at);
		// emotionManager = new EmotionManager(this);
		// textView = (TextView) this.findViewById(R.id.txt_at);
		// //高亮显示话题
		// HighLight(Pattern.compile(TOPIC));
		// //高亮显示@人名
		// HighLight(Pattern.compile(NAME));
		// //高亮显示URL
		// HighLight(Pattern.compile(URL));
		// //显示表情
		// phrase(Pattern.compile(EMOTION));
		// textView.setText(spannableString);
		MainService.addActivity(this);
		Task task = new Task(Task.WEIBO_EMOTIONS, null);
		MainService.newTask(task);
		init();
	}

	/**
	 * 获取话题开始和结束位置
	 */
//	public List<HashMap<String, String>> getStartAndEnd(Pattern pattern) {
//		List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
//		Matcher matcher = pattern.matcher(string);
//		while (matcher.find()) {
//			HashMap<String, String> map = new HashMap<String, String>();
//			map.put(PHRASE, matcher.group());
//			map.put(START, matcher.start() + "");
//			map.put(END, matcher.end() + "");
//			list.add(map);
//		}
//		return list;
//	}

	/**
	 * 
	 */

	/**
	 * 高亮显示文本(标题、链接、人名)
	 */
	public void HighLight(Pattern pattern) {
//		List<HashMap<String, String>> list = getStartAndEnd(pattern);
//		for (HashMap<String, String> map : list) {
//			ForegroundColorSpan span = new ForegroundColorSpan(Color.BLUE);
//			spannableString.setSpan(span, Integer.parseInt(map.get(START)), Integer.parseInt(map.get(END)),
//					Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//		}
	}

	/**
	 * 高亮显示表情
	 */
	public void phrase(Pattern pattern) {
//		List<HashMap<String, String>> list = getStartAndEnd(pattern);
//		for (HashMap<String, String> map : list) {
//			Drawable drawable = emotionManager.getDrawableByPhrase(map.get(PHRASE));
//			drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
//			ImageSpan imageSpan = new ImageSpan(drawable);
//			spannableString.setSpan(imageSpan, Integer.parseInt(map.get(START)), Integer.parseInt(map.get(END)),
//					Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//		}
	}

	@Override
	public void init() {
		pDialog = new ProgressDialog(this);
		pDialog.setMessage("正在下载");
        pDialog.show();
	}

	@Override
	public void refresh(Object... objects) {
//		if (pDialog != null) {
//			pDialog.dismiss();
//		}
//		List<Emotion> emotions = (List<Emotion>) objects[0];
//		EmotionDownload eDownload = new EmotionDownload(emotions.size());
//		System.out.println("emotions.size()值:"+emotions.size());
//		for (int i = 0;i<emotions.size();i++) {
//			eDownload.PutUrlToQuene(emotions.get(i).getUrl());
//		}
//		eDownload.StartDownThread();
	}
}
