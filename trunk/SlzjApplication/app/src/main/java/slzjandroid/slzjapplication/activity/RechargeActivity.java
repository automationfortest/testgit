package slzjandroid.slzjapplication.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alipay.sdk.app.PayTask;
import com.google.gson.Gson;
import com.tencent.mm.sdk.constants.Build;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import java.net.ConnectException;
import java.util.HashMap;
import java.util.Map;

import retrofit.RetrofitError;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import slzjandroid.slzjapplication.R;
import slzjandroid.slzjapplication.context.AppContext;
import slzjandroid.slzjapplication.customView.NavigationView;
import slzjandroid.slzjapplication.dto.EnterpriseInfo;
import slzjandroid.slzjapplication.dto.LoginUser;
import slzjandroid.slzjapplication.dto.PayResponse;
import slzjandroid.slzjapplication.dto.PayResult;
import slzjandroid.slzjapplication.dto.TokenRefresh;
import slzjandroid.slzjapplication.dto.WXPayResponse;
import slzjandroid.slzjapplication.helper.SPLoginUser;
import slzjandroid.slzjapplication.lang.Constant;
import slzjandroid.slzjapplication.lang.StatusCode;
import slzjandroid.slzjapplication.service.ServiceProvider;
import slzjandroid.slzjapplication.utils.CommonUtils;
import slzjandroid.slzjapplication.utils.DateUtil;
import slzjandroid.slzjapplication.utils.EditinputFilter;
import slzjandroid.slzjapplication.utils.ToastUtils;

/**
 * Created by hdb on 2016/3/23.
 */
public class RechargeActivity extends BasicActivity implements NavigationView.ClickCallback, View.OnClickListener {
    private TextView tv_recharge_service_agreement, tv_recharge_balance, tv_recharge_amount_500, tv_recharge_amount_1000, tv_recharge_amount_5000, tv_recharge_amount_10000, tv_recharge_amount_20000, tv_recharge_amount_50000;
    private ImageView imvv_recharge_amount_500, imv_recharge_amount_1000, imvv_recharge_amount_5000, imv_recharge_amount_10000, imv_recharge_amount_20000, imv_recharge_amount_50000;
    private LinearLayout wx_layout, zfb_layout;
    private EditText edt_recharge_amount;
    private CheckBox cb_agree;

