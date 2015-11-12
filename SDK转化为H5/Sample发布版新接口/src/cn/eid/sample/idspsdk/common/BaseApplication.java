package cn.eid.sample.idspsdk.common;

import com.decard.ble.cardreader.DcCardReader;

import android.app.Application;

public class BaseApplication extends Application {
	
	DcCardReader dcReader = null;
	
	public void setDcReader(DcCardReader dcReader) {
		
		this.dcReader = dcReader;
		
	}
	
	public DcCardReader getDcReader() {
		
		return dcReader;
		
	}

}
