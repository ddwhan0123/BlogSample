package cn.eid.sample.idspsdk.test;

import com.trimps.eid.sdk.defines.common.ErrorCode;
import com.trimps.eid.sdk.defines.common.pin.PinResult;
import com.trimps.eid.sdk.idspapi.core.DeviceReader;

import cn.eid.sample.idspsdk.common.BaseApiTest;

/**
 * 登录卡片
 * 每张卡片都有它的PIN码，有些功能需验证PIN码后才能使用，如私钥验签等
 * 超过错误次数，卡片会锁定。
 * 
 * 
 * 清除设备安全状态
 * 当调用Login.execute登录成功后，卡片会保持当前的安全状态。
 * 为了防止其它程序访问卡片而导致可能的安全问题，当不再需要保持该状态时，请调用Logout.execute来清除设备安全状态。
 */
public class LoginLogoutTest extends BaseApiTest {

	private String pin = "";
	
	public LoginLogoutTest(DeviceReader reader, String pin) {
		super(reader);
		
		this.pin = pin;
		
	}

	@Override
	public ResultData perform() {
		
		ResultData result = new ResultData();
		
		long ret = ErrorCode.ERR_SUCCESS;
		try {
			
			ret = deviceReader.openDevice();
			
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if (ErrorCode.ERR_SUCCESS != ret) {
			
			appendMore("openDevice失败", ret);
			result.isOK = false;
			result.more = buildMore();
			
			return result;
			
		}
		appendMore("openDevice成功");
		
		
		PinResult pinResult = new PinResult();
		try {
			
			ret = deviceReader.login(pin, pinResult);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (ErrorCode.ERR_SUCCESS != ret) {
			
			if (pinResult.isLock) {
				
				appendMore("login 失败，卡已锁定", ret);
				
			} else{
				
				appendMore("login 失败，卡未锁定", ret);
				
			}
			
			closeDevice(deviceReader);
			result.isOK = false;
			result.more = buildMore();
			
			return result;
			
		}
		appendMore("login 成功");
		
		
		try {
			
			ret = deviceReader.logout();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (ErrorCode.ERR_SUCCESS != ret) {
			
			appendMore("logout失败", ret);
			result.isOK = false;
			result.more = buildMore();
			closeDevice(deviceReader);
			
			return result;
			
		}
		appendMore("logout成功");
		
		closeDevice(deviceReader);
		result.isOK = true;
		result.more = buildMore();
		
		return result;
		
	}

}
