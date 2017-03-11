package slzjandroid.slzjapplication.service;

import retrofit.http.GET;
import retrofit.http.Query;
import rx.Observable;
import slzjandroid.slzjapplication.dto.CityResponse;

/**
 * Created by hdb on 2016/4/2.
 */
public interface CityService {
    @GET("/h5/cityCar")
    public Observable<CityResponse> getCity(@Query("access_token") String token);

}
