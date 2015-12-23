package imageloadinganim.pro.wjj.imageloadinganim;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.Environment;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.apkfuns.logutils.LogUtils;
import com.squareup.okhttp.Request;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.BitmapCallback;
import com.zhy.http.okhttp.callback.FileCallBack;

import java.io.File;

public class MainActivity extends Father {
    private Button btn;
    private ImageView imageView;
    private Paint paint;
    private Canvas canvas;
    private Bitmap bitmap;
    private String fileUrl;
    private float imageWidth, imageHeight;
    Paint paint2;

    @Override
    public int getLayout() {
        return R.layout.activity_main;
    }

    @Override
    public void init() {
        btn=(Button)findViewById(R.id.btn);
        imageView = (ImageView) findViewById(R.id.imageView);
        imageWidth = (int) getResources().getDimension(R.dimen.image_width);
        imageHeight = (int) getResources().getDimension(R.dimen.image_height);
    }

    @Override
    public void setClick() {
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fileUrl = Environment.getExternalStorageDirectory().getAbsolutePath() + "/wjj/" + "meizi.jpg";

                if (checkFile(fileUrl)) {
                    LogUtils.d("--->onResume checkFile==true");
                    imageView.setImageBitmap(BitmapFactory.decodeFile(fileUrl));
                    Toast.makeText(MainActivity.this,"图片已经存在于SD卡内",Toast.LENGTH_SHORT).show();
                } else {
                    LogUtils.d("--->onResume checkFile==false");
                    DownLoadImage(ConFig.ImageUrl);
                }
            }
        });
    }

    @Override
    public void Logic() {
        LogUtils.d("--->MainActivity Logic getWidth  " + imageWidth + " getHeight " + imageHeight);
        if (bitmap == null) {
            bitmap = Bitmap.createBitmap((int) imageWidth,
                    (int) imageHeight, Bitmap.Config.ARGB_8888);
            canvas = new Canvas(bitmap);
            canvas.drawColor(getResources().getColor(R.color.Gray));
        }
        paint = new Paint();
        paint.setColor(getResources().getColor(R.color.White));
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));

        paint2 = new Paint();

        paint2.setColor(getResources().getColor(R.color.Gray));
        paint2.setTextSize(90);
        paint2.setTextAlign(Paint.Align.CENTER);
        imageView.setImageBitmap(bitmap);
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    private void DownLoadImage(String ImageUrl) {
        OkHttpUtils
                .get()
                .url(ImageUrl)
                .build()
                .execute(new FileCallBack(Environment.getExternalStorageDirectory().getAbsolutePath() + "/wjj/", "meizi.jpg") {

                    @Override
                    public void inProgress(float progress) {
                        LogUtils.d((int) (100 * progress) + "高度等于 " + imageHeight * progress);
                        canvas.drawRect(0, 0, imageWidth, imageHeight * progress, paint);
                        canvas.drawText((int) (100 * progress) + "%", imageWidth / 2, imageHeight / 2 - 200, paint2);

                        imageView.setImageBitmap(bitmap);
                    }

                    @Override
                    public void onError(Request request, Exception e) {
                        LogUtils.e("onError :" + e.getMessage());
                    }

                    @Override
                    public void onResponse(File file) {
                        LogUtils.d("onResponse :" + file.getAbsolutePath());
                        imageView.setImageBitmap(BitmapFactory.decodeFile(file.getAbsolutePath()));
                    }
                });
    }


    private boolean checkFile(String checkFile) {
        File mFile = new File(fileUrl);
        //若该文件存在
        if (mFile.exists()) {
            return true;
        } else {
            return false;
        }
    }
}
