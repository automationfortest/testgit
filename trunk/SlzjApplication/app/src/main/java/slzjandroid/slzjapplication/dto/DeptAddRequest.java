package slzjandroid.slzjapplication.dto;

import java.io.Serializable;

/**
 * 添加部门请求
 * Created by hdb on 2016/3/14.
 */
public  class DeptAddRequest implements Serializable{
    private String enterpriseIdx;
    private String deptName;

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getEnterpriseIdx() {
        return enterpriseIdx;
    }

    public void setEnterpriseIdx(String enterpriseIdx) {
        this.enterpriseIdx = enterpriseIdx;
    }
}
