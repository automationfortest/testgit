package slzjandroid.slzjapplication.activity;

import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import slzjandroid.slzjapplication.R;
import slzjandroid.slzjapplication.context.AppContext;
import slzjandroid.slzjapplication.customView.NavigationView;
import slzjandroid.slzjapplication.dto.ChargeInfoResult;
import slzjandroid.slzjapplication.dto.ChargeList;
import slzjandroid.slzjapplication.dto.EnterpriseInfo;
import slzjandroid.slzjapplication.dto.LoginUser;
import slzjandroid.slzjapplication.dto.ReceiptRequest;
import slzjandroid.slzjapplication.dto.RechargeInfoResponse;
import slzjandroid.slzjapplication.dto.RegisterResponse;
import slzjandroid.slzjapplication.dto.TicketTemplateResponse;
import slzjandroid.slzjapplication.lang.StatusCode;
import slzjandroid.slzjapplication.service.ServiceProvider;
import slzjandroid.slzjapplication.utils.Base64;
import slzjandroid.slzjapplication.utils.CommonUtils;
import slzjandroid.slzjapplication.utils.NumbersUtils;
import slzjandroid.slzjapplication.utils.ToastUtils;

/**
 * 发票管理
 * Created by xuyifei on 16/5/9
 */
public class ReceiptActivity extends BasicActivity implements View.OnClickListener, NavigationView.ClickCallback {

    private TextView amountTv;
    private TextView selectNumberTv;
    private EditText receiptTitleET;
    private EditText nameET;
    private EditText phoneEt;
    private EditText addressEt;
    private EditText zipCodeEt;
    private TextView serviceTypeTv;

    private Button commonBtn;

    private ReceiptRequest receiptRequest;
    private List<String> chargeTransNos;

    private ArrayList<Integer> indexs;
    private LoginUser user;
    private ArrayList<ChargeList> chargeList;


    @Override
    protected int getLayoutID() {
        return R.layout.activity_receipt;
    }

    @Override
    protected void findViews() {
        amountTv = (TextView) findViewById(R.id.tv_amount_number);
        selectNumberTv = (TextView) findViewById(R.id.tv_select_number);

        receiptTitleET = (EditText) findViewById(R.id.et_receipt_title);
        serviceTypeTv = (TextView) findViewById(R.id.tv_service_type);

        nameET = (EditText) findViewById(R.id.et_name);
        phoneEt = (EditText) findViewById(R.id.phone_et);
        addressEt = (EditText) findViewById(R.id.et_address);
        zipCodeEt = (EditText) findViewById(R.id.et_zip_code);

        commonBtn = (Button) findViewById(R.id.btn_common);

    }


    @Override
    protected void onResume() {
        super.onResume();
        getDate();
        getTicktModle();
    }

    @Override
    protected void init() {
        AppContext.getInstance().addActivity(this);
        NavigationView navigationView = (NavigationView) super.findViewById(R.id.nav_main);
        navigationView.setTitle("开具发票");
        navigationView.setRightViewIsShow(true);
        navigationView.setRightTile("发票记录");
        navigationView.setClickCallback(this);
        user = LoginUser.getUser();

        if (null == chargeTransNos) {
            chargeTransNos = new ArrayList<>();
        }
    }

    @Override
    protected void bindViews() {
        selectNumberTv.setOnClickListener(this);
        commonBtn.setOnClickListener(this);

    }


    @Override
    public void onBackClick() {
        finish();
    }

