package slzjandroid.slzjapplication.dto;

import java.io.Serializable;

/**
 * Created by hdb on 2016/3/14.
 */
public class RuleInfo implements Serializable {
    //    private String ruleType="";//规则类型
//    private String startTime="";//本日允许打车起始时间
//    private String endTimeT="";//本日允许打车
//    private String endTimeT1="";//次日允许打车结束时间
//    private String departureName="";//允许出发地名称
//    private String departureLng="";//允许出发地坐标（火星）
//    private String departureLat="";//允许出发地坐标（火星）
    private String ruleType;//类型
    private String conpouName;
    private String xAxis;//经度
    private String yAxis;//纬度
    private String range;

    public String getRange() {
        return range;
    }

    public void setRange(String range) {
        this.range = range;
    }

    public String getRuleType() {
        return ruleType;
    }

    public void setRuleType(String ruleType) {
        this.ruleType = ruleType;
    }

    public String getConpouName() {
        return conpouName;
    }

    public void setConpouName(String conpouName) {
        this.conpouName = conpouName;
    }

    public String getXAxis() {
        return xAxis;
    }

    public void setxAxis(String xAxis) {
        this.xAxis = xAxis;
    }

    public String getYAxis() {
        return yAxis;
    }

    public void setyAxis(String yAxis) {
        this.yAxis = yAxis;
    }
}
