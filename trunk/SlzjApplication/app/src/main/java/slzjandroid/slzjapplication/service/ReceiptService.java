package slzjandroid.slzjapplication.service;

import java.util.Map;

import retrofit.http.FieldMap;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Query;
import rx.Observable;
import slzjandroid.slzjapplication.dto.RegisterResponse;
import slzjandroid.slzjapplication.dto.TicketResponse;
import slzjandroid.slzjapplication.dto.TicketTemplateResponse;

/**
 * Created by xuyifei on 16/5/9.
 */
public interface ReceiptService {

    @FormUrlEncoded
    @POST("/h5/ticket")
    Observable<RegisterResponse> postReceipt(@Query("access_token") String token, @FieldMap Map<String, Object> options);


    @GET("/h5/ticket")
    Observable<TicketResponse> getReceiptTicket(@Query("access_token") String token);


    @GET("/h5/ticketTemplate")
    Observable<TicketTemplateResponse> getTicketTemplate(@Query("access_token") String token);


}
