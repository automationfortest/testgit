package slzjandroid.slzjapplication.dto;

import java.io.Serializable;

/**
 * 企业信息对象
 * Created by hdb on 2016/3/14.
 */
public class EnterpriseInfo implements Serializable {
    private String creditBalance;//信用用户余额
    private String debitBalance;//储值账户余额
    private String email;
    private String enterpriseAuthFlag;//企业实名认证标识
    private String enterpriseIdx;//企业索引
    private String enterpriseName;//企业名称
    private String authDate;
    private String resaon;

    public String getAuthDate() {
        return authDate;
    }

    public void setAuthDate(String authDate) {
        this.authDate = authDate;
    }

    public String getResaon() {
        return resaon;
    }

    public void setResaon(String resaon) {
        this.resaon = resaon;
    }


    public String getEnterpriseAuthFlag() {
        return enterpriseAuthFlag;
    }

    public void setEnterpriseAuthFlag(String enterpriseAuthFlag) {
        this.enterpriseAuthFlag = enterpriseAuthFlag;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public String getEnterpriseIdx() {
        return enterpriseIdx;
    }

    public void setEnterpriseIdx(String enterpriseIdx) {
        this.enterpriseIdx = enterpriseIdx;
    }

    public String getEnterpriseName() {
        return enterpriseName;
    }

    public void setEnterpriseName(String enterpriseName) {
        this.enterpriseName = enterpriseName;
    }


    public String getDebitBalance() {
        return debitBalance;
    }

    public void setDebitBalance(String debitBalance) {
        this.debitBalance = debitBalance;
    }

    public String getCreditBalance() {
        return creditBalance;
    }

    public void setCreditBalance(String creditBalance) {
        this.creditBalance = creditBalance;
    }
}
