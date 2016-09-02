package com.wjj.numberdemo.activity;

import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.wjj.numberdemo.R;
import com.wjj.numberdemo.bean.Book;
import com.wjj.numberdemo.imp.ReadImp;
import com.wjj.numberdemo.listener.CommunicateListener;
import com.wjj.numberdemo.thread.ReadThread;
import com.wjj.numberdemo.widget.ReadBookTextView;


public class MainActivity extends AppCompatActivity implements View.OnClickListener, CommunicateListener {
    private Button read_btn, send_text;
    private ReadImp readImp;
    private EditText edit_baobao;
    private ReadThread readThread;
    private Thread thread;
    private volatile Book<String> book;
    private ReadBookTextView read_text;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        Log.d("--->", "主线程的 ID 是 " + Thread.currentThread().getId());
        //给书赋值
        addBook();
        readImp = new ReadImp(book);
        readImp.setMessager(this);
        readThread = new ReadThread(book, readImp);
        thread = new Thread(readThread);
        initWidget();
    }

    @Override
    protected void onStart() {
        super.onStart();
        thread.start();
        Log.d("--->", "开启的线程 ID 是 " + thread.getId());
    }

    @Override
    protected void onStop() {
        super.onStop();
        thread.interrupt();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        thread = null;
        readThread = null;
    }

    private void initWidget() {
        read_text = (ReadBookTextView) findViewById(R.id.read_text);
        read_btn = (Button) findViewById(R.id.read_btn);
        send_text = (Button) findViewById(R.id.send_text);
        edit_baobao = (EditText) findViewById(R.id.edit_baobao);
        send_text.setOnClickListener(this);
        read_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.read_btn:
                read_text.setText("");
                thread.interrupt();
                break;
            case R.id.send_text:
                if (edit_baobao.getText().toString().trim().length() > 0) {
                    read_text.setText(edit_baobao.getText().toString().trim());
                }
                break;
        }
    }

    @Override
    public void setMsg(Message msg) {
        read_text.setText("补充内容是 " + msg.obj.toString());
    }

    /*
    * 模拟数据
    * */
    private Book<String> addBook() {
        book = new Book<>();
        book.setBookMoreMSG("神秘的反派角色");
        book.setName("奥特曼打怪兽");
        book.setPageCount(15);
        book.setNowPage(0);
        return book;
    }
}
