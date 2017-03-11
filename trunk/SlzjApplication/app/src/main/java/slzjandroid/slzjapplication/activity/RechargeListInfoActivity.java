package slzjandroid.slzjapplication.activity;

import android.widget.ListView;

import java.net.ConnectException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit.RetrofitError;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import slzjandroid.slzjapplication.R;
import slzjandroid.slzjapplication.adapter.RechargeInfoAdapter;
import slzjandroid.slzjapplication.context.AppContext;
import slzjandroid.slzjapplication.customView.NavigationView;
import slzjandroid.slzjapplication.dto.ChargeList;
import slzjandroid.slzjapplication.dto.LoginUser;
import slzjandroid.slzjapplication.dto.RechargeInfoResponse;
import slzjandroid.slzjapplication.lang.StatusCode;
import slzjandroid.slzjapplication.service.ServiceProvider;
import slzjandroid.slzjapplication.utils.ToastUtils;

/**
 * Created by ASUS on 2016/4/28.
 */
public class RechargeListInfoActivity extends BasicActivity implements NavigationView.ClickCallback {
    private ListView lv_rechargelist;
    private RechargeInfoAdapter rechargeInfoAdapter;
    private ArrayList<ChargeList> chargeLists = new ArrayList<>();

    @Override
    protected int getLayoutID() {
        return R.layout.activity_rechargelist;
    }

    @Override
    protected void findViews() {
        lv_rechargelist = (ListView) findViewById(R.id.lv_rechargelist);
        AppContext.getInstance().addActivity(this);
        NavigationView navigationView = (NavigationView) super.findViewById(R.id.nav_main);
        navigationView.setTitle("充值记录");
        navigationView.setClickCallback(this);
    }

    @Override
    protected void init() {
        try {
            rechargeInfoAdapter = new RechargeInfoAdapter(RechargeListInfoActivity.this, chargeLists);
            lv_rechargelist.setAdapter(rechargeInfoAdapter);
            Map<String, String> options = new HashMap<>();
            LoginUser user = LoginUser.getUser();
            options.put("access_token", user.getAccessToken());
            options.put("enterpriseIdx", user.getEnterpriseInfo().getEnterpriseIdx());
            options.put("chargeType", "0");
            options.put("startDate", "");
            options.put("endDate", "");
            options.put("flag", "1");
            ServiceProvider.payService.getchargelist(options).observeOn(AndroidSchedulers.mainThread()).
                    subscribe(new Action1<RechargeInfoResponse>() {
                        @Override
                        public void call(RechargeInfoResponse rechargeInfoResponse) {
                            if (rechargeInfoResponse.getStatus() == StatusCode.RESPONSE_OK) {
                                chargeLists.addAll(rechargeInfoResponse.getResult().getChargeList());
                                rechargeInfoAdapter.notifyDataSetChanged();
                            } else {
                                ToastUtils.showToast(RechargeListInfoActivity.this, rechargeInfoResponse.getMessage());
                            }
                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {
                            throwable.printStackTrace();
                            if (throwable instanceof ConnectException || throwable instanceof RetrofitError) {
                                ToastUtils.showToast(RechargeListInfoActivity.this, "网络异常，无法连接服务器");
                                return;
                            }
                            ToastUtils.showToast(RechargeListInfoActivity.this, "查看充值记录失败");

                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void bindViews() {

    }

    @Override
    public void onBackClick() {
        finish();
    }

    @Override
    public void onRightClick() {

    }
}
