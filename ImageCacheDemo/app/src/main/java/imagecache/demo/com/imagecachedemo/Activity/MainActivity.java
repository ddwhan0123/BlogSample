package imagecache.demo.com.imagecachedemo.Activity;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.apkfuns.logutils.LogUtils;

import java.io.File;

import imagecache.demo.com.imagecachedemo.R;
import imagecache.demo.com.imagecachedemo.Root;

public class MainActivity extends Root {
    static String TAG = MainActivity.class.getName();
    ImageView imageView;
    public static final String TAG_CACHE                = "image_cache";
    /** cache folder path which be used when saving images **/
    public static final String DEFAULT_CACHE_FOLDER     = new StringBuilder()
            .append(Environment.getExternalStorageDirectory()
                    .getAbsolutePath()).append(File.separator)
            .append("wjj").append(File.separator)
            .append("AndroidDemo").append(File.separator)
            .append("ImageCache").toString();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView=(ImageView)findViewById(R.id.imageView);
        IMAGE_CACHE.initData(this, TAG_CACHE);
        IMAGE_CACHE.setContext(MainActivity.this);
        IMAGE_CACHE.setCacheFolder(DEFAULT_CACHE_FOLDER);
        IMAGE_CACHE.get("http://e.hiphotos.baidu.com/baike/c0%3Dbaike80%2C5%2C5%2C80%2C26/sign=12b7a0ba0a33874488c8272e3066b29c/a9d3fd1f4134970ad4ddeec593cad1c8a6865daa.jpg", imageView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        LogUtils.d(TAG, "--->onResume");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogUtils.d("---->MainActivity onDestroy stopNetworkService");
        stopNetworkService();
    }

}
