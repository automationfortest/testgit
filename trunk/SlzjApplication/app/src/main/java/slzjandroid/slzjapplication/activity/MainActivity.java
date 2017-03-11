package slzjandroid.slzjapplication.activity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.HashMap;

import rx.functions.Action1;
import slzjandroid.slzjapplication.R;
import slzjandroid.slzjapplication.context.AppContext;
import slzjandroid.slzjapplication.customView.SlidingMenu;
import slzjandroid.slzjapplication.dto.EnterpriseInfo;
import slzjandroid.slzjapplication.dto.LoginUser;
import slzjandroid.slzjapplication.dto.TokenRefresh;
import slzjandroid.slzjapplication.fragment.ProductFragment;
import slzjandroid.slzjapplication.helper.SPLoginUser;
import slzjandroid.slzjapplication.lang.UsrType;
import slzjandroid.slzjapplication.service.ServiceProvider;
import slzjandroid.slzjapplication.utils.CommonUtils;
import slzjandroid.slzjapplication.utils.ToastUtils;

public class MainActivity extends BasicActivity implements View.OnClickListener {

    private SlidingMenu mMenu;
    private long exitTime = 0;
    private ProductFragment productFragment;
    private RelativeLayout menuRelativelayout, rly_menu_recharge,
            rly_menu_my_order, rly_menu_invoice_management,
            rly_team_managent, rly_menu_coupon, rly_menu_advice, rly_menu_more, rly_menu_member_info;
    private TextView tv_menu_usrname,
            tv_menu_comname, tv_menu_un_realname, tv_tv_menu_momey;
    private LinearLayout lly_menu_member_info;
    private ImageView imv_menu_realname_below, imv_menu_recharge_below, imv_menu_invoice_management_below,
            imv_menu_team_management_below, imv_menu_coupon_below;

