package cn.eid.sample.idspsdk.common.reader;

import java.util.Arrays;

import com.decard.ble.cardreader.DcCardReader;
import com.decard.ble.util.HexDump;
import com.trimps.eid.sdk.defines.base.ByteResult;
import com.trimps.eid.sdk.defines.common.ErrorCode;
import com.trimps.eid.sdk.reader.CardReader;



public class DcReader extends CardReader {

	DcCardReader reader = null;
	
	byte[] byteRecv = new byte[1022];
	byte[] byteState = new byte[2];
	
	public DcReader(Object arg0) {
		super(arg0);
		
		this.reader = (DcCardReader) arg0;
		
		getATR();
		
	}
	
	long checkConnect() {
		
		long state = reader.getConnectState();
		if (state != DcCardReader.CONN_STATE_CONNECTED) {
			
			return state;
			
		}
		
		return ErrorCode.ERR_SUCCESS;
		
	}
	
	long prepare() {
		
		long state = checkConnect();
		if (state != ErrorCode.ERR_SUCCESS) {
			
			return ErrorCode.ERR_BASE_USER | state;
			
		}
		
		return ErrorCode.ERR_SUCCESS;
		
	}
	
	
	@Override
	public long openDevice() {
		
		long ret = prepare();
		if (ErrorCode.ERR_SUCCESS != ret) {
			
			return ret;
			
		}
		
		ret = reader.GetDeviceState();
		if (ret == DcCardReader.ERR_CODE_CARD_WITHPOWER) {
			
			//鏈夊崱涓斿凡涓婄數鐘舵�
			return ErrorCode.ERR_SUCCESS;
			
		}
		
		
		if (ret != DcCardReader.ERR_CODE_CARD_WITHOUTPOWER) {
			
			//涓嶆槸鈥滄湁鍗℃湭涓婄數鈥濈殑鐘舵�
			return ErrorCode.ERR_BASE_USER | ret;
			
		}
		
		ret = reader.OpenDevice();
		if (DcCardReader.CODE_SUCCESS != ret) {
			
			return ErrorCode.ERR_BASE_USER | ret;
			
		}
		
		return ErrorCode.ERR_SUCCESS;
		
	}
	
	@Override
	public long closeDevice() {
		
		long ret = prepare();
		if (ErrorCode.ERR_SUCCESS != ret) {
			
			return ret;
			
		}
		
		ret = reader.CloseDevice();
		if (DcCardReader.CODE_SUCCESS != ret) {
			
			return ErrorCode.ERR_BASE_USER | ret;
			
		}
		
		return ErrorCode.ERR_SUCCESS;
		
	}
	
	
	long getATR() {
		
		long ret = prepare();
		if (ErrorCode.ERR_SUCCESS != ret) {
			
			return ret;
			
		}
		
		byte[] atr = reader.GetCardATS();
		if (null != atr) {
			
			super.arrATS = Arrays.copyOf(atr, atr.length);
			return ErrorCode.ERR_SUCCESS;
			
		}
		
		return ErrorCode.ERR_BASE_USER | DcCardReader.CODE_FAILED;
		
	}
	
	
	@Override
	public long sendApdu(byte[] cmd, ByteResult recv, ByteResult state) {
		
		long ret = prepare();
		if (ErrorCode.ERR_SUCCESS != ret) {
			
			return ret;
			
		}
		
		Arrays.fill(byteRecv, (byte) 0);
		Arrays.fill(byteState, (byte) 0);
		
		System.out.println("SendApdu - cmd = \"" + HexDump.toHexString(cmd) + "\"");
		long begin = System.currentTimeMillis();
		long ok = reader.SendApdu(cmd, byteRecv, byteState);
		long end = System.currentTimeMillis();
		System.out.println("SendApdu - COST " + (end - begin));
		
		if (ok > 0) {

			if (ok > 2) {
				
				state.data = byteState;
				
				int dataLen = (int) ok - 2;
				recv.data = new byte[dataLen];
				System.arraycopy(byteRecv, 0, recv.data, 0, dataLen);
				
				System.out.println("SendApdu - recv = \"" + HexDump.toHexString(recv.data) + "\"");
				System.out.println("SendApdu - state = \"" + HexDump.toHexString(state.data) + "\"");
				
			} else {
				
				state.data = Arrays.copyOf(byteState, byteState.length);
				
				System.out.println("SendApdu - state = \"" + HexDump.toHexString(state.data) + "\"");
				
			}
			
			
			return ErrorCode.ERR_SUCCESS;
			
		}
		
		return ErrorCode.ERR_BASE_USER | ok;
		
	}
	
	
	
	@Override
	public long reset() {
		
		return ErrorCode.ERR_BASE_USER | 0xFF;
		
	}

	@Override
	public long lock() {
		
		return ErrorCode.ERR_BASE_USER | 0xFF;
		
	}


	@Override
	public long unlock() {
		
		return ErrorCode.ERR_BASE_USER | 0xFF;
		
	}

}
