package slzjandroid.slzjapplication.dto;

import java.io.Serializable;

/**
 * 删除部门请求
 * Created by hdb on 2016/3/14.
 */
public class DeptDelRequest implements Serializable {
    private String enterpriseIdx;//企业索引
    private String deptNo;//部门索引

    public String getEnterpriseIdx() {
        return enterpriseIdx;
    }

    public void setEnterpriseIdx(String enterpriseIdx) {
        this.enterpriseIdx = enterpriseIdx;
    }

    public String getDeptNo() {
        return deptNo;
    }

    public void setDeptNo(String deptNo) {
        this.deptNo = deptNo;
    }
}
