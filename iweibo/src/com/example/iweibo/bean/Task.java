package com.example.iweibo.bean;

import java.util.Map;
/**
 * ������ ����Task��javabean
 * @author Administrator
 *
 */
public class Task {
	private int taskId;
	private Map<String,Object> taskParams;
	//��¼
    public static final int WEIBO_LOGIN = 1;
    //��ȡ��Ȩ��Ϣ
    public static final int GET_AccessToken = 2;
    //��ȡ��Ȩ��ַ
    public static final int GET_AccessToken_URL = 3;
    //��ȡ��ǰ��¼�û��Լ�����ע�û�������΢��
    public static final int FRIENDS_TIMELINE = 4;
    //����emotion����
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
