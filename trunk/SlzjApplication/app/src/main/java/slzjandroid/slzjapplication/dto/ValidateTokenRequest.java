package slzjandroid.slzjapplication.dto;


public  class ValidateTokenRequest implements java.io.Serializable{


    private String accessToken;
    private String loginName;

    /**
     * 验证后是否续期
     */
    private boolean isRenewal;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public boolean isRenewal() {
        return isRenewal;
    }

    public void setRenewal(boolean renewal) {
        isRenewal = renewal;
    }
}