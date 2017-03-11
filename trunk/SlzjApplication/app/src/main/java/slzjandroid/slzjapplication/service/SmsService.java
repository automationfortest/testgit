package slzjandroid.slzjapplication.service;

import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;
import rx.Observable;
import slzjandroid.slzjapplication.dto.SmsResponse;

/**
 * 短信发送请求接口
 * Created by hdb on 2016/3/14.
 */
public interface SmsService {
//    @POST("/SmsCode")
//    public Observable<SmsResponse> sendSms(@Body SmsRequest smsRequest);
@FormUrlEncoded
@POST("/h5/SmsCode")
public Observable<SmsResponse> sendSms(@Field("cellPhone") String cellphone);

}
