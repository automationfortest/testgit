package slzjandroid.slzjapplication.dto;

/**
 * Created by ASUS on 2016/4/25.
 */
public class DeptGetResponse {
    private int status;
    private String message;
    private DeptGetResult result;

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

    public DeptGetResult getResult() {
        return result;
    }

    public void setResult(DeptGetResult result) {
        this.result = result;
    }
}
