package com.example.iweibo.weibo_parsecache;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;

public interface ContentParseCallBack {
	void refresh(String content,SpannableString ssb);
}
