package slzjandroid.slzjapplication.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.net.ConnectException;
import java.util.ArrayList;

import retrofit.RetrofitError;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import slzjandroid.slzjapplication.R;
import slzjandroid.slzjapplication.context.AppContext;
import slzjandroid.slzjapplication.customView.LoadingDialog;
import slzjandroid.slzjapplication.db.CityDBManager;
import slzjandroid.slzjapplication.dto.CityResponse;
import slzjandroid.slzjapplication.dto.CityResult;
import slzjandroid.slzjapplication.dto.Hotvalue;
import slzjandroid.slzjapplication.dto.LoginRequest;
import slzjandroid.slzjapplication.dto.LoginResponse;
import slzjandroid.slzjapplication.dto.LoginUser;
import slzjandroid.slzjapplication.dto.SmsResponse;
import slzjandroid.slzjapplication.dto.Value;
import slzjandroid.slzjapplication.helper.SPLoginUser;
import slzjandroid.slzjapplication.lang.StatusCode;
import slzjandroid.slzjapplication.service.ServiceProvider;
import slzjandroid.slzjapplication.utils.CommonUtils;
import slzjandroid.slzjapplication.utils.DateUtil;
import slzjandroid.slzjapplication.utils.DialogUtil;
import slzjandroid.slzjapplication.utils.NumbersUtils;
import slzjandroid.slzjapplication.utils.ToastUtils;

public class LoginActivity extends BasicActivity implements View.OnClickListener, Handler.Callback, TextWatcher {
    private Button btn_sms, loginButton;
    private Handler handler = null;
    private int recLen = 61;
    private EditText loginNameEt;
    private EditText loginsmsET;

    private ArrayList<Hotvalue> hotvalues = new ArrayList<>();
    private ArrayList<Value> cityValues = new ArrayList<>();


    @Override
    protected int getLayoutID() {
        return R.layout.activity_login;
    }

    @Override
    protected void findViews() {
        btn_sms = (Button) findViewById(R.id.btn_login_sms);
        loginButton = (Button) findViewById(R.id.btn_login_login);
        loginNameEt = (EditText) findViewById(R.id.loginNameEt);
        loginsmsET = (EditText) findViewById(R.id.loginSMSET);
    }

    @Override
    protected void init() {
        AppContext.getInstance().addActivity(this);
        handler = new Handler(this);
        btn_sms.setTextColor(getResources().getColor(R.color.btn_sms));
    }

