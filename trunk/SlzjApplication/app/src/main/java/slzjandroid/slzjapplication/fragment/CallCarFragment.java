package slzjandroid.slzjapplication.fragment;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import slzjandroid.slzjapplication.R;
import slzjandroid.slzjapplication.activity.ChoseAdressActivity;
import slzjandroid.slzjapplication.adapter.DeptAdapter;
import slzjandroid.slzjapplication.adapter.ReasonAdapter;
import slzjandroid.slzjapplication.customView.LoadingDialog;
import slzjandroid.slzjapplication.customView.NavigationView;
import slzjandroid.slzjapplication.dto.BudGetResponse;
import slzjandroid.slzjapplication.dto.CallReason;
import slzjandroid.slzjapplication.dto.CarServiceType;
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
import slzjandroid.slzjapplication.helper.SPLoginUser;
import slzjandroid.slzjapplication.lang.StatusCode;
import slzjandroid.slzjapplication.service.ServiceProvider;
import slzjandroid.slzjapplication.utils.CommonUtils;
import slzjandroid.slzjapplication.utils.DateUtil;
import slzjandroid.slzjapplication.utils.DialogUtil;
import slzjandroid.slzjapplication.utils.ToastUtils;

/**
 * Created by hdb on 2016/3/21.
 */
public class CallCarFragment extends BaseFragment implements NavigationView.ClickCallback, AMapLocationListener, View.OnClickListener {
    private static final double EARTH_RADIUS = 6378137;

    private AMapLocationClient locationClient = null;
    private AMapLocationClientOption locationOption = null;
    private TextView tv_from, tv_callcar_dept, tv_callcar_reason;
    private ImageView imv_car_type_01, imv_car_type_02, imv_car_type_03, imv_car_type_04, imv_car_type_chose;
    private TextView tv_to, tv_callcar_pre_amount, tv_callcar_discount_price;
    private ImageView imv_main_icon;
    private EditText et_callcar_person;
    private EditText edt_callcar_cellphone;
    private Button btn_callcar;
    private LinearLayout lly_callcar_dept, lly_adress_from, lly_adress_to;
    private LinearLayout lly_callcar_reason, lly_callcar_precoast;
    private List<DepartmentInfo> deptList = null;
    DepartmentInfo departmentInfo = null;
    private List<RuleInfo> ruleInfoList = null;
    //  private RuleInfo ruleInfoList = null;
    private List<CallReason> callReasonList = null;
    private CallReason callReason = null;
    private List<CarServiceType> carServiceTypeList = null;
    private View mContentView;
    private PopupWindow mPopupWindow;
    private String forOtherFlag;
    private LoginUser loginUser;
    final int START_ADDR_REQUEST_CODE = 100;
    final int END_ADDR_REQUEST_CODE = 101;
    private PlaceData place_from = new PlaceData();
    private Boolean from_flag = false;
    private PlaceData place_to = null;

    private String departureLat = "";
    private String departureLng = "";
    private String destinationLat = "";
    private String destinationLng = "";
    private CarType_putong putong_pre;
    private CarType_shushi shushi_pre;
    private CarType_shangwu shangwu_pre;
    private CarType_haohua haohua_pre;
    private String cityName = "北京市";
    private String departureTime = "";
    private String cityCode = "1";
    private int carType = 1;
    private String carKind = "";
    private final int PUTONG = 1;
    private final int SHUSHI = 2;
    private final int SHANGWU = 3;
    private final int HAOHAU = 4;
    private Context mContext;

