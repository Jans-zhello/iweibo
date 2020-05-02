package com.example.iweibo.util;

import weibo4j.Oauth;
import weibo4j.Users;
import weibo4j.http.AccessToken;
import weibo4j.model.User;
import weibo4j.model.WeiboException;
import java.net.URL;
public class OauthUtil {
 static	Oauth oauth = new Oauth();
	/**
	 *获取授权页面URL
	 * @return
	 */
   public static  String  getAuthorizationURL(){
	   try {
		String url = oauth.authorize("code");
		return url;
	} catch (WeiboException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	   return null;
   }
   /**
    * 
    * 获取AccessToken+username+userid+icno
    * @param code
    */
   public static String getAccessToken(String code){ 
	   try {
		  System.out.println("执行到这了");
		  AccessToken aToken = oauth.getAccessTokenByCode(code);
		  String accessToken = aToken.getAccessToken();
		  String uId = aToken.getUid();
		  String expire_in = aToken.getExpireIn();
		  long expire_time = (System.currentTimeMillis()/1000)+(Long.parseLong(expire_in));
		  Users um = new Users(accessToken);
		  User user = um.showUserById(uId);
		  String uname = user.getScreenName();
		  URL url = user.getProfileImageURL();  
		  return accessToken+"\t"+uId+"\t"+uname+"\t"+url+"\t"+expire_time;
	} catch (WeiboException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	   return null;
   }
} 
