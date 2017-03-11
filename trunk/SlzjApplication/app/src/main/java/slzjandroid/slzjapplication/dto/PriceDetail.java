package slzjandroid.slzjapplication.dto;

import java.io.Serializable;

/**
 * 滴滴费用
 */
public class PriceDetail implements Serializable{
    private String name;
    private Double amount;
    private String type;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
