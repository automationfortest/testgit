package slzjandroid.slzjapplication.dto;

import java.util.ArrayList;

/**
 * Created by ASUS on 2016/4/22.
 */
public class MemberAddRequest {
    private ArrayList<MemberAddBean> clientUserInfoList;

    public ArrayList<MemberAddBean> getClientUserInfoList() {
        return clientUserInfoList;
    }

    public void setClientUserInfoList(ArrayList<MemberAddBean> clientUserInfoList) {
        this.clientUserInfoList = clientUserInfoList;
    }
}
