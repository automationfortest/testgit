package slzjandroid.slzjapplication.activity;

import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.webkit.WebSettings;
import android.webkit.WebView;

import slzjandroid.slzjapplication.R;
import slzjandroid.slzjapplication.context.AppContext;
import slzjandroid.slzjapplication.customView.NavigationView;
import slzjandroid.slzjapplication.utils.CommonUtils;

public class DocumentAtivity extends BasicActivity implements NavigationView.ClickCallback {
    private WebView wv_service_explain;
    private String url;

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
        AppContext.getInstance().addActivity(this);
        NavigationView navigationView = (NavigationView) super.findViewById(R.id.nav_main);
        navigationView.setTitle(getTitleStr());
        navigationView.setRightViewIsShow(false);
        navigationView.setClickCallback(this);


    }

    private String getTitleStr() {
        String title = new String();
        Intent intent = getIntent();
        int pageNum = intent.getIntExtra("pageNum", -1);
        switch (pageNum) {
            case 1:
                title = "钱包行云服务协议";
                url = "serviceexplaininfo";
                break;
            case 2:
                title = "关于我们";
                url = "about_info";

                break;
            case 3:
                title = "使用规则";
                url = "coupon_rull";
                break;
            case 4:
                title = "钱包行云服务协议";
                url = "serviceexplaininfo";
                break;
            case 5:
                title = "帮助中心";
                url = "help";
                break;
            case 6:
                title = "钱包行云在线充值服务协议";
                url = "recharge_service_explain_info";
                break;

        }

        loadHtmlFromAssets(url);
        return title;
    }

    private void loadHtmlFromAssets(String url) {
        if (TextUtils.isEmpty(url)) {
            return;
        }

        String html = CommonUtils.readAssest(this, "html/" + url + ".html");
        html = html.replace("@fontName0", "Padauk-bookbold");
        html = html.replace("@fontPath0", "../font/Padauk-bookbold.ttf");// assets相对路径
        String baseurl = "file:///android_asset/html/";
        wv_service_explain.loadDataWithBaseURL(baseurl, html, "text/html", "UTF-8", null);

    }

    @Override
    protected void bindViews() {
        WebSettings settings = wv_service_explain.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setSupportZoom(false);// 允许放大缩小
        settings.setBuiltInZoomControls(false);// 显示放大缩小按钮
        settings.setDisplayZoomControls(false);// api 11以上
        settings.setSupportMultipleWindows(true);
        settings.setGeolocationEnabled(true);// 启用地理定位

        wv_service_explain.setHorizontalScrollBarEnabled(false);

        wv_service_explain.setBackgroundColor(Color.TRANSPARENT); // 设置背景色
    }


    @Override
    public void onBackClick() {
        finish();

    }

    @Override
    public void onRightClick() {

    }


}
