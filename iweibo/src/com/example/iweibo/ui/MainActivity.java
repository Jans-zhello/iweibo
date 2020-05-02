package com.example.iweibo.ui;

import java.util.HashMap;
import java.util.List;

import weibo4j.model.Status;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.iweibo.R;
import com.example.iweibo.adapter.ContentAdapter;
import com.example.iweibo.bean.Task;
import com.example.iweibo.logic.MainService;
import com.example.iweibo.util.ShowTime;

public class MainActivity extends Activity implements IweiboActivity {
	// չʾ΢����Ϣ
	private View pview;
	private View tview;
	private ListView lView;
	// չʾ����΢����Ϣ
	private View moreview;
	private ProgressBar pbar;
	private ContentAdapter contentAdapter;
	private long maxid = 0;
	// ˢ��������Ϣ
	private Button btn;
	private static final int MODE_NEW = 0;
	private static final int MODE_LOAD = 1;
	public static final int MODE_PULL = 2;
	private int mode = MODE_NEW;
	// ����ˢ��
	private RelativeLayout headview;
	private ImageView iView;
	public static ProgressBar pBar;
	private TextView tView_pull;
	private TextView tView_update;
	private TextView tView_time;
	private int press_y;// ����״̬ʱ����¼����ʱ��y����
	private static int height = 105;// headview�ĸ߶�
	private int toppadding;// paddingֵʵ������

	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		init();
	}

	@Override
	public void init() {
		// ����main����
		lView = (ListView) this.findViewById(R.id.lv_weibos);
		tview = this.findViewById(R.id.layout_titlebar);// ��ȡinclude������id
		pview = this.findViewById(R.id.layout_progress);// ��ȡinclude������id
		btn = (Button) tview.findViewById(R.id.btn_refresh);
		// ���صײ�footview����
		moreview = View.inflate(this, R.layout.loadingmore, null);
		pbar = (ProgressBar) moreview.findViewById(R.id.pbar_more);
		((TextView) (tview.findViewById(R.id.home_username)))
				.setText(MainService.loginuser.getUserName());// ��ҳ��ʾ�û���
		// ����ˢ������
		headview = (RelativeLayout) View.inflate(this, R.layout.pull_refresh,
				null);
		iView = (ImageView) headview.findViewById(R.id.pull_img);
		pBar = (ProgressBar) headview.findViewById(R.id.pull_bar);
		tView_pull = (TextView) headview.findViewById(R.id.pull_text);
		tView_update = (TextView) headview.findViewById(R.id.pull_update_text);
		tView_time = (TextView) headview.findViewById(R.id.pull_time);
		// ˢ�°�ť�����¼�
		btn.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				Animation a = AnimationUtils.loadAnimation(MainActivity.this,
						R.animator.refresh_animation);// ��ȡ����xml�ļ�
				btn.startAnimation(a);
				maxid = -1;
				mode = MODE_NEW;
				newWork();
			}
		});
		/**
		 * �ж�lview�Ƿ��䵽�ײ��򶥲�
		 */
		lView.setOnScrollListener(new OnScrollListener() {
			@Override
			public void onScroll(AbsListView view, final int firstVisibleItem,
					final int visibleItemCount, final int totalItemCount) {
				lView.setOnTouchListener(new OnTouchListener() {
					public boolean onTouch(View arg0, MotionEvent arg1) {
						switch (arg1.getAction()) {
						case MotionEvent.ACTION_DOWN:// ����״̬����¼һ��y����
							press_y = (int) arg1.getY();
							break;
						case MotionEvent.ACTION_MOVE:// �϶�״̬,��Ҫͨ��headview��paddingֵʵ������Ч��
							applyHeadViewPadding(arg1);
							break;
						case MotionEvent.ACTION_UP:
							if (firstVisibleItem == 0) {
								View firstVisibleItemView = lView.getChildAt(0);
								if (firstVisibleItemView != null
										&& firstVisibleItemView.getTop() == 0) {
									Log.d("ListView", "##### ���������� #####");
									if (toppadding > 0) {
										headview.setPadding(
												headview.getPaddingLeft(), 0,
												headview.getPaddingRight(),
												headview.getPaddingBottom());
										pBar.setVisibility(View.VISIBLE);
										tView_time.setText(ShowTime.nowTime());
										mode = MODE_PULL;
										maxid = -1;
										newWork();
									}
								}
							} else if ((firstVisibleItem + visibleItemCount) == totalItemCount) {
								View lastVisibleItemView = lView
										.getChildAt(lView.getChildCount() - 1);
								if (lastVisibleItemView != null
										&& lastVisibleItemView.getBottom() == lView.getHeight()) {
									Log.d("ListView", "##### �������ײ� ######");
									pbar.setVisibility(View.VISIBLE);
									mode = MODE_LOAD;
									newWork();
									lView.setEnabled(false);
								}
							}
							break;
						default:
							break;
						}
						return false;
					}
				});
			}

			@Override
			public void onScrollStateChanged(AbsListView arg0, int arg1) {
				// TODO Auto-generated method stub

			}
		});

		lView.addHeaderView(headview, null, false);
		lView.addFooterView(moreview, null, true);// ��ȡ������Ϣ,�������setAdapter����֮ǰ���ã�����ᱨ��
		MainService.addActivity(this);
		newWork();
	}

	private void newWork() {
		HashMap<String, Object> mp = new HashMap<String, Object>(1);
		mp.put("maxid", maxid);
		Task task = new Task(Task.FRIENDS_TIMELINE, mp);
		MainService.newTask(task);

	}

	@Override
	public void refresh(Object... objects) {
		pview.setVisibility(View.GONE);
		List<Status> status = (List<Status>) objects[0];
		if (status != null && status.size() > 0) {
			maxid = Long.parseLong(status.get(status.size() - 1).getMid()) - 1;
			if (mode == MODE_NEW || mode == MODE_PULL) {
				btn.clearAnimation();
				if (pBar != null) {
					pBar.setVisibility(View.GONE);
				}
				contentAdapter = new ContentAdapter(status, this);
				lView.setAdapter(contentAdapter);
				lView.setSelection(1);
			} else {
				System.out.println("���صĽ��:" + status.size());
				contentAdapter.refresh(status);
				pbar.setVisibility(View.GONE);
				lView.setSelection(contentAdapter.getCount() - 20);
				lView.setEnabled(true);
			}

		}
	}

	/**
	 * ���ϱ仯headview��paddingʵ������Ч��
	 * 
	 * @param event
	 */
	public void applyHeadViewPadding(MotionEvent event) {
		int count = event.getHistorySize();// ���ص�ǰ�¼����õ��˶�λ�õ���Ŀ
		for (int i = 0; i < count; i++) {
			int history = (int) event.getHistoricalY(i);
			toppadding = (history - press_y);
			int temptoppadding = (int) (((history - press_y) - height) / 1.5);
			headview.setPadding(headview.getPaddingLeft(), temptoppadding,
					headview.getPaddingRight(), headview.getPaddingBottom());
		}
	}
}
