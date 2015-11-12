package cn.eid.sample.idspsdk.newapi;

import com.trimps.eid.sdk.reader.CardReader;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnCancelListener;
import android.graphics.Color;
import android.nfc.tech.IsoDep;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.eid.sample.idspsdk.common.ApiName;
import cn.eid.sample.idspsdk.common.BaseApiTest.ResultData;
import cn.eid.sample.idspsdk.common.reader.NFCReader;
import cn.eid.sample.idspsdk.common.BaseNfcActivity;
import cn.eid.sample.idspsdk.common.MyChangePINDlg;
import cn.eid.sample.idspsdk.common.MyPINDlg;
import cn.eid.sample.idspsdk.common.MyPromptDlg;
import cn.eid.sample.idspsdk.test.ErrorCodeTest;
import cn.eid.tools.nfc.NFCManager;

public class ReadWithNFC extends BaseNfcActivity {

	public static final String TAG = ReadWithNFC.class.getName();

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
	MyPromptDlg nfcDlg = null;
	String apiName = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.read_with_nfc);

		Intent intent = getIntent();
		apiName = intent.getStringExtra(EXTRA_API_NAME);

		hideResultPanel();
		initializeHandler();

	}

	@Override
	protected void onResume() {
		super.onResume();

		if (null == apiName || apiName.length() == 0) {

			Log.w(TAG, "apiName = " + apiName);
			finish();

			return;

		}

		if (apiName.equals("ErrorCodeTest")) {

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

		NFCManager nfcMgr = NFCManager.getInstance(this);
		if (!nfcMgr.isEnabled()) {

			showPromptDlgForNFCClosed(res.getString(R.string.nfc_closed));
			return;

		}

		if (ApiName.login.equals(apiName) || ApiName.sign.equals(apiName)
				|| ApiName.verify.equals(apiName)) {

			if (null == oldPin || oldPin.length() == 0) {

				isNfcBusy = true;
				showVerifyPinDlg();

			}

		} else if (ApiName.changePin.equals(apiName)) {

			if (null == oldPin || oldPin.length() == 0 || null == newPin
					|| newPin.length() == 0) {

				isNfcBusy = true;
				showChangePinDlg();

			}

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

		if (null != nfcDlg) {

			nfcDlg.dismiss();
			nfcDlg = null;

		}

		super.onDestroy();

	}

	@Override
	protected void execute(IsoDep isoDep) {

		/**
		 * 基于isoDep对象构造reader
		 */
		CardReader reader = new NFCReader(isoDep);

		Log.d(TAG, "execute - startReadCardThread...");
		startReadCardThread(reader);

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

		private ReadWithNFC curAT = null;

		public EventHandler(ReadWithNFC curAT, Looper looper) {
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

		// 开始执行，提示卡片贴紧勿动
		case MSG_READ_BEGIN:
			hideResultPanel();
			showProgressDlg(R.string.reading_card);
			break;

		// 执行完成，显示结果信息
		case MSG_READ_END:
			hideProgressDlg();
			ResultData result = (ResultData) msg.obj;
			showResultPanel(apiName, result.isOK, result.more);
			break;

		// 出现错误
		case MSG_ERROR:
			hideProgressDlg();
			showResultPanel(apiName, false, "error: result is null!");
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

		isNfcBusy = false;

	}

	void hideResultPanel() {

		final LinearLayout resultPanel = (LinearLayout) findViewById(R.id.resultPanel);
		resultPanel.setVisibility(View.GONE);

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
			if (null == result) {

				handler.sendEmptyMessage(MSG_ERROR);
				return;

			}

			Message msg = handler.obtainMessage(MSG_READ_END, result);
			handler.sendMessage(msg);

		}

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

							isNfcBusy = false;
							tempDlg.dismiss();

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

							isNfcBusy = false;
							tempDlg.dismiss();

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

	private void showPromptDlgForNFCClosed(String text) {

		if (this.isFinishing()) {

			Log.w(TAG, "showPromptDlgForNFCClosed - me has been finish!");
			return;

		}

		if (null == nfcDlg) {

			MyPromptDlg.Builder builder = new MyPromptDlg.Builder(this);

			builder.setTitle(R.string.prompt_dlg_title)
					.setText(text)
					.setPositiveButton(res.getString(R.string.common_ok),
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {

									dialog.dismiss();
									openNFCSettings();

								}

							})
					.setNegativeButton(res.getString(R.string.common_cancel),
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {

									dialog.dismiss();
									finish();

								}

							});

			nfcDlg = builder.create();
			nfcDlg.setCancelable(false);
			nfcDlg.setCanceledOnTouchOutside(false);

		}

		nfcDlg.show();

	}

}
