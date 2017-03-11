package slzjandroid.slzjapplication.service;

import java.util.Map;

import retrofit.http.Body;
import retrofit.http.FieldMap;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Query;
import rx.Observable;
import slzjandroid.slzjapplication.dto.CreateTokenRequest;
import slzjandroid.slzjapplication.dto.ResponseDto;
import slzjandroid.slzjapplication.dto.Token;
import slzjandroid.slzjapplication.dto.TokenRefresh;
import slzjandroid.slzjapplication.dto.TokenRenewalResponse;
import slzjandroid.slzjapplication.dto.ValidateTokenRequest;


public interface TokenService {
    @POST("/h5/token/")
    public Observable<ResponseDto<Token>> createToken(@Body CreateTokenRequest tokenRequest);


    @PUT("/h5/token/")
    public Observable<ResponseDto> validateToken(@Body ValidateTokenRequest validateTokenRequest);


    @FormUrlEncoded
    @POST("/h5/tokenRenew")
    public Observable<TokenRenewalResponse> tokenRenewal(@Query("access_token") String token, @FieldMap Map<String, Object> tokenRenewalRequest);

    @FormUrlEncoded
    @POST("/h5/tokenRefresh")
    Observable<TokenRefresh> tokenRefresh(@Query("access_token") String token, @FieldMap Map<String, String> options);


}