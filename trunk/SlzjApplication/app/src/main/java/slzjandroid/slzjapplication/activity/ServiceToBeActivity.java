package slzjandroid.slzjapplication.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.Polyline;
import com.amap.api.maps.model.PolylineOptions;

import java.net.ConnectException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import slzjandroid.slzjapplication.R;
import slzjandroid.slzjapplication.context.AppContext;
import slzjandroid.slzjapplication.customView.LoadingDialog;
import slzjandroid.slzjapplication.customView.NavigationView;
import slzjandroid.slzjapplication.dto.LoginUser;
import slzjandroid.slzjapplication.dto.OrderCancelResponse;
import slzjandroid.slzjapplication.dto.OrderCarResponse;
import slzjandroid.slzjapplication.dto.OrderDetailInfo;
import slzjandroid.slzjapplication.helper.SPLoginUser;
import slzjandroid.slzjapplication.lang.StatusCode;
import slzjandroid.slzjapplication.service.ServiceProvider;
import slzjandroid.slzjapplication.utils.DialogUtil;
import slzjandroid.slzjapplication.utils.MapUtils;
import slzjandroid.slzjapplication.utils.ToastUtils;

/**
 * Created by hdb on 2016/4/5.
 */
public class ServiceToBeActivity extends BasicActivity implements View.OnClickListener, NavigationView.ClickCallback, AMap.CancelableCallback, LocationSource,
        AMapLocationListener, RadioGroup.OnCheckedChangeListener {
    private ImageView imv_service_driver_cellphone, imv_service_tobe_up_down;
    private TextView tv_service_tobe_deiver_name, tv_driver_number_plate, tv_service_tobe_car_kind,
            tv_service_tobe_passenger_cellphone, tv_service_tobe_coast_dept,
            tv_service_tobe_reason, tv_service_tobe_from, tv_service_tobe_to, tv_service_tobe_calltime,
            tv_service_tobe_accept_time, tv_sevice_to_be_status;
    private Timer pollingTimer = new Timer();
    private LinearLayout lly_service_tobe_details;
    private LoginUser loginUser = null;
    private PollingTask pollingTask;
    private OrderDetailInfo orderResult = null;
    private Context mContext;


    //地图需要
    private MapView mMapView;
    private AMap mAmap;
    private Polyline mVirtureRoad;
    private Marker mMoveMarker;
    private double z;
    private double y;


    private LocationSource.OnLocationChangedListener mListener;
    private AMapLocationClient mlocationClient;
    private AMapLocationClientOption mLocationOption;

    // 通过设置间隔时间和距离可以控制速度和图标移动的距离
    private static final int TIME_INTERVAL = 45;
    private static final double DISTANCE = 0.00045;
    TextView rightView;

    @Override
    protected int getLayoutID() {
        return R.layout.fragment_services_to_be;
    }

    @Override
    protected void findViews() {
        imv_service_driver_cellphone = (ImageView) findViewById(R.id.imv_service_driver_cellphone);
        imv_service_tobe_up_down = (ImageView) findViewById(R.id.imv_service_tobe_up_down);
        tv_service_tobe_deiver_name = (TextView) findViewById(R.id.tv_service_tobe_deiver_name);
        tv_driver_number_plate = (TextView) findViewById(R.id.tv_driver_number_plate);
        tv_service_tobe_car_kind = (TextView) findViewById(R.id.tv_service_tobe_car_kind);
        tv_service_tobe_passenger_cellphone = (TextView) findViewById(R.id.tv_service_tobe_passenger_cellphone);
        tv_service_tobe_coast_dept = (TextView) findViewById(R.id.tv_service_tobe_coast_dept);
        tv_service_tobe_reason = (TextView) findViewById(R.id.tv_service_tobe_reason);
        tv_service_tobe_from = (TextView) findViewById(R.id.tv_service_tobe_from);
        tv_service_tobe_to = (TextView) findViewById(R.id.tv_service_tobe_to);
        tv_service_tobe_calltime = (TextView) findViewById(R.id.tv_service_tobe_calltime);
        tv_service_tobe_accept_time = (TextView) findViewById(R.id.tv_service_tobe_accept_time);
        lly_service_tobe_details = (LinearLayout) findViewById(R.id.lly_service_tobe_details);
        tv_sevice_to_be_status = (TextView) findViewById(R.id.tv_sevice_to_be_status);
        loginUser = LoginUser.getUser();
        this.pollingTask = new PollingTask();
    }

    @Override
    protected void bindViews() {
        imv_service_driver_cellphone.setOnClickListener(this);
        imv_service_tobe_up_down.setOnClickListener(this);
        startPolling();
    }

    @Override
    protected void init() {
        AppContext.getInstance().addActivity(this);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_main);
        navigationView.setTitle("订单详情");
        navigationView.setRightViewIsShow(true);
        navigationView.setRightTile("取消订单");
        navigationView.setClickCallback(this);
        rightView = navigationView.getRightView();
        //获取地图
        mMapView = (MapView) findViewById(R.id.map);
        mMapView.onCreate(getBundle());
        mAmap = mMapView.getMap();
        setUpMap();

        mContext = ServiceToBeActivity.this;
        orderResult = (OrderDetailInfo) getIntent().getSerializableExtra("orderresult");
        tv_service_tobe_deiver_name.setText(orderResult.getDriverName());
        tv_driver_number_plate.setText(orderResult.getDriverCard());
        tv_service_tobe_car_kind.setText(orderResult.getDriverCarType());
        tv_service_tobe_passenger_cellphone.setText(orderResult.getPassengerName() + "\t" + orderResult.getPassengerPhone());
        tv_service_tobe_coast_dept.setText(orderResult.getDeptName());
        tv_service_tobe_reason.setText(orderResult.getReasonName());
        tv_service_tobe_from.setText(orderResult.getDepartureName());
        tv_service_tobe_to.setText(orderResult.getDestinationName());
        tv_service_tobe_calltime.setText(orderResult.getOrderTime());
        tv_service_tobe_accept_time.setText(orderResult.getBeginChargeTime());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imv_service_driver_cellphone:
                try {
                    if (orderResult.getDriverPhone() == null) return;
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + orderResult.getDriverPhone()));
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling

                        ServiceToBeActivity.this.startActivity(intent);
                    } else {
                        ToastUtils.showToast(ServiceToBeActivity.this, "钱包行云需要拨打电话权限");
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;
            case R.id.imv_service_tobe_up_down:
                if (lly_service_tobe_details.getVisibility() == View.VISIBLE) {
                    lly_service_tobe_details.setVisibility(View.GONE);
                    imv_service_tobe_up_down.setImageResource(R.mipmap.img_sendcar_down);
                } else {
                    lly_service_tobe_details.setVisibility(View.VISIBLE);
                    imv_service_tobe_up_down.setImageResource(R.mipmap.img_sendcar_arrow_up);
                }
                break;
        }
    }

    //获取订单状态
    private void getOrderStatus() {
        try {

            Map<String, String> options = new HashMap<>();
            options.put("access_token", loginUser.getAccessToken());
            options.put("orderID", SPLoginUser.getOrderId(this));
            ServiceProvider.carOrderService.getOrderStatus(options, new Callback<OrderCarResponse>() {
                @Override
                public void success(OrderCarResponse orderResult, Response response) {
                    if (orderResult.getStatus() == StatusCode.RESPONSE_OK) {

                        String orderStatus = orderResult.getResult().getOrderStatus();
                        if (orderStatus.equals("400") || orderStatus.equals("410")) {
                            //跳转等待司机页
                            String currentLat = orderResult.getResult().getCurrentLat();
                            String currentLng = orderResult.getResult().getCurrentLng();

                            LatLng arrag = new LatLng(Double.parseDouble(currentLat), Double.parseDouble(currentLng));
                            List<LatLng> latLngs = MapUtils.setLatLngs(arrag);
                            Message message = Message.obtain();
                            message.obj = latLngs;
                            message.what = 1;
                            handler.sendMessage(message);

                        } else if (orderStatus.equals("500") || orderStatus.equals("600") || orderStatus.equals("700")) {
                            //跳转行程中页
                            Intent intent1 = new Intent();
                            intent1.setClass(ServiceToBeActivity.this, TravelInActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("orderresult", orderResult.getResult());
                            intent1.putExtras(bundle);
                            startActivityForResult(intent1, 0);
                            finish();

                        } else if (orderStatus.equals("610")) {
                            //异常结束跳转
                            setResult(RESULT_OK);
                            finish();
                            ToastUtils.showToast(mContext, "异常结束，请重新下单");
                        } else {

                        }
                    } else if (orderResult.getStatus() == StatusCode.RESPONSE_ERR) {
                        ToastUtils.showToast(ServiceToBeActivity.this, "您离开时间太长，需要重新登陆");
                        AppContext.getInstance().finishAllActivity();
                        startActivity(new Intent(ServiceToBeActivity.this, LoginActivity.class));
                        finish();
                    }

                }

                @Override
                public void failure(RetrofitError error) {
                    //失败错误处理
                    error.printStackTrace();
                    ToastUtils.showToast(ServiceToBeActivity.this, "网络异常，无法连接服务器");
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackClick() {
        setResult(RESULT_OK);
        finish();

    }

    @Override
    public void onRightClick() {
        rightView.setClickable(false);
        DialogUtil.getInstance().popDialog((Activity) mContext, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogUtil.getInstance().clearDialogs();
                cancelOrder();
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rightView.setClickable(true);
                DialogUtil.getInstance().clearDialogs();
            }
        }, true);

    }

    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (mListener != null && amapLocation != null) {
            if (amapLocation != null && amapLocation.getErrorCode() == 0) {
                z = amapLocation.getLatitude();
                y = amapLocation.getLongitude();
                mListener.onLocationChanged(amapLocation);// 显示系统小蓝点
            } else {
                String errText = "定位失败," + amapLocation.getErrorCode() + ": " + amapLocation.getErrorInfo();
                Log.e("AmapErr", errText);
            }
        }

    }

    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        setMyLocation(onLocationChangedListener);

    }

    @Override
    public void deactivate() {
        mListener = null;
        if (mlocationClient != null) {
            mlocationClient.stopLocation();
            mlocationClient.onDestroy();
        }
        mlocationClient = null;

    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {

    }

    public class PollingTask extends TimerTask {
        public void run() {
            getOrderStatus();
        }
    }

    public void startPolling() {
        pollingTimer.scheduleAtFixedRate(this.pollingTask, 0, 5000);
    }

    //取消订单
    void cancelOrder() {
        final LoadingDialog loadingDialog = DialogUtil.getLoadingDialog(mContext, "正在取消订单...");
        loadingDialog.show();
        try {
            Map<String, Object> options = new HashMap<>();
            options.put("orderID", SPLoginUser.getOrderId(this));
            options.put("forceCancelFlag", true);
            ServiceProvider.carOrderService.cancelOrder(loginUser.getAccessToken(), options).observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<OrderCancelResponse>() {
                        @Override
                        public void call(OrderCancelResponse orderCancelResponse) {
                            rightView.setClickable(true);
                            if (orderCancelResponse.getStatus() == StatusCode.RESPONSE_OK) {
                                loadingDialog.dismiss();
                                //订单取消成功
                                ToastUtils.showToast(mContext, orderCancelResponse.getMessage());
                                setResult(RESULT_OK);
                                finish();
                            } else if (orderCancelResponse.getStatus() == StatusCode.RESPONSE_ERR) {
                                ToastUtils.showToast(ServiceToBeActivity.this, "您离开时间太长，需要重新登陆");
                                AppContext.getInstance().finishAllActivity();
                                startActivity(new Intent(ServiceToBeActivity.this, LoginActivity.class));
                                finish();
                            } else {
                                ToastUtils.showToast(mContext, orderCancelResponse.getMessage());

                            }

                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {
                            rightView.setClickable(true);
                            loadingDialog.dismiss();
                            throwable.printStackTrace();
                            if (throwable instanceof ConnectException || throwable instanceof RetrofitError) {
                                ToastUtils.showToast(ServiceToBeActivity.this, "网络异常，无法连接服务器");
                                return;
                            }
                        }
                    });
        } catch (Exception e) {
            rightView.setClickable(true);
            loadingDialog.dismiss();
            e.printStackTrace();
            ToastUtils.showToast(mContext, "取消订单失败,请重新取消订单");
        } finally {
            rightView.setClickable(true);
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == RESULT_OK) {
            setResult(RESULT_OK);
            finish();
        }
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                List<LatLng> latLngs = (List<LatLng>) msg.obj;
                //模拟每次坐标变化
                initRoadData(latLngs);
                moveLooper();
            }
        }
    };

    /**
     * 该方法是官网给我demo，没有做任何修改 主要是为了解决marker在地图上平滑移动，
     * <p/>
     * 该方法是把给的经纬度分成好多小段，（主要是解决，不让marker在地图上，呈现跳跃效果）
     */
    public void moveLooper() {
        new Thread() {

            public void run() {
                for (int i = 0; i < mVirtureRoad.getPoints().size() - 1; i++) {
                    LatLng startPoint = mVirtureRoad.getPoints().get(i);
                    LatLng endPoint = mVirtureRoad.getPoints().get(i + 1);
                    mMoveMarker.setPosition(startPoint);

                    mMoveMarker.setRotateAngle((float) MapUtils.getAngle(startPoint, endPoint));

                    double slope = MapUtils.getSlope(startPoint, endPoint);
                    //是不是正向的标示（向上设为正向）
                    boolean isReverse = (startPoint.latitude > endPoint.latitude);

                    double intercept = MapUtils.getInterception(slope, startPoint);

                    double xMoveDistance = isReverse ? MapUtils.getXMoveDistance(DISTANCE, slope) : -1 * MapUtils.getXMoveDistance(DISTANCE, slope);

                    for (double j = startPoint.latitude; !((j > endPoint.latitude) ^ isReverse); j = j - xMoveDistance) {
                        LatLng latLng = null;
                        if (slope != Double.MAX_VALUE) {
                            latLng = new LatLng(j, (j - intercept) / slope);
                        } else {
                            latLng = new LatLng(j, startPoint.longitude);
                        }
                        mMoveMarker.setPosition(latLng);
                        try {
                            Thread.sleep(TIME_INTERVAL);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                    }

                }
            }
        }.start();
    }

    /**
     * 第一步默认定位到司机接单位置
     * 第二步是间隔30秒钟的位置
     * 说明：每次在集合中保留两组坐标（star ，end）
     */

    private void initRoadData(List<LatLng> latLngs) {

        PolylineOptions polylineOptions = new PolylineOptions();

        polylineOptions.addAll(latLngs);

        polylineOptions.width(0);

        if (mMoveMarker != null) {
            mMoveMarker.destroy();
        }
        mVirtureRoad = mAmap.addPolyline(polylineOptions);
        MarkerOptions markerOptions = new MarkerOptions();

        markerOptions.setFlat(true);
        markerOptions.anchor(0.5f, 0.5f);
        //自定义marker
        LayoutInflater inflater = LayoutInflater.from(ServiceToBeActivity.this);
        View view = inflater.inflate(R.layout.layout_ic, null);
        markerOptions.icon(BitmapDescriptorFactory.fromView(view));

        markerOptions.position(polylineOptions.getPoints().get(0));
        mMoveMarker = mAmap.addMarker(markerOptions);
        mMoveMarker.showInfoWindow();

        if (latLngs.size() == 2 && z >= 0.0 && y >= 0.0) {
            LatLng latLng2 = latLngs.get(1);
            double v = MapUtils.DistanceOfTwoPoints(z, y, latLng2.latitude, latLng2.longitude);
            mMoveMarker.setTitle("司机距离您还");
            mMoveMarker.setSnippet("有" + v + "米");
        }

        mMoveMarker.setRotateAngle((float) MapUtils.getAngles(0, mVirtureRoad));
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        mMapView.onSaveInstanceState(outState);
    }


    /**
     * 设置当前客户的位置
     */

    private void setMyLocation(LocationSource.OnLocationChangedListener listener) {
        /**
         * 定位当前直接的位置
         */
        mListener = listener;
        if (mlocationClient == null) {
            mlocationClient = new AMapLocationClient(this);
            mLocationOption = new AMapLocationClientOption();
            //设置定位监听
            mlocationClient.setLocationListener(this);
            //设置为高精度定位模式
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            //设置定位参数
            mlocationClient.setLocationOption(mLocationOption);
            // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
            // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
            // 在定位结束后，在合适的生命周期调用onDestroy()方法
            // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
            mlocationClient.startLocation();
        }
    }

    @Override
    public void onFinish() {


    }

    @Override
    public void onCancel() {

    }

    /**
     * 设置一些amap的属性
     */
    private void setUpMap() {
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromResource(R.mipmap.img_sendcar_location_point));// 设置小蓝点的图标
        myLocationStyle.anchor(0.5f, 0.5f);//设置小蓝点的锚点
        myLocationStyle.strokeColor(Color.BLACK);// 设置圆形的边框颜色
        myLocationStyle.radiusFillColor(Color.argb(100, 0, 0, 180));// 设置圆形的填充颜色
        myLocationStyle.strokeWidth(1.0f);// 设置圆形的边框粗细
        mAmap.setMyLocationStyle(myLocationStyle);
        mAmap.setLocationSource(this);// 设置定位监听
        mAmap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
        mAmap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        // 设置定位的类型为定位模式 ，可以由定位、跟随或地图根据面向方向旋转几种
        mAmap.animateCamera(CameraUpdateFactory.zoomIn(), 800, null);
        mAmap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (pollingTimer != null) pollingTimer.cancel();
        pollingTimer = null;
        if (pollingTask != null) {
            pollingTask.cancel();

        }
    }

}
