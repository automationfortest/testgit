package slzjandroid.slzjapplication.dto;

import java.io.Serializable;

/**
 * 报价对象
 * Created by hdb on 2016/3/14.
 */
public class PriceInfo implements Serializable {
    private String carServiceCode;//叫车服务代码
    private String carServiceInfo;//车辆类型说明
    private String startPrice;//起步价
    private String unitPrice;//公里单价
    private String totalPrice;//预计总价

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
}

