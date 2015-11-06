package webview.demo.com.webviewdemo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	private WebView contentWebView = null;
	private TextView msgView = null;

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		contentWebView = (WebView) findViewById(R.id.webview);
		msgView = (TextView) findViewById(R.id.msg);
		//启用js
		contentWebView.getSettings().setJavaScriptEnabled(true);
		contentWebView.addJavascriptInterface(this, "wjj");
		//加载HTML
		contentWebView.loadUrl("file:///android_asset/wjj.html");

		Button button = (Button) findViewById(R.id.button);
		button.setOnClickListener(btnClickListener);
	}

	OnClickListener btnClickListener = new OnClickListener() {
		public void onClick(View v) {
			contentWebView.loadUrl("javascript:javacalljs()");
			contentWebView.loadUrl("javascript:javacalljswithargs(" + "'hello world'" + ")");
		}
	};

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
}