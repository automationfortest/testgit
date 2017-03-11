package slzjandroid.slzjapplication.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import slzjandroid.slzjapplication.R;
import slzjandroid.slzjapplication.activity.OrderDetailsActivity;
import slzjandroid.slzjapplication.activity.OrderListActivity;
import slzjandroid.slzjapplication.dto.GeneralOrderInfo;
import slzjandroid.slzjapplication.helper.SPLoginUser;
import slzjandroid.slzjapplication.utils.CommonUtils;

/**
 * Created by hdb on 2016/4/6.
 */
public class OrderAllListAdapter extends BaseAdapter {
    private Context mContext;
    private List<GeneralOrderInfo> mList = new ArrayList<>();
    private LayoutInflater inflater;

    public OrderAllListAdapter(Context context) {
        this.mContext = context;
        inflater = LayoutInflater.from(mContext);
    }

    public void refresh(List<GeneralOrderInfo> list) {
        if (list == null) return;
        mList = list;
        notifyDataSetChanged();
    }


    public void append(List<GeneralOrderInfo> list) {
        if (list == null) return;
        if (mList == null) {
            mList = new ArrayList<>();
        }
        mList.addAll(list);
        notifyDataSetChanged();
    }


    public void clear(List<GeneralOrderInfo> list) {
        if (list == null) return;
        if (mList == null) {
            mList = new ArrayList<>();
        }
        mList.addAll(list);
        notifyDataSetChanged();
    }

    public void clear() {
        mList.clear();
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
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_orderlist, null);
            viewHolder.tv_time = (TextView) convertView.findViewById(R.id.tv_orderlist_time);
            viewHolder.tv_num = (TextView) convertView.findViewById(R.id.tv_orderlist_num);
            viewHolder.tv_from = (TextView) convertView.findViewById(R.id.tv_orderlist_from);
            viewHolder.tv_to = (TextView) convertView.findViewById(R.id.tv_orderlist_to);
            viewHolder.tv_status = (TextView) convertView.findViewById(R.id.tv_orderlist_status);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final GeneralOrderInfo generalOrderInfo = mList.get(position);
        viewHolder.tv_time.setText(generalOrderInfo.getOrderTime());
        viewHolder.tv_num.setText(generalOrderInfo.getOrderID());
        viewHolder.tv_from.setText(generalOrderInfo.getDepartureName());
        viewHolder.tv_to.setText(generalOrderInfo.getDestinationName());
        final String orderStatus = generalOrderInfo.getOrderStatus();
        if (orderStatus.equals("300")) {
            viewHolder.tv_status.setText("正在派单");
        } else if (orderStatus.equals("400")) {
            viewHolder.tv_status.setText("待服务");
        } else if (orderStatus.equals("410")) {
            viewHolder.tv_status.setText("待服务");
        } else if (orderStatus.equals("500")) {
            viewHolder.tv_status.setText("行程中");
        } else if (orderStatus.equals("600")) {
            viewHolder.tv_status.setText("已完成");
        } else if (orderStatus.equals("610")) {
            viewHolder.tv_status.setText("已取消");
        } else if (orderStatus.equals("700")) {
            viewHolder.tv_status.setText("已完成");
        }
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, OrderDetailsActivity.class);
                intent.putExtra("orderStatus", orderStatus);
                if (CommonUtils.hasText(generalOrderInfo.getOrderID())) {
                    String orderId = SPLoginUser.getOrderId(mContext);
                    if (CommonUtils.hasText(orderId)) {
                        SPLoginUser.clearOrderId(mContext);
                    }
                    SPLoginUser.setOrderId(mContext, generalOrderInfo.getOrderID());
                }
                if (orderStatus.equals("300") || orderStatus.equals("400") || orderStatus.equals("410")) {
                    ((OrderListActivity) mContext).startActivityForResult(intent, 1);
                } else {
                    ((OrderListActivity) mContext).startActivityForResult(intent, 0);
                }
            }
        });
        return convertView;
    }

    static class ViewHolder {
        TextView tv_time, tv_num, tv_from, tv_to, tv_status;
    }
}
