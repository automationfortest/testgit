package slzjandroid.slzjapplication.service;

import retrofit.http.GET;
import retrofit.http.Query;
import rx.Observable;
import slzjandroid.slzjapplication.dto.OrderTemplateGet;

/**
 * Created by hdb on 2016/3/29.
 */
public interface OrderTemplateService {
    @GET("/h5/orderTemplate")
    Observable<OrderTemplateGet> getTempleteInfo(@Query("access_token") String Token);
}
