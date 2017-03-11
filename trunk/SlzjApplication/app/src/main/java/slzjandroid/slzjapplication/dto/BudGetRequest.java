package slzjandroid.slzjapplication.dto;

import java.io.Serializable;

/**
 * 打车费预算
 * Created by hdb on 2016/3/11.
 */
public
class BudGetRequest implements Serializable {
    public String departureTime;//出发时间
    public String cityCode;//出发城市代码
    public String departureLat;//出发地点经度
    public String departureLng;//出发地点纬度
    public String destinationLat;//目的地经度
    public String destinationLng;//目的地纬度

    public String getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
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
}
