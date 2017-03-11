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
import slzjandroid.slzjapplication.adapter.DeptGetAdapter;
import slzjandroid.slzjapplication.context.AppContext;
import slzjandroid.slzjapplication.customView.LoadingDialog;
import slzjandroid.slzjapplication.customView.NavigationView;
import slzjandroid.slzjapplication.dto.ClientUserInfo;
import slzjandroid.slzjapplication.dto.DepartmentInfo;
import slzjandroid.slzjapplication.dto.DeptGetResponse;
import slzjandroid.slzjapplication.dto.EditMemberBean;
import slzjandroid.slzjapplication.dto.EditMemberGson;
import slzjandroid.slzjapplication.dto.LoginUser;
import slzjandroid.slzjapplication.dto.OnlyResponse;
import slzjandroid.slzjapplication.lang.StatusCode;
import slzjandroid.slzjapplication.service.ServiceProvider;
import slzjandroid.slzjapplication.utils.Base64;
import slzjandroid.slzjapplication.utils.CommonUtils;
import slzjandroid.slzjapplication.utils.DialogUtil;
import slzjandroid.slzjapplication.utils.NumbersUtils;
import slzjandroid.slzjapplication.utils.ToastUtils;

/**
 * Created by ASUS on 2016/4/25.
 */
public class MemberEditActivity extends BasicActivity implements View.OnClickListener, NavigationView.ClickCallback {
    private EditText edt_member_edit_cell, edt_member_edit_name;
    private TextView tv_member_edit_character, tv_member_edit_dept, tv_member_edit_project, tv_edit_member_commit;
    private LinearLayout lly_edit_dept, lly_edit_project;
    private ArrayList<DepartmentInfo> changeproject = new ArrayList<>();
    private ArrayList<DepartmentInfo> datas = new ArrayList<>();
    private ArrayList<DepartmentInfo> dept = new ArrayList<>();
    private ArrayList<DepartmentInfo> allType = new ArrayList<>();
    private DeptGetAdapter deptCommitAdapter;
    private View mContentView;
    private PopupWindow mPopupWindow;
    private ClientUserInfo clientUserInfo;
    private EditMemberBean bean = new EditMemberBean();
    private String accessToken;
    private String enterpriseIdx;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_edit_member;
    }

    @Override
    protected void findViews() {
        edt_member_edit_cell = (EditText) findViewById(R.id.edt_member_edit_cell);
        edt_member_edit_name = (EditText) findViewById(R.id.edt_member_edit_name);
        tv_member_edit_character = (TextView) findViewById(R.id.tv_member_edit_character);
        tv_member_edit_dept = (TextView) findViewById(R.id.tv_member_edit_dept);
        tv_member_edit_project = (TextView) findViewById(R.id.tv_member_edit_project);
        lly_edit_dept = (LinearLayout) findViewById(R.id.lly_edit_dept);
        lly_edit_project = (LinearLayout) findViewById(R.id.lly_edit_project);
    }

    @Override
    protected void init() {
        AppContext.getInstance().addActivity(this);

        NavigationView navigationView = (NavigationView) super.findViewById(R.id.nav_main);
        navigationView.setTitle("编辑成员");
        navigationView.setRightViewIsShow(true);
        navigationView.getRightView().setText("完成");
        navigationView.setClickCallback(this);

        if (null == datas) {
            datas = new ArrayList<>();
        } else {
            datas.clear();
        }
        clientUserInfo = (ClientUserInfo) getIntent().getSerializableExtra("clientUserInfo");
        edt_member_edit_cell.setText(clientUserInfo.getClientUsrCellphone());
        edt_member_edit_name.setText(clientUserInfo.getClientUsrName());

        if (clientUserInfo.getUsrType().equals("1")) {
            tv_member_edit_character.setText("用户");
            edt_member_edit_cell.setEnabled(true);

        } else {
            tv_member_edit_character.setText("管理员");
            edt_member_edit_cell.setEnabled(false);
            edt_member_edit_cell.setTextColor(getResources().getColor(R.color.edt_gray_font));
        }
        ArrayList<DepartmentInfo> deptInfo = clientUserInfo.getDeptInfo();
        StringBuilder builder = new StringBuilder();
        if (deptInfo.size() != 0) {
            for (DepartmentInfo data : deptInfo) {
                if (data.getDeptType().equals("0")) {
                    tv_member_edit_dept.setText(data.getDepartmentName());
                    dept.add(data);
                } else {
                    changeproject.add(data);
                    lly_edit_project.setVisibility(View.VISIBLE);
                    builder.append(data.getDepartmentName());
                    builder.append("\t");
                    if (builder.toString().length() <= 18) {
                        tv_member_edit_project.setText(builder.toString());
                    } else {
                        builder.append("...");
                        tv_member_edit_project.setText(builder.toString());
                    }

                }
            }
        }

        LoginUser user = LoginUser.getUser();
        accessToken = user.getAccessToken();
        enterpriseIdx = user.getEnterpriseInfo().getEnterpriseIdx();

        getDeptinfo();

    }

    @Override
    protected void bindViews() {
        lly_edit_dept.setOnClickListener(this);
        lly_edit_project.setOnClickListener(this);
    }

    private void getDeptinfo() {
        try {
            Map<String, String> options = new HashMap<>();
            LoginUser user = LoginUser.getUser();
            String accessToken = user.getAccessToken();
            String enterpriseIdx = user.getEnterpriseInfo().getEnterpriseIdx();

            options.put("access_token", accessToken);
            options.put("enterpriseIdx", enterpriseIdx);
            ServiceProvider.deptGetService.deptGet(options).observeOn(AndroidSchedulers.mainThread()).
                    subscribe(new Action1<DeptGetResponse>() {
                        @Override
                        public void call(DeptGetResponse deptAddResponse) {
                            if (deptAddResponse.getStatus() == StatusCode.RESPONSE_OK) {
                                ArrayList<DepartmentInfo> dt = deptAddResponse.getResult().getDeptInfo();
                                for (int i = 0; i < dt.size(); i++) {
                                    if (dt.get(i).getDeptType().equals("0")) {
                                        datas.add(dt.get(i));
                                    } else {
                                        allType.add(dt.get(i));
                                    }
                                }
                                if (allType.size() != 0) {
                                    lly_edit_project.setVisibility(View.VISIBLE);
                                } else {
                                    lly_edit_project.setVisibility(View.GONE);
                                }
                            } else if (deptAddResponse.getStatus() == StatusCode.RESPONSE_ERR) {
                                ToastUtils.showToast(MemberEditActivity.this, "您离开时间太长，需要重新登陆");
                                AppContext.getInstance().finishAllActivity();
                                startActivity(new Intent(MemberEditActivity.this, LoginActivity.class));
                            }
                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {
                            throwable.printStackTrace();
                            if (throwable instanceof ConnectException || throwable instanceof RetrofitError) {
                                ToastUtils.showToast(MemberEditActivity.this, "网络异常，无法连接服务器");
                                return;
                            }
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.lly_edit_dept:
                if (datas.size() == 0) {
                    return;
                } else {
                    getReasonListView(lly_edit_dept);
                }
                break;
            case R.id.lly_edit_project:
                if (allType.size() == 0) {
                    return;
                } else {
                    setDataTag();
                    Intent intent = new Intent(MemberEditActivity.this, MemberEditDeptActivity.class);
                    intent.putExtra("projects", allType);
                    intent.putExtra("dept", dept);
                    startActivityForResult(intent, 0);
                }
                break;

            default:
                break;
        }
    }

    /**
     * 类型下拉框
     */
    private void getReasonListView(View v) {
        mContentView = LayoutInflater.from(this).inflate(
                R.layout.popupwindow_kind, null);
        setPopupWindow(mContentView, v);

        // 类型
        ListView mKindListView = (ListView) mContentView
                .findViewById(R.id.kind_listview);
        deptCommitAdapter = new DeptGetAdapter(this, datas);

        mKindListView.setAdapter(deptCommitAdapter);

        mKindListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @TargetApi(Build.VERSION_CODES.M)
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (dept.size() != 0) {
                    dept.clear();
                }
                dept.add(datas.get(position));
                tv_member_edit_dept.setText(datas.get(position).getDepartmentName());
                DeptGetAdapter.mSelectIndex = position;
                deptCommitAdapter.notifyDataSetChanged();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == RESULT_OK) {
            changeproject = (ArrayList<DepartmentInfo>) data.getSerializableExtra("changeproject");
            dept = (ArrayList<DepartmentInfo>) data.getSerializableExtra("dept");
            if (null != changeproject) {
                StringBuilder builder = new StringBuilder();
                if (null != changeproject && changeproject.size() > 0) {
                    tv_member_edit_project.setVisibility(View.VISIBLE);
                    for (int i = 0; i < changeproject.size(); i++) {
                        DepartmentInfo departmentInfo = changeproject.get(i);
                        builder.append(departmentInfo.getDepartmentName());
                        builder.append("\t");
                        String type = builder.toString();
                        if (type.length() <= 18) {
                            tv_member_edit_project.setText(type);
                        } else {
                            builder.append("...");
                            tv_member_edit_project.setText(type);
                        }
                    }
                }
            }
        }
    }

    /**
     * 编辑信息
     */
    private void editInfo() {
        final LoadingDialog dialog = DialogUtil.getLoadingDialog(MemberEditActivity.this, "正在编辑...");
        try {
            ClientUserInfo info = new ClientUserInfo();
            String name = edt_member_edit_name.getText().toString();
            if (CommonUtils.hasText(name)) {
                info.setClientUsrName(name);
            } else {
                ToastUtils.showToast(MemberEditActivity.this, "您的姓名不能为空");
                return;
            }

            String pohone = edt_member_edit_cell.getText().toString();
            if (pohone.length() == 11) {
                if (CommonUtils.hasText(pohone) && NumbersUtils.isCellphoneNo(pohone)) {
                    info.setClientUsrCellphone(pohone);
                } else {
                    ToastUtils.showToast(MemberEditActivity.this, "您输入的手机号不合法");
                    return;
                }
            } else {
                ToastUtils.showToast(MemberEditActivity.this, "您输入的手机号长度不合法");
                return;
            }

            info.setClientUsrIdx(clientUserInfo.getClientUsrIdx());
            info.setUsrType(clientUserInfo.getUsrType());
            if (dept.size() > 0) {
                changeproject.add(dept.get(0));
            }
            info.setDeptInfo(changeproject);
            ArrayList<ClientUserInfo> clientUserInfos = new ArrayList<>();
            clientUserInfos.add(info);
            bean.setClientUserInfoList(clientUserInfos);
            Map<String, String> options = new HashMap<>();

            options.put("enterpriseIdx", enterpriseIdx);
            String json = EditMemberGson.toRequst(bean);
            try {
                options.put("clientUserInfoList", Base64.encode(json.getBytes("UTF-8")));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                dialog.dismiss();
            }
            ServiceProvider.teamMenagentService.editMember(accessToken, options)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<OnlyResponse>() {
                        @Override
                        public void call(OnlyResponse onlyResponse) {
                            if (onlyResponse.getStatus() == StatusCode.RESPONSE_OK) {
                                ToastUtils.showToast(MemberEditActivity.this, onlyResponse.getMessage());
                                setResult(RESULT_OK);
                                finish();
                            } else {
                                ToastUtils.showToast(MemberEditActivity.this, onlyResponse.getMessage());
                            }
                        }
                    });

        } catch (Exception e) {
            e.printStackTrace();
            dialog.dismiss();
        }

    }

    @Override
    public void onBackClick() {
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void onRightClick() {
        editInfo();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(RESULT_OK);
        finish();
    }

    private void setDataTag() {
        for (DepartmentInfo data : allType) {
            data.setChoosed(false);
        }
        for (int i = 0; i < allType.size(); i++)
            for (int j = 0; j < changeproject.size(); j++) {
                DepartmentInfo mData = allType.get(i);
                DepartmentInfo data = changeproject.get(j);
                if (mData.getDepartmentName().equals(data.getDepartmentName())) {
                    mData.setChoosed(true);
                }
            }
    }
}
