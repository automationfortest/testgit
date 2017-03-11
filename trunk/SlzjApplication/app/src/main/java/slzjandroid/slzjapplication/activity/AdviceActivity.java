package slzjandroid.slzjapplication.activity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.util.HashMap;
import java.util.Map;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import slzjandroid.slzjapplication.R;
import slzjandroid.slzjapplication.context.AppContext;
import slzjandroid.slzjapplication.customView.LoadingDialog;
import slzjandroid.slzjapplication.dto.LoginUser;
import slzjandroid.slzjapplication.dto.SuggestResult;
import slzjandroid.slzjapplication.service.ServiceProvider;
import slzjandroid.slzjapplication.utils.CommonUtils;
import slzjandroid.slzjapplication.utils.DialogUtil;
import slzjandroid.slzjapplication.utils.ToastUtils;

/**
 * Created by ASUS on 2016/4/21.
 */
public class AdviceActivity extends BasicActivity implements View.OnClickListener {
    private Button btn_commit;
    private ImageView imv_back;
    private EditText et_context;
    private String messge;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_advice;
    }

    @Override
    protected void findViews() {
        btn_commit = (Button) findViewById(R.id.btn_advice_commit);
        imv_back = (ImageView) findViewById(R.id.imv_advice_back);
        et_context = (EditText) findViewById(R.id.et_context);
    }

    @Override
    protected void init() {
        AppContext.getInstance().addActivity(this);
    }

    @Override
    protected void bindViews() {
        btn_commit.setOnClickListener(this);
        imv_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_advice_commit:
                messge = et_context.getText().toString().trim();
                if (messge.length() <= 500 && CommonUtils.hasText(messge)) {
                    commitSuggest();
                } else {
                    ToastUtils.showToast(AdviceActivity.this, "反馈信息不能为空，请填写！");
                }
                break;
            case R.id.imv_advice_back:
                //   break;
            default:
                finish();
                break;

        }
    }

    private void commitSuggest() {
        final LoadingDialog loadingDialog = DialogUtil.getLoadingDialog(this, "正在取消订单...");
        loadingDialog.show();
        Map<String, String> parmers = new HashMap<>();
        LoginUser user = LoginUser.getUser();
        String accessToken = user.getAccessToken();
        parmers.put("enterpriseIdx", user.getEnterpriseInfo().getEnterpriseIdx());
        parmers.put("departmentIdx", "123");
        parmers.put("messageTitle", "反馈信息");
        parmers.put("messageContent", messge);
        ServiceProvider.adviceService.postSuggest(accessToken, parmers).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<SuggestResult>() {
            @Override
            public void call(SuggestResult suggestResult) {
                loadingDialog.dismiss();
                if (suggestResult.getStatus().equals("200")) {
                    ToastUtils.showToast(AdviceActivity.this, suggestResult.getMessage());
                    finish();
                } else {
                    ToastUtils.showToast(AdviceActivity.this, suggestResult.getMessage());
                }
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                loadingDialog.dismiss();
                ToastUtils.showToast(AdviceActivity.this, "反馈信息提交失败。");
            }
        });
    }
}
