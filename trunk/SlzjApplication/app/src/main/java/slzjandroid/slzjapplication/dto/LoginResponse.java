package slzjandroid.slzjapplication.dto;

import java.io.Serializable;

/**
 * 登录响应
 * Created by hdb on 2016/3/11.
 */
public class LoginResponse implements Serializable {
    private int status;//响应码
    private String message;//响应信息
    private TokenAndEnterpriseInfo result;//业务对象 ，泛型

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public TokenAndEnterpriseInfo getResult() {
        return result;
    }

    public void setResult(TokenAndEnterpriseInfo result) {
        this.result = result;
    }
}
