package com.eid.SocketDemoServer;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.apkfuns.logutils.LogUtils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.ipTv)
    TextView ipTv;
    @BindView(R.id.startServer)
    Button startServer;
    @BindView(R.id.contentTv)
    TextView contentTv;
    public int msgCount = 0;
    Intent intent;
    private static final int PORT = 8888;
    private String hostip;
    private Handler myHandler;
    private volatile boolean flag = true;//线程标志位
    private ExecutorService mExecutorService = null; //线程池
    private volatile ServerSocket server = null;
    private List<Socket> mList = new ArrayList<Socket>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);


        myHandler = new Handler() {
            @SuppressLint("HandlerLeak")
            public void handleMessage(Message msg) {
                if (msg.what == 0x1234) {
                    contentTv.append("\n" + msg.obj.toString());
                    msgCount++;
                    LogUtils.d("--->msgCount 累积短信" + msgCount + " 条");
                    intent = new Intent(MainActivity.this, OutService.class);
                    intent.putExtra("msgCount", msgCount);
                    startService(intent);

                    Intent intent1 = new Intent();
                    //为Intent设置Action、Category属性
                    intent1.setAction(Intent.ACTION_MAIN);
                    intent1.addCategory(Intent.CATEGORY_HOME);
                    startActivity(intent1);
                }
            }
        };

    }

    @Override
    protected void onResume() {
        super.onResume();
        hostip = getLocalIpAddress();  //获取本机IP
        ipTv.setText("IP地址 : " + hostip);
    }

    @Override
    protected void onDestroy() {
        stopService(intent);
        super.onDestroy();
    }


    @OnClick(R.id.startServer)
    public void onClick() {
        //如果是“启动”，证明服务器是关闭状态，可以开启服务器
        if (startServer.getText().toString().equals("启动")) {
            LogUtils.d("--->flag:" + flag);
            ServerThread serverThread = new ServerThread();
            flag = true;
            serverThread.start();
            startServer.setText("关闭");
        } else {
            try {
                flag = false;
                server.close();
                for (int p = 0; p < mList.size(); p++) {
                    Socket s = mList.get(p);
                    s.close();
                }
                mExecutorService.shutdownNow();
                startServer.setText("启动");
                System.out.println("服务器已关闭");
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
    }

    //Server端的主线程
    class ServerThread extends Thread {

        public void stopServer() {
            try {
                if (server != null) {
                    server.close();
                    System.out.println("close task successed");
                }
            } catch (IOException e) {
                System.out.println("close task failded");
            }
        }

        public void run() {

            try {
                server = new ServerSocket(PORT);
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                System.out.println("S2: Error");
                e1.printStackTrace();
            }
            mExecutorService = Executors.newCachedThreadPool();  //创建一个线程池
            System.out.println("服务器已启动...");
            Socket client = null;
            while (flag) {
                try {
                    System.out.println("S3: Error");
                    client = server.accept();
                    System.out.println("S4: Error");
                    //把客户端放入客户端集合中
                    mList.add(client);
                    mExecutorService.execute(new Service(client)); //启动一个新的线程来处理连接
                } catch (IOException e) {
                    System.out.println("S1: Error");
                    e.printStackTrace();
                }
            }


        }
    }


    //处理与client对话的线程
    class Service implements Runnable {
        private volatile boolean kk = true;
        private Socket socket;
        private BufferedReader in = null;
        private String msg = "";

        public Service(Socket socket) {
            this.socket = socket;
            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                msg = "OK";
                this.sendmsg(msg);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        public void run() {

            while (kk) {
                try {
                    if ((msg = in.readLine()) != null) {
                        //当客户端发送的信息为：exit时，关闭连接
                        if (msg.equals("exit")) {
                            mList.remove(socket);
                            //in.close();
                            //socket.close();
                            break;
                            //接收客户端发过来的信息msg，然后发送给客户端。
                        } else {
                            Message msgLocal = new Message();
                            msgLocal.what = 0x1234;
                            msgLocal.obj = msg;
                            System.out.println(msgLocal.obj.toString());
                            System.out.println(msg);
                            myHandler.sendMessage(msgLocal);
                            msg = socket.getInetAddress() + ":" + msg + "（服务器发送）";
                            this.sendmsg(msg);
                        }

                    }
                } catch (IOException e) {
                    System.out.println("close");
                    kk = false;
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }

        }

        //向客户端发送信息
        public void sendmsg(String msg) {
            System.out.println(msg);
            PrintWriter pout = null;
            try {
                pout = new PrintWriter(new BufferedWriter(
                        new OutputStreamWriter(socket.getOutputStream())), true);
                pout.println(msg);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //获取本地IP
    public static String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface
                    .getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf
                        .getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && !inetAddress.isLinkLocalAddress()) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (SocketException ex) {
            LogUtils.e("--->WifiPreference IpAddress ", ex.toString());
        }
        return null;
    }

}