    private static final String WXAPPID = "wxfd6ccb7461de6b46";
    private IWXAPI api;
    private static final int SDK_PAY_FLAG = 1;
    private String payInfo = "";
    private String accessToken;
    private String enterpriseIdx;
    private LoginUser user;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_menu_recharge;
    }

    @Override
    protected void findViews() {
        edt_recharge_amount = (EditText) findViewById(R.id.edt_recharge_amount);
        tv_recharge_service_agreement = (TextView) findViewById(R.id.tv_recharge_service_agreement);
        wx_layout = (LinearLayout) findViewById(R.id.wx_layout);
        zfb_layout = (LinearLayout) findViewById(R.id.zfb_layout);
        tv_recharge_balance = (TextView) findViewById(R.id.tv_recharge_balance);
        tv_recharge_amount_500 = (TextView) findViewById(R.id.tv_recharge_amount_500);
        tv_recharge_amount_1000 = (TextView) findViewById(R.id.tv_recharge_amount_1000);
        tv_recharge_amount_5000 = (TextView) findViewById(R.id.tv_recharge_amount_5000);
        tv_recharge_amount_10000 = (TextView) findViewById(R.id.tv_recharge_amount_10000);
        tv_recharge_amount_20000 = (TextView) findViewById(R.id.tv_recharge_amount_20000);
        tv_recharge_amount_50000 = (TextView) findViewById(R.id.tv_recharge_amount_50000);
        imvv_recharge_amount_500 = (ImageView) findViewById(R.id.imvv_recharge_amount_500);
        imv_recharge_amount_1000 = (ImageView) findViewById(R.id.imv_recharge_amount_1000);
        imvv_recharge_amount_5000 = (ImageView) findViewById(R.id.imv_recharge_amount_5000);
        imv_recharge_amount_10000 = (ImageView) findViewById(R.id.imv_recharge_amount_10000);
        imv_recharge_amount_20000 = (ImageView) findViewById(R.id.imv_recharge_amount_20000);
        imv_recharge_amount_50000 = (ImageView) findViewById(R.id.imv_recharge_amount_50000);
        cb_agree = (CheckBox) findViewById(R.id.cb_recharge_agree);

    }

    @Override
    protected void init() {
        api = WXAPIFactory.createWXAPI(this, WXAPPID);
        api.registerApp(WXAPPID);

        AppContext.getInstance().addActivity(this);
        NavigationView navigationView = (NavigationView) super.findViewById(R.id.nav_main);


        navigationView.setRightTile("充值记录");
        navigationView.setTitle("充值");
        navigationView.setClickCallback(this);
        navigationView.setRightViewIsShow(true);
        edt_recharge_amount.setSelection(edt_recharge_amount.getText().length());
    }

    @Override
    protected void onResume() {
        super.onResume();
        user = LoginUser.getUser();
        accessToken = user.getAccessToken();
        enterpriseIdx = user.getEnterpriseInfo().getEnterpriseIdx();
        tv_recharge_balance.setText(user.getBalance());
        zfb_layout.setEnabled(true);
        wx_layout.setEnabled(true);

    }

    @Override
    protected void bindViews() {
        tv_recharge_service_agreement.setOnClickListener(this);
        wx_layout.setOnClickListener(this);
        zfb_layout.setOnClickListener(this);
        tv_recharge_amount_500.setOnClickListener(this);
        tv_recharge_amount_1000.setOnClickListener(this);
        tv_recharge_amount_5000.setOnClickListener(this);
        tv_recharge_amount_10000.setOnClickListener(this);
        tv_recharge_amount_20000.setOnClickListener(this);
        tv_recharge_amount_50000.setOnClickListener(this);
        imvv_recharge_amount_500.setVisibility(View.VISIBLE);
        imv_recharge_amount_1000.setVisibility(View.GONE);
        imvv_recharge_amount_5000.setVisibility(View.GONE);
        imv_recharge_amount_10000.setVisibility(View.GONE);
        imv_recharge_amount_20000.setVisibility(View.GONE);
        imv_recharge_amount_50000.setVisibility(View.GONE);
        EditinputFilter[] filters = {new EditinputFilter()};
        edt_recharge_amount.setFilters(filters);
        edt_recharge_amount.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    edt_recharge_amount.setText("");
                    imvv_recharge_amount_500.setVisibility(View.GONE);
                    imv_recharge_amount_1000.setVisibility(View.GONE);
                    imvv_recharge_amount_5000.setVisibility(View.GONE);
                    imv_recharge_amount_10000.setVisibility(View.GONE);
                    imv_recharge_amount_20000.setVisibility(View.GONE);
                    imv_recharge_amount_50000.setVisibility(View.GONE);
                }
            }
        });
        edt_recharge_amount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (edt_recharge_amount.getText().toString().equals(tv_recharge_amount_500.getText().toString())) {
                    imvv_recharge_amount_500.setVisibility(View.VISIBLE);
                    imv_recharge_amount_1000.setVisibility(View.GONE);
                    imvv_recharge_amount_5000.setVisibility(View.GONE);
                    imv_recharge_amount_10000.setVisibility(View.GONE);
                    imv_recharge_amount_20000.setVisibility(View.GONE);
                    imv_recharge_amount_50000.setVisibility(View.GONE);
                } else if (edt_recharge_amount.getText().toString().equals(tv_recharge_amount_1000.getText().toString())) {
                    imvv_recharge_amount_500.setVisibility(View.GONE);
                    imv_recharge_amount_1000.setVisibility(View.VISIBLE);
                    imvv_recharge_amount_5000.setVisibility(View.GONE);
                    imv_recharge_amount_10000.setVisibility(View.GONE);
                    imv_recharge_amount_20000.setVisibility(View.GONE);
                    imv_recharge_amount_50000.setVisibility(View.GONE);
                } else if (edt_recharge_amount.getText().toString().equals(tv_recharge_amount_5000.getText().toString())) {
                    imvv_recharge_amount_500.setVisibility(View.GONE);
                    imv_recharge_amount_1000.setVisibility(View.GONE);
                    imvv_recharge_amount_5000.setVisibility(View.VISIBLE);
                    imv_recharge_amount_10000.setVisibility(View.GONE);
                    imv_recharge_amount_20000.setVisibility(View.GONE);
                    imv_recharge_amount_50000.setVisibility(View.GONE);
                } else if (edt_recharge_amount.getText().toString().equals(tv_recharge_amount_10000.getText().toString())) {
                    imvv_recharge_amount_500.setVisibility(View.GONE);
                    imv_recharge_amount_1000.setVisibility(View.GONE);
                    imvv_recharge_amount_5000.setVisibility(View.GONE);
                    imv_recharge_amount_10000.setVisibility(View.VISIBLE);
                    imv_recharge_amount_20000.setVisibility(View.GONE);
                    imv_recharge_amount_50000.setVisibility(View.GONE);
                } else if (edt_recharge_amount.getText().toString().equals(tv_recharge_amount_20000.getText().toString())) {
                    imvv_recharge_amount_500.setVisibility(View.GONE);
                    imv_recharge_amount_1000.setVisibility(View.GONE);
                    imvv_recharge_amount_5000.setVisibility(View.GONE);
                    imv_recharge_amount_10000.setVisibility(View.GONE);
                    imv_recharge_amount_20000.setVisibility(View.VISIBLE);
                    imv_recharge_amount_50000.setVisibility(View.GONE);
                } else if (edt_recharge_amount.getText().toString().equals(tv_recharge_amount_50000.getText().toString())) {
                    imvv_recharge_amount_500.setVisibility(View.GONE);
                    imv_recharge_amount_1000.setVisibility(View.GONE);
                    imvv_recharge_amount_5000.setVisibility(View.GONE);
                    imv_recharge_amount_10000.setVisibility(View.GONE);
                    imv_recharge_amount_20000.setVisibility(View.GONE);
                    imv_recharge_amount_50000.setVisibility(View.VISIBLE);
                } else {
                    imvv_recharge_amount_500.setVisibility(View.GONE);
                    imv_recharge_amount_1000.setVisibility(View.GONE);
                    imvv_recharge_amount_5000.setVisibility(View.GONE);
                    imv_recharge_amount_10000.setVisibility(View.GONE);
                    imv_recharge_amount_20000.setVisibility(View.GONE);
                    imv_recharge_amount_50000.setVisibility(View.GONE);
                }
            }
        });
    }


    @Override
    public void onBackClick() {
        finish();
    }

    @Override
    public void onRightClick() {
        Intent intent = new Intent(RechargeActivity.this, RechargeListInfoActivity.class);
        startActivity(intent);

    }

    @Override
    public void onClick(View view) {
        if (!cb_agree.isChecked()) {
            ToastUtils.showToast(RechargeActivity.this, "您必须同意《钱包行云充值服务协议》才能充值");
            return;
        }
        switch (view.getId()) {
            case R.id.wx_layout:
                wx_layout.setEnabled(false);
                wxPay();
                break;
            case R.id.zfb_layout:
                zfb_layout.setEnabled(false);
                setAliPay();
                break;
            case R.id.tv_recharge_amount_500:
                imvv_recharge_amount_500.setVisibility(View.VISIBLE);
                imv_recharge_amount_1000.setVisibility(View.GONE);
                imvv_recharge_amount_5000.setVisibility(View.GONE);
                imv_recharge_amount_10000.setVisibility(View.GONE);
                imv_recharge_amount_20000.setVisibility(View.GONE);
                imv_recharge_amount_50000.setVisibility(View.GONE);
                edt_recharge_amount.setText(tv_recharge_amount_500.getText().toString());
                break;
            case R.id.tv_recharge_amount_1000:
                imvv_recharge_amount_500.setVisibility(View.GONE);
                imv_recharge_amount_1000.setVisibility(View.VISIBLE);
                imvv_recharge_amount_5000.setVisibility(View.GONE);
                imv_recharge_amount_10000.setVisibility(View.GONE);
                imv_recharge_amount_20000.setVisibility(View.GONE);
                imv_recharge_amount_50000.setVisibility(View.GONE);
                edt_recharge_amount.setText(tv_recharge_amount_1000.getText().toString());
                break;
            case R.id.tv_recharge_amount_5000:
                imvv_recharge_amount_500.setVisibility(View.GONE);
                imv_recharge_amount_1000.setVisibility(View.GONE);
                imvv_recharge_amount_5000.setVisibility(View.VISIBLE);
                imv_recharge_amount_10000.setVisibility(View.GONE);
                imv_recharge_amount_20000.setVisibility(View.GONE);
                imv_recharge_amount_50000.setVisibility(View.GONE);
                edt_recharge_amount.setText(tv_recharge_amount_5000.getText().toString());
                break;
            case R.id.tv_recharge_amount_10000:
                imvv_recharge_amount_500.setVisibility(View.GONE);
                imv_recharge_amount_1000.setVisibility(View.GONE);
                imvv_recharge_amount_5000.setVisibility(View.GONE);
                imv_recharge_amount_10000.setVisibility(View.VISIBLE);
                imv_recharge_amount_20000.setVisibility(View.GONE);
                imv_recharge_amount_50000.setVisibility(View.GONE);
                edt_recharge_amount.setText(tv_recharge_amount_10000.getText().toString());
                break;
            case R.id.tv_recharge_amount_20000:
                imvv_recharge_amount_500.setVisibility(View.GONE);
                imv_recharge_amount_1000.setVisibility(View.GONE);
                imvv_recharge_amount_5000.setVisibility(View.GONE);
                imv_recharge_amount_10000.setVisibility(View.GONE);
                imv_recharge_amount_20000.setVisibility(View.VISIBLE);
                imv_recharge_amount_50000.setVisibility(View.GONE);
                edt_recharge_amount.setText(tv_recharge_amount_20000.getText().toString());

                break;
            case R.id.tv_recharge_amount_50000:
                imvv_recharge_amount_500.setVisibility(View.GONE);
                imv_recharge_amount_1000.setVisibility(View.GONE);
                imvv_recharge_amount_5000.setVisibility(View.GONE);
                imv_recharge_amount_10000.setVisibility(View.GONE);
                imv_recharge_amount_20000.setVisibility(View.GONE);
                imv_recharge_amount_50000.setVisibility(View.VISIBLE);
                edt_recharge_amount.setText(tv_recharge_amount_50000.getText().toString());

                break;
            case R.id.tv_recharge_service_agreement:
                Intent intent = new Intent(RechargeActivity.this, DocumentAtivity.class);
                intent.putExtra("pageNum", 6);
                startActivity(intent);
                break;
            default:
                break;
        }
        edt_recharge_amount.setSelection(edt_recharge_amount.getText().length());

    }


    /**
     * 判断支持支付版本
     *
     * @return
     */
    public boolean isPaySupported() {
        return api.getWXAppSupportAPI() >= Build.PAY_SUPPORTED_SDK_INT;
    }


    private void wxPay() {
        if (!api.isWXAppInstalled()) {
            ToastUtils.showToast(RechargeActivity.this, "您目前还没有安装微信！");
            return;
        }
        if (isPaySupported()) {
            setwxPay();
        } else {
            ToastUtils.showToast(RechargeActivity.this, "您当前微信版本不支持支付功能，请升级您的微信！");
        }
    }

    /**
     * 设置微信支付
     */
    private void setwxPay() {
        String amount = edt_recharge_amount.getText().toString().trim();
        if (!isRechargeAmount(amount)) {
            wx_layout.setEnabled(true);
            return;
        }
        ToastUtils.showToast(RechargeActivity.this, "获取订单中...");
        Map<String, String> options = new HashMap<>();
        options.put("paymentMethod", "0");
        if (CommonUtils.hasText(CommonUtils.getIP())) {
            String ip = CommonUtils.getIP();
            String[] split = ip.split("\n");
            options.put("spbillCreateIp", split[0]);
        }

        double v = Double.parseDouble(amount) * 100;
        amount = String.valueOf((int) v);
        options.put("chargeAmount", amount);
        options.put("submitTime", DateUtil.getCurrentTime(DateUtil.YMDHMS_STYLE_TOW));

        ServiceProvider.rechargeService.getWXPay(user.getAccessToken(), options).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<WXPayResponse>() {
                               @Override
                               public void call(WXPayResponse wxPayResponse) {

                                   wx_layout.setEnabled(true);
                                   try {
                                       if (wxPayResponse.getResponseCode().equals("SUC")) {
                                           String payInfoJson = wxPayResponse.getPayInfo();
                                           Gson gson = new Gson();
                                           WXPayResponse.PayInfo payInfo = gson.fromJson(payInfoJson, WXPayResponse.PayInfo.class);

                                           PayReq req = new PayReq();
                                           req.appId = WXAPPID;
                                           req.partnerId = payInfo.getPartnerid();
                                           req.prepayId = payInfo.getPrepayid();
                                           req.packageValue = "Sign=WXPay";
                                           req.nonceStr = payInfo.getNoncestr();
                                           req.timeStamp = payInfo.getTimestamp();
                                           req.sign = payInfo.getSign();
                                           ToastUtils.showToast(RechargeActivity.this, "正常调起支付...");
                                           // 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
                                           api.sendReq(req);

                                       } else if (wxPayResponse.getResponseCode().equals(StatusCode.S_RESPONSE_ERR)) {
                                           ToastUtils.showToast(RechargeActivity.this, "您离开时间太长，需要重新登陆");
                                           AppContext.getInstance().finishAllActivity();
                                           startActivity(new Intent(RechargeActivity.this, LoginActivity.class));
                                       } else {
                                           ToastUtils.showToast(RechargeActivity.this, "返回错误" + wxPayResponse.getResponseDesc());
                                       }
                                   } catch (Exception e) {
                                       ToastUtils.showToast(RechargeActivity.this, "服务器请求错误");
                                   }

                               }
                           }, new Action1<Throwable>() {
                               @Override
                               public void call(Throwable throwable) {
                                   wx_layout.setEnabled(true);
                                   ToastUtils.showToast(RechargeActivity.this, "调用订单失败");
                               }
                           }

                );
    }


    /**
     * 设置支付宝支付
     */
    private void setAliPay() {
        String amount = edt_recharge_amount.getText().toString().trim();
        if (!isRechargeAmount(amount)) {
            zfb_layout.setEnabled(true);
            return;
        }
        try {
            Map<String, String> options = new HashMap<>();
            options.put("enterpriseIdx", enterpriseIdx);
            options.put("paymentMethod", "1");
            options.put("spbillCreateIp", CommonUtils.getIP());
            options.put("chargeAmount", amount);
            options.put("submitTime", DateUtil.getCurrentTime(DateUtil.YMDHMS_STYLE_TOW));
            ServiceProvider.rechargeService.getOrderResult(accessToken, options)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<PayResponse>() {
                        @Override
                        public void call(PayResponse payResponse) {
                            zfb_layout.setEnabled(true);
                            if (payResponse.getResponseCode().equals("SUC")) {
                                payInfo = payResponse.getPayInfo();
                                if (payThread != null) {
                                    payThread = null;
                                }
                                payThread = new Thread(payRunnable);
                                payThread.start();
                            } else if (payResponse.getResponseCode().equals(StatusCode.S_RESPONSE_ERR)) {
                                ToastUtils.showToast(RechargeActivity.this, "您离开时间太长，需要重新登陆");
                                AppContext.getInstance().finishAllActivity();
                                startActivity(new Intent(RechargeActivity.this, LoginActivity.class));
                            } else {
                                ToastUtils.showToast(RechargeActivity.this, payResponse.getResponseDesc());
                            }
                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {
                            zfb_layout.setEnabled(true);
                            if (throwable instanceof ConnectException || throwable instanceof RetrofitError) {
                                ToastUtils.showToast(RechargeActivity.this, "网络异常，无法连接服务器");
                                return;
                            }
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
            zfb_layout.setEnabled(true);
        }

    }

    Runnable payRunnable = new Runnable() {

        @Override
        public void run() {
            // 构造PayTask 对象
            PayTask alipay = new PayTask(RechargeActivity.this);
            // 调用支付接口，获取支付结果
            String result = alipay.pay(payInfo, true);

            Message msg = new Message();
            msg.what = SDK_PAY_FLAG;
            msg.obj = result;
            mHandler.sendMessage(msg);
        }
    };

    // 必须异步调用
    Thread payThread;


    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    zfb_layout.setEnabled(true);
                    PayResult payResult = new PayResult((String) msg.obj);
                    /**
                     * 同步返回的结果必须放置到服务端进行验证（验证的规则请看https://doc.open.alipay.com/doc2/
                     * detail.htm?spm=0.0.0.0.xdvAU6&treeId=59&articleId=103665&
                     * docType=1) 建议商户依赖异步通知
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000")) {
                        ToastUtils.showToast(RechargeActivity.this, "支付成功");
                        tokenRefresh(accessToken);
                    } else {
                        // 判断resultStatus 为非"9000"则代表可能支付失败
                        // "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            ToastUtils.showToast(RechargeActivity.this, "支付结果确认中");
                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            ToastUtils.showToast(RechargeActivity.this, "支付失败");
                        }
                    }
                    break;
                }
                default:
                    break;
            }
        }

    };


    /**
     * token刷新
     */
    private void tokenRefresh(String token) {
        ServiceProvider.tokenService.tokenRefresh(token, new HashMap<String, String>())
                .observeOn(rx.android.schedulers.AndroidSchedulers.mainThread()) // 指定 Subscriber 的回调发生在主线程
                .subscribe(new Action1<TokenRefresh>() {
                               @Override
                               public void call(TokenRefresh tokenRefresh) {
                                   String status = tokenRefresh.getStatus();
                                   if (status.equals("200")) {
                                       TokenRefresh.Result result = tokenRefresh.getResult();
                                       String accessToken = result.getUsrToken().getAccessToken();
                                       SPLoginUser.saveTokenTo(RechargeActivity.this, accessToken);
                                       EnterpriseInfo enterpriseInfo = result.getEnterpriseInfo();
                                       user.setAccessToken(accessToken);
                                       user.setBalance(result.getBalance());
                                       user.setLoginName(result.getUsrName());
                                       user.setAccessToken(accessToken);
                                       user.setEnterpriseInfo(LoginUser.getEnterpriseInfo(enterpriseInfo));
                                       LoginUser.saveUserToDB(user, RechargeActivity.this);
                                   } else if (status.equals("401")) {
                                       //缺失是否存在订单判断，存在订单直接进入订单栏，否则进入登录
                                       startActivity(new Intent(RechargeActivity.this, LoginActivity.class));
                                       RechargeActivity.this.finish();
                                   }
                                   Intent intent = new Intent(RechargeActivity.this, RechargeListInfoActivity.class);
                                   startActivity(intent);
                               }
                           }, new Action1<Throwable>() {
                               @Override
                               public void call(Throwable throwable) {
                                   Intent intent = new Intent(RechargeActivity.this, RechargeListInfoActivity.class);
                                   startActivity(intent);
                               }
                           }
                );
    }


    /**
     * @param amount
     * @return
     */
    private boolean isRechargeAmount(String amount) {
        if (CommonUtils.hasText(amount)) {
            double v = Double.parseDouble(amount);
            if (Constant.isLargeAmount) {
                if (v < 1000) {
                    ToastUtils.showToast(RechargeActivity.this, "充值金额需大于等于1000");
                    return false;
                }
            }
        } else {
            ToastUtils.showToast(RechargeActivity.this, "充值金额不能为空");
            return false;
        }
        return true;

    }
}