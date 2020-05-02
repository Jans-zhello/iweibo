package com.example.iweibo.ui;
import com.example.iweibo.R;
import com.example.iweibo.bean.Task;
import com.example.iweibo.logic.MainService;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
/**
 * 
 * ������Ȩwebҳ��
 * @author Administrator
 *
 */
public class WebViewActivity extends Activity implements IweiboActivity {
	
	private WebView webView;
    private String url;
    private ProgressDialog progressDialog;
    public static String CODE;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web_view); 
        init();
        Task task = new Task(Task.GET_AccessToken_URL,null);
        MainService.newTask(task);
		MainService.addActivity(this);
    }
    public void init(){
      if (progressDialog == null) {
		progressDialog = new ProgressDialog(this);
	  }
  	progressDialog.setMessage(getString(R.string.get_access_URL_ing));
  	progressDialog.show();
	  webView = (WebView) findViewById(R.id.web_view);
	  webView.getSettings().setJavaScriptEnabled(true);
	  //����WebViewĬ��ʹ�õ�������ϵͳĬ�����������ҳ����Ϊ��ʹ��ҳ��WebView��
	  webView.setWebViewClient(new WebViewClient(){
          @Override
       public boolean shouldOverrideUrlLoading(WebView view, String url) {
        	  
           System.out.println("shouldOverrideUrlLoading��ֵ:"+url);
       	   if (url.contains("code=")) {
			CODE = url.substring(url.indexOf("code")+5);
			if (CODE != null) {
				Intent intent = new Intent(WebViewActivity.this, AccessActivity.class);
	 		    startActivity(intent);	
			}
        }
		return true;
      }
       public void onPageFinished(WebView view, String url) {
    	 System.out.println("onPageFinished��ֵ"+url);
       	super.onPageFinished(view, url);
       	progressDialog.dismiss();
       }
      });
      webView.setWebChromeClient(new WebChromeClient(){
   	 public void onProgressChanged(WebView view, int newProgress) {
   		 progressDialog.setMessage(getString(R.string.loading)+newProgress+"%");
   		 super.onProgressChanged(view, newProgress);
   	}      
      });
      }
	public void refresh(Object... objects) {
		url = (String) objects[0];
	        //WebView����web��Դ
	    webView.loadUrl(url);
	   
	}
}


  