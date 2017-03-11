package slzjandroid.slzjapplication.dto;

import java.io.Serializable;

/**
 * 部门信息对象
 * Created by hdb on 2016/3/14.
 */
public class DeptInfo implements Serializable{
    private String deptNo;//部门索引
    private String deptName;//部门名称
    private String deptType;//部门类型
    private Boolean isChoosed=false;//是否被选中
    private String departmentName;
    private String departmentIdx;

    public String getDeptNo() {
        return deptNo;
    }

    public void setDeptNo(String deptNo) {
        this.deptNo = deptNo;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getDeptType() {
        return deptType;
    }

    public void setDeptType(String deptType) {
        this.deptType = deptType;
    }

    public Boolean getIsChoosed() {
        return isChoosed;
    }

    public void setIsChoosed(Boolean choosed) {
        isChoosed = choosed;
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
