package slzjandroid.slzjapplication.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import slzjandroid.slzjapplication.R;
import slzjandroid.slzjapplication.dto.PlaceData;
import slzjandroid.slzjapplication.utils.CommonUtils;

/**
 * Created by hdb on 2016/3/30.
 */
public class AdressAdapter extends BaseAdapter {
    private List<PlaceData> mlist = null;
    private LayoutInflater mInflater = null;
    private Context mContext;

    public AdressAdapter(Context context, List<PlaceData> adressList) {
        mContext = context;
        mlist = adressList;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return mlist.size();
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.listview_address, null);
            viewHolder.tv_adress_name = (TextView) convertView.findViewById(R.id.addr_list_name_tv);
            viewHolder.tv_adress = (TextView) convertView.findViewById(R.id.addr_list_address_tv);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tv_adress_name.setText(mlist.get(position).getDisplayName());
        viewHolder.tv_adress.setText(mlist.get(position).getAddress());
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("adress", mlist.get(position));
                ((Activity) mContext).setResult(Activity.RESULT_OK, intent);
                CommonUtils.disInputMethod(mContext);
                ((Activity) mContext).finish();
            }
        });
        return convertView;
    }

    static class ViewHolder {
        TextView tv_adress_name;
        TextView tv_adress;
    }
}
