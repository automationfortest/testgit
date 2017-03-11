package slzjandroid.slzjapplication.dto;

import java.io.Serializable;

/**
 * 打车费预算响应
 * Created by hdb on 2016/3/11.
 */
public
class BudGetResponse implements Serializable {
    private int status;//响应码
    private String message;//响应信息
    private BudGetTesult result;

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

    public BudGetTesult getResult() {
        return result;
    }

    public void setResult(BudGetTesult result) {
        this.result = result;
    }
}
