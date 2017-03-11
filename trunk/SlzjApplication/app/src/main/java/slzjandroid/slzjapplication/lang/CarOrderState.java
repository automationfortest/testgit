package slzjandroid.slzjapplication.lang;


public enum CarOrderState {
    WAIT_ANSWER_300(300,"等待应答"),
    TIME_OUT_311(311,"订单超时"),//客服取消
    WAIT_DRIVER_400(400,"等待接驾"),
    DRIVER_REAY_410(410,"司机已到达"),
    TRAVEL_IN_500(500,"行程中"),
    TRAVEL_END_600(600,"行程结束"),
    TRAVEL_ERROR_610(610,"行程异常结束"),
    PAID_700(700,"已支付");
    private Integer code;
    private String text;

    CarOrderState(Integer code, String text) {
        this.code=code;
        this.text = text;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }


    public static CarOrderState get(Integer code){
        if(WAIT_ANSWER_300.getCode().equals(code)){
            return WAIT_ANSWER_300;
        }else  if(TIME_OUT_311.getCode().equals(code)){
            return TIME_OUT_311;
        }else  if(WAIT_DRIVER_400.getCode().equals(code)){
            return WAIT_DRIVER_400;
        }else  if(DRIVER_REAY_410.getCode().equals(code)){
            return DRIVER_REAY_410;
        }else  if(TRAVEL_IN_500.getCode().equals(code)){
            return TRAVEL_IN_500;
        }else  if(TRAVEL_END_600.getCode().equals(code)){
            return TRAVEL_END_600;
        }else  if(TRAVEL_ERROR_610.getCode().equals(code)){
            return TRAVEL_ERROR_610;
        }else  if(PAID_700.getCode().equals(code)){
            return PAID_700;
        }else{
            return null;
        }
    }

}
