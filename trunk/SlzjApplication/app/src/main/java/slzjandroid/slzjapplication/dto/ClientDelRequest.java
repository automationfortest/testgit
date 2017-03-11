package slzjandroid.slzjapplication.dto;

import java.io.Serializable;

/**
 * 删除成员请求
 * Created by hdb on 2016/3/14.
 */
public class ClientDelRequest  implements Serializable{
    private String enterpriseIdx;//企业索引
    private String clientUsrNo;//终端用户索引

    public String getEnterpriseIdx() {
        return enterpriseIdx;
    }

    public void setEnterpriseIdx(String enterpriseIdx) {
        this.enterpriseIdx = enterpriseIdx;
    }

    public String getClientUsrNo() {
        return clientUsrNo;
    }

    public void setClientUsrNo(String clientUsrNo) {
        this.clientUsrNo = clientUsrNo;
    }
}
