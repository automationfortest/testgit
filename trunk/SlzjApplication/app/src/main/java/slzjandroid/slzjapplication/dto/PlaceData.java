package slzjandroid.slzjapplication.dto;

import java.io.Serializable;

/**
 *地点数据
 * Created by hdb on 2016/3/14.
 */
public class PlaceData implements Serializable {
    private String displayName;//地点名称
    private String cityCode;//城市代码
    private String adressLng;//地址经度
    private String adressLat;//地址纬度
    private String keyWord;//搜索关键词
    private String cityName;//城市名称
    private String address;//详细地址

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getAdressLng() {
        return adressLng;
    }

    public void setAdressLng(String adressLng) {
        this.adressLng = adressLng;
    }

    public String getAdressLat() {
        return adressLat;
    }

    public void setAdressLat(String adressLat) {
        this.adressLat = adressLat;
    }

    public String getKeyWord() {
        return keyWord;
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
