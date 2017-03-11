package slzjandroid.slzjapplication.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.net.ConnectException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit.RetrofitError;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import slzjandroid.slzjapplication.R;
import slzjandroid.slzjapplication.activity.CallCarActivity;
import slzjandroid.slzjapplication.activity.LoginActivity;
import slzjandroid.slzjapplication.activity.MainActivity;
import slzjandroid.slzjapplication.activity.OrderListActivity;
import slzjandroid.slzjapplication.context.AppContext;
import slzjandroid.slzjapplication.customView.LoadingDialog;
import slzjandroid.slzjapplication.dto.LoginUser;
import slzjandroid.slzjapplication.dto.TokenRenewalResponse;
import slzjandroid.slzjapplication.helper.SPLoginUser;
import slzjandroid.slzjapplication.lang.StatusCode;
import slzjandroid.slzjapplication.service.ServiceProvider;
import slzjandroid.slzjapplication.utils.CommonUtils;
import slzjandroid.slzjapplication.utils.DateUtil;
import slzjandroid.slzjapplication.utils.DialogUtil;
import slzjandroid.slzjapplication.utils.ToastUtils;
import slzjandroid.slzjapplication.weights.ViewPagerHelper;

public class ProductFragment extends BaseFragment implements View.OnClickListener, AdapterView.OnItemClickListener {

    public ViewPager viewPager;
    public LinearLayout viewPoints;
    private RelativeLayout relativeLayout;
    private ImageView imv_menu_icon, imv_menu_alarm;
    private List<View> views = null;
    static MainActivity mainActivity = null;
    int[] imgs = new int[]{R.drawable.selector_main_menu_bg, R.mipmap.img_hotel_icon, R.mipmap.img_air_icon, R.mipmap.img_train_icon};
    private MyAdapter ad;
    private GridView gv;

    private boolean tag = true;

    private LoginUser loginUser;

    public static ProductFragment newInstance() {
        ProductFragment fragment = new ProductFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_product;
    }

    @Override
    protected void findViews() {
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPoints = (LinearLayout) findViewById(R.id.dots_parent);
        relativeLayout = (RelativeLayout) findViewById(R.id.title);
        imv_menu_alarm = (ImageView) relativeLayout.findViewById(R.id.iv_right_bg);
        imv_menu_icon = (ImageView) findViewById(R.id.imv_menu_icon);
    }

    @Override
    protected void bindViews() {

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        imv_menu_icon.setOnClickListener(this);
        imv_menu_alarm.setOnClickListener(this);
    }

    @Override
    protected void init() {
        mainActivity = (MainActivity) getActivity();
        initData();
        gv = (GridView) findViewById(R.id.gv_icon);
        ad = new MyAdapter(mContext);
        gv.setAdapter(ad);
        gv.setOnItemClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imv_menu_icon:
                mainActivity.toggleMenu();
                break;
            case R.id.imv_menu_alarm:
                ToastUtils.showToast(mContext, "暂无消息");
                break;
            case R.id.iv_right_bg:
                ToastUtils.showToast(mContext, "暂无消息");
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        switch (position) {
            case 0:
                if (tag) {
                    getID();
                    tag = false;
                    SPLoginUser.saveLable(getActivity(), "3");
                }
                break;
            default:
                break;
        }
    }

    private class MyAdapter extends BaseAdapter {
        LayoutInflater mInflater;

        public MyAdapter(Context context) {
            this.mInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return imgs.length;
        }

        @Override
        public Object getItem(int position) {
            return imgs[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder v;
            if (convertView == null) {
                v = new ViewHolder();
                convertView = mInflater.inflate(R.layout.item_main_icon_layout, null);
                v.imageView = (ImageView) convertView.findViewById(R.id.imv_main_icon);

                v.imageView.setImageResource(imgs[position]);
                convertView.setTag(v);
            } else {
                v = (ViewHolder) convertView.getTag();

            }
            return convertView;
        }

        class ViewHolder {
            ImageView imageView;
        }
    }

    private void initData() {
        views = new ArrayList<>();
        View view1 = LayoutInflater.from(getActivity()).inflate(R.layout.layout_banner_01, null);
        View view2 = LayoutInflater.from(getActivity()).inflate(R.layout.layout_banner_02, null);
        View view3 = LayoutInflater.from(getActivity()).inflate(R.layout.layout_banner_03, null);
        views.add(view1);
        views.add(view2);
        views.add(view3);
        new ViewPagerHelper(true, viewPager, views, viewPoints, R.mipmap.page_indicator_focused, R.mipmap.page_indicator_unfocused);
    }


