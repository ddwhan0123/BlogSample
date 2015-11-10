package webview.demo.com.webviewdemo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.ConsoleMessage;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import cn.pedant.SweetAlert.SweetAlertDialog;
import webview.demo.com.webviewdemo.tools.BlueToothTools;
import webview.demo.com.webviewdemo.tools.NFCTools;

public class MainActivity extends Activity {

    private WebView contentWebView = null;
    private TextView msgView = null;
    private static boolean sdkInt;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("--->onCreate", "onCreate");
        setContentView(R.layout.activity_main);
        checkSDK();
        contentWebView = (WebView) findViewById(R.id.webview);
        msgView = (TextView) findViewById(R.id.msg);

        WebSettings webSettings = contentWebView.getSettings();
        initWebView(webSettings);
        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(btnClickListener);
    }

    OnClickListener btnClickListener = new OnClickListener() {
        public void onClick(View v) {
            contentWebView.loadUrl("javascript:javacalljs()");
            String str="你好,地球人";
            contentWebView.loadUrl("javascript:javacalljswithargs(" + "'"+str+"'" + ")");
        }
    };

    //返回上一个访问的页面
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (contentWebView.canGoBack() && event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            contentWebView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void initWebView(WebSettings webSettings){
        //启用js
        webSettings.setJavaScriptEnabled(true);
        contentWebView.addJavascriptInterface(this, "wjj");
        //适配操作
        webSettings.setDisplayZoomControls(false);
        webSettings.setDomStorageEnabled(true);//允许本地缓存

        //        LOAD_CACHE_ONLY: 不使用网络，只读取本地缓存数据。
//        LOAD_DEFAULT: 根据cache-control决定是否从网络上取数据。
//        LOAD_CACHE_NORMAL: API level 17中已经废弃, 从API level 11开始作用同LOAD_DEFAULT模式。
//        LOAD_NO_CACHE: 不使用缓存，只从网络获取数据。
//        LOAD_CACHE_ELSE_NETWORK，只要本地有，无论是否过期，或者no-cache，都使用缓存中的数据。

        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);//缓存模式
        webSettings.setUseWideViewPort(true);//关键点
        webSettings.setAllowFileAccess(true); // 允许访问文件
        webSettings.setBuiltInZoomControls(true); // 设置显示缩放按钮
        webSettings.setSupportZoom(true); // 支持缩放

        webSettings.setLoadWithOverviewMode(true);

        String cacheDirPath = getCacheDir().getAbsolutePath()+ "/webViewCache ";
        //开启 database storage API 功能
        webSettings.setDatabaseEnabled(true);
        //设置数据库缓存路径
        webSettings.setDatabasePath(cacheDirPath);
        //开启Application H5 Caches 功能
        webSettings.setAppCacheEnabled(true);
        //设置Application Caches 缓存目录
        webSettings.setAppCachePath(cacheDirPath);

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int mDensity = metrics.densityDpi;
        Log.d("densityDpi", "densityDpi = " + mDensity);
        if (mDensity == 240) {
            webSettings.setDefaultZoom(WebSettings.ZoomDensity.FAR);
        } else if (mDensity == 160) {
            webSettings.setDefaultZoom(WebSettings.ZoomDensity.MEDIUM);
        } else if (mDensity == 120) {
            webSettings.setDefaultZoom(WebSettings.ZoomDensity.CLOSE);
        } else if (mDensity == DisplayMetrics.DENSITY_XHIGH) {
            webSettings.setDefaultZoom(WebSettings.ZoomDensity.FAR);
        } else if (mDensity == DisplayMetrics.DENSITY_TV) {
            webSettings.setDefaultZoom(WebSettings.ZoomDensity.FAR);
        } else {
            webSettings.setDefaultZoom(WebSettings.ZoomDensity.MEDIUM);
        }
        /**
         * 用WebView显示图片，可使用这个参数 设置网页布局类型： 1、LayoutAlgorithm.NARROW_COLUMNS ：
         * 适应内容大小 2、LayoutAlgorithm.SINGLE_COLUMN:适应屏幕，内容将自动缩放
         */
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);

        if(Build.VERSION.SDK_INT >= 19) {
            webSettings.setLoadsImagesAutomatically(true);
        } else {
            webSettings.setLoadsImagesAutomatically(false);
        }

        //加载HTML
        contentWebView.loadUrl("file:///android_asset/wjj.html");
