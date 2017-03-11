package slzjandroid.slzjapplication.dto;

import java.io.Serializable;

/**
 * Created by xuyifei on 16/6/3.
 */
public class VersionResponse implements Serializable {

    private String status;
    private String message;
    private Result result;

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

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public class Result implements Serializable {
        private String versionCode;
        private String versionName;
        private String subimtTime;
        private String url;
        private String description;
        private String isFlag;

        public String getIsFlag() {
            return isFlag;
        }

        public void setIsFlag(String isFlag) {
            this.isFlag = isFlag;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getVersionCode() {
            return versionCode;
        }

        public void setVersionCode(String versionCode) {
            this.versionCode = versionCode;
        }

        public String getVersionName() {
            return versionName;
        }

        public void setVersionName(String versionName) {
            this.versionName = versionName;
        }

        public String getSubimtTime() {
            return subimtTime;
        }

        public void setSubimtTime(String subimtTime) {
            this.subimtTime = subimtTime;
        }
    }


}
