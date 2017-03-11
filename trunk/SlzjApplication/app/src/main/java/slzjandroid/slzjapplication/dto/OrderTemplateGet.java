package slzjandroid.slzjapplication.dto;

import java.io.Serializable;

/**
 * 用户叫车模板获取
 * Created by hdb on 2016/3/11.
 */
public class OrderTemplateGet implements Serializable {
    private int status;//响应码
    private String message;//响应消息
    private OrderTemplateResult result;//业务对象

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

    public OrderTemplateResult getResult() {
        return result;
    }

    public void setResult(OrderTemplateResult result) {
        this.result = result;
    }
}
