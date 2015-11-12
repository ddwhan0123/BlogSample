package cn.eid.sample.idspsdk.newapi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Locale;

import com.trimps.eid.sdk.idspapi.core.DeviceReader;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import cn.eid.sample.idspsdk.common.BaseActivity;
import cn.eid.tools.bluetooth.BluetoothMgr;
import cn.eid.tools.nfc.NFCManager;

public class Home extends BaseActivity {

	public static final String TAG = Home.class.getName();

	private static Handler handler = null;
	private static final int MSG_READ_BEGIN = 0;
	private static final int MSG_READ_FAILED = 1;
	private static final int MSG_READ_OK = 2;

	Thread nfcFirmwareDetectThread = null;
	Thread initBleDeviceThread = null;
	Thread connectBleDeviceThread = null;

	TextView appVerTV;
	TextView sdkVerTV;
	TextView devTV;
	TextView osTV;
	TextView nfcTV;
	TextView bleTV;

	Button nfc, decardReader;

	BluetoothMgr blMgr = null;

	@SuppressLint({ "InlinedApi", "NewApi" })
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.home);

		initBLE();

		createControls();
		initializeHandler();

		startNFCFirmwareDetectThread();

	}

	void initBLE() {

		if (BluetoothMgr.isSupportedBLE(this)) {
		
			blMgr = BluetoothMgr.getInstance(this);
			
		}

	}

	void createControls() {

		appVerTV = (TextView) findViewById(R.id.app_ver);

		PackageInfo pi = null;
		try {

			pi = this.getPackageManager().getPackageInfo(this.getPackageName(),
					PackageManager.GET_ACTIVITIES);

		} catch (NameNotFoundException e) {

			e.printStackTrace();

		}
		String appVerFormat = String.format(res.getString(R.string.app_ver),
				pi.versionName);
		appVerTV.setText(appVerFormat);

		devTV = (TextView) findViewById(R.id.dev_info);
		String devFormat = String.format(res.getString(R.string.dev_info),
				Build.MODEL);
		devTV.setText(devFormat);

		osTV = (TextView) findViewById(R.id.os_info);
		String osFormat = String.format(res.getString(R.string.os_info),
				Build.VERSION.RELEASE + " ( " + Build.VERSION.SDK_INT + " )");
		osTV.setText(osFormat);

		sdkVerTV = (TextView) findViewById(R.id.sdk_ver);
		String sdkVerFormat = String.format(res.getString(R.string.sdk_ver),
				DeviceReader.getVersion());
		sdkVerTV.setText(sdkVerFormat);

		nfcTV = (TextView) findViewById(R.id.nfc_info);
		bleTV = (TextView) findViewById(R.id.ble_info);

		nfc = (Button) findViewById(R.id.nfc);
		String nfcFormat = String.format(
				res.getString(R.string.readerNameList),
				res.getString(R.string.nfc));
		nfc.setText(nfcFormat);
		nfc.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				go2ApiList(H5ApiList.ACTION_NFC);
//				go2ApiList(ApiList.ACTION_NFC);

			}

		});

		decardReader = (Button) findViewById(R.id.decardReader);
		String decardReaderFormat = String.format(
				res.getString(R.string.readerNameList),
				res.getString(R.string.decardReader));
		decardReader.setText(decardReaderFormat);
		decardReader.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				go2ApiList(H5ApiList.ACTION_BLE_DECARD);
