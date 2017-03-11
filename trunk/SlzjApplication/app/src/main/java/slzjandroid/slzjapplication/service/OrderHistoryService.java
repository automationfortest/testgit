package slzjandroid.slzjapplication.service;

import java.util.Map;

import retrofit.http.GET;
import retrofit.http.QueryMap;
import rx.Observable;
import slzjandroid.slzjapplication.dto.OrderDetailResponse;
import slzjandroid.slzjapplication.dto.OrderHistoryResponse;

/**
 * 订单查询
 * Created by hdb on 2016/4/3.
 */
public interface OrderHistoryService {
    /**
     * 订单历史列表
     * @param options
     * @return
     */
    @GET("/h5/orderHistory")
    public Observable<OrderHistoryResponse> getOrderList(@QueryMap Map<String,String> options);

    /**
     * 订单详情
     * @param options
     * @return
     */
    @GET("/h5/orderDetail")
    public Observable<OrderDetailResponse> getOrderDetail(@QueryMap Map<String,String> options);
}
