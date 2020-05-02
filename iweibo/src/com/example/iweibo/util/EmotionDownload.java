package com.example.iweibo.util;

/**
 * ����ʵ���˴���������ͼƬ��ָ��Ŀ¼���������̵Ĺ���
 */
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.Thread.State;
import java.net.URL;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import android.os.Environment;

public class EmotionDownload {
	private  String root = Environment.getExternalStorageDirectory() + "/emotions/";
	private DownLoadImageThread dthread = new DownLoadImageThread();
	private BlockingQueue<String> queue = null;

	public EmotionDownload(int emotionsize) {
		queue = new ArrayBlockingQueue<String>(emotionsize);
		createDir(root);
	}

	/**
	 * ����URL���в�put��ȥ
	 */
	public void PutUrlToQuene(String url) {
			try {
				queue.put(url);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
       System.out.println("quene.size()"+queue.size());
	}

	/**
	 * ����Ŀ��Ŀ¼
	 * 
	 * @param destDirName
	 * @return
	 */
	public static void createDir(String destDirName) {
		File f = new File(destDirName);
		if (f.exists()) {
			System.out.println("Ŀ¼�Ѵ���");
		}else{
			f.mkdirs();
			System.out.println("Ŀ¼�����ɹ�");
		}
	}

	/**
	 * ���������߳���
	 */
	private class DownLoadImageThread extends Thread {

		private boolean isRun = true;
		int num = 0;

		public void ShutDown() {
			isRun = false;
		}

		public void run() {
			try {
				while (isRun) {
					System.out.println("start!!!");
					String url = queue.poll();// �Ӷ�����ȡ��һ�������Ƴ�
					if (url == null) {
						break;
					}
					writeFile(url, getFileName(url));
					System.out.println("д��ɹ�!" + (++num));
				}
			} catch (Exception e) {
				System.out.println("�̳߳����ˣ�������������������");
			} finally {
				System.out.println("shut down������");
				ShutDown();
			}
		}

	}

	// �����߳�
	public void StartDownThread() {
		State state = dthread.getState();
		// �߳̿�ʼִ��
		if (state == State.NEW) {
			dthread.start();
		}
		// �߳�ִ�н���
		if (state == State.TERMINATED) {
			dthread = new DownLoadImageThread();
			dthread.start();
		}
	}

	public void writeFile(String strUrl, String fileName) {
		URL url = null;
		InputStream is = null;
		OutputStream os = null;
		try {
			url = new URL(strUrl);
			is = url.openStream();
			os = new FileOutputStream(root+fileName);
			int bytesRead = 0;
			byte[] buffer = new byte[8192];
			while ((bytesRead = is.read(buffer, 0, 8192)) != -1) {
				os.write(buffer, 0, bytesRead);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (is != null) {
					is.close();
				}
				if (os != null) {
					os.flush();
					os.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public static String getFileName(String url) {
		int index = url.lastIndexOf("/");
		return url.substring(index + 1).toLowerCase();
	}

}
