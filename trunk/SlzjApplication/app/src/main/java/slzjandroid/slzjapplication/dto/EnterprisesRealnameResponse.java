package slzjandroid.slzjapplication.dto;

import java.io.Serializable;

/**
 * Created by xuyifei on 16/5/31.
 */
public class EnterprisesRealnameResponse implements Serializable {

    private String status;
    private String message;
    private String result;
    private String lastSubmitDate;
    private String failureDesc;

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

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getLastSubmitDate() {
        return lastSubmitDate;
    }

    public void setLastSubmitDate(String lastSubmitDate) {
        this.lastSubmitDate = lastSubmitDate;
    }

    public String getFailureDesc() {
        return failureDesc;
    }

    public void setFailureDesc(String failureDesc) {
        this.failureDesc = failureDesc;
    }
}
