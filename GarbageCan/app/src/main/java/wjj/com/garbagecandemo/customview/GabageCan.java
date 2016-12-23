package wjj.com.garbagecandemo.customview;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.Keyframe;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Interpolator;

import com.elvishew.xlog.XLog;

import wjj.com.garbagecandemo.R;


/**
 * Created by jiajiewang on 2016/12/15.
 */

public class GabageCan extends View {

    private Paint paint;
    private Path path;
    private int viewWidth, viewHeight, picWidth, picHeight;
    private int[] centerPoint;
    private int[] viewPoint;
    private Float canBAnimProgress;
    private ValueAnimator animator;
    private Rect globalRect;

    public GabageCan(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GabageCan(Context context) {
        super(context);
        init();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint.setColor(getResources().getColor(R.color.colorAccent));
        //设置画笔
        paint.setStrokeWidth(5f);
//        canvas.drawColor(Color.WHITE);
        //创建下半部分的路径和三条线
        createCanBPath();
        //画路径和线
        canvas.drawPath(path, paint);
        //动画判断是否刷新视图
        if (animator != null && animator.isRunning()) {
            //动画执行过程中具体帧值
            canBAnimProgress = (Float) animator.getAnimatedValue();
            drawCan(0, canvas);
            drawCan(1, canvas);
            drawCan(2, canvas);
            invalidate();
        } else if (animator != null && !animator.isRunning()) {
            drawCan(1, canvas);
            drawCan(2, canvas);
        }
    }

    public void startAnimator() {
        animator = ValueAnimator.ofFloat(1f, 0f);
        animator.setDuration(2500);
        animator.start();
        invalidate();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = getMySize(100, widthMeasureSpec);
        int height = getMySize(100, heightMeasureSpec);

        if (width < height) {
            height = width;
        } else {
            width = height;
        }

        setMeasuredDimension(width, height);
        XLog.d("--->width " + width + " height " + height);
        viewWidth = width > 0 ? width : 0;
        viewHeight = height > 0 ? height : 0;
        if (viewWidth > 0) {
            picWidth = viewWidth / 3;
        }
        if (viewHeight > 0) {
            picHeight = viewHeight / 3;
        }
    }

    //初始化画笔
    private void init() {
        paint = new Paint();
        path = new Path();
        paint.setStyle(Paint.Style.STROKE);
    }

    private void drawCan(int type, Canvas canvas) {
        switch (type) {
            case 0:
                canvas.rotate(canBAnimProgress * 30, viewWidth / 2 + (picHeight / 2), viewHeight / 2 - (picWidth / 2));
                break;
            case 1:
                canvas.drawLine(viewWidth / 2 - (picWidth / 2) - 20, viewHeight / 2 - (picWidth / 2) - (picHeight / 8),
                        viewWidth / 2 + (picHeight / 2) + 20, viewHeight / 2 - (picWidth / 2) - (picHeight / 8), paint);
                break;
            case 2:
                canvas.drawRect(viewWidth / 2 - (picWidth / 9), viewHeight / 2 - (picWidth / 2) - (picHeight / 4), viewWidth / 2 + (picHeight / 9),
                        viewHeight / 2 - (picHeight / 2) - (picHeight / 8), paint);
                break;
            case 3:
                break;
        }
    }


    private void createCanBPath() {
        if (path == null) {
            path = new Path();
        }
        //画轮廓
        path.moveTo(viewWidth / 2 - (picWidth / 2), viewHeight / 2 - (picWidth / 2));
        path.lineTo(viewWidth / 2 - (picWidth / 3), viewHeight / 2 + (picHeight / 2));
        path.lineTo(viewWidth / 2 + (picHeight / 3), viewHeight / 2 + (picHeight / 2));
        path.lineTo(viewWidth / 2 + (picHeight / 3), viewHeight / 2 + (picHeight / 2));
        path.lineTo(viewWidth / 2 + (picHeight / 2), viewHeight / 2 - (picWidth / 2));
        //画里面的竖线
        path.moveTo(viewWidth / 2 - (picWidth / 5), viewHeight / 2 - (picWidth / 3));
        path.lineTo(viewWidth / 2 - (picWidth / 5), viewHeight / 2 + (picHeight / 3));
        path.moveTo(viewWidth / 2 + (picWidth / 5), viewHeight / 2 - (picWidth / 3));
        path.lineTo(viewWidth / 2 + (picWidth / 5), viewHeight / 2 + (picHeight / 3));
        path.moveTo(viewWidth / 2, viewHeight / 2 - (picWidth / 3));
        path.lineTo(viewWidth / 2, viewHeight / 2 + (picHeight / 3));
    }


    private int getMySize(int defaultSize, int measureSpec) {
        int mySize = defaultSize;

        int mode = MeasureSpec.getMode(measureSpec);
        int size = MeasureSpec.getSize(measureSpec);

        switch (mode) {
            case MeasureSpec.UNSPECIFIED: {//如果没有指定大小，就设置为默认大小
                mySize = defaultSize;
                break;
            }
            case MeasureSpec.AT_MOST: {//如果测量模式是最大取值为size
                //我们将大小取最大值,你也可以取其他值
                mySize = size;
                break;
            }
            case MeasureSpec.EXACTLY: {//如果是固定的大小，那就不要去改变它
                mySize = size;
                break;
            }
        }
        return mySize;
    }

    //获取控件中心
    public int[] getCenterPoint() {
        if (globalRect == null) {
            globalRect = new Rect();
        }
        if (centerPoint == null) {
            centerPoint = new int[2];
        }
        this.getLocationOnScreen(centerPoint);
        centerPoint[0] = centerPoint[0] + viewWidth / 2;
        centerPoint[1] = centerPoint[1] + viewHeight / 2;
        return centerPoint;
    }

    //丢垃圾丢动画
    public void throwAnim(final View view) {
        if (viewPoint != null) {
            viewPoint = null;
        }
        viewPoint = new int[2];
        view.getLocationOnScreen(viewPoint);
        XLog.d("--->viewPoint x: " + viewPoint[0] + " y: " + viewPoint[1]);
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(view, "rotationY", 0.0f, 360.0f);
        ObjectAnimator objectAnimator1 = ObjectAnimator.ofFloat(view, "translationX", 0.0f, centerPoint[0] - viewPoint[0] - viewWidth / 2);
        ObjectAnimator objectAnimator2 = ObjectAnimator.ofFloat(view, "translationY", 0.0f, centerPoint[1] - viewPoint[1] - viewHeight / 4);
        ObjectAnimator objectAnimator4 = ObjectAnimator.ofFloat(view, "scaleX", 1.0f, 0.6f);
        ObjectAnimator objectAnimator5 = ObjectAnimator.ofFloat(view, "scaleY", 1.0f, 0.6f);
        AnimatorSet animationSet = new AnimatorSet();//组合动画
        animationSet.play(objectAnimator1).with(objectAnimator2).with(objectAnimator).with(objectAnimator4).with(objectAnimator5);
        animationSet.setDuration(800);
        animationSet.setInterpolator(new AccelerateInterpolator());
        animationSet.start();
        animationSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                startShakeByPropertyAnim(view, 0.4f, 0.6f, 7.0f, 800);
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
    }

    private void startShakeByPropertyAnim(final View view, float scaleSmall, float scaleLarge, float shakeDegrees, long duration) {
        if (view == null) {
            return;
        }

        //先变小后变大
        PropertyValuesHolder scaleXValuesHolder = PropertyValuesHolder.ofKeyframe(View.SCALE_X,
                Keyframe.ofFloat(0f, 1.0f),
                Keyframe.ofFloat(0.25f, scaleSmall),
                Keyframe.ofFloat(0.5f, scaleLarge),
                Keyframe.ofFloat(0.75f, scaleLarge),
                Keyframe.ofFloat(1.0f, scaleLarge)
        );
        PropertyValuesHolder scaleYValuesHolder = PropertyValuesHolder.ofKeyframe(View.SCALE_Y,
                Keyframe.ofFloat(0f, 1.0f),
                Keyframe.ofFloat(0.25f, scaleSmall),
                Keyframe.ofFloat(0.5f, scaleLarge),
                Keyframe.ofFloat(0.75f, scaleLarge),
                Keyframe.ofFloat(1.0f, scaleLarge)
        );

        //先往左再往右
        PropertyValuesHolder rotateValuesHolder = PropertyValuesHolder.ofKeyframe(View.ROTATION,
                Keyframe.ofFloat(0f, 0f),
                Keyframe.ofFloat(0.1f, -shakeDegrees),
                Keyframe.ofFloat(0.2f, shakeDegrees),
                Keyframe.ofFloat(0.3f, -shakeDegrees),
                Keyframe.ofFloat(0.4f, shakeDegrees),
                Keyframe.ofFloat(0.5f, -shakeDegrees),
                Keyframe.ofFloat(0.6f, shakeDegrees),
                Keyframe.ofFloat(0.7f, -shakeDegrees),
                Keyframe.ofFloat(0.8f, shakeDegrees),
                Keyframe.ofFloat(0.9f, -shakeDegrees),
                Keyframe.ofFloat(1.0f, 0f)
        );

        ObjectAnimator objectAnimator = ObjectAnimator.ofPropertyValuesHolder(view, scaleXValuesHolder, scaleYValuesHolder, rotateValuesHolder);
        objectAnimator.setDuration(duration);
        objectAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                view.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        objectAnimator.start();
    }
}
