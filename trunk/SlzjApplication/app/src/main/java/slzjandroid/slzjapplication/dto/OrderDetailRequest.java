package slzjandroid.slzjapplication.dto;

import java.io.Serializable;

/**
 * 订单详情请求
 * Created by hdb on 2016/3/13.
 */
public class OrderDetailRequest<T, O> implements Serializable {
    private String orderID;

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }
}
