package com.eid.SocketDemoClient;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.apkfuns.logutils.LogUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
    private final String ip = "192.168.1.122";
    Handler handler;
    // 定义与服务器通信的子线程
    ClientThread clientThread;
    @BindView(R.id.contentET)
    EditText contentET;
    @BindView(R.id.sendBtn)
    Button sendBtn;
    @BindView(R.id.resultTv)
    TextView resultTv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
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

        clientThread = new ClientThread(handler, ip);
        // 客户端启动ClientThread线程创建网络连接、读取来自服务器的数据
        new Thread(clientThread).start();

    }

    @OnClick(R.id.sendBtn)
    public void onClick() {

        // 当用户按下发送按钮后，将用户输入的数据封装成Message，
        // 然后发送给子线程的Handler
        Message msg = new Message();
        msg.what = 0x345;
        msg.obj = contentET.getText().toString();
        clientThread.revHandler.sendMessage(msg);
        // 清空input文本框
        contentET.setText("");
    }
}
