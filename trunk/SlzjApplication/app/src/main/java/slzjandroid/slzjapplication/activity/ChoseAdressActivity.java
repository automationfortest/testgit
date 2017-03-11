package slzjandroid.slzjapplication.activity;

import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.net.ConnectException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit.RetrofitError;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import slzjandroid.slzjapplication.R;
import slzjandroid.slzjapplication.adapter.AdressAdapter;
import slzjandroid.slzjapplication.context.AppContext;
import slzjandroid.slzjapplication.customView.NavigationView;
import slzjandroid.slzjapplication.customView.NavigationView.ClickCallback;
import slzjandroid.slzjapplication.dto.AdressResponse;
import slzjandroid.slzjapplication.dto.Hotvalue;
import slzjandroid.slzjapplication.dto.LoginUser;
import slzjandroid.slzjapplication.dto.PlaceData;
import slzjandroid.slzjapplication.dto.Value;
import slzjandroid.slzjapplication.lang.StatusCode;
import slzjandroid.slzjapplication.service.ServiceProvider;
import slzjandroid.slzjapplication.utils.ToastUtils;

/**
 * Created by hdb on 2016/3/29.
 */
public class ChoseAdressActivity extends BasicActivity implements ClickCallback {
    private EditText edt_choose_adress;
    private ListView list_adress;
    private ArrayList<PlaceData> addressesList = null;
    private AdressAdapter addressAdapter;
    private LoginUser loginUser;
    private TextView tv_chose_adress_city;
    private LinearLayout lly_chose_adress_city;
    private Value value = new Value();
    public static String cityName;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_chose_adress_from;
    }

    @Override
    protected void findViews() {
        edt_choose_adress = (EditText) findViewById(R.id.edt_choose_adress);
        list_adress = (ListView) findViewById(R.id.list_adress);
        lly_chose_adress_city = (LinearLayout) findViewById(R.id.lly_chose_adress_city);
        tv_chose_adress_city = (TextView) findViewById(R.id.tv_chose_adress_city);
    }


    @Override
    protected void init() {
        NavigationView navigationView = (NavigationView) super.findViewById(R.id.nav_main);
        AppContext.getInstance().addActivity(this);
        navigationView.setRightViewIsShow(false);
        navigationView.setClickCallback(this);

        String title = getIntent().getStringExtra("adress");
        if (title.equals("from")) {
            navigationView.setTitle("选择上车地点");
            edt_choose_adress.setHint("选择上车地点");
        } else {
            navigationView.setTitle("选择下车地点");
            edt_choose_adress.setHint("选择下车地点");
        }
        value = (Value) getIntent().getSerializableExtra("value");
        if (!TextUtils.isEmpty(value.getName())) {
            tv_chose_adress_city.setText(value.getName());
        } else {
            ToastUtils.showToast(ChoseAdressActivity.this, "定位失败，请手动选择城市");
        }

        loginUser = LoginUser.getUser();
        addressesList = new ArrayList<>();
        addressAdapter = new AdressAdapter(ChoseAdressActivity.this, addressesList);
    }

    @Override
    protected void bindViews() {
        lly_chose_adress_city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edt_choose_adress.setText("");
                Intent intent = new Intent(ChoseAdressActivity.this, CityChoseActivity.class);
                startActivityForResult(intent, 0);

            }
        });
        list_adress.setAdapter(addressAdapter);
        edt_choose_adress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    String text = edt_choose_adress.getText().toString().trim();
                    if (text.length() > 1) {
                        //开启查询
                        if (TextUtils.isEmpty(value.getName())) {
                            ToastUtils.showToast(ChoseAdressActivity.this, "请选择城市");
                            return;
                        }
                        Map<String, Object> map = new HashMap<String, Object>();
                        map.put("access_token", loginUser.getAccessToken());

                        map.put("keyWord", text);
                        map.put("cityName", value.getName());
                        ServiceProvider.addressService.getAddressList(map).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<AdressResponse>() {
                            @Override
                            public void call(AdressResponse adressResponse) {
                                int resCode = adressResponse.getStatus();
                                if (resCode == (StatusCode.RESPONSE_OK)) {
                                    addressesList.clear();
                                    addressesList.addAll(adressResponse.getResult());
                                    for (int i = 0; i < addressesList.size(); i++) {
                                        addressesList.get(i).setCityName(value.getName());
                                    }
                                    addressAdapter.notifyDataSetChanged();
                                } else if (resCode == (StatusCode.RESPONSE_ERR)) {
                                    ToastUtils.showToast(ChoseAdressActivity.this, "您离开时间太长，需要重新登陆");
                                    AppContext.getInstance().finishAllActivity();
                                    startActivity(new Intent(ChoseAdressActivity.this, LoginActivity.class));
                                }
                            }
                        }, new Action1<Throwable>() {
                            @Override
                            public void call(Throwable throwable) {
                                throwable.printStackTrace();
                                if (throwable instanceof ConnectException || throwable instanceof RetrofitError) {
                                    ToastUtils.showToast(ChoseAdressActivity.this, "网络异常，无法连接服务器");
                                    return;
                                }
                            }
                        });

                    } else {
                        addressesList.clear();
                        addressAdapter.notifyDataSetChanged();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == RESULT_OK) {
            if (data.getSerializableExtra("city_info") instanceof Value) {
                value = (Value) data.getSerializableExtra("city_info");

            }
            if (data.getSerializableExtra("city_info") instanceof Hotvalue) {
                Hotvalue hotvalue = (Hotvalue) data.getSerializableExtra("city_info");
                value.setArea(hotvalue.getArea());
                value.setCityId(hotvalue.getCityId());
                value.setDistrict(hotvalue.getDistrict());
                value.setName(hotvalue.getName());
                value.setRequireLevel(hotvalue.getRequireLevel());

            }
            tv_chose_adress_city.setText(value.getName());
            cityName = value.getName();
        } else {
            tv_chose_adress_city.setText(value.getName());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        edt_choose_adress.clearFocus();

    }

    @Override
    public void onBackClick() {
        finish();
    }

    @Override
    public void onRightClick() {

    }
}
