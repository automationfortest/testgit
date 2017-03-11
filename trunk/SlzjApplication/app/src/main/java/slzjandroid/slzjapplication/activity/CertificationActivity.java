package slzjandroid.slzjapplication.activity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import slzjandroid.slzjapplication.R;
import slzjandroid.slzjapplication.context.AppContext;
import slzjandroid.slzjapplication.customView.NavigationView;
import slzjandroid.slzjapplication.customView.YesOrNoDialog;
import slzjandroid.slzjapplication.dto.EnterpriseInfo;
import slzjandroid.slzjapplication.dto.LoginUser;
import slzjandroid.slzjapplication.fragment.EnterpriseFragment;
import slzjandroid.slzjapplication.fragment.OtherFragment;
import slzjandroid.slzjapplication.helper.SPLoginUser;
import slzjandroid.slzjapplication.utils.CommonUtils;

/**
 * 实名认证
 */
public class CertificationActivity extends BasicActivity implements View.OnClickListener, NavigationView.ClickCallback {

    private FrameLayout frameLayout;
    private EnterpriseFragment enterpriseFragment;
    private OtherFragment otherFragment;

    private ImageView imv_orderlist_myorder, imv_orderlist_comorder;
    private TextView tv_orderlist_comorder, tv_orderlist_myorder;
    private RelativeLayout rly_orderlist_myorder, rly_orderlist_comorder;

    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private EnterpriseInfo enterpriseInfo;


    @Override
    protected int getLayoutID() {
        return R.layout.activity_certification;
    }

    @Override
    protected void findViews() {
        frameLayout = (FrameLayout) findViewById(R.id.context_framelayout);
        rly_orderlist_myorder = (RelativeLayout) findViewById(R.id.rly_orderlist_myorder);
        rly_orderlist_comorder = (RelativeLayout) findViewById(R.id.rly_orderlist_comorder);

        imv_orderlist_myorder = (ImageView) findViewById(R.id.imv_orderlist_myorder);
        imv_orderlist_comorder = (ImageView) findViewById(R.id.imv_orderlist_comorder);

        tv_orderlist_myorder = (TextView) findViewById(R.id.tv_orderlist_myorder);
        tv_orderlist_comorder = (TextView) findViewById(R.id.tv_orderlist_comorder);
        if (null == enterpriseFragment) {
            enterpriseFragment = new EnterpriseFragment();
        }
        if (null == otherFragment) {
            otherFragment = new OtherFragment();
        }
    }

    @Override
    protected void init() {
        AppContext.getInstance().addActivity(this);
        NavigationView navigationView = (NavigationView) super.findViewById(R.id.nav_main);
        navigationView.setTitle("实名认证");
        navigationView.setRightViewIsShow(false);
        navigationView.setClickCallback(this);


        LoginUser user = LoginUser.getUser();
        enterpriseInfo = user.getEnterpriseInfo();

        if (enterpriseInfo.getEnterpriseAuthFlag().equals("2")) {
            showDialog("您上次于" + (CommonUtils.hasText(enterpriseInfo.getAuthDate())
                    ? enterpriseInfo.getAuthDate() : "时间未知") + "提交的材料，因" +
                    (CommonUtils.hasText(enterpriseInfo.getResaon()) ? enterpriseInfo.getResaon() : "未知")
                    + "原因未能通过实名认证,请您调整后再次提交，带来不便敬请谅解");
        }

        fragmentManager = getFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        tableSwitch(true);
    }

    @Override
    protected void bindViews() {
        rly_orderlist_myorder.setOnClickListener(this);
        rly_orderlist_comorder.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rly_orderlist_myorder:
                tableSwitch(true);
                break;
            case R.id.rly_orderlist_comorder:
                tableSwitch(false);
                break;
            default:
                break;
        }
    }

    private void tableSwitch(boolean tag) {
        if (tag) {
            imv_orderlist_comorder.setVisibility(View.GONE);
            imv_orderlist_myorder.setVisibility(View.VISIBLE);
            tv_orderlist_myorder.setTextColor(getResources().getColor(R.color.title_bg));
            tv_orderlist_comorder.setTextColor(getResources().getColor(R.color.edt_gray_font));
            if (enterpriseFragment != null) {
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.context_framelayout, enterpriseFragment);
                fragmentTransaction.commit();
            }
        } else {
            imv_orderlist_myorder.setVisibility(View.GONE);
            imv_orderlist_comorder.setVisibility(View.VISIBLE);
            tv_orderlist_myorder.setTextColor(getResources().getColor(R.color.edt_gray_font));
            tv_orderlist_comorder.setTextColor(getResources().getColor(R.color.title_bg));
            if (otherFragment != null) {
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.context_framelayout, otherFragment);
                fragmentTransaction.commit();
            }
        }
    }

    @Override
    public void onBackClick() {
        SPLoginUser.clearPicUri(this, "yingye");
        SPLoginUser.clearPicUri(this, "other");
        finish();
    }

    @Override
    public void onRightClick() {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        clearData();
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        clearData();

    }

    private void clearData() {
        SPLoginUser.clearPicUri(this, "yingye");
        SPLoginUser.clearPicUri(this, "other");
    }

    private void showDialog(String message) {
        final YesOrNoDialog yesOrNoDialog = new YesOrNoDialog();
        Bundle bundle = new Bundle();
        bundle.putString(YesOrNoDialog.TITLE, "尊敬的" + enterpriseInfo.getEnterpriseName() + "公司");
        bundle.putString(YesOrNoDialog.MESSAGE, message);
        bundle.putString(yesOrNoDialog.STRPositive, "重新认证");
        yesOrNoDialog.setArguments(bundle);
        yesOrNoDialog.setOnClick(new YesOrNoDialog.OnClick() {
                                     @Override
                                     public void onPositive() {
                                         yesOrNoDialog.dismiss();
                                     }
                                 }

        );
        yesOrNoDialog.setCancelable(false);
        yesOrNoDialog.show(this.getSupportFragmentManager(), "yesOrNoDialog");
    }
}
