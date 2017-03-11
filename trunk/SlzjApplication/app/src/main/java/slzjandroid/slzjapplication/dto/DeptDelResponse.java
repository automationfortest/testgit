package slzjandroid.slzjapplication.dto;

import java.io.Serializable;

/**
 * 删除部门响应
 * Created by hdb on 2016/3/14.
 */
public class DeptDelResponse implements Serializable {
    private String status;//响应码
    private String message;//响应信息

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
}
