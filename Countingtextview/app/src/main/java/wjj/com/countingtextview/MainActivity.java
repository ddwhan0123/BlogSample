package wjj.com.countingtextview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.LinearInterpolator;

public class MainActivity extends AppCompatActivity {
    private countingTextView countingText2, countingText1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        countingText1 = (countingTextView) findViewById(R.id.countingText1);
        countingText2 = (countingTextView) findViewById(R.id.countingText2);

        countingText1.setInterpolator(new LinearInterpolator());
        countingText1.animateFromZero(60, 60000);
    }
}
