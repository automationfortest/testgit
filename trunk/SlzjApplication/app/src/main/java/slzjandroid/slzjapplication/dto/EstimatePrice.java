package slzjandroid.slzjapplication.dto;


public class EstimatePrice  implements java.io.Serializable{
    private String carTypeNo;
    private String carTypeName;//经济 舒适 商务 豪华
    private String price;//价格 单位：元
    private String startPrice;//起步价格 单位：元
    private String normalUnitPrice;//每公里单价 单位：元

    public String getCarTypeNo() {
        return carTypeNo;
    }

    public void setCarTypeNo(String carTypeNo) {
        this.carTypeNo = carTypeNo;
    }

    public String getCarTypeName() {
        return carTypeName;
    }

    public void setCarTypeName(String carTypeName) {
        this.carTypeName = carTypeName;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getStartPrice() {
        return startPrice;
    }

    public void setStartPrice(String startPrice) {
        this.startPrice = startPrice;
    }

    public String getNormalUnitPrice() {
        return normalUnitPrice;
    }

    public void setNormalUnitPrice(String normalUnitPrice) {
        this.normalUnitPrice = normalUnitPrice;
    }
}
