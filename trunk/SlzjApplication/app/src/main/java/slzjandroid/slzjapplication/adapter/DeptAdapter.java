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
import slzjandroid.slzjapplication.dto.DepartmentInfo;

/**
 * Created by hdb on 2016/4/3.
 */
public class DeptAdapter extends BaseAdapter {
    private List<DepartmentInfo> data = new ArrayList<DepartmentInfo>();
    private Context mContext;
    public static int mSelectIndex = -1;
    public DeptAdapter(Context context, List<DepartmentInfo> data) {
        this.mContext = context;
        this.data = data;
    }

    static class ViewHolder {
        TextView mPriceTv;
        TextView mPriceSelectImg;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater
                    .inflate(R.layout.item_popupwindow_kind, null);

            ViewHolder holder = new ViewHolder();
            holder.mPriceTv = (TextView) convertView
                    .findViewById(R.id.item_kind_tv);
            holder.mPriceSelectImg = (TextView) convertView
                    .findViewById(R.id.kind_select_img);
            convertView.setTag(holder);
        }
        final ViewHolder holder = (ViewHolder) convertView.getTag();

        if (mSelectIndex == position) {
            holder.mPriceSelectImg.setVisibility(View.VISIBLE);
        } else {
            holder.mPriceSelectImg.setVisibility(View.GONE);
        }
        holder.mPriceTv.setText(data.get(position).getDepartmentName());
        return convertView;
    }
}
