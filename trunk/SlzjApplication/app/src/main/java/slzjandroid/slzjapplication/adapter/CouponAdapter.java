package slzjandroid.slzjapplication.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import slzjandroid.slzjapplication.R;
import slzjandroid.slzjapplication.dto.CouponRequest;
import slzjandroid.slzjapplication.utils.CommonUtils;
import slzjandroid.slzjapplication.utils.DateUtil;

/**
 * Created by xuyifei on 16/4/20.
 */
public class CouponAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private String tag;

    public void setTag(String tag) {
        this.tag = tag;
    }

    private List<CouponRequest.CouponInfo> mdata = new ArrayList<>();

    public CouponAdapter(Context context) {
        inflater = LayoutInflater.from(context);
    }

    public void setData(List<CouponRequest.CouponInfo> datas) {
        if (datas == null) return;
        if (!mdata.isEmpty()) {
            mdata.clear();
        }
        this.mdata = datas;
        notifyDataSetChanged();
    }

    public void clearData() {
        if (!mdata.isEmpty()) {
            mdata.clear();
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mdata != null ? mdata.size() : 0;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHodler hodler;
        if (view == null) {
            hodler = new ViewHodler();
            view = inflater.inflate(R.layout.item_compon_layout, null);
            hodler.layout = (LinearLayout) view.findViewById(R.id.layout);
            hodler.tv_compon_name = (TextView) view.findViewById(R.id.tv_compon_name);

            hodler.tv_compon_num = (TextView) view.findViewById(R.id.tv_compon_num);
            hodler.tv_commpon_explain = (TextView) view.findViewById(R.id.tv_commpon_explain);
            hodler.tv_date = (TextView) view.findViewById(R.id.tv_date);
            hodler.tv_remarks = (TextView) view.findViewById(R.id.tv_remarks);
            hodler.tv_amount = (TextView) view.findViewById(R.id.tv_amount);
            view.setTag(hodler);
        } else {
            hodler = (ViewHodler) view.getTag();
        }
        if (tag.equals("0")) {
            hodler.layout.setBackgroundResource(R.mipmap.not_overdue_bg);
        } else {
            hodler.layout.setBackgroundResource(R.mipmap.overdue_bg);
        }
        CouponRequest.CouponInfo couponInfo = mdata.get(i);
        hodler.tv_compon_name.setText(couponInfo.getCouponName());
        if (!TextUtils.isEmpty(couponInfo.getCouponCnt())) {
            hodler.tv_compon_num.setText(couponInfo.getCouponCnt() + "张");
        } else {
            hodler.tv_compon_num.setText(0 + "张");
        }

        if (!TextUtils.isEmpty(couponInfo.getCouponResource())) {
            hodler.tv_commpon_explain.setText(couponInfo.getCouponResource());
        } else {
            hodler.tv_commpon_explain.setVisibility(View.INVISIBLE);
        }
        String yearMothesDay = DateUtil.getYearMothesDay(couponInfo.getValidate());
        if (CommonUtils.hasText(yearMothesDay)) {
            hodler.tv_date.setText("有效期至：" + "\t" + yearMothesDay);
        } else {
            hodler.tv_date.setText("有效期至：" + "\t暂无");
        }
        // hodler.tv_remarks.setText("备注：" + couponInfo.getExtInfo());
        hodler.tv_remarks.setText("消费满：" + couponInfo.getCouponDiscountLimit() + "元可用，限企业用车服务使用");
        String discountAmount = couponInfo.getDiscountAmount();
        double aDouble = Double.parseDouble(discountAmount);

        hodler.tv_amount.setText((int) aDouble + "");
        return view;
    }

    class ViewHodler {
        LinearLayout layout;
        TextView tv_compon_name;
        TextView tv_compon_num;
        TextView tv_commpon_explain;
        TextView tv_date;
        TextView tv_remarks;
        TextView tv_amount;

    }
}
