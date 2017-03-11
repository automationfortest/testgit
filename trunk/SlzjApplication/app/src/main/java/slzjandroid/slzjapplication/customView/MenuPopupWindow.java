package slzjandroid.slzjapplication.customView;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;

import slzjandroid.slzjapplication.R;

/**
 * Created by liuguanglin on 15/11/19.
 */
public class MenuPopupWindow extends PopupWindow {
    public MenuPopupWindow(Activity activity, View view, View parent){
        super(activity);
        setContentView(view);
        setFocusable(true);
        setOutsideTouchable(true);
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        setBackgroundDrawable(dw);
        setAnimationStyle(R.style.bottom_menu_anim);
        setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        showAtLocation(parent, Gravity.BOTTOM, 0, 0);
    }
}

