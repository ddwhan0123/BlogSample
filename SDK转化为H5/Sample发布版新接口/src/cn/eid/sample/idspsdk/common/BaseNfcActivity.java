package cn.eid.sample.idspsdk.common;

import cn.eid.sample.idspsdk.newapi.R;
import cn.eid.tools.nfc.NFCManager;
import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentFilter.MalformedMimeTypeException;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.IsoDep;
import android.nfc.tech.NfcA;
import android.nfc.tech.NfcB;
import android.nfc.tech.NfcF;
import android.nfc.tech.NfcV;
import android.os.Bundle;
import android.util.Log;




public abstract class BaseNfcActivity extends BaseActivity {

	protected final String TAG = getClass().getSimpleName();
	
	// NFC parts
	private static PendingIntent mPendingIntent;
	private static IntentFilter[] mFilters;
	private static String[][] mTechLists;
	protected boolean isNfcBusy = false;

	@Override
	// 页面创建时
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		isNfcBusy = false;
		initNfc();
		
	}


	@SuppressLint("NewApi")
	private void initNfc() {
 
		mPendingIntent = PendingIntent.getActivity(this, 0,
				new Intent(this.getApplicationContext(), getClass())
						.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
		
		IntentFilter tech = new IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED);
		IntentFilter ndef = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
		IntentFilter tag = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
		
		try {

			ndef.addDataType("*/*");

		} catch (MalformedMimeTypeException e) {

			throw new RuntimeException("failed", e);

		}
		mFilters = new IntentFilter[] { tech, ndef, tag };

		// Setup a teach list for all NfcX tags
		mTechLists = new String[][] {
			
			new String[] { IsoDep.class.getName() },
			new String[] { NfcA.class.getName() }

		};

	}


	@SuppressLint("NewApi")
	@Override
	protected void onResume() {
		super.onResume();

		Log.d(TAG, "onResume");
		
		NFCManager nfcMgr = NFCManager.getInstance(this);
		if (nfcMgr.isEnabled()) {
			
			nfcMgr.getAdapter().enableForegroundDispatch(this, mPendingIntent, mFilters,
					mTechLists);
		
		}
		
	}


	@SuppressLint("NewApi")
	@Override
	protected void onPause() {
		super.onPause();
		
		Log.d(TAG, "onPause");
		
		NFCManager nfcMgr = NFCManager.getInstance(this);
		if (nfcMgr.isEnabled()) {
			
			nfcMgr.getAdapter().disableForegroundDispatch(this);
			
		}
		
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		
		Log.d(TAG, "onNewIntent");
		
		if (isNfcBusy) {
			
			Log.w(TAG, "onNewIntent - isNfcBusy is true already!");
			return;
			
		}
		
		String action = intent.getAction();
		if (null != action) {
			
			if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {
				
				Log.d(TAG, "onNewIntent - ACTION_NDEF_DISCOVERED");
				
			} else if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)) {

				Log.d(TAG, "onNewIntent - ACTION_TECH_DISCOVERED");
				
				IsoDep isoDep = getIsoDep(intent);
				if (null == isoDep) {
					
					Log.e(TAG, "onNewIntent - isoDep is null!");
					showError(R.string.read_card_failed);
					return;
					
				}
				
				isNfcBusy = true;
				Log.d(TAG, "onNewIntent - execute...");
				execute(isoDep);
				
				return;
				
			} else if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)) {
				
				Log.d(TAG, "onNewIntent - ACTION_TAG_DISCOVERED");
				
			}
			
		}
		
		Log.d(TAG, "onNewIntent - showError...");
		showError(R.string.read_card_failed);
				
	}
	
	
	protected abstract void execute(IsoDep isoDep);
	
	
	protected IsoDep getIsoDep(Intent intent) {
		
		Tag tagFromIntent = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
		
		String[] techList = tagFromIntent.getTechList();
		for (int i = 0; i < techList.length; ++i) {
			
			Log.d(TAG, "get - techList[ " + i + " ] = " + techList[i]);
			
		}
		
		return IsoDep.get(tagFromIntent);
		
	}
	
	protected void showError(int textResId) {

		if (this.isFinishing()) {

			Log.w(TAG, "showError - me has been finish!");
			return;

		}
		
		isNfcBusy = true;

		MyPromptDlg.Builder builder = new MyPromptDlg.Builder(this);

		builder.setTitle(res.getString(R.string.prompt_dlg_title))
				.setText(res.getString(textResId))
				.setPositiveButton(res.getString(R.string.common_ok),
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {

								dialog.dismiss();
								isNfcBusy = false;

							}

						});

		MyPromptDlg dlg = builder.create();
		dlg.setCancelable(false);
		dlg.setCanceledOnTouchOutside(false);
		dlg.show();

	}

}
