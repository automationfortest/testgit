package slzjandroid.slzjapplication.activity;

import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import slzjandroid.slzjapplication.R;
import slzjandroid.slzjapplication.adapter.OrderAllListAdapter;
import slzjandroid.slzjapplication.adapter.OrderListAdapter;
import slzjandroid.slzjapplication.context.AppContext;
import slzjandroid.slzjapplication.customView.NavigationView;
import slzjandroid.slzjapplication.dto.LoginUser;
import slzjandroid.slzjapplication.dto.OrderHistoryResponse;
import slzjandroid.slzjapplication.helper.SPLoginUser;
import slzjandroid.slzjapplication.service.ServiceProvider;
import slzjandroid.slzjapplication.utils.CommonUtils;
import slzjandroid.slzjapplication.utils.ToastUtils;
import slzjandroid.slzjapplication.weights.listview.pulltorefresh.library.PullToRefreshBase;
import slzjandroid.slzjapplication.weights.listview.pulltorefresh.library.PullToRefreshListView;

/**
 * Created by hdb on 2016/4/6.
 */
public class OrderListActivity extends BasicActivity implements View.OnClickListener, NavigationView.ClickCallback, PullToRefreshBase.OnRefreshListener2<ListView> {
    private PullToRefreshListView mListView, t_mListView;

    private ImageView imv_orderlist_myorder, imv_orderlist_comorder;
    private TextView tv_orderlist_comorder, tv_orderlist_myorder;
    private LinearLayout lly_orderlist_chose;
    private RelativeLayout rly_orderlist_myorder, rly_orderlist_comorder;
    private OrderListAdapter orderListAdapter;
    private OrderAllListAdapter totalOrderListAdapter;
    private String totalPage = "0";
    private String t_totalPage = "0";


    private int currentpage = 0;
    private int t_currentpage = 0;

    private int prepages = 15;
    private int t_prepages = 15;
    private boolean flag;
    private boolean t_flag;
    private int table = 0;

    private LoginUser user;
    private String accessToken;


    @Override
    protected int getLayoutID() {
        return R.layout.activity_orderlist;
    }

    @Override
    protected void findViews() {

        lly_orderlist_chose = (LinearLayout) findViewById(R.id.lly_orderlist_chose);
        mListView = (PullToRefreshListView) findViewById(R.id.xlist_orderlist);

        t_mListView = (PullToRefreshListView) findViewById(R.id.xlist_total_orderlist);

        rly_orderlist_myorder = (RelativeLayout) findViewById(R.id.rly_orderlist_myorder);
        rly_orderlist_comorder = (RelativeLayout) findViewById(R.id.rly_orderlist_comorder);
        imv_orderlist_myorder = (ImageView) findViewById(R.id.imv_orderlist_myorder);
        imv_orderlist_comorder = (ImageView) findViewById(R.id.imv_orderlist_comorder);
        tv_orderlist_myorder = (TextView) findViewById(R.id.tv_orderlist_myorder);
        tv_orderlist_comorder = (TextView) findViewById(R.id.tv_orderlist_comorder);

    }

