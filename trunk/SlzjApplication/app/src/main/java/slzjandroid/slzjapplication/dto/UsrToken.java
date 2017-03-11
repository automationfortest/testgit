package slzjandroid.slzjapplication.dto;

import java.io.Serializable;

/**
 * 用户登录TOKEN
 * Created by hdb on 2016/3/14.
 */
public class UsrToken implements Serializable {
    private String accessToken;//用户token
    private String createTime;//Token创建时间
    private String expiresIn;//Token过期时间戳

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(String expiresIn) {
        this.expiresIn = expiresIn;
    }
}
