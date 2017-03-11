package slzjandroid.slzjapplication.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;

import java.net.ConnectException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit.RetrofitError;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import slzjandroid.slzjapplication.R;
import slzjandroid.slzjapplication.adapter.DeptAdapter;
import slzjandroid.slzjapplication.adapter.ReasonAdapter;
import slzjandroid.slzjapplication.context.AppContext;
import slzjandroid.slzjapplication.customView.LoadingDialog;
import slzjandroid.slzjapplication.customView.NavigationView;
import slzjandroid.slzjapplication.db.CityDBManager;
import slzjandroid.slzjapplication.dto.BudGetResponse;
import slzjandroid.slzjapplication.dto.CallReason;
import slzjandroid.slzjapplication.dto.CarType_haohua;
import slzjandroid.slzjapplication.dto.CarType_putong;
import slzjandroid.slzjapplication.dto.CarType_shangwu;
import slzjandroid.slzjapplication.dto.CarType_shushi;
import slzjandroid.slzjapplication.dto.DepartmentInfo;
import slzjandroid.slzjapplication.dto.LoginUser;
import slzjandroid.slzjapplication.dto.OrderCarResponse;
import slzjandroid.slzjapplication.dto.OrderTemplateGet;
import slzjandroid.slzjapplication.dto.PlaceData;
import slzjandroid.slzjapplication.dto.RuleInfo;
import slzjandroid.slzjapplication.dto.Value;
import slzjandroid.slzjapplication.helper.SPLoginUser;
import slzjandroid.slzjapplication.lang.StatusCode;
import slzjandroid.slzjapplication.service.ServiceProvider;
import slzjandroid.slzjapplication.utils.CommonUtils;
import slzjandroid.slzjapplication.utils.DateUtil;
import slzjandroid.slzjapplication.utils.DialogUtil;
import slzjandroid.slzjapplication.utils.MapUtils;
import slzjandroid.slzjapplication.utils.NumbersUtils;
import slzjandroid.slzjapplication.utils.ToastUtils;

/**
 * 企业用车
 */
