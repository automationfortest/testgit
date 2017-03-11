package slzjandroid.slzjapplication.dto;


import android.content.ContentValues;
import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import slzjandroid.slzjapplication.context.AppContext;
import slzjandroid.slzjapplication.db.UserDBManage;
import slzjandroid.slzjapplication.helper.SPLoginUser;

/**
 * user domin
 */
public class LoginUser implements java.io.Serializable {
    private String id = "1";
    private String loginName;
    private String cellphone;
    private String accessToken;
    private String userType;
    private String balance;
    private EnterpriseInfo enterpriseInfo;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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


    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }


    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getCellphone() {
        return cellphone;
    }

    public void setCellphone(String cellphone) {
        this.cellphone = cellphone;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    private static Context context;

    /**
     * 获取LoginUser对象
     *
     * @param phoneNum
     * @param loginResponse
     * @return
     */
    public static LoginUser getLoginUser(String phoneNum, LoginResponse loginResponse) {
        LoginUser loginUser = new LoginUser();
        TokenAndEnterpriseInfo result = loginResponse.getResult();
        loginUser.setCellphone(phoneNum);
        loginUser.setAccessToken(result.getUsrToken().getAccessToken());
        loginUser.setLoginName(result.getUsrName());
        loginUser.setUserType(result.getUsrType());
        loginUser.setBalance(result.getBalance());
        EnterpriseInfo enterpriseInfo = result.getEnterpriseInfo();
        loginUser.setEnterpriseInfo(getEnterpriseInfo(enterpriseInfo));
        return loginUser;
    }

    public static LoginUser getLoginUser(String phoneNum, TokenRenewalResponse tokenRenewalResponse) {
        LoginUser loginUser = new LoginUser();

        TokenResult result = tokenRenewalResponse.getResult();
        loginUser.setCellphone(phoneNum);
        loginUser.setAccessToken(result.getUsrToken().getAccessToken());
        loginUser.setLoginName(result.getUsrName());
        loginUser.setUserType(result.getUsrType());
        loginUser.setBalance(result.getBalance());
        EnterpriseInfo enterpriseInfo = result.getEnterpriseInfo();
        loginUser.setEnterpriseInfo(getEnterpriseInfo(enterpriseInfo));

        return loginUser;
    }


    public static EnterpriseInfo getEnterpriseInfo(EnterpriseInfo enterpriseInfo) {
        if (null != enterpriseInfo) {
            EnterpriseInfo data = new EnterpriseInfo();
            data.setEnterpriseName(enterpriseInfo.getEnterpriseName());
            data.setEmail(enterpriseInfo.getEmail());
            data.setEnterpriseIdx(enterpriseInfo.getEnterpriseIdx());
            data.setCreditBalance(enterpriseInfo.getCreditBalance());
            data.setDebitBalance(enterpriseInfo.getDebitBalance());
            data.setEnterpriseAuthFlag(enterpriseInfo.getEnterpriseAuthFlag());
            data.setAuthDate(enterpriseInfo.getAuthDate());
            data.setResaon(enterpriseInfo.getResaon());
            return data;
        }
        return null;
    }

    public static EnterpriseInfo enterpriseInfoFromJson(String json) {
        Gson gson = new Gson();
        EnterpriseInfo enterpriseInfo = gson.fromJson(json, new TypeToken<EnterpriseInfo>() {
        }.getType());
        return enterpriseInfo;
    }


    public ContentValues toContentValues() {
        ContentValues values = new ContentValues();
        Gson gson = new Gson();
        values.put(UserDBManage.UserColumns.userId, id);
        values.put(UserDBManage.UserColumns.loginName, loginName);
        values.put(UserDBManage.UserColumns.cellphone, cellphone);
        values.put(UserDBManage.UserColumns.accessToken, accessToken);
        values.put(UserDBManage.UserColumns.userType, userType);
        values.put(UserDBManage.UserColumns.balance, balance);
        values.put(UserDBManage.UserColumns.enterpriseInfo, gson.toJson(enterpriseInfo));
        return values;
    }


    /**
     * 判断是否是新老客户
     *
     * @param flag 0 为新客户，相反则是老客户
     * @return
     */
    public static boolean getNewUserFlag(String flag) {
        if (flag.equals("0")) {
            return true;
        }
        return false;
    }


    public static LoginUser getUser() {
        if (null == context) {
            context = AppContext.getInstance();
        }
        String token = SPLoginUser.getToken(context);
        return getUserObject(token, context);
    }

    public static EnterpriseInfo getEnterpriseInfoforUser(LoginUser user) {
        return user.getEnterpriseInfo();
    }

    /**
     * 获取user对象
     *
     * @return
     */
    public static LoginUser getUserObject(String token, Context context) {
        UserDBManage manager = new UserDBManage(context);
        return manager.findUser("1");
    }

    public static void saveUserToDB(LoginUser user, Context context) {
        UserDBManage manage = new UserDBManage(context);
        if (manage.userIsExist(user.getId())) {
            manage.updateUser(user);
        } else {
            manage.insertUser(user);
        }
    }

    public static void clearDB(Context context) {
        UserDBManage manage = new UserDBManage(context);
        manage.clearDB();
    }
}
