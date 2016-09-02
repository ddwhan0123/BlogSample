package com.wjj.numberdemo.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import com.wjj.numberdemo.R;


/**
 * Created by jiajiewang on 16/9/2.
 */
public class ReadBookTextView extends SurfaceView implements SurfaceHolder.Callback, View.OnTouchListener {
    private Context context;
    private SurfaceHolder surfaceHolder;
    private CustomThread customThread;


    public ReadBookTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setKeepScreenOn(true);
        this.setFocusable(true);
        //用于触摸事件
        this.setLongClickable(true);
        this.context = context;
        //获取对象实例
        surfaceHolder = this.getHolder();
        // 给SurfaceView添加回调
        surfaceHolder.addCallback(this);
        //创建工作线程
        customThread = new CustomThread(surfaceHolder, context);

    }

    //创建的时候调用
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        customThread.canRun = true;
        //工作线程开始工作
        customThread.start();
        Log.d("--->", "surfaceCreated");
    }

    //发生改变的时候调用
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

        Log.d("--->", "surfaceChanged");
    }

    //销毁时的时候调用
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

        customThread.canRun = false;
        customThread.interrupt();
        Log.d("--->", "surfaceDestroyed");

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:


                Log.d("MotionEvent", "ACTION_DOWN");

                break;

            case MotionEvent.ACTION_UP:


                Log.d("MotionEvent", "ACTION_UP");

                break;


            case MotionEvent.ACTION_MOVE:


                Log.d("MotionEvent", "ACTION_MOVE");

                break;


        }

        return super.onTouchEvent(event);

    }

    public void setText(String text) {
        CustomThread.text = text;
    }
}

//工作线程
class CustomThread extends Thread {
    private Context context;
    private SurfaceHolder surfaceHolder;
    public volatile boolean canRun;

    public static String text = "";
    //创建画笔
    Paint paint;

    public CustomThread(SurfaceHolder surfaceHolder, Context context) {
        this.surfaceHolder = surfaceHolder;
        this.context = context;
        canRun = true;
        paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setTextSize(40);
    }

    @Override
    public void run() {
        Canvas canvas = null;
        while (canRun) {
            //线程同步
            synchronized (surfaceHolder) {
                // 锁定画布(想一下,类似Bitmap的效果)
                canvas = surfaceHolder.lockCanvas();
                //画背景
                canvas.drawColor(context.getResources().getColor(R.color.white));
                //画字
                canvas.drawText(text, 15, 40, paint);
            }

            if (canvas != null) {
                // 结束锁定
                surfaceHolder.unlockCanvasAndPost(canvas);
            }

        }
    }
}