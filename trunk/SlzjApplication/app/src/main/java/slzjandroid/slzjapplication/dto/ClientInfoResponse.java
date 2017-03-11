package slzjandroid.slzjapplication.dto;

import java.io.Serializable;

/**
 * 获取团队信息响应
 * Created by hdb on 2016/3/13.
 */
public class ClientInfoResponse implements Serializable {
    private String status;//响应码
    private String message;//响应信息
    private ClientInfoResult result;//业务对象

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

    public ClientInfoResult getResult() {
        return result;
    }

    public void setResult(ClientInfoResult result) {
        this.result = result;
    }
}
