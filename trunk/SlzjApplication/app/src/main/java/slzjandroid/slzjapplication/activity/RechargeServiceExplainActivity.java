package slzjandroid.slzjapplication.activity;

import android.graphics.Color;
import android.webkit.WebSettings;
import android.webkit.WebView;

import slzjandroid.slzjapplication.R;
import slzjandroid.slzjapplication.context.AppContext;
import slzjandroid.slzjapplication.customView.NavigationView;

/**
 * Created by xuyifei on 16/4/22.
 */
public class RechargeServiceExplainActivity extends BasicActivity implements NavigationView.ClickCallback {

    private WebView wv_service_explain;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_service_agreement;
    }

    @Override
    protected void findViews() {
        wv_service_explain = (WebView) findViewById(R.id.wv_service_explain);

    }

    @Override
    protected void init() {
        NavigationView navigationView = (NavigationView) super.findViewById(R.id.nav_main);
        navigationView.setTitle("钱包行云在线充值服务协议");
        navigationView.setRightViewIsShow(false);
        navigationView.setClickCallback(this);
        AppContext.getInstance().addActivity(this);

    }

    @Override
    protected void bindViews() {
        WebSettings webSettings = wv_service_explain.getSettings();
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);
        wv_service_explain.setBackgroundColor(Color.TRANSPARENT);
        String url = "file:///android_asset/recharge_service_explain_info.html";
        wv_service_explain.loadUrl(url);

    }

    @Override
    public void onBackClick() {
        finish();

    }

    @Override
    public void onRightClick() {

    }
}
