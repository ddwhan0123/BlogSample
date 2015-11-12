package cn.eid.sample.idspsdk.newapi;

import java.util.List;

import com.decard.ble.cardreader.DcCardReader;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.eid.sample.idspsdk.common.ApiName;
import cn.eid.sample.idspsdk.common.BaseActivity;
import cn.eid.sample.idspsdk.common.BaseApplication;
import cn.eid.sample.idspsdk.common.MyListDlg;
import cn.eid.sample.idspsdk.common.MyListDlg.DiscoverFinishedTipClickListener;
import cn.eid.sample.idspsdk.common.MyPromptDlg;
import cn.eid.tools.bluetooth.BluetoothMgr;
import cn.eid.tools.bluetooth.BluetoothMgr.ScanPeriod;
import cn.eid.tools.bluetooth.ble.BleResult;
import cn.eid.tools.bluetooth.ble.IScanListener;



public class ApiList extends BaseActivity implements View.OnClickListener {

	private static final String TAG = ApiList.class.getName();

	// NFC
	public static final String ACTION_NFC = TAG + ".ACTION_NFC";

	// 德卡读卡器
	public static final String ACTION_BLE_DECARD = TAG + ".ACTION_BLE_DECARD";

	// 金联信息读卡器（接触式）
	BluetoothMgr blMgr = null;
	private static Handler handler = null;
	Thread connectBleDeviceThread = null;
	private MyListDlg devListDlg = null;
	private MyPromptDlg btNotOpenDlg = null;
	private MyPromptDlg btConnFailedDlg = null;
	private MyPromptDlg reDisDlg = null;

	private static final int MSG_CONNECT_BEGIN = 1;
	private static final int MSG_CONNECT_OK = 2;
	private static final int MSG_FAILED = 3;

	String action = "";

	TextView readerName;
	TextView connectState;

