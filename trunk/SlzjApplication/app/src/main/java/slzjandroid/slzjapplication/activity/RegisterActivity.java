package slzjandroid.slzjapplication.activity;

import android.content.Intent;
import android.text.InputFilter;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import slzjandroid.slzjapplication.R;
import slzjandroid.slzjapplication.activity.ICallback.ICallBack;
import slzjandroid.slzjapplication.activity.ICallbackService.ExtendTokenTimeModel;
import slzjandroid.slzjapplication.activity.ICallbackService.RegisterModel;
import slzjandroid.slzjapplication.context.AppContext;
import slzjandroid.slzjapplication.customView.LoadingDialog;
import slzjandroid.slzjapplication.customView.NavigationView;
import slzjandroid.slzjapplication.db.CityDBManager;
import slzjandroid.slzjapplication.dto.CityResponse;
import slzjandroid.slzjapplication.dto.CityResult;
import slzjandroid.slzjapplication.dto.Hotvalue;
import slzjandroid.slzjapplication.dto.LoginUser;
import slzjandroid.slzjapplication.dto.RegisterRequest;
import slzjandroid.slzjapplication.dto.RegisterResponse;
import slzjandroid.slzjapplication.dto.TokenRenewalResponse;
import slzjandroid.slzjapplication.dto.UsrToken;
import slzjandroid.slzjapplication.dto.Value;
import slzjandroid.slzjapplication.helper.SPLoginUser;
import slzjandroid.slzjapplication.lang.StatusCode;
import slzjandroid.slzjapplication.service.ServiceProvider;
import slzjandroid.slzjapplication.utils.DialogUtil;
import slzjandroid.slzjapplication.utils.ToastUtils;

/**
 * Created by hdb on 2016/3/20.
 */
public class RegisterActivity extends BasicActivity implements NavigationView.ClickCallback, ICallBack<RegisterResponse> {
    private EditText edt_register_name, edt_register_comname, edt_register_email, edt_register_city;

    private TextView tv_register_value;

    private EditText tv_register_invitation_code;

    private Button btn_register_commit;
    private String phoneNum;
    private RegisterRequest.RegisterObj registerObj;

    private CheckBox cb_register_agree;

