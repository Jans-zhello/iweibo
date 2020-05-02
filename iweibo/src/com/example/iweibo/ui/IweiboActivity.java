package com.example.iweibo.ui;
/**
 * 定义Activity接口
 * 第十一步 为handle初始化数据并更新UI 
 * @author Administrator
 *
 */
public interface IweiboActivity {
 
	/**
	 * 初始化数据
	 */
	void init();
	/**
	 * 刷新UI
	 */
	void refresh(Object... objects );//...表示可变长度的参数
}

