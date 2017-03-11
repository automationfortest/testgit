package slzjandroid.slzjapplication.dto;

import java.io.Serializable;

/**
 * Created by hdb on 2016/3/29.
 */
public
class CarServiceType implements Serializable {
    private String carServiceCode;
    private String carServiceName;

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
}
