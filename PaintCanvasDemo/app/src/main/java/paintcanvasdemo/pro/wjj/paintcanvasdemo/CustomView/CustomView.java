package paintcanvasdemo.pro.wjj.paintcanvasdemo.CustomView;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import paintcanvasdemo.pro.wjj.paintcanvasdemo.R;
import paintcanvasdemo.pro.wjj.paintcanvasdemo.tools;

/**
 * Created by Ezreal on 2015/12/30.
 */
public class CustomView extends View implements View.OnTouchListener{

    public static interface OnViewOnTouchListener {
        void onTouchClick(View view,MotionEvent event);
    }
    private OnViewOnTouchListener mOnTouchClickListener = null;

    public void setOnTouchClickListener(OnViewOnTouchListener listener) {
        this.mOnTouchClickListener = listener;
    }



    Context context;
    int ScreenWidth, ScreenHeight;
    Paint barPaint, textPaint, bgPaint, buttonPaint;

    public CustomView(Context context) {
        super(context);
    }

    public CustomView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
        setOnTouchListener(this);
    }

    public CustomView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
        setOnTouchListener(this);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CustomView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
        setOnTouchListener(this);
    }

    private void init(Context context, AttributeSet attrs) {
        ScreenWidth = tools.getScreenWidth(context);
        ScreenHeight = tools.getScreenHeight(context);
        Log.d("--->Bar高，宽，长", "Bar高度等于 " + 144 + "ScreenWidth等于 " + ScreenWidth + "ScreenHeight等于 " + ScreenHeight);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //画背景色
        bgPaint = new Paint();
        bgPaint.setColor(getResources().getColor(R.color.White));
        canvas.drawRect(0, 0, ScreenWidth, ScreenHeight, bgPaint);

        barPaint = new Paint();
        barPaint.setColor(getResources().getColor(R.color.DoderBlue));
        canvas.drawRect(0, 0, ScreenWidth, 144, barPaint);
        //画Title
        barPaint.setTextSize(60);
        barPaint.setColor(getResources().getColor(R.color.White));
//        canvas.drawText("PaintCanvasDemo",0,60,barPaint);将不会出现任何东西drawText第一个参数是你要显示的字符，第二个为 x起点，第三个为底部的Y坐标而不是Y的起点，第四个参数就是你的画笔
        Paint.FontMetricsInt fontMetrics = barPaint.getFontMetricsInt();
        int baseline = (144 - fontMetrics.bottom - fontMetrics.top) / 2;
        canvas.drawText("PaintCanvasDemo", 40, baseline, barPaint);
        //画 不要怂，就是干
        textPaint = new Paint();
        textPaint.setColor(getResources().getColor(R.color.Black));
        textPaint.setTextSize(80);
        canvas.drawText("不要怂，就是干", 0, 144 + 80, textPaint);
        //画妹子
        // 定义矩阵对象
        Matrix matrix = new Matrix();
        // 缩放原图
        matrix.postScale(1.5f, 1.5f);
        Bitmap meizi = BitmapFactory.decodeResource(getResources(),
                R.drawable.bg2).copy(Bitmap.Config.ARGB_8888, true);
        Bitmap newMeiZi = Bitmap.createBitmap(meizi, 0, 0, meizi.getWidth(), meizi.getHeight(),
                matrix, true);
        //画妹子
        canvas.drawBitmap(newMeiZi, ScreenWidth / 2 - 450, ScreenHeight / 2 - 450, new Paint());

        buttonPaint = new Paint();
        buttonPaint.setColor(getResources().getColor(R.color.LightGrey));
        canvas.translate(0, -70);
        canvas.drawRect(15, ScreenHeight - 144, 231, ScreenHeight, buttonPaint);
        canvas.drawRect(15 + 231, ScreenHeight - 144, 231 * 2, ScreenHeight, buttonPaint);
        canvas.drawRect(15 + 231 + 231, ScreenHeight - 144, 231 * 3, ScreenHeight, buttonPaint);
        Log.d("--->drawRect1", "10  " + (ScreenHeight - 144) + "  231  " + ScreenHeight);
        //居中画字
        buttonPaint.setColor(getResources().getColor(R.color.Black));
        buttonPaint.setTextSize(65);
        canvas.drawText("按钮1", 15 + 30, ScreenHeight - 144 + baseline, buttonPaint);
        canvas.drawText("按钮2", 15 + 30+231, ScreenHeight - 144 + baseline, buttonPaint);
        canvas.drawText("按钮3", 15 + 30+(231*2), ScreenHeight - 144 + baseline, buttonPaint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (mOnTouchClickListener != null) {
            //注意这里使用getTag方法获取数据
            mOnTouchClickListener.onTouchClick(v,event);
        }
        return true;
    }
}
