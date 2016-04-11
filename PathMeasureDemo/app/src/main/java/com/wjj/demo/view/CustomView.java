package com.wjj.demo.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import com.wjj.demo.R;

public class CustomView extends View {

    ValueAnimator valueAnimator;

    private PathMeasure mPathMeasure;
    private Paint paint;
    private Path path;
    private float[] coords = new float[2];
    private int XDraw, YDraw;

    public CustomView(Context context) {
        super(context);
        init();
    }

    public CustomView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }


    private void init() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStrokeWidth(10);
        path = new Path();
        path.moveTo(100, 100);
        path.lineTo(500, 100);
        path.lineTo(500, 500);
        path.lineTo(100, 500);
        path.lineTo(100, 100);

        mPathMeasure = new PathMeasure(path, true);
        coords = new float[2];
        coords[0]=100;
        coords[1]=100;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        XDraw = measureHandler(widthMeasureSpec);
        YDraw = measureHandler(heightMeasureSpec);
        setMeasuredDimension(XDraw, YDraw);
    }

    //尺寸测绘
    private int measureHandler(int measureSpec) {
        int vale = 520;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        if (specMode == MeasureSpec.EXACTLY) {
            vale = specSize;
        } else if (specMode == MeasureSpec.AT_MOST) {
            vale = Math.min(vale, specSize);
        } else {
            vale = 520;
        }
        return vale;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.WHITE);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(getResources().getColor(R.color.plum));
        canvas.drawPath(path, paint);

        // 绘制对应目标
        paint.setColor(getResources().getColor(R.color.Violet));
        paint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(coords[0], coords[1], 20, paint);

    }

    // 开启动画
    public void startAnim(long duration) {
        valueAnimator = ValueAnimator.ofFloat(0, mPathMeasure.getLength());
        Log.d("-->measure length", "measure length = " + mPathMeasure.getLength());
        valueAnimator.setDuration(duration);
        // 减速插值器
        valueAnimator.setInterpolator(new DecelerateInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (Float) animation.getAnimatedValue();
                // 获取当前点坐标封装到coords
                mPathMeasure.getPosTan(value, coords, null);
                postInvalidate();
            }
        });
        valueAnimator.start();

    }
    //停止动画
    public void stopAnim() {
        valueAnimator.cancel();
    }

}
