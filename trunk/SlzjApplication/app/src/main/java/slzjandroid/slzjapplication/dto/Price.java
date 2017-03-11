package slzjandroid.slzjapplication.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

public class Price implements Serializable{
    private BigDecimal estimate;

    private BigDecimal totalPrice;

    private List<PriceDetail> detail;

    public BigDecimal getEstimate() {
        return estimate;
    }

    public void setEstimate(BigDecimal estimate) {
        this.estimate = estimate;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public List<PriceDetail> getDetail() {
        return detail;
    }

    public void setDetail(List<PriceDetail> detail) {
        this.detail = detail;
    }
}
