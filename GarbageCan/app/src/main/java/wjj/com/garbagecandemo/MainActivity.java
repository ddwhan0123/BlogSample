package wjj.com.garbagecandemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.elvishew.xlog.LogLevel;
import com.elvishew.xlog.XLog;

import wjj.com.garbagecandemo.customview.GabageCan;

public class MainActivity extends AppCompatActivity {
    GabageCan gabageCan;
    Button button;
    TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        XLog.init(BuildConfig.DEBUG ? LogLevel.ALL : LogLevel.NONE);
        gabageCan = (GabageCan) findViewById(R.id.view);
        text = (TextView) findViewById(R.id.text);
        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gabageCan.startAnimator();
                XLog.d("--->OnClickListener");
                gabageCan.throwAnim(text);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        XLog.d("--->onResume");
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        XLog.d("--->onWindowFocusChanged hasFocus " + hasFocus);
        XLog.d("--->gabageCan.getCenterPoint x:" + gabageCan.getCenterPoint()[0] + " y: " + gabageCan.getCenterPoint()[1]);
    }

    @Override
    protected void onPause() {
        super.onPause();
        XLog.d("--->onPause");
    }
}
