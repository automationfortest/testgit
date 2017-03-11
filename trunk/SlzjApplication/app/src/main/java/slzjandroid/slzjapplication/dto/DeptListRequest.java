package slzjandroid.slzjapplication.dto;

import java.io.Serializable;

/**
 * 获取部门列表请求
 * Created by hdb on 2016/3/13.
 */
public class DeptListRequest implements Serializable {
    private String enterpriseIdx;//企业索引

    public String getEnterpriseIdx() {
        return enterpriseIdx;
    }

    public void setEnterpriseIdx(String enterpriseIdx) {
        this.enterpriseIdx = enterpriseIdx;
    }
}
