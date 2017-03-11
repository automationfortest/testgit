package slzjandroid.slzjapplication.dto;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by hdb on 2016/3/29.
 */
public class OrderTemplateResult implements Serializable {
    private ArrayList<DepartmentInfo> deparList;
    //  private ArrayList<RuleInfo>  ruleList;
    private ArrayList<RuleInfo> ruleList;
    private ArrayList<CallReason> reasonList;
    private ArrayList<CarServiceType> carServiceList;

    public ArrayList<DepartmentInfo> getDeparList() {
        return deparList;
    }

    public void setDeparList(ArrayList<DepartmentInfo> deparList) {
        this.deparList = deparList;
    }

    public ArrayList<RuleInfo> getRuleList() {
        return ruleList;
    }

    public void setRuleList(ArrayList<RuleInfo> ruleList) {
        this.ruleList = ruleList;
    }

    public ArrayList<CallReason> getReasonList() {
        return reasonList;
    }

    public void setReasonList(ArrayList<CallReason> reasonList) {
        this.reasonList = reasonList;
    }

    public ArrayList<CarServiceType> getCarServiceList() {
        return carServiceList;
    }

    public void setCarServiceList(ArrayList<CarServiceType> carServiceList) {
        this.carServiceList = carServiceList;
    }
}
