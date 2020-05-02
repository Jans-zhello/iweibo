package com.example.iweibo.db;

import android.graphics.drawable.Drawable;

public class UserInfo {
 private Long id;
 private String userId;
 private String userName;
 private String accessToken;
 private String isDefault;
 private Drawable userIcon;
 
 public static final String USER_TABLE_NAME="userinfo";
 public static final String ID="_id";
 public static final String USERID="userId";
 public static final String USERNAME="userName";
 public static final String ACCESSTOKEN="accessToken";
 public static final String ISDEFAULT="isDefault";
 public static final String USERICON = "userIcon";
public UserInfo(String userId, String userName, String accessToken, String isDefault) {
	super();
	this.userId = userId;
	this.userName = userName;
	this.accessToken = accessToken;
	this.isDefault = isDefault;
}
public UserInfo(Long id,String userId, String userName, String accessToken, String isDefault) {
	super();
	this.id = id;
	this.userId = userId;
	this.userName = userName;
	this.accessToken = accessToken;
	this.isDefault = isDefault;
}

public UserInfo(Long id, String userId, String userName, String accessToken, String isDefault, Drawable userIcon) {
	super();
	this.id = id;
	this.userId = userId;
	this.userName = userName;
	this.accessToken = accessToken;
	this.isDefault = isDefault;
	this.userIcon = userIcon;
}
public UserInfo(String userId, String userName, String accessToken, String isDefault, Drawable userIcon) {
	super();
	this.userId = userId;
	this.userName = userName;
	this.accessToken = accessToken;
	this.isDefault = isDefault;
	this.userIcon = userIcon;
}
public Long getId() {
	return id;
}
public void setId(Long id) {
	this.id = id;
}
public String getUserId() {
	return userId;
}
public void setUserId(String userId) {
	this.userId = userId;
}
public String getUserName() {
	return userName;
}
public void setUserName(String userName) {
	this.userName = userName;
}
public String getAccessToken() {
	return accessToken;
}
public void setAccessToken(String accessToken) {
	this.accessToken = accessToken;
}
public String getIsDefault() {
	return isDefault;
}
public void setIsDefault(String idDefault) {
	this.isDefault = idDefault;
}
public Drawable getUserIcon() {
	return userIcon;
}
public void setUserIcon(Drawable userIcon) {
	this.userIcon = userIcon;
}
 
 
 
 
 
}
