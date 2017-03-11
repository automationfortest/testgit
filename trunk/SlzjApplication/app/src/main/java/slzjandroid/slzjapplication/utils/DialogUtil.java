package slzjandroid.slzjapplication.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import slzjandroid.slzjapplication.R;
import slzjandroid.slzjapplication.customView.LoadingDialog;


/**
 * 通用的Dialog显示工具
 *
 * @title DialogUtil.java
 */
public class DialogUtil {

    private Dialog mDialog;
    private Dialog timerDialog;
    private Dialog travelinDialog;
    private static DialogUtil util = null;

    public static DialogUtil getInstance() {
        if (util == null) {
            util = new DialogUtil();
        }
        return util;
    }

    public void clearDialogs() {
        if (mDialog != null) {
            try {
                if (mDialog.isShowing()) {
                    mDialog.dismiss();
                    mDialog = null;
                }
            } catch (Exception e) {
                mDialog = null;
            }
        }
        if (timerDialog != null) {
            try {
                if (timerDialog.isShowing()) {
                    timerDialog.dismiss();
                    timerDialog = null;
                }
            } catch (Exception e) {
                timerDialog = null;
            }


        }
        if (travelinDialog != null) {
            try {
                if (travelinDialog.isShowing()) {
                    travelinDialog.dismiss();
                    travelinDialog = null;
                }
            } catch (Exception e) {
                travelinDialog = null;
            }


        }

    }

    /**
     * 带有两个按钮的弹窗。确认按钮自定义事件监听。取消按钮为dismiss功能。
     *
     * @param activity
     * @param confirmListener 确认按钮事件监听
     */
    public void popDialog(Activity activity,
                          OnClickListener confirmListener, OnClickListener cancelListener) {
        popDialog(activity, confirmListener,
                cancelListener, true);
    }

    /**
     * @param activity
     * @param confirmListener 确认按钮监听事件
     * @param cancleListener  取消按钮监听
     * @param hasCancleBtn    是否有取消按钮
     */
    public void popDialog(Activity activity,
                          OnClickListener confirmListener, OnClickListener cancleListener,
                          boolean hasCancleBtn) {

        if (activity.isFinishing()) {
            mDialog = null;
            return;
        }

        if (mDialog == null) {
            mDialog = new Dialog(activity, R.style.noTitleDialogWithBack);
            mDialog.setCancelable(false);

            // 获取弹窗布局
            Window window = mDialog.getWindow();
            window.setWindowAnimations(R.style.dialogAnimaFromButtom);
            window.setContentView(R.layout.layout_dialog_ordercancel);

            // 设置宽度
            WindowManager.LayoutParams lp = window.getAttributes();
            window.setGravity(Gravity.CENTER);
            lp.width = (int) activity.getResources().getDimension(
                    R.dimen.dialog_white_width);
            window.setAttributes(lp);

            // 确认按钮
            Button confirmBtn = (Button) mDialog
                    .findViewById(R.id.btn_dialog_left);

//			View line = mDialog.findViewById(R.id.dialog_line);
            // 取消按钮（如果没有传递确认按钮的事件监听，则认为只有一个确认按钮）
            Button cancleBtn = (Button) mDialog
                    .findViewById(R.id.btn_dialog_right);
            if (hasCancleBtn) {
//				line.setVisibility(View.VISIBLE);
                cancleBtn.setVisibility(View.VISIBLE);
                if (cancleListener != null) {
                    // 如果有取消按钮的事件监听
                    cancleBtn.setOnClickListener(cancleListener);
                } else {
                    cancleBtn.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mDialog.dismiss();
                        }
                    });
                }
            }

            // 是否有自定义确认监听
            if (confirmListener != null) {
                confirmBtn.setOnClickListener(confirmListener);

            } else {
                confirmBtn.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mDialog.dismiss();
                    }
                });
            }
        }
        if (mDialog != null) {
            mDialog.show();
        }
    }

    public Dialog getDialog() {
        return mDialog;
    }


	/**
	 * 打电话
	 */
