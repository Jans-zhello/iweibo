package com.example.iweibo.bean;

import java.util.Map;
/**
 * 第三步 构造Task的javabean
 * @author Administrator
 *
 */
public class Task {
	private int taskId;
	private Map<String,Object> taskParams;
	//登录
    public static final int WEIBO_LOGIN = 1;
    //获取授权信息
    public static final int GET_AccessToken = 2;
    //获取授权地址
    public static final int GET_AccessToken_URL = 3;
    //获取当前登录用户以及所关注用户的最新微博
    public static final int FRIENDS_TIMELINE = 4;
    //下载emotion表情
    public static final int WEIBO_EMOTIONS = 5;
	public Task(int taskId, Map<String, Object> taskParams) {
		super();
		this.taskId = taskId;
		this.taskParams = taskParams;
	}
  
public int getTaskId() {
	return taskId;
}
public void setTaskId(int taskId) {
	this.taskId = taskId;
}
public Map<String, Object> getTaskParams() {
	return taskParams;
}
public void setTaskParams(Map<String, Object> taskParams) {
	this.taskParams = taskParams;
}
  
}
