package slzjandroid.slzjapplication.dto;

import java.io.Serializable;

/**Token续期响应
 * Created by hdb on 2016/3/11.
 */
public  class TokenRenewalResponse implements Serializable {
    private int status;//响应码
    private String message;//响应信息
    private  TokenResult result;

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

    public TokenResult getResult() {
        return result;
    }

    public void setResult(TokenResult result) {
        this.result = result;
    }
}
