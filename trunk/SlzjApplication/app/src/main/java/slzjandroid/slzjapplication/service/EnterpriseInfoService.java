package slzjandroid.slzjapplication.service;

import java.util.Map;

import retrofit.http.FieldMap;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Query;
import rx.Observable;
import slzjandroid.slzjapplication.dto.EnterPriseInfoResponse;
import slzjandroid.slzjapplication.dto.OnlyResponse;
import slzjandroid.slzjapplication.dto.EnterprisesRealnameResponse;

/**
 * Created by ASUS on 2016/4/21.
 */
public interface EnterpriseInfoService {
    @GET("/h5/enterpriseInfo")
    public Observable<EnterPriseInfoResponse> getenterpriseInfo(@Query("access_token") String token);

    //企业名称修改
    @FormUrlEncoded
    @POST("/h5/enterpriseName")
    public Observable<OnlyResponse> changeEnterpriseName(@Query("access_token") String token, @FieldMap Map<String, String> options);


    //实名认证
    @FormUrlEncoded
    @POST("/h5/enterprisesRealname")
    public Observable<EnterprisesRealnameResponse> postEnterprisesRealname(@Query("access_token") String token, @FieldMap Map<String, String> options);

}
