package slzjandroid.slzjapplication.activity.ICallbackService;

import java.util.HashMap;
import java.util.Map;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import slzjandroid.slzjapplication.activity.ICallback.ICallBack;
import slzjandroid.slzjapplication.customView.LoadingDialog;
import slzjandroid.slzjapplication.dto.CouponRequest;
import slzjandroid.slzjapplication.dto.LoginUser;
import slzjandroid.slzjapplication.lang.StatusCode;
import slzjandroid.slzjapplication.service.ServiceProvider;

/**
 * Created by xuyifei on 16/4/20.
 */
public class CouponModel {


    private LoadingDialog dialog;

    public CouponModel(LoadingDialog dialog) {
        this.dialog = dialog;
    }


    public void getCouponData(String isEffective, final ICallBack callBack) {
        dialog.show();
        LoginUser user = LoginUser.getUser();
        Map<String, String> parmes = new HashMap<>();

        String enterpriseIdx = user.getEnterpriseInfo().getEnterpriseIdx();
        parmes.put("enterpriseIdx", enterpriseIdx);
        parmes.put("couponStatus", isEffective);

        ServiceProvider.couponservice.getCouponService(user.getAccessToken(), parmes)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<CouponRequest>() {
                    @Override
                    public void call(CouponRequest couponRequest) {
                        dialog.dismiss();
                        if (couponRequest.getStatus().equals(String.valueOf(StatusCode.RESPONSE_OK))) {
                            callBack.success(couponRequest);
                        } else {
                            callBack.fail("获取失败");
                        }

                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        dialog.dismiss();
                        callBack.fail(throwable.getMessage());
                    }
                });
    }
}
