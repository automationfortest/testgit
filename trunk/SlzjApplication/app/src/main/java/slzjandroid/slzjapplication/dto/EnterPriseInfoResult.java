package slzjandroid.slzjapplication.dto;

/**
 * Created by ASUS on 2016/4/21.
 */
public class EnterPriseInfoResult {
    private String enterpriseName;
    private String enterpriseEmail;
    private String enterpriseAuthFlag;
    private String deptCnt;
    private String clientCnt;
    private String balance;
    private String frozenMoney;
    private String accountBalance;
    private String creditBalance;
    private String bonusPoint;
    private String couponCnt;
    private String couponAmount;


    public String getFrozenMoney() {
        return frozenMoney;
    }

    public void setFrozenMoney(String frozenMoney) {
        this.frozenMoney = frozenMoney;
    }

    public String getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(String accountBalance) {
        this.accountBalance = accountBalance;
    }

    public String getEnterpriseName() {
        return enterpriseName;
    }

    public void setEnterpriseName(String enterpriseName) {
        this.enterpriseName = enterpriseName;
    }

    public String getEnterpriseEmail() {
        return enterpriseEmail;
    }

    public void setEnterpriseEmail(String enterpriseEmail) {
        this.enterpriseEmail = enterpriseEmail;
    }

    public String getEnterpriseAuthFlag() {
        return enterpriseAuthFlag;
    }

    public void setEnterpriseAuthFlag(String enterpriseAuthFlag) {
        this.enterpriseAuthFlag = enterpriseAuthFlag;
    }

    public String getDeptCnt() {
        return deptCnt;
    }

    public void setDeptCnt(String deptCnt) {
        this.deptCnt = deptCnt;
    }

    public String getClientCnt() {
        return clientCnt;
    }

    public void setClientCnt(String clientCnt) {
        this.clientCnt = clientCnt;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getCreditBalance() {
        return creditBalance;
    }

    public void setCreditBalance(String creditBalance) {
        this.creditBalance = creditBalance;
    }

    public String getBonusPoint() {
        return bonusPoint;
    }

    public void setBonusPoint(String bonusPoint) {
        this.bonusPoint = bonusPoint;
    }

    public String getCouponCnt() {
        return couponCnt;
    }

    public void setCouponCnt(String couponCnt) {
        this.couponCnt = couponCnt;
    }

    public String getCouponAmount() {
        return couponAmount;
    }

    public void setCouponAmount(String couponAmount) {
        this.couponAmount = couponAmount;
    }
}
