package stepperindicator.com.stepperindicatordemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    int position = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final MyStepperIndicator mySI = (MyStepperIndicator) findViewById(R.id.mySI);
        Button button = (Button) findViewById(R.id.button);
        assert button != null;
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mySI.setCurrentStep(position);
                Log.d("--->position", "现在的position= " + position);

                if (position < mySI.getStepCount()) {
                    position++;
                } else {
                    position = 0;
                }
            }
        });
    }

}
