package slzjandroid.slzjapplication.dto;

import java.io.Serializable;

/**
 * Created by xuyifei on 16/5/26.
 */
public class TokenRefresh implements Serializable {

    private String message;
    private Result result;
    private String status;
    private String success;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public class Result implements Serializable {

        private String balance;
        private EnterpriseInfo enterpriseInfo;
        private String newUsrFlag;
        private String onTravelFlag;
        private String onTravelOrderId;
        private String unreadMsg;
        private String userNo;
        private String usrName;
        private UsrToken usrToken;
        private String usrType;

        public String getBalance() {
            return balance;
        }

        public void setBalance(String balance) {
            this.balance = balance;
        }

        public EnterpriseInfo getEnterpriseInfo() {
            return enterpriseInfo;
        }

        public void setEnterpriseInfo(EnterpriseInfo enterpriseInfo) {
            this.enterpriseInfo = enterpriseInfo;
        }

        public String getNewUsrFlag() {
            return newUsrFlag;
        }

        public void setNewUsrFlag(String newUsrFlag) {
            this.newUsrFlag = newUsrFlag;
        }

        public String getOnTravelFlag() {
            return onTravelFlag;
        }

        public void setOnTravelFlag(String onTravelFlag) {
            this.onTravelFlag = onTravelFlag;
        }

        public String getOnTravelOrderId() {
            return onTravelOrderId;
        }

        public void setOnTravelOrderId(String onTravelOrderId) {
            this.onTravelOrderId = onTravelOrderId;
        }

        public String getUnreadMsg() {
            return unreadMsg;
        }

        public void setUnreadMsg(String unreadMsg) {
            this.unreadMsg = unreadMsg;
        }

        public String getUserNo() {
            return userNo;
        }

        public void setUserNo(String userNo) {
            this.userNo = userNo;
        }

        public String getUsrName() {
            return usrName;
        }

        public void setUsrName(String usrName) {
            this.usrName = usrName;
        }

        public UsrToken getUsrToken() {
            return usrToken;
        }

        public void setUsrToken(UsrToken usrToken) {
            this.usrToken = usrToken;
        }

        public String getUsrType() {
            return usrType;
        }

        public void setUsrType(String usrType) {
            this.usrType = usrType;
        }
    }


    public class UsrToken implements Serializable {
        private String accessToken;
        private String createTime;
        private String expiresIn;

        public String getAccessToken() {
            return accessToken;
        }

        public void setAccessToken(String accessToken) {
            this.accessToken = accessToken;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getExpiresIn() {
            return expiresIn;
        }

        public void setExpiresIn(String expiresIn) {
            this.expiresIn = expiresIn;
        }
    }
}



