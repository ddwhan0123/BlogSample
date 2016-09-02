package com.wjj.numberdemo.thread;

import com.wjj.numberdemo.bean.Book;
import com.wjj.numberdemo.imp.ReadImp;

/**
 * Created by jiajiewang on 16/9/2.
 */
public class ReadThread implements Runnable {
    Book book;
    ReadImp readImp;

    public ReadThread(Book book, ReadImp readImp) {
        this.book = book;
        this.readImp = readImp;
    }

    @Override
    public void run() {
        readImp.start();
        while (book.getNowPage() < book.getPageCount()) {

            try {
                Thread.sleep(400);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                e.printStackTrace();
            }
            book.NowPage++;
            readImp.doing(book);

        }
        readImp.finish(book);
    }
}