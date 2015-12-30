package paintcanvasdemo.pro.wjj.paintcanvasdemo.CustomView;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import paintcanvasdemo.pro.wjj.paintcanvasdemo.R;

/**
 * Created by Ezreal on 2015/12/28.
 */
public class TestView extends View {
    Paint paint1;
    Context context;
    Bitmap bitmap;

    public TestView(Context context) {
        super(context);
        this.context = context;
    }

    public TestView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public TestView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public TestView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        paint1 = new Paint();
//        paint.setColor(getResources().getColor(R.color.SlateBlue));
        paint1.setColor(getResources().getColor(R.color.Gold));
        paint1.setStrokeWidth(3);                        //粗细
        paint1.setAntiAlias(true);                       //设置画笔为无锯齿
        bitmap = BitmapFactory.decodeResource(getResources(),
                R.drawable.bg).copy(Bitmap.Config.ARGB_8888, true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.d("--->onDraw", "onDraw()");
//        canvas.drawCircle(0, 0, 90, paint);
        canvas.drawCircle(100, 100, 90, paint1);
        canvas.drawBitmap(bitmap, 120, 120, paint1);
        Canvas canvas1 = new Canvas(bitmap);

        Paint paint2 = new Paint();
        paint2.setColor(getResources().getColor(R.color.LightPink));
        paint2.setTextSize(50);

//        canvas1.translate(300, 300);
        canvas1.drawText("Ezreal", 0, 200, paint2);
//        canvas1.rotate(30);
        canvas1.drawText("Malzahar ", 0, 300, paint2);
        canvas1.scale(1, 1.5f);//y轴放大1.5f x不变
        canvas1.skew(1.732f,0);//X轴倾斜60度，Y轴不变
        canvas1.drawText("Akali ", 200, 200, paint2);
//        canvas1.drawText("Katarina Du Couteau ", 200, 400, paint2);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        Log.d("--->onLayout", "changed = " + changed + " left = " + left + " top = " + top + " right = " + right + " bottom " + bottom);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.d("--->onMeasure", "  widthMeasureSpec =" + widthMeasureSpec + "  heightMeasureSpec = " + heightMeasureSpec);
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
    }
}