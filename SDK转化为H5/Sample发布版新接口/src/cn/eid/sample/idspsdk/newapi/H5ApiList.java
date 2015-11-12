package cn.eid.sample.idspsdk.newapi;

import java.util.List;

import com.decard.ble.cardreader.DcCardReader;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnCancelListener;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;
import cn.eid.sample.idspsdk.common.BaseActivity;
import cn.eid.sample.idspsdk.common.BaseApplication;
import cn.eid.sample.idspsdk.common.MyListDlg;
import cn.eid.sample.idspsdk.common.MyLoadingDlg;
import cn.eid.sample.idspsdk.common.MyPromptDlg;
import cn.eid.sample.idspsdk.common.MyListDlg.DiscoverFinishedTipClickListener;
import cn.eid.tools.bluetooth.BluetoothMgr;
import cn.eid.tools.bluetooth.BluetoothMgr.ScanPeriod;
import cn.eid.tools.bluetooth.ble.BleResult;
import cn.eid.tools.bluetooth.ble.IScanListener;

public class H5ApiList extends Activity {

	private static final String TAG = H5ApiList.class.getName();

	// NFC
	public static final String ACTION_NFC = TAG + ".ACTION_NFC";

	// 德卡读卡器
	public static final String ACTION_BLE_DECARD = TAG + ".ACTION_BLE_DECARD";

	private Resources res;
	// 金联信息读卡器（接触式）
	BluetoothMgr blMgr = null;
	private static Handler handler = null;
	Thread connectBleDeviceThread = null;
	private MyListDlg devListDlg = null;
	private MyPromptDlg btNotOpenDlg = null;
	private MyPromptDlg btConnFailedDlg = null;
	private MyPromptDlg reDisDlg = null;
	protected MyLoadingDlg progressDlg = null;

	private static final int MSG_CONNECT_BEGIN = 1;
	private static final int MSG_CONNECT_OK = 2;
	private static final int MSG_FAILED = 3;

