package slzjandroid.slzjapplication.service;

import java.util.List;
import java.util.Map;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.QueryMap;
import rx.Observable;
import slzjandroid.slzjapplication.dto.AddressRequest;
import slzjandroid.slzjapplication.dto.AdressResponse;
import slzjandroid.slzjapplication.dto.ResponseDto;


public interface AddressService {

    @POST("/h5/address/")
    public void getAddresses(@Body AddressRequest addressRequest, Callback<ResponseDto<AddressRequest>> callback);


    @GET("/h5/adress")
    public Observable<AdressResponse> getAddressList(@QueryMap  Map<String, Object> options);
}