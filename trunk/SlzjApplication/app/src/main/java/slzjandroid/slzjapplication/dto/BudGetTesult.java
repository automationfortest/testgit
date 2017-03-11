package slzjandroid.slzjapplication.dto;

import java.io.Serializable;

/**
 * Created by hdb on 2016/3/30.
 */
public  class BudGetTesult implements Serializable {
    private CarType_putong putong;
    private CarType_shushi shushi;
    private CarType_shangwu shangwu;
    private CarType_haohua haohua;

    public CarType_putong getPutong() {
        return putong;
    }

    public void setPutong(CarType_putong putong) {
        this.putong = putong;
    }

    public CarType_shushi getShushi() {
        return shushi;
    }

    public void setShushi(CarType_shushi shushi) {
        this.shushi = shushi;
    }

    public CarType_shangwu getShangwu() {
        return shangwu;
    }

    public void setShangwu(CarType_shangwu shangwu) {
        this.shangwu = shangwu;
    }

    public CarType_haohua getHaohua() {
        return haohua;
    }

    public void setHaohua(CarType_haohua haohua) {
        this.haohua = haohua;
    }
}
