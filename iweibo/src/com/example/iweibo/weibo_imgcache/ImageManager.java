package com.example.iweibo.weibo_imgcache;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import com.example.iweibo.R;
import com.example.iweibo.util.ImageInfoExchange;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * image缓存区(SoftReference为了让系统更好的回收资源)
 * 
 * @author Administrator
 *
 */
public class ImageManager {
	Map<String, SoftReference<Bitmap>> imgcache;
	private Context context;

	public ImageManager(Context context) {
		imgcache = new HashMap<String, SoftReference<Bitmap>>();
		this.context = context;
	}

	public boolean contains(String url) {
		return imgcache.containsKey(url);
	}

	// 从缓存中获取图片
	public Bitmap getFromCache(String url) {
		Bitmap bitmap = null;
		bitmap = this.getFromMapCache(url);
		if (bitmap == null) {
			bitmap = this.getFromFileCache(url);
		}
		return bitmap;
	}

	// 从Map缓存中获取
	public Bitmap getFromMapCache(String url) {
		Bitmap bitmap = null;
		SoftReference<Bitmap> reference = null;
		synchronized (this) {
			reference = imgcache.get(url);
		}
		if (reference != null) {
			bitmap = reference.get();
			if (bitmap != null) {
				return bitmap;
			}
		}
		return null;

	}

	/**
	 * 获取文件名
	 * 
	 * @param url
	 * @return
	 */
	public static String getFileName(String url) {
		int index = url.lastIndexOf("/");
		return url.substring(index + 1);
	}

	// 从文件系统中获取
	public Bitmap getFromFileCache(String url) {
		String filename = getFileName(url);
		FileInputStream is = null;
		try {
			is = context.openFileInput(filename);
			return BitmapFactory.decodeStream(is);
		} catch (FileNotFoundException e) {
			return null;
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}

	}

	/**
	 * 从文件系统中获取图片并放到Map中去
	 * 
	 * @param url
	 * @return
	 */
	@SuppressWarnings("unused")
	public Bitmap safeGet(String url) {
		Bitmap bitmap = this.getFromFileCache(url);
		if (null != bitmap) {
			synchronized (this) {
				imgcache.put(url, new SoftReference<Bitmap>(bitmap));
			}
			return bitmap;
		}
		return downLoadImg(url);
	}

	// 从网上下载图片实现
	public Bitmap downLoadImg(String urlstr) {
		try {
			URL url = new URL(urlstr);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			String filenamepath = WriterToFile(getFileName(urlstr), connection.getInputStream());
			return BitmapFactory.decodeFile(filenamepath);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	// 将网上下载的图片存放到文件里面
	public String WriterToFile(String filename, InputStream inputStream) {
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;
		try {
			bis = new BufferedInputStream(inputStream);
			bos = new BufferedOutputStream(context.openFileOutput(filename, Context.MODE_PRIVATE));
			byte[] buffer = new byte[1024];
			int length;
			try {
				while ((length = bis.read(buffer)) != -1) {
					try {
						bos.write(buffer, 0, length);
						System.out.println("写入中!!!");
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				System.out.println("写入成功!!!");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (null != bis) {
				try {
					bis.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (null != bos) {
				try {
					bos.flush();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					bos.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		return context.getFilesDir() + "/" + filename;
	}
}
