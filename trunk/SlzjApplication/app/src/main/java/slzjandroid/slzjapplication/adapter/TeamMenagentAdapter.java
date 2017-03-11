package slzjandroid.slzjapplication.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import slzjandroid.slzjapplication.R;
import slzjandroid.slzjapplication.activity.LoginActivity;
import slzjandroid.slzjapplication.activity.MemberEditActivity;
import slzjandroid.slzjapplication.activity.TeamManagentActivity;
import slzjandroid.slzjapplication.context.AppContext;
import slzjandroid.slzjapplication.customView.DragView;
import slzjandroid.slzjapplication.dto.ClientUserInfo;
import slzjandroid.slzjapplication.dto.DepartmentInfo;
import slzjandroid.slzjapplication.dto.LoginUser;
import slzjandroid.slzjapplication.dto.MemberDelBean;
import slzjandroid.slzjapplication.dto.MemberDelGson;
import slzjandroid.slzjapplication.dto.MemberDelInfo;
import slzjandroid.slzjapplication.dto.OnlyResponse;
import slzjandroid.slzjapplication.lang.StatusCode;
import slzjandroid.slzjapplication.service.ServiceProvider;
import slzjandroid.slzjapplication.utils.Base64;
import slzjandroid.slzjapplication.utils.ToastUtils;

/**
 * Created by hdb on 2016/4/11.
 */
public class TeamMenagentAdapter extends BaseAdapter {
    private TeamManagentActivity mContext;
    private ArrayList<ClientUserInfo> mList = new ArrayList<>();
    private LayoutInflater mInflater;

    public void setiRefreh(IRefreh iRefreh) {
        this.iRefreh = iRefreh;
    }

    private IRefreh iRefreh;

    List<DragView> views = new ArrayList<>();

    public TeamMenagentAdapter(TeamManagentActivity context) {
        this.mContext = context;
        mInflater = LayoutInflater.from(mContext);

    }

    public void append(List<ClientUserInfo> list) {
        notifyDataSetChanged();
        if (list == null) return;
        if (mList == null) {
            mList = new ArrayList<>();
        }
        mList.addAll(list);
        notifyDataSetChanged();

    }


    public void refrehData(List<ClientUserInfo> list) {
        notifyDataSetChanged();
        if (list == null) return;
        if (mList == null) {
            mList = new ArrayList<>();
        }
        mList.clear();
        mList.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_listview_delete, null);

            holder.dv = (DragView) convertView.findViewById(R.id.drag_view);
            views.add(holder.dv);

            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_team_menagent_p_name);
            holder.tv_cellphone = (TextView) convertView.findViewById(R.id.tv_team_menagent_p_cellphone);
            holder.tv_dept1 = (TextView) convertView.findViewById(R.id.tv_team_dept_1);

            holder.dv = (DragView) convertView.findViewById(R.id.drag_view);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final ClientUserInfo clientUserInfo = mList.get(position);
        holder.dv.setOnDragStateListener(new DragView.DragStateListener() {
            @Override
            public void onOpened(DragView dragView) {
                close(position);

            }

            @Override
            public void onClosed(DragView dragView) {

            }

            @Override
            public void onForegroundViewClick(DragView dragView, View v) {
                Intent intent = new Intent(mContext, MemberEditActivity.class);
                intent.putExtra("clientUserInfo", clientUserInfo);
                mContext.startActivityForResult(intent, 1);
            }

            @Override
            public void onBackgroundViewClick(DragView dragView, View v) {

                switch (v.getId()) {
                    case R.id.delete:
                        delData(position);
                        break;
                }


            }
        });
        holder.dv.setTag(position);
        holder.dv.close();

        holder.tv_name.setText(clientUserInfo.getClientUsrName());
        holder.tv_cellphone.setText(clientUserInfo.getClientUsrCellphone());

        //判断部门列表
        ArrayList<DepartmentInfo> deptInfo = clientUserInfo.getDeptInfo();

        StringBuilder builder = new StringBuilder();
        if (null != deptInfo) {
            for (int i = 0; i < deptInfo.size(); i++) {
                DepartmentInfo departmentInfo = deptInfo.get(i);
                if (departmentInfo.getDeptType().equals("0")) {
                    builder.append(departmentInfo.getDepartmentName());
                    builder.append("\t");
                    if (builder.toString().length() <= 18) {
                        holder.tv_dept1.setText(builder.toString());
                    } else {
                        builder.append("...");
                        holder.tv_dept1.setText(builder.toString());
                        break;
                    }
                }
            }
        }
        return convertView;
    }

    class ViewHolder {
        private DragView dv;
        TextView tv_name, tv_cellphone, tv_dept1;
    }

    public void close(int postion) {
        for (int i = 0; i < views.size(); i++) {
            if (i != postion && views.get(i).isOpen())
                views.get(i).closeAnim();
        }
    }


    /**
     * 删除指定成员
     *
     * @param position
     */
    private void delData(final int position) {
        Map<String, String> prames = new HashMap<>();
        LoginUser loginUser = LoginUser.getUser();
        prames.put("enterpriseIdx", loginUser.getEnterpriseInfo().getEnterpriseIdx());

        MemberDelInfo info = new MemberDelInfo();
        info.setClientUsrIdx(mList.get(position).getClientUsrIdx());
        info.setClientUsrCellphone(mList.get(position).getClientUsrCellphone());
        ArrayList<MemberDelInfo> clientUserInfos = new ArrayList<>();
        clientUserInfos.add(info);
        MemberDelBean bean = new MemberDelBean();
        bean.setClientUserInfoList(clientUserInfos);


        try {
            prames.put("clientUserInfoList", Base64.encode(MemberDelGson.toRequst(bean).getBytes("utf-8")));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        ServiceProvider.teamMenagentService.getDeptDelResult(loginUser.getAccessToken(), prames).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<OnlyResponse>() {
                    @Override
                    public void call(OnlyResponse onlyResponse) {
                        if (onlyResponse.getStatus() == StatusCode.RESPONSE_OK) {
                            mList.remove(position);
                            iRefreh.onRefreh();
                            notifyDataSetChanged();
                            ToastUtils.showToast(mContext, "删除成功");
                        } else if (onlyResponse.getStatus() == StatusCode.RESPONSE_ERR) {
                            ToastUtils.showToast(mContext, "您离开时间太长，需要重新登陆");
                            AppContext.getInstance().finishAllActivity();
                            mContext.startActivity(new Intent(mContext, LoginActivity.class));
                        } else {
                            ToastUtils.showToast(mContext, onlyResponse.getMessage());
                        }

                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                        ToastUtils.showToast(mContext, "删除失败");
                    }
                });
    }

    public interface IRefreh {
        void onRefreh();
    }

}