	Button getPinRange, changePin, login, isEIDCard, getCardBankNO,
			getFinancialCardInfo, getRandom, getAbilityInfo, getCert,
			hash, sign, verify, ErrorCodeTest;
	private Button scanDevice;
	private Button disConnectDevice;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.api_list);
		action = getIntent().getAction();

		createControls();

		initBLE();
		initializeHandler();

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

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.scanDevice:
			doBleScan();
			break;

		case R.id.disConnectDevice:
			doBleDeCardDisConnect();
			break;

		case R.id.login:
			go2Reader(ApiName.login);
			break;

		case R.id.getPinRange:
			go2Reader(ApiName.getPinRange);
			break;

		case R.id.changePin:
			go2Reader(ApiName.changePin);
			break;

		case R.id.isEIDCard:
			go2Reader(ApiName.isEIDCard);
			break;

		case R.id.getAbilityInfo:
			go2Reader(ApiName.getAbilityInfo);
			break;

		case R.id.getCardBankNO:
			go2Reader(ApiName.getCardBankNO);
			break;

		case R.id.getCert:
			go2Reader(ApiName.getCert);
			break;

		case R.id.getFinancialCardInfo:
			go2Reader(ApiName.getFinancialCardInfo);
			break;

		case R.id.getRandom:
			go2Reader(ApiName.getRandom);
			break;

		case R.id.hash:
			go2Reader(ApiName.hash);
			break;

		case R.id.sign:
			go2Reader(ApiName.sign);
			break;

		case R.id.verify:
			go2Reader(ApiName.verify);
			break;

		case R.id.ErrorCodeTest:
			go2Reader(ApiName.ErrorCodeTest);
			break;

		default:
			break;
		}
	}

	void createControls() {

		readerName = (TextView) findViewById(R.id.readerName);
		String selectedReader = String.format(
				res.getString(R.string.readerSelected),
				getReaderNameByAction(action));
		readerName.setText(selectedReader);

		connectState = (TextView) findViewById(R.id.connectState);
		connectState.setText(R.string.devDisconnected);

		scanDevice = (Button) findViewById(R.id.scanDevice);
		scanDevice.setOnClickListener(this);

		disConnectDevice = (Button) findViewById(R.id.disConnectDevice);
		disConnectDevice.setOnClickListener(this);
		disConnectDevice.setEnabled(false);
		final RelativeLayout deviceOperation = (RelativeLayout) findViewById(R.id.device_operation);
		if (ACTION_BLE_DECARD.equals(action)) {

			connectState.setVisibility(View.VISIBLE);
			deviceOperation.setVisibility(View.VISIBLE);

		} else {

			deviceOperation.setVisibility(View.GONE);
			connectState.setVisibility(View.INVISIBLE);

		}

		// 认证相关
		login = (Button) findViewById(R.id.login);
		login.setOnClickListener(this);

		getPinRange = (Button) findViewById(R.id.getPinRange);
		getPinRange.setOnClickListener(this);

		changePin = (Button) findViewById(R.id.changePin);
		changePin.setOnClickListener(this);

		// 功能相关
		isEIDCard = (Button) findViewById(R.id.isEIDCard);
		isEIDCard.setOnClickListener(this);

		getAbilityInfo = (Button) findViewById(R.id.getAbilityInfo);
		getAbilityInfo.setOnClickListener(this);

		getCardBankNO = (Button) findViewById(R.id.getCardBankNO);
		getCardBankNO.setOnClickListener(this);

		getCert = (Button) findViewById(R.id.getCert);
		getCert.setOnClickListener(this);

		getFinancialCardInfo = (Button) findViewById(R.id.getFinancialCardInfo);
		getFinancialCardInfo.setOnClickListener(this);

		getRandom = (Button) findViewById(R.id.getRandom);
		getRandom.setOnClickListener(this);

		hash = (Button) findViewById(R.id.hash);
		hash.setOnClickListener(this);

		sign = (Button) findViewById(R.id.sign);
		sign.setOnClickListener(this);

		verify = (Button) findViewById(R.id.verify);
		verify.setOnClickListener(this);

		ErrorCodeTest = (Button) findViewById(R.id.ErrorCodeTest);
		ErrorCodeTest.setOnClickListener(this);

	}

	@Override
	protected void onResume() {
		super.onResume();

		BaseApplication app = (BaseApplication) getApplication();
		DcCardReader dcReader = app.getDcReader();
		if (null != dcReader) {
			
			int state = dcReader.getConnectState();
			boolean code = (state == DcCardReader.CONN_STATE_CONNECTED);
			connectState.setText(code ? R.string.devConnected
					: R.string.devDisconnected);
			if (code) {
	
				disConnectDevice.setEnabled(true);
	
			} else {
	
				disConnectDevice.setEnabled(false);
	
			}
			
		}

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

	void doBleScan() {

		if (!blMgr.isEnabled()) {

			notifyBTNotOpen();
			return;

		}

		scanBleReader();
	}

	void doBleDeCardDisConnect() {

		BaseApplication app = (BaseApplication) getApplication();
		DcCardReader dcReader = app.getDcReader();
		if (null != dcReader) {
			
			dcReader.disconnect();
			app.setDcReader(null);
			
		}

		connectState.setText(R.string.devDisconnected);
		disConnectDevice.setEnabled(false);

	}

	// 进入对应ACTION和ApiName的执行环境、刷卡界面
	void go2Reader(String apiName) {

		//showToast(apiName);

		if (ACTION_NFC.equals(action)) {

			go2ReadWithNFC(apiName);

		} else if (ACTION_BLE_DECARD.equals(action)) {

			go2ReadWithBleReader(apiName);

		} else {

			go2ReadWithBleReader(apiName);

		}

	}

	void go2ReadWithNFC(String apiName) {

		Intent intent = new Intent(this, ReadWithNFC.class);
		intent.putExtra(ReadWithNFC.EXTRA_API_NAME, apiName);
		startActivity(intent);

	}

	void go2ReadWithBleReader(String apiName) {

		Intent intent = new Intent(this, ReadWithBleReader.class);
		intent.setAction(action);
		intent.putExtra(ReadWithBleReader.EXTRA_API_NAME, apiName);
		startActivity(intent);

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

		private ApiList curAT = null;

		public EventHandler(ApiList curAT, Looper looper) {
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
			showProgressDlg(R.string.bluetooth_connecting);
			disConnectDevice.setEnabled(false);
			connectState.setText(R.string.devDisconnected);
			break;

		case MSG_CONNECT_OK:
			disConnectDevice.setEnabled(true);
			connectState.setText(R.string.devConnected);

			hideProgressDlg();
			break;

		case MSG_FAILED:
			hideProgressDlg();
			disConnectDevice.setEnabled(false);
			connectState.setText(R.string.devDisconnected);

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

	void notifyBTNotOpen() {

		showPromptDlgForBTNotOpen();

	}

	/**
	 * list blue tooth devices discovered
	 */
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

	void hideListDeviceDlg() {

		if (null != devListDlg) {

			devListDlg.hide();
			devListDlg.dismiss();
			devListDlg = null;

		}

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

	void scanBleReader() {

		showProgressDlg(R.string.scan_ble);

		blMgr.clearAllDevices();
		blMgr.startScan(ScanPeriod.SP_MIN, scanListener);

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

	// /end ble scan
}
