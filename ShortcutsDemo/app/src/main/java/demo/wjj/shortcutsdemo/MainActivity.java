package demo.wjj.shortcutsdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {

    private final static String ONE = "one";
    private final static String TWO = "two";
    private final static String THREE = "three";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView textView = (TextView) findViewById(R.id.textview);

        switch (getIntent().getAction()){
            case ONE:
                textView.setText("我是第一个");
                break;
            case TWO:
                textView.setText("我是第二个");
                break;
            case THREE:
                textView.setText("我是第三个");
                break;
            default:
                break;
        }
    }
}
