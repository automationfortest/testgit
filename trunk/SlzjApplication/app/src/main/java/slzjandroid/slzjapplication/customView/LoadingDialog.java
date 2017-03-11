package slzjandroid.slzjapplication.customView;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;

import slzjandroid.slzjapplication.R;

/**
 * 加载中Dialog
 *
 * @author xuyifei
 */
public class LoadingDialog extends AlertDialog {

    private String message;
    private TextView tips_loading_msg;
    private int layoutResId;

    /**
     * 构造方法
     *
     * @param context     上下文
     * @param layoutResId 要传入的dialog布局文件的id
     */
    public LoadingDialog(Context context, int layoutResId, String msg) {
        super(context);
        this.layoutResId = layoutResId;
        this.message = msg;
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(layoutResId);
        tips_loading_msg = (TextView) findViewById(R.id.tips_loading_msg);
        if (!TextUtils.isEmpty(message)) {
            tips_loading_msg.setText(message);
        }

    }

}
