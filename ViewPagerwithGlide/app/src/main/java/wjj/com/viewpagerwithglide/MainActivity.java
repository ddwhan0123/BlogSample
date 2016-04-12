package wjj.com.viewpagerwithglide;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.apkfuns.logutils.LogUtils;
import com.github.mrengineer13.snackbar.SnackBar;

import java.util.Arrays;

import wjj.com.viewpagerwithglide.holder.BannerHolder;
import wjj.com.viewpagerwithglide.lib.ConvenientBanner;
import wjj.com.viewpagerwithglide.lib.holder.CBViewHolderCreator;
import wjj.com.viewpagerwithglide.lib.listener.OnItemClickListener;

public class MainActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener, OnItemClickListener {
    private ConvenientBanner banner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findID();
        init();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //开始自动翻页
        banner.startTurning(3000);//此值不能小于1200（即ViewPagerScroller的mScrollDuration的值），否则最后一页翻页效果会出问题。如果硬要兼容1200以下，那么请修改ViewPagerScroller的mScrollDuration的值，不过修改后，3d效果就没那么明显了。
        LogUtils.d("--->onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        banner.stopTurning();
        LogUtils.d("--->onPause");
    }

    private void findID() {
        banner = (ConvenientBanner) findViewById(R.id.banner);
    }

    private void init() {
        banner.setPages(new CBViewHolderCreator<BannerHolder>() {
            @Override
            public BannerHolder createHolder() {
                return new BannerHolder();
            }
        }, Arrays.asList(getResources().getStringArray(R.array.imagesArray)))//设置两个点图片作为翻页指示器，不设置则没有指示器，可以根据自己需求自行配合自己的指示器,不需要圆点指示器可用不设
                .setPageIndicator(new int[]{R.mipmap.ic_page_indicator, R.mipmap.ic_page_indicator_focused})
                //设置指示器的方向
                .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.ALIGN_PARENT_RIGHT)
                .setOnItemClickListener(this)//点击监听
                .setOnPageChangeListener(this);//监听翻页事件
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//        LogUtils.d("--->onPageScrolled  " + position);
    }

    @Override
    public void onPageSelected(int position) {
//        LogUtils.d("--->onPageSelected  " + position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
//        LogUtils.d("--->onPageSelected " + state);
    }

    @Override
    public void onItemClick(int position) {
        LogUtils.d("--->点击了第 " + position);
        showToast(position);
    }

    private void showToast(int position) {
        short time = 1500;
        new SnackBar.Builder(this)
                .withMessage("点击了第" + position + "个页面")
                .withActionMessage("确认")
                .withDuration(time)
                .show();
    }
}
