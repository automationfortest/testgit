package slzjandroid.slzjapplication.service;

import java.util.Map;

import retrofit.http.FieldMap;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;
import retrofit.http.Query;
import rx.Observable;
import slzjandroid.slzjapplication.dto.BudGetResponse;

/**
 * Created by hdb on 2016/3/30.
 */
public interface BudGetService {
    @FormUrlEncoded
    @POST("/h5/price")
    public Observable<BudGetResponse> getBug(@Query("access_token") String token,@FieldMap Map<String,String > options);
}
