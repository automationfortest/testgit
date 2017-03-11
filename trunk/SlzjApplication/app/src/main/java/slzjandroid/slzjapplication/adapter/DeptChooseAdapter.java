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
import slzjandroid.slzjapplication.dto.DeptInfo;

/**
 * Created by ASUS on 2016/4/17.
 */
public class DeptChooseAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<DeptInfo> mlist;
    public Boolean edit_flag=false;//是否开启编辑
    private int lastposition=-1;//记录最新的list记录
    public DeptChooseAdapter(Context con,ArrayList<DeptInfo> list){
        this.mContext =con;
        this.mlist =list;
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
    public View getView(final int position,  View convertView, ViewGroup parent) {
          ViewHolder viewHolder=null;
        if(convertView==null){
            convertView = LinearLayout.inflate(mContext, R.layout.item_choose_dept,null);
            viewHolder = new ViewHolder();
            viewHolder.edt_dept = (EditText) convertView.findViewById(R.id.edt_item_member_add_dept);
            viewHolder.tv_dept = (TextView) convertView.findViewById(R.id.tv_item_member_add_dept);
            viewHolder.imv_choose= (ImageView) convertView.findViewById(R.id.imv_dept_choosed);
  //          viewHolder.edt_dept.setText(mlist.get(position).getDeptName());

            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if(mlist.get(position).getIsChoosed()){
            viewHolder.imv_choose.setVisibility(View.VISIBLE);
        }else{
            viewHolder.imv_choose.setVisibility(View.INVISIBLE);
        }
        if(edit_flag){
            viewHolder.edt_dept.setVisibility(View.VISIBLE);
            viewHolder.tv_dept.setVisibility(View.GONE);
        }else {
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
                //  viewHolder.imv_choose.setVisibility(View.GONE)；
                if(edit_flag)return;//
                if (mlist.get(position).getIsChoosed()) {
                    mlist.get(position).setIsChoosed(false);
                } else {
                    mlist.get(position).setIsChoosed(true);
                    if(lastposition!=-1&&lastposition!=position){
                        mlist.get(lastposition).setIsChoosed(false);

                    }

                }

                lastposition = position;
                notifyDataSetChanged();

            }
        });
        return convertView;
    }
    private static class ViewHolder{
        TextView tv_dept;
        EditText edt_dept;
        ImageView imv_choose;
    }

    /**
     * 编辑切换
     */
    public void changeEdit(){
            if (edit_flag){
                edit_flag=false;
            }else {
                edit_flag=true;
                if(lastposition!=-1){
                    mlist.get(lastposition).setIsChoosed(false);

                }
            }
        notifyDataSetChanged();
    }
    public ArrayList<DeptInfo> getAllData(){
        return mlist;
    }
    public DeptInfo getData(){
        return mlist.get(lastposition);
    }
}