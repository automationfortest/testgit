package slzjandroid.slzjapplication.dto;

import java.util.Date;

public class Driver implements java.io.Serializable {

    /**
     * 驾驶员姓名
     */
    private String driverName;

    /**
     * 驾驶员电话
     */
    private String driverPhone;

    /**
     * 车牌号码
     */
    private String driverCard;

    /**
     * 司机星级
     */
    private Integer driverLevel;

    /**
     * 车型品牌
     */
    private String driverCarType;

    /**
     * 司机抢单时间
     */
    private Date driverGrabTime;

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

    public String getDriverCard() {
        return driverCard;
    }

    public void setDriverCard(String driverCard) {
        this.driverCard = driverCard;
    }

    public Integer getDriverLevel() {
        return driverLevel;
    }

    public void setDriverLevel(Integer driverLevel) {
        this.driverLevel = driverLevel;
    }

    public String getDriverCarType() {
        return driverCarType;
    }

    public void setDriverCarType(String driverCarType) {
        this.driverCarType = driverCarType;
    }

    public Date getDriverGrabTime() {
        return driverGrabTime;
    }

    public void setDriverGrabTime(Date driverGrabTime) {
        this.driverGrabTime = driverGrabTime;
    }
}
