package slzjandroid.slzjapplication.dto;

import java.io.Serializable;

/**
 * Created by hdb on 2016/4/1.
 */
public class OrderCancelResult implements Serializable {
    private String cancelCost;//取消订单最低支付金额

    public String getCancelCost() {
        return cancelCost;
    }

    public void setCancelCost(String cancelCost) {
        this.cancelCost = cancelCost;
    }
}
