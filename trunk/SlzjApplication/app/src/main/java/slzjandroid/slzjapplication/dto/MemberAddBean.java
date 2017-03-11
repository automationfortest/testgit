package slzjandroid.slzjapplication.dto;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by ASUS on 2016/4/22.
 */
public class MemberAddBean implements Serializable{
    private String clientUsrName;
    private String clientUsrCellphone;
    private String usrType="1";
    private ArrayList<DeptInfo> deptInfo;

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

    public ArrayList<DeptInfo> getDeptInfo() {
        return deptInfo;
    }

    public void setDeptInfo(ArrayList<DeptInfo> deptInfo) {
        this.deptInfo = deptInfo;
    }
}
