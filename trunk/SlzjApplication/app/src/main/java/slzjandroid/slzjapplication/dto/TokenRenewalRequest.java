package slzjandroid.slzjapplication.dto;

import java.io.Serializable;

/**
 * Token续期请求
 * Created by hdb on 2016/3/11.
 */
public class TokenRenewalRequest implements Serializable {
    private String Cellphone;
    private String timestamp;

    public String getCellphone() {
        return Cellphone;
    }

    public void setCellphone(String cellphone) {
        Cellphone = cellphone;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
