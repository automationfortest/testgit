package slzjandroid.slzjapplication.dto;

import java.io.Serializable;

/**
 * 获取部门列表响应
 * Created by hdb on 2016/3/13.
 */
public class DeptListResponse<T> implements Serializable {
    private String status;//响应码
    private String message;//响应信息
    private T result;//业务对象
    private DeptInfo deptInfo;//部门信息对象

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    public DeptInfo getDeptInfo() {
        return deptInfo;
    }

    public void setDeptInfo(DeptInfo deptInfo) {
        this.deptInfo = deptInfo;
    }
}
