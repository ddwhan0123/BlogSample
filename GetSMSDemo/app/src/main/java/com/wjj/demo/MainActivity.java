package com.wjj.demo;


import android.Manifest;
import android.content.IntentFilter;
import android.os.Handler;
import android.widget.TextView;
import android.widget.Toast;

import com.apkfuns.logutils.LogUtils;
import com.wjj.demo.BroadcastReceiver.SMSBroadcastReceiver;
import com.wjj.demo.Model.PhoneMessage;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import kr.co.namee.permissiongen.PermissionFail;
import kr.co.namee.permissiongen.PermissionGen;
import kr.co.namee.permissiongen.PermissionSuccess;

public class MainActivity extends BaseActivity {
    private static final String ACTION = "android.provider.Telephony.SMS_RECEIVED";
    private SMSBroadcastReceiver mSMSBroadcastReceiver;
    private List<String> intentList;
    private Handler handler;
    @BindView(R.id.text)
    TextView text;

    @Override
    int getLayout() {
        return R.layout.activity_main;
    }

    @Override
    void init() {
        ButterKnife.bind(this);
        intentList = getIntent().getStringArrayListExtra("dataList");
    }

    @Override
    void logic() {
        //授权
        PermissionGen.with(MainActivity.this)
                .addRequestCode(100)
                .permissions(
                        Manifest.permission.RECEIVE_SMS,
                        Manifest.permission.READ_SMS)
                .request();

        //生成广播处理
        mSMSBroadcastReceiver = new SMSBroadcastReceiver();
        mSMSBroadcastReceiver.setOnReceivedMessageListener(new SMSBroadcastReceiver.MessageListener() {
            @Override
            public void onReceived(PhoneMessage phoneMessage) {
                for (int k = 0; k < intentList.size(); k++) {
                    if (phoneMessage.getMsgContent().contains(intentList.get(k))) {
                        String msg = phoneMessage.getPhoneNumber() + "*" + phoneMessage.getMsgTime() + "*" + phoneMessage.getMsgContent();
                        LogUtils.d("--->发送的对象是 " + msg);
                        text.setText(msg);
                    }
                }

            }
        });
    }

    @Override
    void onResumeInit() {


    }

    @Override
    void onResumeLogic() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mSMSBroadcastReceiver != null) {
            this.unregisterReceiver(mSMSBroadcastReceiver);
            mSMSBroadcastReceiver = null;
        }
    }


    @PermissionSuccess(requestCode = 100)
    public void doRegisterReceiver() {
        //实例化过滤器并设置要过滤的广播
        IntentFilter intentFilter = new IntentFilter(ACTION);
        intentFilter.setPriority(1000);
        //注册广播
        this.registerReceiver(mSMSBroadcastReceiver, intentFilter);
    }

    @PermissionFail(requestCode = 100)
    public void doFailRegisterReceiver() {
        Toast.makeText(this, "Contact permission is not granted", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        PermissionGen.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }
}
