package slzjandroid.slzjapplication.activity;

import android.content.Intent;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ListView;
import android.widget.TextView;

import java.net.ConnectException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit.RetrofitError;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import slzjandroid.slzjapplication.R;
import slzjandroid.slzjapplication.adapter.TeamMenagentAdapter;
import slzjandroid.slzjapplication.context.AppContext;
import slzjandroid.slzjapplication.customView.NavigationView;
import slzjandroid.slzjapplication.dto.ClientInfoResponse;
import slzjandroid.slzjapplication.dto.ClientInfoResult;
import slzjandroid.slzjapplication.dto.ClientUserInfo;
import slzjandroid.slzjapplication.dto.EnterpriseInfo;
import slzjandroid.slzjapplication.dto.LoginUser;
import slzjandroid.slzjapplication.service.ServiceProvider;
import slzjandroid.slzjapplication.utils.CommonUtils;
import slzjandroid.slzjapplication.utils.ToastUtils;
import slzjandroid.slzjapplication.weights.DeletableEditText;
import slzjandroid.slzjapplication.weights.listview.pulltorefresh.library.PullToRefreshBase;
import slzjandroid.slzjapplication.weights.listview.pulltorefresh.library.PullToRefreshListView;

/**
 * Created by hdb on 2016/4/11.
 */
public class TeamManagentActivity extends BasicActivity implements PullToRefreshBase.OnRefreshListener2<ListView>, NavigationView.ClickCallback, TeamMenagentAdapter.IRefreh {
    private TextView tv_team_managent_total_person, tv_team_menagent_manager;
    private DeletableEditText edt_team_menagent_search;
    private PullToRefreshListView lv;
    private LoginUser loginUser;
    private EnterpriseInfo enterpriseInfo;
    private String keyWord = "";
    private int perpage = 15;
    private int allPage = 0;
    private int currentPage = 0;
    private TeamMenagentAdapter adapter;
    private String totalperson = "";
    private String managerperson = "";

    private boolean flag = true;
    private int tag = 0;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_menu_team_managent;
    }

    @Override
    protected void findViews() {
        tv_team_managent_total_person = (TextView) findViewById(R.id.tv_team_managent_total_person);
        tv_team_menagent_manager = (TextView) findViewById(R.id.tv_team_menagent_manager);
        edt_team_menagent_search = (DeletableEditText) findViewById(R.id.edt_team_menagent_search);
        lv = (PullToRefreshListView) findViewById(R.id.list_team_menagent_member);
    }

    @Override
    protected void init() {
        AppContext.getInstance().addActivity(this);
        NavigationView navigationView = (NavigationView) super.findViewById(R.id.nav_main);
        navigationView.setTitle("成员");
        navigationView.getRightView().setText("添加");
        navigationView.setRightViewIsShow(true);
        navigationView.setClickCallback(this);

        loginUser = LoginUser.getUser();
        enterpriseInfo = loginUser.getEnterpriseInfo();
        adapter = new TeamMenagentAdapter(TeamManagentActivity.this);
        adapter.setiRefreh(this);
        lv.setAdapter(adapter);

    }

    @Override
    protected void onResume() {
        super.onResume();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                lv.setRefreshing(true);
            }
        }, 800);

    }


    @Override
    protected void bindViews() {
        lv.setOnRefreshListener(this);

        edt_team_menagent_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                keyWord = edt_team_menagent_search.getText().toString().trim();
                flag = true;
                lv.setRefreshing(true);
            }
        });
    }

    /**
     * 查询和获取都是走这个接口
     */
    public void getMenberList() {
        Map<String, String> map = new HashMap<>();
        map.put("access_token", loginUser.getAccessToken());
        map.put("enterpriseIdx", enterpriseInfo.getEnterpriseIdx());

        if (CommonUtils.hasText(keyWord)) {
            map.put("keyword", keyWord);
        }
        map.put("perpage", perpage + "");
        map.put("currentPage", currentPage + "");
        try {
            ServiceProvider.teamMenagentService.getMenberList(map).observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<ClientInfoResponse>() {
                        @Override
                        public void call(ClientInfoResponse clientInfoResponse) {
                            lv.onRefreshComplete();
                            if (clientInfoResponse.getStatus().equals("200")) {
                                ClientInfoResult result = clientInfoResponse.getResult();
                                allPage = Integer.valueOf(result.getTotalPage());
                                ArrayList<ClientUserInfo> clientUserInfo = result.getClientUserInfo();
                                adapter.refrehData(clientUserInfo);
                                totalperson = clientInfoResponse.getResult().getTotalClientCnt();
                                managerperson = clientInfoResponse.getResult().getAdminCnt();

                                tv_team_managent_total_person.setText(totalperson);
                                tv_team_menagent_manager.setText(managerperson);

                            } else if (clientInfoResponse.getStatus().equals("401")) {
                                ToastUtils.showToast(TeamManagentActivity.this, "您离开时间太长，需要重新登陆");
                                AppContext.getInstance().finishAllActivity();
                                startActivity(new Intent(TeamManagentActivity.this, LoginActivity.class));
                            } else {
                                ToastUtils.showToast(TeamManagentActivity.this, clientInfoResponse.getMessage());
                            }


                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {
                            throwable.printStackTrace();
                            lv.onRefreshComplete();
                            if (throwable instanceof ConnectException || throwable instanceof RetrofitError) {
                                ToastUtils.showToast(TeamManagentActivity.this, "网络异常，无法连接服务器");
                                return;
                            }
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
            lv.onRefreshComplete();
        }
    }


    @Override
    public void onBackClick() {
        finish();
    }


    @Override
    public void onRightClick() {
        startActivityForResult(new Intent(TeamManagentActivity.this, MemberManuallyaddActivity.class), 1);
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
        String label = CommonUtils.setRefreshTime(TeamManagentActivity.this);
        refreshView.getLoadingLayoutProxy().setLastUpdatedLabel("最后更新:" + "\t" + label);
        if (tag == 0) {
            flag = true;
        }
        dowoningData();
    }


    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
        if (tag == 0) {
            flag = true;
        }
        dowoningData();
    }

    public void dowoningData() {
        if (flag) {
            currentPage = 1;
            getMenberList();
            flag = false;
        } else {
            if (currentPage < Integer.valueOf(allPage)) {
                currentPage++;
                getMenberList();
            } else {
                colseHeader();
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            flag = true;
        }
    }

    private void colseHeader() {
        lv.postDelayed(new Runnable() {
            @Override
            public void run() {
                lv.onRefreshComplete();
            }
        }, 1000);
    }

    @Override
    public void onRefreh() {
        flag = true;
        lv.setRefreshing(true);
    }
}
