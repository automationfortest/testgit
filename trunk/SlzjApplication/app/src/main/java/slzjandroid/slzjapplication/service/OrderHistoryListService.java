package slzjandroid.slzjapplication.service;

import java.util.Map;

import retrofit.http.GET;
import retrofit.http.QueryMap;
import rx.Observable;
import slzjandroid.slzjapplication.dto.OrderHistoryResponse;

/**
 * Created by hdb on 2016/4/6.
 */
public interface OrderHistoryListService {
    @GET("/h5/orderHistory")
    public Observable<OrderHistoryResponse> getOrderList(@QueryMap Map<String,String> options);

//    @GET("/orderDetail")
//    public Observable getOrderDetail();

}
