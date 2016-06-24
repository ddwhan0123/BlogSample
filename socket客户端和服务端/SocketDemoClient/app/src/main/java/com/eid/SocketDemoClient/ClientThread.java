package com.eid.SocketDemoClient;

/**
 * Created by jiajiewang on 16/6/23.
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketTimeoutException;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.apkfuns.logutils.LogUtils;

/**
 */
public class ClientThread implements Runnable {
    private Socket s;
    public String ip;
    // 定义向UI线程发送消息的Handler对象
    private Handler handler;
    // 定义接收UI线程的消息的Handler对象
    public Handler revHandler;
    // 该线程所处理的Socket所对应的输入流
    BufferedReader br = null;
    OutputStream os = null;

    public ClientThread(Handler handler, String ip) {
        this.handler = handler;
        this.ip = ip;
    }

    public void run() {
        try {
            System.out.println("T1");
            s = new Socket(ip, 8888);
            System.out.println("T2");
            br = new BufferedReader(new InputStreamReader(
                    s.getInputStream()));
            os = s.getOutputStream();
            // 启动一条子线程来读取服务器响应的数据
            new Thread() {
                @Override
                public void run() {
                    String content = null;
                    // 不断读取Socket输入流中的内容。
                    try {
                        while ((content = br.readLine()) != null) {
                            // 每当读到来自服务器的数据之后，发送消息通知程序界面显示该数据
                            Message msg = new Message();
                            msg.what = 0x123;
                            msg.obj = content;
                            handler.sendMessage(msg);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }.start();
            // 为当前线程初始化Looper
            Looper.prepare();
            // 创建revHandler对象
            revHandler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    // 接收到UI线程中用户输入的数据
                    if (msg.what == 0x345) {
                        // 将用户在文本框内输入的内容写入网络
                        try {
                            LogUtils.d("---> 发送的内容是 " + msg.obj.toString());
                            os.write((msg.obj.toString() + "\r\n")
                                    .getBytes("utf-8"));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            };
            // 启动Looper
            Looper.loop();
        } catch (SocketTimeoutException e1) {
            LogUtils.e("--->网络连接超时！！");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}