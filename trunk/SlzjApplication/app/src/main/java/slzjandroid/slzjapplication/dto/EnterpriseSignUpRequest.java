package slzjandroid.slzjapplication.dto;

import java.io.Serializable;

/**
 * 企业注册请求
 * Created by hdb on 2016/3/11.
 */
public class EnterpriseSignUpRequest implements Serializable {
    private String enterpriseName;//企业名称
    private String enterpriseEmail;//企业邮箱
    private String inviteCode;//邀请码
    private String cityName;//城市名称

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

    public String getInviteCode() {
        return inviteCode;
    }

    public void setInviteCode(String inviteCode) {
        this.inviteCode = inviteCode;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }
}