    @Override
    public void onRightClick() {
        Intent intent = new Intent(ReceiptActivity.this, ReceiptRecordActivity.class);
        startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_select_number:
                final Intent intent = new Intent(ReceiptActivity.this, SelectedAmountActivity.class);
                if (null != indexs && indexs.size() != 0) {
                    intent.putIntegerArrayListExtra("indexs", indexs);
                    intent.putExtra("datas", chargeList);
                    startActivityForResult(intent, 0);
                } else {
                    if (null == chargeList) {
                        intent.putExtra("datas", new ChargeList());
                    }
                    intent.putExtra("datas", chargeList);
                    startActivityForResult(intent, 0);
                }
                break;
            case R.id.btn_common:
                if (isMsgComplete()) {
                    postOrders();
                }
                break;
        }

    }


    /**
     * 判断信息填写的是否完整
     *
     * @return
     */
    private boolean isMsgComplete() {
        receiptRequest = new ReceiptRequest();
        String amount = amountTv.getText().toString();
        String receiptTitle = receiptTitleET.getText().toString();
        String name = nameET.getText().toString();
        String phone = phoneEt.getText().toString();
        String address = addressEt.getText().toString();
        String zipCode = zipCodeEt.getText().toString();

        if (!CommonUtils.hasText(amount)) {
            ToastUtils.showToast(ReceiptActivity.this, "开具金额不能为空");
            return false;
        }
        if (!CommonUtils.hasText(receiptTitle)) {
            ToastUtils.showToast(ReceiptActivity.this, "发票抬头不能为空");
            return false;
        } else if (receiptTitle.length() < 2) {
            ToastUtils.showToast(ReceiptActivity.this, "发票抬头长度不能小于2位");
            return false;
        }
        if (!CommonUtils.hasText(name)) {
            ToastUtils.showToast(ReceiptActivity.this, "姓名不能为空");
            return false;
        }
        if (!CommonUtils.hasText(phone)) {
            ToastUtils.showToast(ReceiptActivity.this, "联系人电话不能空");
            return false;
        }

        if (!NumbersUtils.isCellphoneNo(phone)) {
            ToastUtils.showToast(ReceiptActivity.this, "您输入的手机号不合法");
            return false;
        }

        if (!CommonUtils.hasText(address)) {
            ToastUtils.showToast(ReceiptActivity.this, "地址不能为空");
            return false;
        } else if (address.length() < 2) {
            ToastUtils.showToast(ReceiptActivity.this, "地址长度不能小于2位");
            return false;
        }
        if (CommonUtils.hasText(zipCode)) {
            if (!NumbersUtils.isZipCode(zipCode)) {
                ToastUtils.showToast(ReceiptActivity.this, "您输入的邮编不合法");
                return false;
            }
        }

        receiptRequest.setTitle(receiptTitle);
        if (CommonUtils.hasText(amount)) {
            String newAmount = amount.substring(0, amount.length() - 1);
            Double.parseDouble(newAmount);
        } else {
            amount = "0.0";
        }
        receiptRequest.setTicketAmount(amount);
        receiptRequest.setContactName(name);
        receiptRequest.setContactCellphone(phone);
        receiptRequest.setContactAddress(address);
        receiptRequest.setContactZip(zipCode);

        return true;
    }


    /**
     * 下单
     */
    private void postOrders() {
        Map<String, Object> parems = new HashMap<>();
        LoginUser user = LoginUser.getUser();
        EnterpriseInfo enterpriseInfo = LoginUser.getEnterpriseInfoforUser(user);

        parems.put("enterpriseIdx", enterpriseInfo.getEnterpriseIdx());
        parems.put("title", receiptRequest.getTitle());

        String ticketContent = amountTv.getText().toString().trim();
        if (CommonUtils.hasText(ticketContent)) {
            String nStringPrice = ticketContent.substring(0, ticketContent.length() - 1);
            parems.put("ticketAmount", nStringPrice.trim());
        }
        parems.put("ticketContent", "服务费");
        parems.put("contactName", receiptRequest.getContactName());
        parems.put("contactCellphone", receiptRequest.getContactCellphone());
        parems.put("contactAddress", receiptRequest.getContactAddress());
        parems.put("contactZip", receiptRequest.getContactZip());

        if (chargeTransNos.size() == 0) {
            ToastUtils.showToast(ReceiptActivity.this, "您还没有选择要开具的金额");
            return;
        }
        Gson gosn = new Gson();
        if (null != chargeTransNos && chargeTransNos.size() != 0) {

            List<Map<String, String>> listDatas = new ArrayList<>();
            Map<String, String> datas = new Hashtable<>();
            for (String chargeTrasNo : chargeTransNos) {
                datas.put("chargeTransNo", chargeTrasNo);
                listDatas.add(datas);
            }

            String code = gosn.toJson(listDatas);
            try {
                String encode = Base64.encode(code.getBytes("UTF-8"));
                parems.put("chargeTransNoList", encode);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        ServiceProvider.receiptService.postReceipt(user.getAccessToken(), parems)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<RegisterResponse>() {
                    @Override
                    public void call(RegisterResponse registerResponse) {
                        if (registerResponse.getStatus() == StatusCode.RESPONSE_OK) {
                            ToastUtils.showToast(ReceiptActivity.this, "开具发票成功");
                            Intent intent = new Intent(ReceiptActivity.this, ReceiptRecordActivity.class);
                            startActivity(intent);
                            finish();
                        }

                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        ToastUtils.showToast(ReceiptActivity.this, "开具发票失败");

                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (resultCode) { //resultCode为回传的标记，我在B中回传的是RESULT_OK
            case RESULT_OK:
                if (null != data) {
                    String amount = data.getStringExtra("amount");
                    amountTv.setText(amount + "\t元");
                    if (chargeTransNos != null) {
                        chargeTransNos.clear();
                    }
                    Map<Integer, ChargeList> chargeLists = (Map<Integer, ChargeList>) data.getSerializableExtra("data");
                    Set<Integer> keys = chargeLists.keySet();
                    //遍历key集合，获取value
                    for (Integer key : keys) {
                        ChargeList chargeList = chargeLists.get(key);
                        if (CommonUtils.hasText(chargeList.getChargeTransNo())) {
                            chargeTransNos.add(chargeList.getChargeTransNo());
                        }

                    }
                    indexs = data.getIntegerArrayListExtra("indexs");
                }
                break;
            default:
                break;
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            finish();
        }
        return true;
    }


    public void getTicktModle() {
        ServiceProvider.receiptService.getTicketTemplate(user.getAccessToken())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<TicketTemplateResponse>() {
            @Override
            public void call(TicketTemplateResponse ticketTemplateResponse) {
                if (ticketTemplateResponse.getStatus() == StatusCode.RESPONSE_OK) {
                    TicketTemplateResponse.Result result = ticketTemplateResponse.getResult();
                    if (CommonUtils.hasText(result.getTitle())) {
                        receiptTitleET.setText(result.getTitle());
                    } else {
                        receiptTitleET.setText(user.getEnterpriseInfo().getEnterpriseName());
                    }
                    if (CommonUtils.hasText(result.getTicketContent())) {
                        serviceTypeTv.setText(result.getTicketContent());
                    } else {
                        serviceTypeTv.setText("服务费");
                    }
                    if (CommonUtils.hasText(result.getContactName())) {
                        nameET.setText(result.getContactName());
                    } else {
                        nameET.setText(user.getLoginName());
                    }
                    if (CommonUtils.hasText(result.getContactCellphone())) {
                        phoneEt.setText(result.getContactCellphone());
                    } else {
                        phoneEt.setText(user.getCellphone());
                    }
                    addressEt.setText(result.getContactAddress());
                    zipCodeEt.setText(result.getContactZip());
                }
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {

            }
        });
    }

    private void getDate() {
        try {
            Map<String, String> options = new HashMap<>();
            options.put("access_token", user.getAccessToken());
            options.put("enterpriseIdx", user.getEnterpriseInfo().getEnterpriseIdx());
            options.put("chargeType", "0");
            options.put("startDate", "");
            options.put("endDate", "");
            options.put("flag", "2");
            ServiceProvider.payService.getchargelist(options).observeOn(AndroidSchedulers.mainThread()).
                    subscribe(new Action1<RechargeInfoResponse>() {
                        @Override
                        public void call(RechargeInfoResponse rechargeInfoResponse) {
                            if (rechargeInfoResponse.getStatus() == StatusCode.RESPONSE_OK) {
                                ChargeInfoResult result = rechargeInfoResponse.getResult();

                                chargeList = result.getChargeList();
                                double allNumber = NumbersUtils.getAllNumber(chargeList);
                                String price = amountTv.getText().toString().trim();
                                if (!CommonUtils.hasText(price)) {
                                    amountTv.setText(allNumber + "\t元");
                                }
                            }
                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {

                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}