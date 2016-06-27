package com.eid.SocketDemoServer.CustomView;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.apkfuns.logutils.LogUtils;
import com.eid.SocketDemoServer.Moeel.PhoneMessage;
import com.eid.SocketDemoServer.OutService;
import com.eid.SocketDemoServer.R;

/**
 * Created by jiajiewang on 16/6/27.
 */
public class FloatWindowMsgView extends LinearLayout {
    /**
     * 记录大悬浮窗的宽度
     */
    public static int viewWidth;

    /**
     * 记录大悬浮窗的高度
     */
    public static int viewHeight;

    public FloatWindowMsgView(final Context context, final PhoneMessage phoneMessage) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.float_window_msg, this);
        View view = findViewById(R.id.msg_root_layout);
        viewWidth = view.getLayoutParams().width;
        viewHeight = view.getLayoutParams().height;
        //手机号,内容,短信时间
        TextView msgTime = (TextView) findViewById(R.id.msgTime);
        TextView msgContent = (TextView) findViewById(R.id.msgContent);
        TextView phoneNumber = (TextView) findViewById(R.id.phoneNumber);


        msgTime.setText(phoneMessage.getMsgTime());
        msgContent.setText(phoneMessage.getMsgContent());
        phoneNumber.setText(phoneMessage.getPhoneNumber());
        findViewById(R.id.closeMsgWindow).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                MyWindowManager.removeMsgWindow(context);
                Intent intent = new Intent(getContext(), OutService.class);
                context.stopService(intent);
            }
        });
    }
}
