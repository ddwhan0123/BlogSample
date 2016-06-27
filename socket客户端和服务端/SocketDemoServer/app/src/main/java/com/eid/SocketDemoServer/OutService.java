package com.eid.SocketDemoServer;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Handler;
import android.os.IBinder;

import com.apkfuns.logutils.LogUtils;
import com.eid.SocketDemoServer.CustomView.MyWindowManager;
import com.eid.SocketDemoServer.Moeel.PhoneMessage;

public class OutService extends Service {
    private int nativeCount;
    public List<PhoneMessage> phoneMessageArrayList;
    private boolean lastCount;

    /**
     * 用于在线程中创建或移除悬浮窗。
     */
    private Handler handler = new Handler();

    /**
     * 定时器，定时进行检测当前应该创建还是移除悬浮窗。
     */
    private Timer timer;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        List<PhoneMessage> valueList = (List<PhoneMessage>) intent.getSerializableExtra("msgContent");

        if (valueList != null) {
            phoneMessageArrayList = valueList;
            lastCount = true;
            LogUtils.d("--->lastCount 构造里" + lastCount);
        }


        int value = intent.getExtras().getInt("msgCount");
        nativeCount = value;
        // 开启定时器，每隔2秒刷新一次
        if (timer == null) {
            timer = new Timer();
            timer.scheduleAtFixedRate(new RefreshTask(), 0, 2000);
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Service被终止的同时也停止定时器继续运行
        timer.cancel();
        timer = null;
    }

    class RefreshTask extends TimerTask {

        @Override
        public void run() {
            // 当前界面是桌面，且没有悬浮窗显示，则创建悬浮窗。
            if (isHome() && !MyWindowManager.isWindowShowing()) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        MyWindowManager.createListWindow(getApplicationContext(), phoneMessageArrayList);
                    }
                });
            }
            // 当前界面是桌面，且有悬浮窗显示，则更新内存数据。
            else if (isHome() && MyWindowManager.isWindowShowing()) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
//                        LogUtils.d("---->内存数据 条数为 " + nativeCount);
//                        MyWindowManager.createBigWindow(getApplicationContext());
                        MyWindowManager.updateUsedPercent(getApplicationContext(), nativeCount);
                        if (lastCount) {
                            MyWindowManager.removeSmallWindow(getApplicationContext());
                            MyWindowManager.removeListWindow(getApplicationContext());
                            MyWindowManager.createListWindow(getApplicationContext(), phoneMessageArrayList);
                            lastCount = false;
                        }

                    }
                });
            }
        }

    }

    /**
     * 判断当前界面是否是桌面
     */
    private boolean isHome() {
        ActivityManager mActivityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningTaskInfo> rti = mActivityManager.getRunningTasks(1);
        return getHomes().contains(rti.get(0).topActivity.getPackageName());
    }

    /**
     * 获得属于桌面的应用的应用包名称
     *
     * @return 返回包含所有包名的字符串列表
     */
    private List<String> getHomes() {
        List<String> names = new ArrayList<String>();
        PackageManager packageManager = this.getPackageManager();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        List<ResolveInfo> resolveInfo = packageManager.queryIntentActivities(intent,
                PackageManager.MATCH_DEFAULT_ONLY);
        for (ResolveInfo ri : resolveInfo) {
            names.add(ri.activityInfo.packageName);
        }
        return names;
    }
}
