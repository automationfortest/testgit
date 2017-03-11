package slzjandroid.slzjapplication.dto;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by hdb on 2016/4/2.
 */
public class Hotvalue implements Serializable {
    private String area;
    private ArrayList<RequireLevel> requireLevel;
    private String district;
    private String name;
    private String cityId;

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public ArrayList<RequireLevel> getRequireLevel() {
        return requireLevel;
    }

    public void setRequireLevel(ArrayList<RequireLevel> requireLevel) {
        this.requireLevel = requireLevel;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }
}