//        contentWebView.loadUrl("https://www.baidu.com/");

        contentWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onConsoleMessage(ConsoleMessage consoleMessage) {       // TODO Auto-generated method stub
                if (consoleMessage.message().contains("Uncaught ReferenceError")) {
                    Log.d("--->onConsoleMessage", consoleMessage.message());
                }
                return super.onConsoleMessage(consoleMessage);
            }
            //进度变化
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                Log.d("--->onProgressChanged", "onProgressChanged");
            }
        });

        contentWebView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                // 开始加载网页时处理 如：显示"加载提示" 的加载对话框
                Log.d("--->onPageStarted", "onPageStarted");
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                // 网页加载完成时处理  如：让 加载对话框 消失
                Log.d("--->onPageFinished", "onPageFinished");
                if (!contentWebView.getSettings().getLoadsImagesAutomatically()) {
                    contentWebView.getSettings().setLoadsImagesAutomatically(true);
                }
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                // 加载网页失败时处理  如：
                Log.d("--->onReceivedError", "onReceivedError");
            }
        });
    }

    // 判断版本
    private boolean checkSDK() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {

            sdkInt = true;

            return sdkInt;

        } else {

            sdkInt = false;

            return sdkInt;
        }
    }

    @JavascriptInterface
    public void startFunction() {
        Toast.makeText(this, "js调用java", Toast.LENGTH_SHORT).show();
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                msgView.setText(msgView.getText() + "\njs调用java");

            }
        });
    }

    @JavascriptInterface
    public void startFunction(final String str) {
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                msgView.setText(msgView.getText() + "\njs调用java传递参数" + str);
            }
        });
    }

    @JavascriptInterface
    public void startRangePoints(final String points) {
        Toast.makeText(this, "points获取的值是 " + points, Toast.LENGTH_SHORT).show();
    }

    @JavascriptInterface
    public void startgetBrowsers(final String broValue){
        Toast.makeText(this, "broValue获取的值是 " + broValue, Toast.LENGTH_SHORT).show();
    }

    /*判断是否支持NFC*/
    @JavascriptInterface
    public void checkNFCAvailable(){
        boolean nfcFlag= NFCTools.getInstance(this).isAvailable();
        if(nfcFlag){
            Toast.makeText(this, "该设备支持NFC" , Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "该设备不支持NFC" , Toast.LENGTH_SHORT).show();
        }
    }

    /*判断是否支持常规蓝牙*/
    @JavascriptInterface
    public void checkBlueToothavailable(){
        boolean blueToothFlag= BlueToothTools.getInstance(this).available();
        if(blueToothFlag){
            Toast.makeText(this, "支持常规蓝牙" , Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "不支持常规蓝牙" , Toast.LENGTH_SHORT).show();
        }
    }

    /*判断是否支持BLE*/
    @JavascriptInterface
    public void checkSupportBLE(){
        if(sdkInt){
            Toast.makeText(this, "支持BLE" , Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "不支持BLE" , Toast.LENGTH_SHORT).show();
        }
    }

    /*判断常规蓝牙是否可用*/
    @JavascriptInterface
    public void checkBlueToothisEnabled(){
        boolean blueToothFlag= BlueToothTools.getInstance(this).isEnabled();
        if(blueToothFlag){
            Toast.makeText(this, "蓝牙已开启" , Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "蓝牙未开启" , Toast.LENGTH_SHORT).show();

            new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("蓝牙未开启")
                    .setContentText("不开启将无法使用其他功能")
                    .setCancelText("取消")
                    .setConfirmText("开启蓝牙")
                    .showCancelButton(true)
                    .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            // reuse previous dialog instance, keep widget user state, reset them if you need
                            sDialog.dismissWithAnimation();
                        }
                    })
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            Intent intent = new Intent(Settings.ACTION_BLUETOOTH_SETTINGS);
                            MainActivity.this.startActivity(intent);
                            sDialog.dismiss();
                        }
                    })
                    .show();
        }
    }

    /*判断NFC是否可用*/
    @JavascriptInterface
    public void checkNFCisEnabled(){
        boolean enabler = NFCTools.getInstance(this).isEnabled();
        if(enabler){
            Toast.makeText(this, "NFC已开启" , Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "NFC未开启" , Toast.LENGTH_SHORT).show();

            new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("NFC未开启")
                    .setContentText("不开启将无法使用其他功能")
                    .setCancelText("取消")
                    .setConfirmText("开启NFC")
                    .showCancelButton(true)
                    .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            // reuse previous dialog instance, keep widget user state, reset them if you need
                            sDialog.dismissWithAnimation();
                        }
                    })
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            Intent intent = new Intent(Settings.ACTION_NFC_SETTINGS);
                            MainActivity.this.startActivity(intent);
                            sDialog.dismiss();
                        }
                    })
                    .show();
        }
    }

}