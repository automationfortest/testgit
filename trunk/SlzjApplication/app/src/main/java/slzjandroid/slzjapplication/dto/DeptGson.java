package slzjandroid.slzjapplication.dto;

import com.google.gson.Gson;

/**
 * Created by ASUS on 2016/4/18.
 */
public class DeptGson {
    public static String toRequst(DeptInfoBean request){
        Gson gosn = new Gson();
        String data = gosn.toJson(request, DeptInfoBean.class);
        return data;
    }
}
