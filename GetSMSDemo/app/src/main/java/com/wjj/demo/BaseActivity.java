package com.wjj.demo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by jiajiewang on 16/6/30.
 */
public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayout());
        init();
        logic();
    }

    abstract int getLayout();

    abstract void init();

    abstract void logic();

    @Override
    protected void onResume() {
        super.onResume();
        onResumeInit();
        onResumeLogic();
    }

    abstract void onResumeInit();

    abstract void onResumeLogic();
}