    private Handler handler;
    private LoginUser user;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_main;
    }


    @Override
    protected void findViews() {

        mMenu = (SlidingMenu) findViewById(R.id.id_menu);

        menuRelativelayout = (RelativeLayout) mMenu.findViewById(R.id.lay_main_menu);
        tv_menu_usrname = (TextView) menuRelativelayout.findViewById(R.id.tv_menu_usrname);
        tv_menu_comname = (TextView) menuRelativelayout.findViewById(R.id.tv_menu_comname);
        rly_menu_member_info = (RelativeLayout) menuRelativelayout.findViewById(R.id.rly_menu_member_info);
        tv_tv_menu_momey = (TextView) menuRelativelayout.findViewById(R.id.tv_tv_menu_momey);
        rly_menu_recharge = (RelativeLayout) menuRelativelayout.findViewById(R.id.rly_menu_recharge);
        rly_menu_my_order = (RelativeLayout) menuRelativelayout.findViewById(R.id.rly_menu_my_order);
        rly_menu_invoice_management = (RelativeLayout) menuRelativelayout.findViewById(R.id.rly_menu_invoice_management);
        rly_team_managent = (RelativeLayout) menuRelativelayout.findViewById(R.id.rly_team_managent);
        rly_menu_coupon = (RelativeLayout) menuRelativelayout.findViewById(R.id.rly_menu_coupon);
        rly_menu_advice = (RelativeLayout) menuRelativelayout.findViewById(R.id.rly_menu_advice);
        rly_menu_more = (RelativeLayout) menuRelativelayout.findViewById(R.id.rly_menu_more);
        lly_menu_member_info = (LinearLayout) menuRelativelayout.findViewById(R.id.lly_menu_member_info);
        tv_menu_un_realname = (TextView) menuRelativelayout.findViewById(R.id.tv_menu_un_realname);
        //隐藏线
        imv_menu_realname_below = (ImageView) menuRelativelayout.findViewById(R.id.imv_menu_realname_below);
        imv_menu_recharge_below = (ImageView) menuRelativelayout.findViewById(R.id.imv_menu_recharge_below);
        imv_menu_invoice_management_below = (ImageView) menuRelativelayout.findViewById(R.id.imv_menu_invoice_management_below);
        imv_menu_team_management_below = (ImageView) menuRelativelayout.findViewById(R.id.imv_menu_team_management_below);
        imv_menu_coupon_below = (ImageView) menuRelativelayout.findViewById(R.id.imv_menu_coupon_below);

        CommonUtils.hideNavigationBar(rly_team_managent);
    }


    @Override
    protected void init() {
        AppContext.getInstance().addActivity(this);
        if (productFragment == null) {
            setDefaultFragment();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        user = LoginUser.getUser();
        String userType = user.getUserType();
        if (CommonUtils.hasText(userType)) {
            hideView(userType);
        }
        tokenRefresh();
    }

    private void setDefaultFragment() {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        productFragment = ProductFragment.newInstance();
        transaction.replace(R.id.container, productFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    protected void bindViews() {
        rly_menu_member_info.setOnClickListener(this);
        rly_menu_recharge.setOnClickListener(this);
        rly_menu_my_order.setOnClickListener(this);
        rly_team_managent.setOnClickListener(this);
        rly_menu_invoice_management.setOnClickListener(this);
        rly_menu_coupon.setOnClickListener(this);
        rly_menu_advice.setOnClickListener(this);
        rly_menu_more.setOnClickListener(this);
        rly_menu_recharge.setOnClickListener(this);

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

                if (msg.what == 11) {
                    user = LoginUser.getUser();
                    String userType = user.getUserType();
                    hideView(userType);
                }
            }
        };
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    public void toggleMenu() {
        mMenu.toggle();
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.rly_menu_member_info:
                //会员信息
                intent.setClass(MainActivity.this, MenuMemberInfoActivity.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                break;
            case R.id.rly_menu_recharge:
                //立即充值
                intent.setClass(MainActivity.this, RechargeActivity.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                break;
            case R.id.rly_menu_my_order:
                //我的订单，进入订单list
                intent.setClass(MainActivity.this, OrderListActivity.class);
                startActivityForResult(intent, 0);
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                break;
            case R.id.rly_menu_invoice_management:
                //发票管理
                intent.setClass(MainActivity.this, ReceiptActivity.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                break;
            case R.id.rly_team_managent:
                intent.setClass(MainActivity.this, TeamManagentActivity.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                break;
            case R.id.rly_menu_coupon:
                //优惠券
                intent.setClass(MainActivity.this, CouponActivity.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                break;
            case R.id.rly_menu_advice:
                //建议反馈
                intent.setClass(MainActivity.this, AdviceActivity.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                break;
            case R.id.rly_menu_more:
                //更多
                intent.setClass(MainActivity.this, MoreActivity.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == RESULT_OK) {
            Intent intent = new Intent(MainActivity.this, CallCarActivity.class);
            startActivity(intent);
        }
    }

    @Override
    protected String getDisplayTitle() {
        return "mainActivity";
    }


    private void hideView(String usrType) {
        tv_menu_usrname.setText(user.getLoginName());
        tv_menu_comname.setText(user.getEnterpriseInfo().getEnterpriseName());
        tv_tv_menu_momey.setText(user.getBalance());

        if (usrType.equals(UsrType.USRTYPE_GENERAL_USR)) {
            rly_menu_member_info.setVisibility(View.GONE);
            rly_menu_recharge.setVisibility(View.GONE);
            rly_menu_invoice_management.setVisibility(View.GONE);
            rly_team_managent.setVisibility(View.GONE);
            rly_menu_coupon.setVisibility(View.GONE);
            lly_menu_member_info.setVisibility(View.GONE);
            imv_menu_realname_below.setVisibility(View.GONE);
            imv_menu_recharge_below.setVisibility(View.GONE);
            imv_menu_invoice_management_below.setVisibility(View.GONE);
            imv_menu_team_management_below.setVisibility(View.GONE);
            imv_menu_coupon_below.setVisibility(View.GONE);
        } else {
//            int flag = user.getEnterpriseInfo().getEnterpriseAuthFlag();
//            if (flag == 3) {
//                tv_menu_un_realname.setVisibility(View.GONE);
//            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            if (productFragment != null && productFragment.isVisible()) {

                if ((System.currentTimeMillis() - exitTime) > 2000) {
                    ToastUtils.showToast(MainActivity.this, "再按一次退出程序");
                    exitTime = System.currentTimeMillis();
                } else {
                    finish();
                    System.exit(0);
                }
            } else {
                productFragment = ProductFragment.newInstance();
                FragmentManager fm = getFragmentManager();
                FragmentTransaction tx = fm.beginTransaction();

                tx.replace(R.id.container, productFragment);
                tx.commit();
            }

            return true;
        }
        return super.onKeyDown(keyCode, event);
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
                            SPLoginUser.saveTokenTo(MainActivity.this, accessToken);

                            if (result.getUsrType().equals("1")) {
                                user.setAccessToken(accessToken);
                            } else {
                                user.setBalance(result.getBalance());
                                user.setLoginName(result.getUsrName());
                                user.setAccessToken(accessToken);
                                user.setEnterpriseInfo(LoginUser.getEnterpriseInfo(enterpriseInfo));

                                tv_menu_usrname.setText(result.getUsrName());
                                tv_menu_comname.setText(enterpriseInfo.getEnterpriseName());
                                tv_tv_menu_momey.setText(result.getBalance());

                                String flag = enterpriseInfo.getEnterpriseAuthFlag();
                                if (CommonUtils.hasText(flag)) {
                                    if (flag.equals("0")) {
                                        tv_menu_un_realname.setText("待审核");
                                    } else if (flag.equals("1")) {
                                        tv_menu_un_realname.setText("已通过");
                                    } else if (flag.equals("2")) {
                                        tv_menu_un_realname.setText("已驳回");
                                    } else {
                                        tv_menu_un_realname.setText("未实名");
                                    }
                                }
                            }
                            LoginUser.saveUserToDB(user, MainActivity.this);
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {

                    }
                });
    }
}
