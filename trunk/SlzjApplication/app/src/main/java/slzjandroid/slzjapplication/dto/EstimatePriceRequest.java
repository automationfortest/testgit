package slzjandroid.slzjapplication.dto;


public class EstimatePriceRequest implements java.io.Serializable{
    private String callerPhone;
    private String cityCode;
    private String cityName;//城市名称（中文）
    private String departureTime;
    private String flat;//出发地经度
    private String flng;//出发地纬度
    private String tlat;//目的地经度
    private String tlng;//目的地纬度

    public String getCallerPhone() {
        return callerPhone;
    }

    public void setCallerPhone(String callerPhone) {
        this.callerPhone = callerPhone;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
    }

    public String getFlat() {
        return flat;
    }

    public void setFlat(String flat) {
        this.flat = flat;
    }

    public String getFlng() {
        return flng;
    }

    public void setFlng(String flng) {
        this.flng = flng;
    }

    public String getTlat() {
        return tlat;
    }

    public void setTlat(String tlat) {
        this.tlat = tlat;
    }

    public String getTlng() {
        return tlng;
    }

    public void setTlng(String tlng) {
        this.tlng = tlng;
    }
}
