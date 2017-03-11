package slzjandroid.slzjapplication.service;

import retrofit.RequestInterceptor;

public class  RestRequestInterceptor implements RequestInterceptor {
    @Override
    public void intercept(retrofit.RequestInterceptor.RequestFacade request) {
//        LoginUser loginUser=AppContext.getInstance().getLoginUser();
        request.addHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
//        if(loginUser!=null){
//            String accessToken=loginUser.getAccessToken();
//            request.addHeader("login_name", loginUser.getLoginName());
//            request.addHeader("access_token", accessToken);
//        }
    }
}
