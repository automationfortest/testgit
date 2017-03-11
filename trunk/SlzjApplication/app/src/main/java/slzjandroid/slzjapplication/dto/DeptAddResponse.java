package slzjandroid.slzjapplication.dto;

import java.io.Serializable;

/**
 * 添加部门响应
 * Created by hdb on 2016/3/14.
 */
public  class DeptAddResponse implements Serializable{
    private int status;
    private String message;
    private DeptResult result;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public DeptResult getResult() {
        return result;
    }

    public void setResult(DeptResult result) {
        this.result = result;
    }
}
