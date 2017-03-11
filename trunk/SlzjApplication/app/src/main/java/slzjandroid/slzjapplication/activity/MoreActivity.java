package slzjandroid.slzjapplication.activity;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import slzjandroid.slzjapplication.R;
import slzjandroid.slzjapplication.context.AppContext;
import slzjandroid.slzjapplication.customView.NavigationView;
import slzjandroid.slzjapplication.dto.LoginUser;
import slzjandroid.slzjapplication.helper.SPLoginUser;

/**
 * Created by hdb on 2016/4/11.
 */
public class MoreActivity extends BasicActivity implements View.OnClickListener, NavigationView.ClickCallback {
    private Button btn_exit;
    private LinearLayout lly_more_about_us, lly_menu_help, lly_menu_service_agreement;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_menu_more;
    }

    @Override
    protected void findViews() {
        lly_more_about_us = (LinearLayout) findViewById(R.id.lly_more_about_us);
        lly_menu_help = (LinearLayout) findViewById(R.id.lly_menu_help);
        lly_menu_service_agreement = (LinearLayout) findViewById(R.id.lly_menu_service_agreement);
        btn_exit = (Button) findViewById(R.id.btn_exit);

    }

    @Override
    protected void init() {
        AppContext.getInstance().addActivity(this);
        NavigationView navigationView = (NavigationView) super.findViewById(R.id.nav_main);
        navigationView.setTitle("更多");
        navigationView.setRightViewIsShow(false);
        navigationView.setClickCallback(this);


    }

    @Override
    protected void bindViews() {
        btn_exit.setOnClickListener(this);
        lly_menu_help.setOnClickListener(this);
        lly_more_about_us.setOnClickListener(this);
        lly_menu_service_agreement.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_exit:
                SPLoginUser.clearToken(MoreActivity.this);
                LoginUser.clearDB(MoreActivity.this);
                SPLoginUser.clearLable(MoreActivity.this);
                SPLoginUser.clearfinshTag(MoreActivity.this);
                AppContext.getInstance().finishAllActivity();
                Intent intent = new Intent(MoreActivity.this, LoginActivity.class);
                startActivity(intent);
                break;
            case R.id.lly_more_about_us:
                intentPage(2);
                break;
            case R.id.lly_menu_help:
                intentPage(5);
                break;
            case R.id.lly_menu_service_agreement:
                intentPage(4);
                break;
            default:
                break;
        }
    }

    private void intentPage(int tag) {
        Intent intent = new Intent(MoreActivity.this, DocumentAtivity.class);
        intent.putExtra("pageNum", tag);
        startActivity(intent);
    }


    @Override
    public void onBackClick() {
        finish();
    }

    @Override
    public void onRightClick() {

    }
}
