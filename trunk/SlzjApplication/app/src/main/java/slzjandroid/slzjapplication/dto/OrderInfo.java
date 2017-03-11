package slzjandroid.slzjapplication.dto;

import java.io.Serializable;

/**
 * 订单对象
 * Created by hdb on 2016/3/14.
 */
public class OrderInfo implements Serializable {
    private String orderID;//订单号
    private String cityCode;//出发城市代码
    private String orderType;//订单类型
    private String callerPhone;//叫车人手机号
    private String passengerPhone;//乘客手机号
    private String orderStatus;//订单状态
    private String departureName;//出发地名称
    private String departureLat;//出发地点经度
    private String departureLng;//出发地点纬度
    private String destinationName;//目的地名称
    private String destinationLat;//目的地经度
    private String destinationLng;//目的地纬度
    private String departureTime;//出发时间
    private String orderTime;//订单时间
    private String estimatePrice;//预估车费
    private String extraInfo;//附加信息

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getCallerPhone() {
        return callerPhone;
    }

    public void setCallerPhone(String callerPhone) {
        this.callerPhone = callerPhone;
    }

    public String getPassengerPhone() {
        return passengerPhone;
    }

    public void setPassengerPhone(String passengerPhone) {
        this.passengerPhone = passengerPhone;
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

    public String getDepartureLat() {
        return departureLat;
    }

    public void setDepartureLat(String departureLat) {
        this.departureLat = departureLat;
    }

    public String getDepartureLng() {
        return departureLng;
    }

    public void setDepartureLng(String departureLng) {
        this.departureLng = departureLng;
    }

    public String getDestinationName() {
        return destinationName;
    }

    public void setDestinationName(String destinationName) {
        this.destinationName = destinationName;
    }

    public String getDestinationLat() {
        return destinationLat;
    }

    public void setDestinationLat(String destinationLat) {
        this.destinationLat = destinationLat;
    }

    public String getDestinationLng() {
        return destinationLng;
    }

    public void setDestinationLng(String destinationLng) {
        this.destinationLng = destinationLng;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public String getEstimatePrice() {
        return estimatePrice;
    }

    public void setEstimatePrice(String estimatePrice) {
        this.estimatePrice = estimatePrice;
    }

    public String getExtraInfo() {
        return extraInfo;
    }

    public void setExtraInfo(String extraInfo) {
        this.extraInfo = extraInfo;
    }
}
