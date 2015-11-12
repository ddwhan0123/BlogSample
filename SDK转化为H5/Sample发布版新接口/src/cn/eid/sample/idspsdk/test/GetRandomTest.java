package cn.eid.sample.idspsdk.test;

import com.trimps.eid.sdk.defines.base.ByteResult;
import com.trimps.eid.sdk.defines.common.ErrorCode;
import com.trimps.eid.sdk.defines.common.RandomLen;
import com.trimps.eid.sdk.idspapi.core.DeviceReader;

import cn.eid.sample.idspsdk.common.BaseApiTest;
import cn.eid.sample.idspsdk.common.ConverterUtil;

/**
 * 获取随机数
 */
public class GetRandomTest extends BaseApiTest {

	public GetRandomTest(DeviceReader reader) {
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
			
			ByteResult random = new ByteResult();
			RandomLen rlen = null;
			
			try {
				
				for (int i = 0; i <= 3; ++i) {
					
					rlen = null;
					
					if (i == 0) {
						
						rlen = RandomLen.TEID_LENGTH_4;
						
					} else if (i == 1) {
						
						rlen = RandomLen.TEID_LENGTH_8;
						
					} else if (i == 2) {
						
						rlen = RandomLen.TEID_LENGTH_16;
						
					}
					
					ret = deviceReader.getRandom(rlen, random);
					if (ErrorCode.ERR_SUCCESS != ret) {
						
						result.isOK = false;
						appendMore("getRandom 失败 - 随机数长度为[" + rlen + "]", ret);
						
					} else {
						
						result.isOK = true;
						
						appendMore("getRandom 成功 - 随机数长度为["+ rlen + "] ： " + ConverterUtil.getHexString(random.data, random.data.length));	
						
					}
				}

				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		
		closeDevice(deviceReader);
		result.more = buildMore();
		
		return result;
		
	}

}
