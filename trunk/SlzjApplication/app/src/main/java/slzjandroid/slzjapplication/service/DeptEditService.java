package slzjandroid.slzjapplication.service;


import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;
import retrofit.http.Query;
import rx.Observable;
import slzjandroid.slzjapplication.dto.DeptEditResponse;

/**
 * Created by ASUS on 2016/4/19.
 */
public interface DeptEditService {
    @FormUrlEncoded
    @POST("/h5/dept")
    public Observable<DeptEditResponse> getDeptEditRes(@Query("access_token") String token, @Field("deptInfoList") String dept);
}