    private RegisterModel registerModel;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_register;
    }

    @Override
    protected void findViews() {
        tv_register_value = (TextView) findViewById(R.id.tv_register_value);
        edt_register_name = (EditText) findViewById(R.id.edt_register_name);
        edt_register_name.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});

        edt_register_comname = (EditText) findViewById(R.id.edt_register_comname);
        edt_register_comname.setFilters(new InputFilter[]{new InputFilter.LengthFilter(50)});

        edt_register_email = (EditText) findViewById(R.id.edt_register_email);

        edt_register_city = (EditText) findViewById(R.id.edt_register_city);
        edt_register_city.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});

        tv_register_invitation_code = (EditText) findViewById(R.id.tv_register_invitation_code);
        btn_register_commit = (Button) findViewById(R.id.btn_register_commit);

        cb_register_agree = (CheckBox) findViewById(R.id.cb_register_agree);

        findViewById(R.id.tv_service_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this, DocumentAtivity.class);
                intent.putExtra("pageNum", 1);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void init() {
        registerObj = new RegisterRequest().new RegisterObj();
        NavigationView navigationView = (NavigationView) super.findViewById(R.id.nav_main);
        navigationView.setRightViewIsShow(false);
        navigationView.setTitle("注册");
        navigationView.setClickCallback(this);

        AppContext.getInstance().addActivity(this);
        phoneNum = getIntent().getStringExtra("phoneNum");
        tv_register_value.setText(RegisterModel.setPhoneStyle(phoneNum));


        LoadingDialog loadingDialog = DialogUtil.getLoadingDialog(this, "正在注册中...");
        registerModel = new RegisterModel(RegisterActivity.this, loadingDialog);
    }

    private void initRegisterObj() {

        registerObj.setUserName(edt_register_name.getText().toString().trim());
        registerObj.setEnterpriseName(edt_register_comname.getText().toString().trim());
        registerObj.setEnterpriseEmail(edt_register_email.getText().toString().trim());
        registerObj.setCityName(edt_register_city.getText().toString().trim());
        registerObj.setInviteCode(tv_register_invitation_code.getText().toString().trim());
    }

    private boolean isDataNull() {
        boolean name = RegisterModel.isContant(RegisterActivity.this, registerObj.getUserName(), "联系人姓名");
        if (!name) {
            return false;
        }
        boolean enterproseName = RegisterModel.isContant(RegisterActivity.this, registerObj.getEnterpriseName(), "企业名称");
        if (!enterproseName) {
            return false;
        }
        boolean email = RegisterModel.isMEamil(RegisterActivity.this, registerObj.getEnterpriseEmail());
        if (!email) {
            return false;
        }
        if (!cb_register_agree.isChecked()) {
            ToastUtils.showToast(RegisterActivity.this, "您必须同意《钱包行云服务协议》才能注册！");
            return false;
        }
        return true;
    }


    @Override
    protected void bindViews() {

        btn_register_commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initRegisterObj();
                if (isDataNull()) {
                    registerModel.register(registerObj, RegisterActivity.this);
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

    }

    @Override
    public void success(RegisterResponse registerResponse) {
        if (null != registerResponse && registerResponse.getStatus() == StatusCode.RESPONSE_OK) {

            ExtendTokenTimeModel.getInstance(this).setExtendTokenTimeModel(phoneNum, new ICallBack<TokenRenewalResponse>() {
                @Override
                public void success(TokenRenewalResponse tokenRenewalResponse) {
                    if (tokenRenewalResponse.getStatus() == StatusCode.RESPONSE_OK) {
                        //保存在sp里面（后期需要加密）
                        UsrToken usrToken = tokenRenewalResponse.getResult().getUsrToken();
                        SPLoginUser.saveTokenTo(RegisterActivity.this, usrToken.getAccessToken());
                        ToastUtils.showToast(RegisterActivity.this, "注册成功");
                        LoginUser loginUser = LoginUser.getLoginUser(phoneNum, tokenRenewalResponse);
                        LoginUser.saveUserToDB(loginUser, RegisterActivity.this);
                        getCityList();
                    }
                }

                @Override
                public void fail(String info) {
                    ToastUtils.showToast(RegisterActivity.this, info);
                }

                @Override
                public void error(Throwable throwable) {
                    ToastUtils.showToast(RegisterActivity.this, throwable.getMessage());
                }
            });
        }
    }

    @Override
    public void fail(String info) {
        ToastUtils.showToast(RegisterActivity.this, info);
    }

    @Override
    public void error(Throwable throwable) {
        ToastUtils.showToast(RegisterActivity.this, throwable.getMessage());
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private ArrayList<Hotvalue> hotvalues = new ArrayList<>();
    private ArrayList<Value> cityValues = new ArrayList<>();

    private void getCityList() {
        LoginUser user = LoginUser.getUser();

        try {
            ServiceProvider.cityService.getCity(user.getAccessToken()).observeOn(AndroidSchedulers.mainThread())
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
                                    Intent intent = new Intent();
                                    intent.setClass(RegisterActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    RegisterActivity.this.finish();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    ToastUtils.showToast(RegisterActivity.this, "初始化数据失败，请重新启动");
                                }
                            } else {
                                ToastUtils.showToast(RegisterActivity.this, "初始化数据失败，请重新启动");
                            }


                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {
                            throwable.printStackTrace();
                            ToastUtils.showToast(RegisterActivity.this, "初始化数据失败，请重新启动");
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
            ToastUtils.showToast(RegisterActivity.this, "初始化数据失败，请重新启动");
        } finally {

        }

    }

    private void setCityData() {
        CityDBManager cityDbManager = new CityDBManager(RegisterActivity.this);
        cityDbManager.deleteDB();
        cityDbManager.addHotcity(hotvalues);
        cityDbManager.add(cityValues);
        cityDbManager.closeDB();
    }


}
