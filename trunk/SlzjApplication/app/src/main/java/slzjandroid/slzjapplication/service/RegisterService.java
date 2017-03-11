package slzjandroid.slzjapplication.service;

import java.util.Map;

import retrofit.http.FieldMap;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Query;
import retrofit.http.QueryMap;
import rx.Observable;
import slzjandroid.slzjapplication.dto.RegisterResponse;

/**
 * Created by hdb on 2016/4/11.
 */
public interface RegisterService {
    @FormUrlEncoded
    @POST("/h5/enterprises")
    public Observable<RegisterResponse> register(@Query("access_token") String token,@FieldMap Map<String,String> options);


}
