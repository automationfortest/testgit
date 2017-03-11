package slzjandroid.slzjapplication.dto;

import java.io.Serializable;

/**
 * Created by hdb on 2016/4/2.
 */
public class RequireLevel implements Serializable{
    private String rule;//规则
    private String normalUnitPrice;//单价
    private String isDefault;//是否默认选中
    private String startPrice;//起步价
    private String name;//车型
    private String carLevel;//叫车服务代码

    public String getRule() {
        return rule;
    }

    public void setRule(String rule) {
        this.rule = rule;
    }

    public String getNormalUnitPrice() {
        return normalUnitPrice;
    }

    public void setNormalUnitPrice(String normalUnitPrice) {
        this.normalUnitPrice = normalUnitPrice;
    }

    public String getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(String isDefault) {
        this.isDefault = isDefault;
    }

    public String getStartPrice() {
        return startPrice;
    }

    public void setStartPrice(String startPrice) {
        this.startPrice = startPrice;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCarLevel() {
        return carLevel;
    }

    public void setCarLevel(String carLevel) {
        this.carLevel = carLevel;
    }
}
