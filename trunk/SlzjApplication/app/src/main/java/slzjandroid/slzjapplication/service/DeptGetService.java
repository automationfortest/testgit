package slzjandroid.slzjapplication.service;

import java.util.Map;


import retrofit.http.GET;

import retrofit.http.QueryMap;
import rx.Observable;
import slzjandroid.slzjapplication.dto.DeptAddResponse;
import slzjandroid.slzjapplication.dto.DeptGetResponse;

/**
 * Created by ASUS on 2016/4/17.
 */
public interface DeptGetService {
    @GET("/h5/dept")
    public Observable<DeptAddResponse> getDept(@QueryMap Map<String,String> options);
    @GET("/h5/dept")
    public Observable<DeptGetResponse> deptGet(@QueryMap Map<String,String> options);
}
