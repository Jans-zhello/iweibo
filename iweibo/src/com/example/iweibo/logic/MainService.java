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
 * 系统主服务
 * 
 * @author Administrator
 *
 */
public class MainService extends Service implements Runnable {

	private static Queue<Task> tasks = new LinkedList<Task>();// 第二步 定义任务队列
	private boolean isRun;// 第五步 定义boolean变量默认是 false
	public static UserInfo loginuser;// 当前系统登录用户
	private static Timeline timeline;
	private static String temptoken;
	private static long expiretime;

	/**
	 * 第四步添加任务到任务队列中去
	 * 
	 * @param t
	 */
	public static void newTask(Task t) {
		tasks.add(t);

	}

	@Override
	public void onCreate() {
		isRun = true;// 第六步设置成true
		Thread thread = new Thread(this);// 第一步(1)
		thread.start();// 第一步 (2)启动线程调用run()方法
		super.onCreate();
	}

	@Override
	public void run() {
		while (isRun)// 第七步
		{
			Task task = null;
			if (!tasks.isEmpty()) {
				task = tasks.poll();
				if (task != null) {
					doTask(task);
				}
			}
			try {
				Thread.sleep(1000);// 第八步
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	/**
	 * 第九步 处理任务
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
				System.out.println("过期了");
				message.obj = "AccessToken过期,请重新授权";
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
			System.out.println("发送给Handler************>>>>>" + StringToken);
			message.obj = StringToken;
			break;
		case Task.GET_AccessToken_URL:
			String url = OauthUtil.getAuthorizationURL();
			message.obj = url;
			break;
		// 获取当前登录用户以及所关注用户的最新微博
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
				page.setCount(20);//一次加载20条
				List<Status> status = (List<Status>) timeline.getFriendsTimeline(0,0,page).getStatuses();
				message.obj = status;
			} catch (WeiboException e) {
			System.out.println("获取动态失败");
			}
			break;
	   //获取微博表情
		case Task.WEIBO_EMOTIONS:
			try {
				if (timeline == null) {
					timeline = new Timeline(temptoken);
				}
				List<Emotion> emotions = timeline.getEmotions("face","cnname");
				message.obj = emotions;
			} catch (WeiboException e) {
				System.out.println("获取表情失败!!!!!!!");
			}
			break;
		default:
			break;
		}
		h.sendMessage(message);
	}

	/**
	 * 第十步 定义handle 更新UI
	 * 
	 */
	Handler h = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case Task.WEIBO_LOGIN:// 用户登录
				// 并更新UI
				IweiboActivity activity = (IweiboActivity) getActivityByName("LoginActivity");// 第十三步
				activity.refresh(msg.obj);
				if (msg.obj != null) {
					System.out.println("obj的消息是:" + msg.obj);
				}
				break;
			case Task.GET_AccessToken:// 用户授权
				// 并更新UI
				IweiboActivity activity2 = (IweiboActivity) getActivityByName("AccessActivity");// 第十三步
				System.out.println("Handler接收到用户信息**********" + msg.obj);
				activity2.refresh(msg.obj);
				break;
			case Task.GET_AccessToken_URL:// 用户授权URL
				// 并更新UI
				IweiboActivity activity3 = (IweiboActivity) getActivityByName("WebViewActivity");// 第十三步
				System.out.println("Handler接收到URL**********" + msg.obj);
				activity3.refresh(msg.obj);
				break;
			case Task.FRIENDS_TIMELINE:// 用户最新微博消息
				// 并更新UI
				IweiboActivity activity4 = (IweiboActivity) getActivityByName("MainActivity");// 第十三步
				activity4.refresh(msg.obj);
				break;
			case Task.WEIBO_EMOTIONS:// 下载微博表情
				// 并更新UI
				IweiboActivity activity5 = (IweiboActivity) getActivityByName("AtActivity");// 第十三步
				activity5.refresh(msg.obj);
				break;
			default:
				break;
			}

		};
	};
	/**
	 * 第十二步 添加activity到list中去(类比添加任务到Quene中)
	 * 
	 */
	private static ArrayList<Activity> appactivities = new ArrayList<Activity>();

	public static void addActivity(Activity t) {
		/**
		 * //避免相同名字的activity重复添加,先移除一个
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
	 * 第十四根据name获取Activity对象
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
