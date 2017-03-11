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
import slzjandroid.slzjapplication.dto.Hotvalue;

/**
 * Created by ASUS on 2016/4/12.
 */
public class HotValueAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<Hotvalue> mList;
    public HotValueAdapter (Context con,ArrayList<Hotvalue> list){
        this.mContext = con;
        this.mList =list;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(mContext, R.layout.item_hotcity_chose, null);
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_hotcity_name);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv_name.setText(mList.get(position).getName());
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =  new Intent();
                Bundle bundle = new Bundle();
                bundle.putSerializable("city_info", mList.get(position));
                intent.putExtras(bundle);
                ((Activity) mContext).setResult(Activity.RESULT_OK, intent);
                ((Activity) mContext).finish();
            }
        });
        return convertView;
    }
    static class ViewHolder {
        TextView tv_name;
    }
}
