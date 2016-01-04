package paintcanvasdemo.pro.wjj.paintcanvasdemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends Activity {
    ImageView imageView;
    Button button3,toAnim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button toCopy = (Button) findViewById(R.id.toCopy);
        toCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.toCopy) {
                    Intent intent = new Intent(MainActivity.this, CopyActivity.class);
                    startActivity(intent);
                }
            }
        });
        TypedValue tv = new TypedValue();
        if (this.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            int actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, this.getResources().getDisplayMetrics());
            Log.d("---> actionBarHeight ", actionBarHeight + "");
        }

        imageView=(ImageView)findViewById(R.id.imageView);
        button3=(Button)findViewById(R.id.button3);

        toAnim=(Button)findViewById(R.id.toAnim);
        toAnim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.toAnim) {
                    Intent intent = new Intent(MainActivity.this, AnimActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        Log.d("-->image的长，宽", "长 " + imageView.getHeight() + "宽 " + imageView.getWidth());
        Log.d("-->button3的长，宽", "长 " + button3.getHeight() + "宽 " + button3.getWidth());
    }
}
