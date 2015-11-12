package cn.eid.sample.idspsdk.newapi;

import com.trimps.eid.sdk.reader.CardReader;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnCancelListener;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.eid.sample.idspsdk.common.ApiName;
import cn.eid.sample.idspsdk.common.BaseActivity;
import cn.eid.sample.idspsdk.common.BaseApplication;
import cn.eid.sample.idspsdk.common.MyChangePINDlg;
import cn.eid.sample.idspsdk.common.MyPINDlg;
import cn.eid.sample.idspsdk.common.BaseApiTest.ResultData;
import cn.eid.sample.idspsdk.common.reader.DcReader;
import cn.eid.sample.idspsdk.test.ErrorCodeTest;




public class ReadWithBleReader extends BaseActivity {

	public static final String TAG = ReadWithBleReader.class.getName();

	public static final String EXTRA_API_NAME = TAG + ".EXTRA_API_NAME";

	private String oldPin = "";
	private String newPin = "";

	private static Handler handler = null;
	private static final int MSG_READ_BEGIN = 0;
	private static final int MSG_READ_END = 1;
	private static final int MSG_ERROR = 2;


	Thread readCard = null;
	MyPINDlg pinDlg = null;
	MyChangePINDlg changePinDlg = null;

	String action = null;
	String apiName = null;
	
