package imageloadinganim.pro.wjj.imageloadinganim;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Ezreal on 2015/12/23.
 */
public abstract class Father extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayout());
        init();
        setClick();
        Logic();
    }

    abstract public int getLayout();
    abstract public void init();
    abstract public void setClick();
    abstract public void Logic();
}
