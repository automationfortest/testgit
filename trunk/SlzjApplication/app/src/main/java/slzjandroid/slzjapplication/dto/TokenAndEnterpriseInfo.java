package slzjandroid.slzjapplication.dto;

import java.io.Serializable;

/**
 * Created by hdb on 2016/3/20.
 */
public class TokenAndEnterpriseInfo implements Serializable {

    private UsrToken usrToken;//Token对象
    private EnterpriseInfo enterpriseInfo;//企业信息对象
    private String newUsrFlag;//新用户标识
    private String usrType;//用户标志
    private String usrName;//用户姓名
    private String unreadMsg;//未读信息条数
    private String onTravelFlag;//在途标识
    private String onTravelOrderId;//在途订单ID
    private String balance;
    private String creditBalance;

    public UsrToken getUsrToken() {
        return usrToken;
    }

    public void setUsrToken(UsrToken usrToken) {
        this.usrToken = usrToken;
    }

    public EnterpriseInfo getEnterpriseInfo() {
        return enterpriseInfo;
    }

    public void setEnterpriseInfo(EnterpriseInfo enterpriseInfo) {
        this.enterpriseInfo = enterpriseInfo;
    }

    public String getNewUsrFlag() {
        return newUsrFlag;
    }

    public void setNewUsrFlag(String newUsrFlag) {
        this.newUsrFlag = newUsrFlag;
    }

    public String getUsrType() {
        return usrType;
    }

    public void setUsrType(String usrType) {
        this.usrType = usrType;
    }

    public String getUsrName() {
        return usrName;
    }

    public void setUsrName(String usrName) {
        this.usrName = usrName;
    }

    public String getUnreadMsg() {
        return unreadMsg;
    }

    public void setUnreadMsg(String unreadMsg) {
        this.unreadMsg = unreadMsg;
    }

    public String getOnTravelFlag() {
        return onTravelFlag;
    }

    public void setOnTravelFlag(String onTravelFlag) {
        this.onTravelFlag = onTravelFlag;
    }

    public String getOnTravelOrderId() {
        return onTravelOrderId;
    }

    public void setOnTravelOrderId(String onTravelOrderId) {
        this.onTravelOrderId = onTravelOrderId;
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
}
