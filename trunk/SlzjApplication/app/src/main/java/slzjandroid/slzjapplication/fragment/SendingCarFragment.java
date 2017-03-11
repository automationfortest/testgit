package slzjandroid.slzjapplication.fragment;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
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

/**
 * Created by hdb on 2016/3/31.
 */
@SuppressWarnings("ALL")
public class SendingCarFragment extends BaseFragment implements NavigationView.ClickCallback {
    private TextView tv_sendingcar_timer;
    private AnimationDrawable anim_wheel;
    private ImageView imv_sendingcar_wheel;
    private ImageView imv_sendingcar_road_left, imv_sendingcar_road_right;
    private LoginUser loginUser = null;
    private OrderDetailInfo orderResult = null;
    private TextView tv_sending_car_passenger, tv_sendingcar_cellphone,
            tv_sendingcar_cartype, tv_sendingcar_reason, tv_sendingcar_adress_from,
            tv_sendingcar_adress_to, tv_sendingcar_call_time, tv_sendingcar_pre_coast, tv_sendingcar_costs_attributable;
    private Timer pollingTimer = new Timer();
    private PollingTask pollingTask;
    private Context mContext;

    @Override
    protected int getLayoutId() {
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
        Animation anim = AnimationUtils.loadAnimation(getActivity(), R.anim.road_a_anim);
        anim.setInterpolator(new LinearInterpolator());//不停顿;
        imv_sendingcar_road_left.startAnimation(anim);
        Animation anim2 = AnimationUtils.loadAnimation(getActivity(), R.anim.road_b_anim);
        anim2.setInterpolator(new LinearInterpolator());//不停顿
        imv_sendingcar_road_right.startAnimation(anim2);
    }

    @Override
    protected void bindViews() {
    }

    @Override
    protected void init() {

        NavigationView navigationView = (NavigationView) super.findViewById(R.id.nav_main);
        navigationView.setTitle("订单详情");
        navigationView.setRightViewIsShow(true);
        navigationView.setRightTile("取消订单");
        navigationView.setClickCallback(this);


        mContext = getActivity();
        loginUser = LoginUser.getUser();
        this.pollingTask = new PollingTask();
        startTiming();
        //设置订单状态
        Bundle bundle = getArguments();
        String cartype = bundle.getString("cartype");
        String estimatePrice = bundle.getString("pre_coast");
        orderResult = (OrderDetailInfo) bundle.getSerializable("orderresult");
        String deptname = bundle.getString("departureName");
        String callreason = bundle.getString("callreason");
        String passenger = bundle.getString("passenger");
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

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroyView() {
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
        super.onDestroyView();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onDestroy() {
        if (pollingTimer != null) {
            pollingTimer.cancel();
        }
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        super.onDestroy();
    }

    //获取订单状态
    private void getOrderStatus() {
        try {
            final Map<String, String> options = new HashMap<>();
            options.put("access_token", loginUser.getAccessToken());
            options.put("orderID", SPLoginUser.getOrderId(getActivity()));
            ServiceProvider.carOrderService.getOrderStatus(options, new Callback<OrderCarResponse>() {
                @Override
                public void success(OrderCarResponse orderResult, Response response) {
                    String orderStatus = orderResult.getResult().getOrderStatus();
                    if (orderStatus.equals("300")) {
                        //继续循环发起查询
                    } else if (orderStatus.equals("400")) {
                        //跳转等待司机页
                        ServiceToBeFragment serviceToBeFragment = new ServiceToBeFragment();
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("orderresult", orderResult.getResult());
                        serviceToBeFragment.setArguments(bundle);
                        FragmentManager fm = getFragmentManager();
                        FragmentTransaction tx = fm.beginTransaction();
                        tx.replace(R.id.container, serviceToBeFragment);

                        tx.commitAllowingStateLoss();
                    } else if (orderStatus.equals("410")) {
                        //跳转司机到达页
                        ServiceToBeFragment serviceToBeFragment = new ServiceToBeFragment();
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("orderresult", orderResult.getResult());
                        serviceToBeFragment.setArguments(bundle);
                        FragmentManager fm = getFragmentManager();
                        FragmentTransaction tx = fm.beginTransaction();
                        tx.replace(R.id.container, serviceToBeFragment);

                        tx.commitAllowingStateLoss();
                    } else if (orderStatus.equals("500")) {
                        //跳转行程中页
                        TravelInFragment travelInFragment = new TravelInFragment();
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("orderresult", orderResult.getResult());
                        travelInFragment.setArguments(bundle);
                        FragmentManager fm = getFragmentManager();
                        FragmentTransaction tx = fm.beginTransaction();
                        tx.replace(R.id.container, travelInFragment);
                        tx.commitAllowingStateLoss();
                    } else if (orderStatus.equals("600")) {
                        //跳转行程结束页
                        TravelInFragment travelInFragment = new TravelInFragment();
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("orderresult", orderResult.getResult());
                        travelInFragment.setArguments(bundle);
                        FragmentManager fm = getFragmentManager();
                        FragmentTransaction tx = fm.beginTransaction();
                        tx.replace(R.id.container, travelInFragment);
                        tx.commitAllowingStateLoss();
                    } else if (orderStatus.equals("610")) {
                        //异常结束跳转
                        ToastUtils.showToast(mContext, "订单超时，请重新下单");
                        ((Activity) mContext).getFragmentManager().popBackStack();
                    } else if (orderStatus.equals("700")) {
                        //已支付页
                        TravelInFragment travelInFragment = new TravelInFragment();
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("orderresult", orderResult.getResult());
                        travelInFragment.setArguments(bundle);
                        FragmentManager fm = getFragmentManager();
                        FragmentTransaction tx = fm.beginTransaction();
                        tx.replace(R.id.container, travelInFragment);
                        tx.commitAllowingStateLoss();
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    //失败错误处理
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        } finally {


        }

    }

    @Override
    public void onBackClick() {
        ((Activity) mContext).getFragmentManager().popBackStack();
    }

    @Override
    public void onRightClick() {
        DialogUtil.getInstance().popDialog((Activity) mContext, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelOrder();
            }
        }, null, true);

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
                            if (orderCancelResponse.getStatus() == StatusCode.RESPONSE_OK) {
                                loadingDialog.dismiss();
                                //订单取消成
                                ToastUtils.showToast(mContext, orderCancelResponse.getMessage());
                                getFragmentManager().popBackStack();
                                ((Activity) mContext).getFragmentManager().popBackStack();
                            } else {
                                ToastUtils.showToast(mContext, orderCancelResponse.getMessage());

                            }
                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {
                            loadingDialog.dismiss();
                            throwable.printStackTrace();

                        }
                    });

        } catch (Exception e) {
            loadingDialog.dismiss();
            e.printStackTrace();
        } finally {
        }
    }

    int minute = 5;
    int second = 00;
    final static String tag = "tag";
    Timer timer;
    TimerTask timerTask;
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (minute == 0) {
                if (second == 0) {
                    tv_sendingcar_timer.setText("00:00");
                    DialogUtil.getInstance().popTimingDialog((Activity) mContext, new View.OnClickListener() {
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
                            DialogUtil.getInstance().clearDialogs();
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

        ;
    };

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
}
