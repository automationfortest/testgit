package slzjandroid.slzjapplication.service;

import retrofit.ErrorHandler;
import retrofit.RetrofitError;
import retrofit.client.Response;
import slzjandroid.slzjapplication.lang.UnauthorizedException;

public class RestErrorHandler implements ErrorHandler {
    @Override public Throwable handleError(RetrofitError cause) {
        Response r = cause.getResponse();
        if (r != null && r.getStatus() == 401) {
            return new UnauthorizedException(cause);
        }
        return cause;
    }
}