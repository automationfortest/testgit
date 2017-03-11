package slzjandroid.slzjapplication.service;

import java.util.Map;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.FieldMap;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;
import retrofit.http.Query;
import retrofit.http.QueryMap;
import rx.Observable;
import slzjandroid.slzjapplication.dto.CancelOrderRequest;
import slzjandroid.slzjapplication.dto.EstimatePrice;
import slzjandroid.slzjapplication.dto.EstimatePriceRequest;
import slzjandroid.slzjapplication.dto.OrderCancelResponse;
import slzjandroid.slzjapplication.dto.OrderCarResponse;
import slzjandroid.slzjapplication.dto.OrderId;
import slzjandroid.slzjapplication.dto.ResponseDto;


public interface CarOrderService {


    @POST("/h5/estimate/")
    public void getEstimate(@Body EstimatePriceRequest estimatePriceRequest, Callback<ResponseDto<Map<String,EstimatePrice>>> callback);



    @POST("/h5/order/tryCancel/")
    public void tryCancel(@Body CancelOrderRequest cancelOrderRequest, Callback<ResponseDto<String>> callback);


    @POST("/h5/order/forceCancel/")
    public void forceCancel(@Body CancelOrderRequest cancelOrderRequest, Callback<ResponseDto<String>> callback);



    @FormUrlEncoded
    @POST("/h5/ordercar")
    public Observable<OrderCarResponse> createOrder(@Query("access_token") String token,@FieldMap Map<String,String> options);
    //获取订单状态
    @GET("/h5/ordercar")
    public void getOrderStatus(@QueryMap Map<String ,String > optins,Callback<OrderCarResponse> callback);
    //取消订单
    @FormUrlEncoded
    @POST("/h5/cancelordercar")
    public Observable<OrderCancelResponse> cancelOrder(@Query("access_token") String token,@FieldMap Map<String,Object> options);

}