    @Override
    protected void init() {

        AppContext.getInstance().addActivity(this);
        NavigationView navigationView = (NavigationView) super.findViewById(R.id.nav_main);
        navigationView.setTitle("订单列表");
        navigationView.setRightViewIsShow(false);
        navigationView.setClickCallback(this);

        user = LoginUser.getUser();
        accessToken = user.getAccessToken();

        try {
            String userType = user.getUserType();
            if (("1").equals(userType)) {
                lly_orderlist_chose.setVisibility(View.GONE);
                t_mListView.setVisibility(View.GONE);
            } else {
                lly_orderlist_chose.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {

        }
        imv_orderlist_comorder.setVisibility(View.GONE);
        t_mListView.setVisibility(View.GONE);
        orderListAdapter = new OrderListAdapter(OrderListActivity.this);
        mListView.setAdapter(orderListAdapter);

        totalOrderListAdapter = new OrderAllListAdapter(OrderListActivity.this);
        t_mListView.setAdapter(totalOrderListAdapter);


    }


    @Override
    protected void bindViews() {
        rly_orderlist_myorder.setOnClickListener(this);
        rly_orderlist_comorder.setOnClickListener(this);

        mListView.setOnRefreshListener(this);

        t_mListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                String label = CommonUtils.setRefreshTime(OrderListActivity.this);
                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel("最后更新:" + "\t" + label);
                donwingDataTwo();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                donwingDataTwo();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (table == 0) {
                    flag = false;
                    currentpage = 0;
                    orderListAdapter.clear();
                    donwingDataOne();
                } else {
                    flag = false;
                    t_currentpage = 0;
                    totalOrderListAdapter.clear();
                    donwingDataTwo();
                }

            }
        }, 800);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void getOrderlist() {
        try {
            Map<String, String> options = new HashMap<>();
            options.put("access_token", accessToken);
            options.put("usrType", user.getUserType());
            options.put("orderType", "0");
            options.put("perpage", prepages + "");
            options.put("currentPage", currentpage + "");
            options.put("startDate", "");
            options.put("endDate", "");
            ServiceProvider.orderHistoryService.getOrderList(options).observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<OrderHistoryResponse>() {
                        @Override
                        public void call(OrderHistoryResponse orderHistoryResponse) {
                            if (orderHistoryResponse.getStatus() == 200) {
                                mListView.onRefreshComplete();
                                totalPage = orderHistoryResponse.getResult().getTotalPage();
                                orderListAdapter.append(orderHistoryResponse.getResult().getGeneralOrderInfo());
                            } else if (orderHistoryResponse.getStatus() == 401) {
                                ToastUtils.showToast(OrderListActivity.this, orderHistoryResponse.getMessage());
                                AppContext.getInstance().finishAllActivity();
                                startActivity(new Intent(OrderListActivity.this, LoginActivity.class));
                            }
                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {
                            mListView.onRefreshComplete();
                            throwable.printStackTrace();

                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
            mListView.onRefreshComplete();
        }

    }

    private void getTotalOrderlist() {
        try {
            Map<String, String> options = new HashMap<>();
            options.put("access_token", accessToken);
            options.put("usrType", user.getUserType());
            options.put("orderType", "1");
            options.put("perpage", t_prepages + "");
            options.put("currentPage", t_currentpage + "");
            options.put("startDate", "");
            options.put("endDate", "");
            ServiceProvider.orderHistoryService.getOrderList(options).observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<OrderHistoryResponse>() {
                        @Override
                        public void call(OrderHistoryResponse orderHistoryResponse) {
                            if (orderHistoryResponse.getStatus() == 200) {
                                t_mListView.onRefreshComplete();
                                t_totalPage = orderHistoryResponse.getResult().getTotalPage();
                                totalOrderListAdapter.append(orderHistoryResponse.getResult().getGeneralOrderInfo());
                            } else if (orderHistoryResponse.getStatus() == 401) {
                                ToastUtils.showToast(OrderListActivity.this, orderHistoryResponse.getMessage());
                                AppContext.getInstance().finishAllActivity();
                                startActivity(new Intent(OrderListActivity.this, LoginActivity.class));
                            }
                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {
                            t_mListView.onRefreshComplete();
                            throwable.printStackTrace();
                        }
                    });
        } catch (Exception e) {
            t_mListView.onRefreshComplete();
            e.printStackTrace();
        }

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rly_orderlist_myorder:
                table = 0;
                imv_orderlist_comorder.setVisibility(View.GONE);
                imv_orderlist_myorder.setVisibility(View.VISIBLE);
                t_mListView.setVisibility(View.GONE);
                mListView.setVisibility(View.VISIBLE);
                tv_orderlist_myorder.setTextColor(getResources().getColor(R.color.title_bg));
                tv_orderlist_comorder.setTextColor(getResources().getColor(R.color.edt_gray_font));
                mListView.setRefreshing(true);
                break;
            case R.id.rly_orderlist_comorder:
                table = 1;
                imv_orderlist_myorder.setVisibility(View.GONE);
                imv_orderlist_comorder.setVisibility(View.VISIBLE);
                t_mListView.setVisibility(View.VISIBLE);
                mListView.setVisibility(View.GONE);
                tv_orderlist_myorder.setTextColor(getResources().getColor(R.color.edt_gray_font));
                tv_orderlist_comorder.setTextColor(getResources().getColor(R.color.title_bg));
                t_mListView.setRefreshing(true);
                break;
        }
    }

    @Override
    public void onBackClick() {
        finish();
    }

    @Override
    public void onRightClick() {

    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
        String label = CommonUtils.setRefreshTime(OrderListActivity.this);
        // 显示最后更新的时间
        refreshView.getLoadingLayoutProxy().setLastUpdatedLabel("最后更新:" + "\t" + label);
        donwingDataOne();

    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
        donwingDataOne();
    }


    private void donwingDataOne() {
        if (!flag) {
            currentpage++;
            getOrderlist();
            flag = true;
        } else {
            if (currentpage < Integer.valueOf(totalPage)) {
                currentpage++;
                getOrderlist();
            } else {
                colseFooter(mListView);
            }
        }
    }

    private void donwingDataTwo() {
        if (!t_flag) {
            t_currentpage++;
            getTotalOrderlist();
            t_flag = true;
        } else {
            if (t_currentpage < Integer.valueOf(t_totalPage)) {
                t_currentpage++;
                getTotalOrderlist();
            } else {
                colseFooter(t_mListView);

            }
        }
    }


    private void colseFooter(final PullToRefreshListView pullToRefreshListView) {
        colseHeader(pullToRefreshListView);
    }

    private void colseHeader(final PullToRefreshListView pullToRefreshListView) {
        pullToRefreshListView.postDelayed(new Runnable() {
            @Override
            public void run() {
                pullToRefreshListView.onRefreshComplete();
            }
        }, 800);
    }

    @Override
    protected void onActivityResult(final int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (requestCode == 1 && SPLoginUser.getfinshTag(OrderListActivity.this).equals("1")) {
                    setResult(RESULT_OK);
                    finish();
                }
            }
        }, 1000);

    }

    @Override
    protected String getDisplayTitle() {
        return "OrderListActivity";
    }
}
