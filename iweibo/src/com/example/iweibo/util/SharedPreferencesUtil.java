package com.example.iweibo.util;
import com.example.iweibo.db.UserInfo;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.drawable.Drawable;
public class SharedPreferencesUtil
{

	
	
	private static final String LOGIN_USER="login_user";

	
	
	/**
	 * 保存登录用户信息
	 * @param context
	 * @param user
	 */
	public static void saveLoginUser(Context context,UserInfo user)
	{
		SharedPreferences sp = context.getSharedPreferences(LOGIN_USER, Context.MODE_PRIVATE);
		 Editor editor = sp.edit();	
		 editor.putString(UserInfo.USERID, user.getUserId());
		 editor.putString(UserInfo.USERNAME, user.getUserName());
		 editor.putString(UserInfo.ACCESSTOKEN, user.getAccessToken());
		 String temp = ImageInfoExchange.drawableToByte(user.getUserIcon());
		 editor.putString(UserInfo.USERICON,temp);
		 editor.commit();
	}
	
	
	/**
	 * 获取登录用户信息
	 * @param context
	 * @return
	 */
	public static UserInfo getLoginUser(Context context)
	{
		SharedPreferences sp = context.getSharedPreferences(LOGIN_USER, Context.MODE_PRIVATE);
		String userId=sp.getString(UserInfo.USERID, "");
		String userName=sp.getString(UserInfo.USERNAME, "");
		String token=sp.getString(UserInfo.ACCESSTOKEN, "");
        Drawable temp = ImageInfoExchange.byteToDrawable(sp.getString(UserInfo.USERICON,""));	
		if("".equals(userId))
			return null;
		return new UserInfo(userId, userName, token, "1",temp);
	}
}
