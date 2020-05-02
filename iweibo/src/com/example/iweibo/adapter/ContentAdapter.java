package com.example.iweibo.adapter;

import java.util.List;

import com.example.iweibo.R;
import com.example.iweibo.ui.MainActivity;
import com.example.iweibo.util.ShowTime;
import com.example.iweibo.util.WeiboContenUtil;
import com.example.iweibo.weibo_imgcache.SimpleImgLoader;
import com.example.iweibo.weibo_parsecache.SimpleContentManager;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.util.FloatMath;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import weibo4j.model.Status;

/**
 * ��status��΢����������Դ
 * 
 * @author Administrator
 *
 */
public class ContentAdapter extends BaseAdapter {
	private List<Status> status;
	private LayoutInflater mInflater;
	private Dialog dialog;
	private ImageView iView;
	private ProgressBar pBar;
	private Button button_large;
	private Button button_small;
	private View view;
	private Context context;
	/**
	 * ͼƬ��㴥�����϶�
	 */
	private static final int NONE = 0;// ��״̬
	private static final int DRAG = 0;// ק��״̬
	private static final int ZOOM = 0;// ��㴥��
	private int mode = NONE;// ��ʾģʽ�ı�������ʼ״̬Ϊ��״̬
	private Matrix matrix;// �����仯�ľ���
	private Matrix currmatrix;// ��ǰ����
	private PointF spF;// ��ʼ��
	private PointF mpF;// �м��
	private float startDistance;// �ж���ֻ��ָ�ľ����Ƿ�Ϊ��㴥��״̬

	public ContentAdapter(List<Status> status, Context context) {
		super();
		this.context = context;
		this.status = status;
		this.mInflater = LayoutInflater.from(context);
		view = mInflater.inflate(R.layout.weibo_img_dialog, null);// ��ȡ�����ļ�
		dialog = new Dialog(context, R.style.imgview_dialog);
		iView = (ImageView) view.findViewById(R.id.weibo_img);
		pBar = (ProgressBar) view.findViewById(R.id.weibo_img_load);
		button_large = (Button) view.findViewById(R.id.btn_large);
		button_small = (Button) view.findViewById(R.id.btn_small);
		dialog.setContentView(view);
	}

	public int getCount() {
		// TODO Auto-generated method stub
		return status == null ? 0 : status.size();
	}

