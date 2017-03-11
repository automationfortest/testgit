package slzjandroid.slzjapplication.dto;

public class CreateTokenRequest implements java.io.Serializable{

     private String appSecret;
     private String loginCode;
     private String loginName;
     private long timestamp;

     public String getAppSecret() {
          return appSecret;
     }

     public void setAppSecret(String appSecret) {
          this.appSecret = appSecret;
     }

     public String getLoginCode() {
          return loginCode;
     }

     public void setLoginCode(String loginCode) {
          this.loginCode = loginCode;
     }

     public String getLoginName() {
          return loginName;
     }

     public void setLoginName(String loginName) {
          this.loginName = loginName;
     }

     public long getTimestamp() {
          return timestamp;
     }

     public void setTimestamp(long timestamp) {
          this.timestamp = timestamp;
     }
}
