package slzjandroid.slzjapplication.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import slzjandroid.slzjapplication.R;
import slzjandroid.slzjapplication.dto.Value;

/**
 * Created by hdb on 2016/4/9.
 */
public class CityAdapter extends BaseAdapter {
    private ArrayList<Value> values;
    private Context mContext;
    public CityAdapter(Context context,ArrayList<Value> results){
        this.mContext =context;
        this.values = results;


    }
    @Override
    public int getCount() {
        return values.size();
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
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(mContext, R.layout.item_city_chose, null);
            holder.tv_py = (TextView) convertView.findViewById(R.id.tv_py);
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        String string = null;

        if (position == 0) {
            string = values.get(position).getPinyin();
        } else {
            String py = values.get(position).getPinyin();
            String spy = values.get(position - 1).getPinyin();
            if (!py.equals(spy)) {
                string = values.get(position).getPinyin();
            }
        }
        if (string == null) {
            holder.tv_py.setVisibility(View.GONE);
        } else {
            holder.tv_py.setVisibility(View.VISIBLE);
            holder.tv_py.setText(string);
        }
        holder.tv_name.setText(values.get(position).getName());
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =  new Intent();
                Bundle bundle = new Bundle();
                bundle.putSerializable("city_info",values.get(position));
                intent.putExtras(bundle);
                ((Activity) mContext).setResult(Activity.RESULT_OK, intent);
                ((Activity) mContext).finish();
            }
        });
        return convertView;
    }

    static class ViewHolder {
        TextView tv_py, tv_name;
    }
}
