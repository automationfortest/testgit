package slzjandroid.slzjapplication.dto;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 地址获取
 * Created by hdb on 2016/3/11.
 */
public
class AdressResponse implements Serializable {
    private int status;//响应码
    private String message;//响应信息
    //    private String keyWord;//搜索关键词
//    private ArrayList<PlaceData> placeData;//地点数据
    private ArrayList<PlaceData> result;//地点数据

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

    public ArrayList<PlaceData> getResult() {
        return result;
    }

    public void setResult(ArrayList<PlaceData> result) {
        this.result = result;
    }
}
