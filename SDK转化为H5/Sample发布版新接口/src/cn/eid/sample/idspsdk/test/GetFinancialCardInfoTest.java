package cn.eid.sample.idspsdk.test;

import com.trimps.eid.sdk.defines.common.ErrorCode;
import com.trimps.eid.sdk.defines.common.FinancialCardInfo;
import com.trimps.eid.sdk.defines.common.FinancialCardInfo.CardType;
import com.trimps.eid.sdk.idspapi.core.DeviceReader;

import cn.eid.sample.idspsdk.common.BaseApiTest;

/**
 * 判断是否金融IC卡 
 */
public class GetFinancialCardInfoTest extends BaseApiTest {

	public GetFinancialCardInfoTest(DeviceReader reader) {
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
			
		
		
		FinancialCardInfo cardInfo = new FinancialCardInfo();
		try {
			
			ret = deviceReader.getFinancialCardInfo(cardInfo);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (ErrorCode.ERR_SUCCESS != ret) {
			
			appendMoreFailed("getFinancialCardInfo", ret);
			result.isOK = false;
			result.more = buildMore();
			
			closeDevice(deviceReader);
			
			return result;
			
		}
		
		if (cardInfo.isOK()) {
			
			String cardType = convertCardType(cardInfo.getCardType());
			appendMore("getFinancialCardInfo 成功，是金融卡，类型：" + cardType + "\"");
			
		} else {
			
			appendMore("getFinancialCardInfo 成功，非金融卡。");
			
		}

		closeDevice(deviceReader);
		result.isOK = true;
		result.more = buildMore();
		
		return result;
		
	}
	
	
	String convertCardType(int type) {
		
		String typeStr = "未知类型";
		switch (type) {
		
		case CardType.SEMI_CREDIT_CARD:
			typeStr = "银联准贷记";
			break;

		case CardType.DEBIT_CARD:
			typeStr = "银联借记";
			break;
		
		case CardType.CREDIT_CARD:
			typeStr = "银联贷记";
			break;
			
		case CardType.CASH_CARD:
			typeStr = "银联电子现金";
			break;

		default:
			break;
		}
		
		return typeStr;
		
	}

}
