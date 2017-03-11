package slzjandroid.slzjapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import slzjandroid.slzjapplication.R;
import slzjandroid.slzjapplication.dto.ChargeList;

/**
 * Created by xuyifei on 16/5/10.
 */

public class SelectedAmountAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private IClickTag iClickTag;

    public void setClickTag(IClickTag iClickTag) {
        this.iClickTag = iClickTag;
    }


    public ArrayList<ChargeList> datas;
    public Map<Integer, Boolean> mCBFlag;


    public SelectedAmountAdapter(Context context, ArrayList<ChargeList> datas) {
        inflater = LayoutInflater.from(context);
        mCBFlag = new HashMap<>();
        this.datas = datas;
        initCheckFlag();
    }


    public ArrayList<ChargeList> getDatas() {
        return datas;
    }

    private void initCheckFlag() {

        if (null != datas) {
            for (int i = 0; i < datas.size(); i++) {
                mCBFlag.put(i, false);
            }
        }
    }


    @Override
    public int getCount() {
        return null != datas ? datas.size() : 0;
    }

    @Override
    public Object getItem(int i) {
        return datas.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {

        ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.item_selected_amount, null);
            holder.chooseCb = (CheckBox) view.findViewById(R.id.ch_choose);
            holder.dateTv = (TextView) view.findViewById(R.id.tv_date);
            holder.orderNumTv = (TextView) view.findViewById(R.id.tv_order_num);
            holder.payWayTv = (TextView) view.findViewById(R.id.tv_pay_way);
            holder.amountTv = (TextView) view.findViewById(R.id.tv_amount);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        ChargeList chargeList = datas.get(i);
        holder.dateTv.setText(chargeList.getChargeDate());
        holder.orderNumTv.setText("订单号\t" + chargeList.getChargeTransNo());
        holder.payWayTv.setText(chargeList.getPaymentChannel());
        holder.amountTv.setText("+\t" + chargeList.getChargeAmount() + "元");

        holder.chooseCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    mCBFlag.put(i, true);
                } else {
                    mCBFlag.put(i, false);
                }
                iClickTag.onClickTag(getChooseDate(), i);
            }
        });
        holder.chooseCb.setChecked(mCBFlag.get(i));

        return view;
    }

    class ViewHolder {
        CheckBox chooseCb;
        TextView dateTv;
        TextView orderNumTv;
        TextView payWayTv;
        TextView amountTv;
    }


    /**
     * 获取选中的开票金额
     *
     * @return
     */
    public double getChooseDate() {
        List<Integer> indexs = new ArrayList<>();
        Set<Integer> integers = mCBFlag.keySet();
        //遍历key集合，获取value
        for (Integer key : integers) {
            Boolean aBoolean = mCBFlag.get(key);
            if (aBoolean) {
                indexs.add(key);
            }
        }
        double sum = 0;
        for (Integer i : indexs) {
            String chargeAmount = this.datas.get(i).getChargeAmount();
            sum = sum + Double.parseDouble(chargeAmount);
        }

        java.text.DecimalFormat df = new java.text.DecimalFormat("#.00");
        return Double.parseDouble(df.format(sum));
    }

    public interface IClickTag {
        void onClickTag(double num, int postion);
    }
}
