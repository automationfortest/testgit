package slzjandroid.slzjapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import slzjandroid.slzjapplication.R;
import slzjandroid.slzjapplication.customView.QuickAlphabeticBar;
import slzjandroid.slzjapplication.dto.ContactBean;
import slzjandroid.slzjapplication.utils.CommonUtils;


public class ContactListAdapter extends BaseAdapter {
    private LayoutInflater inflater;

    public List<ContactBean> getList() {
        return list;
    }

    public void setList(List<ContactBean> list) {
        this.list = list;
        this.notifyDataSetChanged();
    }

    private List<ContactBean> list;
    private HashMap<String, Integer> alphaIndexer; // 字母索引
    private String[] sections; // 存储每个章节
    private Context ctx; // 上下文
    public Map<Integer, Boolean> mCBFlag = new HashMap<>();

    public ContactListAdapter(Context context, List<ContactBean> list, QuickAlphabeticBar alpha) {
        this.ctx = context;
        this.inflater = LayoutInflater.from(context);
        this.list = list;
        this.alphaIndexer = new HashMap<>();
        this.sections = new String[list.size()];

        for (int i = 0; i < list.size(); i++) {
            // 得到字母
            String name = CommonUtils.getAlpha(list.get(i).getSortKey(), false);
            if (!alphaIndexer.containsKey(name)) {
                alphaIndexer.put(name, i);
            }
        }

        Set<String> sectionLetters = alphaIndexer.keySet();
        ArrayList<String> sectionList = new ArrayList<String>(sectionLetters);
        Collections.sort(sectionList); // 根据首字母进行排序
        sections = new String[sectionList.size()];
        sectionList.toArray(sections);
        alpha.setAlphaIndexer(alphaIndexer);
        initCheckFlag();

    }


    private void initCheckFlag() {
        for (int i = 0; i < list.size(); i++) {
            mCBFlag.put(i, false);
        }
    }


    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
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
            convertView = inflater.inflate(R.layout.item_memberadd_connects, null);
            holder.tv_py = (TextView) convertView.findViewById(R.id.tv_memberadd_py);
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_memberadd_connects_name);
            holder.cb_choose = (CheckBox) convertView.findViewById(R.id.cb_memberadd_connects);
            holder.tv_cell = (TextView) convertView.findViewById(R.id.cb_memberadd_connects_cellphone);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        ContactBean contact = list.get(position);
        String name = contact.getDesplayName();
        String number = contact.getPhoneNum();
        holder.tv_name.setText(name);
        holder.tv_cell.setText(number);

        // 当前字母
        String currentStr = CommonUtils.getAlpha(contact.getSortKey(), false);
        // 前面的字母
        String previewStr = (position - 1) >= 0 ? CommonUtils.getAlpha(list.get(
                position - 1).getSortKey(), false) : " ";

        if (!previewStr.equals(currentStr)) {
            holder.tv_py.setVisibility(View.VISIBLE);
            holder.tv_py.setText(currentStr);
        } else {
            holder.tv_py.setVisibility(View.GONE);
        }
        holder.cb_choose.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    mCBFlag.put(position, true);
                } else {
                    mCBFlag.put(position, false);
                }
            }
        });
        holder.cb_choose.setChecked(mCBFlag.get(position));
        return convertView;
    }

    static class ViewHolder {
        TextView tv_py, tv_name, tv_cell;
        CheckBox cb_choose;
    }


}