    private LinearLayout layout;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_callcar;
    }

    @Override
    protected void findViews() {
        layout = (LinearLayout) findViewById(R.id.layout);
        tv_callcar_dept = (TextView) findViewById(R.id.tv_callcar_dept);
        tv_callcar_reason = (TextView) findViewById(R.id.tv_callcar_reason);

        et_callcar_person = (EditText) findViewById(R.id.edt_callcar_person);
        et_callcar_person.clearFocus();
        edt_callcar_cellphone = (EditText) findViewById(R.id.edt_callcar_cellphone);
        edt_callcar_cellphone.clearFocus();

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
    protected void bindViews() {
        layout.setOnClickListener(this);
        btn_callcar.setOnClickListener(this);
        lly_callcar_dept.setOnClickListener(this);
        lly_callcar_reason.setOnClickListener(this);
        imv_car_type_01.setOnClickListener(this);
        imv_car_type_02.setOnClickListener(this);
        imv_car_type_03.setOnClickListener(this);
        imv_car_type_04.setOnClickListener(this);
        lly_adress_from.setOnClickListener(this);
        lly_adress_to.setOnClickListener(this);
    }

    @Override
    protected void init() {
        loginUser = LoginUser.getUser();
        mContext = getActivity();

        NavigationView navigationView = (NavigationView) super.findViewById(R.id.nav_main);
        navigationView.getBackView().setImageResource(R.drawable.selector_home_bg);
        navigationView.setTitle("企业用车");
        navigationView.setRightViewIsShow(false);
        navigationView.setClickCallback(this);
        activate();
        initData();
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {

        if (aMapLocation != null) {
            Log.i("initStartAddress", aMapLocation.getCityCode() + aMapLocation.getCity());
            if (from_flag == false) {
                if (aMapLocation.getCity().equals("北京市")) {

                    departureLat = aMapLocation.getLatitude() + "";
                    departureLng = aMapLocation.getLongitude() + "";
                    tv_from.setText(aMapLocation.getAddress());
                    place_from.setDisplayName(aMapLocation.getAddress());
                    place_from.setCityCode("1");
                    place_from.setAdressLat(departureLat);
                    place_from.setAdressLng(departureLng);
                } else {
                    place_from.setDisplayName("");
                }

            }

        } else {
            Log.v("定位失败", "哈哈哈");
        }

    }

    /**
     * 激活定位
     */
    public void activate() {
        if (locationClient == null) {
            locationClient = new AMapLocationClient(getActivity());
            locationOption = new AMapLocationClientOption();
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

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        deactivate();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imv_main_icon:
                ((Activity) mContext).getFragmentManager().popBackStack();
                break;
            case R.id.btn_callcar:
                //下单前检查信息完整性
                if (callReason == null || departmentInfo == null || place_from.getAdressLat().equals("") || place_to == null) {
                    ToastUtils.showToast(mContext, "请检查打车信息并保证完整");
                    return;
                }
                if (place_from.getDisplayName().equals(place_to.getDisplayName())) {
                    ToastUtils.showToast(mContext, "出发地和目的地不能相同");
                    return;

                }
                Double from_lat = Double.parseDouble(place_from.getAdressLat());
                Double from_lng = Double.parseDouble(place_from.getAdressLng());
                Double to_lat = Double.parseDouble(place_to.getAdressLat());
                Double to_lng = Double.parseDouble(place_to.getAdressLng());
                Double distance = DistanceOfTwoPoints(from_lat, from_lng, to_lat, to_lng);
                //规则判断
                //
//                RuleInfo ruleInfo0 = new RuleInfo();
//                RuleInfo ruleInfo1 = new RuleInfo();
//
                for (int i = 0; i < ruleInfoList.size(); i++) {
                    //限制出发地
                    if (ruleInfoList.get(i).getRuleType().equals("0")) {
                        Double aDouble = DistanceOfTwoPoints(Double.parseDouble(ruleInfoList.get(i).getYAxis()), Double.parseDouble(ruleInfoList.get(i).getXAxis()), Double.parseDouble(place_from.getAdressLat()), Double.parseDouble(place_from.getAdressLng()));
                        if (aDouble > 500) {
                            ToastUtils.showToast(mContext, "您的用车地址范围在" + ruleInfoList.get(i).getConpouName() + "500米内，请确认上车地点");
                            Log.i("'出发地距离", aDouble + "");
                            return;
                        }
                    } else if (ruleInfoList.get(i).getRuleType().equals("1")) {
                        //限制目的地
                        Double aDouble = DistanceOfTwoPoints(Double.parseDouble(ruleInfoList.get(i).getYAxis()), Double.parseDouble(ruleInfoList.get(i).getXAxis()), Double.parseDouble(place_to.getAdressLat()), Double.parseDouble(place_to.getAdressLng()));
                        if (aDouble > 500) {
                            ToastUtils.showToast(mContext, "您的用车地址范围在" + ruleInfoList.get(i).getConpouName() + "500米内，请确认下车地点");
                            Log.i("'目的地距离", aDouble + "");
                            return;
                        }
                    } else if (ruleInfoList.get(i).getRuleType().equals("2")) {

                    }
                }

                if (distance > 100000) {
                    ToastUtils.showToast(mContext, "暂不支持远距离乘车，请修改出发地或目的地");
                    return;
                }
                sendOrder();
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
                if (putong_pre == null) return;
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

                tv_callcar_discount_price.setText("行云已优惠" + putong_pre.getUnitPrice() + "元");
                break;
            case R.id.imv_car_type_02:
                carType = SHUSHI;
                imv_car_type_chose.setImageResource(R.mipmap.img_carording_car_bg_02);
                if (null == shushi_pre) return;

                if (!shushi_pre.getStartPrice().equals("0")) {
                    tv_callcar_pre_amount.setText(shushi_pre.getStartPrice());
                } else {
                    tv_callcar_pre_amount.setText("12");
                }
                tv_callcar_pre_amount.setText(shushi_pre.getTotalPrice());
                tv_callcar_discount_price.setText("行云已优惠" + shushi_pre.getUnitPrice() + "元");
                break;
            case R.id.imv_car_type_03:
                carType = SHANGWU;

                imv_car_type_chose.setImageResource(R.mipmap.img_carording_car_bg_03);
                if (null == shangwu_pre) return;
                if (!shangwu_pre.getStartPrice().equals("0")) {
                    tv_callcar_pre_amount.setText(shangwu_pre.getStartPrice());
                } else {
                    tv_callcar_pre_amount.setText("17");
                }
                tv_callcar_pre_amount.setText(shangwu_pre.getTotalPrice());
                tv_callcar_discount_price.setText("行云已优惠" + shangwu_pre.getUnitPrice() + "元");
                break;
            case R.id.imv_car_type_04:
                carType = HAOHAU;

                imv_car_type_chose.setImageResource(R.mipmap.img_carording_car_bg_04);
                if (null == haohua_pre) return;
                if (!haohua_pre.getStartPrice().equals("0")) {
                    tv_callcar_pre_amount.setText(haohua_pre.getStartPrice());
                } else {
                    tv_callcar_pre_amount.setText("20");
                }
                tv_callcar_pre_amount.setText(haohua_pre.getTotalPrice());
                tv_callcar_discount_price.setText("行云已优惠" + haohua_pre.getUnitPrice() + "元");
                break;
            case R.id.lly_adress_from:
                Intent intent_from = new Intent(mContext, ChoseAdressActivity.class);
                intent_from.putExtra("adress", "from");
                startActivityForResult(intent_from, START_ADDR_REQUEST_CODE);
                break;
            case R.id.lly_adress_to:
                Intent intent_to = new Intent(mContext, ChoseAdressActivity.class);
                intent_to.putExtra("adress", "to");
                startActivityForResult(intent_to, END_ADDR_REQUEST_CODE);
                break;
            case R.id.layout:
//                InputMethodManager imm = (InputMethodManager)
//                        getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
//                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                break;
        }
    }

    /**
     * 获取打车模板
     */
    private void initData() {
        LoginUser user = LoginUser.getUser();
        try {
            ServiceProvider.orderTemplateService.getTempleteInfo(user.getAccessToken())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<OrderTemplateGet>() {
                                   @Override
                                   public void call(OrderTemplateGet orderTemplateGet) {
                                       if (orderTemplateGet.getStatus() == (StatusCode.RESPONSE_OK)) {
                                           deptList = orderTemplateGet.getResult().getDeparList();
                                           ruleInfoList = orderTemplateGet.getResult().getRuleList();
                                           callReasonList = orderTemplateGet.getResult().getReasonList();
                                           carServiceTypeList = orderTemplateGet.getResult().getCarServiceList();
                                           if (deptList != null && deptList.size() > 0) {
                                               tv_callcar_dept.setText(deptList.get(0).getDepartmentName());
                                               departmentInfo = deptList.get(0);
                                           }
                                           callReason = callReasonList.get(0);
                                           tv_callcar_reason.setText(callReason.getReasonName());
                                           et_callcar_person.setText(loginUser.getLoginName());
                                           edt_callcar_cellphone.setText(loginUser.getCellphone());

                                       }
                                   }
                               }, new Action1<Throwable>() {
                                   @Override
                                   public void call(Throwable throwable) {
                                       Log.e("getTem", "模板获取失败：" + throwable.getMessage(), throwable);
                                       ToastUtils.showToast(mContext, "获取个人信息失败");
                                   }
                               }
                    );

        } catch (Exception e) {
            ToastUtils.showToast(mContext, "获取个人信息失败");
            e.printStackTrace();
        }
    }

    private ReasonAdapter reasonAdapter;
    private DeptAdapter deptAdapter;

    private void getDeptListView(View v) {
        mContentView = LayoutInflater.from(mContext).inflate(
                R.layout.popupwindow_kind, null);
        setPopupWindow(mContentView, v);

        // 类型
        ListView mKindListView = (ListView) mContentView
                .findViewById(R.id.kind_listview);

        deptAdapter = new DeptAdapter(mContext, deptList);

        mKindListView.setAdapter(deptAdapter);

        mKindListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                tv_callcar_dept.setText(deptList.get(position).getDepartmentName());
                departmentInfo = deptList.get(position);
                deptAdapter.mSelectIndex = position;
                deptAdapter.notifyDataSetChanged();
                mPopupWindow.dismiss();


            }
        });
    }

    /**
     * 类型下拉框
     */
    private void getReasonListView(View v) {
        mContentView = LayoutInflater.from(mContext).inflate(
                R.layout.popupwindow_kind, null);
        setPopupWindow(mContentView, v);

        // 类型
        ListView mKindListView = (ListView) mContentView
                .findViewById(R.id.kind_listview);

        reasonAdapter = new ReasonAdapter(mContext, callReasonList);

        mKindListView.setAdapter(reasonAdapter);

        mKindListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @TargetApi(Build.VERSION_CODES.M)
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                tv_callcar_reason.setText(callReasonList.get(position).getReasonName());
                callReason = callReasonList.get(position);
                forOtherFlag = callReasonList.get(position).getForOtherFlag();
                if (forOtherFlag.equals("1")) {
                    et_callcar_person.setText("");
                    edt_callcar_cellphone.setText("");
                    et_callcar_person.setEnabled(true);
                    edt_callcar_cellphone.setEnabled(true);
                } else {
                    et_callcar_person.setText(loginUser.getLoginName());
                    edt_callcar_cellphone.setText(loginUser.getCellphone());
                    et_callcar_person.setEnabled(false);
                    edt_callcar_cellphone.setEnabled(false);

                }
                reasonAdapter.mSelectIndex = position;
                reasonAdapter.notifyDataSetChanged();
                mPopupWindow.dismiss();


            }
        });
    }

    /**
     * 设置popupwindow的半透明背景和隐藏
     */
    private void setPopupWindow(View mContentView2, View v) {
        mPopupWindow = new PopupWindow(mContentView, LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT, true);
        mPopupWindow.setFocusable(true);
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        mPopupWindow.setBackgroundDrawable(dw);
        mPopupWindow.setOnDismissListener(new OnDismissListener() {

            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = ((Activity) mContext).getWindow().getAttributes();
                lp.alpha = 1f;
                ((Activity) mContext).getWindow().setAttributes(lp);
            }
        });
        mPopupWindow.showAsDropDown(v);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        CommonUtils.disInputMethod(getActivity());
        try {
            if (data == null) {
                if (place_to != null) {
                    try {
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
                departureLat = place_from.getAdressLat();
                departureLng = place_from.getAdressLng();
                cityCode = place_from.getCityCode();
                Log.i("citycode", cityCode);
                cityName = ChoseAdressActivity.cityName;
                if (resultCode == Activity.RESULT_OK) {
                    tv_from.setText(place_from.getDisplayName());
                }
                if (place_to != null) {
                    try {
                        getPreFee();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else if (requestCode == END_ADDR_REQUEST_CODE) {
                place_to = (PlaceData) data.getSerializableExtra("adress");
                destinationLat = place_to.getAdressLat();
                destinationLng = place_to.getAdressLng();
                if (resultCode == Activity.RESULT_OK) {
                    tv_to.setText(place_to.getDisplayName());
                    try {
                        getPreFee();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
            // updateEstimatePrice();
        } finally {
            super.onActivityResult(requestCode, resultCode, data);
        }

    }

    //发起预算
    private void getPreFee() {
        Map<String, String> options = new HashMap<>();
        departureTime = DateUtil.getCurrentTime(DateUtil.YMDHMS_STYLE_TOW);
        options.put("departureTime", DateUtil.getCurrentTime(DateUtil.YMDHMS_STYLE_TOW));
        //城市code 测试需要添加逻辑
        options.put("cityCode", cityCode);
        options.put("departureLat", departureLat);
        options.put("departureLng", departureLng);
        options.put("destinationLat", destinationLat);
        options.put("destinationLng", destinationLng);
        ServiceProvider.budGetService.getBug(loginUser.getAccessToken(), options).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<BudGetResponse>() {
                    @Override
                    public void call(BudGetResponse budGetResponse) {
                        if (budGetResponse.getStatus() == StatusCode.RESPONSE_OK) {
                            putong_pre = budGetResponse.getResult().getPutong();
                            shushi_pre = budGetResponse.getResult().getShushi();
                            shangwu_pre = budGetResponse.getResult().getShangwu();
                            haohua_pre = budGetResponse.getResult().getHaohua();
                            lly_callcar_precoast.setVisibility(View.VISIBLE);
                            switch (carType) {
                                case PUTONG:
                                    if (putong_pre.getTotalPrice().equals("0")) {
                                        if (putong_pre.getStartPrice().equals("0")) {
                                            tv_callcar_pre_amount.setText("10");
                                        } else {
                                            tv_callcar_pre_amount.setText(putong_pre.getStartPrice());
                                        }

                                    } else {
                                        tv_callcar_pre_amount.setText(putong_pre.getTotalPrice());
                                    }

                                    tv_callcar_discount_price.setText("行云已优惠" + putong_pre.getUnitPrice() + "元");
                                    break;
                                case SHUSHI:
                                    if (shushi_pre.getTotalPrice().equals("0")) {
                                        if (shushi_pre.getStartPrice().equals("0")) {
                                            tv_callcar_pre_amount.setText("12");
                                        } else {
                                            tv_callcar_pre_amount.setText(shushi_pre.getStartPrice());
                                        }
                                    } else {
                                        tv_callcar_pre_amount.setText(shushi_pre.getTotalPrice());
                                    }
                                    tv_callcar_discount_price.setText("行云已优惠" + shushi_pre.getUnitPrice() + "元");
                                    break;
                                case SHANGWU:
                                    if (shangwu_pre.getTotalPrice().equals("0")) {
                                        if (shangwu_pre.getStartPrice().equals("0")) {
                                            tv_callcar_pre_amount.setText("17");

                                        } else {
                                            tv_callcar_pre_amount.setText(shangwu_pre.getStartPrice());
                                        }
                                    } else {
                                        tv_callcar_pre_amount.setText(shangwu_pre.getTotalPrice());
                                    }

                                    tv_callcar_discount_price.setText("行云已优惠" + shangwu_pre.getUnitPrice() + "元");
                                    break;
                                case HAOHAU:
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
                                    tv_callcar_discount_price.setText("行云已优惠" + haohua_pre.getUnitPrice() + "元");
                                    break;
                            }
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                    }
                });
    }

    //下订单
    private void sendOrder() {
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
            map.put("departureTime", DateUtil.getCurrentTime(DateUtil.YMDHMS_STYLE_TOW));
            map.put("callerPhone", loginUser.getCellphone());
            map.put("passengerPhone", edt_callcar_cellphone.getText().toString());
            map.put("passengerName", et_callcar_person.getText().toString());
            map.put("cityName", cityName);
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

            final LoadingDialog dialog = DialogUtil.getLoadingDialog(mContext, "正在下单...");
            dialog.show();

            ServiceProvider.carOrderService.createOrder(loginUser.getAccessToken(), map).observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<OrderCarResponse>() {
                        @Override
                        public void call(OrderCarResponse orderCarResponse) {
                            dialog.dismiss();
                            if (orderCarResponse.getStatus() == StatusCode.RESPONSE_OK) {

                           SPLoginUser.setOrderId(getActivity(), orderCarResponse.getResult().getOrderID());

                                String orderStatus = orderCarResponse.getResult().getOrderStatus();
                                if (orderStatus.equals("300")) {
                                    //进入等待界面
                                    //SendingCarFragment sendingCarFragment = SendingCarFragment.newInstance();
                                    SendingCarFragment sendingCarFragment = new SendingCarFragment();
                                    Bundle bundle = new Bundle();
                                    bundle.putSerializable("orderresult", orderCarResponse.getResult());
                                    bundle.putString("cartype", carKind);
                                    bundle.putString("pre_coast", tv_callcar_pre_amount.getText().toString());
                                    bundle.putString("departureName", tv_callcar_dept.getText().toString());
                                    bundle.putString("callreason", tv_callcar_reason.getText().toString());
                                    bundle.putString("passenger", et_callcar_person.getText().toString());
                                    sendingCarFragment.setArguments(bundle);
                                    FragmentManager fm = getFragmentManager();
                                    FragmentTransaction tx = fm.beginTransaction();
                                    tx.replace(R.id.container, sendingCarFragment);
                                    tx.addToBackStack(null);
                                    tx.commitAllowingStateLoss();
                                }

                            } else if (orderCarResponse.getStatus() == 4203) {
                                ToastUtils.showToast(mContext, orderCarResponse.getMessage());
                            } else {
                                ToastUtils.showToast(mContext, "叫车失败，请检查打车信息并重新下单");
                            }

                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {
                            throwable.printStackTrace();
                            dialog.dismiss();
                        }
                    });

        } catch (Exception e) {
            e.printStackTrace();
            ToastUtils.showToast(mContext, "叫车失败，请检查打车信息并重新下单");
        }
    }

    private static double rad(double d) {
        return d * Math.PI / 180.0;

    }

    /**
     * 8.     * 根据两点间经纬度坐标（double值），计算两点间距离，
     * 9.     *
     * 10.     * @param lat1
     * 11.     * @param lng1
     * 12.     * @param lat2
     * 13.     * @param lng2
     * 14.     * @return 距离：单位为米
     * 15.
     */
    public double DistanceOfTwoPoints(double lat1, double lng1,
                                      double lat2, double lng2) {
        double radLat1 = rad(lat1);
        double radLat2 = rad(lat2);
        double a = radLat1 - radLat2;
        double b = rad(lng1) - rad(lng2);
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
                + Math.cos(radLat1) * Math.cos(radLat2)
                * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
        s = Math.round(s * 10000) / 10000;
        return s;
    }


    @Override
    public void onBackClick() {
        ((Activity) mContext).getFragmentManager().popBackStack();

    }

    @Override
    public void onRightClick() {

    }
}
