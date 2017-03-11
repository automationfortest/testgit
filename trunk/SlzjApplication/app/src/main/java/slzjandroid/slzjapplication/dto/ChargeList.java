package slzjandroid.slzjapplication.dto;

import java.io.Serializable;

/**
 * Created by ASUS on 2016/4/28.
 */
public class ChargeList implements Serializable {
    private String chargeTransNo;
    private String chargeDate;
    private String paymentChannel;
    private String chargeAmount;
    private String chargeStatus;
    private boolean isCliced;

    public boolean isCliced() {
        return isCliced;
    }

    public void setCliced(boolean cliced) {
        isCliced = cliced;
    }


    public String getChargeTransNo() {
        return chargeTransNo;
    }

    public void setChargeTransNo(String chargeTransNo) {
        this.chargeTransNo = chargeTransNo;
    }

    public String getChargeDate() {
        return chargeDate;
    }

    public void setChargeDate(String chargeDate) {
        this.chargeDate = chargeDate;
    }

    public String getPaymentChannel() {
        return paymentChannel;
    }

    public void setPaymentChannel(String paymentChannel) {
        this.paymentChannel = paymentChannel;
    }

    public String getChargeAmount() {
        return chargeAmount;
    }

    public void setChargeAmount(String chargeAmount) {
        this.chargeAmount = chargeAmount;
    }

    public String getChargeStatus() {
        return chargeStatus;
    }

    public void setChargeStatus(String chargeStatus) {
        this.chargeStatus = chargeStatus;
    }
}
