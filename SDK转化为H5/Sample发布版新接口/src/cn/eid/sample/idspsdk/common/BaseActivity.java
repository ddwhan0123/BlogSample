package cn.eid.sample.idspsdk.common;


import com.trimps.eid.sdk.idspapi.core.DeviceReader;
import com.trimps.eid.sdk.reader.CardReader;

import cn.eid.sample.idspsdk.common.BaseApiTest.ResultData;
import cn.eid.sample.idspsdk.newapi.R;
import cn.eid.sample.idspsdk.test.ChangePinTest;
import cn.eid.sample.idspsdk.test.GetAbilityInfoTest;
import cn.eid.sample.idspsdk.test.GetCardBankNOTest;
import cn.eid.sample.idspsdk.test.GetCertTest;
import cn.eid.sample.idspsdk.test.GetFinancialCardInfoTest;
import cn.eid.sample.idspsdk.test.GetPinRangeTest;
import cn.eid.sample.idspsdk.test.GetRandomTest;
import cn.eid.sample.idspsdk.test.HashTest;
import cn.eid.sample.idspsdk.test.IseIDCardTest;
import cn.eid.sample.idspsdk.test.LoginLogoutTest;
import cn.eid.sample.idspsdk.test.PrivateKeySignTest;
import cn.eid.sample.idspsdk.test.PublicKeyVerifyTest;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;



public class BaseActivity extends Activity {

	public static final String TAG = BaseActivity.class.getName();
	
	protected Resources res;
	protected MyLoadingDlg progressDlg = null;
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		res = getResources();
		
	}

	protected void showProgressDlg() {

		showProgressDlg(R.string.common_dealing_request);

	}
	
	protected void showProgressDlg(String text) {
		
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

	
	protected void showProgressDlg(int textResId) {

		showProgressDlg(res.getString(textResId));

	}

	
	protected void hideProgressDlg() {

		if (this.isFinishing()) {

			Log.w(TAG, "hideProcessDlg - me has been finish!");
			return;

		}

		if (null != progressDlg && progressDlg.isShowing()) {

			progressDlg.hide();
			progressDlg.stopAnim();
			progressDlg.dismiss();
			progressDlg = null;

		}

	}
	
	
	protected void showToast(String tip, int length) {
		
		Toast.makeText(this, tip, length).show();
		
	}

	protected void showToast(int tipResId) {
		
		showToast(res.getString(tipResId));
		
	}
	
	protected void showToast(String tip) {
		
		Toast.makeText(this, tip, Toast.LENGTH_LONG).show();
		
	}
	
	
	protected void openNFCSettings() {

        Intent intent = new Intent(Settings.ACTION_NFC_SETTINGS);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
        
    }
	
	
	protected void openBTSettings() {

		Intent intent = new Intent(Settings.ACTION_BLUETOOTH_SETTINGS);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);

	}
	
	
	
	protected ResultData runTests(String apiName, CardReader userReader, String oldPin, String newPin) {
		
		//根据用户实现的CardReader对象来构造“卡读取设备”。
		DeviceReader reader = new DeviceReader(userReader);
		
		// 请根据apiName的值来判断是哪个API的测试
		BaseApiTest test = null;
		
		if (apiName.equals(ApiName.login)) {
	
			test = new LoginLogoutTest(reader, oldPin);
	
		} else if (apiName.equals(ApiName.getPinRange)) {
	
			test = new GetPinRangeTest(reader);
	
		} else if (apiName.equals(ApiName.changePin)) {
	
			test = new ChangePinTest(reader, oldPin, newPin);
	
		} else if (apiName.equals(ApiName.isEIDCard)) {
	
			test = new IseIDCardTest(reader);
	
		} else if (apiName.equals(ApiName.getAbilityInfo)) {
	
			test = new GetAbilityInfoTest(reader);
	
		} else if (apiName.equals(ApiName.getCardBankNO)) {
	
			test = new GetCardBankNOTest(reader);
	
		} else if (apiName.equals(ApiName.getCert)) {
	
			test = new GetCertTest(reader);
	
		} else if (apiName.equals(ApiName.getFinancialCardInfo)) {
	
			test = new GetFinancialCardInfoTest(reader);
	
		} else if (apiName.equals(ApiName.getRandom)) {
	
			test = new GetRandomTest(reader);
	
		} else if (apiName.equals(ApiName.hash)) {
	
			test = new HashTest(reader);
	
		}else if (apiName.equals(ApiName.sign)) {
	
			test = new PrivateKeySignTest(reader, oldPin);
	
		} else if (apiName.equals(ApiName.verify)) {
	
			test = new PublicKeyVerifyTest(reader, oldPin);
	
		}
		
		return test.perform();
		
	}

}
