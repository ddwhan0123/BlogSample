package webview.demo.com.webviewdemo.tools;

import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.os.Build;

/**
 * Created by Ezreal on 2015/11/10.
 */

public class BlueToothTools {
    private static BlueToothTools oneInstance = null;
    private static BluetoothAdapter adapter = null;

    private boolean available = false;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    private BlueToothTools(Context context) {

        int sdkInt = Build.VERSION.SDK_INT;

        if (sdkInt <= Build.VERSION_CODES.JELLY_BEAN_MR1) {

            adapter = BluetoothAdapter.getDefaultAdapter();

        } else {

            BluetoothManager manager = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
            adapter = manager.getAdapter();

        }

        available = (null != adapter);

    }


    public boolean available() {

        return available;

    }

    public boolean isEnabled() {

        return adapter.isEnabled();

    }

    public static BlueToothTools getInstance(Context context) {

        if (null == oneInstance) {

            oneInstance = new BlueToothTools(context);

        }

        return oneInstance;

    }
}
