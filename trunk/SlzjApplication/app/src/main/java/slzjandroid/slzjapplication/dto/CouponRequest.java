package slzjandroid.slzjapplication.dto;

import java.io.Serializable;
import java.util.List;

/**
 * Created by xuyifei on 16/4/20.
 */
public class CouponRequest implements Serializable {
    private String status;
    private String message;
    private ResultInfo result;
    private String success;

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

    public ResultInfo getResult() {
        return result;
    }

    public void setResult(ResultInfo result) {
        this.result = result;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public class CouponInfo implements Serializable {
        private String couponName;
        private String validate;
        private String discountAmount;
        private String couponCnt;
        private String extInfo;
        private String couponResource;
        private String couponDiscountLimit;

        public String getCouponDiscountLimit() {
            return couponDiscountLimit;
        }

        public void setCouponDiscountLimit(String couponDiscountLimit) {
            this.couponDiscountLimit = couponDiscountLimit;
        }



        public String getCouponName() {
            return couponName;
        }

        public void setCouponName(String couponName) {
            this.couponName = couponName;
        }

        public String getValidate() {
            return validate;
        }

        public void setValidate(String validate) {
            this.validate = validate;
        }

        public String getDiscountAmount() {
            return discountAmount;
        }

        public void setDiscountAmount(String discountAmount) {
            this.discountAmount = discountAmount;
        }

        public String getCouponCnt() {
            return couponCnt;
        }

        public void setCouponCnt(String couponCnt) {
            this.couponCnt = couponCnt;
        }

        public String getExtInfo() {
            return extInfo;
        }

        public void setExtInfo(String extInfo) {
            this.extInfo = extInfo;
        }

        public String getCouponResource() {
            return couponResource;
        }

        public void setCouponResource(String couponResource) {
            this.couponResource = couponResource;
        }
    }


    public class ResultInfo implements Serializable {
        private List<CouponInfo> couponInfo;

        public List<CouponInfo> getCouponInfo() {
            return couponInfo;
        }

        public void setCouponInfo(List<CouponInfo> couponInfo) {
            this.couponInfo = couponInfo;
        }
    }
}
