package slzjandroid.slzjapplication.service;

import java.util.Map;

import retrofit.http.FieldMap;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;
import retrofit.http.Query;
import rx.Observable;
import slzjandroid.slzjapplication.dto.SuggestResult;

/**
 * Created by xuyifei on 16/5/23.
 */
public interface AdviceService {
    @FormUrlEncoded
    @POST("/h5/suggest")
    Observable<SuggestResult> postSuggest(@Query("access_token") String token, @FieldMap Map<String, String> options);
}