//				go2ApiList(ApiList.ACTION_BLE_DECARD);

			}

		});

		NFCManager nfcMgr = NFCManager.getInstance(this);
		if (!nfcMgr.isAvailable()) {

			String nfcAvialableFormat = String.format(
					res.getString(R.string.nfc_info),
					res.getString(R.string.nfc_not_supported));
			nfcTV.setText(nfcAvialableFormat);

			nfc.setEnabled(false);

		} else {

			String nfcAvialableFormat = String.format(
					res.getString(R.string.nfc_info),
					res.getString(R.string.nfc_supported));
			nfcTV.setText(nfcAvialableFormat);

			nfc.setEnabled(true);

		}

		
		boolean ble = false;
		if (null == blMgr || !blMgr.isAvailable()) {
			
			ble = false;

		} else {

			ble = blMgr.isSupportBLE();
			

		}
		
		String bleAvialableFormat = String.format(
				res.getString(R.string.ble_info),
				ble ? res.getString(R.string.ble_supported) : res
						.getString(R.string.ble_not_supported));
		bleTV.setText(bleAvialableFormat);
		
		decardReader.setEnabled(ble);

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

		private Home curAT = null;

		public EventHandler(Home curAT, Looper looper) {
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

		case MSG_READ_BEGIN:
			showProgressDlg(R.string.detect_nfc_firmware);
			break;

		case MSG_READ_FAILED:
			hideProgressDlg();

			String failedTip = String.format(
					res.getString(R.string.nfc_firmware),
					res.getString(R.string.detect_nfc_firmware_failed));
			handleResult(failedTip);
			break;

		case MSG_READ_OK:
			hideProgressDlg();

			String nfcFirmware = (String) msg.obj;
			handleResult(nfcFirmware);
			break;

		default:
			break;

		}
	}

	void handleResult(String fmFormat) {

		final TextView nfcFirmware = (TextView) findViewById(R.id.nfc_firmware);
		nfcFirmware.setText(fmFormat);

	}

	void startNFCFirmwareDetectThread() {

		if (null != nfcFirmwareDetectThread) {

			nfcFirmwareDetectThread.interrupt();
			nfcFirmwareDetectThread = null;

		}

		nfcFirmwareDetectThread = new NFCFirmwareDetectThread();
		nfcFirmwareDetectThread.start();

	}

	private class NFCFirmwareDetectThread extends Thread {

		private NFCFirmwareDetectThread() {
			super();

		}

		@Override
		public void run() {
			super.run();

			handler.sendEmptyMessage(MSG_READ_BEGIN);

			Log.d(TAG, "NFCFirmwareDetectThread - detectNFCFirmware...");
			String nfcFirmware = detectNFCFirmware();
			if (nfcFirmware.equals("")) {

				handler.sendEmptyMessage(MSG_READ_FAILED);

			} else {

				Message msg = handler.obtainMessage(MSG_READ_OK, nfcFirmware);
				handler.sendMessage(msg);

			}

		}

	}

	String detectNFCFirmware() {

		String nfcFirmware = "";

		if (!NFCManager.getInstance(this).isAvailable()) {

			return nfcFirmware;

		}

		String vendorShell = execShell("ls /system/vendor/firmware");
		if (vendorShell.length() == 0) {

			return nfcFirmware;

		}

		String[] vendorPrefix = vendorShell.split("#");
		for (int i = 0; i < vendorPrefix.length; ++i) {

			nfcFirmware = parseFileFilter(vendorPrefix[i]);
			if (!"".equals(nfcFirmware)) {

				break;

			}

		}

		if (nfcFirmware.equals("")) {

			String etcShell = execShell("ls /system/etc/firmware");
			String[] etcPrefix = etcShell.split("#");
			for (int i = 0; i < etcPrefix.length; ++i) {

				nfcFirmware = parseFileFilter(etcPrefix[i]);
				if (!"".equals(nfcFirmware)) {

					break;

				}

			}

		}

		return nfcFirmware;

	}

	/**
	 * 根据ls系列的shell，获取执行后遍历得到的文件的名称（暂且相信没有文件夹，以#分隔）
	 * 
	 * @param shell
	 * @return
	 */
	String execShell(String shell) {

		StringBuffer lines = new StringBuffer();
		String result = "";

		Process p = null;
		BufferedReader in = null;

		try {

			p = Runtime.getRuntime().exec(shell);
			in = new BufferedReader(new InputStreamReader(p.getInputStream()));

			String line = null;
			while ((line = in.readLine()) != null) {

				line += "#";
				lines.append(line);

			}

			result = lines.toString();

		} catch (IOException e) {

			Log.e(TAG, e.toString());
			result = "";

		} finally {

			if (null != in) {

				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				in = null;

			}

			if (null != p) {

				p.destroy();
				p = null;

			}

		}

		if (result.length() == 0) {

			return "";

		}

		result = result.substring(0, result.length() - 1);
		return result;

	}

	String parseFileFilter(String fileFilter) {

		fileFilter = fileFilter.toLowerCase(Locale.US);

		String postFix3 = fileFilter.substring(fileFilter.length() - 3,
				fileFilter.length());
		String postFix4 = fileFilter.substring(fileFilter.length() - 4,
				fileFilter.length());
		if (fileFilter.startsWith("libpn") && postFix3.equalsIgnoreCase(".so")) {

			return String.format(res.getString(R.string.nfc_firmware),
					res.getString(R.string.nxp_firmware));

		}

		if (fileFilter.startsWith("bcm") && postFix4.equalsIgnoreCase(".ncd")) {

			return String.format(res.getString(R.string.nfc_firmware),
					res.getString(R.string.broadcom_firmware));

		}

		return "";

	}

	@Override
	protected void onResume() {
		super.onResume();

	}

	void go2ApiList(String action) {

//		Intent intent = new Intent(this, ApiList.class);
		Intent intent = new Intent(this, H5ApiList.class);
		intent.setAction(action);
		startActivity(intent);

	}

	@Override
	protected void onDestroy() {

		if (null != handler) {

			handler.removeCallbacksAndMessages(null);

			handler = null;

		}

		super.onDestroy();

	}

}
