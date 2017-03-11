package slzjandroid.slzjapplication.activity;

import android.Manifest;
import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ListView;

import net.sourceforge.pinyin4j.PinyinHelper;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import slzjandroid.slzjapplication.R;
import slzjandroid.slzjapplication.adapter.ContactListAdapter;
import slzjandroid.slzjapplication.context.AppContext;
import slzjandroid.slzjapplication.customView.NavigationView;
import slzjandroid.slzjapplication.customView.QuickAlphabeticBar;
import slzjandroid.slzjapplication.dto.ContactBean;
import slzjandroid.slzjapplication.dto.MemberAddBean;
import slzjandroid.slzjapplication.utils.CommonUtils;
import slzjandroid.slzjapplication.utils.NumbersUtils;
import slzjandroid.slzjapplication.utils.ToastUtils;
import slzjandroid.slzjapplication.weights.DeletableEditText;

/**
 * Created by ASUS on 2016/4/22.
 */
public class MemberAddConnectsActivity extends BasicActivity implements NavigationView.ClickCallback {

    private DeletableEditText edt_team_menagent_search;
    private ListView contactList;
    //转换为拼音的List
    private QuickAlphabeticBar alphabeticBar;
    private AsyncQueryHandler asyncQueryHandler; // 异步查询数据库类对象

