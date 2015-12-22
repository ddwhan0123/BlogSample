package popupwindowdemo.pro.wjj.popupwindowdemo.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.apkfuns.logutils.LogUtils;

import popupwindowdemo.pro.wjj.popupwindowdemo.R;

public class HomeActivity extends Root implements View.OnClickListener, View.OnTouchListener {
    private Button showPopup, clearPopup, makeBgGray, oneBtn, twoBtn, threeBtn, fourBtn, fiveBtn, sixBtn, ninePlusBtn, eightBtn, nineBtn, sevenBtn;
    private LinearLayout oneLayout, twoLayout;
    private View popView;
    private PopupWindow popupWindow;
    private RelativeLayout rootLayout;
    private boolean rootLayoutFlag = false;
    static final float moveY=100f;

    //记录Y坐标
    private float y;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int getLayout() {
        LogUtils.d("---> HomeActivity getLayout");
        return R.layout.activity_root;
    }

    @Override
    public void init() {
        rootLayout = (RelativeLayout) findViewById(R.id.rootLayout);
        showPopup = (Button) findViewById(R.id.showPopup);
        clearPopup = (Button) findViewById(R.id.clearPopup);
        makeBgGray = (Button) findViewById(R.id.makeBgGray);
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        popView = layoutInflater.inflate(R.layout.popuwindow_item, null);
        popupWindow = new PopupWindow(popView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, false);
        oneLayout = (LinearLayout) popView.findViewById(R.id.oneLayout);
        twoLayout = (LinearLayout) popView.findViewById(R.id.twoLayout);
        //把那一堆丢下面，看了眼瞎
        findButton();
        LogUtils.d("---> HomeActivity init");
    }

    @Override
    public void seClick() {
        rootLayout.setOnTouchListener(this);
        clearPopup.setOnClickListener(this);
        showPopup.setOnClickListener(this);
        makeBgGray.setOnClickListener(this);
        //跟findID一样眼瞎的 丢下面
        setButtonclick();
        LogUtils.d("---> HomeActivity seClick");
    }

    @Override
    public void logic() {
        LogUtils.d("---> HomeActivity logic");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.showPopup:
                LogUtils.d("--->HomeActivity onClick R.id.showPopup");
                popupWindow.showAtLocation(v, Gravity.BOTTOM, 0, 0);
                break;
            case R.id.clearPopup:
                LogUtils.d("--->HomeActivity onClick R.id.clearPopup");
                dissmissPop();
                break;
            case R.id.makeBgGray:
                //模拟按了变灰，让用户认为 只可以按POP的内容（如果只能接受按pop才有事件，那么把跟目录的事件触发为false即可）
                rootLayout.setBackgroundColor(getResources().getColor(R.color.Gray));
                popupWindow.showAtLocation(v, Gravity.BOTTOM, 0, 0);
                rootLayoutFlag = true;
                LogUtils.d("--->HomeActivity onClick R.id.makeBgGray");
                break;
            case R.id.oneBtn:
                Toast.makeText(HomeActivity.this,"点击了 1",Toast.LENGTH_SHORT).show();
                LogUtils.d("--->HomeActivity onClick R.id.oneBtn");
                break;
            case R.id.twoBtn:
                LogUtils.d("--->HomeActivity onClick R.id.twoBtn");
                break;
            case R.id.threeBtn:
                LogUtils.d("--->HomeActivity onClick R.id.threeBtn");
                break;
            case R.id.fourBtn:
                Toast.makeText(HomeActivity.this,"点击了 4",Toast.LENGTH_SHORT).show();
                LogUtils.d("--->HomeActivity onClick R.id.fourBtn");
                break;
            case R.id.fiveBtn:
                LogUtils.d("--->HomeActivity onClick R.id.fiveBtn");
                break;
            case R.id.sixBtn:
                LogUtils.d("--->HomeActivity onClick R.id.sixBtn");
                break;
            case R.id.sevenBtn:
                Toast.makeText(HomeActivity.this,"点击了 7",Toast.LENGTH_SHORT).show();
                LogUtils.d("--->HomeActivity onClick R.id.sevenBtn");
                break;
            case R.id.eightBtn:
                LogUtils.d("--->HomeActivity onClick R.id.eightBtn");
                break;
            case R.id.nineBtn:
                LogUtils.d("--->HomeActivity onClick R.id.nineBtn");
                break;
            case R.id.ninePlusBtn:
                Toast.makeText(HomeActivity.this,"点击了 9P",Toast.LENGTH_SHORT).show();
                LogUtils.d("--->HomeActivity onClick R.id.ninePlusBtn");
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_MENU:// 菜单键监听
                popupWindow.dismiss();
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void findButton() {
        oneBtn = (Button) oneLayout.findViewById(R.id.oneBtn);
        twoBtn = (Button) oneLayout.findViewById(R.id.twoBtn);
        threeBtn = (Button) oneLayout.findViewById(R.id.threeBtn);
        fourBtn = (Button) oneLayout.findViewById(R.id.fourBtn);
        fiveBtn = (Button) oneLayout.findViewById(R.id.fiveBtn);
        sixBtn = (Button) twoLayout.findViewById(R.id.sixBtn);
        sevenBtn = (Button) twoLayout.findViewById(R.id.sevenBtn);
        eightBtn = (Button) twoLayout.findViewById(R.id.eightBtn);
        nineBtn = (Button) twoLayout.findViewById(R.id.nineBtn);
        ninePlusBtn = (Button) twoLayout.findViewById(R.id.ninePlusBtn);
    }

    private void setButtonclick() {
        oneBtn.setOnClickListener(this);
        twoBtn.setOnClickListener(this);
        threeBtn.setOnClickListener(this);
        fourBtn.setOnClickListener(this);
        fiveBtn.setOnClickListener(this);
        sixBtn.setOnClickListener(this);
        sevenBtn.setOnClickListener(this);
        eightBtn.setOnClickListener(this);
        ninePlusBtn.setOnClickListener(this);
        nineBtn.setOnClickListener(this);
    }

    private void dissmissPop(){
        popupWindow.dismiss();
        if (rootLayoutFlag) {
            rootLayout.setBackgroundColor(getResources().getColor(R.color.White));
            rootLayoutFlag = false;
        }
    }

    //滑动隐藏实现
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                y=event.getY();
                LogUtils.d("--->HomeActivity onTouch  y=event.getY() "+y);
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                float tempY=event.getY();
                LogUtils.d("--->HomeActivity onTouch  tempY=event.getY() "+tempY);
                //从上往下滑
                if(y-tempY<moveY){
                    dissmissPop();
                    //从下往上滑
                }else if(y-tempY>moveY){
                    popupWindow.showAtLocation(v, Gravity.BOTTOM, 0, 0);
                }
                break;
        }
        return true;
    }
}
