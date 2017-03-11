package slzjandroid.slzjapplication.customView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import slzjandroid.slzjapplication.R;

/**
 * Created by xuyifei on 16/4/15.
 */
public class NavigationView extends LinearLayout implements View.OnClickListener {

    private ImageView backView;
    private TextView titleView;
    private TextView rightView;
    private ImageView iv_right_bg;

    private ClickCallback callback;

    public NavigationView(Context context, AttributeSet attrs) {
        super(context, attrs);
        View view = LayoutInflater.from(context).inflate(R.layout.common_header_layout, this, true);
        backView = (ImageView) view.findViewById(R.id.iv_back);
        backView.setOnClickListener(this);
        titleView = (TextView) view.findViewById(R.id.tv_title);

        rightView = (TextView) view.findViewById(R.id.tv_right_title);
        iv_right_bg = (ImageView) view.findViewById(R.id.iv_right_bg);

        rightView.setOnClickListener(this);
        iv_right_bg.setOnClickListener(this);
    }

    /**
     * 获取返回按钮
     *
     * @return
     */
    public ImageView getBackView() {
        return backView;
    }

    /**
     * 获取标题控件
     *
     * @return
     */
    public TextView getTitleView() {
        return titleView;
    }

    /**
     * 设置标题
     *
     * @param title
     */
    public void setTitle(String title) {
        getTitleView().setText(title);
    }

    /**
     * 获取右侧按钮,默认不显示
     *
     * @return
     */
    public TextView getRightView() {
        return rightView;
    }


    public void setRightTile(String title) {
        iv_right_bg.setVisibility(View.GONE);
        getRightView().setText(title);
    }

    public void setRightViewIsShow(boolean flag) {
        if (flag) {
            rightView.setVisibility(View.VISIBLE);
        } else {
            rightView.setVisibility(View.GONE);
        }
    }


    /**
     * 设置按钮点击回调接口
     *
     * @param callback
     */
    public void setClickCallback(ClickCallback callback) {
        this.callback = callback;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                callback.onBackClick();
                break;
            case R.id.tv_right_title:
                callback.onRightClick();
                break;
            case R.id.iv_right_bg:
                callback.onRightClick();
                break;
        }

    }


    public interface ClickCallback {
        /**
         * 点击返回按钮回调
         */
        void onBackClick();

        void onRightClick();
    }
}


