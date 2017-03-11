package slzjandroid.slzjapplication.dto;

import java.io.Serializable;

/**
 * 订单状态查询响应
 * Created by hdb on 2016/3/11.
 */
public class OrderStatusResponse<T> implements Serializable {
    private String status;//响应码
    private String message;//响应信息
    private T result;//业务对象
    private OrderDetailInfo orderDetailInfo;//订单详情对象

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

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    public OrderDetailInfo getOrderDetailInfo() {
        return orderDetailInfo;
    }

    public void setOrderDetailInfo(OrderDetailInfo orderDetailInfo) {
        this.orderDetailInfo = orderDetailInfo;
    }
}
