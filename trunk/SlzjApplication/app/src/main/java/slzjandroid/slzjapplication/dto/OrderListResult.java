package slzjandroid.slzjapplication.dto;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by hdb on 2016/4/6.
 */
public class OrderListResult implements Serializable {
    private String  orderingInfo;
    private String totalPage;
    private String currentPage;
    private String currentPrePage;
    private ArrayList<GeneralOrderInfo> generalOrderInfo;

    public String getOrderingInfo() {
        return orderingInfo;
    }

    public void setOrderingInfo(String orderingInfo) {
        this.orderingInfo = orderingInfo;
    }

    public String getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(String totalPage) {
        this.totalPage = totalPage;
    }

    public String getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(String currentPage) {
        this.currentPage = currentPage;
    }

    public String getCurrentPrePage() {
        return currentPrePage;
    }

    public void setCurrentPrePage(String currentPrePage) {
        this.currentPrePage = currentPrePage;
    }

    public ArrayList<GeneralOrderInfo> getGeneralOrderInfo() {
        return generalOrderInfo;
    }

    public void setGeneralOrderInfo(ArrayList<GeneralOrderInfo> generalOrderInfo) {
        this.generalOrderInfo = generalOrderInfo;
    }
}
