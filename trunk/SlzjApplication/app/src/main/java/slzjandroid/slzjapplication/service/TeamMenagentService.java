package slzjandroid.slzjapplication.service;

import java.util.Map;

import retrofit.http.FieldMap;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Query;
import retrofit.http.QueryMap;
import rx.Observable;
import slzjandroid.slzjapplication.dto.ClientInfoResponse;
import slzjandroid.slzjapplication.dto.OnlyResponse;

/**
 * Created by hdb on 2016/4/11.
 */
public interface TeamMenagentService {
  //查看成员列表
  @GET("/h5/client")
    public Observable<ClientInfoResponse> getMenberList(@QueryMap  Map<String,String> options);
//删除成员
  @FormUrlEncoded
  @POST("/h5/removeClient")
  public Observable<OnlyResponse>  getDeptDelResult(@Query("access_token") String token,@FieldMap Map<String,String> options);
  //增加成员
  @FormUrlEncoded
  @POST("/h5/client")
  public Observable<OnlyResponse> getAddResult(@Query("access_token") String token,@FieldMap Map<String,String> options);

  @FormUrlEncoded
  @POST("/h5/editClient")
  public Observable<OnlyResponse> editMember(@Query("access_token") String token,@FieldMap Map<String ,String> options);

}
