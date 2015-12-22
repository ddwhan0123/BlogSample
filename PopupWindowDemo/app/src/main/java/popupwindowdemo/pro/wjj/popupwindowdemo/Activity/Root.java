package popupwindowdemo.pro.wjj.popupwindowdemo.Activity;

import android.app.Activity;
import android.os.Bundle;

public abstract class Root extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayout());
        init();
        seClick();
        logic();
    }

    abstract public int getLayout();

    abstract public void init();

    abstract public void seClick();

    abstract public void logic();
}
