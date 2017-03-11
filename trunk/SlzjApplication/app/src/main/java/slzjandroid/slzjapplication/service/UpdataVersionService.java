package slzjandroid.slzjapplication.service;

import java.util.Map;

import retrofit.http.FieldMap;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;
import retrofit.http.Query;
import rx.Observable;
import slzjandroid.slzjapplication.dto.VersionResponse;

/**
 * Created by xuyifei on 16/6/3.
 */
public interface UpdataVersionService {

    @FormUrlEncoded
    @POST("/app/version")
    public Observable<VersionResponse> getVersion(@Query("access_token") String token, @FieldMap Map<String, String> parameters);


}
