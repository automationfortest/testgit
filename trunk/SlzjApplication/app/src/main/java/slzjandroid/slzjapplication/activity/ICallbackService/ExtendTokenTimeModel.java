package slzjandroid.slzjapplication.activity.ICallbackService;

import android.content.Context;

import java.util.HashMap;
import java.util.Map;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import slzjandroid.slzjapplication.activity.ICallback.ICallBack;
import slzjandroid.slzjapplication.customView.LoadingDialog;
import slzjandroid.slzjapplication.dto.LoginUser;
import slzjandroid.slzjapplication.dto.TokenRenewalResponse;
import slzjandroid.slzjapplication.lang.StatusCode;
import slzjandroid.slzjapplication.service.ServiceProvider;
import slzjandroid.slzjapplication.utils.DateUtil;
import slzjandroid.slzjapplication.utils.DialogUtil;

/**
 * Created by xuyifei on 16/4/18.
 */
public class ExtendTokenTimeModel {

    private static ExtendTokenTimeModel extendTokenTimeModel = null;

    private LoadingDialog dialog;

    private Context context;

    private ExtendTokenTimeModel(Context context) {

        this.context = context;
        dialog = DialogUtil.getLoadingDialog(context, "正在登陆...");

    }

    public static ExtendTokenTimeModel getInstance(Context context) {
        if (extendTokenTimeModel == null) {
            extendTokenTimeModel = new ExtendTokenTimeModel(context);
        }
        return extendTokenTimeModel;
    }


    public void setExtendTokenTimeModel(String phoneNum, final ICallBack iCallBack) {
        Map<String, Object> parems = new HashMap<>();
        LoginUser user = LoginUser.getUser();
        parems.put("cellphone", phoneNum);
        parems.put("timestamp", DateUtil.getCurrentTime(DateUtil.YMDHMS_STYLE_ONE));
        ServiceProvider.tokenService.tokenRenewal(user.getAccessToken(), parems)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<TokenRenewalResponse>() {
            @Override
            public void call(TokenRenewalResponse tokenResponse) {
                if (tokenResponse.getStatus() == StatusCode.RESPONSE_OK) {
                    iCallBack.success(tokenResponse);
                } else {
                    iCallBack.fail("延时Token值失败");
                }
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                iCallBack.error(throwable);
            }
        });

    }
}
