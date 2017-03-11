package slzjandroid.slzjapplication.dto;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by hdb on 2016/3/14.
 */
public class OrderDetailInfo implements Serializable {
    private String orderID;//订单号
    private String cityCode;//出发城市代码
    private String orderType;//订单类型
    private String passengerPhone;//乘客手机
    private String orderStatus;//订单状态
    private String driverName;//司机名
    private String driverPhone;//司机电话
    private String driverNum;//已通知司机数量
    private String driverCarType;//车类型
    private String driverCard;//车牌号
    private String driverAvatar;//司机照片URL
    private String driverOrderCount;//司机抢单数
    private String driverLevel;//司机级别
    private String departureName;//出发地名称
    private String departureLat;//出发地点经度
    private String departureLng;//出发地点纬度
    private String destinationName;//目的地名称
    private String destinationLat;//目的地经度
    private String destinationLng;//目的地纬度
    private String departureTime;//出发时间
    private String orderTime;//订单时间
    private String beginChargeTime;//开始计价时间
    private String finishTime;//结束时间
    private String normalDistance;//行驶总路程
    private String estimatePrice;//预估车费
    private String extraInfo;//附加信息
    private String totalPrice;//实际价格
    private ArrayList<PriceDetails> priceDetail;//价格明细对象
    private String passengerName;
    private String reasonName;
    private String departmentName;
    private String deptName;
    private String carServiceCode;
    private String carServiceName;
    private String currentLat;//司机纬度
    private String currentLng;//司机经度

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

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getDriverPhone() {
        return driverPhone;
    }

    public void setDriverPhone(String driverPhone) {
        this.driverPhone = driverPhone;
    }

    public String getDriverNum() {
        return driverNum;
    }

    public void setDriverNum(String driverNum) {
        this.driverNum = driverNum;
    }

    public String getDriverCarType() {
        return driverCarType;
    }

    public void setDriverCarType(String driverCarType) {
        this.driverCarType = driverCarType;
    }

    public String getDriverCard() {
        return driverCard;
    }

    public void setDriverCard(String driverCard) {
        this.driverCard = driverCard;
    }

    public String getDriverAvatar() {
        return driverAvatar;
    }

    public void setDriverAvatar(String driverAvatar) {
        this.driverAvatar = driverAvatar;
    }

    public String getDriverOrderCount() {
        return driverOrderCount;
    }

    public void setDriverOrderCount(String driverOrderCount) {
        this.driverOrderCount = driverOrderCount;
    }

    public String getDriverLevel() {
        return driverLevel;
    }

    public void setDriverLevel(String driverLevel) {
        this.driverLevel = driverLevel;
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

    public String getBeginChargeTime() {
        return beginChargeTime;
    }

    public void setBeginChargeTime(String beginChargeTime) {
        this.beginChargeTime = beginChargeTime;
    }

    public String getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(String finishTime) {
        this.finishTime = finishTime;
    }

    public String getNormalDistance() {
        return normalDistance;
    }

    public void setNormalDistance(String normalDistance) {
        this.normalDistance = normalDistance;
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

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public ArrayList<PriceDetails> getPriceDetail() {
        return priceDetail;
    }

    public void setPriceDetail(ArrayList<PriceDetails> priceDetail) {
        this.priceDetail = priceDetail;
    }

    public String getPassengerName() {
        return passengerName;
    }

    public void setPassengerName(String passengerName) {
        this.passengerName = passengerName;
    }

    public String getReasonName() {
        return reasonName;
    }

    public void setReasonName(String reasonName) {
        this.reasonName = reasonName;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getCarServiceCode() {
        return carServiceCode;
    }

    public void setCarServiceCode(String carServiceCode) {
        this.carServiceCode = carServiceCode;
    }

    public String getCarServiceName() {
        return carServiceName;
    }

    public void setCarServiceName(String carServiceName) {
        this.carServiceName = carServiceName;
    }

    public String getCurrentLat() {
        return currentLat;
    }

    public void setCurrentLat(String currentLat) {
        this.currentLat = currentLat;
    }

    public String getCurrentLng() {
        return currentLng;
    }

    public void setCurrentLng(String currentLng) {
        this.currentLng = currentLng;
    }
}
