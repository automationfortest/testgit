package slzjandroid.slzjapplication.dto;


import java.io.Serializable;

public class OrderId implements Serializable {
    private Long orderId;
    private String thirdOrderId;

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getThirdOrderId() {
        return thirdOrderId;
    }

    public void setThirdOrderId(String thirdOrderId) {
        this.thirdOrderId = thirdOrderId;
    }
}
