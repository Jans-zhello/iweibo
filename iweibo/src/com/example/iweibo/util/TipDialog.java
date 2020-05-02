package com.example.iweibo.util;

import com.example.iweibo.R;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.Toast;

public class TipDialog {
	public static void showTipDialog(final Context context,String string,String tip) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		// 设置Title的图标
		builder.setIcon(R.drawable.iweibo_icon);
		// 设置Title的内容
		builder.setTitle("温馨提示");
		// 设置Content来显示一个信息
		builder.setMessage(string);
		// 设置一个PositiveButton
		builder.setPositiveButton(tip, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Toast.makeText(context, "positive: " + which, Toast.LENGTH_SHORT).show();
			}
		});
		// 设置一个NegativeButton
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Toast.makeText(context, "negative: " + which, Toast.LENGTH_SHORT).show();
			}
		});
		// 显示出该对话框
		builder.show();
	}
}
