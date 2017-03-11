package slzjandroid.slzjapplication.activity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;

import java.net.ConnectException;
import java.util.HashMap;
import java.util.Map;

import retrofit.RetrofitError;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import slzjandroid.slzjapplication.R;
import slzjandroid.slzjapplication.context.AppContext;
import slzjandroid.slzjapplication.dto.LoginUser;
import slzjandroid.slzjapplication.dto.OrderDetailInfo;
import slzjandroid.slzjapplication.dto.OrderDetailResponse;
import slzjandroid.slzjapplication.fragment.DetailCancelSendingCarFragment;
import slzjandroid.slzjapplication.fragment.DetailSendingCarFragment;
import slzjandroid.slzjapplication.fragment.DetailServiceToBeFragment;
import slzjandroid.slzjapplication.fragment.DetailTravelInFragment;
import slzjandroid.slzjapplication.helper.SPLoginUser;
import slzjandroid.slzjapplication.service.ServiceProvider;
import slzjandroid.slzjapplication.utils.ToastUtils;

/**
 * Created by hdb on 2016/4/7.
 */
public class OrderDetailsActivity extends BasicActivity {
    private String orderID;
    private String orderStatus;

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_orderdetail;
    }


    @Override
    public Bundle getBundle() {
        return super.getBundle();
    }

    @Override
    protected void findViews() {

    }


    @Override
    protected void init() {
        AppContext.getInstance().addActivity(this);
        orderID = SPLoginUser.getOrderId(OrderDetailsActivity.this);
        orderStatus = getIntent().getStringExtra("orderStatus");
        getActionBar().hide();
        getOrderStatus();
    }


    @Override
    protected void bindViews() {

    }

    //获取订单状态
    private void getOrderStatus() {
        try {
            final Map<String, String> options = new HashMap<>();
            LoginUser user = LoginUser.getUser();
            String accessToken = user.getAccessToken();
            options.put("access_token", accessToken);
            options.put("orderID", orderID);
            ServiceProvider.orderHistoryService.getOrderDetail(options).observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<OrderDetailResponse>() {
                        @Override
                        public void call(OrderDetailResponse orderDetailResponse) {
                            if (orderDetailResponse.getStatus().equals("200")) {
                                OrderDetailInfo info = orderDetailResponse.getResult().getInfo();
                                if (orderStatus.equals("300")) {
                                    DetailSendingCarFragment sendingCarFragment = new DetailSendingCarFragment();
                                    Bundle bundle = new Bundle();
                                    bundle.putString("orderID", orderID);
                                    sendingCarFragment.setArguments(bundle);
                                    FragmentManager fm = getFragmentManager();
                                    FragmentTransaction tx = fm.beginTransaction();
                                    tx.replace(R.id.frame_container, sendingCarFragment);
                                    tx.commitAllowingStateLoss();
                                }
                                if (info.getOrderStatus().equals("400")) {
                                    //跳转等待司机页
                                    DetailServiceToBeFragment serviceToBeFragment = new DetailServiceToBeFragment();
                                    Bundle bundle = new Bundle();
                                    bundle.putSerializable("orderresult", info);
                                    serviceToBeFragment.setArguments(bundle);
                                    FragmentManager fm = getFragmentManager();
                                    FragmentTransaction tx = fm.beginTransaction();
                                    tx.replace(R.id.frame_container, serviceToBeFragment);
                                    tx.commitAllowingStateLoss();
                                } else if (info.getOrderStatus().equals("410")) {
                                    DetailServiceToBeFragment serviceToBeFragment = new DetailServiceToBeFragment();
                                    Bundle bundle = new Bundle();
                                    bundle.putSerializable("orderresult", info);
                                    serviceToBeFragment.setArguments(bundle);
                                    FragmentManager fm = getFragmentManager();
                                    FragmentTransaction tx = fm.beginTransaction();
                                    tx.replace(R.id.frame_container, serviceToBeFragment);
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
                                    if (orderStatus.equals(300)) {
                                        ToastUtils.showToast(OrderDetailsActivity.this, "订单超时，请重新下单");
                                    }

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
                                ToastUtils.showToast(OrderDetailsActivity.this, "您离开时间太长，需要重新登陆");
                                AppContext.getInstance().finishAllActivity();
                                startActivity(new Intent(OrderDetailsActivity.this, LoginActivity.class));
                            } else {
                                ToastUtils.showToast(OrderDetailsActivity.this, orderDetailResponse.getMessage());
                                finish();
                            }

                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {
                            if (throwable instanceof ConnectException || throwable instanceof RetrofitError) {
                                ToastUtils.showToast(OrderDetailsActivity.this, "网络异常，无法连接服务器");
                                finish();
                                return;
                            }
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
            finish();
        }

    }

}
