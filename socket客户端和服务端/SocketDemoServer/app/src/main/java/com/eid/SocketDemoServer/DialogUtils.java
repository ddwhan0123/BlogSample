package com.eid.SocketDemoServer;

import android.content.Context;

import com.afollestad.materialdialogs.MaterialDialog;

/**
 * Created by jiajiewang on 16/6/24.
 */
public class DialogUtils {

    public static void showDialog(Context context, String title, String content) {
        new MaterialDialog.Builder(context)
                .title(title)
                .content(content)
                .positiveText("确定")
                .show();
    }
}
