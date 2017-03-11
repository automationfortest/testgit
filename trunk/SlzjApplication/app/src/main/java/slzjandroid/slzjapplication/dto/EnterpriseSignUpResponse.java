package slzjandroid.slzjapplication.dto;

import java.io.Serializable;

/**
 * 企业注册响应
 * Created by hdb on 2016/3/11.
 */
public class EnterpriseSignUpResponse  implements Serializable {
    private String status;//响应码
    private String message;//响应信

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
