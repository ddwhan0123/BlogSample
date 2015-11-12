package cn.eid.sample.idspsdk.test;

import com.trimps.eid.sdk.defines.common.ErrorCode;
import com.trimps.eid.sdk.defines.common.pin.PinLenRange;
import com.trimps.eid.sdk.idspapi.core.DeviceReader;

import cn.eid.sample.idspsdk.common.BaseApiTest;

/**
 * 
 * 获取签名密码的长度范围
 */
public class GetPinRangeTest extends BaseApiTest {

	public GetPinRangeTest(DeviceReader reader) {
		super(reader);
		
		
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
			
			result.isOK = false;
			appendMoreFailed("openDevice", ret);
			
		} else {
			
			appendMoreSuccess("openDevice");
			
			PinLenRange range = new PinLenRange();
			try {
				
				ret = deviceReader.getPinRange(range);
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if (ErrorCode.ERR_SUCCESS != ret) {
				
				result.isOK = false;
				appendMoreFailed("getPinRange", ret);
				
			} else {
				
				result.isOK = true;
				appendMoreSuccess("getPinRange");
				appendMore("eID签名密码范围： [ " + range.min + "， " + range.max + " ]");
				
			}
			
		}
		
		closeDevice(deviceReader);
		result.more = buildMore();
		
		return result;
		
	}

}
