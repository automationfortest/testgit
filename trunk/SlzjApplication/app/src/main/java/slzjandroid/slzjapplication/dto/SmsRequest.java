package slzjandroid.slzjapplication.dto;

import java.io.Serializable;

/**
 * Created by hdb on 2016/3/11.
 */
public class SmsRequest implements Serializable{
    private String cellPhone;

    public String getCellPhone() {
        return cellPhone;
    }

    public void setCellPhone(String cellPhone) {
        this.cellPhone = cellPhone;
    }
}
