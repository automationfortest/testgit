package slzjandroid.slzjapplication.activity;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.FragmentActivity;
import android.view.WindowManager;

import com.umeng.analytics.MobclickAgent;

import slzjandroid.slzjapplication.utils.CommonUtils;

public abstract class BasicActivity extends FragmentActivity {

    public Bundle bundle;

    public Bundle getBundle() {
        return bundle;
    }

    public void setBundle(Bundle bundle) {
        this.bundle = bundle;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // CommonUtils.disInputMethod(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏
            // getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        setBundle(savedInstanceState);
        setContentView(getLayoutID());
        findViews();
        init();
        bindViews();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }


    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }

    protected abstract int getLayoutID();

    protected abstract void findViews();

    protected abstract void init();

    protected abstract void bindViews();

    protected String getDisplayTitle() {
        return "";
    }

    @Override
    protected void onResume() {
        super.onResume();
        umengCount(true);
    }

    @Override
    protected void onPause() {
        super.onPause();
        umengCount(false);
    }


    public void umengCount(boolean tag) {
        String displayTitle = getDisplayTitle();

        if (CommonUtils.hasText(displayTitle)) {
            if (tag) {
                MobclickAgent.onPageStart(displayTitle);
            } else {
                MobclickAgent.onPageEnd(displayTitle);
            }
        } else {
            if (tag) {
                MobclickAgent.onPageStart(getClass().getSimpleName());
            } else {
                MobclickAgent.onPageEnd(getClass().getSimpleName());
            }
        }

        MobclickAgent.onResume(this);
    }

    @Override
    public void finish() {
        super.finish();
    }
}
