package imagecache.demo.com.imagecachedemo.utils;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.apkfuns.logutils.LogUtils;

/**
 * Created by Ezreal on 2015/10/16.
 * 网络判断
 */
public class NetworkStateService extends Service {

    final static String TAG = NetworkStateService.class.getName() + ".TAG";

    class NetworkState {
        final static int TYPE_WIFI = 1;
        final static int TYPE_MOBILE = 2;
        final static int TYPE_NO_NETWORK = 3;

    }

    private ConnectivityManager connectivityManager;
    private NetworkInfo netInfo;


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case NetworkState.TYPE_WIFI:
                    //Toast.makeText(getApplicationContext(),"WIFI状态",Toast.LENGTH_SHORT).show();
                    break;
                case NetworkState.TYPE_MOBILE:
                    //Toast.makeText(getApplicationContext(),"移动网络",Toast.LENGTH_SHORT).show();
                    break;
                case NetworkState.TYPE_NO_NETWORK:
                    Toast.makeText(getApplicationContext(), "~。~没有网络了", Toast.LENGTH_SHORT).show();

//                    NetworkSettingDialog(getApplicationContext());
                    break;
            }
        }
    };

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
                connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                netInfo = connectivityManager.getActiveNetworkInfo();
                if (netInfo != null && netInfo.isAvailable()) {
                    //网络连接
                    String name = netInfo.getTypeName();

                    if (netInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                        //WiFi网络
                        LogUtils.d("Wifi :" + name);
                        mHandler.sendEmptyMessageAtTime(NetworkState.TYPE_WIFI, 1000);
                    } else if (netInfo.getType() == ConnectivityManager.TYPE_ETHERNET) {
                        //有线网络
                        LogUtils.d("有线网络 :" + name);
                    } else if (netInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                        //手机网络
                        LogUtils.d("手机网络 :" + name);
                        mHandler.sendEmptyMessageAtTime(NetworkState.TYPE_MOBILE, 1000);
                    }
                } else {
                    //网络断开
                    LogUtils.d("网络断开");
                    mHandler.sendEmptyMessage(NetworkState.TYPE_NO_NETWORK);

                }
            }
        }

    };


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        IntentFilter mFilter = new IntentFilter();
        mFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(mReceiver, mFilter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtils.d("--->onDestroy " + TAG);
        if (mReceiver != null) {
            unregisterReceiver(mReceiver);
        }
    }

//    public static void NetworkSettingDialog(final Context context) {
//        LogUtils.d("---->NetworkSettingDialog被执行");
//        AlertDialog.Builder builder = new AlertDialog.Builder(context);
//        builder.setMessage("要开启网络设置么?");
//        builder.setTitle("提示内容");
//        builder.setPositiveButton("开启", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//                // 跳转到系统的网络设置界面
//                Intent intent = null;
//                // 先判断当前系统版本
//                if(android.os.Build.VERSION.SDK_INT > 10){  // 3.0以上
//                    intent = new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                }else{
//                    intent = new Intent();
//                    intent.setClassName("com.android.settings", "com.android.settings.WirelessSettings");
//                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                }
//                context.startActivity(intent);
//
//            }
//        });
//        builder.setNegativeButton("不开启", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//            }
//        });
//        Dialog dialog=builder.create();
//        dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
//        dialog.show();
//    }

}
