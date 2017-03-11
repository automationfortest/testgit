package slzjandroid.slzjapplication.dto;

import java.io.Serializable;

/**
 * Created by hdb on 2016/3/31.
 */
public class PriceDetails implements Serializable{
    private String feeName;//价格明细名称
    private String feeType;//价格明细类型
    private String feeAmount;//价格明细金额

    public String getFeeName() {
        return feeName;
    }

    public void setFeeName(String feeName) {
        this.feeName = feeName;
    }

    public String getFeeType() {
        return feeType;
    }

    public void setFeeType(String feeType) {
        this.feeType = feeType;
    }

    public String getFeeAmount() {
        return feeAmount;
    }

    public void setFeeAmount(String feeAmount) {
        this.feeAmount = feeAmount;
    }
}
