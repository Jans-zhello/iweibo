package com.example.iweibo.logic;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import com.example.iweibo.bean.Task;
import com.example.iweibo.db.UserInfo;
import com.example.iweibo.ui.IweiboActivity;
import com.example.iweibo.ui.MainActivity;
import com.example.iweibo.ui.WebViewActivity;
import com.example.iweibo.util.OauthUtil;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import weibo4j.Timeline;
import weibo4j.model.Emotion;
import weibo4j.model.Paging;
import weibo4j.model.Status;
import weibo4j.model.WeiboException;

/**
 * 
 * ϵͳ������
 * 
 * @author Administrator
 *
 */
public class MainService extends Service implements Runnable {

	private static Queue<Task> tasks = new LinkedList<Task>();// �ڶ��� �����������
	private boolean isRun;// ���岽 ����boolean����Ĭ���� false
	public static UserInfo loginuser;// ��ǰϵͳ��¼�û�
	private static Timeline timeline;
	private static String temptoken;
	private static long expiretime;

	/**
	 * ���Ĳ�����������������ȥ
	 * 
	 * @param t
	 */
	public static void newTask(Task t) {
		tasks.add(t);

	}

	@Override
	public void onCreate() {
		isRun = true;// ���������ó�true
		Thread thread = new Thread(this);// ��һ��(1)
		thread.start();// ��һ�� (2)�����̵߳���run()����
		super.onCreate();
	}

	@Override
	public void run() {
		while (isRun)// ���߲�
		{
			Task task = null;
			if (!tasks.isEmpty()) {
				task = tasks.poll();
				if (task != null) {
					doTask(task);
				}
			}
			try {
				Thread.sleep(1000);// �ڰ˲�
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	/**
	 * �ھŲ� ��������
	 * 
	 * @param task
	 */
	private void doTask(Task task) {
		Message message = h.obtainMessage();
		message.what = task.getTaskId();
		switch (task.getTaskId()) {
		case Task.WEIBO_LOGIN:
			loginuser = (UserInfo) task.getTaskParams().get("nowuser");
			if ((System.currentTimeMillis() / 1000) == expiretime) {
				System.out.println("������");
				message.obj = "AccessToken����,��������Ȩ";
			} else {
				temptoken = loginuser.getAccessToken();
			}
			break;
		case Task.GET_AccessToken:
			String code = null;
			while (code == null) {
				code = WebViewActivity.CODE;
			}
			String StringToken = null;
			while (StringToken == null) {
				StringToken = OauthUtil.getAccessToken(code);
			}
			String[] temp = StringToken.split("\t");
			expiretime = Long.parseLong(temp[4]);
			System.out.println("���͸�Handler************>>>>>" + StringToken);
			message.obj = StringToken;
			break;
		case Task.GET_AccessToken_URL:
			String url = OauthUtil.getAuthorizationURL();
			message.obj = url;
			break;
		// ��ȡ��ǰ��¼�û��Լ�����ע�û�������΢��
		case Task.FRIENDS_TIMELINE:
			try {
				System.out.println("temp" + temptoken);
				timeline = new Timeline(temptoken);
				Long maxid = (Long) task.getTaskParams().get("maxid");
				Paging page = new Paging();
				if (maxid <= 0) {
					page.setPage(1);
				}else{
					page.setMaxId(maxid);
				}
				page.setCount(20);//һ�μ���20��
				List<Status> status = (List<Status>) timeline.getFriendsTimeline(0,0,page).getStatuses();
				message.obj = status;
			} catch (WeiboException e) {
			System.out.println("��ȡ��̬ʧ��");
			}
			break;
	   //��ȡ΢������
		case Task.WEIBO_EMOTIONS:
			try {
				if (timeline == null) {
					timeline = new Timeline(temptoken);
				}
				List<Emotion> emotions = timeline.getEmotions("face","cnname");
				message.obj = emotions;
			} catch (WeiboException e) {
				System.out.println("��ȡ����ʧ��!!!!!!!");
			}
			break;
		default:
			break;
		}
		h.sendMessage(message);
	}

	/**
	 * ��ʮ�� ����handle ����UI
	 * 
	 */
	Handler h = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case Task.WEIBO_LOGIN:// �û���¼
				// ������UI
				IweiboActivity activity = (IweiboActivity) getActivityByName("LoginActivity");// ��ʮ����
				activity.refresh(msg.obj);
				if (msg.obj != null) {
					System.out.println("obj����Ϣ��:" + msg.obj);
				}
				break;
			case Task.GET_AccessToken:// �û���Ȩ
				// ������UI
				IweiboActivity activity2 = (IweiboActivity) getActivityByName("AccessActivity");// ��ʮ����
				System.out.println("Handler���յ��û���Ϣ**********" + msg.obj);
				activity2.refresh(msg.obj);
				break;
			case Task.GET_AccessToken_URL:// �û���ȨURL
				// ������UI
				IweiboActivity activity3 = (IweiboActivity) getActivityByName("WebViewActivity");// ��ʮ����
				System.out.println("Handler���յ�URL**********" + msg.obj);
				activity3.refresh(msg.obj);
				break;
			case Task.FRIENDS_TIMELINE:// �û�����΢����Ϣ
				// ������UI
				IweiboActivity activity4 = (IweiboActivity) getActivityByName("MainActivity");// ��ʮ����
				activity4.refresh(msg.obj);
				break;
			case Task.WEIBO_EMOTIONS:// ����΢������
				// ������UI
				IweiboActivity activity5 = (IweiboActivity) getActivityByName("AtActivity");// ��ʮ����
				activity5.refresh(msg.obj);
				break;
			default:
				break;
			}

		};
	};
	/**
	 * ��ʮ���� ���activity��list��ȥ(����������Quene��)
	 * 
	 */
	private static ArrayList<Activity> appactivities = new ArrayList<Activity>();

	public static void addActivity(Activity t) {
		/**
		 * //������ͬ���ֵ�activity�ظ����,���Ƴ�һ��
		 */
		if (!appactivities.isEmpty()) {
			for (Activity a : appactivities) {
				if (a.getClass().getName().equals(t.getClass().getName())) {
					appactivities.remove(a);
					break;
				}
			}
		}
		appactivities.add(t);

	}

	/**
	 * ��ʮ�ĸ���name��ȡActivity����
	 * 
	 * @param name
	 * @return
	 */
	private Activity getActivityByName(String name) {
		if (!appactivities.isEmpty()) {

			for (Activity activity : appactivities) {
				if (activity.getClass().getName().indexOf(name) > 0) {
					return activity;
				}

			}

		}

		return null;
	}

	@Override
	public IBinder onBind(Intent arg0) {

		return null;
	}
}
