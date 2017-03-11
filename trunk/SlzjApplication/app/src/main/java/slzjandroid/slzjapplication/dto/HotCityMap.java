package slzjandroid.slzjapplication.dto;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by hdb on 2016/4/9.
 */
public class HotCityMap  implements Serializable{
    private String hot;
    private ArrayList<Hotvalue> hotvalue;

    public String getHot() {
        return hot;
    }

    public void setHot(String hot) {
        this.hot = hot;
    }

    public ArrayList<Hotvalue> getHotvalue() {
        return hotvalue;
    }

    public void setHotvalue(ArrayList<Hotvalue> hotvalue) {
        this.hotvalue = hotvalue;
    }
}
