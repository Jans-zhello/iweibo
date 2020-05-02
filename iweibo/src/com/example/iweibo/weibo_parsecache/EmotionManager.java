package com.example.iweibo.weibo_parsecache;

import java.util.HashMap;

import com.example.iweibo.R;
import com.example.iweibo.util.IDs;

import android.content.Context;
import android.graphics.drawable.Drawable;

public class EmotionManager {
	private Context context;
	private static HashMap<String, Integer> emotionmap;
	private static String[] phrase;
	public EmotionManager(Context context) {
		this.context = context;
		phrase = context.getResources().getStringArray(R.array.default_emotion);// 关联String文件中array数组
		if (IDs.emtion_id.length != phrase.length) {
			throw new RuntimeException("长度不等!");
		}
		int length = IDs.emtion_id.length;
		emotionmap = new HashMap<String, Integer>(length);
		for (int i = 0; i < length; i++) {
			emotionmap.put(phrase[i], IDs.emtion_id[i]);
		}
	}

	public Drawable getDrawableByPhrase(String phrase) {
		if (emotionmap != null) {
			int id = emotionmap.get(phrase);
			return context.getResources().getDrawable(id);
		}
		return null;
	}
}
