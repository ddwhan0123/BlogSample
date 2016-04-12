package wjj.com.viewpagerwithglide.holder;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.apkfuns.logutils.LogUtils;
import com.bumptech.glide.Glide;

import wjj.com.viewpagerwithglide.R;
import wjj.com.viewpagerwithglide.lib.holder.Holder;

/**
 * Created by jiajiewang on 16/4/12.
 */
public class BannerHolder implements Holder<String> {

    private ImageView imageView;

    @Override
    public View createView(Context context) {
        imageView = new ImageView(context);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        return imageView;

    }

    @Override
    public void UpdateUI(Context context, int position, String data) {
//        LogUtils.d("UpdateUI   --->position" + position + " ---> dataURL: " + data);
        Glide.with(context).load(data).placeholder(R.mipmap.ic_default_adimage).into(imageView);
    }
}
