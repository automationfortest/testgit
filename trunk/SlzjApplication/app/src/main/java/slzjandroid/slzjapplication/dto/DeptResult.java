package slzjandroid.slzjapplication.dto;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by ASUS on 2016/4/17.
 */
public class DeptResult implements Serializable{
    private ArrayList<DeptInfo> deptInfo;

    public ArrayList<DeptInfo> getDeptInfo() {
        return deptInfo;
    }

    public void setDeptInfo(ArrayList<DeptInfo> deptInfo) {
        this.deptInfo = deptInfo;
    }
}
