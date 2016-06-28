package com.eid.SocketDemoClient;

import android.Manifest;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;

import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.apkfuns.logutils.LogUtils;
import com.eid.SocketDemoClient.Moeel.PhoneMessage;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import kr.co.namee.permissiongen.PermissionFail;
import kr.co.namee.permissiongen.PermissionGen;
import kr.co.namee.permissiongen.PermissionSuccess;

public class MainActivity extends AppCompatActivity {
    Handler handler;
    // 定义与服务器通信的子线程
    ClientThread clientThread;
    @BindView(R.id.contentET)
    EditText contentET;
    @BindView(R.id.sendBtn)
    Button sendBtn;
    @BindView(R.id.resultTv)
    TextView resultTv;
    private String value;
    private SMSBroadcastReceiver mSMSBroadcastReceiver;
    private ArrayList<String> filterArray;

    private static final String ACTION = "android.provider.Telephony.SMS_RECEIVED";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        value = getIntent().getExtras().getString("ipAddress");

        filterArray = getIntent().getStringArrayListExtra("filterArray");
        if (filterArray.size() > 0) {
            LogUtils.d("---> filterArray.size()  = " + filterArray.size());
        }

        //生成广播处理
        mSMSBroadcastReceiver = new SMSBroadcastReceiver();

        PermissionGen.with(MainActivity.this)
                .addRequestCode(100)
                .permissions(
                        Manifest.permission.RECEIVE_SMS,
                        Manifest.permission.READ_SMS)
                .request();


        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                // 如果消息来自于子线程
                if (msg.what == 0x123) {
                    // 将读取的内容追加显示在文本框中
                    resultTv.append("\n" + msg.obj.toString());
                }
            }
        };

        clientThread = new ClientThread(handler, value);
        // 客户端启动ClientThread线程创建网络连接、读取来自服务器的数据
        new Thread(clientThread).start();

        mSMSBroadcastReceiver.setOnReceivedMessageListener(new SMSBroadcastReceiver.MessageListener() {
            @Override
            public void onReceived(PhoneMessage phoneMessage) {
                for (int k = 0; k < filterArray.size(); k++) {
                    if (phoneMessage.getMsgContent().contains(filterArray.get(k))) {
                        String msg = phoneMessage.getPhoneNumber() + "*" + phoneMessage.getMsgTime() + "*" + phoneMessage.getMsgContent();
                        sendMsg(msg);
                        LogUtils.d("--->发送的对象是 " + phoneMessage);
                    }
                }

            }
        });
    }

    @OnClick(R.id.sendBtn)
    public void onClick() {
        if (contentET.getText().toString().trim().length() > 0) {
            sendMsg(contentET.getText().toString());
        } else {
            Toast.makeText(MainActivity.this, "输入内容不能为空", Toast.LENGTH_SHORT).show();
        }

    }

    private void sendMsg(String value) {
        LogUtils.d("---> sendMsg");
        // 当用户按下发送按钮后，将用户输入的数据封装成Message，
        // 然后发送给子线程的Handler
        Message msg = new Message();
        msg.what = 0x345;
        msg.obj = value;
        clientThread.revHandler.sendMessage(msg);
        // 清空input文本框
        contentET.setText("");
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mSMSBroadcastReceiver != null) {
            this.unregisterReceiver(mSMSBroadcastReceiver);
            mSMSBroadcastReceiver = null;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        PermissionGen.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
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
}
