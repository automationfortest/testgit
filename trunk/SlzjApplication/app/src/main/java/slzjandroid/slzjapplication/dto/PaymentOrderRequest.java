package slzjandroid.slzjapplication.dto;

import java.io.Serializable;

/**
 * 充值订单获取请求
 * Created by hdb on 2016/3/14.
 */
public class PaymentOrderRequest implements Serializable {
    private String enterpriseIdx;//企业索引
    private String paymentMethod;//支付方式，0：微信支付 1：支付宝支付
    private String spbillCreateIp;//终端ip
    private String chargeAmount;//充值金额（元）
    private String submitTime;//提交时间

    public String getEnterpriseIdx() {
        return enterpriseIdx;
    }

    public void setEnterpriseIdx(String enterpriseIdx) {
        this.enterpriseIdx = enterpriseIdx;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getSpbillCreateIp() {
        return spbillCreateIp;
    }

    public void setSpbillCreateIp(String spbillCreateIp) {
        this.spbillCreateIp = spbillCreateIp;
    }

    public String getChargeAmount() {
        return chargeAmount;
    }

    public void setChargeAmount(String chargeAmount) {
        this.chargeAmount = chargeAmount;
    }

    public String getSubmitTime() {
        return submitTime;
    }

    public void setSubmitTime(String submitTime) {
        this.submitTime = submitTime;
    }
}
