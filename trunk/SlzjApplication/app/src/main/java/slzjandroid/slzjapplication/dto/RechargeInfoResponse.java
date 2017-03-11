package slzjandroid.slzjapplication.dto;

/**
 * Created by ASUS on 2016/4/28.
 */
public class RechargeInfoResponse {
    private int status;
    private String message;
    private ChargeInfoResult result;

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

    public ChargeInfoResult getResult() {
        return result;
    }

    public void setResult(ChargeInfoResult result) {
        this.result = result;
    }
}
