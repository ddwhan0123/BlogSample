package com.pinganfang.haofang.business.house.oldf;

import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pinganfang.haofang.R;
import com.pinganfang.haofang.base.BaseToolBarActivity;
import com.pinganfang.haofang.business.iconfont.HaofangIcon;
import com.projectzero.android.library.util.IconfontUtil;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

/**
 * Created by ex-wangjiajie256 on 2016-07-21.
 */
@EActivity(R.layout.activity_esf_taxes)
public class SecondHandHousingTaxesActivity extends BaseToolBarActivity implements View.OnTouchListener {
    private LinearLayout taxes_content_layout, loan_content_layout;
    /*
    弹窗的pop
    * */
    private PopupWindow taxesPop, loanPop;
    private Bitmap bitmap;
    private float touchY;

    @ViewById(R.id.show_loan)
    TextView show_loan;

    @ViewById(R.id.show_taxes)
    TextView show_taxes;

    @ViewById(R.id.back_esf)
    TextView back_esf;

    @ViewById(R.id.info_icon)
    TextView info_icon;

    @ViewById(R.id.loan_layout)
    RelativeLayout loan_layout;

    @ViewById(R.id.taxes_layout)
    RelativeLayout taxes_layout;

    @ViewById(R.id.to_inner)
    RelativeLayout to_inner;

    @ViewById(R.id.add_icon)
    TextView add_icon;

    @Override
    public int getHeadType() {
        return HEAD_TYPE_FULL_SCREEN;
    }

    @AfterViews
    void afterView() {
        makeFullScreen();
        IconfontUtil.setIcon(SecondHandHousingTaxesActivity.this, show_taxes, "#777777", 20, HaofangIcon.ICON_NEXT);
        IconfontUtil.setIcon(SecondHandHousingTaxesActivity.this, show_loan, "#777777", 20, HaofangIcon.ICON_NEXT);
        IconfontUtil.setIcon(SecondHandHousingTaxesActivity.this, back_esf, "#FFFFFF", 26, HaofangIcon.IC_BACK);
        IconfontUtil.setIcon(SecondHandHousingTaxesActivity.this, add_icon, "#777777", 24, HaofangIcon.IC_ADD);
    }

    @Click(R.id.back_esf)
    void backToEsf() {
        finish();
    }

    @Click(R.id.to_inner)
    void toInner() {
        showToast("跳转到 好按揭页面");
    }

    @Click(R.id.taxes_layout)
    void showTaxesPop() {
        getTaxesPopupWindow();
        taxesPop.showAtLocation(taxes_layout, Gravity.CENTER, 0, 0);
    }

    @Click(R.id.loan_layout)
    void showLoanPop() {
        getLoanPopupWindow();
        loanPop.showAtLocation(loan_layout, Gravity.CENTER, 0, 0);
    }


