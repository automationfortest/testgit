package slzjandroid.slzjapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.net.ConnectException;
import java.util.HashMap;
import java.util.Map;

import retrofit.RetrofitError;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import slzjandroid.slzjapplication.R;
import slzjandroid.slzjapplication.context.AppContext;
import slzjandroid.slzjapplication.customView.LoadingDialog;
import slzjandroid.slzjapplication.customView.NavigationView;
import slzjandroid.slzjapplication.customView.YesOrNoDialog;
import slzjandroid.slzjapplication.dto.EnterPriseInfoResponse;
import slzjandroid.slzjapplication.dto.EnterPriseInfoResult;
import slzjandroid.slzjapplication.dto.EnterpriseInfo;
import slzjandroid.slzjapplication.dto.LoginUser;
import slzjandroid.slzjapplication.dto.OnlyResponse;
import slzjandroid.slzjapplication.dto.TokenRefresh;
import slzjandroid.slzjapplication.helper.SPLoginUser;
import slzjandroid.slzjapplication.lang.StatusCode;
import slzjandroid.slzjapplication.service.ServiceProvider;
import slzjandroid.slzjapplication.utils.CommonUtils;
import slzjandroid.slzjapplication.utils.DialogUtil;
import slzjandroid.slzjapplication.utils.ToastUtils;

/**
 * Created by hdb on 2016/3/22.
 */
public class MenuMemberInfoActivity extends BasicActivity implements View.OnClickListener, NavigationView.ClickCallback {
    private Button btn_menu_member_info_commit;
    private LinearLayout lly_member_info_waring;
    private TextView tv_member_info_realname_now,
            tv_memberinfo_email, tv_memberinfo_num,
            tv_memberinfo_blance, tv_memberinfo_coupon, tv_tag;
    private EditText edt_memberinfo_com_name;

    private TextView tv_freeze_amonut;

    private TextView tv_account_amount;

    private String accessToken;
    private LoginUser user;

