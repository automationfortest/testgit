package slzjandroid.slzjapplication.dto;

import java.io.Serializable;

/**
 * 部门信息对象
 * Created by hdb on 2016/3/14.
 */
public class MemberDeptInfo implements Serializable{

    private String deptType;//部门类型
    private String departmentName;
    private String departmentIdx;

    public String getDeptType() {
        return deptType;
    }

    public void setDeptType(String deptType) {
        this.deptType = deptType;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getDepartmentIdx() {
        return departmentIdx;
    }

    public void setDepartmentIdx(String departmentIdx) {
        this.departmentIdx = departmentIdx;
    }
}