    @Override
    protected void bindViews() {
        btn_sms.setOnClickListener(this);
        loginButton.setOnClickListener(this);
        loginNameEt.addTextChangedListener(this);
        loginsmsET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                boolean isLegal = NumbersUtils.isNumberLenght(editable.toString().trim(), 6);
                if (isLegal) {
                    CommonUtils.disInputMethod(LoginActivity.this);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        String phoneNum = loginNameEt.getText().toString().trim();
        String smsNum = loginsmsET.getText().toString().trim();
        switch (v.getId()) {
            case R.id.btn_login_sms:
                isPhoneNull(phoneNum);
                break;
            case R.id.btn_login_login:
                isPhoneAndSMSNull(phoneNum, smsNum);
                break;
        }
    }


    private void isPhoneNull(String phoneNum) {

        if (TextUtils.isEmpty(phoneNum)) {
            ToastUtils.showToast(this, getResources().getString(R.string.context_is_not_null));
            return;
        } else {
            if (NumbersUtils.isCellphoneNo(phoneNum)) {
                sendSMS(phoneNum);
            } else {
                ToastUtils.showToast(this, getResources().getString(R.string.notify_sms));
                return;
            }
        }
    }

    private void isPhoneAndSMSNull(String phoneNum, String smsNum) {
        if (TextUtils.isEmpty(phoneNum)) {
            ToastUtils.showToast(this, getResources().getString(R.string.context_is_not_null));
            return;
        } else if (TextUtils.isEmpty(smsNum)) {
            ToastUtils.showToast(this, getResources().getString(R.string.sms_context_not_null));
            return;
        } else {
            if (NumbersUtils.isCellphoneNo(phoneNum)) {
                login(phoneNum, smsNum);
            } else {
                ToastUtils.showToast(this, getResources().getString(R.string.notify_sms));
                return;
            }
        }
    }

    /**
     * 登陆
     */
    public void login(final String phoneNum, String smsNum) {
        final LoadingDialog dialog = DialogUtil.getLoadingDialog(this, "正在登陆...");
        dialog.show();

        ServiceProvider.loginService.getLoginInfo(LoginRequest.loginReqInfo(phoneNum, smsNum, "", DateUtil.getCurrentTime(DateUtil.YMDHMS_STYLE_ONE)))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<LoginResponse>() {

                    @Override
                    public void call(LoginResponse loginResponse) {
                        if (loginResponse.getStatus() == StatusCode.RESPONSE_OK) {
                            dialog.dismiss();
                            String newUsrFlag = loginResponse.getResult().getNewUsrFlag();
                            //跳转到MainActivity
                            LoginUser loginUser = LoginUser.getLoginUser(phoneNum, loginResponse);
                            //保存在user数据库中（后期需要加密）
                            LoginUser.saveUserToDB(loginUser, LoginActivity.this);
                            //保存在sp里面（后期需要加密）
                            SPLoginUser.saveTokenTo(LoginActivity.this, loginUser.getAccessToken());
                            if (LoginUser.getNewUserFlag(newUsrFlag)) {
                                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                                intent.putExtra("phoneNum", phoneNum);
                                startActivity(intent);
                            } else {
                                getCityList();
                            }
                        } else {
                            ToastUtils.showToast(LoginActivity.this, loginResponse.getMessage());
                            dialog.dismiss();
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        dialog.dismiss();
                        throwable.printStackTrace();
                        if (throwable instanceof ConnectException || throwable instanceof RetrofitError) {
                            ToastUtils.showToast(LoginActivity.this, "网络异常，无法连接服务器");
                            return;
                        }

                    }
                });
    }


    void sendSMS(String cellPhone) {
        final LoadingDialog dialog = DialogUtil.getLoadingDialog(this, "正在获取验证码...");
        try {
            dialog.show();
            ServiceProvider.smsService.sendSms(cellPhone)
                    .observeOn(rx.android.schedulers.AndroidSchedulers.mainThread()).
                    subscribe(new Action1<SmsResponse>() {
                                  @Override
                                  public void call(SmsResponse smsResponse) {
                                      dialog.dismiss();
                                      if (smsResponse.getStatus() == (StatusCode.RESPONSE_OK)) {
                                          handler.sendEmptyMessage(1);
                                          ToastUtils.showToast(LoginActivity.this, getResources().getString(R.string.notify_sms_sucess));
                                      } else {
                                          ToastUtils.showToast(LoginActivity.this, smsResponse.getMessage());
                                      }
                                  }
                              }, new Action1<Throwable>() {
                                  @Override
                                  public void call(Throwable throwable) {
                                      dialog.dismiss();
                                      throwable.printStackTrace();
                                      if (throwable instanceof ConnectException || throwable instanceof RetrofitError) {
                                          ToastUtils.showToast(LoginActivity.this, "网络异常，无法连接服务器");
                                          return;
                                      }

                                  }
                              }
                    );
        } catch (Exception e) {
            e.printStackTrace();
            dialog.dismiss();
        }
    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case 0:
                break;
            case 1:
                recLen--;
                btn_sms.setText("已发送(" + recLen + "秒)");
                if (recLen > 0) {
                    loginNameEt.setEnabled(false);
                    btn_sms.setClickable(false);
                    Message message = handler.obtainMessage(1);
                    handler.sendMessageDelayed(message, 1000); // send message
                } else {
                    btn_sms.setText("重新获取");
                    loginNameEt.setEnabled(true);
                    btn_sms.setClickable(true);
                    recLen = 61;
                }
                break;
            case 2:
                // 忘记密码的验证失败
                btn_sms.setText("重新获取");
                loginNameEt.setEnabled(true);
                btn_sms.setClickable(true);
                recLen = 61;
                break;
            default:
                break;
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (handler != null) {
            handler.removeMessages(1);
            handler = null;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {

        boolean isLegal = NumbersUtils.isNumberLenght(editable.toString().trim(), 11);
        if (isLegal) {
            CommonUtils.disInputMethod(LoginActivity.this);
            btn_sms.setTextColor(getResources().getColor(R.color.maroon));
        } else {
            btn_sms.setTextColor(getResources().getColor(R.color.btn_sms));
        }
    }


    private void getCityList() {
        String token = SPLoginUser.getToken(LoginActivity.this);
        try {
            ServiceProvider.cityService.getCity(token).observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<CityResponse>() {
                        @Override
                        public void call(CityResponse cityResponse) {
                            if (cityResponse.getStatus().equals("200")) {
                                for (int i = 0; i < cityResponse.getResult().size(); i++) {
                                    CityResult cityResult = cityResponse.getResult().get(i);
                                    if (cityResult.getHotvalue() != null) {
                                        hotvalues.addAll(cityResult.getHotvalue());
                                    }
                                    if (cityResult.getValue() != null) {
                                        for (int j = 0; j < cityResult.getValue().size(); j++) {
                                            Value value = cityResult.getValue().get(j);
                                            value.setPinyin(cityResult.getLetter());
                                            cityValues.add(value);
                                        }
                                    }
                                }
                                try {
                                    setCityData();
                                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                    LoginActivity.this.finish();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    ToastUtils.showToast(LoginActivity.this, "初始化数据失败，请重新启动");
                                }
                            } else {
                                ToastUtils.showToast(LoginActivity.this, "初始化数据失败，请重新启动");
                            }


                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {
                            throwable.printStackTrace();
                            ToastUtils.showToast(LoginActivity.this, "初始化数据失败，请重新启动");
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
            ToastUtils.showToast(LoginActivity.this, "初始化数据失败，请重新启动");
        } finally {

        }

    }

    private void setCityData() {
        CityDBManager cityDbManager = new CityDBManager(this);
        cityDbManager.deleteDB();
        cityDbManager.addHotcity(hotvalues);
        cityDbManager.add(cityValues);
        cityDbManager.closeDB();
    }

    @Override
    protected String getDisplayTitle() {
        return "LoginActivity";
    }


}