    private EnterpriseInfo data;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_menu_member_info;
    }

    @Override
    protected void findViews() {
        tv_member_info_realname_now = (TextView) findViewById(R.id.tv_member_info_realname_now);

        btn_menu_member_info_commit = (Button) findViewById(R.id.btn_menu_member_info_commit);
        tv_memberinfo_email = (TextView) findViewById(R.id.tv_memberinfo_email);
        tv_memberinfo_num = (TextView) findViewById(R.id.tv_memberinfo_num);
        tv_memberinfo_blance = (TextView) findViewById(R.id.tv_memberinfo_blance);
        tv_memberinfo_coupon = (TextView) findViewById(R.id.tv_memberinfo_coupon);
        edt_memberinfo_com_name = (EditText) findViewById(R.id.edt_memberinfo_com_name);
        lly_member_info_waring = (LinearLayout) findViewById(R.id.lly_member_info_waring);

        tv_tag = (TextView) findViewById(R.id.tv_tag);

        tv_freeze_amonut = (TextView) findViewById(R.id.tv_freeze_amonut);
        tv_account_amount = (TextView) findViewById(R.id.tv_account_amount);
    }

    @Override
    protected void init() {
        AppContext.getInstance().addActivity(this);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_main);
        navigationView.setTitle("企业会员信息");
        navigationView.setRightViewIsShow(false);
        navigationView.setClickCallback(this);

        user = LoginUser.getUser();
        accessToken = user.getAccessToken();
        data = user.getEnterpriseInfo();

        getEnterpriseInfo();

        String flag = data.getEnterpriseAuthFlag();
        if (CommonUtils.hasText(flag)) {
            if (flag.equals("0")) {
                tv_tag.setText("待审核");
            } else if (flag.equals("1")) {
                tv_tag.setText("已通过");
            } else if (flag.equals("2")) {
                tv_tag.setText("已驳回");
            } else {
                tv_tag.setText("未实名");
            }
        }
    }

    @Override
    protected void bindViews() {
        btn_menu_member_info_commit.setOnClickListener(this);
        tv_member_info_realname_now.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        tokenRefresh();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btn_menu_member_info_commit:
                changeEnterpriseName();
                break;
            case R.id.tv_member_info_realname_now:
                String flag = data.getEnterpriseAuthFlag();
                if (CommonUtils.hasText(flag)) {
                    if (flag.equals("2") || flag.equals("3")) {
                        Intent intent = new Intent(MenuMemberInfoActivity.this, CertificationActivity.class);
                        startActivity(intent);
                    } else if (flag.equals("1")) {
                        showDialog("您的公司已经实名");
                    } else if (flag.equals("0")) {
                        showDialog("您的材料已提交，我们正飞速在为您核对信息。三个工作日内将会展示结果！");

                    }
                }
                break;
        }
    }

    private void showDialog(String message) {
        final YesOrNoDialog yesOrNoDialog = new YesOrNoDialog();
        Bundle bundle = new Bundle();
        bundle.putString(YesOrNoDialog.TITLE, "尊敬的" + data.getEnterpriseName());
        bundle.putString(YesOrNoDialog.MESSAGE, message);
        yesOrNoDialog.setArguments(bundle);
        yesOrNoDialog.setOnClick(new YesOrNoDialog.OnClick() {
                                     @Override
                                     public void onPositive() {
                                         yesOrNoDialog.dismiss();
                                     }
                                 }

        );
        yesOrNoDialog.show(this.getSupportFragmentManager(), "yesOrNoDialog");
    }

    private void getEnterpriseInfo() {
        final LoadingDialog dialog = DialogUtil.getLoadingDialog(MenuMemberInfoActivity.this, "正在修改...");
        try {
            ServiceProvider.enterpriseInfoService.getenterpriseInfo(user.getAccessToken())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<EnterPriseInfoResponse>() {
                        @Override
                        public void call(EnterPriseInfoResponse enterPriseInfoResponse) {
                            dialog.dismiss();
                            if (enterPriseInfoResponse.getStatus() == StatusCode.RESPONSE_OK) {
                                EnterPriseInfoResult result = enterPriseInfoResponse.getResult();

                                edt_memberinfo_com_name.setText(result.getEnterpriseName());
                                edt_memberinfo_com_name.setSelection(result.getEnterpriseName().length());
                                tv_memberinfo_email.setText(result.getEnterpriseEmail());
                                tv_memberinfo_num.setText(result.getClientCnt() + "人" + "(" + result.getDeptCnt() + "部门)");
                                tv_memberinfo_blance.setText(result.getBalance() + "元");
                                tv_freeze_amonut.setText(result.getFrozenMoney() + "元");
                                tv_account_amount.setText(result.getAccountBalance() + "元");

                                if (CommonUtils.hasText(result.getCouponAmount())) {
                                    tv_memberinfo_coupon.setText("价值" + result.getCouponAmount() + "元");
                                } else {
                                    tv_memberinfo_coupon.setText("价值0.00元");
                                }


                            } else if (enterPriseInfoResponse.getStatus() == StatusCode.RESPONSE_ERR) {
                                ToastUtils.showToast(MenuMemberInfoActivity.this, "您离开时间太长，需要重新登陆");
                                AppContext.getInstance().finishAllActivity();
                                startActivity(new Intent(MenuMemberInfoActivity.this, LoginActivity.class));
                            } else {
                                ToastUtils.showToast(MenuMemberInfoActivity.this, enterPriseInfoResponse.getMessage());
                            }
                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {
                            throwable.printStackTrace();
                            dialog.dismiss();
                            if (throwable instanceof ConnectException || throwable instanceof RetrofitError) {
                                ToastUtils.showToast(MenuMemberInfoActivity.this, "网络异常，无法连接服务器");
                                return;
                            }
                            ToastUtils.showToast(MenuMemberInfoActivity.this, "获取信息失败");
                        }
                    });
        } catch (Exception e) {
            dialog.dismiss();
            ToastUtils.showToast(MenuMemberInfoActivity.this, "获取信息失败");
        }

    }

    private void changeEnterpriseName() {
        final LoadingDialog dialog = DialogUtil.getLoadingDialog(MenuMemberInfoActivity.this, "正在修改...");
        try {
            Map<String, String> options = new HashMap<>();
            options.put("enterpriseIdx", data.getEnterpriseIdx());
            options.put("enterpriseName", edt_memberinfo_com_name.getText().toString());
            ServiceProvider.enterpriseInfoService.changeEnterpriseName(user.getAccessToken(), options)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<OnlyResponse>() {
                        @Override
                        public void call(OnlyResponse onlyResponse) {
                            dialog.dismiss();
                            if (onlyResponse.getStatus() == StatusCode.RESPONSE_OK) {
                                finish();
                                ToastUtils.showToast(MenuMemberInfoActivity.this, "修改成功");
                            } else if (onlyResponse.getStatus() == StatusCode.RESPONSE_ERR) {
                                ToastUtils.showToast(MenuMemberInfoActivity.this, "您离开时间太长，需要重新登陆");
                                AppContext.getInstance().finishAllActivity();
                                startActivity(new Intent(MenuMemberInfoActivity.this, LoginActivity.class));
                            } else {
                                ToastUtils.showToast(MenuMemberInfoActivity.this, onlyResponse.getMessage());
                            }
                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {
                            throwable.printStackTrace();
                            dialog.dismiss();
                            if (throwable instanceof ConnectException || throwable instanceof RetrofitError) {
                                ToastUtils.showToast(MenuMemberInfoActivity.this, "网络异常，无法连接服务器");
                                return;
                            }
                            ToastUtils.showToast(MenuMemberInfoActivity.this, "修改失败");
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
            dialog.dismiss();
            ToastUtils.showToast(MenuMemberInfoActivity.this, "修改失败");
        }

    }

    @Override
    public void onBackClick() {
        finish();
    }

    @Override
    public void onRightClick() {

    }

    private void tokenRefresh() {
        ServiceProvider.tokenService.tokenRefresh(user.getAccessToken(), new HashMap<String, String>())
                .observeOn(rx.android.schedulers.AndroidSchedulers.mainThread()) // 指定 Subscriber 的回调发生在主线程
                .subscribe(new Action1<TokenRefresh>() {
                    @Override
                    public void call(TokenRefresh tokenRefresh) {
                        String status = tokenRefresh.getStatus();
                        if (status.equals("200")) {
                            TokenRefresh.Result result = tokenRefresh.getResult();
                            EnterpriseInfo enterpriseInfo = result.getEnterpriseInfo();

                            String accessToken = result.getUsrToken().getAccessToken();
                            SPLoginUser.saveTokenTo(MenuMemberInfoActivity.this, accessToken);

                            if (result.getUsrType().equals("1")) {
                                user.setAccessToken(accessToken);
                            } else {
                                user.setBalance(result.getBalance());
                                user.setLoginName(result.getUsrName());
                                user.setAccessToken(accessToken);
                                user.setEnterpriseInfo(LoginUser.getEnterpriseInfo(enterpriseInfo));

                                String flag = enterpriseInfo.getEnterpriseAuthFlag();
                                if (CommonUtils.hasText(flag)) {
                                    if (flag.equals("0")) {
                                        tv_tag.setText("待审核");
                                    } else if (flag.equals("1")) {
                                        tv_tag.setText("已通过");
                                    } else if (flag.equals("2")) {
                                        tv_tag.setText("已驳回");
                                    } else {
                                        tv_tag.setText("未实名");
                                    }
                                }
                            }
                            LoginUser.saveUserToDB(user, MenuMemberInfoActivity.this);
                            user = LoginUser.getUser();
                            data = user.getEnterpriseInfo();
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {

                    }
                });
    }
}
