package com.eid.SocketDemoClient.Moeel;

import java.io.Serializable;

/**
 * Created by jiajiewang on 16/6/27.
 */
public class PhoneMessage implements Serializable {
    String PhoneNumber;
    String MsgContent;

    public String getMsgTime() {
        return MsgTime;
    }

    public void setMsgTime(String msgTime) {
        MsgTime = msgTime;
    }

    String MsgTime;

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    public String getMsgContent() {
        return MsgContent;
    }

    public void setMsgContent(String msgContent) {
        MsgContent = msgContent;
    }
}
