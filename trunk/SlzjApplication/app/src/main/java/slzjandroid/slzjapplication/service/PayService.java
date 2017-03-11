package slzjandroid.slzjapplication.service;


import java.util.Map;

import retrofit.http.FieldMap;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Query;
import retrofit.http.QueryMap;
import rx.Observable;
import slzjandroid.slzjapplication.dto.PayResponse;
import slzjandroid.slzjapplication.dto.RechargeInfoResponse;

/**
 * Created by ASUS on 2016/4/20.
 */
public interface PayService {
    @FormUrlEncoded
    @POST("/app/paymentOrder")
    public Observable<PayResponse> getOrderResult(@Query("access_token") String token, @FieldMap Map<String, String> options);

    @GET("/h5/chargeList")
    public Observable<RechargeInfoResponse> getchargelist(@QueryMap Map<String, String> options);

}
