package com.wjj.numberdemo.imp;

import android.os.Message;
import android.util.Log;

import com.wjj.numberdemo.bean.Book;
import com.wjj.numberdemo.listener.CommunicateListener;
import com.wjj.numberdemo.listener.ReadListener;

/**
 * Created by jiajiewang on 16/9/2.
 */
public class ReadImp implements ReadListener<Book<String>> {
    Book<String> book;
    CommunicateListener communicate;

    public void setMessager(CommunicateListener communicate) {
        this.communicate = communicate;
    }

    public ReadImp(Book<String> book) {
        this.book = book;
    }

    @Override
    public void start() {
        Log.d("--->", "ReadImp 开始读书");
    }

    @Override
    public void doing(Book<String> book) {
        Log.d("--->", "现在读到第 " + book.getNowPage() + "页");
    }

    @Override
    public void finish(Book<String> book) {
        Log.d("--->", "读完了,总共读了 " + book.getNowPage() + "页");
        Message message = Message.obtain();
        message.obj = book.getBookMoreMSG();
        communicate.setMsg(message);
    }
}
