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
	private String string = "#ת��#//@������Ļ��:��#���ĸ��� ��һ��#���ڶ������ص�ַ:http://stareyou.com [΢Ц][ɫ][ץ��]";
	private SpannableString spannableString = new SpannableString(string);
	//EmotionManager emotionManager;

	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.at);
		// emotionManager = new EmotionManager(this);
		// textView = (TextView) this.findViewById(R.id.txt_at);
		// //������ʾ����
		// HighLight(Pattern.compile(TOPIC));
		// //������ʾ@����
		// HighLight(Pattern.compile(NAME));
		// //������ʾURL
		// HighLight(Pattern.compile(URL));
		// //��ʾ����
		// phrase(Pattern.compile(EMOTION));
		// textView.setText(spannableString);
		MainService.addActivity(this);
		Task task = new Task(Task.WEIBO_EMOTIONS, null);
		MainService.newTask(task);
		init();
	}

	/**
	 * ��ȡ���⿪ʼ�ͽ���λ��
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
	 * ������ʾ�ı�(���⡢���ӡ�����)
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
	 * ������ʾ����
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
		pDialog.setMessage("��������");
        pDialog.show();
	}

	@Override
	public void refresh(Object... objects) {
//		if (pDialog != null) {
//			pDialog.dismiss();
//		}
//		List<Emotion> emotions = (List<Emotion>) objects[0];
//		EmotionDownload eDownload = new EmotionDownload(emotions.size());
//		System.out.println("emotions.size()ֵ:"+emotions.size());
//		for (int i = 0;i<emotions.size();i++) {
//			eDownload.PutUrlToQuene(emotions.get(i).getUrl());
//		}
//		eDownload.StartDownThread();
	}
}
