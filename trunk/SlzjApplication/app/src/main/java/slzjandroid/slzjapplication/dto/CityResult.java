package slzjandroid.slzjapplication.dto;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by hdb on 2016/4/2.
 */
public class CityResult implements Serializable {
    private ArrayList<Value> value;
    private String letter;
    private ArrayList<Hotvalue> hotvalue;
    private String hot;

    public ArrayList<Value> getValue() {
        return value;
    }

    public void setValue(ArrayList<Value> value) {
        this.value = value;
    }

    public String getLetter() {
        return letter;
    }

    public void setLetter(String letter) {
        this.letter = letter;
    }

    public ArrayList<Hotvalue> getHotvalue() {
        return hotvalue;
    }

    public void setHotvalue(ArrayList<Hotvalue> hotvalue) {
        this.hotvalue = hotvalue;
    }

    public String getHot() {
        return hot;
    }

    public void setHot(String hot) {
        this.hot = hot;
    }
}
