package slzjandroid.slzjapplication.customView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;

/**
 * Created by xuyifei on 16/5/11.
 */
public class YesOrNoDialog extends BaseDialogFragment {
    public static final String TITLE = "title";
    public static final String MESSAGE = "message";
    public static final String STRPositive = "strPositive";
    public static final String STRNegative = "strNegative";

    public String strPositive = "确定";
    public String strNegative = "取消";

    String title = "提示";
    String message = "确定吗";

    private OnClick onClick;

    public OnClick getOnClick() {
        return onClick;
    }

    public void setOnClick(OnClick onClick) {
        this.onClick = onClick;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if (null != getArguments()) {
            title = getArguments().getString(TITLE, "提示");
            message = getArguments().getString(MESSAGE, "确定吗");
            strPositive = getArguments().getString(STRPositive, "确认");
            // strNegative = getArguments().getString(STRNegative, "取消");

        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setCancelable(true);

        builder.setPositiveButton(strPositive, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        if (onClick != null) {
                            onClick.onPositive();
                        }
                    }
                });
//                .setNegativeButton(strNegative, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                        if (onClick != null) {
//                            onClick.onNegative();
//                        }
//                    }
//                });
        return builder.create();
    }

    public interface OnClick {
        void onPositive();

        // void onNegative();
    }
}
