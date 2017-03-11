package slzjandroid.slzjapplication.dto;

import com.google.gson.Gson;

/**
 * Created by ASUS on 2016/4/18.
 */
public class EditMemberGson {
    public static String toRequst(EditMemberBean request){
        Gson gosn = new Gson();
        String data = gosn.toJson(request, EditMemberBean.class);
        return data;
    }
}
