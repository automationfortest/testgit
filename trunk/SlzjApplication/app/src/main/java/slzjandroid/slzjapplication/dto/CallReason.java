package slzjandroid.slzjapplication.dto;

import java.io.Serializable;

/**
 * 叫车原因LIST
 * Created by hdb on 2016/3/14.
 */
public
class CallReason implements Serializable {
    private String reasonIndex;//叫车原因索引
    private String reasonName;//叫车原因名称
    private String forOtherFlag;//乘车人标志

    public String getReasonIndex() {
        return reasonIndex;
    }

    public void setReasonIndex(String reasonIndex) {
        this.reasonIndex = reasonIndex;
    }

    public String getReasonName() {
        return reasonName;
    }

    public void setReasonName(String reasonName) {
        this.reasonName = reasonName;
    }

    public String getForOtherFlag() {
        return forOtherFlag;
    }

    public void setForOtherFlag(String forOtherFlag) {
        this.forOtherFlag = forOtherFlag;
    }
}
