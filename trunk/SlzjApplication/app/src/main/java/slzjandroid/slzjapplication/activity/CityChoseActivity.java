package slzjandroid.slzjapplication.activity;

import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

import slzjandroid.slzjapplication.R;
import slzjandroid.slzjapplication.adapter.CityAdapter;
import slzjandroid.slzjapplication.adapter.HotValueAdapter;
import slzjandroid.slzjapplication.context.AppContext;
import slzjandroid.slzjapplication.customView.QuickindexBar;
import slzjandroid.slzjapplication.db.CityDBManager;
import slzjandroid.slzjapplication.dto.Hotvalue;
import slzjandroid.slzjapplication.dto.LoginUser;
import slzjandroid.slzjapplication.dto.Value;

/**
 * Created by hdb on 2016/4/9.
 */
public class CityChoseActivity extends BasicActivity {

    private QuickindexBar slideBar;
    private ListView lv;
    private ArrayList<Value> citys = new ArrayList<>();
    private GridView gv_hot;
    private TextView tv_zimu;
    private Handler handler;
    private LoginUser loginUser;
    private ArrayList<Value> cityValues = new ArrayList<>();
    CityAdapter adapter;
    private ArrayList<Hotvalue> hotvalues = new ArrayList<>();
    HotValueAdapter hotAdapter;
    private ImageView imv_chose_adress_city_back;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_chosecity;
    }

    @Override
    protected void findViews() {
        imv_chose_adress_city_back = (ImageView) findViewById(R.id.imv_chose_adress_city_back);
        tv_zimu = (TextView) findViewById(R.id.tv_zimu);
        handler = new Handler();
        slideBar = (QuickindexBar) findViewById(R.id.slideBar);
        slideBar.setOnSlideTouchListener(new QuickindexBar.OnSlideTouchListener() {

            @Override
            public void onBack(String str) {

                showZimu(str);

                for (int i = 0; i < citys.size(); i++) {
                    if (citys.get(i).getPinyin().equals(str)) {
                        lv.setSelection(i);
                        break;
                    }
                }
            }
        });

        lv = (ListView) findViewById(R.id.lv);
        gv_hot = (GridView) findViewById(R.id.gv_chose_adress_hot);
        // 进行排序
        hotAdapter = new HotValueAdapter(CityChoseActivity.this, hotvalues);
        adapter = new CityAdapter(CityChoseActivity.this, citys);
        lv.setAdapter(adapter);
        gv_hot.setAdapter(hotAdapter);

    }

    @Override
    protected void init() {
        AppContext.getInstance().addActivity(this);
        loginUser = LoginUser.getUser();
        getCityList();
    }

    @Override
    protected void bindViews() {
        imv_chose_adress_city_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    // 显示在屏幕中间的字母
    private void showZimu(String string) {

        tv_zimu.setVisibility(View.VISIBLE);
        tv_zimu.setText(string);
        handler.removeCallbacksAndMessages(null);
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                tv_zimu.setVisibility(View.GONE);
            }
        }, 1500);
    }

    private void getCityList() {
        CityDBManager cityDbManager = new CityDBManager(CityChoseActivity.this);
        try {

            cityValues = (ArrayList<Value>) cityDbManager.query();
            hotvalues.addAll((ArrayList<Hotvalue>) cityDbManager.queryHotCity());
            Log.i("热门城市", hotvalues.size() + "");
            citys.addAll(cityValues);
            Collections.sort(citys);
            adapter.notifyDataSetChanged();
            hotAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cityDbManager.closeDB();
        }

    }
}
