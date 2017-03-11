package slzjandroid.slzjapplication.dto;

import java.io.Serializable;

/**
 * 订单下单请求
 * Created by hdb on 2016/3/11.
 */
public class OrderCarRequest implements Serializable {
    private String carServiceCode;//叫车服务代码
    private String orderType;//订单类型
    private String departureTime;//出发时间
    private String callerPhone;//叫车人手机号
    private String passengerPhone;//乘客手机号
    private String cityName;//出发城市名称
    private String cityCode;//出发城市代码
    private String departureName;//出发地名称
    private String departureLat;//出发地点经度
    private String departureLng;//出发地点纬度
    private String destinationName;//目的地名称
    private String destinationLat;//目的地经度
    private String destinationLng;//目的地纬度
    private String submitTime;//订单提交时间
    private String departmentIdx;//部门索引
    private String departmentName;//部门名称
    private String reasonIndex;//叫车原因索引
    private String reasonName;//叫车原因名称

    public String getCarServiceCode() {
        return carServiceCode;
    }

    public void setCarServiceCode(String carServiceCode) {
        this.carServiceCode = carServiceCode;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
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

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
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

    public String getSubmitTime() {
        return submitTime;
    }

    public void setSubmitTime(String submitTime) {
        this.submitTime = submitTime;
    }

    public String getDepartmentIdx() {
        return departmentIdx;
    }

    public void setDepartmentIdx(String departmentIdx) {
        this.departmentIdx = departmentIdx;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getReasonIndex() {
        return reasonIndex;
    }

    public void setReasonIndex(String reasonIndex) {
        this.reasonIndex = reasonIndex;
    }

    public String getReasonName() {
        return reasonName;
    }

    public void setReasonName(String reasonName) {
        this.reasonName = reasonName;
    }
}
