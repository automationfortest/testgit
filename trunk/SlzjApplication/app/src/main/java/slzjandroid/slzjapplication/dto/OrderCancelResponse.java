package slzjandroid.slzjapplication.dto;

import java.io.Serializable;

/**
 * 订单取消响应
 * Created by hdb on 2016/3/11.
 */
public class OrderCancelResponse implements Serializable {
    private int status;//响应状态码
    private String message;//响应信息
    private OrderCancelResult result;//业务对象

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

    public OrderCancelResult getResult() {
        return result;
    }

    public void setResult(OrderCancelResult result) {
        this.result = result;
    }
}
