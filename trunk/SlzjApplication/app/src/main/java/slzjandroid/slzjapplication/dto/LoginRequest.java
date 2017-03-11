package slzjandroid.slzjapplication.dto;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 登录请求
 * Created by hdb on 2016/3/11.
 */
public class LoginRequest implements Serializable {
    private String cellphone;
    private String smsCode;
    private String deviceID;
    private String timeStamp;

    public String getCellphone() {
        return cellphone;
    }

    public void setCellphone(String cellphone) {
        this.cellphone = cellphone;
    }

    public String getSmsCode() {
        return smsCode;
    }

    public void setSmsCode(String smsCode) {
        this.smsCode = smsCode;
    }

    public String getDeviceID() {
        return deviceID;
    }

    public void setDeviceID(String deviceID) {
        this.deviceID = deviceID;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public static Map<String, Object> loginReqInfo(String cellphone, String smsCode, String deviceID, String timeStamp) {
        Map<String, Object> map = new HashMap<>();
        map.put("cellphone", cellphone);
        map.put("smsCode", smsCode);
        map.put("deviceID", deviceID);
        map.put("timeStamp", timeStamp);
        return map;
    }
}
