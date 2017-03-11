package slzjandroid.slzjapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import slzjandroid.slzjapplication.R;
import slzjandroid.slzjapplication.dto.TicketResponse;

/**
 * 发票记录列表
 * Created by xuyifei on 16/5/9.
 */
public class ReceiptRecordAdapter extends BaseAdapter {


    private LayoutInflater inflater;
    private List<TicketResponse.TicketList> datas;

    public ReceiptRecordAdapter(Context context) {
        inflater = LayoutInflater.from(context);

    }

    public void setData(List<TicketResponse.TicketList> datas) {
        if (null == datas) {
            datas = new ArrayList<>();
        }
        this.datas = datas;
        notifyDataSetChanged();

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
    public View getView(int i, View view, ViewGroup viewGroup) {

        ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.item_receipt_record, null);
            holder.mechanismTv = (TextView) view.findViewById(R.id.tv_mechanism);
            holder.namePhoneTv = (TextView) view.findViewById(R.id.tv_name_phone);
            holder.addressTv = (TextView) view.findViewById(R.id.tv_address);
            holder.dateTv = (TextView) view.findViewById(R.id.tv_date);
            holder.amountTv = (TextView) view.findViewById(R.id.tv_amount);
            holder.flagTv = (TextView) view.findViewById(R.id.tv_flag);
            holder.serialNumberTv = (TextView) view.findViewById(R.id.tv_serial_number);
            holder.expressNumberTv = (TextView) view.findViewById(R.id.tv_express_number);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        TicketResponse.TicketList ticketList = datas.get(i);

        holder.mechanismTv.setText(ticketList.getTicketTitle());
        holder.namePhoneTv.setText(ticketList.getContactName() + "\t" + ticketList.getContactCellphone());
        holder.addressTv.setText(ticketList.getContactAddress());

        holder.amountTv.setText(ticketList.getTicketAmount() + "元");

        holder.serialNumberTv.setText("开票流水单号：" + ticketList.getTicketTransNo());
        holder.expressNumberTv.setText(ticketList.getLogisticsName() + "\t" + ticketList.getLogisticsNo());

        String ticketStatus = ticketList.getTicketStatus();

        if (ticketStatus.equals("0")) {
            holder.flagTv.setText("已申请");
        }
        if (ticketStatus.equals("1")) {
            holder.flagTv.setText("已开具");
        }
        if (ticketStatus.equals("2")) {
            holder.flagTv.setText("已邮寄");
        }
        if (ticketStatus.equals("3")) {
            holder.flagTv.setText("已废除");
        }
        return view;
    }


    class ViewHolder {
        TextView mechanismTv;
        TextView namePhoneTv;
        TextView addressTv;
        TextView dateTv;
        TextView amountTv;
        TextView flagTv;
        TextView serialNumberTv;
        TextView expressNumberTv;
    }
}
