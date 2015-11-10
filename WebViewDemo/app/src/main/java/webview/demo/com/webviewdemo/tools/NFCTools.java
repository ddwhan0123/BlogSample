package webview.demo.com.webviewdemo.tools;

import android.content.Context;
import android.nfc.NfcAdapter;

/**
 * Created by Ezreal on 2015/11/10.
 */
public class NFCTools {

    private static NFCTools oneInstance = null;
    private static NfcAdapter nfcAdapter = null;


    private NFCTools(Context context) {

        nfcAdapter = NfcAdapter.getDefaultAdapter(context.getApplicationContext());

    }


    public static NFCTools getInstance(Context context) {

        if (null == oneInstance) {

            oneInstance = new NFCTools(context);

        }

        return oneInstance;

    }


    public NfcAdapter getAdapter() {

        return nfcAdapter;

    }


    public boolean isAvailable() {

        return null != nfcAdapter;

    }


    public boolean isEnabled() {

        return null != nfcAdapter && nfcAdapter.isEnabled();

    }
}
