package slzjandroid.slzjapplication.dto;

/**
 * Created by ASUS on 2016/4/21.
 */
public class EnterPriseInfoResponse {
    private int status;
    private String message;
    private EnterPriseInfoResult result;

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

    public EnterPriseInfoResult getResult() {
        return result;
    }

    public void setResult(EnterPriseInfoResult result) {
        this.result = result;
    }
}
