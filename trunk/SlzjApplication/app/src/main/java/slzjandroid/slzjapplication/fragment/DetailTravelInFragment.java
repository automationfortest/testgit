package slzjandroid.slzjapplication.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.net.ConnectException;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import retrofit.RetrofitError;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import slzjandroid.slzjapplication.R;
import slzjandroid.slzjapplication.activity.LoginActivity;
import slzjandroid.slzjapplication.context.AppContext;
import slzjandroid.slzjapplication.customView.NavigationView;
import slzjandroid.slzjapplication.dto.LoginUser;
import slzjandroid.slzjapplication.dto.OrderDetailInfo;
import slzjandroid.slzjapplication.dto.OrderDetailResponse;
import slzjandroid.slzjapplication.service.ServiceProvider;
import slzjandroid.slzjapplication.utils.ToastUtils;

/**
 * Created by hdb on 2016/4/5.
 */
public class DetailTravelInFragment extends BaseFragment implements NavigationView.ClickCallback {
    private TextView tv_travelin_status, tv_travelin_driver_name,
            tv_travelin_cartype, tv_travelin_car_num, tv_travelin_passenger,
            tv_travelin_cellphone, tv_travelin_costs_attributable,
            tv_travelin_reason, tv_travelin_adress_from, tv_travelin_adress_to,
            tv_travelin_geton_time, tv_travelin_getdown_time, tv_travelin_pre_coast;
    private Timer pollingTimer = new Timer();
    private PollingTask pollingTask;
    private LoginUser loginUser = null;
    private LinearLayout lly_travelin_coast,lly_travelin_getdown_time;
    private String orderID;
    private Context mContext;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_order_end;
    }

    @Override
    protected void findViews() {
        tv_travelin_status = (TextView) findViewById(R.id.tv_travelin_status);
        tv_travelin_driver_name = (TextView) findViewById(R.id.tv_travelin_driver_name);
        tv_travelin_cartype = (TextView) findViewById(R.id.tv_travelin_cartype);
        tv_travelin_car_num = (TextView) findViewById(R.id.tv_travelin_car_num);
        tv_travelin_cellphone = (TextView) findViewById(R.id.tv_travelin_cellphone);
        tv_travelin_costs_attributable = (TextView) findViewById(R.id.tv_travelin_costs_attributable);
        tv_travelin_reason = (TextView) findViewById(R.id.tv_travelin_reason);
        tv_travelin_adress_from = (TextView) findViewById(R.id.tv_travelin_adress_from);
        tv_travelin_adress_to = (TextView) findViewById(R.id.tv_travelin_adress_to);
        tv_travelin_geton_time = (TextView) findViewById(R.id.tv_travelin_geton_time);
        tv_travelin_getdown_time = (TextView) findViewById(R.id.tv_travelin_getdown_time);
        tv_travelin_pre_coast = (TextView) findViewById(R.id.tv_travelin_pre_coast);
        lly_travelin_coast = (LinearLayout) findViewById(R.id.lly_travelin_coast);
        lly_travelin_getdown_time = (LinearLayout) findViewById(R.id.lly_travelin_getdown_time);
    }

    @Override
    protected void bindViews() {

    }

    @Override
    protected void init() {
        NavigationView navigationView = (NavigationView) super.findViewById(R.id.nav_main);
        navigationView.setTitle("订单详情");
        navigationView.setRightViewIsShow(false);
        navigationView.setClickCallback(this);

        mContext = getActivity();
        lly_travelin_coast.setVisibility(View.INVISIBLE);
        loginUser =LoginUser.getUser();
        this.pollingTask = new PollingTask();
        orderID = getArguments().getString("orderID");
        startPolling();

    }

    //获取订单状态
    private void getOrderStatus() {
        try {
            Map<String, String> options = new HashMap<>();
            options.put("access_token", loginUser.getAccessToken());
            options.put("orderID", orderID);
            ServiceProvider.orderHistoryService.getOrderDetail(options)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<OrderDetailResponse>() {
                        @Override
                        public void call(OrderDetailResponse orderDetailResponse) {
                            if (orderDetailResponse.getStatus().equals("200")) {
                                OrderDetailInfo info = orderDetailResponse.getResult().getInfo();
                                tv_travelin_driver_name.setText(info.getDriverName());
                                tv_travelin_cartype.setText(info.getDriverCarType());
                                tv_travelin_car_num.setText(info.getDriverCard());
                                tv_travelin_cellphone.setText(info.getPassengerName() + "\t" + info.getPassengerPhone());
                                tv_travelin_costs_attributable.setText(info.getDeptName());
                                tv_travelin_reason.setText(info.getReasonName());
                                tv_travelin_adress_from.setText(info.getDepartureName());
                                tv_travelin_adress_to.setText(info.getDestinationName());
                                tv_travelin_geton_time.setText(info.getBeginChargeTime());
                                tv_travelin_getdown_time.setText(info.getFinishTime());
                                tv_travelin_pre_coast.setText(info.getTotalPrice());
                                if (info.getOrderStatus().equals("500")) {
                                    //跳转行程中页
                                    // tv_travelin_pre_coast.setText(orderResult.getResult().getEstimatePrice());

                                } else if (info.getOrderStatus().equals("600")) {
                                    //跳转行程结束页

                                    tv_travelin_status.setText("已完成");
                                    lly_travelin_coast.setVisibility(View.VISIBLE);

                                } else if (info.getOrderStatus().equals("610")) {
                                    //异常结束跳转

                                    ToastUtils.showToast(mContext, "异常结束，请重新下单");
                                    ((Activity) mContext).finish();
                                } else if (info.getOrderStatus().equals("700")) {
                                    //已支付页
                                    if (pollingTimer != null) {
                                        pollingTimer.cancel();
                                        pollingTimer = null;
                                    }
                                    if (pollingTask != null) {
                                        pollingTask.cancel();
                                        pollingTask = null;
                                    }
                                    lly_travelin_coast.setVisibility(View.VISIBLE);
                                    lly_travelin_getdown_time.setVisibility(View.VISIBLE);
                                    tv_travelin_status.setText("已完成");
                                    tv_travelin_pre_coast.setText(info.getTotalPrice());
                                    tv_travelin_getdown_time.setText(info.getFinishTime());
                                }
                            } else if (orderDetailResponse.getStatus().equals("401")) {

                                ToastUtils.showToast(mContext, "您离开时间太长，需要重新登陆");
                                AppContext.getInstance().finishAllActivity();
                                startActivity(new Intent(mContext, LoginActivity.class));
                            }


                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {
                            throwable.printStackTrace();
                            if(throwable instanceof ConnectException ||throwable instanceof RetrofitError){
                                ToastUtils.showToast(mContext,"网络异常，无法连接服务器");
                                return;
                            }
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
            ToastUtils.showToast(mContext, "订单详情查询失败");
            ((Activity) mContext).finish();
        }

    }

    @Override
    public void onBackClick() {


        ((Activity) mContext).finish();
    }

    @Override
    public void onRightClick() {

    }

    public class PollingTask extends TimerTask {
        public void run() {
            getOrderStatus();
        }
    }

    public void startPolling() {
        pollingTimer.scheduleAtFixedRate(this.pollingTask, 0, 5000);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (pollingTimer != null) {
            pollingTimer.cancel();
            pollingTimer = null;
        }
        if (pollingTask != null) {
            pollingTask.cancel();
            pollingTask = null;
        }
    }
}
