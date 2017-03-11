package slzjandroid.slzjapplication.dto;

import java.io.Serializable;

/**
 * 充值请求响应
 * Created by hdb on 2016/3/14.
 */
public class PaymentOrderResponse<T> implements Serializable {
    private String  status;//响应码
    private String message;//响应详情
    private T result;//业务对象
    private String  paymentMethod;//支付方式

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

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
}
