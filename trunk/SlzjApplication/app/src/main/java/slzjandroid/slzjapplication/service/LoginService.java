package slzjandroid.slzjapplication.service;

import java.util.Map;

import retrofit.http.Field;
import retrofit.http.FieldMap;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;
import rx.Observable;
import slzjandroid.slzjapplication.dto.LoginResponse;

/**
 * 登录服务
 * Created by hdb on 2016/3/14.
 */
public interface LoginService {

    @FormUrlEncoded
    @POST("/h5/login")
    public Observable<LoginResponse> getLoginInfo(@FieldMap Map<String ,Object > infos);
    @FormUrlEncoded
    @POST("/h5/login")
    public Observable<LoginResponse> getLoginInfo(@Field("cellphone") String infos,@Field("smsCode") String smsCode,@Field("timeStamp") String timeStamp);

    @FormUrlEncoded
    @POST("/h5/login")
    public Observable<LoginResponse> getLoginInfos(@Field("cellphone") String cellphone,@Field("smsCode") String smsCode,@Field("deviceID") String deviceID
    ,@Field("timeStamp") String timeStamp);



}