//	public void callPhone(final Context context, final String tel) {
//		DialogUtil.getInstance().clearDialogs();
//		DialogUtil.getInstance().popDialog((Activity) context, "是否拨打 " + tel,
//				"立即拨打", "取消", new OnClickListener() {
//
//					@Override
//					public void onClick(View v) {
//						DialogUtil.getInstance().getDialog().dismiss();
//	//					UIUtils.call((Activity) context, tel.replace("-", ""));
//					}
//				});
//	}

    /**
     * 倒计时提示
     */
    public void popTimingDialog(Activity activity, OnClickListener confirmListener, OnClickListener cancleListener) {
        if (activity.isFinishing()) {
            timerDialog = null;
            return;
        }
        if (timerDialog == null) {
            timerDialog = new Dialog(activity, R.style.noTitleDialogWithBack);
            timerDialog.setCancelable(false);

            // 获取弹窗布局
            Window window = timerDialog.getWindow();
            window.setWindowAnimations(R.style.dialogAnimaFromButtom);
            window.setContentView(R.layout.layout_dialog_timing);

            // 设置宽度
            WindowManager.LayoutParams lp = window.getAttributes();
            window.setGravity(Gravity.CENTER);
            lp.width = (int) activity.getResources().getDimension(
                    R.dimen.dialog_white_width);
            window.setAttributes(lp);

            // 确认按钮
            Button confirmBtn = (Button) timerDialog
                    .findViewById(R.id.btn_dialog_timing_left);

//			View line = mDialog.findViewById(R.id.dialog_line);
            // 取消按钮（如果没有传递确认按钮的事件监听，则认为只有一个确认按钮）
            Button cancleBtn = (Button) timerDialog
                    .findViewById(R.id.btn_dialog_timing_right);
            cancleBtn.setVisibility(View.VISIBLE);
            if (cancleListener != null) {
                // 如果有取消按钮的事件监听
                cancleBtn.setOnClickListener(cancleListener);
            } else {
                cancleBtn.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        timerDialog.dismiss();
                    }
                });
            }
            // 是否有自定义确认监听
            if (confirmListener != null) {
                confirmBtn.setOnClickListener(confirmListener);

            } else {
                confirmBtn.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        timerDialog.dismiss();
                    }
                });
            }
        }
        if (timerDialog != null) {
            timerDialog.show();
        }
    }

    /**
     * 在途
     * @param activity
     * @param confirmListener
     * @param cancleListener
     */
    public void popTravelingDialog(Activity activity,
                                OnClickListener confirmListener, OnClickListener cancleListener
    ) {

        if (activity.isFinishing()) {
            travelinDialog = null;
            return;
        }

        if (travelinDialog == null) {
            travelinDialog = new Dialog(activity, R.style.noTitleDialogWithBack);
            travelinDialog.setCancelable(false);

            // 获取弹窗布局
            Window window = travelinDialog.getWindow();
            window.setWindowAnimations(R.style.dialogAnimaFromButtom);
            window.setContentView(R.layout.layout_dialog_travelin);

            // 设置宽度
            WindowManager.LayoutParams lp = window.getAttributes();
            window.setGravity(Gravity.CENTER);
            lp.width = (int) activity.getResources().getDimension(
                    R.dimen.dialog_white_width);
            window.setAttributes(lp);

            // 确认按钮
            Button confirmBtn = (Button) travelinDialog
                    .findViewById(R.id.btn_dialog_travelin_left);

//			View line = mDialog.findViewById(R.id.dialog_line);
            // 取消按钮（如果没有传递确认按钮的事件监听，则认为只有一个确认按钮）
            Button cancleBtn = (Button) travelinDialog
                    .findViewById(R.id.btn_dialog_travelin_right);
            cancleBtn.setVisibility(View.VISIBLE);
            if (cancleListener != null) {
                // 如果有取消按钮的事件监听
                cancleBtn.setOnClickListener(cancleListener);
            } else {
                cancleBtn.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        travelinDialog.dismiss();
                    }
                });
            }


            // 是否有自定义确认监听
            if (confirmListener != null) {
                confirmBtn.setOnClickListener(confirmListener);

            } else {
                confirmBtn.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        travelinDialog.dismiss();
                    }
                });
            }
        }
        if (travelinDialog != null) {
            travelinDialog.show();
        }
    }

    public Dialog getTimerDialog() {
        return timerDialog;
    }

    public Dialog getTravelinDialog() {
        return travelinDialog;
    }

    public static LoadingDialog getLoadingDialog(Context context, String msg) {
        return new LoadingDialog(context, R.layout.view_tips_loading, msg);
    }



}
