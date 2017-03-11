package slzjandroid.slzjapplication.dto;

import java.io.Serializable;

/**
 * Created by hdb on 2016/4/7.
 */
public class OrderDetailResult implements Serializable {
    private OrderDetailInfo info;

    public OrderDetailInfo getInfo() {
        return info;
    }

    public void setInfo(OrderDetailInfo info) {
        this.info = info;
    }
}
