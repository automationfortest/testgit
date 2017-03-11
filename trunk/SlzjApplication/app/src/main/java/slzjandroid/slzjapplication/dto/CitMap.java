package slzjandroid.slzjapplication.dto;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by hdb on 2016/4/9.
 */
public class CitMap implements Serializable {
    private String letter;
    private ArrayList<Value> value;

    public String getLetter() {
        return letter;
    }

    public void setLetter(String letter) {
        this.letter = letter;
    }

    public ArrayList<Value> getValue() {
        return value;
    }

    public void setValue(ArrayList<Value> value) {
        this.value = value;
    }
}
