package slzjandroid.slzjapplication.dto;

import java.io.Serializable;

/**
 * 费用归属（部门）列表信息 LIST
 * Created by hdb on 2016/3/14.
 */
public class DepartmentInfo implements Serializable {
    private String departmentIdx;//部门索引
    private String departmentName;//部门名称
    private String deptType;//类型
    private boolean isChoosed;

    public String getDepartmentIdx() {
        return departmentIdx;
    }

    public void setDepartmentIdx(String departmentIdx) {
        this.departmentIdx = departmentIdx;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getDeptType() {
        return deptType;
    }

    public void setDeptType(String deptType) {
        this.deptType = deptType;
    }

    public boolean isChoosed() {
        return isChoosed;
    }

    public void setChoosed(boolean choosed) {
        isChoosed = choosed;
    }
}

