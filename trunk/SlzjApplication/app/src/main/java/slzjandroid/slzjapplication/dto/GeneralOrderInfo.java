package slzjandroid.slzjapplication.dto;

import java.io.Serializable;

/**
 * 订单列表对象
 * Created by hdb on 2016/3/14.
 */
public  class GeneralOrderInfo implements Serializable{
    private String orderID;//订单号
    private String orderStatus;//订单状态
    private String departureName;//出发地名称
    private String destinationName;//目的地名
    private String orderTime;//订单时间

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getDepartureName() {
        return departureName;
    }

    public void setDepartureName(String departureName) {
        this.departureName = departureName;
    }

    public String getDestinationName() {
        return destinationName;
    }

    public void setDestinationName(String destinationName) {
        this.destinationName = destinationName;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }
}
