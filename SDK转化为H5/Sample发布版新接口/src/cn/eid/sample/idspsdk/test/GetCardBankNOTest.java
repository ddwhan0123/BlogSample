package cn.eid.sample.idspsdk.test;

import com.trimps.eid.sdk.defines.base.StringResult;
import com.trimps.eid.sdk.defines.common.ErrorCode;
import com.trimps.eid.sdk.idspapi.core.DeviceReader;

import cn.eid.sample.idspsdk.common.BaseApiTest;

/**
 * 获取银行卡卡号
 */
public class GetCardBankNOTest extends BaseApiTest {

	public GetCardBankNOTest(DeviceReader reader) {
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
		if(ErrorCode.ERR_SUCCESS != ret){
			
			appendMore("openDevice 失败", ret);
			result.isOK = false;
			result.more = buildMore();
			return result;
			
		}
		
		appendMore("openDevice 成功");
		
		StringResult cardBankNO = new StringResult();
		try {
			
			ret = deviceReader.getCardBankNO(cardBankNO);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if(ErrorCode.ERR_SUCCESS != ret){
			
			result.isOK = false;							
			appendMore("getCardBankNO 失败", ret);
			
		}else {
			
			result.isOK = true;
			appendMore("getCardBankNO 成功，银行卡号为：\"" + cardBankNO.data + "\"");
			
			
		}
		
		closeDevice(deviceReader);
		result.more = buildMore();
		
		return result;
		
	}

}
