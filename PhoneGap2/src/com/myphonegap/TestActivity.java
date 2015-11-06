package com.myphonegap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class TestActivity extends Activity {
	private EditText edittext;
	private Button button;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		edittext = (EditText) findViewById(R.id.EditText1);
		button = (Button) findViewById(R.id.Button1);
		// 接收html页面参数
		String str = getIntent().getStringExtra("name");
		String str1 = getIntent().getStringExtra("pass");
		edittext.setText("name:" + str + "  " + "pass:  " + str1);
		button.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(TestActivity.this,
						HomeActivity.class);
				startActivity(intent);
			}
		});
	}
}
