package slzjandroid.slzjapplication.wxapi;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import java.util.HashMap;

import rx.functions.Action1;
import slzjandroid.slzjapplication.R;
import slzjandroid.slzjapplication.activity.LoginActivity;
import slzjandroid.slzjapplication.activity.RechargeListInfoActivity;
import slzjandroid.slzjapplication.customView.LoadingDialog;
import slzjandroid.slzjapplication.dto.EnterpriseInfo;
import slzjandroid.slzjapplication.dto.LoginUser;
import slzjandroid.slzjapplication.dto.TokenRefresh;
import slzjandroid.slzjapplication.helper.SPLoginUser;
import slzjandroid.slzjapplication.service.ServiceProvider;
import slzjandroid.slzjapplication.utils.DialogUtil;
import slzjandroid.slzjapplication.utils.ToastUtils;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

    private static final String TAG = "MicroMsg.SDKSample.WXPayEntryActivity";

    private IWXAPI api;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_result);

        api = WXAPIFactory.createWXAPI(this, "wxfd6ccb7461de6b46");
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {

    }

    @Override
    public void onResp(BaseResp resp) {
        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("钱包行云充值提示:");
            if (resp.errCode == 0) {
                builder.setMessage("支付成功");
                final AlertDialog alertDioalog = builder.show();
                alertDioalog.dismiss();
                tokenRefresh();
            } else if (resp.errCode == -2) {
                ToastUtils.showToast(this, "您放弃了本次支付");
                finish();
            } else if (resp.errCode == -1) {
                ToastUtils.showToast(this, "调用微信支付失败");
                finish();
            } else {
                builder.setMessage(getString(R.string.pay_result_callback_msg + resp.errCode));
                final AlertDialog alertDioalog = builder.show();
                builder.setPositiveButton("关闭", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        alertDioalog.dismiss();
                        finish();
                    }
                });
            }
        }
    }

    /**
     * token刷新
     */
    private void tokenRefresh() {
        final LoadingDialog dialog = DialogUtil.getLoadingDialog(this, "正在刷新数据，请稍等...");
        dialog.show();
        final LoginUser user = LoginUser.getUser();
        ServiceProvider.tokenService.tokenRefresh(user.getAccessToken(), new HashMap<String, String>())
                .observeOn(rx.android.schedulers.AndroidSchedulers.mainThread()) // 指定 Subscriber 的回调发生在主线程
                .subscribe(new Action1<TokenRefresh>() {
                               @Override
                               public void call(TokenRefresh tokenRefresh) {
                                   dialog.dismiss();
                                   String status = tokenRefresh.getStatus();
                                   if (status.equals("200")) {
                                       TokenRefresh.Result result = tokenRefresh.getResult();
                                       String accessToken = result.getUsrToken().getAccessToken();
                                       SPLoginUser.saveTokenTo(WXPayEntryActivity.this, accessToken);
                                       EnterpriseInfo enterpriseInfo = result.getEnterpriseInfo();
                                       user.setAccessToken(accessToken);
                                       user.setBalance(result.getBalance());
                                       user.setLoginName(result.getUsrName());
                                       user.setAccessToken(accessToken);
                                       user.setEnterpriseInfo(LoginUser.getEnterpriseInfo(enterpriseInfo));
                                       LoginUser.saveUserToDB(user, WXPayEntryActivity.this);
                                   } else if (status.equals("401")) {
                                       //缺失是否存在订单判断，存在订单直接进入订单栏，否则进入登录
                                       startActivity(new Intent(WXPayEntryActivity.this, LoginActivity.class));
                                       WXPayEntryActivity.this.finish();
                                   }
                                   Message m = Message.obtain();
                                   m.what = 1;
                                   handler.sendMessage(m);

                               }
                           }, new Action1<Throwable>() {
                               @Override
                               public void call(Throwable throwable) {
                                   Message m = Message.obtain();
                                   m.what = 1;
                                   handler.sendMessage(m);
                               }
                           }
                );
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                Intent intent = new Intent(WXPayEntryActivity.this, RechargeListInfoActivity.class);
                startActivity(intent);
                WXPayEntryActivity.this.finish();
            }
        }
    };

}