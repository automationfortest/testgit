package slzjandroid.slzjapplication.dto;

import java.io.Serializable;

/**
 * 删除成员响应
 * Created by hdb on 2016/3/14.
 */
public  class ClientDelResponse implements Serializable {
    private String status;
    private String message;

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
