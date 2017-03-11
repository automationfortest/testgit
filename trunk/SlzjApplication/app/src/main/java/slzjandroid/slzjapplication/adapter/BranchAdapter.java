package slzjandroid.slzjapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import slzjandroid.slzjapplication.R;
import slzjandroid.slzjapplication.dto.DeptInfo;

/**
 * Created by xuyifei on 16/5/14.
 */
public class BranchAdapter extends BaseAdapter {
    ArrayList<DeptInfo> deptInfos;
    private LayoutInflater inflater;

    public BranchAdapter(ArrayList<DeptInfo> deptInfos, Context mContext) {
        this.deptInfos = deptInfos;
        inflater = LayoutInflater.from(mContext);

    }

    @Override
    public int getCount() {
        return deptInfos.size();
    }

    @Override
    public Object getItem(int i) {
        return deptInfos.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.item_select_name, null);
        TextView tv = (TextView) view.findViewById(R.id.tv_uploadphoto_select_name_item);
        tv.setText(deptInfos.get(i).getDepartmentName());
        return view;
    }
}
