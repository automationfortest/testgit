package slzjandroid.slzjapplication.dto;

import java.io.Serializable;

/**
 * Created by xuyifei on 16/6/3.
 */
public class PicDomin implements Serializable {

    private String tag;
    private String picUri;
    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    public String getPicUri() {
        return picUri;
    }

    public void setPicUri(String picUri) {
        this.picUri = picUri;
    }


    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

}
