package slzjandroid.slzjapplication.dto;

import com.google.gson.Gson;

/**
 * Created by ASUS on 2016/4/19.
 */
public class MemberDelGson {
    public static String toRequst(MemberDelBean request){
        Gson gosn = new Gson();
        String data = gosn.toJson(request, MemberDelBean.class);
        return data;
    }

}
