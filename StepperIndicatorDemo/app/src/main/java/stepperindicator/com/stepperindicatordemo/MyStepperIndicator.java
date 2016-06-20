package stepperindicator.com.stepperindicatordemo;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Build;
import android.support.annotation.UiThread;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jiajiewang on 16/6/20.
 */
public class MyStepperIndicator extends View {
    private static final float EXPAND_MARK = 1.3f;
    private static final int DEFAULT_ANIMATION_DURATION = 250;
    private Resources resources;
    private Paint circlePaint;
    private Paint linePaint, lineDonePaint, lineDoneAnimatedPaint;
    private Paint indicatorPaint;
    private float circleRadius;
    private float animProgress;
    private float checkRadius;
    private float animIndicatorRadius;
    private float indicatorRadius;
    private float lineLength;
    private float lineMargin;
    private float animCheckRadius;
    private int animDuration;
    private int stepCount;
    private int currentStep;
    private Bitmap doneIcon;
    private float[] indicators;
    private List<Path> linePathList = new ArrayList<>();
    private AnimatorSet animatorSet;
    private ObjectAnimator lineAnimator, indicatorAnimator, checkAnimator;
    private int previousStep;

    //构造函数
    public MyStepperIndicator(Context context) {
        this(context, null);
    }

    public MyStepperIndicator(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyStepperIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public MyStepperIndicator(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        //获取项目资源用
        resources = getResources();
        // 默认值
        int defaultCircleColor = ContextCompat.getColor(context, R.color.stpi_default_circle_color);
        float defaultCircleRadius = resources.getDimension(R.dimen.stpi_default_circle_radius);
        float defaultCircleStrokeWidth = resources.getDimension(R.dimen.stpi_default_circle_stroke_width);

        int defaultIndicatorColor = ContextCompat.getColor(context, R.color.stpi_default_indicator_color);
        float defaultIndicatorRadius = resources.getDimension(R.dimen.stpi_default_indicator_radius);

        float defaultLineStrokeWidth = resources.getDimension(R.dimen.stpi_default_line_stroke_width);
        float defaultLineMargin = resources.getDimension(R.dimen.stpi_default_line_margin);
        int defaultLineColor = ContextCompat.getColor(context, R.color.stpi_default_line_color);
        int defaultLineDoneColor = ContextCompat.getColor(context, R.color.stpi_default_line_done_color);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.StepperIndicator, defStyleAttr, 0);

        circlePaint = new Paint();
        circlePaint.setStrokeWidth(a.getDimension(R.styleable.StepperIndicator_stpi_circleStrokeWidth, defaultCircleStrokeWidth));
        circlePaint.setStyle(Paint.Style.STROKE);
        circlePaint.setColor(a.getColor(R.styleable.StepperIndicator_stpi_circleColor, defaultCircleColor));
        circlePaint.setAntiAlias(true);

        indicatorPaint = new Paint(circlePaint);
        indicatorPaint.setStyle(Paint.Style.FILL);
        indicatorPaint.setColor(a.getColor(R.styleable.StepperIndicator_stpi_indicatorColor, defaultIndicatorColor));
        indicatorPaint.setAntiAlias(true);

        linePaint = new Paint();
        linePaint.setStrokeWidth(a.getDimension(R.styleable.StepperIndicator_stpi_lineStrokeWidth, defaultLineStrokeWidth));
        linePaint.setStrokeCap(Paint.Cap.ROUND);
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setColor(a.getColor(R.styleable.StepperIndicator_stpi_lineColor, defaultLineColor));
        linePaint.setAntiAlias(true);

        lineDonePaint = new Paint(linePaint);
        lineDonePaint.setColor(a.getColor(R.styleable.StepperIndicator_stpi_lineDoneColor, defaultLineDoneColor));

        lineDoneAnimatedPaint = new Paint(lineDonePaint);

        circleRadius = a.getDimension(R.styleable.StepperIndicator_stpi_circleRadius, defaultCircleRadius);
        checkRadius = circleRadius + circlePaint.getStrokeWidth() / 2f;
        indicatorRadius = a.getDimension(R.styleable.StepperIndicator_stpi_indicatorRadius, defaultIndicatorRadius);
        animIndicatorRadius = indicatorRadius;
        animCheckRadius = checkRadius;
        lineMargin = a.getDimension(R.styleable.StepperIndicator_stpi_lineMargin, defaultLineMargin);

        setStepCount(a.getInteger(R.styleable.StepperIndicator_stpi_stepCount, 2));
        animDuration = a.getInteger(R.styleable.StepperIndicator_stpi_animDuration, DEFAULT_ANIMATION_DURATION);

        a.recycle();

        doneIcon = BitmapFactory.decodeResource(resources, R.drawable.ic_done_white_18dp);

        if (isInEditMode())
            currentStep = Math.max((int) Math.ceil(stepCount / 2f), 1);
    }

