package com.example.iweibo.weibo_parsecache;

import android.text.SpannableString;
import android.widget.TextView;

public class SimpleContentManager {
	private static LazyContentExplain cExplain = new LazyContentExplain();
	public static void display(String content,TextView textView){
		textView.setText(content);
		textView.setText(cExplain.parseContent(content, getCallBack(content,textView)));
	}
	private static ContentParseCallBack getCallBack(final String content,final TextView textView){
            return new ContentParseCallBack() {
				public void refresh(String content, SpannableString ssb) {
					textView.setText(ssb);//高亮显示最后一步
				}
			};
		
	} 
}
