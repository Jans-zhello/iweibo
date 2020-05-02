package com.example.iweibo.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ShowTime {
	public static String getInterval(Date createAt) {
		// 定义最终返回的结果字符串。
		String interval = null;

		long millisecond = new Date().getTime() - createAt.getTime();

		long second = millisecond / 1000;

		if (second <= 0) {
			second = 0;
		}

		if (second == 0) {
			interval = "刚刚";
		} else if (second < 30) {
			interval = second + "秒以前";
		} else if (second >= 30 && second < 60) {
			interval = "半分钟前";
		} else if (second >= 60 && second < 60 * 60) {
			long minute = second / 60;
			interval = minute + "分钟前";
		} else if (second >= 60 * 60 && second < 60 * 60 * 24) {
			long hour = (second / 60) / 60;
			if (hour <= 3) {
				interval = hour + "小时前";
			} else {
				interval = "今天" + getFormatTime(createAt, "hh:mm");
			}
		} else if (second >= 60 * 60 * 24 && second <= 60 * 60 * 24 * 2) {
			interval = "昨天" + getFormatTime(createAt, "hh:mm");
		} else if (second >= 60 * 60 * 24 * 2 && second <= 60 * 60 * 24 * 7) {
			long day = ((second / 60) / 60) / 24;
			interval = day + "天前";
		} else if (second >= 60 * 60 * 24 * 7) {
			interval = getFormatTime(createAt, "MM-dd hh:mm");
		} else if (second >= 60 * 60 * 24 * 365) {
			interval = getFormatTime(createAt, "YYYY-MM-dd hh:mm");
		} else {
			interval = "0";
		}
		// 最后返回处理后的结果。
		return interval;
	}

	public static String getFormatTime(Date date, String sdf) {
		return (new SimpleDateFormat(sdf)).format(date);
	}
	public static String nowTime(){
		Date date = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        return	dateFormat.format(date);	
	}
}
