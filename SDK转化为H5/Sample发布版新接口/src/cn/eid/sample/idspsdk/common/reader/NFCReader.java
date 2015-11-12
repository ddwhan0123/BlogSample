/**
 * NFC设备控制类
 * 错误码处理示例类
 */
package cn.eid.sample.idspsdk.common.reader;

import java.io.IOException;
import java.util.Arrays;

import android.annotation.SuppressLint;
import android.nfc.tech.IsoDep;
import android.util.Log;
import cn.eid.sample.idspsdk.common.ConverterUtil;

import com.trimps.eid.sdk.defines.base.ByteResult;
import com.trimps.eid.sdk.defines.common.ErrorCode;
import com.trimps.eid.sdk.reader.CardReader;




public class NFCReader extends CardReader {
	
	public static final String TAG = NFCReader.class.getName();
	
	/**
	 * 设备不支持NFC
	 */
	public static final long ERR_NFC_NOT_SUPPORTED = 0x1;
	
	/**
	 * NFC通信未连接
	 */
	public static final long ERR_NFC_NOT_CONNECTED = 0x2;
	
	
	/**
	 * NFC通信失败
	 */
	public static final long ERR_NFC_CONNECT_FAILED = 0x3;
	
	/**
	 * 无效的长度
	 */
	public static final long ERR_BAD_LEN = 0x4;
	
	/**
	 * 未知错误
	 */
	public static final long ERR_UNKNOWN = 0x5;
	
	

	private IsoDep mIsoDep;

	public NFCReader(IsoDep objIsoDep) {
		super(objIsoDep);
		
		mIsoDep = objIsoDep;
		arrATS = mIsoDep.getHistoricalBytes();

	}
	
	private boolean isAviliable() {
		
		return null != mIsoDep;
		
	}
	
	@Override
	public long openDevice() {
		
		if (!isAviliable()) {
			
			return ErrorCode.ERR_BASE_USER | ERR_NFC_NOT_SUPPORTED;
			
		}
		
		if (!mIsoDep.isConnected()) {
		
			try {
				
				mIsoDep.connect();
				
			} catch (IOException e) {
				
				return ErrorCode.ERR_BASE_USER | ERR_NFC_CONNECT_FAILED;
				
			}
			
		}
		
		return ErrorCode.ERR_SUCCESS;
		
	}


	@Override
	public long closeDevice() {
		
		if (!isAviliable()) {
			
			return ErrorCode.ERR_BASE_USER | ERR_NFC_NOT_SUPPORTED;
			
		}

		try {
			
			mIsoDep.close();
			
		} catch (IOException e) {
			
			return ErrorCode.ERR_BASE_USER | ERR_NFC_CONNECT_FAILED;
			
		}
		
		return ErrorCode.ERR_SUCCESS;
	}


	
	@SuppressLint("NewApi")
	@Override
	public synchronized long sendApdu(byte[] arrApdu, ByteResult objRecv, ByteResult objState) {
		
		if (!isAviliable()) {
			
			return ErrorCode.ERR_BASE_USER | ERR_NFC_NOT_SUPPORTED;
			
		}
		
		int iMaxLength = mIsoDep.getMaxTransceiveLength();
		if (iMaxLength < arrApdu.length) {
			
			return ErrorCode.ERR_BASE_USER | ERR_BAD_LEN;
			
		}

		if (arrApdu != null)
			
			System.out.println("CMD:" + ConverterUtil.getHexString(arrApdu, arrApdu.length));
		
		try {

			byte[] response = mIsoDep.transceive(arrApdu);
			if(2 > response.length){
				
				return ErrorCode.ERR_BASE_USER | ERR_UNKNOWN;
				
			}
			
			if (2 <= response.length) {
				byte[] sw = new byte[2];
				sw[0] = response[response.length - 2];
				sw[1] = response[response.length - 1];
				objState.data = sw;
			}
			
			if (2 < response.length) {
				
				objRecv.data = Arrays.copyOf(response, response.length - 2);
				
			}
			
			if(objState.data != null)
				System.out.println("SW:" + ConverterUtil.getHexString(objState.data, objState.data.length));
			
			if(objRecv.data != null)
				System.out.println("Data:" + ConverterUtil.getHexString(objRecv.data, objRecv.data.length));
			
		} catch (IOException e) {
			
			Log.w(TAG, " transmit catch Exception:"+e.getMessage());
			return ErrorCode.ERR_BASE_USER | ERR_NFC_CONNECT_FAILED;
			
		}
		
		return ErrorCode.ERR_SUCCESS;
	}

	
	@Override
	public long reset() {
		
		return ErrorCode.ERR_BASE_USER | ERR_UNKNOWN;
		
	}


	@Override
	public long lock() {
		
		return ErrorCode.ERR_BASE_USER | ERR_UNKNOWN;
		
	}

	
	@Override
	public long unlock() {
		
		return ErrorCode.ERR_BASE_USER | ERR_UNKNOWN;
		
	}


}
