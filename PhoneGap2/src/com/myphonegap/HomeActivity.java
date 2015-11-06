package com.myphonegap;

import org.apache.cordova.DroidGap;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

public class HomeActivity extends DroidGap {
	/** Called when the activity is first created. */

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 解决黑屏问题
		super.init();
		this.appView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
		this.appView.addJavascriptInterface(new PluginMethod(this, appView), "wjj");
		this.appView.setBackgroundResource(R.drawable.login);
		super.setIntegerProperty("splashscreen", R.drawable.main_bg);
		// 固定页脚
		this.getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
		super.loadUrl("file:///android_asset/www/index.html", 1500);
	}

	

	/* 弹出吐司 */
	public void makeToast() {
		Toast.makeText(this, "弹出了内容", Toast.LENGTH_SHORT).show();
	}
	
	
	class PluginMethod {
	    private WebView webView;
	    private DroidGap droidGap;
	 
	    public PluginMethod(DroidGap gap, WebView view) {
	        webView = view;
	        droidGap = gap;
	    }
	 
	    /* native和JS传参 */
		public void goToActivity(final String str,final String str1) {
			Log.d("goToActivity", " 帐号 "+str+" "+" 密码 "+str1);
			Intent intent = new Intent();
			intent.putExtra("name", str);
			intent.putExtra("pass", str1);
			intent.setClass(HomeActivity.this, TestActivity.class);
			startActivity(intent);
		}
	    
	    public void makeToast(final String value) {
	        Toast.makeText(HomeActivity.this, "获取到了"+value, Toast.LENGTH_SHORT).show();
	    }
	    
	    public void getvalue(final String value) {
	        Toast.makeText(HomeActivity.this, "rande获取到了"+value, Toast.LENGTH_SHORT).show();
	    }
	}

}