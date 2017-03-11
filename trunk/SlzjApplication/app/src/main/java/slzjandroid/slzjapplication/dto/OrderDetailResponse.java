package slzjandroid.slzjapplication.dto;

import java.io.Serializable;

/**
 * 订单详情响应
 * Created by hdb on 2016/3/13.
 */
public class OrderDetailResponse implements Serializable{
    private String status;//响应码
    private String message;//响应详情
    private OrderDetailResult result;//业务对象

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

    public OrderDetailResult getResult() {
        return result;
    }

    public void setResult(OrderDetailResult result) {
        this.result = result;
    }
}