    private void getID() {
        final LoadingDialog dialog = DialogUtil.getLoadingDialog(mContext, "正在进行...");
        try {
            loginUser = LoginUser.getUser();
            if (loginUser == null) {
                startActivity(new Intent(getActivity(), LoginActivity.class));
                getActivity().finish();
            } else {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("cellphone", loginUser.getCellphone());
                map.put("timestamp", DateUtil.getCurrentTime(DateUtil.YMDHMS_STYLE_ONE));
                ServiceProvider.tokenService.tokenRenewal(loginUser.getAccessToken(), map).subscribeOn(Schedulers.io())
                        .observeOn(rx.android.schedulers.AndroidSchedulers.mainThread()) // 指定 Subscriber 的回调发生在主线程
                        .subscribe(
                                new Action1<TokenRenewalResponse>() {
                                    @Override
                                    public void call(TokenRenewalResponse tokenResponse) {

                                        try {
                                            if (tokenResponse.getStatus() == StatusCode.RESPONSE_OK) {
                                                ad.notifyDataSetChanged();
                                                dialog.dismiss();
                                                //存在在途订单，显示详情页
                                                String accessToken = tokenResponse.getResult().getUsrToken().getAccessToken();
                                                SPLoginUser.saveTokenTo(getActivity(), accessToken);
                                                loginUser.setAccessToken(accessToken);
                                                LoginUser.saveUserToDB(loginUser, getActivity());

                                                String onTravelFlag = tokenResponse.getResult().getOnTravelFlag();
                                                if ("1".equals(onTravelFlag)) {
                                                    DialogUtil.getInstance().popTravelingDialog(mainActivity, new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            DialogUtil.getInstance().clearDialogs();
                                                            Intent intent = new Intent(mainActivity, OrderListActivity.class);
                                                            startActivity(intent);
                                                        }
                                                    }, new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            DialogUtil.getInstance().clearDialogs();
                                                            Intent intent = new Intent(mainActivity, CallCarActivity.class);
                                                            SPLoginUser.setReasonName(getActivity(), "代人叫车");
                                                            startActivity(intent);
                                                        }
                                                    });

                                                } else {
                                                    DialogUtil.getInstance().clearDialogs();
                                                    Intent intent = new Intent(getActivity(), CallCarActivity.class);
                                                    startActivity(intent);
                                                }
                                            } else if (tokenResponse.getStatus() == StatusCode.RESPONSE_ERR) {
                                                DialogUtil.getInstance().clearDialogs();
                                                ToastUtils.showToast(mainActivity, "您离开时间太长，需要重新登陆");
                                                AppContext.getInstance().finishAllActivity();
                                                startActivity(new Intent(mainActivity, LoginActivity.class));
                                                getActivity().finish();
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            DialogUtil.getInstance().clearDialogs();
                                            AppContext.getInstance().finishAllActivity();
                                            startActivity(new Intent(mainActivity, LoginActivity.class));
                                            getActivity().finish();
                                        }
                                    }
                                },
                                new Action1<Throwable>() {
                                    @Override
                                    public void call(Throwable e) {
                                        dialog.dismiss();
                                        e.printStackTrace();

                                        Log.e("验证token异常", e.getMessage());
                                        DialogUtil.getInstance().clearDialogs();
                                        AppContext.getInstance().finishAllActivity();
                                        startActivity(new Intent(mainActivity, LoginActivity.class));
                                        getActivity().finish();
                                        if (e instanceof ConnectException || e instanceof RetrofitError) {
                                            ToastUtils.showToast(mainActivity, "网络异常，无法连接服务器");
                                            return;
                                        }
                                    }
                                }
                        );
                return;
            }
        } catch (Exception e) {
            dialog.dismiss();
            e.printStackTrace();
            Log.e("验证token异常", e.getMessage());
            AppContext.getInstance().finishAllActivity();
            startActivity(new Intent(mainActivity, LoginActivity.class));
            getActivity().finish();
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        String lable = SPLoginUser.getLable(getActivity());
        if (CommonUtils.hasText(lable) && lable.equals("3")) {
            tag = true;
        }
    }
}