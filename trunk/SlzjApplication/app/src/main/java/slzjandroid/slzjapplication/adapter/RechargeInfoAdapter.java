package slzjandroid.slzjapplication.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import slzjandroid.slzjapplication.R;
import slzjandroid.slzjapplication.dto.ChargeList;

/**
 * Created by hdb on 2016/3/24.
 */
public class RechargeInfoAdapter extends BaseAdapter {
    private List<ChargeList> mList;
    private Context mContext;

    public RechargeInfoAdapter(Context context, List<ChargeList> list) {
        this.mList = list;
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LinearLayout.inflate(mContext, R.layout.item_rechargeinfo, null);
            viewHolder.tv_item_serial_number = (TextView) convertView.findViewById(R.id.tv_rechargeinfo_num);
            viewHolder.tv_item_pay_kind = (TextView) convertView.findViewById(R.id.tv_rechargeinfo_kind);
            viewHolder.tv_item_pay_time = (TextView) convertView.findViewById(R.id.tv_rechargeinfo_time);
            viewHolder.tv_item_pay_momey = (TextView) convertView.findViewById(R.id.tv_rechargeinfo_amount);
            viewHolder.tv_item_status = (TextView) convertView.findViewById(R.id.tv_rechargeinfo_status);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tv_item_serial_number.setText(mList.get(position).getChargeTransNo());
        viewHolder.tv_item_pay_kind.setText(mList.get(position).getPaymentChannel());
        viewHolder.tv_item_pay_momey.setText("+" + mList.get(position).getChargeAmount());
        viewHolder.tv_item_pay_time.setText(mList.get(position).getChargeDate());
        if ("2".equals(mList.get(position).getChargeStatus())) {
            viewHolder.tv_item_status.setText("充值成功");
            viewHolder.tv_item_status.setTextColor(mContext.getResources().getColor(R.color.title_bg));
        } else {
            viewHolder.tv_item_status.setText("充值中");
            viewHolder.tv_item_status.setTextColor(mContext.getResources().getColor(R.color.font_color));
        }

        return convertView;
    }

    static class ViewHolder {
        TextView tv_item_serial_number;
        TextView tv_item_pay_kind;
        TextView tv_item_pay_time;
        TextView tv_item_pay_momey;
        TextView tv_item_status;

    }
}
