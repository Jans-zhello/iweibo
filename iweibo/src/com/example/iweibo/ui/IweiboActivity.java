package com.example.iweibo.ui;
/**
 * ����Activity�ӿ�
 * ��ʮһ�� Ϊhandle��ʼ�����ݲ�����UI 
 * @author Administrator
 *
 */
public interface IweiboActivity {
 
	/**
	 * ��ʼ������
	 */
	void init();
	/**
	 * ˢ��UI
	 */
	void refresh(Object... objects );//...��ʾ�ɱ䳤�ȵĲ���
}

