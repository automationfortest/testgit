package slzjandroid.slzjapplication.activity.ICallbackService;

import android.content.Context;
import android.text.TextUtils;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import slzjandroid.slzjapplication.activity.ICallback.ICallBack;
import slzjandroid.slzjapplication.customView.LoadingDialog;
import slzjandroid.slzjapplication.dto.LoginUser;
import slzjandroid.slzjapplication.dto.RegisterRequest;
import slzjandroid.slzjapplication.dto.RegisterResponse;
import slzjandroid.slzjapplication.lang.StatusCode;
import slzjandroid.slzjapplication.service.ServiceProvider;
import slzjandroid.slzjapplication.utils.NumbersUtils;
import slzjandroid.slzjapplication.utils.ToastUtils;

/**
 * Created by xuyifei on 16/4/18.
 * 注册M
 */
public class RegisterModel {

    private LoadingDialog dialog;
    private Context context;

    public RegisterModel(Context context, LoadingDialog dialog) {
        this.dialog = dialog;
        this.context = context;
    }


    public void register(RegisterRequest.RegisterObj registerObj, final ICallBack iCallBack) {
        dialog.show();
        LoginUser user = LoginUser.getUser();
        ServiceProvider.registerService.register(user.getAccessToken(), RegisterRequest.registerInfo(registerObj))
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<RegisterResponse>() {
                    @Override
                    public void call(RegisterResponse registerResponse) {
                        dialog.dismiss();
                        if (registerResponse.getStatus() == StatusCode.RESPONSE_OK) {
                            iCallBack.success(registerResponse);
                        } else {
                            iCallBack.fail(registerResponse.getMessage());
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        dialog.dismiss();
                        iCallBack.error(throwable);
                    }
                });
    }


    /**
     * 拼接电话号码
     *
     * @param phone
     * @return
     */
    public static String setPhoneStyle(String phone) {
        if (TextUtils.isEmpty(phone)) {
            return "";
        }
        if (phone.length() == 11) {
            String sum1 = phone.substring(0, 3);
            String sum2 = phone.substring(3, 7);
            String sum3 = phone.substring(7, 11);
            StringBuffer sb = new StringBuffer();
            sb.append(sum1);
            sb.append("-");
            sb.append(sum2);
            sb.append("-");
            sb.append(sum3);
            return sb.toString();
        }
        return "";
    }


    public static boolean isContant(Context context, String contant, String str) {
        if (contant.equals("")) {
            ToastUtils.showToast(context, str + "不能为空");
            return false;
        } else {
            if (contant.length() < 2) {
                ToastUtils.showToast(context, str + "姓名最少不能少于两位");
                return false;
            }
            contant = contant.replace("（", "(").replace("）", ")");
            if (!NumbersUtils.isNum(contant)) {
                ToastUtils.showToast(context, str + "姓名只支持字母和汉字输入");
                return false;
            }
        }
        return true;
    }


    public static boolean isMEamil(Context context, String email) {
        if (TextUtils.isEmpty(email) || email.equals("")) {
            ToastUtils.showToast(context, "邮箱不能为空");
            return false;
        } else {
            if (NumbersUtils.isEmail(email)) {
                return true;
            } else {
                ToastUtils.showToast(context, "您输入的邮箱不合法");
                return false;
            }
        }
    }
}
