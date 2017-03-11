package slzjandroid.slzjapplication.lang;



public enum CarType {

    CT1("1"),CT2("2"),CT3("3"),CT4("4");

    private String code;

    CarType(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
