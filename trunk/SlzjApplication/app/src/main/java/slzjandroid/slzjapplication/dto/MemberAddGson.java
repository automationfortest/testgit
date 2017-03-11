package slzjandroid.slzjapplication.dto;

import com.google.gson.Gson;

/**
 * Created by ASUS on 2016/4/22.
 */
public class MemberAddGson {
    public static String toRequst(MemberAddRequest request){
        Gson gosn = new Gson();
        String data = gosn.toJson(request, MemberAddRequest.class);
        return data;
    }
}
