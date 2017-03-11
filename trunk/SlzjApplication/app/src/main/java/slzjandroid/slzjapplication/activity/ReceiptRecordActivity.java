package slzjandroid.slzjapplication.activity;

import android.view.View;
import android.widget.ListView;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import slzjandroid.slzjapplication.R;
import slzjandroid.slzjapplication.adapter.ReceiptRecordAdapter;
import slzjandroid.slzjapplication.context.AppContext;
import slzjandroid.slzjapplication.customView.LoadingDialog;
import slzjandroid.slzjapplication.customView.NavigationView;
import slzjandroid.slzjapplication.dto.LoginUser;
import slzjandroid.slzjapplication.dto.TicketResponse;
import slzjandroid.slzjapplication.lang.StatusCode;
import slzjandroid.slzjapplication.service.ServiceProvider;
import slzjandroid.slzjapplication.utils.DialogUtil;


/**
 * 发票记录
 * Created by xuyifei on 16/5/9
 */
public class ReceiptRecordActivity extends BasicActivity implements View.OnClickListener, NavigationView.ClickCallback {


    private ListView contextLv;
    private ReceiptRecordAdapter receiptRecordAdapter;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_receipt_record;
    }


    @Override
    protected void init() {
        AppContext.getInstance().addActivity(this);
        NavigationView navigationView = (NavigationView) super.findViewById(R.id.nav_main);
        navigationView.setTitle("发票记录");
        navigationView.setRightViewIsShow(false);
        navigationView.setClickCallback(this);
        receiptRecordAdapter = new ReceiptRecordAdapter(this);


    }

    @Override
    protected void findViews() {
        contextLv = (ListView) findViewById(R.id.lv_context);
    }


    @Override
    protected void bindViews() {
        contextLv.setAdapter(receiptRecordAdapter);

    }

    @Override
    public void onBackClick() {
        finish();
    }

    @Override
    public void onRightClick() {

    }

    @Override
    public void onClick(View view) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        getReceiptRecordList();
    }


    /**
     * 获取发票记录列表
     */
    private void getReceiptRecordList() {
        final LoadingDialog loadingDialog = DialogUtil.getLoadingDialog(this, "获取发票记录...");
        LoginUser user = LoginUser.getUser();
        ServiceProvider.receiptService.getReceiptTicket(user.getAccessToken())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<TicketResponse>() {
                    @Override
                    public void call(TicketResponse ticketResponse) {
                        loadingDialog.dismiss();
                        if (ticketResponse.getStatus() == StatusCode.RESPONSE_OK) {
                            receiptRecordAdapter.setData(ticketResponse.getResult().getTicketList());
                        }

                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        loadingDialog.dismiss();
                    }
                });

    }
}
