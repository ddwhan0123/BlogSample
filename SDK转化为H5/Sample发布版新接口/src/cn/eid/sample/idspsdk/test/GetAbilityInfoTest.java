package cn.eid.sample.idspsdk.test;

import cn.eid.sample.idspsdk.common.BaseApiTest;
import cn.eid.sample.idspsdk.common.ConverterUtil;

import com.trimps.eid.sdk.defines.common.AbilityInfo;
import com.trimps.eid.sdk.defines.common.ErrorCode;
import com.trimps.eid.sdk.idspapi.core.DeviceReader;

/**
 * 
 * 获取能力文件信息
 *
 * <br/>可获取的包括：非对称算法能力标识、Hash算法能力标识、载体自定义标识、载体机构代码、
 * <br/> 芯片版本号 、COS版本号、库版本号、文件系统版本号、JAVA版本号、离线认证标识、对称算法能力标识等能力标识信息
 */
public class GetAbilityInfoTest extends BaseApiTest {

	public GetAbilityInfoTest(DeviceReader reader) {
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
			
			appendMore("openDevice 失败", ret);
			result.isOK = false;
			result.more = buildMore();
			return result;
			
		}
		
		appendMore("openDevice 成功");
		
		AbilityInfo info = new AbilityInfo();
		try {
			
			ret = deviceReader.getAbilityInfo(info);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (ErrorCode.ERR_SUCCESS != ret) {				
			
			appendMore("getAbilityInfo 失败", ret);
			
			closeDevice(deviceReader);
			result.isOK = false;
			result.more = buildMore();
			
			return result;
			
		}
		appendMore("getAbilityInfo 成功");
		
		{
			appendMore("【能力文件信息为】：");
			appendMore(" 非对称算法能力标识: 0x" + Integer.toHexString(info.asymmetricItems));
			appendMore(" Hash算法能力标识: 0x" + Integer.toHexString(info.hashItems));
			appendMore(" 芯片型号: 0x" + Integer.toHexString(info.shChipVer));
			appendMore(" COS厂商代码: 0x" + Integer.toHexString(info.shCosVer));
			appendMore(" 库版本号: 0x" + Integer.toHexString(info.shDllVer));
			appendMore(" 文件系统版本号: 0x" + Integer.toHexString(info.shFileSystemVer));
			appendMore(" Java版本号: 0x" + Integer.toHexString(info.shJavaVer));
			appendMore(" 离线验证标识: 0x" + Integer.toHexString(info.shOfflineFlag));
			appendMore(" 对称算法能力标识: 0x" + Integer.toHexString(info.symmetricItems));
			appendMore(" 载体自定义标识: 0x" + ConverterUtil.getHexString(info.idCarrier, info.idCarrier.length));
			appendMore(" 载体机构代码: 0x" + ConverterUtil.getHexString(info.issuerOrg, info.issuerOrg.length ));
			appendMore(" 用户公私钥对算法标识: 0x" + Integer.toHexString(info.shUserAsymAlgType));
			
		}
		
		closeDevice(deviceReader);
		
		result.isOK = true;
		result.more = buildMore();
		return result;
		
	}

}