	@Override
	public Object getItem(int arg0) {
		return status == null ? null : status.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return Long.parseLong(status.get(arg0).getId());
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		try {
			WeiBoHolder holder = null;
			if (holder == null) {
				holder = new WeiBoHolder();
			}
			if (arg1 == null) {
				arg1 = mInflater.inflate(R.layout.contenttemplate, null);
				System.out.println("arg1View-------->" + arg1);
			}
			Status st = status.get(arg0);
			holder.img_wb_item_head = (ImageView) arg1.findViewById(R.id.img_wb_item_head);
			holder.txt_wb_item_uname = (TextView) arg1.findViewById(R.id.txt_wb_item_uname);
			holder.img_wb_item_V = (ImageView) arg1.findViewById(R.id.img_wb_item_V);
			holder.txt_wb_item_time = (TextView) arg1.findViewById(R.id.txt_wb_item_time);
			holder.txt_wb_item_content = (TextView) arg1.findViewById(R.id.txt_wb_item_content);
			holder.img_wb_item_content_pic = (ImageView) arg1.findViewById(R.id.img_wb_item_content_pic);
			holder.weibo_item_redirect = (LinearLayout) arg1.findViewById(R.id.weibo_item_redirect);
			holder.txt_wb_item_subcontent = (TextView) arg1.findViewById(R.id.txt_wb_item_subcontent);
			holder.img_wb_item_content_subpic = (ImageView) arg1.findViewById(R.id.img_wb_item_content_subpic);
			holder.weibo_from = (TextView) arg1.findViewById(R.id.weibo_from);
			holder.txt_wb_item_redirect = (TextView) arg1.findViewById(R.id.txt_wb_item_redirect);
			holder.txt_wb_item_comment = (TextView) arg1.findViewById(R.id.txt_wb_item_comment);
			// �����û�ͷ��
			SimpleImgLoader.showImg(st.getUser().getProfileImageUrl(), holder.img_wb_item_head);
			// �����û���
			holder.txt_wb_item_uname.setText(st.getUser().getScreenName());
			// �ж��Ƿ�ͨ����V��֤(Ĭ��û��)
			if (st.getUser().isVerified()) {
				holder.img_wb_item_V.setVisibility(View.VISIBLE);
			} else {
				holder.img_wb_item_V.setVisibility(View.GONE);
			}
			// ����΢�������ı�(������ʾ)
			SimpleContentManager.display(st.getText(), holder.txt_wb_item_content);
			// holder.txt_wb_item_content.setText(st.getText());
			// �ж����������Ƿ���ͼƬ
			if (!WeiboContenUtil.isEmpty(st.getThumbnailPic())) {
				holder.img_wb_item_content_pic.setVisibility(View.VISIBLE);
				SimpleImgLoader.showImg(st.getThumbnailPic(), holder.img_wb_item_content_pic);
				holder.img_wb_item_content_pic.setOnClickListener(new PictureOnlickListener(st.getBmiddlePic()));
			} else {
				holder.img_wb_item_content_pic.setVisibility(View.GONE);
			}
			// �ж��Ƿ���ת������
			if (st.getRetweetedStatus() != null) {
				holder.weibo_item_redirect.setVisibility(View.VISIBLE);
				// ����ת���ı�
				SimpleContentManager.display(st.getRetweetedStatus().getText(), holder.txt_wb_item_subcontent);
				// holder.txt_wb_item_subcontent.setText(st.getRetweetedStatus().getText());
				// �ж��Ƿ�ת�������Ƿ���ͼƬ
				if (!WeiboContenUtil.isEmpty(st.getRetweetedStatus().getThumbnailPic())) {
					holder.img_wb_item_content_subpic.setVisibility(View.VISIBLE);
					SimpleImgLoader.showImg(st.getRetweetedStatus().getThumbnailPic(),
							holder.img_wb_item_content_subpic);
					holder.img_wb_item_content_subpic
							.setOnClickListener(new PictureOnlickListener(st.getRetweetedStatus().getBmiddlePic()));
				} else {
					holder.img_wb_item_content_subpic.setVisibility(View.GONE);
				}
			} else {
				holder.weibo_item_redirect.setVisibility(View.GONE);
			}
			// ����ת������
			holder.txt_wb_item_redirect.setText(String.valueOf(st.getRepostsCount()));
			// �������۴���
			holder.txt_wb_item_comment.setText(st.getCommentsCount() + "");
			// ������Դ
			if (st.getSource() != null) {
				holder.weibo_from.setText("����:" + st.getSource().getName());
			} else {
				holder.weibo_from.setText("");
			}
			// ����ʱ��
			holder.txt_wb_item_time.setText(ShowTime.getInterval(st.getCreatedAt()));
		} catch (Exception e) {
			e.printStackTrace();
		}

		return arg1;
	}
	public void refresh(List<Status> s){
		status.addAll(s);
		this.notifyDataSetChanged();
	}

	private static class WeiBoHolder {
		ImageView img_wb_item_head;
		TextView txt_wb_item_uname;
		ImageView img_wb_item_V;
		TextView txt_wb_item_time;
		TextView txt_wb_item_content;
		ImageView img_wb_item_content_pic;
		LinearLayout weibo_item_redirect;
		TextView txt_wb_item_subcontent;
		ImageView img_wb_item_content_subpic;
		TextView weibo_from;
		TextView txt_wb_item_redirect;
		TextView txt_wb_item_comment;
	}

	/**
	 * ͼƬ�鿴��
	 */
	class PictureOnlickListener implements OnClickListener {
		private double scale_large_width = 1.25;
		private double scale_large_height = 1.45;
		private double scale_small_width = 0.8;
		private double scale_small_height = 0.6;
		private float scale_width = 1;// ��ķŴ���
		private float scale_height = 1;// �ߵķŴ���
		private String bmiddleimgurl;
		private Matrix mtx;

		public PictureOnlickListener(String bmiddleimgurl) {
			super();
			this.bmiddleimgurl = bmiddleimgurl;
		}

