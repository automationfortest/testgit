package slzjandroid.slzjapplication.dto;

/**
 * Created by hdb on 2016/3/30.
 */

import java.io.Serializable;

public
class CarType_shangwu implements Serializable {
    private String carServiceName;
    private String carServiceCode;
    private String carServiceInfo;
    private String startPrice;
    private String unitPrice;
    private String totalPrice;
    private String couponPrice;

    public String getCarServiceName() {
        return carServiceName;
    }

    public void setCarServiceName(String carServiceName) {
        this.carServiceName = carServiceName;
    }

    public String getCarServiceCode() {
        return carServiceCode;
    }

    public void setCarServiceCode(String carServiceCode) {
        this.carServiceCode = carServiceCode;
    }

    public String getCarServiceInfo() {
        return carServiceInfo;
    }

    public void setCarServiceInfo(String carServiceInfo) {
        this.carServiceInfo = carServiceInfo;
    }

    public String getStartPrice() {
        return startPrice;
    }

    public void setStartPrice(String startPrice) {
        this.startPrice = startPrice;
    }

    public String getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(String unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getCouponPrice() {
        return couponPrice;
    }

    public void setCouponPrice(String couponPrice) {
        this.couponPrice = couponPrice;
    }
}
