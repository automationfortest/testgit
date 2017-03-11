package slzjandroid.slzjapplication.dto;


public class ResponseDto<T>  implements java.io.Serializable{
    public static final String SUCCESS_CODE="0";
    public static final String ERROR_CODE="-1";
    private String code;
    private String msg;
    private T result;

    public static String getSuccessCode() {
        return SUCCESS_CODE;
    }

    public static String getErrorCode() {
        return ERROR_CODE;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }
}
