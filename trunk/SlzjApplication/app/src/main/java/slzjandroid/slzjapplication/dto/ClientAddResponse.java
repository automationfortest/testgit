package slzjandroid.slzjapplication.dto;

import java.io.Serializable;

/**
 * 增加成员响应
 * Created by hdb on 2016/3/14.
 */
public class ClientAddResponse<T> implements Serializable {
    private String status;//响应码
    private String message;//响应信息
    private T result;//业务对象
    private String clientUsrNo;//终端用户索引

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    public String getClientUsrNo() {
        return clientUsrNo;
    }

    public void setClientUsrNo(String clientUsrNo) {
        this.clientUsrNo = clientUsrNo;
    }
}
