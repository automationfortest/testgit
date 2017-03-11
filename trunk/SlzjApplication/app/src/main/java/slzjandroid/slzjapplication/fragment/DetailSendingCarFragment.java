package slzjandroid.slzjapplication.fragment;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
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

import java.net.ConnectException;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import retrofit.RetrofitError;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import slzjandroid.slzjapplication.R;
import slzjandroid.slzjapplication.activity.LoginActivity;
import slzjandroid.slzjapplication.context.AppContext;
import slzjandroid.slzjapplication.customView.NavigationView;
import slzjandroid.slzjapplication.dto.LoginUser;
import slzjandroid.slzjapplication.dto.OrderCancelResponse;
import slzjandroid.slzjapplication.dto.OrderDetailInfo;
import slzjandroid.slzjapplication.dto.OrderDetailResponse;
import slzjandroid.slzjapplication.helper.SPLoginUser;
import slzjandroid.slzjapplication.lang.StatusCode;
import slzjandroid.slzjapplication.service.ServiceProvider;
import slzjandroid.slzjapplication.utils.DialogUtil;
import slzjandroid.slzjapplication.utils.ToastUtils;

/**
 * Created by hdb on 2016/3/31.
 */
@SuppressWarnings("ALL")
public class DetailSendingCarFragment extends BaseFragment implements NavigationView.ClickCallback {
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
    private String orderID;
    private int minute = 3;
    private int second = 00;
    final static String tag = "tag";
    Timer timer;
    TimerTask timerTask;
    private Context mContext;
    private Boolean cancel_flag = false;
    private TextView rightView;