	boolean isScanning = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.read_with_ble_reader);

		Intent intent = getIntent();
		action = intent.getAction();
		apiName = intent.getStringExtra(EXTRA_API_NAME);

		hideResultPanel();
		initializeHandler();
		
		isScanning = false;

	}

	@Override
	protected void onResume() {
		super.onResume();

		if (null == apiName || apiName.length() == 0) {

			Log.w(TAG, "apiName = " + apiName);
			finish();

			return;

		}

		if (apiName.equals(ApiName.ErrorCodeTest)) {

			ErrorCodeTest errorTest = new ErrorCodeTest();
			ResultData result = errorTest.perform();
			if (null == result) {

				handler.sendEmptyMessage(MSG_ERROR);
				return;

			}

			Message msg = handler.obtainMessage(MSG_READ_END, result);
			handler.sendMessage(msg);

			return;

		}

		if (ApiName.login.equals(apiName) || ApiName.sign.equals(apiName)
				|| ApiName.verify.equals(apiName)) {

			if (null == oldPin || oldPin.length() == 0) {

				showVerifyPinDlg();
				return;

			}

		} else if (ApiName.changePin.equals(apiName)) {

			if (null == oldPin || oldPin.length() == 0 || null == newPin
					|| newPin.length() == 0) {

				showChangePinDlg();
				return;

			}

		}

		
		if (!isScanning) {
			
			scanBLE();
			
		} else {
			
			Toast.makeText(this, "正在扫描。。。", Toast.LENGTH_LONG).show();
			
		}

	}

	@Override
	protected void onDestroy() {

		Log.d(TAG, "onDestroy");

		if (null != handler) {

			handler.removeCallbacksAndMessages(null);

		}

		oldPin = "";
		newPin = "";

		if (null != pinDlg) {

			pinDlg.dismiss();
			pinDlg = null;

		}

		if (null != changePinDlg) {

			changePinDlg.dismiss();
			changePinDlg = null;

		}

		super.onDestroy();

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

		private ReadWithBleReader curAT = null;

		public EventHandler(ReadWithBleReader curAT, Looper looper) {
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

	void startReadCardThread(CardReader reader) {

		if (null != readCard) {

			readCard.interrupt();
			readCard = null;

		}

		handler.sendEmptyMessage(MSG_READ_BEGIN);

		readCard = new ReadCardThread(reader);
		readCard.start();

	}

	private class ReadCardThread extends Thread {

		CardReader reader;

		private ReadCardThread(CardReader reader) {
			super();

			this.reader = reader;

		}

		@Override
		public void run() {
			super.run();

			ResultData result = runTests(apiName, reader, oldPin, newPin);

			// result = null;
			if (null == result) {

				Message msg = handler.obtainMessage(MSG_ERROR,
						"result is null!");
				handler.sendMessage(msg);
				return;

			}

			Message msg = handler.obtainMessage(MSG_READ_END, result);
			handler.sendMessage(msg);

		}

	}

	private void processMsg(Message msg) {

		switch (msg.what) {

		// 开始执行，提示卡片贴紧勿动
		case MSG_READ_BEGIN:
			isScanning = true;
			hideResultPanel();
			if (action.equals(ApiList.ACTION_BLE_DECARD)) {

				showProgressDlg(R.string.reading_card);

			}
			break;

		// 执行完成，显示结果信息
		case MSG_READ_END:
			isScanning = false;
			hideProgressDlg();
			ResultData result = (ResultData) msg.obj;
			showResultPanel(apiName, result.isOK, result.more);
			break;

		// 出现错误
		case MSG_ERROR:
			isScanning = false;
			hideProgressDlg();
			String more = (String) msg.obj;
			showResultPanel(apiName, false, "错误：" + more);
			break;

		default:
			break;

		}
	}

	void showResultPanel(String apiName, boolean succeed, String resultMoreStr) {

		final LinearLayout resultPanel = (LinearLayout) findViewById(R.id.resultPanel);
		resultPanel.setVisibility(View.VISIBLE);

		final LinearLayout shadowPanel = (LinearLayout) findViewById(R.id.shadowPanel);
		shadowPanel.setVisibility(View.GONE);

		final TextView result = (TextView) findViewById(R.id.result);
		result.setText(String.format(res.getString(R.string.result), apiName));

		final TextView resultTag = (TextView) findViewById(R.id.resultTag);
		if (succeed) {

			resultTag.setText(res.getString(R.string.resultOK));
			resultTag.setTextColor(Color.parseColor("#00AE5F"));

		} else {

			resultTag.setText(res.getString(R.string.resultFailed));
			resultTag.setTextColor(Color.RED);

		}

		final TextView resultMore = (TextView) findViewById(R.id.resultMore);
		resultMore.setText(resultMoreStr);

	}

	void hideResultPanel() {

		final LinearLayout resultPanel = (LinearLayout) findViewById(R.id.resultPanel);
		resultPanel.setVisibility(View.GONE);

	}

	private void showChangePinDlg() {

		if (this.isFinishing()) {

			Log.w(TAG, "showChangePinDlg - me has been finish!");
			return;

		}

		if (null == changePinDlg) {

			MyChangePINDlg.Builder builder = new MyChangePINDlg.Builder(this);

			builder.setPositiveButton(res.getString(R.string.common_ok),
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {

							MyChangePINDlg tempDlg = (MyChangePINDlg) dialog;
							oldPin = tempDlg.getOldPIN();
							newPin = tempDlg.getNewPIN();

							if (null == oldPin || oldPin.length() < 4) {

								showToast(R.string.invalid_pin);
								return;

							}

							if (null == newPin || newPin.length() < 4) {

								showToast(R.string.invalid_pin);
								return;

							}

							tempDlg.dismiss();
							scanBLE();

						}

					}).setNegativeButton(R.string.common_cancel,
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {

							finish();

						}

					});

			changePinDlg = builder.create();
			changePinDlg.setTitle(res.getString(R.string.titleChangePin));
			changePinDlg.setCanceledOnTouchOutside(false);
			changePinDlg.setOnCancelListener(new OnCancelListener() {

				@Override
				public void onCancel(DialogInterface dialog) {

					finish();

				}

			});

		}

		changePinDlg.show();

	}

	private void showVerifyPinDlg() {

		if (this.isFinishing()) {

			Log.w(TAG, "showVerifyPinDlg - me has been finish!");
			return;

		}

		if (null == pinDlg) {

			MyPINDlg.Builder builder = new MyPINDlg.Builder(this);

			builder.setPositiveButton(R.string.common_ok,
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {

							MyPINDlg tempDlg = (MyPINDlg) dialog;
							oldPin = tempDlg.getPIN();
							if (null == oldPin || oldPin.length() < 4) {

								showToast(R.string.invalid_pin);
								return;

							}

							tempDlg.dismiss();
							scanBLE();

						}

					}).setNegativeButton(R.string.common_cancel,
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {

							finish();

						}

					});

			pinDlg = builder.create();
			pinDlg.setTitle(res.getString(R.string.titleVerifyPin));
			pinDlg.setCanceledOnTouchOutside(false);
			pinDlg.setOnCancelListener(new OnCancelListener() {

				@Override
				public void onCancel(DialogInterface dialog) {

					finish();

				}

			});

		}

		pinDlg.show();

	}

	void scanBLE() {
		
		if (action.equals(ApiList.ACTION_BLE_DECARD)) {

			Log.d(TAG, "doRead - startReadCardThread...");
			BaseApplication app = (BaseApplication) getApplication();
			startReadCardThread(new DcReader(app.getDcReader()));

		}

	}

}