	String action = "";
	private WebView contentWebView = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.h5_list);
		action = getIntent().getAction();
		res = getResources();
		Log.d("---->action", action);

		contentWebView = (WebView) findViewById(R.id.webview);
		WebSettings webSettings = contentWebView.getSettings();
		initWebView(webSettings);

		initBLE();
		initializeHandler();
	}

	void initBLE() {

		if (BluetoothMgr.isSupportedBLE(this)) {

			blMgr = BluetoothMgr.getInstance(this);
			Log.d(TAG, "BluetoothMgr version: \"" + BluetoothMgr.getVersion()
					+ "\"");

			// 记录cardreader对象
			BaseApplication app = (BaseApplication) getApplication();
			app.setDcReader(new DcCardReader(this));

			Log.d(TAG, "DcCardReader device type: \""
					+ app.getDcReader().GetDeviceTypeName()
					+ "\", device version: \""
					+ app.getDcReader().GetDeviceVersion() + "\"");

		}

	}

	private void initializeHandler() {

		Looper looper;
		if (null != (looper = Looper.myLooper())) {

			handler = new EventHandler(this, looper);

		} else if (null != (looper = Looper.getMainLooper())) {

			handler = new EventHandler(this, looper);

		} else {

			handler = null;

		}
	}

	private static class EventHandler extends Handler {

		private H5ApiList curAT = null;

		public EventHandler(H5ApiList curAT, Looper looper) {
			super(looper);

			this.curAT = curAT;

		}

		public void handleMessage(Message msg) {

			if (null != curAT) {

				curAT.processMsg(msg);
				return;

			}

			super.handleMessage(msg);

		}

	}

	private void processMsg(Message msg) {

		switch (msg.what) {

		case MSG_CONNECT_BEGIN:
			Log.d("--->MSG_CONNECT_BEGIN", "MSG_CONNECT_BEGIN");
			showProgressDlg(R.string.bluetooth_connecting);
			break;

		case MSG_CONNECT_OK:
			Log.d("--->MSG_CONNECT_OK", "MSG_CONNECT_OK");
			// disConnectDevice.setEnabled(true);
			contentWebView.loadUrl("javascript:changeConnectState(" + "'"
					+ "设备已连接" + "'" + ")");

			hideProgressDlg();
			break;

		case MSG_FAILED:
			hideProgressDlg();
			// disConnectDevice.setEnabled(false);
			contentWebView.loadUrl("javascript:changeConnectState(" + "'"
					+ "设备已断开" + "'" + ")");

			String more = (String) msg.obj;
			showPromptDlgForBTFailed(more);
			break;

		default:
			break;

		}
	}

	private IScanListener scanListener = new IScanListener() {

		@Override
		public void onScanFound(BleResult result) {

			final BluetoothDevice device = result.device;
			String devName = device.getName();
			String devMAC = device.getAddress();
			int rssi = result.rssi;
			Log.d(TAG, "onScanFound - devName = " + devName);
			Log.d(TAG, "onScanFound - devMAC = " + devMAC);
			Log.d(TAG, "onScanFound - rssi = " + rssi);
			if (null == devName || devName.length() == 0 || null == devMAC
					|| devMAC.length() == 0) {

				Log.w(TAG, "onScanFound - device name or MAC is NOT invalid!");
				return;

			}
			blMgr.addOne(result);

			hideProgressDlg();
			showListDeviceDlg();

		}

		@Override
		public void onScanFailed(int errorCode) {

			Log.d(TAG, "onScanFailed - errorCode = " + errorCode);

			hideListDeviceDlg();
			hideRediscoverDialog();
			hideProgressDlg();

			showPromptDlgForBTFailed("onScanFailed - errorCode = " + errorCode);

		}

		@Override
		public void onScanFinished() {

			Log.d(TAG, "onScanFinished");

			hideProgressDlg();

			if (blMgr.isDevListEmpty()) {

				hideListDeviceDlg();
				showRediscoverDialog(R.string.bluetooth_not_found);

			} else {

				if (null != devListDlg && devListDlg.isShowing()) {

					devListDlg.showProgress(false);
					devListDlg.showTip(true);

				}

			}

		}

	};

	@SuppressLint({ "JavascriptInterface", "SetJavaScriptEnabled" })
	public void initWebView(WebSettings webSettings) {
		// 启用js
		webSettings.setJavaScriptEnabled(true);
		contentWebView.addJavascriptInterface(H5ApiList.this, "wjj");
		// 适配操作
		webSettings.setDisplayZoomControls(false);
		webSettings.setDomStorageEnabled(true);// 允许本地缓存

		// LOAD_CACHE_ONLY: 不使用网络，只读取本地缓存数据。
		// LOAD_DEFAULT: 根据cache-control决定是否从网络上取数据。
		// LOAD_CACHE_NORMAL: API level 17中已经废弃, 从API level
		// 11开始作用同LOAD_DEFAULT模式。
		// LOAD_NO_CACHE: 不使用缓存，只从网络获取数据。
		// LOAD_CACHE_ELSE_NETWORK，只要本地有，无论是否过期，或者no-cache，都使用缓存中的数据。

		webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);// 缓存模式
		webSettings.setUseWideViewPort(true);// 关键点
		webSettings.setAllowFileAccess(true); // 允许访问文件
		webSettings.setBuiltInZoomControls(true); // 设置显示缩放按钮
		webSettings.setSupportZoom(true); // 支持缩放

		webSettings.setLoadWithOverviewMode(true);

		String cacheDirPath = getCacheDir().getAbsolutePath()
				+ "/webViewCache ";
		// 开启 database storage API 功能
		webSettings.setDatabaseEnabled(true);
		// 设置数据库缓存路径
		webSettings.setDatabasePath(cacheDirPath);
		// 开启Application H5 Caches 功能
		webSettings.setAppCacheEnabled(true);
		// 设置Application Caches 缓存目录
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
		webSettings
				.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);

		if (Build.VERSION.SDK_INT >= 19) {
			webSettings.setLoadsImagesAutomatically(true);
		} else {
			webSettings.setLoadsImagesAutomatically(false);
		}

		// 加载HTML
		contentWebView.loadUrl("file:///android_asset/list.html");

		contentWebView.setWebChromeClient(new WebChromeClient() {
			@Override
			public boolean onConsoleMessage(ConsoleMessage consoleMessage) { // TODO
																				// Auto-generated
																				// method
																				// stub
				if (consoleMessage.message()
						.contains("Uncaught ReferenceError")) {
					Log.d("--->onConsoleMessage", consoleMessage.message());
				}
				return super.onConsoleMessage(consoleMessage);
			}

			// 进度变化
			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				super.onProgressChanged(view, newProgress);
				Log.d("--->onProgressChanged", "onProgressChanged");
			}
		});

		contentWebView.setWebViewClient(new WebViewClient() {

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				// TODO Auto-generated method stub
				super.onPageStarted(view, url, favicon);
				Log.d("--->onPageStarted", "onPageStarted");
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
				// 网页加载完成时处理 如：让 加载对话框 消失
				Log.d("--->onPageFinished", "onPageFinished");
				if (!contentWebView.getSettings().getLoadsImagesAutomatically()) {
					contentWebView.getSettings().setLoadsImagesAutomatically(
							true);
				}
				createControls();

			}

			@Override
			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl) {
				super.onReceivedError(view, errorCode, description, failingUrl);
				// 加载网页失败时处理 如：
				Log.d("--->onReceivedError", "onReceivedError");
			}
		});
	}

	// 初始化
	void createControls() {
		String selectedReader = String.format(
				res.getString(R.string.readerSelected),
				getReaderNameByAction(action));
		contentWebView.loadUrl("javascript:javacalljssetreadername(" + "'"
				+ selectedReader + "'" + ")");

		contentWebView.loadUrl("javascript:changeConnectState(" + "'" + "设备未连接"
				+ "'" + ")");
		
		// 蓝牙模式下显示，连接状态
		if (ACTION_BLE_DECARD.equals(action)) {

			contentWebView.loadUrl("javascript:HiddenBtn(" + "'" + "d" + "'"
					+ ")");

		} else {

			contentWebView.loadUrl("javascript:HiddenBtn(" + "'" + "h" + "'"
					+ ")");

		}

	}

	String getReaderNameByAction(String action) {

		String readerName = "";
		if (action.equals(ACTION_NFC)) {

			readerName = res.getString(R.string.nfc);

		} else if (action.equals(ACTION_BLE_DECARD)) {

			readerName = res.getString(R.string.decardReader);

		}

		return readerName;

	}

	// js调用JAVA方法
	@JavascriptInterface
	public void jsToJava(final String str) {
		Toast.makeText(this, "js传来的是" + str, Toast.LENGTH_SHORT).show();

		switch (str) {
		case "scanDevice":
			doBleScan();
			break;
		case "disConnectDevice":
			doBleDeCardDisConnect();
			break;
		case "getPinRange":
			 runOnUiThread(new Runnable() {

		            @Override
		            public void run() {
		            	contentWebView.loadUrl("javascript:changeConnectState(" + "'" + "设备已连接"
		        				+ "'" + ")");
		            }
		        });
			break;
		case "changePin":
			break;
		case "login":

			break;
		case "isEIDCard":

			break;
		case "getFinancialCardInfo":

			break;
		case "getCardBankNO":

			break;
		case "getRandom":

			break;
		case "getAbilityInfo":

			break;
		case "getCert":

			break;
		case "hash":

			break;
		case "sign":

			break;
		case "verify":

			break;
		case "ErrorCodeTest":

			break;

		default:
			break;
		}
	}

	void doBleScan() {

		if (!blMgr.isEnabled()) {

			notifyBTNotOpen();
			return;

		}

		scanBleReader();
	}

	void scanBleReader() {
		  
		showProgressDlg(R.string.scan_ble);

		blMgr.clearAllDevices();
		blMgr.startScan(ScanPeriod.SP_MIN, scanListener);
	}

	void doBleDeCardDisConnect() {

		BaseApplication app = (BaseApplication) getApplication();
		DcCardReader dcReader = app.getDcReader();
		if (null != dcReader) {

			dcReader.disconnect();
			app.setDcReader(null);

		}

		contentWebView.loadUrl("javascript:changeConnectState(" + "'" + "设备已断开"
				+ "'" + ")");
		// disConnectDevice.setEnabled(false);

	}

	void connectDevice(String macAddr) {

		// 连接设备
		Log.d(TAG, "connectDevice \"" + macAddr + "\" ...");

		startConnectBleDeviceThread(macAddr);

	}

	void startConnectBleDeviceThread(String devInfo) {

		if (null != connectBleDeviceThread) {

			connectBleDeviceThread.interrupt();
			connectBleDeviceThread = null;

		}

		connectBleDeviceThread = new ConnectBleDeviceThread(this, devInfo);
		connectBleDeviceThread.start();

	}

	private class ConnectBleDeviceThread extends Thread {

		Context context;
		String devInfo;

		private ConnectBleDeviceThread(Context context, String devInfo) {
			super();

			this.context = context;
			this.devInfo = devInfo;

		}

		@Override
		public void run() {
			super.run();

			handler.sendEmptyMessage(MSG_CONNECT_BEGIN);

			BaseApplication app = (BaseApplication) getApplication();
			boolean ret = app.getDcReader().init();
			if (!ret) {

				Message msg = handler
						.obtainMessage(MSG_FAILED, "初始化BLE终端管理器失败");
				handler.sendMessage(msg);
				return;

			}

			int code = app.getDcReader().connect(devInfo);
			if (DcCardReader.CONNECT_SUCESS != code) {

				Message msg = handler.obtainMessage(MSG_FAILED, "连接BLE终端\""
						+ devInfo + "\"失败， code = " + code);
				handler.sendMessage(msg);
				return;

			}

			handler.sendEmptyMessage(MSG_CONNECT_OK);

		}

	}

	void notifyBTNotOpen() {

		showPromptDlgForBTNotOpen();

	}

	protected void showPromptDlgForBTNotOpen() {

		if (this.isFinishing()) {

			return;

		}

		if (null == btNotOpenDlg) {

			MyPromptDlg.Builder builder = new MyPromptDlg.Builder(this);

			builder.setTitle(res.getString(R.string.prompt_dlg_title))
					.setText(res.getString(R.string.bt_check_message))
					.setPositiveButton(res.getString(R.string.common_ok),
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {

									dialog.dismiss();
									openBTSettings();

								}

							})
					.setNegativeButton(res.getString(R.string.common_cancel),
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {

									dialog.dismiss();

								}

							});

			btNotOpenDlg = builder.create();
			btNotOpenDlg.setCancelable(false);
			btNotOpenDlg.setCanceledOnTouchOutside(false);
		}

		if (!btNotOpenDlg.isShowing()) {

			btNotOpenDlg.show();

		}

	}

	void hideListDeviceDlg() {

		if (null != devListDlg) {

			devListDlg.hide();
			devListDlg.dismiss();
			devListDlg = null;

		}

	}

	protected void showPromptDlgForBTFailed(String tip) {

		if (this.isFinishing()) {

			return;

		}

		if (null == btConnFailedDlg) {

			MyPromptDlg.Builder builder = new MyPromptDlg.Builder(this);

			builder.setTitle(res.getString(R.string.prompt_dlg_title))
					.setText(tip)
					.setPositiveButton(res.getString(R.string.common_ok),
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {

									dialog.dismiss();
									scanBleReader();

								}

							});
			builder.setNegativeButton(res.getString(R.string.common_cancel),
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {

							dialog.dismiss();

						}

					});

			btConnFailedDlg = builder.create();
			btConnFailedDlg.setCancelable(false);
			btConnFailedDlg.setCanceledOnTouchOutside(false);
		}

		if (!btConnFailedDlg.isShowing()) {

			btConnFailedDlg.show();

		}

	}

	void showListDeviceDlg() {

		if (null == this || this.isFinishing()) {

			return;

		}

		if (devListDlg == null) {

			devListDlg = new MyListDlg.Builder(this)
					.setTitle(res.getString(R.string.bluetooth_find_title))
					.setDiscoverFinishedTipClickListener(
							new DiscoverFinishedTipClickListener() {

								@Override
								public void onClick(View view) {

									devListDlg.dismiss();

									if (!blMgr.isEnabled()) {

										notifyBTNotOpen();

									} else {

										scanBleReader();

									}

								}
							})
					.setMyListItemClickListener(
							new MyListDlg.MyListItemClickListener() {

								@Override
								public void itemClick(final int pos) {

									devListDlg.dismiss();

									blMgr.stopScan();

									final BleResult result = blMgr.findOne(pos);
									connectDevice(result.device.getAddress());

								}

							}).create();

			devListDlg.showProgress(true);

			devListDlg.setOnCancelListener(new OnCancelListener() {

				@Override
				public void onCancel(DialogInterface dialog) {

					devListDlg.showTip(false);
					devListDlg.dismiss();
					blMgr.stopScan();
					blMgr.clearAllDevices();

				}

			});

		}

		if (!devListDlg.isShowing()) {

			devListDlg.show();

		}

		List<BleResult> items = blMgr.getDevList();
		devListDlg.updateListItems(items);

	}

	private void showRediscoverDialog(int tipResId) {

		if (this.isFinishing()) {

			return;

		}

		String tip = res.getString(tipResId);

		if (null == reDisDlg) {

			MyPromptDlg.Builder builder = new MyPromptDlg.Builder(this);
			builder.setTitle(res.getString(R.string.prompt_dlg_title))
					.setText(tip)
					.setPositiveButton(
							res.getString(R.string.bluetooth_rescan),
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {

									dialog.dismiss();

									if (!blMgr.isEnabled()) {

										notifyBTNotOpen();

									} else {

										scanBleReader();

									}

								}

							});
			builder.setNegativeButton(res.getString(R.string.common_cancel),
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {

							dialog.dismiss();

						}

					});

			reDisDlg = builder.create();
			reDisDlg.setCancelable(false);
			reDisDlg.setCanceledOnTouchOutside(false);

		}

		if (!reDisDlg.isShowing()) {

			reDisDlg.setMyText(tip);
			reDisDlg.show();

		}

	}

	void hideRediscoverDialog() {

		if (null != reDisDlg) {

			reDisDlg.hide();
			reDisDlg.dismiss();
			reDisDlg = null;

		}

	}

	void releaseDlgs() {

		if (null != btNotOpenDlg) {

			btNotOpenDlg.dismiss();
			btNotOpenDlg = null;

		}

		if (null != btConnFailedDlg) {

			btConnFailedDlg.dismiss();
			btConnFailedDlg = null;

		}

		if (null != reDisDlg) {

			reDisDlg.dismiss();
			reDisDlg = null;

		}

		if (null != devListDlg) {

			devListDlg.dismiss();
			devListDlg = null;

		}

	}

	private void hideProgressDlg() {

		if (this.isFinishing()) {

			Log.w(TAG, "hideProcessDlg - me has been finish!");
			return;

		}

		if (null != progressDlg && progressDlg.isShowing()) {

			// progressDlg.hide();
			progressDlg.stopAnim();
			progressDlg.dismiss();
			progressDlg = null;

		}

	}

	private void showProgressDlg(String text) {

		if (this.isFinishing()) {

			Log.w(TAG, "showProgressDlg - me has been finish!");
			return;

		}

		if (null == progressDlg) {

			MyLoadingDlg.Builder builder = new MyLoadingDlg.Builder(this);
			progressDlg = builder.create();
			progressDlg.setCancelable(false);
			progressDlg.setCanceledOnTouchOutside(false);
			progressDlg.startAnim();

		}

		progressDlg.setText(text);

		if (!progressDlg.isShowing()) {

			progressDlg.show();

		}

	}

	private void showProgressDlg(int textResId) {

		showProgressDlg(res.getString(textResId));

	}

	private void openBTSettings() {

		Intent intent = new Intent(Settings.ACTION_BLUETOOTH_SETTINGS);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);

	}

	@Override
	protected void onDestroy() {

		if (null != handler) {

			handler.removeCallbacksAndMessages(null);

			handler = null;

		}

		doBleDeCardDisConnect();

		releaseDlgs();

		super.onDestroy();
	}
}
