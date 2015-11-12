package cn.eid.sample.idspsdk.test;

import cn.eid.sample.idspsdk.common.BaseApiTest;
import com.trimps.eid.sdk.defines.common.ErrorCode;
import com.trimps.eid.sdk.defines.common.pin.PinResult;
import com.trimps.eid.sdk.idspapi.core.DeviceReader;

/**
 * 
 * 修改eID签名密码
 * <br/>修改密码必须输入正确的密码，超过错误次数，卡片会锁定。
 */
public class ChangePinTest extends BaseApiTest {

	private String oldPin = "";
	private String newPin = "";
	
	public ChangePinTest(DeviceReader reader, String oldPin, String newPin) {
		super(reader);
		
		this.oldPin = oldPin;
		this.newPin = newPin;
		
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
			
			ret = deviceReader.changePin(oldPin, newPin, pinResult);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (ErrorCode.ERR_SUCCESS != ret) {
			
			result.isOK = false;
			if (pinResult.isLock) {
				
				appendMore("changePin 失败，卡已锁定", ret);
				
			} else {
				
				appendMore("changePin 失败，卡未锁定", ret);
				
			}
			
			closeDevice(deviceReader);
			result.isOK = false;
			result.more = buildMore();
			return result;

		} 
		
		appendMore("changePin 成功");
		
		closeDevice(deviceReader);
		
		result.isOK = true;
		result.more = buildMore();
		return result;
		
	}

}
