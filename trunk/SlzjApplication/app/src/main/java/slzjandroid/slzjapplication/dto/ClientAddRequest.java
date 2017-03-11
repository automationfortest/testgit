package slzjandroid.slzjapplication.dto;

import java.io.Serializable;

/**
 * 增加成员
 * Created by hdb on 2016/3/14.
 */
public class ClientAddRequest implements Serializable {
    private String enterpriseIdx;//企业索引
    private ClientUserInfo clientUserInfo;//用户信息对象
    private DeptInfo deptInfo;//部门信息对象

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
