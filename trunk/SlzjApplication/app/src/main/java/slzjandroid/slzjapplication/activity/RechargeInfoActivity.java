package slzjandroid.slzjapplication.activity;

import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import slzjandroid.slzjapplication.R;
import slzjandroid.slzjapplication.adapter.RechargeInfoAdapter;
import slzjandroid.slzjapplication.context.AppContext;
import slzjandroid.slzjapplication.dto.ChargeList;
import slzjandroid.slzjapplication.weights.listview.XListView;

/**
 * Created by hdb on 2016/3/24.
 */
public class RechargeInfoActivity extends BasicActivity {
    private ImageView imv_back;
    private XListView xListView;
    private RechargeInfoAdapter mRechargeInfoAdapter ;
    private List<ChargeList> mList = new ArrayList<>();

    @Override
    protected int getLayoutID() {
        return R.layout.activity_menu_recharge_info;
    }

    @Override
    protected void findViews() {
        imv_back = (ImageView) findViewById(R.id.imv_recharge_info_back);
        xListView = (XListView) findViewById(R.id.xlistview_recharge_info);
    }

    @Override
    protected void init() {
        AppContext.getInstance().addActivity(this);
        mRechargeInfoAdapter = new RechargeInfoAdapter(RechargeInfoActivity.this,mList);
    }

    @Override
    protected void bindViews() {
        xListView.setAdapter(mRechargeInfoAdapter);
        imv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
