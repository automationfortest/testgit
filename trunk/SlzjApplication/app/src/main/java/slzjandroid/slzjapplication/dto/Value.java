package slzjandroid.slzjapplication.dto;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by hdb on 2016/4/2.
 */
public class Value implements Serializable, Comparable<Value> {
    private static final long serialVersionUID = -3834382558399929847L;
    private String area;
    private ArrayList<RequireLevel> requireLevel;
    private String district;
    private String name;
    private String cityId;
    private String pinyin;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

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

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }

    @Override
    public int compareTo(Value value) {
        return 0;
    }
}
