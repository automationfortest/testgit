package slzjandroid.slzjapplication.activity;

import android.content.Intent;
import android.widget.ListView;
import android.widget.TextView;

import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit.RetrofitError;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import slzjandroid.slzjapplication.R;
import slzjandroid.slzjapplication.adapter.MemberAddCommitAdapter;
import slzjandroid.slzjapplication.context.AppContext;
import slzjandroid.slzjapplication.customView.LoadingDialog;
import slzjandroid.slzjapplication.customView.NavigationView;
import slzjandroid.slzjapplication.dto.DeptAddResponse;
import slzjandroid.slzjapplication.dto.DeptInfo;
import slzjandroid.slzjapplication.dto.LoginUser;
import slzjandroid.slzjapplication.dto.MemberAddBean;
import slzjandroid.slzjapplication.dto.MemberAddGson;
import slzjandroid.slzjapplication.dto.MemberAddRequest;
import slzjandroid.slzjapplication.dto.OnlyResponse;
import slzjandroid.slzjapplication.lang.StatusCode;
import slzjandroid.slzjapplication.service.ServiceProvider;
import slzjandroid.slzjapplication.utils.Base64;
import slzjandroid.slzjapplication.utils.DialogUtil;
import slzjandroid.slzjapplication.utils.ToastUtils;

/**
 * Created by ASUS on 2016/4/22.
 */
public class MenberAddCommitActivity extends BasicActivity implements NavigationView.ClickCallback {

    private TextView tv_memberadd_commit_num;
    private ListView lv_memberadd_commit;
    private ArrayList<DeptInfo> deptInfos = new ArrayList<>();
    private ArrayList<MemberAddBean> clientUserInfos = new ArrayList<>();
    private MemberAddCommitAdapter memberAddCommitAdapter;
    private TextView rightView;
    private String accessToken;
    private String enterpriseIdx;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_memberadd_commit;
    }

    @Override
    protected void findViews() {
        tv_memberadd_commit_num = (TextView) findViewById(R.id.tv_memberadd_commit_num);
        lv_memberadd_commit = (ListView) findViewById(R.id.lv_memberadd_commit);
    }

    @Override
    protected void init() {
        AppContext.getInstance().addActivity(this);

        NavigationView navigationView = (NavigationView) super.findViewById(R.id.nav_main);
        navigationView.setTitle("成员列表");
        navigationView.setRightViewIsShow(true);
        navigationView.setRightTile("确定");
        rightView = navigationView.getRightView();
        navigationView.setClickCallback(this);

        LoginUser user = LoginUser.getUser();
        accessToken = user.getAccessToken();
        enterpriseIdx = user.getEnterpriseInfo().getEnterpriseIdx();

        clientUserInfos = (ArrayList<MemberAddBean>) getIntent().getSerializableExtra("memberchosed");
        tv_memberadd_commit_num.setText(clientUserInfos.size() + "");

        memberAddCommitAdapter = new MemberAddCommitAdapter(MenberAddCommitActivity.this, clientUserInfos, deptInfos);
        lv_memberadd_commit.setAdapter(memberAddCommitAdapter);
        getDeptData();
    }

    @Override
    protected void bindViews() {

    }

    private void getDeptData() {
        try {
            Map<String, String> options = new HashMap<>();
            options.put("access_token", accessToken);
            options.put("enterpriseIdx", enterpriseIdx);
            ServiceProvider.deptGetService.getDept(options).observeOn(AndroidSchedulers.mainThread()).
                    subscribe(new Action1<DeptAddResponse>() {
                        @Override
                        public void call(DeptAddResponse deptAddResponse) {
                            if (deptAddResponse.getStatus() == StatusCode.RESPONSE_OK) {
                                ArrayList<DeptInfo> dt = deptAddResponse.getResult().getDeptInfo();
                                for (int i = 0; i < dt.size(); i++) {
                                    if (dt.get(i).getDeptType().equals("0")) {
                                        deptInfos.add(dt.get(i));
                                    }
                                }
                                for (int i = 0; i < clientUserInfos.size(); i++) {
                                    ArrayList<DeptInfo> beans = new ArrayList<DeptInfo>();
                                    DeptInfo dept = deptInfos.get(0);
                                    beans.add(dept);
                                    clientUserInfos.get(i).setDeptInfo(beans);
                                }
                                memberAddCommitAdapter.notifyDataSetChanged();
                            }
                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {
                            throwable.printStackTrace();
                            if (throwable instanceof ConnectException || throwable instanceof RetrofitError) {
                                ToastUtils.showToast(MenberAddCommitActivity.this, "网络异常，无法连接服务器");
                                return;
                            }
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void addMember() {
        rightView.setEnabled(false);
        final LoadingDialog dialog = DialogUtil.getLoadingDialog(MenberAddCommitActivity.this, "正在添加...");
        try {
            Map<String, String> map = new HashMap<>();
            map.put("enterpriseIdx", enterpriseIdx);
            MemberAddRequest request = new MemberAddRequest();
            request.setClientUserInfoList(clientUserInfos);

            try {
                map.put("clientUserInfoList", Base64.encode(MemberAddGson.toRequst(request).getBytes("UTF-8")));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            map.put("flag", "1");
            ServiceProvider.teamMenagentService.getAddResult(accessToken, map).observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<OnlyResponse>() {
                        @Override
                        public void call(OnlyResponse onlyResponse) {
                            rightView.setEnabled(true);
                            dialog.dismiss();
                            if (onlyResponse.getStatus() == StatusCode.RESPONSE_OK) {
                                setResult(RESULT_OK);
                                finish();
                                ToastUtils.showToast(MenberAddCommitActivity.this, onlyResponse.getMessage());
                            } else if (onlyResponse.getStatus() == StatusCode.RESPONSE_ERR) {
                                ToastUtils.showToast(MenberAddCommitActivity.this, "您离开时间太长，需要重新登陆");
                                AppContext.getInstance().finishAllActivity();
                                startActivity(new Intent(MenberAddCommitActivity.this, LoginActivity.class));
                            } else {

                                ToastUtils.showToast(MenberAddCommitActivity.this, onlyResponse.getMessage());
                            }
                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {
                            rightView.setEnabled(true);
                            throwable.printStackTrace();
                            dialog.dismiss();
                            if (throwable instanceof ConnectException || throwable instanceof RetrofitError) {
                                ToastUtils.showToast(MenberAddCommitActivity.this, "网络异常，无法连接服务器");
                                return;
                            }
                            ToastUtils.showToast(MenberAddCommitActivity.this, "添加失败");

                        }
                    });
        } catch (Exception e) {
            rightView.setEnabled(true);
            dialog.dismiss();
            ToastUtils.showToast(MenberAddCommitActivity.this, "添加失败");
        }
    }

    @Override
    public void onBackClick() {
        finish();
    }

    @Override
    public void onRightClick() {
        if (deptInfos.size() == 0) {
            //部门数据没有初始化，继续初始化
            getDeptData();
            return;
        } else {
            addMember();
        }
    }
}
