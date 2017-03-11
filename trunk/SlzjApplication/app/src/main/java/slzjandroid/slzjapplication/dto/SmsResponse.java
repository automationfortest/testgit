package slzjandroid.slzjapplication.dto;

import java.io.Serializable;

/**
 * 短信响应
 * Created by hdb on 2016/3/11.
 */
public class SmsResponse implements Serializable{
    private int status;//响应码 长度3
    private String message;//响应描述信息

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
}
