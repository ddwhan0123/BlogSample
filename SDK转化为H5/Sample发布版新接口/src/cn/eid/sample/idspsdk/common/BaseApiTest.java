package cn.eid.sample.idspsdk.common;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import android.util.Log;
import com.trimps.eid.sdk.defines.common.ErrorCode;
import com.trimps.eid.sdk.idspapi.core.DeviceReader;


public abstract class BaseApiTest {

	private static final String TAG = BaseApiTest.class.getName();
	
	protected DeviceReader deviceReader = null;
	
    private static final String RESULT_SUCCESS = "成功";
    private static final String RESULT_FAILED = "失败";
    

    /**
	 * 结果数据定义
	 */
	public class ResultData {
		
		public boolean isOK = false;
		public String more = "";
		
		public ResultData() {
			
			isOK = false;
			more = "";
			
		}
		
	}
	
	private StringBuffer log = new StringBuffer();
	
	public BaseApiTest() {
		
		clearMore();
		
	}
	
	public BaseApiTest(DeviceReader reader) {
		this();
		
		deviceReader = reader;
		
	}
	
	public abstract ResultData perform();
	

	/**
	 * 关闭设备
	 * 在开始每一个测试时，都需要先打开设备，完成测试或出现错误时，请务必对应的关闭设备。
	 */
	protected void closeDevice(DeviceReader reader) {
		
		long ret = ErrorCode.ERR_SUCCESS;
		try {
			
			ret = reader.closeDevice();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (ErrorCode.ERR_SUCCESS != ret) {
			
			appendMore("closeDevice失败", ret);
			
		} else {
			
			appendMore("closeDevice成功");
			
		}
		
	}
	
	
	public void appendMore(String more) {
		
		log.append("\n" + more + "\n");
		
	}
	
	public void appendMore(String more, long ret) {
		
		log.append("\n" + more + "->" + ErrorCode.getErrorDescription(ret) + "（" + Long.toHexString(ret) + "）\n");
		
	}
	
	public void appendMoreSuccess(String more){
		
		log.append("\n" + more + " " + RESULT_SUCCESS + "\n");
		
	}
	
	public void appendMoreFailed(String more, long ret){
		
		log.append("\n" + more + " " + RESULT_FAILED + " " +
				ErrorCode.getErrorDescription(ret) + "（" + Long.toHexString(ret) + "）\n");
		
	}
	
	public String buildMore() {
		
		return log.toString();
		
	}
	
	public void clearMore() {
		
		log.delete(0, log.length());
		
	}
	
	
	protected static byte[] SHA1(byte[] input) {

        try {

            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            digest.update(input);

            return digest.digest();

        } catch (NoSuchAlgorithmException e) {

        	Log.e(TAG, e.toString());

        }

        return null;
    }
	
}