    public void setStepCount(int stepCount) {
        if (stepCount < 2)
            throw new IllegalArgumentException("stepCount must be >= 2");
        this.stepCount = stepCount;
        currentStep = 0;
        compute();
        invalidate();
    }

    private void compute() {
        indicators = new float[stepCount];
        linePathList.clear();

        float startX = circleRadius * EXPAND_MARK + circlePaint.getStrokeWidth() / 2f;

        // Compute position of indicators and line length
        float divider = (getMeasuredWidth() - startX * 2f) / (stepCount - 1);
        lineLength = divider - (circleRadius * 2f + circlePaint.getStrokeWidth()) - (lineMargin * 2);

        // Compute position of circles and lines once
        for (int i = 0; i < indicators.length; i++)
            indicators[i] = startX + divider * i;
        for (int i = 0; i < indicators.length - 1; i++) {
            float position = ((indicators[i] + indicators[i + 1]) / 2) - lineLength / 2;
            final Path linePath = new Path();
            linePath.moveTo(position, getMeasuredHeight() / 2);
            linePath.lineTo(position + lineLength, getMeasuredHeight() / 2);
            linePathList.add(linePath);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        compute();
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    protected void onDraw(Canvas canvas) {
        float centerY = getMeasuredHeight() / 2f;

        // Currently Drawing animation from step n-1 to n, or back from n+1 to n
        boolean inAnimation = false;
        boolean inLineAnimation = false;
        boolean inIndicatorAnimation = false;
        boolean inCheckAnimation = false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            inAnimation = animatorSet != null && animatorSet.isRunning();
            inLineAnimation = lineAnimator != null && lineAnimator.isRunning();
            inIndicatorAnimation = indicatorAnimator != null && indicatorAnimator.isRunning();
            inCheckAnimation = checkAnimator != null && checkAnimator.isRunning();
        }

        boolean drawToNext = previousStep == currentStep - 1;
        boolean drawFromNext = previousStep == currentStep + 1;

        for (int i = 0; i < indicators.length; i++) {
            final float indicator = indicators[i];
            boolean drawCheck = i < currentStep || (drawFromNext && i == currentStep);

            // Draw back circle
            canvas.drawCircle(indicator, centerY, circleRadius, circlePaint);

            // If current step, or coming back from next step and still animating
            if ((i == currentStep && !drawFromNext) || (i == previousStep && drawFromNext && inAnimation)) {
                // Draw animated indicator
                canvas.drawCircle(indicator, centerY, animIndicatorRadius, indicatorPaint);
            }

            // Draw check mark
            if (drawCheck) {
                float radius = checkRadius;
                if ((i == previousStep && drawToNext)
                        || (i == currentStep && drawFromNext))
                    radius = animCheckRadius;
                canvas.drawCircle(indicator, centerY, radius, indicatorPaint);
                if (!isInEditMode()) {
                    if ((i != previousStep && i != currentStep) || (!inCheckAnimation && !(i == currentStep && !inAnimation)))
                        canvas.drawBitmap(doneIcon, indicator - (doneIcon.getWidth() / 2), centerY - (doneIcon.getHeight() / 2), null);
                }
            }

            // Draw lines
            if (i < linePathList.size()) {
                if (i >= currentStep) {
                    canvas.drawPath(linePathList.get(i), linePaint);
                    if (i == currentStep && drawFromNext && (inLineAnimation || inIndicatorAnimation)) // Coming back from n+1
                        canvas.drawPath(linePathList.get(i), lineDoneAnimatedPaint);
                } else {
                    if (i == currentStep - 1 && drawToNext && inLineAnimation) {
                        // Going to n+1
                        canvas.drawPath(linePathList.get(i), linePaint);
                        canvas.drawPath(linePathList.get(i), lineDoneAnimatedPaint);
                    } else
                        canvas.drawPath(linePathList.get(i), lineDonePaint);
                }
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int desiredHeight = (int) Math.ceil((circleRadius * EXPAND_MARK * 2) + circlePaint.getStrokeWidth());

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int width = widthMode == MeasureSpec.EXACTLY ? widthSize : getSuggestedMinimumWidth();
        int height = heightMode == MeasureSpec.EXACTLY ? heightSize : desiredHeight;

        setMeasuredDimension(width, height);
    }

    public int getStepCount() {
        return stepCount;
    }

    public int getCurrentStep() {
        return currentStep;
    }

    /**
     * Sets the current step
     *
     * @param currentStep a value between 0 (inclusive) and stepCount (inclusive)
     */
    @UiThread
    public void setCurrentStep(int currentStep) {
        if (currentStep < 0 || currentStep > stepCount)
            throw new IllegalArgumentException("Invalid step value " + currentStep);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            if (animatorSet != null)
                animatorSet.cancel();
            animatorSet = null;
            lineAnimator = null;
            indicatorAnimator = null;

            if (currentStep == this.currentStep + 1) {
                previousStep = this.currentStep;
                animatorSet = new AnimatorSet();

                // First, draw line to new
                lineAnimator = ObjectAnimator.ofFloat(MyStepperIndicator.this, "animProgress", 1.0f, 0.0f);

                // Same time, pop check mark
                checkAnimator = ObjectAnimator
                        .ofFloat(MyStepperIndicator.this, "animCheckRadius", indicatorRadius, checkRadius * EXPAND_MARK, checkRadius);

                // Finally, pop current step indicator
                animIndicatorRadius = 0;
                indicatorAnimator = ObjectAnimator
                        .ofFloat(MyStepperIndicator.this, "animIndicatorRadius", 0f, indicatorRadius * 1.4f, indicatorRadius);

                animatorSet.play(lineAnimator).with(checkAnimator).before(indicatorAnimator);
            } else if (currentStep == this.currentStep - 1) {
                previousStep = this.currentStep;
                animatorSet = new AnimatorSet();

                // First, pop out current step indicator
                indicatorAnimator = ObjectAnimator.ofFloat(MyStepperIndicator.this, "animIndicatorRadius", indicatorRadius, 0f);

                // Then delete line
                animProgress = 1.0f;
                lineDoneAnimatedPaint.setPathEffect(null);
                lineAnimator = ObjectAnimator.ofFloat(MyStepperIndicator.this, "animProgress", 0.0f, 1.0f);

                // Finally, pop out check mark to display step indicator
                animCheckRadius = checkRadius;
                checkAnimator = ObjectAnimator.ofFloat(MyStepperIndicator.this, "animCheckRadius", checkRadius, indicatorRadius);

                animatorSet.playSequentially(indicatorAnimator, lineAnimator, checkAnimator);
            }

            if (animatorSet != null) {
                lineAnimator.setDuration(Math.min(500, animDuration));
                lineAnimator.setInterpolator(new DecelerateInterpolator());
                indicatorAnimator.setDuration(lineAnimator.getDuration() / 2);
                checkAnimator.setDuration(lineAnimator.getDuration() / 2);

                animatorSet.start();
            }
        }

        this.currentStep = currentStep;
        invalidate();
    }

    public void setAnimIndicatorRadius(float animIndicatorRadius) {
        this.animIndicatorRadius = animIndicatorRadius;
        invalidate();
    }

    public void setAnimCheckRadius(float animCheckRadius) {
        this.animCheckRadius = animCheckRadius;
        invalidate();
    }

}
