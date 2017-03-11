package slzjandroid.slzjapplication.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * 提示框
 */
public class ToastUtils extends Toast {

    public ToastUtils(Context context) {
        super(context);
    }

    public static void showToast(Context context, String msg) {
        makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

}
