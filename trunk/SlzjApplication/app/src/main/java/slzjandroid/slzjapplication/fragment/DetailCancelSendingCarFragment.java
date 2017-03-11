package slzjandroid.slzjapplication.fragment;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.net.ConnectException;
import java.util.HashMap;
import java.util.Map;

import retrofit.RetrofitError;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import slzjandroid.slzjapplication.R;
import slzjandroid.slzjapplication.activity.LoginActivity;
import slzjandroid.slzjapplication.context.AppContext;
import slzjandroid.slzjapplication.customView.LoadingDialog;
import slzjandroid.slzjapplication.customView.NavigationView;
import slzjandroid.slzjapplication.dto.LoginUser;
import slzjandroid.slzjapplication.dto.OrderDetailInfo;
import slzjandroid.slzjapplication.dto.OrderDetailResponse;
import slzjandroid.slzjapplication.service.ServiceProvider;
import slzjandroid.slzjapplication.utils.DialogUtil;
import slzjandroid.slzjapplication.utils.ToastUtils;

/**
 * Created by hdb on 2016/3/31.
 */
@SuppressWarnings("ALL")
public class DetailCancelSendingCarFragment extends Fragment implements NavigationView.ClickCallback {
    private LoginUser loginUser = null;
    private OrderDetailInfo orderResult = null;
    private TextView tv_rider_info, tv_sending_car_passenger, tv_sendingcar_cellphone, tv_sending_car_status,
            tv_sendingcar_cartype, tv_sendingcar_reason, tv_sendingcar_adress_from,
            tv_sendingcar_adress_to, tv_sendingcar_call_time, tv_sendingcar_pre_coast, tv_sendingcar_costs_attributable;
    private String orderID;
    private LinearLayout lly_sending_car_amount;

    public static DetailCancelSendingCarFragment newInstance() {
        DetailCancelSendingCarFragment fragment = new DetailCancelSendingCarFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_detail_sending_car_cancel, null);

        initView(view);
        loginUser = LoginUser.getUser();
        //设置订单状态
        orderID = getArguments().getString("orderID");
        getOrderStatus();
        return view;
    }

    private void initView(View view) {
        NavigationView navigationView = (NavigationView) view.findViewById(R.id.nav_main);
        navigationView.setTitle("订单详情");
        navigationView.setRightViewIsShow(false);
        navigationView.setClickCallback(this);

        lly_sending_car_amount = (LinearLayout) view.findViewById(R.id.lly_sending_car_amount);
        tv_sending_car_status = (TextView) view.findViewById(R.id.tv_sending_car_status);
        tv_sendingcar_cellphone = (TextView) view.findViewById(R.id.tv_sendingcar_cellphone);
        tv_sendingcar_cartype = (TextView) view.findViewById(R.id.tv_sendingcar_cartype);
        tv_sendingcar_reason = (TextView) view.findViewById(R.id.tv_sendingcar_reason);
        tv_sendingcar_adress_from = (TextView) view.findViewById(R.id.tv_sendingcar_adress_from);
        tv_sendingcar_adress_to = (TextView) view.findViewById(R.id.tv_sendingcar_adress_to);
        tv_sendingcar_call_time = (TextView) view.findViewById(R.id.tv_sendingcar_call_time);
        tv_sendingcar_pre_coast = (TextView) view.findViewById(R.id.tv_sendingcar_pre_coast);
        tv_sendingcar_costs_attributable = (TextView) view.findViewById(R.id.tv_sendingcar_costs_attributable);

    }


    //获取订单状态
    private void getOrderStatus() {
        final LoadingDialog loadingDialog = DialogUtil.getLoadingDialog(getActivity(), "正在获取订单信息...");
        loadingDialog.show();
        try {
            final Map<String, String> options = new HashMap<>();
            options.put("access_token", loginUser.getAccessToken());
            options.put("orderID", orderID);
            ServiceProvider.orderHistoryService.getOrderDetail(options).observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<OrderDetailResponse>() {
                        @Override
                        public void call(OrderDetailResponse orderDetailResponse) {
                            loadingDialog.dismiss();
                            if (orderDetailResponse.getStatus().equals("200")) {
                                OrderDetailInfo info = orderDetailResponse.getResult().getInfo();
                                tv_sendingcar_cellphone.setText(info.getPassengerName() + "\t" + info.getPassengerPhone());
                                if (!info.getDriverCarType().equals("")) {
                                    tv_sendingcar_cartype.setText(info.getDriverCarType());
                                } else {
                                    tv_sendingcar_cartype.setText(info.getCarServiceName());
                                }
                                tv_sendingcar_reason.setText(info.getReasonName());
                                tv_sendingcar_adress_from.setText(info.getDepartureName());
                                tv_sendingcar_adress_to.setText(info.getDestinationName());
                                tv_sendingcar_call_time.setText(info.getOrderTime());
                                if (info.getTotalPrice() == null) {
                                    info.setTotalPrice("0");
                                }
                                tv_sendingcar_pre_coast.setText(info.getTotalPrice());
                                tv_sendingcar_costs_attributable.setText(info.getDeptName());

                            } else if (orderDetailResponse.getStatus().equals("401")) {
                                ToastUtils.showToast(getActivity(), "您离开时间太长，需要重新登陆");
                                AppContext.getInstance().finishAllActivity();
                                startActivity(new Intent(getActivity(), LoginActivity.class));
                            }

                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {
                            throwable.printStackTrace();
                            if(throwable instanceof ConnectException ||throwable instanceof RetrofitError){
                                ToastUtils.showToast(getActivity(),"网络异常，无法连接服务器");
                                return;
                            }
                        }
                    });
        } catch (Exception e) {
            loadingDialog.dismiss();
            e.printStackTrace();
        }

    }


    @Override
    public void onBackClick() {
        getActivity().finish();
    }

    @Override
    public void onRightClick() {

    }
}
