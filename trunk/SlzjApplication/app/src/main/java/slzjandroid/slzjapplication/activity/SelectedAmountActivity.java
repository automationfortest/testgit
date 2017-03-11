package slzjandroid.slzjapplication.activity;

import android.content.Intent;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;

import slzjandroid.slzjapplication.R;
import slzjandroid.slzjapplication.adapter.SelectedAmountAdapter;
import slzjandroid.slzjapplication.context.AppContext;
import slzjandroid.slzjapplication.customView.NavigationView;
import slzjandroid.slzjapplication.dto.ChargeList;
import slzjandroid.slzjapplication.utils.CommonUtils;
import slzjandroid.slzjapplication.utils.NumbersUtils;
import slzjandroid.slzjapplication.utils.ToastUtils;

/**
 * 选择金额
 * Created by xuyifei on 16/5/9
 */
public class SelectedAmountActivity extends BasicActivity implements NavigationView.ClickCallback, SelectedAmountAdapter.IClickTag {

    private SelectedAmountAdapter selectedAmountAdapter;
    private CheckBox allchoose;
    private TextView allamount;
    private ListView lvContext;
    private ArrayList<Integer> indexs;
    private double pirce;
    private int tag = -1;
    private ArrayList<ChargeList> datas;


    @Override
    protected int getLayoutID() {
        return R.layout.activity_selected_amount;
    }

    @Override
    protected void findViews() {

        allchoose = (CheckBox) findViewById(R.id.ch_all_choose);
        allamount = (TextView) findViewById(R.id.tv_allamount);
        lvContext = (ListView) findViewById(R.id.lv_context);
    }

    @Override
    protected void init() {
        AppContext.getInstance().addActivity(this);
        NavigationView navigationView = (NavigationView) super.findViewById(R.id.nav_main);
        navigationView.setTitle("选择开票金额");
        navigationView.setRightViewIsShow(true);
        navigationView.setRightTile("确定");
        navigationView.setClickCallback(this);

        datas = (ArrayList<ChargeList>) getIntent().getSerializableExtra("datas");
        //总价格
        if (datas != null && datas.size() != 0) {
            pirce = NumbersUtils.getAllNumber(datas);
        } else {
            pirce = 0.0;
        }

        indexs = getIntent().getIntegerArrayListExtra("indexs");
        if (null == indexs) {
            indexs = new ArrayList<>();
        }

        if (null == selectedAmountAdapter) {
            selectedAmountAdapter = new SelectedAmountAdapter(this, datas);
        }

        if (indexs.size() != 0) {
            setCheckboxTag();
        }
        selectedAmountAdapter.setClickTag(this);
        lvContext.setAdapter(selectedAmountAdapter);

        if (indexs.size() == 0) {
            firstShow();
        }


    }

    @Override
    protected void bindViews() {
        allchoose.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (tag == 1) {
                    if (b) {
                        allChooseCheckBox();
                        showPrice(pirce);
                    } else {
                        allClosedCheckBox();
                        showPrice(0.0);
                    }
                } else if (tag == 2) {
                    if (b) {
                        allChooseCheckBox();
                        showPrice(pirce);
                    } else {
                        tag = 1;
                        return;
                    }
                }

            }
        });
    }


    @Override
    public void onBackClick() {
        finish();
    }

    @Override
    public void onRightClick() {
        Map<Integer, ChargeList> chooseDate = getChooseDate();
        if (chooseDate.size() != 0) {
            Intent intent = new Intent();
            String amount = allamount.getText().toString().trim();
            if (CommonUtils.hasText(amount)) {
                String newAmount = amount.substring(4, amount.length() - 1);
                intent.putExtra("amount", newAmount);
            }
            intent.putExtra("data", (Serializable) chooseDate);
            intent.putIntegerArrayListExtra("indexs", indexs);
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    /**
     * 全选
     */
    private void allChooseCheckBox() {
        for (int i = 0; i < selectedAmountAdapter.getCount(); i++) {
            selectedAmountAdapter.mCBFlag.put(i, true);
            selectedAmountAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 取消全选
     */
    private void allClosedCheckBox() {
        for (int i = 0; i < selectedAmountAdapter.getCount(); i++) {
            selectedAmountAdapter.mCBFlag.put(i, false);
            selectedAmountAdapter.notifyDataSetChanged();
        }
    }


    /**
     * 根据选中的值来设置checkbox 状态
     *
     * @return
     */

    private void setCheckboxTag() {
        for (int i = 0; i < indexs.size(); i++) {
            selectedAmountAdapter.mCBFlag.put(indexs.get(i), true);
            selectedAmountAdapter.notifyDataSetChanged();

        }

    }

    /**
     * 第一次显示
     */
    private void firstShow() {
        allchoose.setChecked(true);
        allChooseCheckBox();
        allamount.setText(pirce + "\t元");
    }

    /**
     * 获取选中的开票金额
     *
     * @return
     */
    public Map<Integer, ChargeList> getChooseDate() {
        Map<Integer, ChargeList> chargeLists = new Hashtable<>();
        Map<Integer, Boolean> mCBFlag = selectedAmountAdapter.mCBFlag;
        Set<Integer> integers = mCBFlag.keySet();
        if (indexs.size() != 0) {
            indexs.clear();
        }
        //遍历key集合，获取value
        for (Integer key : integers) {
            Boolean aBoolean = mCBFlag.get(key);
            if (aBoolean) {
                indexs.add(key);
            }
        }
        if (indexs.size() == 0) {
            ToastUtils.showToast(SelectedAmountActivity.this, "您还没有选择要开具项目！");
            return chargeLists;
        }
        ArrayList<ChargeList> datas = selectedAmountAdapter.getDatas();

        for (int i = 0; i < indexs.size(); i++) {
            chargeLists.put(i, datas.get(indexs.get(i)));
        }
        return chargeLists;
    }

    @Override
    public void onClickTag(double num, int postion) {
        if (num == pirce) {
            tag = 1;
            allchoose.setChecked(true);
        } else if (num != 0.0 && num < pirce) {
            tag = 2;
            allchoose.setChecked(false);
        } else {
            tag = 1;
            allchoose.setChecked(false);
        }
        showPrice(num);
    }

    /**
     * 在标题上显示价格
     *
     * @param price
     */
    private void showPrice(double price) {
        allamount.setText("总金额:" + price + "\t元");
        int end = allamount.length();
        SpannableStringBuilder style = new SpannableStringBuilder(allamount.getText().toString());
        style.setSpan(new ForegroundColorSpan(Color.parseColor("#ffb93212")), 4, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        allamount.setText(style);
    }
}
