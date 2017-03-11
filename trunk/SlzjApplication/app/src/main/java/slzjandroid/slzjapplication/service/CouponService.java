package slzjandroid.slzjapplication.service;

import java.util.Map;

import retrofit.http.GET;
import retrofit.http.Query;
import retrofit.http.QueryMap;
import rx.Observable;
import slzjandroid.slzjapplication.dto.CouponRequest;

/**
 * Created by xuyifei on 16/4/20.
 */
public interface CouponService {
    @GET("/h5/couponList")
    public Observable<CouponRequest> getCouponService(@Query("access_token") String token, @QueryMap Map<String, String> options);
}
