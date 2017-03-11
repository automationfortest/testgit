package slzjandroid.slzjapplication.dto;

import java.io.Serializable;

/**
 * Created by hdb on 2016/3/11.
 */
public class MainPageInfoGet<T> implements Serializable {
    private String status;//响应码
    private String message;//响应信息
    private T result;//业务对象
    private String orderNum;//本月订单数量
    private String totalPayment;//本月支付总金额
    private String area1Desc;//区域1文字
    private String area1Url;//区域1图片URL
    private String area2Desc;//区域2文字
    private String area2Url;//区域2图片URL
    private String area3Desc;//区域3文字
    private String area3URL;//区域3图片URL
    private String area4Desc;//区域4文字
    private String area4URL;//区域4图片URL

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

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    public String getTotalPayment() {
        return totalPayment;
    }

    public void setTotalPayment(String totalPayment) {
        this.totalPayment = totalPayment;
    }

    public String getArea1Desc() {
        return area1Desc;
    }

    public void setArea1Desc(String area1Desc) {
        this.area1Desc = area1Desc;
    }

    public String getArea1Url() {
        return area1Url;
    }

    public void setArea1Url(String area1Url) {
        this.area1Url = area1Url;
    }

    public String getArea2Desc() {
        return area2Desc;
    }

    public void setArea2Desc(String area2Desc) {
        this.area2Desc = area2Desc;
    }

    public String getArea2Url() {
        return area2Url;
    }

    public void setArea2Url(String area2Url) {
        this.area2Url = area2Url;
    }

    public String getArea3Desc() {
        return area3Desc;
    }

    public void setArea3Desc(String area3Desc) {
        this.area3Desc = area3Desc;
    }

    public String getArea3URL() {
        return area3URL;
    }

    public void setArea3URL(String area3URL) {
        this.area3URL = area3URL;
    }

    public String getArea4Desc() {
        return area4Desc;
    }

    public void setArea4Desc(String area4Desc) {
        this.area4Desc = area4Desc;
    }

    public String getArea4URL() {
        return area4URL;
    }

    public void setArea4URL(String area4URL) {
        this.area4URL = area4URL;
    }
}
