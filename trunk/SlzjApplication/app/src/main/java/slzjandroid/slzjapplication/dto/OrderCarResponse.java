package slzjandroid.slzjapplication.dto;

import java.io.Serializable;

/**
 * 订单下单响应
 * Created by hdb on 2016/3/11.
 */
public class OrderCarResponse implements Serializable{
    private int status;//响应状态
    private String message;//响应信息
    private OrderDetailInfo result;

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

    public OrderDetailInfo getResult() {
        return result;
    }

    public void setResult(OrderDetailInfo result) {
        this.result = result;
    }
}
