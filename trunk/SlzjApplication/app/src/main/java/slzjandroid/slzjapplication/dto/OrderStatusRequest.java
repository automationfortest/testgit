package slzjandroid.slzjapplication.dto;

import java.io.Serializable;

/**
 * 订单状态查询请求
 * Created by hdb on 2016/3/11.
 */
public class OrderStatusRequest implements Serializable {
    private String orderID;//订单号

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }
}
