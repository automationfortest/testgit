package slzjandroid.slzjapplication.dto;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 用户信息对象
 * Created by hdb on 2016/3/14.
 */
public class ClientUserInfo implements Serializable {
    private String clientUsrIdx;//终端用户索引
    private String clientUsrName;//终端用户姓名
    private String clientUsrCellphone;//终端用户手机号
    private String usrType="1";//用户标志
    private ArrayList<DepartmentInfo> deptInfo;//部门信息对象

    public String getClientUsrIdx() {
        return clientUsrIdx;
    }

    public void setClientUsrIdx(String clientUsrIdx) {
        this.clientUsrIdx = clientUsrIdx;
    }

    public String getClientUsrName() {
        return clientUsrName;
    }

    public void setClientUsrName(String clientUsrName) {
        this.clientUsrName = clientUsrName;
    }

    public String getClientUsrCellphone() {
        return clientUsrCellphone;
    }

    public void setClientUsrCellphone(String clientUsrCellphone) {
        this.clientUsrCellphone = clientUsrCellphone;
    }

    public String getUsrType() {
        return usrType;
    }

    public void setUsrType(String usrType) {
        this.usrType = usrType;
    }

    public ArrayList<DepartmentInfo> getDeptInfo() {
        return deptInfo;
    }

    public void setDeptInfo(ArrayList<DepartmentInfo> deptInfo) {
        this.deptInfo = deptInfo;
    }
}