    private Map<Integer, ContactBean> contactIdMap = null;
    private List<ContactBean> list;
    private ContactListAdapter adapter;
    private ArrayList<Integer> indexs;
    private List<ContactBean> datas;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_memberadd_connects;
    }

    @Override
    protected void findViews() {
        edt_team_menagent_search = (DeletableEditText) findViewById(R.id.edt_team_menagent_search);
        contactList = (ListView) findViewById(R.id.contact_list);
        alphabeticBar = (QuickAlphabeticBar) findViewById(R.id.fast_scroller);
    }

    @Override
    protected void init() {
        AppContext.getInstance().addActivity(this);
        NavigationView navigationView = (NavigationView) super.findViewById(R.id.nav_main);
        navigationView.setTitle("通讯录");
        navigationView.setRightViewIsShow(true);
        navigationView.setRightTile("确定");
        navigationView.setClickCallback(this);

        if (null == indexs) {
            indexs = new ArrayList<>();
        }

        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI; // 联系人Uri；
        // 查询的字段
        String[] projection = {ContactsContract.CommonDataKinds.Phone._ID,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.DATA1, "sort_key",
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
                ContactsContract.CommonDataKinds.Phone.PHOTO_ID,
                ContactsContract.CommonDataKinds.Phone.LOOKUP_KEY};
        // 实例化
        asyncQueryHandler = new MyAsyncQueryHandler(getContentResolver());
        // 按照sort_key升序查詢
        asyncQueryHandler.startQuery(0, null, uri, projection, null, null,
                "sort_key COLLATE LOCALIZED asc");
    }

    @Override
    protected void bindViews() {
        edt_team_menagent_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (null != adapter) {
                    if (s.length() != 0) {
                        adapter.setList(changeContactsList(s.toString().trim().toUpperCase()));
                    } else {
                        adapter.setList(datas);
                    }
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            ToastUtils.showToast(MemberAddConnectsActivity.this, "请您打开钱包行云的通讯录权限来添加团队成员信息");
            return;
        }
    }

    @Override
    protected void onStart() {

        super.onStart();
    }


    private synchronized List<ContactBean> changeContactsList(String str) {

        List<ContactBean> mdatas = new ArrayList<>();
        for (int i = 0; i < datas.size(); i++) {
            ContactBean contactBean = datas.get(i);
            //判断输入的第一个字符是否是汉字
            String[] chars = PinyinHelper.toHanyuPinyinStringArray(str.charAt(0));
            if (chars != null) {
                //如果是汉字，则直接匹配字符串
                if (contactBean.getDesplayName().contains(str)) {
                    mdatas.add(contactBean);
                }
            } else {

                //判断输入的字符串的长度,如果只是输入了一个字符
                if (str.length() == 1) {
                    //把输入的内容转换成拼音并获取收个字母
                    if (str.equalsIgnoreCase("#")) {
                        mdatas.add(contactBean);
                    } else {
                        String alpha = CommonUtils.getAlpha(str, true);
                        String s = CommonUtils.getPingYin(contactBean.getDesplayName()).toUpperCase();
                        if (s.contains(alpha)) {
                            mdatas.add(contactBean);
                        }
                    }
                } else {
                    String s = CommonUtils.getPingYin(contactBean.getDesplayName()).toUpperCase();
                    if (s.contains(str)) {
                        mdatas.add(contactBean);
                    }
                }

            }

        }
        return mdatas;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == RESULT_OK) {
            setResult(RESULT_OK);
            finish();
        }
        edt_team_menagent_search.setText("");
    }

    @Override
    public void onBackClick() {
        finish();
    }

    @Override
    public void onRightClick() {
        List<MemberAddBean> chooseDate = getChooseDate();
        if (chooseDate.size() != 0) {
            Intent intent = new Intent(MemberAddConnectsActivity.this, MenberAddCommitActivity.class);
            intent.putExtra("memberchosed", (Serializable) chooseDate);
            startActivityForResult(intent, 0);
        }
    }

    /**
     * @author Administrator
     */
    private class MyAsyncQueryHandler extends AsyncQueryHandler {

        public MyAsyncQueryHandler(ContentResolver cr) {
            super(cr);
        }

        @Override
        protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
            if (cursor != null && cursor.getCount() > 0) {
                contactIdMap = new HashMap<>();
                list = new ArrayList<>();
                cursor.moveToFirst(); // 游标移动到第一项
                for (int i = 0; i < cursor.getCount(); i++) {
                    cursor.moveToPosition(i);
                    String name = cursor.getString(1);
                    String number = cursor.getString(2);
                    String sortKey = cursor.getString(3);
                    int contactId = cursor.getInt(4);
                    Long photoId = cursor.getLong(5);
                    String lookUpKey = cursor.getString(6);

                    if (contactIdMap.containsKey(contactId)) {
                        // 无操作
                    } else {
                        // 创建联系人对象
                        ContactBean contact = new ContactBean();
                        contact.setDesplayName(name);
                        contact.setPhoneNum(number);
                        contact.setSortKey(sortKey);
                        contact.setPhotoId(photoId);
                        contact.setLookUpKey(lookUpKey);
                        list.add(contact);

                        contactIdMap.put(contactId, contact);
                    }
                }
                if (list.size() > 0) {
                    setAdapter(list);
                }
            }

            super.onQueryComplete(token, cookie, cursor);
        }

    }

    private void setAdapter(List<ContactBean> list) {

        datas = new ArrayList<>();
        if (null != list && list.size() != 0) {
            for (ContactBean m : list) {
                String phoneNumber = m.getPhoneNum();

                ContactBean contactBean = new ContactBean();
                contactBean.setSortKey(m.getSortKey());
                contactBean.setLookUpKey(m.getLookUpKey());
                contactBean.setDesplayName(m.getDesplayName());

                if (phoneNumber.contains(" ")) {
                    String p = phoneNumber.replace(" ", "");
                    contactBean.setPhoneNum(p);
                } else if (phoneNumber.contains("-")) {
                    String p = phoneNumber.replace("-", "");
                    contactBean.setPhoneNum(p);
                } else if (phoneNumber.contains("+86")) {
                    String p = phoneNumber.replace("+86", "");
                    contactBean.setPhoneNum(p);
                } else {
                    contactBean.setPhoneNum(phoneNumber);
                }
                if (NumbersUtils.isCellphoneNo(contactBean.getPhoneNum())) {
                    datas.add(contactBean);
                }
            }
            if (adapter == null) {
                adapter = new ContactListAdapter(this, datas, alphabeticBar);
            }
            contactList.setAdapter(adapter);
            alphabeticBar.init(MemberAddConnectsActivity.this);
            alphabeticBar.setListView(contactList);
            alphabeticBar.setHight(alphabeticBar.getHeight());
            alphabeticBar.setVisibility(View.VISIBLE);
        }
    }


    /**
     * 获取选中的联系人
     *
     * @return
     */
    public List<MemberAddBean> getChooseDate() {
        List<MemberAddBean> contactBeans = new ArrayList<>();
        if (null != adapter) {
            Map<Integer, Boolean> mCBFlag = adapter.mCBFlag;
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
                ToastUtils.showToast(MemberAddConnectsActivity.this, "您还没有选择联系人！");
                return contactBeans;
            }
            List<ContactBean> list = adapter.getList();
            if (null != list && list.size() != 0) {
                for (int i = 0; i < indexs.size(); i++) {
                    ContactBean contactBean = list.get(indexs.get(i));
                    MemberAddBean m = new MemberAddBean();
                    m.setClientUsrName(contactBean.getDesplayName());
                    m.setClientUsrCellphone(contactBean.getPhoneNum());
                    contactBeans.add(i, m);
                }
            }
        }
        return contactBeans;
    }


}
