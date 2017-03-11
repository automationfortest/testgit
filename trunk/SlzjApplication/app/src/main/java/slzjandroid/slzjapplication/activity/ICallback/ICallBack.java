package slzjandroid.slzjapplication.activity.ICallback;

/**
 * Created by xuyifei on 16/4/18.
 */
public interface ICallBack<T> {

    void success(T t);

    void fail(String info);

    void error(Throwable throwable);

}
