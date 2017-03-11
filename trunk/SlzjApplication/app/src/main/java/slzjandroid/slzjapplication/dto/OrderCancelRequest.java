package slzjandroid.slzjapplication.dto;

import java.io.Serializable;

/**
 * 订单取消请求
 * Created by hdb on 2016/3/11.
 */
public class OrderCancelRequest implements Serializable{
    private String orderID;//订单号
    private String forceCancelFlag;//强制取消标

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getForceCancelFlag() {
        return forceCancelFlag;
    }

    public void setForceCancelFlag(String forceCancelFlag) {
        this.forceCancelFlag = forceCancelFlag;
    }
}