		public void onClick(View arg0) {
			iView.setVisibility(View.GONE);
			pBar.setVisibility(View.VISIBLE);
			button_large.setVisibility(View.GONE);
			button_small.setVisibility(View.GONE);
			dialog.show();
			SimpleImgLoader.displayForDialog(iView, bmiddleimgurl, pBar, button_large, button_small);
			iView.setOnTouchListener(new ImgViewOnTouchListener(mtx));
			/**
			 * ����Ŵ�ͼƬ
			 */
			button_large.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					iView.setScaleType(ScaleType.MATRIX);
					float dx = (iView.getWidth() / 2)- (SimpleImgLoader.bp.getWidth()/2);
					float dy = (iView.getHeight() / 2)- (SimpleImgLoader.bp.getWidth()/2);
					scale_height = (float) (scale_height * scale_large_height);
					scale_width = (float) (scale_width * scale_large_width);
					mtx = new Matrix();
					mtx.preTranslate(dx, dy);
					mtx.setScale(scale_width, scale_height);
					mtx.postTranslate(dx, dy);
					iView.setImageMatrix(mtx);
					iView.setOnTouchListener(new ImgViewOnTouchListener(mtx));
				}
			});
			/**
			 * �����СͼƬ
			 */
			button_small.setOnClickListener(new OnClickListener() {

				public void onClick(View arg0) {
					iView.setScaleType(ScaleType.MATRIX);
					float dx = (iView.getWidth() / 2) - (SimpleImgLoader.bp.getWidth()/2);
					float dy = (iView.getHeight() / 2)- (SimpleImgLoader.bp.getWidth()/2);
					scale_height = (float) (scale_height * scale_small_height);
					scale_width = (float) (scale_width * scale_small_width);
					mtx = new Matrix();
					mtx.preTranslate(dx, dy);
					mtx.setScale(scale_width, scale_height);
					mtx.postTranslate(dx, dy);
					iView.setImageMatrix(mtx);
					iView.setOnTouchListener(new ImgViewOnTouchListener(mtx));
				}
			});
		}
	}

	/**
	 * 
	 * ʵ��ͼƬ��㴥�����϶�
	 */
	class ImgViewOnTouchListener implements OnTouchListener {
		public ImgViewOnTouchListener(Matrix mtrix) {
			if (mtrix == null) {
				matrix = new Matrix();
			}else{
				matrix = mtrix;	
			}
			currmatrix = new Matrix();
			spF = new PointF();
		}

		public float disTance(MotionEvent event) {
			float ex = event.getX(1) - event.getX(0);
			float ey = event.getY(1) - event.getY(0);
			return FloatMath.sqrt((ex * ex) + (ey * ey));
		}

		public PointF middisTance(MotionEvent event) {
			float ex = (event.getX(1) - event.getX(0)) / 2;
			float ey = (event.getY(1) - event.getY(0)) / 2;
			PointF pf = new PointF();
			pf.set(ex, ey);
			return pf;
		}

		public boolean onTouch(View arg0, MotionEvent arg1) {
			switch (arg1.getAction() & MotionEvent.ACTION_MASK) {

			case MotionEvent.ACTION_DOWN:// ��ֻ��ָ���´���
				currmatrix.set(matrix);
				spF.set(arg1.getX(), arg1.getY());
				mode = DRAG;
				break;
			case MotionEvent.ACTION_POINTER_DOWN:// һֻ��ָ�Ѿ����£�������һֻ��ָ����ʱ �����¼�
				startDistance = disTance(arg1);
				if (startDistance > 5f) {
					mode = ZOOM;
					currmatrix.set(matrix);
					mpF = middisTance(arg1);
				}

				break;
			case MotionEvent.ACTION_MOVE:// ��ֻ��ָ����ʱ�¼�
				if (mode == DRAG) {// ��קģʽ��
					// ���� ���� ����״̬�µĽ���λ��
					float dx = arg1.getX() - spF.x;
					float dy = arg1.getY() - spF.y;
					matrix.set(currmatrix);
					matrix.postTranslate(dx, dy);// �ƶ���ָ��λ��
				} else if (mode == ZOOM) {
					// ���� ˫�� �����Ŵ����С�ı���
					float dis = disTance(arg1);
					if (dis > 5f) {
						matrix.set(currmatrix);
						float scale = dis / startDistance;// ����
						matrix.preScale(scale, scale, mpF.x, mpF.y);
					}
				}
				break;
			case MotionEvent.ACTION_UP:// ���һֻ��ָ�뿪��Ļ�����¼�
			case MotionEvent.ACTION_POINTER_UP:// һֻ��ָ�뿪��������һֻ��ָû���뿪 �����¼�
				mode = NONE;
				break;
			default:
				break;
			}
			iView.setImageMatrix(matrix);

			return true;
		}
	}

}
