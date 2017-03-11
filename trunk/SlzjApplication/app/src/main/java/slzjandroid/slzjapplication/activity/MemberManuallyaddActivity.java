package slzjandroid.slzjapplication.activity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
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
import slzjandroid.slzjapplication.adapter.MemberManualaddAdapter;
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
import slzjandroid.slzjapplication.utils.CommonUtils;
import slzjandroid.slzjapplication.utils.DialogUtil;
import slzjandroid.slzjapplication.utils.NumbersUtils;
import slzjandroid.slzjapplication.utils.ToastUtils;

/**
 * Created by ASUS on 2016/4/19.
 */
public class MemberManuallyaddActivity extends BasicActivity implements View.OnClickListener, NavigationView.ClickCallback {
    private TextView tv_menualadd_dept, tv_member_had_manualadded;
    private EditText edt_member_manual_name, edt_member_manual_cellphone;
    private LinearLayout lly_menual_add_choose_part;
    private Button btn_menual_add_commit;
    private ArrayList<DeptInfo> deptInfos = new ArrayList<>();
    private DeptInfo deptInfo;
    private View mContentView;
    private PopupWindow mPopupWindow;
    private MemberManualaddAdapter memberManualaddAdapter;
    private int count = 0;
    private String accessToken;
    private String enterpriseIdx;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_membermanuladd;
    }

    @Override
    protected void findViews() {
        tv_menualadd_dept = (TextView) findViewById(R.id.tv_menualadd_dept);
        edt_member_manual_name = (EditText) findViewById(R.id.edt_member_manual_name);
        edt_member_manual_cellphone = (EditText) findViewById(R.id.edt_member_manual_cellphone);
        lly_menual_add_choose_part = (LinearLayout) findViewById(R.id.lly_menual_add_choose_part);
        btn_menual_add_commit = (Button) findViewById(R.id.btn_menual_add_commit);
        tv_member_had_manualadded = (TextView) findViewById(R.id.tv_member_had_manualadded);
    }


    @Override
    protected void init() {
        AppContext.getInstance().addActivity(this);
        NavigationView navigationView = (NavigationView) super.findViewById(R.id.nav_main);
        navigationView.setTitle("添加成员");
        navigationView.getRightView().setText("从通讯录添加");
        navigationView.setRightViewIsShow(true);
        navigationView.setClickCallback(this);

        LoginUser user = LoginUser.getUser();
        accessToken = user.getAccessToken();
        enterpriseIdx = user.getEnterpriseInfo().getEnterpriseIdx();
        getDept();
        tv_member_had_manualadded.setText(count + "");
    }

    @Override
    protected void bindViews() {
        lly_menual_add_choose_part.setOnClickListener(this);
        btn_menual_add_commit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lly_menual_add_choose_part:
                if (deptInfos == null) {
                    getDept();
                    return;
                }
                getReasonListView(lly_menual_add_choose_part);
                break;
            case R.id.btn_menual_add_commit:
                String name = edt_member_manual_name.getText().toString();
                if (!CommonUtils.hasText(name)) {
                    ToastUtils.showToast(MemberManuallyaddActivity.this, "请您填写姓名");
                    return;
                }
                if (name.length() < 2) {
                    ToastUtils.showToast(MemberManuallyaddActivity.this, "姓名不能最少为2位");
                    return;
                }
                if (edt_member_manual_cellphone.getText().toString().equals("")) {
                    ToastUtils.showToast(MemberManuallyaddActivity.this, "请您填写电话号码");
                    return;
                }
                if (!NumbersUtils.isCellphoneNo(edt_member_manual_cellphone.getText().toString())) {
                    ToastUtils.showToast(MemberManuallyaddActivity.this, "请您填写正确的电话号码");
                    return;
                }
                addMember();
                break;
        }
    }

    /**
     * 类型下拉框
     */
    private void getReasonListView(View v) {
        mContentView = LayoutInflater.from(MemberManuallyaddActivity.this).inflate(
                R.layout.popupwindow_kind, null);
        setPopupWindow(mContentView, v);

        // 类型
        ListView mKindListView = (ListView) mContentView.findViewById(R.id.kind_listview);

        memberManualaddAdapter = new MemberManualaddAdapter(MemberManuallyaddActivity.this, deptInfos);

        mKindListView.setAdapter(memberManualaddAdapter);

        mKindListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @TargetApi(Build.VERSION_CODES.M)
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                tv_menualadd_dept.setText(deptInfos.get(position).getDepartmentName());
                memberManualaddAdapter.mSelectIndex = position;
                deptInfo = deptInfos.get(position);
                memberManualaddAdapter.notifyDataSetChanged();
                mPopupWindow.dismiss();

            }
        });
    }

    /**
     * 设置popupwindow的半透明背景和隐藏
     */
    private void setPopupWindow(View mContentView2, View v) {
        mPopupWindow = new PopupWindow(mContentView, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT, true);
        mPopupWindow.setFocusable(true);
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        mPopupWindow.setBackgroundDrawable(dw);
        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = 1f;
                getWindow().setAttributes(lp);
            }
        });
        mPopupWindow.showAsDropDown(v);
    }

    private void getDept() {
        final LoadingDialog dialog = DialogUtil.getLoadingDialog(MemberManuallyaddActivity.this, "正在获取信息...");
        try {
            Map<String, String> options = new HashMap<>();

            options.put("access_token", accessToken);
            options.put("enterpriseIdx", enterpriseIdx);
            ServiceProvider.deptGetService.getDept(options).observeOn(AndroidSchedulers.mainThread()).
                    subscribe(new Action1<DeptAddResponse>() {
                        @Override
                        public void call(DeptAddResponse deptAddResponse) {
                            dialog.dismiss();
                            if (deptAddResponse.getStatus() == StatusCode.RESPONSE_OK) {
                                ArrayList<DeptInfo> dt = deptAddResponse.getResult().getDeptInfo();
                                for (int i = 0; i < dt.size(); i++) {
                                    if (dt.get(i).getDeptType().equals("0")) {
                                        deptInfos.add(dt.get(i));
                                    }
                                }
                                if (deptInfos.size() > 0) {
                                    deptInfo = deptInfos.get(0);
                                    tv_menualadd_dept.setText(deptInfos.get(0).getDepartmentName());
                                }
                            } else if (deptAddResponse.getStatus() == StatusCode.RESPONSE_ERR) {
                                ToastUtils.showToast(MemberManuallyaddActivity.this, "您离开时间太长，需要重新登陆");
                                AppContext.getInstance().finishAllActivity();
                                startActivity(new Intent(MemberManuallyaddActivity.this, LoginActivity.class));
                            } else {
                                ToastUtils.showToast(MemberManuallyaddActivity.this, deptAddResponse.getMessage());
                            }
                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {
                            throwable.printStackTrace();
                            dialog.dismiss();
                            if (throwable instanceof ConnectException || throwable instanceof RetrofitError) {
                                ToastUtils.showToast(MemberManuallyaddActivity.this, "网络异常，无法连接服务器");
                                return;
                            }
                            ToastUtils.showToast(MemberManuallyaddActivity.this, "获取模板信息失败");
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
            dialog.dismiss();
            ToastUtils.showToast(MemberManuallyaddActivity.this, "网络错误，请您检查网络并重新登陆");
        }

    }

    private void addMember() {
        final LoadingDialog dialog = DialogUtil.getLoadingDialog(MemberManuallyaddActivity.this, "正在添加...");
        try {
            Map<String, String> map = new HashMap<>();
            map.put("enterpriseIdx", enterpriseIdx);
            MemberAddBean bean = new MemberAddBean();
            bean.setClientUsrName(edt_member_manual_name.getText().toString());
            bean.setClientUsrCellphone(edt_member_manual_cellphone.getText().toString());
            ArrayList<DeptInfo> deptlist = new ArrayList<>();
            deptlist.add(deptInfo);
            bean.setDeptInfo(deptlist);
            bean.setUsrType("1");
            ArrayList<MemberAddBean> beanlist = new ArrayList<>();
            beanlist.add(bean);
            MemberAddRequest request = new MemberAddRequest();
            request.setClientUserInfoList(beanlist);

            try {
                map.put("clientUserInfoList", Base64.encode(MemberAddGson.toRequst(request).getBytes("UTF-8")));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            map.put("flag", "0");

            dialog.show();
            ServiceProvider.teamMenagentService.getAddResult(accessToken, map).observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<OnlyResponse>() {
                        @Override
                        public void call(OnlyResponse onlyResponse) {
                            dialog.dismiss();
                            if (onlyResponse.getStatus() == StatusCode.RESPONSE_OK) {
                                count++;
                                tv_member_had_manualadded.setText(count + "");
                                ToastUtils.showToast(MemberManuallyaddActivity.this, onlyResponse.getMessage());

                            } else if (onlyResponse.getStatus() == StatusCode.RESPONSE_ERR) {
                                ToastUtils.showToast(MemberManuallyaddActivity.this, "您离开时间太长，需要重新登陆");
                                AppContext.getInstance().finishAllActivity();
                                startActivity(new Intent(MemberManuallyaddActivity.this, LoginActivity.class));
                            } else {
                                ToastUtils.showToast(MemberManuallyaddActivity.this, onlyResponse.getMessage());
                            }
                            clearData();
                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {
                            throwable.printStackTrace();
                            dialog.dismiss();
                            if (throwable instanceof ConnectException || throwable instanceof RetrofitError) {
                                ToastUtils.showToast(MemberManuallyaddActivity.this, "网络异常，无法连接服务器");
                                return;
                            }
                            ToastUtils.showToast(MemberManuallyaddActivity.this, "添加失败");
                        }
                    });
        } catch (Exception e) {
            dialog.dismiss();
            ToastUtils.showToast(MemberManuallyaddActivity.this, "添加失败");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == RESULT_OK) {
            setResult(RESULT_OK);
            finish();
        }
    }

    @Override
    public void onBackClick() {
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void onRightClick() {
        startActivityForResult(new Intent(MemberManuallyaddActivity.this, MemberAddConnectsActivity.class), 0);

    }

    private void clearData() {
        edt_member_manual_name.setText("");
        edt_member_manual_cellphone.setText("");
        tv_menualadd_dept.setText("公司");

    }
}
