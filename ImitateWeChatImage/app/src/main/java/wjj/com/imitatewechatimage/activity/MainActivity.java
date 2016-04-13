package wjj.com.imitatewechatimage.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import wjj.com.imitatewechatimage.R;

import com.apkfuns.logutils.LogUtils;
import com.bumptech.glide.Glide;

import wjj.com.imitatewechatimage.Config;
import wjj.com.imitatewechatimage.obj.ImageInfoObj;
import wjj.com.imitatewechatimage.obj.ImageWidgetInfoObj;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView imageView;
    private ImageInfoObj imageInfoObj;
    private ImageWidgetInfoObj imageWidgetInfoObj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findId();
        init();
        Listener();
    }

    private void findId() {
        imageView = (ImageView) findViewById(R.id.imageView);
    }

    private void init() {
        Glide.with(MainActivity.this).load(Config.IMAGE_URL).placeholder(R.mipmap.maimai).into(imageView);

        imageInfoObj = new ImageInfoObj();
        imageInfoObj.imageUrl = Config.IMAGE_URL;
        imageInfoObj.imageWidth = 1280;
        imageInfoObj.imageHeight = 720;

        imageWidgetInfoObj = new ImageWidgetInfoObj();
        imageWidgetInfoObj.x = imageView.getLeft();
        imageWidgetInfoObj.y = imageView.getTop();
        imageWidgetInfoObj.width = imageView.getLayoutParams().width;
        imageWidgetInfoObj.height = imageView.getLayoutParams().height;

    }

    private void Listener() {
        imageView.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        LogUtils.d("--->MainActivity onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        LogUtils.d("--->MainActivity onPause");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogUtils.d("--->MainActivity onDestroy");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageView:
                Intent intent = new Intent(MainActivity.this, howImageActivity.class);
                intent.putExtra("imageInfoObj", imageInfoObj);
                intent.putExtra("imageWidgetInfoObj", imageWidgetInfoObj);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}
