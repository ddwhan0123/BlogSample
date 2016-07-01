package com.wjj.demo.BroadcastReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsMessage;

import com.apkfuns.logutils.LogUtils;
import com.wjj.demo.Model.PhoneMessage;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by jiajiewang on 16/6/30.
 */
public class SMSBroadcastReceiver extends BroadcastReceiver {


    private static MessageListener mMessageListener;
    public static final String SMS_RECEIVED_ACTION = "android.provider.Telephony.SMS_RECEIVED";

    public SMSBroadcastReceiver() {
        super();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        LogUtils.d("----> SMSBroadcastReceiver onReceive");
        if (intent.getAction().equals(SMS_RECEIVED_ACTION)) {
            Object[] pdus = (Object[]) intent.getExtras().get("pdus");
            for (Object pdu : pdus) {
                SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdu);
                String sender = smsMessage.getDisplayOriginatingAddress();
                //短信内容
                String content = smsMessage.getDisplayMessageBody();
                long date = smsMessage.getTimestampMillis();
                Date tiemDate = new Date(date);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String time = simpleDateFormat.format(tiemDate);
                PhoneMessage phoneMessage = new PhoneMessage();
                phoneMessage.setPhoneNumber(sender);
                phoneMessage.setMsgTime(time);
                phoneMessage.setMsgContent(content);

                mMessageListener.onReceived(phoneMessage);
                abortBroadcast();
            }
        }

    }

    //回调接口
    public interface MessageListener {
        void onReceived(PhoneMessage phoneMessage);
    }

    public void setOnReceivedMessageListener(MessageListener messageListener) {
        this.mMessageListener = messageListener;
    }


}