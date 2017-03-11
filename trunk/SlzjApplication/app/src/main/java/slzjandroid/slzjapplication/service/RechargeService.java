package slzjandroid.slzjapplication.service;

import java.util.Map;

import retrofit.http.FieldMap;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;
import retrofit.http.Query;
import rx.Observable;
import slzjandroid.slzjapplication.dto.PayResponse;
import slzjandroid.slzjapplication.dto.WXPayResponse;

/**
 * Created by ggy101600 on 2016/5/31.
 */
public interface RechargeService {

    @FormUrlEncoded
    @POST("/wxpay/wxPayOrder")
    Observable<WXPayResponse> getWXPay(@Query("access_token") String token, @FieldMap Map<String, String> options);
//    @POST("/gateway_pay/wxpay/wxPayOrder")


    @FormUrlEncoded
//    @POST("/gateway_pay/pay/alipayOrder")
    @POST("/app/paymentOrder")
    public Observable<PayResponse> getOrderResult(@Query("access_token") String token, @FieldMap Map<String, String> options);

}
