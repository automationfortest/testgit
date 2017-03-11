package slzjandroid.slzjapplication.lang;

 
public class UnauthorizedException extends  RuntimeException {

    public UnauthorizedException() {
    }

    public UnauthorizedException(String detailMessage) {
        super(detailMessage);
    }

    public UnauthorizedException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public UnauthorizedException(Throwable throwable) {
        super(throwable);
    }
}
