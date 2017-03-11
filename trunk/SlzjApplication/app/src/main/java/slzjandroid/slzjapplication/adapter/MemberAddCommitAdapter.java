package slzjandroid.slzjapplication.adapter;

import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import slzjandroid.slzjapplication.R;
import slzjandroid.slzjapplication.activity.MenberAddCommitActivity;
import slzjandroid.slzjapplication.dto.DeptInfo;
import slzjandroid.slzjapplication.dto.MemberAddBean;

/**
 * Created by hdb on 2016/4/9.
 */
public class MemberAddCommitAdapter extends BaseAdapter {
    private ArrayList<MemberAddBean> values;
    private MenberAddCommitActivity mContext;
    private ArrayList<DeptInfo> deptInfos;
    private int lastposition = -1;

    public MemberAddCommitAdapter(MenberAddCommitActivity context, ArrayList<MemberAddBean> results, ArrayList<DeptInfo> deptlist) {
        this.mContext = context;
        this.values = results;
        this.deptInfos = deptlist;

    }

    @Override
    public int getCount() {
        return values.size();
    }

    @Override
    public Object getItem(int position) {
        return values.get(position);
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
            convertView = View.inflate(mContext, R.layout.item_memberadd_commit, null);
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_item_member_connect_name);
            holder.tv_cell = (TextView) convertView.findViewById(R.id.tv_item_member_add_commit_cell);
            holder.tv_dept = (TextView) convertView.findViewById(R.id.tv_memberadd_commit_dept);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv_name.setText(values.get(position).getClientUsrName());
        holder.tv_cell.setText(values.get(position).getClientUsrCellphone());
        if (values.get(position).getDeptInfo() != null) {
            holder.tv_dept.setText(values.get(position).getDeptInfo().get(0).getDepartmentName());
        }
        holder.tv_dept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lastposition = position;
                showDialog();
            }
        });
        return convertView;
    }

    static class ViewHolder {
        TextView tv_name, tv_cell, tv_dept;
    }


    public void showDialog() {
        final Dialog builder = new Dialog(mContext, R.style.Dialog);
        LayoutInflater inflater = LayoutInflater.from(mContext);// 渲染器
        View v = inflater.inflate(R.layout.branch, null);

        ListView listView = (ListView) v.findViewById(R.id.contact_list);

        builder.setContentView(v);
        listView.setAdapter(new BranchAdapter(deptInfos, mContext));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ArrayList<DeptInfo> dept = new ArrayList<>();
                dept.add(deptInfos.get(position));
                values.get(lastposition).setDeptInfo(dept);
                notifyDataSetChanged();
                builder.dismiss();
            }
        });

        builder.show();
    }

}
