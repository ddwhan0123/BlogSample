package cn.eid.sample.idspsdk.test;

import java.io.UnsupportedEncodingException;
import java.util.Random;

import org.apache.commons.codec.binary.Base64;

import android.util.Log;

import com.trimps.eid.sdk.defines.common.ErrorCode;
import com.trimps.eid.sdk.defines.common.HashDataFrom;
import com.trimps.eid.sdk.defines.common.SignAlgInfo;
import com.trimps.eid.sdk.defines.common.SignResult;
import com.trimps.eid.sdk.defines.common.pin.PinResult;
import com.trimps.eid.sdk.idspapi.core.DeviceReader;

import cn.eid.sample.idspsdk.common.BaseApiTest;
import cn.eid.sample.idspsdk.common.ConverterUtil;

/**
 * 私钥签名 
 */
public class PrivateKeySignTest extends BaseApiTest {

	private static final String TAG = PrivateKeySignTest.class.getName();
	
	private static final int PLAINTEXT_LENGTH = 117;
	private byte[] inData = new byte[PLAINTEXT_LENGTH];
	
	private byte[] inputData = new byte[1024];

	private String pin = "";

	public PrivateKeySignTest(DeviceReader reader, String pin) {
		super(reader);
		
		this.pin = pin;
		
	}
	
	@Override
	public ResultData perform() {
		
		ResultData result = new ResultData();
		
		setInputData();
		
		
		
		//打开设备
		long ret = ErrorCode.ERR_SUCCESS;
		try {
			
			ret = deviceReader.openDevice();
			
		} catch (Exception e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		if (ErrorCode.ERR_SUCCESS != ret) {
			
			appendMoreFailed("openDevice", ret);
			result.isOK = false;
			result.more = buildMore();
			return result;
			
		}
		appendMoreSuccess("openDevice");
		
		
		
		
		
		
		PinResult objPinResult = new PinResult();
		try {
			
			ret = deviceReader.login(pin, objPinResult);
			
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if (ErrorCode.ERR_SUCCESS != ret) {
			
			appendMoreFailed("login", ret);
			
			closeDevice(deviceReader);
			result.more = buildMore();
			result.isOK = false;
			
			return result;
			
		}	
		appendMoreSuccess("login");
		
		
		
		
		
		
		SignAlgInfo info = new SignAlgInfo();
		try {
			
			ret = deviceReader.getSignAlg(info);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (ErrorCode.ERR_SUCCESS != ret) {
			
			appendMoreFailed("getSignAlg", ret);
			result.isOK = false;
			result.more = buildMore();
			
			return result;
			
		}
		appendMoreSuccess("getSignAlg");
		appendMore("当前卡片支持的签名算法：\r\n\t" + info.alg + "[Index = " + info.alg.getIndex() + "]");
		
		HashDataFrom from = HashDataFrom.EXTERNAL;

		try {
			
			ret = deviceReader.signInit(info.alg, from);
			
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if (ErrorCode.ERR_SUCCESS != ret) {
			
			appendMoreFailed("signInit", ret);
			result.isOK = false;
			result.more = buildMore();
			
			return result;
			
		}
		appendMoreSuccess("signInit");
		
		
		
		//如果签名原文，已经base64加密，则可以参考以下代码进行解密后，再签名。
		String data2Sign = new String("MjAxNTEwMjcxMDMzMTk6MjAxNTEwMjcxMDMzMTk6ZGF0YQ==");
		byte[] data_to_sign_bs = null;
		try {
			
			data_to_sign_bs = Base64.decodeBase64(data2Sign.getBytes("utf-8"));
			System.out.println("data_to_sign解密原文： " + new String(data_to_sign_bs, "utf-8"));
			
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			
			//replace the "inData" with data_to_sign_bs
			ret = deviceReader.signUpdate(data_to_sign_bs);
			
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if (ErrorCode.ERR_SUCCESS != ret) {
			
			appendMoreFailed("signUpdate", ret);
			result.isOK = false;
			result.more = buildMore();
			
			return result;
			
		}
		appendMoreSuccess("signUpdate");
		
		
		
		
		SignResult signResult = new SignResult();
		try {
			
			ret = deviceReader.signFinal(signResult);
			
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if (ErrorCode.ERR_SUCCESS != ret) {
			
			appendMoreFailed("signFinal", ret);
			result.isOK = false;
			result.more = buildMore();
			
			return result;
			
		}
		appendMoreSuccess("signFinal");
		
		result.isOK = true;
		
		appendMore("实际使用的sign算法：" + signResult.alg);
		appendMore("signFinal - " + from +  " ： " + ConverterUtil.getHexString(
				signResult.data, 
				signResult.data.length));

		
		//测试——拿到签名数据，先做base64加密
		byte[] dataOutEncoded = Base64.encodeBase64(signResult.data);
		try {
			
			//再转成String，用于提交到服务器验证
			String sign = new String(dataOutEncoded, "utf-8");
			Log.d("RSA or SM2", "sign： " + sign);
			
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		try {
			
			ret = deviceReader.logout();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (ErrorCode.ERR_SUCCESS != ret) {
			
			appendMore("logout失败", ret);
			closeDevice(deviceReader);
			
			result.isOK = false;
			result.more = buildMore();
			return result;
			
		}
		appendMore("logout成功");
		result.more = buildMore();
		
		closeDevice(deviceReader);
		
		return result;
		
	}
	
	/**
	 * 准备数据
	 */
	private void setInputData() {

		Random r = new Random();
		int value;
		for (int i = 0; i < inputData.length; i++) {
			
			value = r.nextInt(16);
			inputData[i] = (byte) value;
			
		}

		System.arraycopy(inputData, 0, inData, 0, PLAINTEXT_LENGTH);

	}

}
