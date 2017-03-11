package slzjandroid.slzjapplication.dto;

import java.io.Serializable;

/**
 * Created by xuyifei on 16/5/31.
 */
public class EnterprisesRealnameRequest implements Serializable {

    private String enterpriseIdx;
    private String enterpriseName;
    private String enterpriseType;
    private String registerNo;
    private String agentName;
    private String agentIdCard;
    private String licencePic;
    private String idCardPic;
    private String proxyPic;
    private String submitDate;
    private String city;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
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

    public String getEnterpriseType() {
        return enterpriseType;
    }

    public void setEnterpriseType(String enterpriseType) {
        this.enterpriseType = enterpriseType;
    }

    public String getRegisterNo() {
        return registerNo;
    }

    public void setRegisterNo(String registerNo) {
        this.registerNo = registerNo;
    }

    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    public String getAgentIdCard() {
        return agentIdCard;
    }

    public void setAgentIdCard(String agentIdCard) {
        this.agentIdCard = agentIdCard;
    }

    public String getLicencePic() {
        return licencePic;
    }

    public void setLicencePic(String licencePic) {
        this.licencePic = licencePic;
    }

    public String getIdCardPic() {
        return idCardPic;
    }

    public void setIdCardPic(String idCardPic) {
        this.idCardPic = idCardPic;
    }

    public String getProxyPic() {
        return proxyPic;
    }

    public void setProxyPic(String proxyPic) {
        this.proxyPic = proxyPic;
    }

    public String getSubmitDate() {
        return submitDate;
    }

    public void setSubmitDate(String submitDate) {
        this.submitDate = submitDate;
    }
}
