package slzjandroid.slzjapplication.dto;

import java.io.Serializable;

/**
 * 修改成员请求
 * Created by hdb on 2016/3/14.
 */
public class ClientChangeRequest implements Serializable {
    private String enterpriseIdx;
    private ClientUserInfo clientUserInfo;
    private DeptInfo deptInfo;

    public String getEnterpriseIdx() {
        return enterpriseIdx;
    }

    public void setEnterpriseIdx(String enterpriseIdx) {
        this.enterpriseIdx = enterpriseIdx;
    }

    public ClientUserInfo getClientUserInfo() {
        return clientUserInfo;
    }

    public void setClientUserInfo(ClientUserInfo clientUserInfo) {
        this.clientUserInfo = clientUserInfo;
    }

    public DeptInfo getDeptInfo() {
        return deptInfo;
    }

    public void setDeptInfo(DeptInfo deptInfo) {
        this.deptInfo = deptInfo;
    }
}
