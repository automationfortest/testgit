package slzjandroid.slzjapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import slzjandroid.slzjapplication.R;
import slzjandroid.slzjapplication.activity.ICallback.ICallBack;
import slzjandroid.slzjapplication.activity.ICallbackService.CouponModel;
import slzjandroid.slzjapplication.adapter.CouponAdapter;
import slzjandroid.slzjapplication.context.AppContext;
import slzjandroid.slzjapplication.customView.LoadingDialog;
import slzjandroid.slzjapplication.customView.NavigationView;
import slzjandroid.slzjapplication.dto.CouponRequest;
import slzjandroid.slzjapplication.utils.DialogUtil;

/**
 * 优惠券页面
 * create xuyifei
 */
public class CouponActivity extends BasicActivity implements NavigationView.ClickCallback, View.OnClickListener {

    private CouponAdapter couponAdapter;

    private ListView lv_coupon;

    private TextView tv_use_info;
    private TextView tv_look_verdue_info;

    private boolean flag = false;
    private CouponModel couponModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_coupon;
    }

    @Override
    protected void findViews() {
        lv_coupon = (ListView) findViewById(R.id.lv_coupon);
        tv_use_info = (TextView) findViewById(R.id.tv_use_info);
        tv_look_verdue_info = (TextView) findViewById(R.id.tv_look_verdue_info);

    }


    @Override
    protected void init() {
        AppContext.getInstance().addActivity(this);
        NavigationView navigationView = (NavigationView) super.findViewById(R.id.nav_main);
        navigationView.getBackView().setImageResource(R.drawable.selector_home_bg);
        navigationView.setRightViewIsShow(false);
        navigationView.setTitle("优惠券");
        navigationView.setClickCallback(this);

        LoadingDialog loadingDialog = DialogUtil.getLoadingDialog(this, "正在获取优惠券...");

        couponModel = new CouponModel(loadingDialog);


    }

    @Override
    protected void bindViews() {
        couponAdapter = new CouponAdapter(this);
        lv_coupon.setAdapter(couponAdapter);
        getCouponData("0");
        tv_use_info.setOnClickListener(this);
        tv_look_verdue_info.setOnClickListener(this);

    }

    @Override
    public void onBackClick() {
        finish();
    }

    @Override
    public void onRightClick() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_use_info:
                Intent intent = new Intent(CouponActivity.this, DocumentAtivity.class);
                intent.putExtra("pageNum",3);
                startActivity(intent);
                break;

            case R.id.tv_look_verdue_info:
                if (!flag) {
                    getCouponData("1");
                    flag = true;
                    tv_look_verdue_info.setText("查看未过期券");
                } else {
                    getCouponData("0");
                    flag = false;
                    tv_look_verdue_info.setText("查看过期券");
                }
                break;

        }
    }


    public void getCouponData(final String tag) {
        couponModel.getCouponData(tag, new ICallBack<CouponRequest>() {

            @Override
            public void success(CouponRequest couponRequest) {
                couponAdapter.setTag(tag);
                couponAdapter.setData(couponRequest.getResult().getCouponInfo());
            }

            @Override
            public void fail(String info) {
                couponAdapter.clearData();

            }

            @Override
            public void error(Throwable throwable) {
                couponAdapter.clearData();
            }
        });

    }
}
