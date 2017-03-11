package slzjandroid.slzjapplication.dto;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by hdb on 2016/4/11.
 */
public class RegisterRequest implements Serializable {

    public static Map<String, String> registerInfo(RegisterObj registerObj) {
        Map<String, String> map = new HashMap<>();
        map.put("contactsName", registerObj.getUserName());
        map.put("enterpriseName", registerObj.getEnterpriseName());
        map.put("enterpriseEmail", registerObj.getEnterpriseEmail());
        map.put("cityName", registerObj.getCityName());
        map.put("inviteCode", registerObj.getInviteCode());
        return map;
    }


    public class RegisterObj implements Serializable {

        private String userName;
        private String enterpriseName;
        private String enterpriseEmail;
        private String cityName;
        private String inviteCode;

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getEnterpriseName() {
            return enterpriseName;
        }

        public void setEnterpriseName(String enterpriseName) {
            this.enterpriseName = enterpriseName;
        }

        public String getEnterpriseEmail() {
            return enterpriseEmail;
        }

        public void setEnterpriseEmail(String enterpriseEmail) {
            this.enterpriseEmail = enterpriseEmail;
        }

        public String getCityName() {
            return cityName;
        }

        public void setCityName(String cityName) {
            this.cityName = cityName;
        }

        public String getInviteCode() {
            return inviteCode;
        }

        public void setInviteCode(String inviteCode) {
            this.inviteCode = inviteCode;
        }
    }

}
