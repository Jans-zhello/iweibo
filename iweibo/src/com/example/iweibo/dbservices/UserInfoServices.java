package com.example.iweibo.dbservices;
import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;
import com.example.iweibo.db.DBHelper;
import com.example.iweibo.db.UserInfo;
import com.example.iweibo.util.ImageInfoExchange;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
public class UserInfoServices {
  private DBHelper dbHelper;
  private String[] columns = {UserInfo.ID,UserInfo.USERID,UserInfo.USERNAME,UserInfo.ACCESSTOKEN,UserInfo.ISDEFAULT,UserInfo.USERICON};
  public UserInfoServices(Context context){
	  dbHelper = new DBHelper(context);
	  
  }
  /**
   * 添加用戶信息
   * @param userInfo
   */
	public void inserUser(UserInfo userInfo){
		
		
		SQLiteDatabase database = dbHelper.getWritableDatabase();
		ContentValues values = new ContentValues(2);
		Bitmap bitmap = ImageInfoExchange.drawableToBitmap(userInfo.getUserIcon());  
		byte[] iconbyte = ImageInfoExchange.BitmapToByte(bitmap);
		values.put(UserInfo.USERID,userInfo.getUserId());
		values.put(UserInfo.USERNAME,userInfo.getUserName());
		values.put(UserInfo.ACCESSTOKEN,userInfo.getAccessToken());
		values.put(UserInfo.ISDEFAULT,userInfo.getIsDefault());
		values.put(UserInfo.USERICON,iconbyte);
		database.insert(UserInfo.USER_TABLE_NAME, null, values);
		database.close();
	}
	/**
	 * 根据userid获取用户信息
	 */
	public UserInfo getUserInfoByUserId(String userid){
	  SQLiteDatabase database = dbHelper.getReadableDatabase();
	  UserInfo userInfo = null;
	  Cursor cursor = database.query(UserInfo.USER_TABLE_NAME, columns,
			  UserInfo.USERID+"=?",
			  new String[]{userid},null,null,null);
	  if (cursor.moveToFirst() == false) {
		return null;
	  }
		if (cursor.getCount()>0 && cursor !=null) {
			Long id = cursor.getLong(cursor.getColumnIndex(UserInfo.ID));
			String userId = cursor.getString(cursor.getColumnIndex(UserInfo.USERID));
			String username = cursor.getString(cursor.getColumnIndex(UserInfo.USERNAME));
			String accesstoken = cursor.getString(cursor.getColumnIndex(UserInfo.ACCESSTOKEN));
			String isdefault = cursor.getString(cursor.getColumnIndex(UserInfo.ISDEFAULT));
			byte[] byteIcon = cursor.getBlob(cursor.getColumnIndex(UserInfo.USERICON));
			Drawable icondrawable = null;
			if (byteIcon != null) {
				ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteIcon);
			    icondrawable = Drawable.createFromStream(byteArrayInputStream, "image");
			}
			userInfo = new UserInfo(id,userId,username,accesstoken,isdefault,icondrawable); 
		}
	  cursor.close();
	  database.close();
	  return userInfo;
	}
	/**
	 * 更新用户信息
	 * 
	 */
     public void updateUserInfo(String  userId,String accessToken){
    	 SQLiteDatabase database = dbHelper.getReadableDatabase();
    	 ContentValues values = new ContentValues();
    	 values.put(UserInfo.ACCESSTOKEN,accessToken);
    	 database.update(UserInfo.USER_TABLE_NAME, values,UserInfo.USERID+"=?",new String[]{userId});
    	 database.close();
     }
     /**
      * 获取所有用户信息
      * 
      * 
      */
     public List<UserInfo> getAllUsers(){
    	 SQLiteDatabase database = dbHelper.getReadableDatabase();
    	 List<UserInfo> users = null;
    	 Cursor cursor = database.query(UserInfo.USER_TABLE_NAME, columns, null,null,null,null,null);
    	  if (cursor.getCount()>0 && cursor !=null) {
    		  users = new ArrayList<UserInfo>(cursor.getCount());
    		  UserInfo userInfo = null;
			   while(cursor.moveToNext()){
				    Long id = cursor.getLong(cursor.getColumnIndex(UserInfo.ID));
					String userId = cursor.getString(cursor.getColumnIndex(UserInfo.USERID));
					String username = cursor.getString(cursor.getColumnIndex(UserInfo.USERNAME));
					String accesstoken = cursor.getString(cursor.getColumnIndex(UserInfo.ACCESSTOKEN));
					String isdefault = cursor.getString(cursor.getColumnIndex(UserInfo.ISDEFAULT));
					byte[] byteIcon = cursor.getBlob(cursor.getColumnIndex(UserInfo.USERICON));
					Drawable icondrawable = null;
					if (byteIcon != null) {
						ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteIcon);
					    icondrawable = Drawable.createFromStream(byteArrayInputStream, "image");
					}
					userInfo = new UserInfo(id,userId,username,accesstoken,isdefault,icondrawable); 
					users.add(userInfo); 
    			  }
    		  }
    	     cursor.close();
    	     database.close();
    	     return users;
    	  }
}
