package slzjandroid.slzjapplication.weights;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import slzjandroid.slzjapplication.R;

public class HeaderLayout extends LinearLayout {
	private LayoutInflater mInflater;
	private View mHeader;
	private LinearLayout mLayoutLeftContainer;
	//private LinearLayout mLayoutMiddleContainer;
	private LinearLayout mLayoutRightContainer;
	private HandyTextView mHtvSubTitle;
	private LinearLayout mLayoutRightImageButtonLayout;
	private ImageButton mRightImageButton;
	private onRightImageButtonClickListener mRightImageButtonClickListener;

	private LinearLayout mLayoutLeftImageButtonLayout;
	private ImageButton mLeftImageButton;
	private onLeftImageButtonClickListener mLeftImageButtonClickListener;

	public enum HeaderStyle {// 头部整体样式
		DEFAULT_TITLE, TITLE_LIFT_IMAGEBUTTON, TITLE_RIGHT_IMAGEBUTTON, TITLE_DOUBLE_IMAGEBUTTON;
	}

	public HeaderLayout(Context context) {
		super(context);
		init(context);
	}

	public HeaderLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public void init(Context context) {
		mInflater = LayoutInflater.from(context);
		mHeader = mInflater.inflate(R.layout.common_headerbar, null);
		addView(mHeader);
		initViews();
	}
	public void initViews() {
		mLayoutLeftContainer = (LinearLayout) findViewByHeaderId(R.id.header_layout_leftview_container);
		//mLayoutMiddleContainer = (LinearLayout) findViewByHeaderId(R.id.header_layout_middleview_container);中间部分添加搜索或者其他按钮时可打开
		mLayoutRightContainer = (LinearLayout) findViewByHeaderId(R.id.header_layout_rightview_container);
		mHtvSubTitle = (HandyTextView) findViewByHeaderId(R.id.header_htv_subtitle);

	}

	public View findViewByHeaderId(int id) {
		return mHeader.findViewById(id);
	}

	public void init(HeaderStyle hStyle) {
		switch (hStyle) {
			case DEFAULT_TITLE:
				defaultTitle();
				break;

			case TITLE_LIFT_IMAGEBUTTON:
				defaultTitle();
				titleLeftImageButton();
				break;

			case TITLE_RIGHT_IMAGEBUTTON:
				defaultTitle();
				titleRightImageButton();
				break;

			case TITLE_DOUBLE_IMAGEBUTTON:
				defaultTitle();
				titleLeftImageButton();
				titleRightImageButton();
				break;
		}
	}

	// 默认文字标题
	private void defaultTitle() {
		mLayoutLeftContainer.removeAllViews();
		mLayoutRightContainer.removeAllViews();
	}

	// 左侧自定义按钮
	private void titleLeftImageButton() {
		View mleftImageButtonView = mInflater.inflate(R.layout.include_header_imagebutton, null);
		mLayoutLeftContainer.addView(mleftImageButtonView);
		mLayoutLeftImageButtonLayout = (LinearLayout) mleftImageButtonView.findViewById(R.id.header_layout_imagebuttonlayout);
		mLeftImageButton = (ImageButton) mleftImageButtonView.findViewById(R.id.header_ib_imagebutton);
		mLayoutLeftImageButtonLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (mLeftImageButtonClickListener != null) {
					mLeftImageButtonClickListener.onClick();
				}
			}
		});
	}

	// 右侧自定义按钮
	private void titleRightImageButton() {
		View mRightImageButtonView = mInflater.inflate(R.layout.include_header_right_imagebutton, null);
		mLayoutRightContainer.addView(mRightImageButtonView);
		mLayoutRightImageButtonLayout = (LinearLayout) mRightImageButtonView.findViewById(R.id.header_layout_imagebuttonlayout);
		mRightImageButton = (ImageButton) mRightImageButtonView.findViewById(R.id.header_ib_imagebutton);
		mLayoutRightImageButtonLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (mRightImageButtonClickListener != null) {
					mRightImageButtonClickListener.onClick();
				}
			}
		});
	}

	public void setDefaultTitle(CharSequence title) {
		if (title != null) {
			mHtvSubTitle.setText(title);

		} else {
			mHtvSubTitle.setVisibility(View.GONE);
		}
	}
	public void setDefaultTitle(CharSequence title,int colorID,int resid) {
		if (title != null) {
			mHtvSubTitle.setText(title);
			mHtvSubTitle.setTextColor(colorID);
			mHeader.setBackgroundResource(resid);
		} else {
			mHtvSubTitle.setVisibility(View.GONE);
		}
	}

	//文字标题
	public void setTitleAndRightImageButton(CharSequence title, int id,
											onRightImageButtonClickListener onRightImageButtonClickListener) {
		setDefaultTitle(title);
		if (mRightImageButton != null && id > 0) {
			mRightImageButton.setImageResource(id);
			setOnRightImageButtonClickListener(onRightImageButtonClickListener);
		}
	}
	//图片标题
	public void setTitleAndRightImageButton(int TitleId, int id,
											onRightImageButtonClickListener onRightImageButtonClickListener) {

		setImageTitle(TitleId);

		if (mRightImageButton != null && id > 0) {
			mRightImageButton.setImageResource(id);
			setOnRightImageButtonClickListener(onRightImageButtonClickListener);
		}
	}

	public void setTitleAndLeftImageButton(CharSequence title, int id,
										   onLeftImageButtonClickListener listener) {
		setDefaultTitle(title);
		if (mLeftImageButton != null && id > 0) {
			mLeftImageButton.setImageResource(id);
			setOnLeftImageButtonClickListener(listener);
		}
	}

	public void setOnRightImageButtonClickListener(
			onRightImageButtonClickListener listener) {
		mRightImageButtonClickListener = listener;
	}

	public interface onRightImageButtonClickListener {
		void onClick();
	}

	public void setOnLeftImageButtonClickListener(
			onLeftImageButtonClickListener listener) {
		mLeftImageButtonClickListener = listener;
	}

	public interface onLeftImageButtonClickListener {
		void onClick();
	}

	void setImageTitle(int id){
		mHtvSubTitle.setBackgroundResource(id);
		mHtvSubTitle.setText("");
	}
}
