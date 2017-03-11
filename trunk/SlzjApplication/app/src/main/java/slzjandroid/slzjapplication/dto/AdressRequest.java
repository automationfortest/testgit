package slzjandroid.slzjapplication.dto;

import java.io.Serializable;

/**
 * 地址请求
 * Created by hdb on 2016/3/11.
 */
public
class AdressRequest implements Serializable {
    private String cityName;//城市名称
    private String keyWord;//搜索关键字

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getKeyWord() {
        return keyWord;
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }
}
