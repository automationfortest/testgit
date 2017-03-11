package slzjandroid.slzjapplication.dto;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by hdb on 2016/4/11.
 */
public class ClientInfoResult implements Serializable{
    private String totalClientCnt;//公司总人数
    private String adminCnt;//管理员人数
    private String totalPage;//总页数
    private String currentPage;//当前查询页
    private String currentPrePage;//当前页记录条数
    private ArrayList<ClientUserInfo>  clientUserInfo;//用户信息对象

    public String getTotalClientCnt() {
        return totalClientCnt;
    }

    public void setTotalClientCnt(String totalClientCnt) {
        this.totalClientCnt = totalClientCnt;
    }

    public String getAdminCnt() {
        return adminCnt;
    }

    public void setAdminCnt(String adminCnt) {
        this.adminCnt = adminCnt;
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

    public ArrayList<ClientUserInfo> getClientUserInfo() {
        return clientUserInfo;
    }

    public void setClientUserInfo(ArrayList<ClientUserInfo> clientUserInfo) {
        this.clientUserInfo = clientUserInfo;
    }
}
