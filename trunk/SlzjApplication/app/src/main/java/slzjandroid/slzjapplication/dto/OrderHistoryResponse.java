package slzjandroid.slzjapplication.dto;

import java.io.Serializable;

/**
 * 历史记录查询响应
 * Created by hdb on 2016/3/13.
 */
public class OrderHistoryResponse implements Serializable{
    private int status;//响应码
    private String message;//响应详情
    private OrderListResult result;//业务对象

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

    public OrderListResult getResult() {
        return result;
    }

    public void setResult(OrderListResult result) {
        this.result = result;
    }
}
