package com.wjj.demo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.wjj.demo.view.CustomView;

public class MainActivity extends Activity {

    private CustomView mView;
    private Button button, button1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        mView = (CustomView) findViewById(R.id.myView);

        button = (Button) findViewById(R.id.button);
        button1 = (Button) findViewById(R.id.button1);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mView.startAnim(5000);
            }
        });

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mView.stopAnim();
            }
        });
    }

}
