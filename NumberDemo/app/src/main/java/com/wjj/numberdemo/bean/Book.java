package com.wjj.numberdemo.bean;

/**
 * Created by jiajiewang on 16/9/2.
 */
public class Book<T> {

    public String Name; //书名
    public int PageCount; //总页数
    public int NowPage; //当前页数
    public T BookMoreMSG; //附加信息

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public int getPageCount() {
        return PageCount;
    }

    public void setPageCount(int pageCount) {
        PageCount = pageCount;
    }

    public T getBookMoreMSG() {
        return BookMoreMSG;
    }

    public void setBookMoreMSG(T bookMoreMSG) {
        BookMoreMSG = bookMoreMSG;
    }

    public int getNowPage() {
        return NowPage;
    }

    public void setNowPage(int nowPage) {
        NowPage = nowPage;
    }
}
