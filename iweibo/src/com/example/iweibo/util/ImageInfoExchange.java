package com.example.iweibo.util;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;
import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Base64;
public class ImageInfoExchange {

	/**
	 * 
	 * Drawble转换bitmap
	 * @param 
	 * @return
	 */
	public static Bitmap drawableToBitmap(Drawable drawable) // drawable 转换成bitmap  
	{  
		if (drawable == null) {
			return null;
		}
	    int width = drawable.getIntrinsicWidth();// 取drawable的长宽  
	    int height = drawable.getIntrinsicHeight();  
	    Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ?Bitmap.Config.ARGB_8888:Bitmap.Config.RGB_565;// 取drawable的颜色格式  
	    Bitmap bitmap = Bitmap.createBitmap(width, height, config);// 建立对应bitmap  
	    Canvas canvas = new Canvas(bitmap);// 建立对应bitmap的画布  
	    drawable.setBounds(0, 0, width, height);  
	    drawable.draw(canvas);// 把drawable内容画到画布中  
	    return bitmap;
  }
	/**
	 * bitmap转换成byte[]
	 * @param 
	 * @return
	 */
	public static byte[] BitmapToByte(Bitmap bitmap){
		if (bitmap == null) {
			return null;
		}
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
		return baos.toByteArray();
	}
	/**
	 * 
	 * URL转换Drawable
	 */
	public static Drawable UrlToDrawble(URL url){
		if (url == null) {
			return null;
		}
		try {
			if (url.getProtocol().toLowerCase().equals("http")){
				HttpURLConnection connection = (HttpURLConnection) url.openConnection();
				return Drawable.createFromStream(connection.getInputStream(),"image");
			}else{
				HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
				return Drawable.createFromStream(connection.getInputStream(),"image");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * Drawble转String
	 */
    @SuppressLint("NewApi")
	public  static synchronized  String drawableToString(Drawable drawable) {    
        
        if (drawable != null) {    
            Bitmap bitmap = Bitmap    
                    .createBitmap(    
                            drawable.getIntrinsicWidth(),    
                            drawable.getIntrinsicHeight(),    
                            drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888    
                                    : Bitmap.Config.RGB_565);    
            Canvas canvas = new Canvas(bitmap);    
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),    
                    drawable.getIntrinsicHeight());    
            drawable.draw(canvas);    
            int size = bitmap.getWidth() * bitmap.getHeight() * 4;    
            
            // 创建一个字节数组输出流,流的大小为size    
            ByteArrayOutputStream baos = new ByteArrayOutputStream(size);    
            // 设置位图的压缩格式，质量为100%，并放入字节数组输出流中    
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);    
            // 将字节数组输出流转化为字节数组byte[]    
            byte[] imagedata = baos.toByteArray();    
              
           String icon= Base64.encodeToString(imagedata, Base64.DEFAULT);  
            return icon;    
        }    
        return null;    
    }
    /**
     * String转Drawble
     */
    @SuppressLint("NewApi")
	public static synchronized Drawable stringToDrawable(String icon) {    
        
        byte[] img=Base64.decode(icon.getBytes(), Base64.DEFAULT);  
        Bitmap bitmap;    
        if (img != null) {    
            bitmap = BitmapFactory.decodeByteArray(img,0, img.length);    
            @SuppressWarnings("deprecation")  
            Drawable drawable = new BitmapDrawable(bitmap);    
                
            return drawable;    
        }    
        return null;    
    
    } 
}
