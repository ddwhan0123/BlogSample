package paintcanvasdemo.pro.wjj.paintcanvasdemo;

import android.app.ActionBar;
import android.app.Activity;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class AnimActivity extends Activity implements View.OnClickListener {
    Button startAnim, stopAnim, toppleAnim;
    ActionBar actionBar;
    ImageView animImageView;
    private AnimationDrawable anim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anim);
        Log.d("--->onCreate", "--->AnimActivity onCreate");
        actionBar = getActionBar();

        actionBar.setTitle("AnimActivity");

        startAnim = (Button) findViewById(R.id.startAnim);
        stopAnim = (Button) findViewById(R.id.stopAnim);
        toppleAnim = (Button) findViewById(R.id.toppleAnim);

        animImageView = (ImageView) findViewById(R.id.animImageView);

        stopAnim.setOnClickListener(this);
        startAnim.setOnClickListener(this);
        toppleAnim.setOnClickListener(this);

        anim = (AnimationDrawable) animImageView.getDrawable();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("--->onResume", "onResume");
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        Log.d("-->onWindowFocusChanged", "--->AnimActivity onWindowFocusChanged");
        if (hasFocus) {
            anim.start();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("--->onPause", "onPause");
    }

    @Override
    public void onClick(View v) {
        int flag = v.getId();
        switch (flag) {
            case R.id.startAnim:
                animImageView.setImageResource(R.drawable.positive_anim);
                anim.start();
                break;
            case R.id.toppleAnim:
                animImageView.setImageResource(R.drawable.back_anim);
                anim.start();
                break;
            case R.id.stopAnim:
                anim.stop();
                break;
        }
    }
}
