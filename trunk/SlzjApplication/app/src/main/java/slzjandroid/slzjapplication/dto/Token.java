package slzjandroid.slzjapplication.dto;


public class Token implements java.io.Serializable {
    /**
     * 接口获取授权后的access token
     */
    private String accessToken;
    /**
     * access_token的生命周期，单位是秒数
     */
    private int expiresIn;
    /**
     * 权限范围
     */
    private String scope;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public int getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(int expiresIn) {
        this.expiresIn = expiresIn;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }
}
