package paintcanvasdemo.pro.wjj.paintcanvasdemo;


import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import paintcanvasdemo.pro.wjj.paintcanvasdemo.CustomView.CustomView;

public class CopyActivity extends Activity {
    CustomView customView;
    float x, y;
    boolean flag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_copy);
        customView = (CustomView) findViewById(R.id.customView);

        customView.setOnTouchClickListener(new CustomView.OnViewOnTouchListener() {
            @Override
            public void onTouchClick(View view, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // 记录开始触摸的点的坐标
                        x = event.getX();
                        y = event.getY();
                        Log.d("一开始的接触点", "x " + x + "  y  " + y);
                        if (x < (232 + 15) && y > (1920 - 144)) {
                            flag = true;
                        }
                        break;

                    case MotionEvent.ACTION_UP:
                        x = event.getX();
                        y = event.getY();
                        if (flag) {
                            if (x < (232 + 15) && y > (1920 - 144)) {
                                Toast.makeText(CopyActivity.this, "点击了 按钮1", Toast.LENGTH_SHORT).show();
                            }
                        }
                        break;
                }
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
