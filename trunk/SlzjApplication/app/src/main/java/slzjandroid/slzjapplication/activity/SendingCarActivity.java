package slzjandroid.slzjapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import java.net.ConnectException;
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
import slzjandroid.slzjapplication.utils.ToastUtils;

public class SendingCarActivity extends BasicActivity implements NavigationView.ClickCallback {
    private TextView tv_sendingcar_timer;
    private ImageView imv_sendingcar_wheel;
    private ImageView imv_sendingcar_road_left, imv_sendingcar_road_right;
    private LoginUser loginUser = null;
    private OrderDetailInfo orderResult = null;
    private TextView tv_sendingcar_cellphone,
            tv_sendingcar_cartype, tv_sendingcar_reason, tv_sendingcar_adress_from,
            tv_sendingcar_adress_to, tv_sendingcar_call_time, tv_sendingcar_pre_coast, tv_sendingcar_costs_attributable;
    private Timer pollingTimer = new Timer();
    private PollingTask pollingTask;
    private Boolean cancel_flag = false;
    private TextView rightView;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_sending_car;
    }

    @Override
    protected void findViews() {
        tv_sendingcar_timer = (TextView) findViewById(R.id.tv_sendingcar_timer);
        imv_sendingcar_wheel = (ImageView) findViewById(R.id.imv_sendingcar_wheel);
        imv_sendingcar_road_left = (ImageView) findViewById(R.id.imv_sendingcar_road_left);
        imv_sendingcar_road_right = (ImageView) findViewById(R.id.imv_sendingcar_road_right);
        tv_sendingcar_cellphone = (TextView) findViewById(R.id.tv_sendingcar_cellphone);
        tv_sendingcar_cartype = (TextView) findViewById(R.id.tv_sendingcar_cartype);
        tv_sendingcar_reason = (TextView) findViewById(R.id.tv_sendingcar_reason);
        tv_sendingcar_adress_from = (TextView) findViewById(R.id.tv_sendingcar_adress_from);
        tv_sendingcar_adress_to = (TextView) findViewById(R.id.tv_sendingcar_adress_to);
        tv_sendingcar_call_time = (TextView) findViewById(R.id.tv_sendingcar_call_time);
        tv_sendingcar_pre_coast = (TextView) findViewById(R.id.tv_sendingcar_pre_coast);
        tv_sendingcar_costs_attributable = (TextView) findViewById(R.id.tv_sendingcar_costs_attributable);
        RotateAnimation animation = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setDuration(6000);//设定转一圈的时间
        animation.setInterpolator(new LinearInterpolator());//不停顿
        animation.setRepeatCount(Animation.INFINITE);//设定无限循环
        animation.setRepeatMode(Animation.RESTART);
        imv_sendingcar_wheel.startAnimation(animation);
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.road_a_anim);
        anim.setInterpolator(new LinearInterpolator());//不停顿;
        imv_sendingcar_road_left.startAnimation(anim);
        Animation anim2 = AnimationUtils.loadAnimation(this, R.anim.road_b_anim);
        anim2.setInterpolator(new LinearInterpolator());//不停顿
        imv_sendingcar_road_right.startAnimation(anim2);

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

        loginUser = LoginUser.getUser();
        this.pollingTask = new PollingTask();
        startTiming();
        //设置订单状态
        Intent intent = getIntent();
        orderResult = (OrderDetailInfo) intent.getExtras().getSerializable("orderresult");
        String cartype = intent.getStringExtra("cartype");
        String estimatePrice = intent.getStringExtra("pre_coast");
        String deptname = intent.getStringExtra("departureName");
        String callreason = intent.getStringExtra("callreason");
        String passenger = intent.getStringExtra("passenger");

        if (orderResult != null) {
            tv_sendingcar_cellphone.setText(passenger + "\t" + orderResult.getPassengerPhone());
            tv_sendingcar_cartype.setText(cartype);
            tv_sendingcar_reason.setText(callreason);
            tv_sendingcar_adress_from.setText(orderResult.getDepartureName());
            tv_sendingcar_adress_to.setText(orderResult.getDestinationName());
            tv_sendingcar_call_time.setText(orderResult.getOrderTime());
            tv_sendingcar_pre_coast.setText(estimatePrice);
            tv_sendingcar_costs_attributable.setText(deptname);
            tv_sendingcar_cartype.setText(cartype);

        }
        startPolling();

    }

    public void startPolling() {
        pollingTimer.scheduleAtFixedRate(this.pollingTask, 0, 5000);
    }

    @Override
    protected void bindViews() {

    }


    //获取订单状态
    private void getOrderStatus() {
        try {
            final Map<String, String> options = new HashMap<>();
            options.put("access_token", loginUser.getAccessToken());
            options.put("orderID", SPLoginUser.getOrderId(this));
            ServiceProvider.carOrderService.getOrderStatus(options, new Callback<OrderCarResponse>() {
                @Override
                public void success(OrderCarResponse orderResult, Response response) {
                    String orderStatus = orderResult.getResult().getOrderStatus();
                    if (orderStatus.equals("300")) {
                        //继续循环发起查询
                    } else if (orderStatus.equals("400")) {
                        //跳转等待司机页
                        Intent intent0 = new Intent();
                        intent0.setClass(SendingCarActivity.this, ServiceToBeActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("orderresult", orderResult.getResult());
                        intent0.putExtras(bundle);
                        startActivityForResult(intent0, 0);
                        finish();
                    } else if (orderStatus.equals("410")) {
                        //跳转司机到达页
                        Intent intent1 = new Intent();
                        intent1.setClass(SendingCarActivity.this, ServiceToBeActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("orderresult", orderResult.getResult());
                        intent1.putExtras(bundle);
                        startActivityForResult(intent1, 0);
                        finish();
                    } else if (orderStatus.equals("500")) {
                        //跳转行程中页
                        Intent intent2 = new Intent();
                        intent2.setClass(SendingCarActivity.this, TravelInActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("orderresult", orderResult.getResult());
                        intent2.putExtras(bundle);
                        startActivityForResult(intent2, 0);
                        finish();
                    } else if (orderStatus.equals("600")) {
                        //跳转行程结束页
                        Intent intent3 = new Intent();
                        intent3.setClass(SendingCarActivity.this, TravelInActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("orderresult", orderResult.getResult());
                        intent3.putExtras(bundle);
                        startActivityForResult(intent3, 0);
                        finish();
                    } else if (orderStatus.equals("610")) {
                        //异常结束跳转
                        ToastUtils.showToast(SendingCarActivity.this, "订单超时，请重新下单");
                        finish();
                    } else if (orderStatus.equals("700")) {
                        //已支付页
                        Intent intent4 = new Intent();
                        intent4.setClass(SendingCarActivity.this, TravelInActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("orderresult", orderResult.getResult());
                        intent4.putExtras(bundle);
                        startActivityForResult(intent4, 0);
                        finish();
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    error.printStackTrace();
                    ToastUtils.showToast(SendingCarActivity.this, "网络异常，无法连接服务器");
                }
            });
        } catch (Exception e) {
            e.printStackTrace();

        }

    }

    @Override
    public void onBackClick() {
        finish();
    }

    @Override
    public void onRightClick() {
        rightView.setClickable(false);
        DialogUtil.getInstance().popDialog(this, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel_flag = false;
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

    //取消订单
    void cancelOrder() {

        final LoadingDialog loadingDialog = DialogUtil.getLoadingDialog(this, "正在取消订单...");
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
                                //订单取消成
                                if (cancel_flag) {
                                    ToastUtils.showToast(SendingCarActivity.this, "请您选择其他车型");
                                } else {
                                    ToastUtils.showToast(SendingCarActivity.this, orderCancelResponse.getMessage());
                                }
                                finish();
                            } else {
                                ToastUtils.showToast(SendingCarActivity.this, orderCancelResponse.getMessage());

                            }
                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {
                            rightView.setClickable(true);
                            loadingDialog.dismiss();
                            throwable.printStackTrace();
                            if (throwable instanceof ConnectException || throwable instanceof RetrofitError) {
                                ToastUtils.showToast(SendingCarActivity.this, "网络异常，无法连接服务器");
                                return;
                            }

                        }
                    });

        } catch (Exception e) {
            rightView.setClickable(true);
            loadingDialog.dismiss();
            e.printStackTrace();
        } finally {
            rightView.setClickable(true);
        }


    }

    int minute = 3;
    int second = 00;
    Timer timer;
    TimerTask timerTask;
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (minute == 0) {
                if (second == 0) {
                    tv_sendingcar_timer.setText("00:00");
                    DialogUtil.getInstance().popTimingDialog(SendingCarActivity.this, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            minute = 3;
                            second = 00;
                            if (timer != null) {
                                timer.cancel();
                                timer = null;
                            }
                            if (timerTask != null) {
                                timerTask.cancel();
                                timerTask = null;
                            }
                            startTiming();
                            DialogUtil.getInstance().clearDialogs();
                        }
                    }, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            cancel_flag = true;
                            cancelOrder();
                        }
                    });

                } else {
                    second--;
                    if (second >= 10) {
                        tv_sendingcar_timer.setText("0" + minute + ":" + second);
                    } else {
                        tv_sendingcar_timer.setText("0" + minute + ":0" + second);
                    }
                }
            } else {
                if (second == 0) {
                    second = 59;
                    minute--;
                    if (minute >= 10) {
                        tv_sendingcar_timer.setText(minute + ":" + second);
                    } else {
                        tv_sendingcar_timer.setText("0" + minute + ":" + second);
                    }
                } else {
                    second--;
                    if (second >= 10) {
                        if (minute >= 10) {
                            tv_sendingcar_timer.setText(minute + ":" + second);
                        } else {
                            tv_sendingcar_timer.setText("0" + minute + ":" + second);
                        }
                    } else {
                        if (minute >= 10) {
                            tv_sendingcar_timer.setText(minute + ":0" + second);
                        } else {
                            tv_sendingcar_timer.setText("0" + minute + ":0" + second);
                        }
                    }
                }
            }
        }
    };

    public class PollingTask extends TimerTask {
        public void run() {
            getOrderStatus();
        }
    }

    private void startTiming() {
        if (timerTask == null) {
            timerTask = new TimerTask() {
                @Override
                public void run() {
                    handler.sendEmptyMessage(0);
                }
            };
        }
        if (timer == null) {
            timer = new Timer();
            timer.schedule(timerTask, 0, 1000);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (pollingTimer != null) {
            pollingTimer.cancel();
        }
        if (pollingTask != null) {
            pollingTask.cancel();
        }
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        if (timerTask != null) {
            timerTask.cancel();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == RESULT_OK) {
            finish();
        }
    }
}
