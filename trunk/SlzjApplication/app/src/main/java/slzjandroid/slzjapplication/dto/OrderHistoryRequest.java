package slzjandroid.slzjapplication.dto;

import java.io.Serializable;

/**
 * 订单历史查询请求
 * Created by hdb on 2016/3/13.
 */
public class OrderHistoryRequest implements Serializable {
    private String usrType;//用户类型
    private String orderType;//订单类型
    private String perpage;//每页显示记录条数
    private String currentPage;//
    private String startDate;//订单查询开始日期
    private String endDate;//订单查询结束日期

    public String getUsrType() {
        return usrType;
    }

    public void setUsrType(String usrType) {
        this.usrType = usrType;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getPerpage() {
        return perpage;
    }

    public void setPerpage(String perpage) {
        this.perpage = perpage;
    }

    public String getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(String currentPage) {
        this.currentPage = currentPage;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
}

