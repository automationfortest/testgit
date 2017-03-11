package slzjandroid.slzjapplication.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import slzjandroid.slzjapplication.R;
import slzjandroid.slzjapplication.dto.DepartmentInfo;

/**
 * Created by ASUS on 2016/4/17.
 */
public class ProjectChooseAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<DepartmentInfo> mlist;
    public Boolean edit_flag = false;//是否开启编辑

    public ProjectChooseAdapter(Context con, ArrayList<DepartmentInfo> list) {
        this.mContext = con;
        this.mlist = list;
    }

    @Override
    public int getCount() {
        return mlist.size();
    }

    @Override
    public Object getItem(int position) {
        return mlist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = LinearLayout.inflate(mContext, R.layout.item_choose_dept, null);
            viewHolder = new ViewHolder();
            viewHolder.edt_dept = (EditText) convertView.findViewById(R.id.edt_item_member_add_dept);
            viewHolder.tv_dept = (TextView) convertView.findViewById(R.id.tv_item_member_add_dept);
            viewHolder.imv_choose = (ImageView) convertView.findViewById(R.id.imv_dept_choosed);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (mlist.get(position).isChoosed()) {
            viewHolder.imv_choose.setVisibility(View.VISIBLE);
        } else {
            viewHolder.imv_choose.setVisibility(View.INVISIBLE);
        }
        if (edit_flag) {
            viewHolder.edt_dept.setVisibility(View.VISIBLE);
            viewHolder.tv_dept.setVisibility(View.GONE);
        } else {
            viewHolder.edt_dept.setVisibility(View.GONE);
            viewHolder.tv_dept.setVisibility(View.VISIBLE);
        }
        viewHolder.edt_dept.setText(mlist.get(position).getDepartmentName());
        viewHolder.tv_dept.setText(mlist.get(position).getDepartmentName());
        viewHolder.edt_dept.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mlist.get(position).setDepartmentName(s.toString());
            }
        });
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edit_flag) return;//
                if (mlist.get(position).isChoosed()) {
                    mlist.get(position).setChoosed(false);
                } else {
                    mlist.get(position).setChoosed(true);


                }
                notifyDataSetChanged();

            }
        });
        return convertView;
    }

    private static class ViewHolder {
        TextView tv_dept;
        EditText edt_dept;
        ImageView imv_choose;
    }

    /**
     * 编辑切换
     */
    public void changeEdit() {
        if (edit_flag) {
            edit_flag = false;
        } else {
            edit_flag = true;
        }
        notifyDataSetChanged();
    }

    public ArrayList<DepartmentInfo> getAllData() {
        return mlist;
    }

    public ArrayList<DepartmentInfo> getData() {
        ArrayList<DepartmentInfo> list = new ArrayList<>();
        int size = mlist.size();
        for (int i = 0; i < size; i++) {
            if (mlist.get(i).isChoosed()) {
                list.add(mlist.get(i));
            }
        }
        return list;
    }
}