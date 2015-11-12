package cn.eid.sample.idspsdk.test;

import cn.eid.sample.idspsdk.common.BaseApiTest;

import com.trimps.eid.sdk.defines.base.BoolResult;
import com.trimps.eid.sdk.defines.common.ErrorCode;
import com.trimps.eid.sdk.idspapi.core.DeviceReader;

/**
 * 
 * 判断是否eID卡，需要先成功调用open。
 * 如果调用成功，可以通过出参BoolResult的boolean成员data的值来判断是否eID卡。
 */
public class IseIDCardTest extends BaseApiTest {

	public IseIDCardTest(DeviceReader reader) {
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
			
			appendMore("openDevice失败", ret);
			result.isOK = false;
			result.more = buildMore();
			return result;
			
		}
		appendMore("openDevice成功");
		
		
		BoolResult code = new BoolResult();
		try {
			
			ret = deviceReader.iseIDCard(code);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(ErrorCode.ERR_SUCCESS != ret) {
			
			appendMore("iseIDCard 失败", ret);
			closeDevice(deviceReader);
			result.isOK = false;
			result.more = buildMore();
			
			return result;
			
		}
		
		appendMore("iseIDCard 成功，" + (code.result ? "是eID卡" : "非eID卡"));
		closeDevice(deviceReader);
		
		result.isOK = true;
		result.more = buildMore();
		
		return result;
		
	}

}