    /*
        初始化税费pop
        * */
    private void initTaxesPop() {
        if (bitmap == null) {
            bitmap = getFastBlurCurrentImage();
        }
        View taxesPopView = getLayoutInflater().inflate(R.layout.esf_taxes_pop, null, false);
        taxesPop = new PopupWindow(taxesPopView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
        TextView info_icon = (TextView) taxesPopView.findViewById(R.id.info_icon);
        RelativeLayout taxes_pop_root = (RelativeLayout) taxesPopView.findViewById(R.id.taxes_pop_root);
        taxes_content_layout = (LinearLayout) taxesPopView.findViewById(R.id.content_layout);
        IconfontUtil.setIcon(SecondHandHousingTaxesActivity.this, info_icon, "#999999", 10, HaofangIcon.ICON_INFO);
        taxes_pop_root.setBackgroundDrawable(new BitmapDrawable(bitmap));
        taxesPop.setBackgroundDrawable(new BitmapDrawable());
        taxes_pop_root.setOnTouchListener(this);
    }

    private void initLoanPop() {
        if (bitmap == null) {
            bitmap = getFastBlurCurrentImage();
        }
        View loanPopView = getLayoutInflater().inflate(R.layout.esf_loan_pop, null, false);
        loanPop = new PopupWindow(loanPopView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
        RelativeLayout loan_pop_root = (RelativeLayout) loanPopView.findViewById(R.id.loan_pop_root);
        loan_content_layout = (LinearLayout) loanPopView.findViewById(R.id.content_layout);
        loan_pop_root.setBackgroundDrawable(new BitmapDrawable(bitmap));
        loan_pop_root.setOnTouchListener(this);
        loanPop.setOutsideTouchable(true);
        loanPop.setBackgroundDrawable(new BitmapDrawable());

        LinearLayout to_Loan = (LinearLayout) loanPopView.findViewById(R.id.to_Loan);
        to_Loan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToast("跳转计算器");
            }
        });
    }

    /**
     * 获取PopipWinsow实例
     */
    private void getTaxesPopupWindow() {
        if (null != taxesPop) {
            if (taxesPop.isShowing()) {
                taxesPop.dismiss();
            }
            return;
        } else {
            initTaxesPop();
        }
    }

    private void getLoanPopupWindow() {
        if (null != loanPop) {
            if (loanPop.isShowing()) {
                loanPop.dismiss();
            }
            return;
        } else {
            initLoanPop();
        }
    }

    /**
     * 获取和保存当前屏幕的截图
     */
    private Bitmap getFastBlurCurrentImage() {
        // View是你需要截图的View
        View view = SecondHandHousingTaxesActivity.this.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap b1 = view.getDrawingCache();

        // 获取状态栏高度
        Rect frame = new Rect();
        SecondHandHousingTaxesActivity.this.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;
        System.out.println(statusBarHeight);

        // 获取屏幕长和高
        int width = SecondHandHousingTaxesActivity.this.getWindowManager().getDefaultDisplay().getWidth();
        int height = SecondHandHousingTaxesActivity.this.getWindowManager().getDefaultDisplay()
                .getHeight();
        // 去掉标题栏
        // Bitmap b = Bitmap.createBitmap(b1, 0, 25, 320, 455);
        Bitmap b = Bitmap.createBitmap(b1, 0, statusBarHeight, width, height
                - statusBarHeight);
        view.destroyDrawingCache();
        b = fastBlur(b, 8);
        if (b != null) {
            return b;
        } else {
            throw new NullPointerException("--->fastblur(Bmp, 30) 为空");
        }
    }


    /*
    高斯模糊处理
    * */
    public static Bitmap fastBlur(Bitmap sbitmap, float radiusf) {
        Bitmap bitmap = Bitmap.createScaledBitmap(sbitmap, sbitmap.getWidth() / 8, sbitmap.getHeight() / 8, false);//先缩放图片，增加模糊速度
        int radius = (int) radiusf;
        if (radius < 1) {
            return (null);
        }

        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        int[] pix = new int[w * h];
        bitmap.getPixels(pix, 0, w, 0, 0, w, h);

        int wm = w - 1;
        int hm = h - 1;
        int wh = w * h;
        int div = radius + radius + 1;

        int r[] = new int[wh];
        int g[] = new int[wh];
        int b[] = new int[wh];
        int rsum, gsum, bsum, x, y, i, p, yp, yi, yw;
        int vmin[] = new int[Math.max(w, h)];

        int divsum = (div + 1) >> 1;
        divsum *= divsum;
        int dv[] = new int[256 * divsum];
        for (i = 0; i < 256 * divsum; i++) {
            dv[i] = (i / divsum);
        }

        yw = yi = 0;

        int[][] stack = new int[div][3];
        int stackpointer;
        int stackstart;
        int[] sir;
        int rbs;
        int r1 = radius + 1;
        int routsum, goutsum, boutsum;
        int rinsum, ginsum, binsum;

        for (y = 0; y < h; y++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            for (i = -radius; i <= radius; i++) {
                p = pix[yi + Math.min(wm, Math.max(i, 0))];
                sir = stack[i + radius];
                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);
                rbs = r1 - Math.abs(i);
                rsum += sir[0] * rbs;
                gsum += sir[1] * rbs;
                bsum += sir[2] * rbs;
                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }
            }
            stackpointer = radius;

            for (x = 0; x < w; x++) {

                r[yi] = dv[rsum];
                g[yi] = dv[gsum];
                b[yi] = dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (y == 0) {
                    vmin[x] = Math.min(x + radius + 1, wm);
                }
                p = pix[yw + vmin[x]];

                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[(stackpointer) % div];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi++;
            }
            yw += w;
        }
        for (x = 0; x < w; x++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            yp = -radius * w;
            for (i = -radius; i <= radius; i++) {
                yi = Math.max(0, yp) + x;

                sir = stack[i + radius];

                sir[0] = r[yi];
                sir[1] = g[yi];
                sir[2] = b[yi];

                rbs = r1 - Math.abs(i);

                rsum += r[yi] * rbs;
                gsum += g[yi] * rbs;
                bsum += b[yi] * rbs;

                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }

                if (i < hm) {
                    yp += w;
                }
            }
            yi = x;
            stackpointer = radius;
            for (y = 0; y < h; y++) {
                // Preserve alpha channel: ( 0xff000000 & pix[yi] )
                pix[yi] = (0xff000000 & pix[yi]) | (dv[rsum] << 16) | (dv[gsum] << 8) | dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (x == 0) {
                    vmin[y] = Math.min(y + r1, hm) * w;
                }
                p = x + vmin[y];

                sir[0] = r[p];
                sir[1] = g[p];
                sir[2] = b[p];

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[stackpointer];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi += w;
            }
        }
        bitmap.setPixels(pix, 0, w, 0, 0, w, h);
        return (bitmap);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bitmap = null;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touchY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:

                if (touchY < taxes_content_layout.getTop() || touchY > taxes_content_layout.getBottom()) {
                    if (v.getId() == R.id.taxes_pop_root) {
                        taxesPop.dismiss();
                    } else if (v.getId() == R.id.loan_pop_root) {
                        loanPop.dismiss();
                    }
                }
                break;
            default:
                break;
        }
        return true;
    }
}
