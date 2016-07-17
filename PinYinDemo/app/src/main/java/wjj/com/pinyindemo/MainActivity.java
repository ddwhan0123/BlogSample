package wjj.com.pinyindemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.github.promeg.pinyinhelper.Pinyin;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView resultTV;
    private EditText inputET;
    private Button submitBtn;

    private String pinYin = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submitBtn:
                pinYin = inputET.getText().toString().trim();
                if (!pinYin.isEmpty()) {
                    resultTV.setText(Pinyin.toPinyin(pinYin.charAt(0)));
                }
                inputET.setText("");
                break;
            default:
                break;
        }
    }

    private void init() {
        resultTV = (TextView) findViewById(R.id.resultTV);
        inputET = (EditText) findViewById(R.id.inputET);
        submitBtn = (Button) findViewById(R.id.submitBtn);
        if (submitBtn != null) {
            submitBtn.setOnClickListener(this);
        }
    }
}
