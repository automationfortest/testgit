package slzjandroid.slzjapplication.dto;


public
class AddressRequest implements java.io.Serializable {
    private String callerPhone;
    private String cityMapId;
    private String city;
    private String input;

    public String getCallerPhone() {
        return callerPhone;
    }

    public void setCallerPhone(String callerPhone) {
        this.callerPhone = callerPhone;
    }

    public String getCityMapId() {
        return cityMapId;
    }

    public void setCityMapId(String cityMapId) {
        this.cityMapId = cityMapId;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }
}