    public static DetailSendingCarFragment newInstance() {
        DetailSendingCarFragment fragment = new DetailSendingCarFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = getActivity();
    }

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
        rightView = navigationView.getRightView();
        loginUser = LoginUser.getUser();
        this.pollingTask = new PollingTask();
        startTiming();
        //设置订单状态
        orderID = getArguments().getString("orderID");
        startPolling();
    }


    //获取订单状态
    private void getOrderStatus() {
        try {
            final Map<String, String> options = new HashMap<>();
            options.put("access_token", loginUser.getAccessToken());
            options.put("orderID", orderID);
            ServiceProvider.orderHistoryService.getOrderDetail(options).observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<OrderDetailResponse>() {
                        @Override
                        public void call(OrderDetailResponse orderDetailResponse) {
                            if (orderDetailResponse.getStatus().equals("200")) {
                                OrderDetailInfo info = orderDetailResponse.getResult().getInfo();
                                tv_sendingcar_cellphone.setText(info.getPassengerName() + "\t" + info.getPassengerPhone());
                                if (info.getDriverCarType().equals("")) {
                                    tv_sendingcar_cartype.setText(info.getCarServiceName());
                                } else {
                                    tv_sendingcar_cartype.setText(info.getDriverCarType());
                                }

                                tv_sendingcar_reason.setText(info.getReasonName());
                                tv_sendingcar_adress_from.setText(info.getDepartureName());
                                tv_sendingcar_adress_to.setText(info.getDestinationName());
                                tv_sendingcar_call_time.setText(info.getOrderTime());
                                tv_sendingcar_pre_coast.setText(info.getEstimatePrice());
                                tv_sendingcar_costs_attributable.setText(info.getDeptName());
                                if (info.getOrderStatus().equals("400")) {
                                    //跳转等待司机页
                                    DetailServiceToBeFragment serviceToBeFragment = new DetailServiceToBeFragment();
                                    Bundle bundle = new Bundle();
                                    bundle.putSerializable("orderresult", info);
                                    serviceToBeFragment.setArguments(bundle);
                                    FragmentManager fm = getFragmentManager();
                                    FragmentTransaction tx = fm.beginTransaction();
                                    tx.replace(R.id.frame_container, serviceToBeFragment);
                                    tx.addToBackStack(null);
                                    tx.commitAllowingStateLoss();
                                } else if (info.getOrderStatus().equals("410")) {
                                    DetailServiceToBeFragment serviceToBeFragment = new DetailServiceToBeFragment();
                                    Bundle bundle = new Bundle();
                                    bundle.putSerializable("orderresult", info);
                                    serviceToBeFragment.setArguments(bundle);
                                    FragmentManager fm = getFragmentManager();
                                    FragmentTransaction tx = fm.beginTransaction();
                                    tx.replace(R.id.frame_container, serviceToBeFragment);
                                    tx.addToBackStack(null);
                                    tx.commitAllowingStateLoss();
                                } else if (info.getOrderStatus().equals("500")) {
                                    //跳转行程中页
                                    DetailTravelInFragment travelInFragment = new DetailTravelInFragment();
                                    Bundle bundle = new Bundle();
                                    bundle.putString("orderID", orderID);
                                    travelInFragment.setArguments(bundle);
                                    FragmentManager fm = getFragmentManager();
                                    FragmentTransaction tx = fm.beginTransaction();
                                    tx.replace(R.id.frame_container, travelInFragment);
                                    tx.commitAllowingStateLoss();
                                } else if (info.getOrderStatus().equals("600")) {
                                    //跳转行程结束页
                                    DetailSendingCarFragment travelInFragment = new DetailSendingCarFragment();
                                    Bundle bundle = new Bundle();
                                    bundle.putString("orderID", orderID);
                                    travelInFragment.setArguments(bundle);
                                    FragmentManager fm = getFragmentManager();
                                    FragmentTransaction tx = fm.beginTransaction();
                                    tx.replace(R.id.frame_container, travelInFragment);
                                    tx.commitAllowingStateLoss();
                                } else if (info.getOrderStatus().equals("610")) {

                                    DetailCancelSendingCarFragment cancelFragment = new DetailCancelSendingCarFragment();
                                    Bundle bundle = new Bundle();
                                    bundle.putString("orderID", orderID);
                                    cancelFragment.setArguments(bundle);
                                    FragmentManager fm = getFragmentManager();
                                    FragmentTransaction tx = fm.beginTransaction();
                                    tx.replace(R.id.frame_container, cancelFragment);
                                    tx.commitAllowingStateLoss();
                                    ToastUtils.showToast(mContext, "订单超时，请重新下单");
                                } else if (info.getOrderStatus().equals("700")) {

                                    DetailTravelInFragment travelInFragment = new DetailTravelInFragment();
                                    Bundle bundle = new Bundle();
                                    bundle.putString("orderID", orderID);
                                    travelInFragment.setArguments(bundle);
                                    FragmentManager fm = getFragmentManager();
                                    FragmentTransaction tx = fm.beginTransaction();
                                    tx.replace(R.id.frame_container, travelInFragment);
                                    tx.commitAllowingStateLoss();
                                }
                            } else if (orderDetailResponse.getStatus().equals("401")) {
                                ToastUtils.showToast(mContext, "您离开时间太长，需要重新登陆");
                                AppContext.getInstance().finishAllActivity();
                                startActivity(new Intent(mContext, LoginActivity.class));
                            } else {
                                ToastUtils.showToast(mContext, orderDetailResponse.getMessage());

                            }

                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {
                            throwable.printStackTrace();
                            if (throwable instanceof ConnectException || throwable instanceof RetrofitError) {
                                ToastUtils.showToast(mContext, "网络异常，无法连接服务器");
                                return;
                            }
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onBackClick() {
        ((Activity) mContext).finish();

    }

    @Override
    public void onRightClick() {
        rightView.setClickable(false);
        DialogUtil.getInstance().popDialog((Activity) mContext, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel_flag = false;
                DialogUtil.getInstance().clearDialogs();
                SPLoginUser.setfinshTag(getActivity(), "2");
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

        try {
            Map<String, Object> options = new HashMap<>();
            options.put("orderID", orderID);
            options.put("forceCancelFlag", true);
            ServiceProvider.carOrderService.cancelOrder(loginUser.getAccessToken(), options).observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<OrderCancelResponse>() {
                        @Override
                        public void call(OrderCancelResponse orderCancelResponse) {
                            rightView.setClickable(true);
                            if (orderCancelResponse.getStatus() == StatusCode.RESPONSE_OK) {
                                //订单取消成功
                                if (cancel_flag) {
                                    ToastUtils.showToast(mContext, "请您选择其他车型");
                                } else {

                                    ToastUtils.showToast(mContext, orderCancelResponse.getMessage());
                                }
                                getActivity().setResult(getActivity().RESULT_OK);
                                getActivity().finish();
                            } else if (orderCancelResponse.getStatus() == StatusCode.RESPONSE_ERR) {
                                ToastUtils.showToast(mContext, "您离开时间太长，需要重新登陆");
                                AppContext.getInstance().finishAllActivity();
                                startActivity(new Intent(mContext, LoginActivity.class));
                            } else {
                                ToastUtils.showToast(mContext, orderCancelResponse.getMessage());

                            }

                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {
                            rightView.setClickable(true);
                            throwable.printStackTrace();
                        }
                    });

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            rightView.setClickable(true);
            ((Activity) mContext).finish();
        }

    }


    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (minute == 0) {
                if (second == 0) {

                    tv_sendingcar_timer.setText("00:00");
                    DialogUtil.getInstance().popTimingDialog((Activity) mContext, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            minute = 1;
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
                            rightView.setClickable(false);
                            SPLoginUser.setfinshTag(getActivity(), "1");
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
        if (timerTask == null)
            timerTask = new TimerTask() {
                @Override
                public void run() {
                    Message msg = new Message();
                    msg.what = 0;
                    handler.sendMessage(msg);
                }
            };
        if (timer == null)
            timer = new Timer();
        timer.scheduleAtFixedRate(this.timerTask, 0, 1000);
    }

    @Override
    public void onDestroyView() {
        if (pollingTimer != null)
            pollingTimer.cancel();
        pollingTimer = null;
        if (pollingTask != null) pollingTask.cancel();
        if (timer != null)
            timer.cancel();
        timer = null;
        if (timerTask != null) {
            timerTask.cancel();
        }
        super.onDestroyView();
    }
}
