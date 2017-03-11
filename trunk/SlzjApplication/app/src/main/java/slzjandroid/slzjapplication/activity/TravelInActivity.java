package slzjandroid.slzjapplication.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import slzjandroid.slzjapplication.R;
import slzjandroid.slzjapplication.context.AppContext;
import slzjandroid.slzjapplication.customView.NavigationView;
import slzjandroid.slzjapplication.dto.LoginUser;
import slzjandroid.slzjapplication.dto.OrderCarResponse;
import slzjandroid.slzjapplication.dto.OrderDetailInfo;
import slzjandroid.slzjapplication.helper.SPLoginUser;
import slzjandroid.slzjapplication.lang.StatusCode;
import slzjandroid.slzjapplication.service.ServiceProvider;
import slzjandroid.slzjapplication.utils.CommonUtils;
import slzjandroid.slzjapplication.utils.ToastUtils;

/**
 * Created by hdb on 2016/4/5.
 */
public class TravelInActivity extends BasicActivity implements NavigationView.ClickCallback {
    private TextView tv_travelin_status, tv_travelin_driver_name, get_down_name, tv_travelin_cartype, tv_travelin_car_num, tv_travelin_passenger, tv_travelin_cellphone,
            tv_travelin_costs_attributable, tv_travelin_reason,
            tv_travelin_adress_from, tv_travelin_adress_to, tv_travelin_geton_time,
            tv_travelin_getdown_time, tv_travelin_pre_coast;
    private OrderDetailInfo orderDetailInfo;
    private Timer pollingTimer = new Timer();
    private PollingTask pollingTask;
    private LoginUser loginUser = null;
    private LinearLayout lly_travelin_coast, lly_travelin_getdown_time;
    private Context mContext;

    @Override
    protected int getLayoutID() {
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

        lly_travelin_getdown_time = (LinearLayout) findViewById(R.id.lly_travelin_getdown_time);
        tv_travelin_getdown_time = (TextView) findViewById(R.id.tv_travelin_getdown_time);

        tv_travelin_pre_coast = (TextView) findViewById(R.id.tv_travelin_pre_coast);
        lly_travelin_coast = (LinearLayout) findViewById(R.id.lly_travelin_coast);

        get_down_name = (TextView) findViewById(R.id.get_down_name);

    }

    @Override
    protected void bindViews() {
    }

    @Override
    protected void init() {
        AppContext.getInstance().addActivity(this);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_main);
        navigationView.setTitle("订单详情");
        navigationView.setRightViewIsShow(false);
        navigationView.setClickCallback(this);
        lly_travelin_getdown_time.setVisibility(View.INVISIBLE);
        lly_travelin_coast.setVisibility(View.INVISIBLE);
        mContext = TravelInActivity.this;
        loginUser = LoginUser.getUser();
        this.pollingTask = new PollingTask();
        orderDetailInfo = (OrderDetailInfo) getIntent().getSerializableExtra("orderresult");
        tv_travelin_driver_name.setText(orderDetailInfo.getDriverName());
        tv_travelin_cartype.setText(orderDetailInfo.getDriverCarType());
        tv_travelin_car_num.setText(orderDetailInfo.getDriverCard());
//        tv_travelin_passenger.setText(orderDetailInfo.getPassengerName());
        tv_travelin_cellphone.setText(orderDetailInfo.getPassengerName() + " " + orderDetailInfo.getPassengerPhone());
        tv_travelin_costs_attributable.setText(orderDetailInfo.getDeptName());
        tv_travelin_reason.setText(orderDetailInfo.getReasonName());
        tv_travelin_adress_from.setText(orderDetailInfo.getDepartureName());
        tv_travelin_adress_to.setText(orderDetailInfo.getDestinationName());
        tv_travelin_geton_time.setText(orderDetailInfo.getBeginChargeTime());
        if (CommonUtils.hasText(orderDetailInfo.getFinishTime())) {
            lly_travelin_getdown_time.setVisibility(View.VISIBLE);
            tv_travelin_getdown_time.setText(orderDetailInfo.getFinishTime());
        } else {
            lly_travelin_getdown_time.setVisibility(View.GONE);
        }
        tv_travelin_pre_coast.setText(orderDetailInfo.getTotalPrice());

        startPolling();

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
                        if (orderResult.getResult().getOrderStatus().equals("500")) {
                            //跳转行程中页
                            // tv_travelin_pre_coast.setText(orderResult.getResult().getEstimatePrice());

                        } else if (orderResult.getResult().getOrderStatus().equals("600")) {
                            //跳转行程结束页

                            tv_travelin_status.setText("已完成");
                            lly_travelin_coast.setVisibility(View.VISIBLE);

                        } else if (orderResult.getResult().getOrderStatus().equals("610")) {
                            //异常结束跳转
                            ToastUtils.showToast(mContext, "异常结束，请重新下单");
                            setResult(RESULT_OK);
                            finish();
                        } else if (orderResult.getResult().getOrderStatus().equals("700")) {
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
                            tv_travelin_status.setText("已完成");
                            tv_travelin_pre_coast.setText(orderResult.getResult().getTotalPrice());
                            
                            lly_travelin_getdown_time.setVisibility(View.VISIBLE);
                            tv_travelin_getdown_time.setText(orderResult.getResult().getFinishTime());
                        }
                    } else if (orderResult.getStatus() == StatusCode.RESPONSE_ERR) {
                        ToastUtils.showToast(TravelInActivity.this, "您离开时间太长，需要重新登陆");
                        AppContext.getInstance().finishAllActivity();
                        startActivity(new Intent(TravelInActivity.this, LoginActivity.class));
                        finish();
                    }

                }

                @Override
                public void failure(RetrofitError error) {
                    //失败错误处理
                    error.printStackTrace();
                    ToastUtils.showToast(TravelInActivity.this, "网络异常，无法连接服务器");
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
    protected void onRestart() {
        super.onRestart();

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (pollingTimer != null)
            pollingTimer.cancel();
        pollingTimer = null;
        if (pollingTask != null) {
            pollingTask.cancel();
            pollingTask = null;
        }
    }

}
