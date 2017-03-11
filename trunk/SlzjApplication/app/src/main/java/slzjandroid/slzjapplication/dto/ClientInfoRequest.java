package slzjandroid.slzjapplication.dto;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 获取团队信息请求
 * Created by hdb on 2016/3/13.
 */
public class ClientInfoRequest implements Serializable {
    private String enterpriseIdx;//企业索引
    private String keyword;//搜索关键词
    private String perpage;//每页显示记录条数
    private String currentPage;//当前查询页

    public String getEnterpriseIdx() {
        return enterpriseIdx;
    }

    public void setEnterpriseIdx(String enterpriseIdx) {
        this.enterpriseIdx = enterpriseIdx;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
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


}