public class CallCarActivity extends BasicActivity implements NavigationView.ClickCallback, AMapLocationListener, View.OnClickListener, TextWatcher {
    private static final double EARTH_RADIUS = 6378137;
    private AMapLocationClient locationClient = null;
    private AMapLocationClientOption locationOption = null;
    private TextView tv_from, tv_callcar_dept, tv_callcar_reason;
    private ImageView imv_car_type_01, imv_car_type_02, imv_car_type_03, imv_car_type_04, imv_car_type_chose;
    private TextView tv_to, tv_callcar_pre_amount, tv_callcar_discount_price;
    private EditText et_callcar_person;
    private EditText edt_callcar_cellphone;
    private Button btn_callcar;
    private LinearLayout lly_callcar_dept, lly_adress_from, lly_adress_to;
    private LinearLayout lly_callcar_reason, lly_callcar_precoast;
    private List<DepartmentInfo> deptList = null;
    private DepartmentInfo departmentInfo = null;
    private ArrayList<RuleInfo> ruleInfoList = null;
    private List<CallReason> callReasonList = null;
    private CallReason callReason = null;
    private View mContentView;
    private PopupWindow mPopupWindow;
    private LoginUser loginUser;
    final int START_ADDR_REQUEST_CODE = 100;
    final int END_ADDR_REQUEST_CODE = 101;
    private PlaceData place_from = new PlaceData();
    private Boolean from_flag = false;
    private PlaceData place_to = new PlaceData();
    private Value LocationValue = new Value();
    private CarType_putong putong_pre;
    private CarType_shushi shushi_pre;
    private CarType_shangwu shangwu_pre;
    private CarType_haohua haohua_pre;
    private int carType = 1;
    private String carKind = "";
    private static final int PUTONG = 1;
    private static final int SHUSHI = 2;
    private static final int SHANGWU = 3;
    private static final int HAOHAU = 4;
    private CityDBManager cityDbManager;
    private ReasonAdapter reasonAdapter;
    private DeptAdapter deptAdapter;
    private Boolean pre_flag = false;
    private String tag;
    private int flag;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_call_car;
    }

    @Override
    protected void findViews() {
        tv_callcar_dept = (TextView) findViewById(R.id.tv_callcar_dept);
        tv_callcar_reason = (TextView) findViewById(R.id.tv_callcar_reason);

        et_callcar_person = (EditText) findViewById(R.id.edt_callcar_person);
        edt_callcar_cellphone = (EditText) findViewById(R.id.edt_callcar_cellphone);

        tv_from = (TextView) findViewById(R.id.tv_callcar_from);
        tv_to = (TextView) findViewById(R.id.tv_callcar_to);
        tv_callcar_pre_amount = (TextView) findViewById(R.id.tv_callcar_pre_amount);
        tv_callcar_discount_price = (TextView) findViewById(R.id.tv_callcar_discount_price);
        btn_callcar = (Button) findViewById(R.id.btn_callcar);
        lly_callcar_dept = (LinearLayout) findViewById(R.id.lly_callcar_dept);
        lly_callcar_reason = (LinearLayout) findViewById(R.id.lly_callcar_reason);
        imv_car_type_01 = (ImageView) findViewById(R.id.imv_car_type_01);
        imv_car_type_02 = (ImageView) findViewById(R.id.imv_car_type_02);
        imv_car_type_03 = (ImageView) findViewById(R.id.imv_car_type_03);
        imv_car_type_04 = (ImageView) findViewById(R.id.imv_car_type_04);
        imv_car_type_chose = (ImageView) findViewById(R.id.imv_car_type_chose);
        lly_adress_from = (LinearLayout) findViewById(R.id.lly_adress_from);
        lly_adress_to = (LinearLayout) findViewById(R.id.lly_adress_to);
        lly_callcar_precoast = (LinearLayout) findViewById(R.id.lly_callcar_precoast);
        lly_callcar_precoast.setVisibility(View.INVISIBLE);

    }


    @Override
    protected void init() {
        AppContext.getInstance().addActivity(this);
        loginUser = LoginUser.getUser();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_main);
        navigationView.getBackView().setImageResource(R.drawable.selector_home_bg);
        navigationView.setTitle("企业用车");
        navigationView.setRightViewIsShow(false);
        navigationView.setClickCallback(this);
        flag = getIntent().getIntExtra("tag", -1);
        initData();
    }

    @Override
    protected void bindViews() {
        btn_callcar.setOnClickListener(this);
        lly_callcar_dept.setOnClickListener(this);
        lly_callcar_reason.setOnClickListener(this);
        imv_car_type_01.setOnClickListener(this);
        imv_car_type_02.setOnClickListener(this);
        imv_car_type_03.setOnClickListener(this);
        imv_car_type_04.setOnClickListener(this);
        lly_adress_from.setOnClickListener(this);
        lly_adress_to.setOnClickListener(this);
        edt_callcar_cellphone.addTextChangedListener(this);
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation != null) {
            Log.i("initStartAddress", aMapLocation.getCityCode() + aMapLocation.getCity());
            String address = aMapLocation.getAddress();
            if (from_flag == false) {
                if (!TextUtils.isEmpty(aMapLocation.getCity())) {
                    LocationValue = cityDbManager.queryCityCode(aMapLocation.getCity());
                    if (LocationValue.getCityId() != null) {
                        place_from.setAddress(aMapLocation.getAddress());
                        place_from.setCityName(LocationValue.getName());
                        place_from.setCityCode(LocationValue.getCityId());
                        place_from.setAdressLat(aMapLocation.getLatitude() + "");
                        place_from.setAdressLng(aMapLocation.getLongitude() + "");
                        place_from.setDisplayName(address);
                        place_from.setCityCode(LocationValue.getCityId());
                        tv_from.setText(aMapLocation.getAddress());
                    } else {
                        ToastUtils.showToast(CallCarActivity.this, "您所在的城市不支持打车服务，请手动输入出发地");
                    }
                } else {
                    ToastUtils.showToast(CallCarActivity.this, "定位失败，您可手动添加出发地");
                }
            }
        } else {
            ToastUtils.showToast(CallCarActivity.this, "定位失败，您可手动添加出发地");
        }
    }

    @Override
    public void onBackClick() {
        SPLoginUser.clearReasonName(CallCarActivity.this);
        finish();
    }

    @Override
    public void onRightClick() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        et_callcar_person.clearFocus();
        edt_callcar_cellphone.clearFocus();

        cityDbManager = new CityDBManager(CallCarActivity.this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ToastUtils.showToast(CallCarActivity.this, "钱包行云需要GPS权限来打车定位");
            return;
        }
        activate();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_callcar:
                //下单前检查信息完整性
                btn_callcar.setEnabled(false);
                if (callReason == null || departmentInfo == null || TextUtils.isEmpty(place_from.getAdressLat()) || TextUtils.isEmpty(place_to.getAdressLat())) {
                    ToastUtils.showToast(this, "请检查打车信息并保证完整");
                    btn_callcar.setEnabled(true);
                    return;
                }
                if (place_from.getDisplayName().equals(place_to.getDisplayName())) {
                    ToastUtils.showToast(this, "出发地和目的地不能相同");
                    btn_callcar.setEnabled(true);
                    return;
                }
                if ("".equals(et_callcar_person.getText().toString().trim())) {
                    ToastUtils.showToast(this, "乘车人姓名不能为空");
                    btn_callcar.setEnabled(true);
                    return;
                }
                if (!NumbersUtils.isCellphoneNo(edt_callcar_cellphone.getText().toString())) {
                    ToastUtils.showToast(this, "请输入正确的乘车人手机号");
                    btn_callcar.setEnabled(true);
                    return;
                }
                /**
                 * 限制500/m，1000/m，1500/m，2000/m，3000/m
                 */
                if (ruleInfoList != null && ruleInfoList.size() != 0) {
                    String name = "";
                    for (int i = 0; i < ruleInfoList.size(); i++) {
                        RuleInfo ruleInfo = ruleInfoList.get(i);
                        name = name + "\t" + ruleInfo.getConpouName();
                        //限制出发地
                        if (ruleInfo.getRuleType().equals("0")) {
                            double v = Double.parseDouble(ruleInfo.getRange());
                            Double aDouble = MapUtils.DistanceOfTwoPoints(
                                    Double.parseDouble(ruleInfoList.get(i).getYAxis()),
                                    Double.parseDouble(ruleInfoList.get(i).getXAxis()),
                                    Double.parseDouble(place_from.getAdressLat()),
                                    Double.parseDouble(place_from.getAdressLng()));
                            if (aDouble < v) {
                                btn_callcar.setEnabled(true);
                                tag = "0";
                                break;
                            } else if (aDouble > v) {
                                if (i == ruleInfoList.size() - 1) {
                                    ToastUtils.showToast(CallCarActivity.this, "您的用车地址在" + ruleInfo.getConpouName() + v + "米范围内,请确认上车地点！");
                                    btn_callcar.setEnabled(true);
                                    tag = "1";
                                }
                            }
                        } else if (ruleInfo.getRuleType().equals("1")) {
                            double v = Double.parseDouble(ruleInfo.getRange());
                            //限制目的地
                        }
                    }
                    if (CommonUtils.hasText(tag) && tag.equals("1")) {
                        return;
                    }
                    sendOrder();
                } else {
                    Double from_lat = Double.parseDouble(place_from.getAdressLat());
                    Double from_lng = Double.parseDouble(place_from.getAdressLng());
                    Double to_lat = Double.parseDouble(place_to.getAdressLat());
                    Double to_lng = Double.parseDouble(place_to.getAdressLng());
                    Double distance = MapUtils.DistanceOfTwoPoints(from_lat, from_lng, to_lat, to_lng);
                    if (distance > 100000) {
                        ToastUtils.showToast(this, "暂不支持远距离乘车，请修改出发地或目的地");
                        btn_callcar.setEnabled(true);
                        return;
                    }
                    sendOrder();
                }
                break;
            case R.id.lly_callcar_dept:
                if (deptList == null) {
                    return;
                }
                getDeptListView(lly_callcar_dept);
                break;
            case R.id.lly_callcar_reason:
                if (callReasonList == null) {
                    return;
                }
                getReasonListView(lly_callcar_reason);
                break;
            case R.id.imv_car_type_01:
                carType = PUTONG;
                imv_car_type_chose.setImageResource(R.mipmap.img_carording_car_bg_01);
                if (putong_pre == null) {
                    if (pre_flag) ToastUtils.showToast(CallCarActivity.this, "您选的地点不支持此车型");
                    return;
                }

                if (putong_pre.getTotalPrice().equals("0")) {
                    //预估价为0，显示起步价
                    if (!putong_pre.getStartPrice().equals("0")) {
                        tv_callcar_pre_amount.setText(putong_pre.getStartPrice());
                    } else {
                        tv_callcar_pre_amount.setText("10");
                    }

                } else {
                    tv_callcar_pre_amount.setText(putong_pre.getTotalPrice());
                }
                lly_callcar_precoast.setVisibility(View.VISIBLE);
                if (putong_pre.getCouponPrice().equals("0")) {
                    tv_callcar_discount_price.setVisibility(View.INVISIBLE);
                } else {
                    tv_callcar_discount_price.setVisibility(View.VISIBLE);
                    tv_callcar_discount_price.setText("行云已优惠" + putong_pre.getCouponPrice() + "元");
                }
                break;
            case R.id.imv_car_type_02:
                carType = SHUSHI;
                imv_car_type_chose.setImageResource(R.mipmap.img_carording_car_bg_02);
                if (null == shushi_pre) {
                    if (pre_flag) ToastUtils.showToast(CallCarActivity.this, "您选的地点不支持此车型");
                    return;
                }
                if (shushi_pre.getTotalPrice().equals("0")) {
                    if (!shushi_pre.getStartPrice().equals("0")) {
                        tv_callcar_pre_amount.setText(shushi_pre.getStartPrice());
                    } else {
                        tv_callcar_pre_amount.setText("12");
                    }
                } else {
                    tv_callcar_pre_amount.setText(shushi_pre.getTotalPrice());
                }
                lly_callcar_precoast.setVisibility(View.VISIBLE);

                if (shushi_pre.getCouponPrice().equals("0")) {
                    tv_callcar_discount_price.setVisibility(View.INVISIBLE);
                } else {
                    tv_callcar_discount_price.setVisibility(View.VISIBLE);
                    tv_callcar_discount_price.setText("行云已优惠" + shushi_pre.getCouponPrice() + "元");
                }

                break;
            case R.id.imv_car_type_03:
                carType = SHANGWU;
                imv_car_type_chose.setImageResource(R.mipmap.img_carording_car_bg_03);
                if (null == shangwu_pre) {
                    if (pre_flag) ToastUtils.showToast(CallCarActivity.this, "您选的地点不支持此车型");
                    return;
                }
                if (shangwu_pre.getTotalPrice().equals("0")) {
                    if (!shangwu_pre.getStartPrice().equals("0")) {
                        tv_callcar_pre_amount.setText(shangwu_pre.getStartPrice());
                    } else {
                        tv_callcar_pre_amount.setText("17");
                    }
                } else {
                    tv_callcar_pre_amount.setText(shangwu_pre.getTotalPrice());
                }
                lly_callcar_precoast.setVisibility(View.VISIBLE);

                if (shangwu_pre.getCouponPrice().equals("0")) {
                    tv_callcar_discount_price.setVisibility(View.INVISIBLE);
                } else {
                    tv_callcar_discount_price.setVisibility(View.VISIBLE);
                    tv_callcar_discount_price.setText("行云已优惠" + shangwu_pre.getCouponPrice() + "元");
                }
                break;
            case R.id.imv_car_type_04:
                carType = HAOHAU;

                imv_car_type_chose.setImageResource(R.mipmap.img_carording_car_bg_04);
                if (null == haohua_pre) {
                    if (pre_flag) ToastUtils.showToast(CallCarActivity.this, "您选的地点不支持此车型");
                    return;
                }
                if (haohua_pre.getTotalPrice().equals("0")) {
                    if (!haohua_pre.getStartPrice().equals("0")) {
                        tv_callcar_pre_amount.setText(haohua_pre.getStartPrice());
                    } else {
                        tv_callcar_pre_amount.setText("20");
                    }
                } else {
                    tv_callcar_pre_amount.setText(haohua_pre.getTotalPrice());
                }

                lly_callcar_precoast.setVisibility(View.VISIBLE);
                if (haohua_pre.getCouponPrice().equals("0")) {
                    tv_callcar_discount_price.setVisibility(View.INVISIBLE);
                } else {
                    tv_callcar_discount_price.setVisibility(View.VISIBLE);
                    tv_callcar_discount_price.setText("行云已优惠" + haohua_pre.getCouponPrice() + "元");
                }
                break;
            case R.id.lly_adress_from:
                Intent intent_from = new Intent(this, ChoseAdressActivity.class);
                intent_from.putExtra("adress", "from");
                Value value = new Value();
                if (place_from != null && !TextUtils.isEmpty(place_from.getCityName())) {
                    value.setName(place_from.getCityName());
                    value.setCityId(place_from.getCityCode());
                } else {
                    value = LocationValue;
                }
                intent_from.putExtra("value", value);
                startActivityForResult(intent_from, START_ADDR_REQUEST_CODE);
                break;
            case R.id.lly_adress_to:
                Intent intent_to = new Intent(this, ChoseAdressActivity.class);
                Value v = new Value();
                if (place_to != null && !TextUtils.isEmpty(place_to.getCityName())) {
                    v.setName(place_to.getCityName());
                    v.setCityId(place_to.getCityCode());
                } else {
                    v = LocationValue;
                }
                intent_to.putExtra("adress", "to");
                intent_to.putExtra("value", v);
                startActivityForResult(intent_to, END_ADDR_REQUEST_CODE);
                break;
        }

    }

    /**
     * 激活定位
     */
    public void activate() {
        if (locationClient == null) {
            locationClient = new AMapLocationClient(this);
            locationOption = new AMapLocationClientOption();
            locationOption.setInterval(5000);//秒定位一次
            //设置定位监听
            locationClient.setLocationListener(this);
            //设置为高精度定位模式
            //设置定位参数
            locationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            locationClient.setLocationOption(locationOption);
            // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
            // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
            // 在定位结束后，在合适的生命周期调用onDestroy()方法
            // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
            locationClient.startLocation();
        }
    }

    public void deactivate() {

        if (locationClient != null) {
            locationClient.stopLocation();
            locationClient.onDestroy();
        }
        locationClient = null;
    }


    /**
     * 获取模板信息
     */
    private void initData() {
        final LoadingDialog dialog = DialogUtil.getLoadingDialog(this, "正在获取模板信息...");
        dialog.show();
        try {
            ServiceProvider.orderTemplateService.getTempleteInfo(loginUser.getAccessToken())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<OrderTemplateGet>() {
                                   @Override
                                   public void call(OrderTemplateGet orderTemplateGet) {
                                       dialog.dismiss();
                                       if (orderTemplateGet.getStatus() == (StatusCode.RESPONSE_OK)) {
                                           deptList = orderTemplateGet.getResult().getDeparList();
                                           ruleInfoList = orderTemplateGet.getResult().getRuleList();
                                           callReasonList = orderTemplateGet.getResult().getReasonList();
                                           if (deptList != null && deptList.size() > 0) {
                                               tv_callcar_dept.setText(deptList.get(0).getDepartmentName());
                                               departmentInfo = deptList.get(0);
                                           }
                                           callReason = callReasonList.get(0);
                                           String reasonName = SPLoginUser.getReasonName(CallCarActivity.this);
                                           if (CommonUtils.hasText(reasonName) && reasonName.equals("代人叫车")) {
                                               tv_callcar_reason.setText("代人叫车");
                                               et_callcar_person.setEnabled(true);
                                               edt_callcar_cellphone.setEnabled(true);
                                           } else {
                                               tv_callcar_reason.setText(callReason.getReasonName());
                                           }
                                           et_callcar_person.setText(loginUser.getLoginName());
                                           edt_callcar_cellphone.setText(loginUser.getCellphone());
                                       } else if (orderTemplateGet.getStatus() == StatusCode.RESPONSE_ERR) {
                                           ToastUtils.showToast(CallCarActivity.this, "您离开时间太长，需要重新登陆");
                                           AppContext.getInstance().finishAllActivity();
                                           startActivity(new Intent(CallCarActivity.this, LoginActivity.class));
                                           finish();
                                       }
                                   }
                               }, new Action1<Throwable>() {
                                   @Override
                                   public void call(Throwable throwable) {
                                       dialog.dismiss();
                                       ToastUtils.showToast(CallCarActivity.this, "获取个人信息失败");
                                   }
                               }
                    );

        } catch (Exception e) {
            dialog.dismiss();
            ToastUtils.showToast(this, "获取个人信息失败");
            e.printStackTrace();
        }
    }


    private void getDeptListView(View v) {
        mContentView = LayoutInflater.from(this).inflate(R.layout.popupwindow_kind, null);
        setPopupWindow(v);
        // 类型
        ListView mKindListView = (ListView) mContentView.findViewById(R.id.kind_listview);

        deptAdapter = new DeptAdapter(this, deptList);
        mKindListView.setAdapter(deptAdapter);

        mKindListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                tv_callcar_dept.setText(deptList.get(position).getDepartmentName());
                departmentInfo = deptList.get(position);
                DeptAdapter.mSelectIndex = position;
                deptAdapter.notifyDataSetChanged();
                mPopupWindow.dismiss();
            }
        });
    }

    /**
     * 类型下拉框
     */
    private void getReasonListView(View v) {
        mContentView = LayoutInflater.from(this).inflate(R.layout.popupwindow_kind, null);
        setPopupWindow(v);
        // 类型
        ListView mKindListView = (ListView) mContentView.findViewById(R.id.kind_listview);
        reasonAdapter = new ReasonAdapter(this, callReasonList);

        mKindListView.setAdapter(reasonAdapter);

        mKindListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @TargetApi(Build.VERSION_CODES.M)
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                String reasonName = callReasonList.get(position).getReasonName();
                if (position == 3) {
                    String spreasonName = SPLoginUser.getReasonName(CallCarActivity.this);
                    if (CommonUtils.hasText(spreasonName)) {
                        SPLoginUser.clearReasonName(CallCarActivity.this);
                        SPLoginUser.setReasonName(CallCarActivity.this, reasonName);
                    }
                } else {
                    SPLoginUser.clearReasonName(CallCarActivity.this);
                }

                tv_callcar_reason.setText(reasonName);

                callReason = callReasonList.get(position);
                String forOtherFlag = callReasonList.get(position).getForOtherFlag();
                if (forOtherFlag.equals("1")) {
                    et_callcar_person.setText("");
                    edt_callcar_cellphone.setText("");
                    et_callcar_person.setEnabled(true);
                    edt_callcar_cellphone.setEnabled(true);
                } else {
                    et_callcar_person.setText(loginUser.getLoginName());
                    edt_callcar_cellphone.setText(loginUser.getCellphone());
                    edt_callcar_cellphone.clearFocus();
                    et_callcar_person.clearFocus();
                    et_callcar_person.setEnabled(false);
                    edt_callcar_cellphone.setEnabled(false);
                    CommonUtils.disInputMethod(CallCarActivity.this);
                }
                ReasonAdapter.mSelectIndex = position;
                reasonAdapter.notifyDataSetChanged();
                mPopupWindow.dismiss();
            }
        });
    }

    /**
     * 设置popupwindow的半透明背景和隐藏
     */
    private void setPopupWindow(View v) {
        mPopupWindow = new PopupWindow(mContentView, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT, true);
        mPopupWindow.setFocusable(true);
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        mPopupWindow.setBackgroundDrawable(dw);
        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = 1f;
                getWindow().setAttributes(lp);
            }
        });
        mPopupWindow.showAsDropDown(v);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            if (data == null) {
                if (place_to != null && !TextUtils.isEmpty(place_to.getAdressLat())) {
                    try {
                        lly_callcar_precoast.setVisibility(View.INVISIBLE);
                        getPreFee();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                return;
            }
            if (requestCode == START_ADDR_REQUEST_CODE) {
                from_flag = true;
                place_from = (PlaceData) data.getSerializableExtra("adress");
                if (resultCode == Activity.RESULT_OK) {
                    tv_from.setText(place_from.getDisplayName());
                }
                if (place_to != null && !TextUtils.isEmpty(place_to.getAdressLat())) {
                    try {
                        lly_callcar_precoast.setVisibility(View.INVISIBLE);
                        getPreFee();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else if (requestCode == END_ADDR_REQUEST_CODE) {
                place_to = (PlaceData) data.getSerializableExtra("adress");
                if (resultCode == Activity.RESULT_OK) {
                    tv_to.setText(place_to.getDisplayName());
                    try {
                        getPreFee();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
        } finally {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    //发起预算
    private void getPreFee() {
        try {
            Map<String, String> options = new HashMap<>();
            options.put("departureTime", DateUtil.getCurrentTime(DateUtil.YMDHMS_STYLE_TOW));
            //城市code 测试需要添加逻辑
            options.put("cityCode", place_from.getCityCode());
            options.put("departureLat", place_from.getAdressLat());
            options.put("departureLng", place_from.getAdressLng());
            options.put("destinationLat", place_to.getAdressLat());
            options.put("destinationLng", place_to.getAdressLng());
            ServiceProvider.budGetService.getBug(loginUser.getAccessToken(), options).observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<BudGetResponse>() {
                        @Override
                        public void call(BudGetResponse budGetResponse) {
                            if (budGetResponse.getStatus() == StatusCode.RESPONSE_OK) {
                                pre_flag = true;
                                putong_pre = budGetResponse.getResult().getPutong();
                                shushi_pre = budGetResponse.getResult().getShushi();
                                shangwu_pre = budGetResponse.getResult().getShangwu();
                                haohua_pre = budGetResponse.getResult().getHaohua();

                                switch (carType) {
                                    case PUTONG:
                                        if (putong_pre == null) {
                                            ToastUtils.showToast(CallCarActivity.this, "您的打车地点暂不支持该车型");
                                            return;
                                        }
                                        if (putong_pre.getTotalPrice().equals("0")) {
                                            if (putong_pre.getStartPrice().equals("0")) {
                                                tv_callcar_pre_amount.setText("10");
                                            } else {
                                                tv_callcar_pre_amount.setText(putong_pre.getStartPrice());
                                            }

                                        } else {
                                            tv_callcar_pre_amount.setText(putong_pre.getTotalPrice());
                                        }

                                        if (putong_pre.getCouponPrice().equals("0")) {
                                            tv_callcar_discount_price.setVisibility(View.INVISIBLE);
                                        } else {
                                            tv_callcar_discount_price.setVisibility(View.VISIBLE);
                                            tv_callcar_discount_price.setText("行云已优惠" + putong_pre.getCouponPrice() + "元");
                                        }
                                        break;
                                    case SHUSHI:
                                        if (shushi_pre == null) {
                                            ToastUtils.showToast(CallCarActivity.this, "您的打车地点暂不支持该车型");
                                            return;
                                        }
                                        if (shushi_pre.getTotalPrice().equals("0")) {
                                            if (shushi_pre.getStartPrice().equals("0")) {
                                                tv_callcar_pre_amount.setText("12");
                                            } else {
                                                tv_callcar_pre_amount.setText(shushi_pre.getStartPrice());
                                            }
                                        } else {
                                            tv_callcar_pre_amount.setText(shushi_pre.getTotalPrice());
                                        }

                                        if (shushi_pre.getCouponPrice().equals("0")) {
                                            tv_callcar_discount_price.setVisibility(View.INVISIBLE);
                                        } else {
                                            tv_callcar_discount_price.setVisibility(View.VISIBLE);
                                            tv_callcar_discount_price.setText("行云已优惠" + shushi_pre.getCouponPrice() + "元");
                                        }
                                        break;
                                    case SHANGWU:
                                        if (shangwu_pre == null) {
                                            ToastUtils.showToast(CallCarActivity.this, "您的打车地点暂不支持该车型");
                                            return;
                                        }
                                        if (shangwu_pre.getTotalPrice().equals("0")) {
                                            if (shangwu_pre.getStartPrice().equals("0")) {
                                                tv_callcar_pre_amount.setText("17");

                                            } else {
                                                tv_callcar_pre_amount.setText(shangwu_pre.getStartPrice());
                                            }
                                        } else {
                                            tv_callcar_pre_amount.setText(shangwu_pre.getTotalPrice());
                                        }
                                        if (shangwu_pre.getCouponPrice().equals("0")) {
                                            tv_callcar_discount_price.setVisibility(View.INVISIBLE);
                                        } else {
                                            tv_callcar_discount_price.setVisibility(View.VISIBLE);
                                            tv_callcar_discount_price.setText("行云已优惠" + shangwu_pre.getCouponPrice() + "元");
                                        }
                                        break;
                                    case HAOHAU:
                                        if (haohua_pre == null) {
                                            ToastUtils.showToast(CallCarActivity.this, "您的打车地点暂不支持该车型");
                                            return;
                                        }
                                        if (haohua_pre.getTotalPrice().equals("0")) {
                                            if (haohua_pre.getStartPrice().equals("0")) {
                                                tv_callcar_pre_amount.setText("20");
                                            } else {
                                                tv_callcar_pre_amount.setText(haohua_pre.getStartPrice());
                                            }
                                        } else {
                                            tv_callcar_pre_amount.setText(haohua_pre.getTotalPrice());
                                        }
                                        tv_callcar_pre_amount.setText(haohua_pre.getTotalPrice());

                                        if (haohua_pre.getCouponPrice().equals("0")) {
                                            tv_callcar_discount_price.setVisibility(View.INVISIBLE);
                                        } else {
                                            tv_callcar_discount_price.setVisibility(View.VISIBLE);
                                            tv_callcar_discount_price.setText("行云已优惠" + haohua_pre.getCouponPrice() + "元");
                                        }
                                        break;
                                }
                                lly_callcar_precoast.setVisibility(View.VISIBLE);
                            } else if (budGetResponse.getStatus() == StatusCode.RESPONSE_ERR) {
                                ToastUtils.showToast(CallCarActivity.this, "您离开时间太长，需要重新登陆");
                                AppContext.getInstance().finishAllActivity();
                                startActivity(new Intent(CallCarActivity.this, LoginActivity.class));
                                finish();
                            }
                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {
                            throwable.printStackTrace();
                            if (throwable instanceof ConnectException || throwable instanceof RetrofitError) {
                                ToastUtils.showToast(CallCarActivity.this, "网络异常，无法连接服务器");
                                return;
                            }
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //下订单
    private void sendOrder() {
        final LoadingDialog dialog = DialogUtil.getLoadingDialog(CallCarActivity.this, "正在下单...");
        try {
            Map<String, String> map = new HashMap<>();
            switch (carType) {
                case PUTONG:
                    map.put("carServiceCode", putong_pre.getCarServiceCode());
                    carKind = putong_pre.getCarServiceName();
                    break;
                case SHUSHI:
                    map.put("carServiceCode", shushi_pre.getCarServiceCode());
                    carKind = shushi_pre.getCarServiceName();
                    break;
                case SHANGWU:
                    carKind = shangwu_pre.getCarServiceName();
                    map.put("carServiceCode", shangwu_pre.getCarServiceCode());
                    break;
                case HAOHAU:
                    map.put("carServiceCode", haohua_pre.getCarServiceCode());
                    carKind = haohua_pre.getCarServiceName();
                    break;
            }
            //默认实时0,预约1
            map.put("orderType", "0");
            //map.put("orderType", "1");
            map.put("departureTime", DateUtil.getCurrentTime(DateUtil.YMDHMS_STYLE_TOW));
            map.put("callerPhone", loginUser.getCellphone());
            map.put("passengerPhone", edt_callcar_cellphone.getText().toString());
            map.put("passengerName", et_callcar_person.getText().toString());
            map.put("cityName", place_from.getCityName());
            map.put("cityCode", place_from.getCityCode());
            map.put("departureName", place_from.getDisplayName());
            map.put("departureLat", place_from.getAdressLat());
            map.put("departureLng", place_from.getAdressLng());
            map.put("destinationName", place_to.getDisplayName());
            map.put("destinationLat", place_to.getAdressLat());
            map.put("destinationLng", place_to.getAdressLng());
            map.put("submitTime", DateUtil.getCurrentTime(DateUtil.YMDHMS_STYLE_TOW));
            map.put("departmentIdx", departmentInfo.getDepartmentIdx());
            map.put("departmentName", departmentInfo.getDepartmentName());
            map.put("reasonIndex", callReason.getReasonIndex());
            map.put("reasonName", callReason.getReasonName());
            map.put("estimtePrice", tv_callcar_pre_amount.getText().toString());
            dialog.show();
            ServiceProvider.carOrderService.createOrder(loginUser.getAccessToken(), map).observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<OrderCarResponse>() {
                        @Override
                        public void call(OrderCarResponse orderCarResponse) {
                            btn_callcar.setEnabled(true);
                            dialog.dismiss();
                            if (orderCarResponse.getStatus() == StatusCode.RESPONSE_OK) {
                                SPLoginUser.setOrderId(CallCarActivity.this, orderCarResponse.getResult().getOrderID());
                                String orderStatus = orderCarResponse.getResult().getOrderStatus();
                                if (orderStatus.equals("300")) {
                                    Intent intent = new Intent(CallCarActivity.this, SendingCarActivity.class);
                                    intent.putExtra("orderresult", orderCarResponse.getResult());
                                    intent.putExtra("cartype", carKind);
                                    intent.putExtra("pre_coast", tv_callcar_pre_amount.getText().toString());
                                    intent.putExtra("departureName", tv_callcar_dept.getText().toString());
                                    intent.putExtra("callreason", tv_callcar_reason.getText().toString());
                                    intent.putExtra("passenger", et_callcar_person.getText().toString());
                                    startActivity(intent);
                                }

                            } else if (orderCarResponse.getStatus() == StatusCode.RESPONSE_ERR) {
                                ToastUtils.showToast(CallCarActivity.this, "您离开时间太长，需要重新登陆");
                                AppContext.getInstance().finishAllActivity();
                                startActivity(new Intent(CallCarActivity.this, LoginActivity.class));
                                finish();
                            } else if (orderCarResponse.getStatus() == 4203) {
                                dialog.dismiss();
                                ToastUtils.showToast(CallCarActivity.this, orderCarResponse.getMessage());
                            } else {
                                dialog.dismiss();
                                ToastUtils.showToast(CallCarActivity.this, orderCarResponse.getMessage());
                            }

                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {
                            throwable.printStackTrace();
                            btn_callcar.setEnabled(true);
                            dialog.dismiss();
                            if (throwable instanceof ConnectException || throwable instanceof RetrofitError) {
                                ToastUtils.showToast(CallCarActivity.this, "网络异常，无法连接服务器");
                                return;
                            }
                        }
                    });

        } catch (Exception e) {
            e.printStackTrace();
            dialog.dismiss();
            btn_callcar.setEnabled(true);
            ToastUtils.showToast(CallCarActivity.this, "叫车失败，请检查打车信息并重新下单");
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        deactivate();
        cityDbManager.closeDB();
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
            CommonUtils.disInputMethod(CallCarActivity.this);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        SPLoginUser.clearReasonName(CallCarActivity.this);
    }
}
