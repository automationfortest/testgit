package slzjandroid.slzjapplication.fragment;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import slzjandroid.slzjapplication.R;
import slzjandroid.slzjapplication.activity.OrderDetailsActivity;
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
public class ServiceToBeFragment extends BaseFragment implements View.OnClickListener, NavigationView.ClickCallback, AMap.CancelableCallback, LocationSource,
        AMapLocationListener {
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

    private LocationSource.OnLocationChangedListener mListener;
    private AMapLocationClient mlocationClient;
    private AMapLocationClientOption mLocationOption;


    // 通过设置间隔时间和距离可以控制速度和图标移动的距离
    private static final int TIME_INTERVAL = 80;
    private static final double DISTANCE = 0.0001;
    private TextView rightView;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_services_to_be;
    }

    @Override
    protected void findViews() {
        mMapView = (MapView) findViewById(R.id.map);
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
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_main);
        navigationView.setTitle("订单详情");
        navigationView.setRightViewIsShow(true);
        navigationView.setRightTile("取消订单");
        navigationView.setClickCallback(this);
        rightView = navigationView.getRightView();
        //获取地图
        mMapView.onCreate(((OrderDetailsActivity) getActivity()).getBundle());
        mAmap = mMapView.getMap();

        setUpMap();

        mContext = getActivity();
        orderResult = (OrderDetailInfo) getArguments().getSerializable("orderresult");
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
                    startActivity(intent);
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
            options.put("orderID", SPLoginUser.getOrderId(getActivity()));
            ServiceProvider.carOrderService.getOrderStatus(options, new Callback<OrderCarResponse>() {
                @Override
                public void success(OrderCarResponse orderResult, Response response) {
                    String order = orderResult.getResult().getOrderStatus();
                    if (order.equals("400") || order.equals("410")) {
                        //跳转等待司机页
                        //跳转司机到达页
                        String currentLat = orderResult.getResult().getCurrentLat();
                        String currentLng = orderResult.getResult().getCurrentLng();
                        initRoadData(Double.parseDouble(currentLat), Double.parseDouble(currentLng));

                    } else if (order.equals("500")) {
                        //跳转行程中页
                        TravelInFragment travelInFragment = new TravelInFragment();
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("orderresult", orderResult.getResult());
                        travelInFragment.setArguments(bundle);
                        FragmentManager fm = getFragmentManager();
                        FragmentTransaction tx = fm.beginTransaction();
                        tx.replace(R.id.container, travelInFragment);
                        tx.commit();

                    } else if (order.equals("600")) {
                        //跳转行程结束页
                        //跳转行程中页
                        TravelInFragment travelInFragment = new TravelInFragment();
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("orderresult", orderResult.getResult());
                        travelInFragment.setArguments(bundle);
                        FragmentManager fm = getFragmentManager();
                        FragmentTransaction tx = fm.beginTransaction();
                        tx.replace(R.id.container, travelInFragment);
                        tx.commit();
                    } else if (order.equals("610")) {
                        //异常结束跳转
                        getFragmentManager().popBackStack();
                        ToastUtils.showToast(mContext, "异常结束，请重新下单");
                    } else if (order.equals("700")) {
                        //已支付页
                        //跳转行程中页
                        TravelInFragment travelInFragment = new TravelInFragment();
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("orderresult", orderResult.getResult());
                        travelInFragment.setArguments(bundle);
                        FragmentManager fm = getFragmentManager();
                        FragmentTransaction tx = fm.beginTransaction();
                        tx.replace(R.id.container, travelInFragment);
                        tx.commit();
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    //失败错误处理
                }
            });

        } catch (Exception e) {

        }
    }

    @Override
    public void onBackClick() {
        ((Activity) mContext).getFragmentManager().popBackStack();

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
        }, null, true);

    }

    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (mListener != null && amapLocation != null) {
            if (amapLocation != null && amapLocation.getErrorCode() == 0) {
                mListener.onLocationChanged(amapLocation);// 显示系统小蓝点
            } else {
                String errText = "定位失败," + amapLocation.getErrorCode() + ": " + amapLocation.getErrorInfo();
                Log.e("AmapErr", errText);
            }
        }

    }

    @Override
    public void onFinish() {

    }

    @Override
    public void onCancel() {

    }

    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        setMyLocation(onLocationChangedListener);


    }


    /**
     * 第一步默认定位到司机接单位置
     * 第二步是间隔30秒钟的位置
     * 说明：每次在集合中保留两组坐标（star ，end）
     *
     * @param x
     * @param y
     */


    private void initRoadData(double x, double y) {
        PolylineOptions polylineOptions = new PolylineOptions();

        LatLng mLatLng = new LatLng(x, y);

        polylineOptions.addAll(MapUtils.setLatLngs(mLatLng));

        polylineOptions.width(0);
        mVirtureRoad = mAmap.addPolyline(polylineOptions);
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.setFlat(true);
        markerOptions.anchor(0.5f, 0.5f);
        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.img_sendcar_driver));
        markerOptions.position(polylineOptions.getPoints().get(0));
        mMoveMarker = mAmap.addMarker(markerOptions);
        mMoveMarker.setRotateAngle((float) MapUtils.getAngles(0, mVirtureRoad));

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
        final LoadingDialog loadingDialog = DialogUtil.getLoadingDialog(getActivity(), "正在取消订单...");
        loadingDialog.show();
        try {
            Map<String, Object> options = new HashMap<>();
            options.put("orderID", SPLoginUser.getOrderId(getActivity()));
            options.put("forceCancelFlag", true);
            ServiceProvider.carOrderService.cancelOrder(loginUser.getAccessToken(), options).observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<OrderCancelResponse>() {
                        @Override
                        public void call(OrderCancelResponse orderCancelResponse) {
                            rightView.setClickable(true);
                            if (orderCancelResponse.getStatus() == StatusCode.RESPONSE_OK) {
                                loadingDialog.dismiss();
                                //订单取消成功
                                ToastUtils.showToast(getActivity(), orderCancelResponse.getMessage());
                                getActivity().getFragmentManager().popBackStack();
                            } else {
                                ToastUtils.showToast(getActivity(), orderCancelResponse.getMessage());

                            }
                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {
                            rightView.setClickable(true);
                            loadingDialog.dismiss();
                            throwable.printStackTrace();
                            getActivity().getFragmentManager().popBackStack();
                        }
                    });
        } catch (Exception e) {
            rightView.setClickable(true);
            loadingDialog.dismiss();
            e.printStackTrace();
            ToastUtils.showToast(getActivity(), "取消订单失败,请重新取消订单");
        } finally {
            rightView.setClickable(true);
        }

    }

    /**
     * 循环进行移动逻辑
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

                    double xMoveDistance = isReverse ? MapUtils.getXMoveDistance(DISTANCE, slope)
                            : -1 * MapUtils.getXMoveDistance(DISTANCE, slope);

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


    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
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
            mlocationClient = new AMapLocationClient(getActivity());
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
    public void onDestroyView() {
        super.onDestroyView();
        if (pollingTimer != null) pollingTimer.cancel();
        pollingTimer = null;
        if (pollingTask != null) {
            pollingTask.cancel();

        }
    }
}
