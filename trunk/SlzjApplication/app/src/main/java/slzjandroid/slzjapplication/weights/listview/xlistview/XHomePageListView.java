package slzjandroid.slzjapplication.weights.listview.xlistview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.animation.DecelerateInterpolator;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Scroller;

import slzjandroid.slzjapplication.R;

public class XHomePageListView extends ListView implements OnScrollListener {

	//	private boolean scrollFlag = false;// 标记是否滑动
	private View mUpdateStateView;// 需要改变状态的view

	protected float mLastY = -1; // save event y
	protected Scroller mScroller; // used for scroll back
	protected OnScrollListener mScrollListener; // user's scroll listener

	// the interface to trigger refresh and load more.

	protected IXListViewLoadMore mLoadMore;
	protected IXListViewRefreshListener mOnRefresh;

	// -- header view
	protected XListViewHeader mHeaderView;
	// header view content, use it to calculate the Header's height. And hide it
	// when disable pull refresh.
	protected RelativeLayout mHeaderViewContent;
	protected int mHeaderViewHeight; // header view's height
	protected boolean mEnablePullRefresh = true;
	protected boolean mPullRefreshing = false; // is refreashing.

	// -- footer view
	protected XListViewFooter mFooterView;
	protected boolean mEnablePullLoad;
	protected boolean mPullLoading;
	protected boolean mIsFooterReady = false;

	// total list items, used to detect is at the bottom of listview.
	protected int mTotalItemCount;

	// for mScroller, scroll back from header or footer.
	protected int mScrollBack;
	protected final static int SCROLLBACK_HEADER = 0;
	protected final static int SCROLLBACK_FOOTER = 1;

	protected final static int SCROLL_DURATION = 400; // scroll back duration
	protected final static int PULL_LOAD_MORE_DELTA = 50; // when pull up >=
	// 50px
	// at bottom,
	// trigger
	// load more.
	protected final static float OFFSET_RADIO = 1.8f; // support iOS like pull
	private ListAdapter mAdapter;

	// feature.

	private Context mContext;


	/**
	 * @param context
	 */
	public XHomePageListView(Context context) {
		super(context);
		initWithContext(context);
	}

	public XHomePageListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initWithContext(context);
		mGestureDetector = new GestureDetector(new YScrollDetector());
		setFadingEdgeLength(0);
	}

	public XHomePageListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initWithContext(context);
	}

	///////////////////////////////////////////////////////////
	// 解决listview和其他滑动的控件的冲突
	private GestureDetector mGestureDetector;
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		return super.onInterceptTouchEvent(ev)
				&& mGestureDetector.onTouchEvent(ev);
	}

	class YScrollDetector extends SimpleOnGestureListener {
		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2,
								float distanceX, float distanceY) {
			if (distanceY != 0 && distanceX != 0) {

			}
			if (Math.abs(distanceY) >= Math.abs(distanceX)) {
				return true;
			}
			return false;
		}
	}
	//////////////////////////////////////////////////////////

	protected void initWithContext(Context context) {
		this.mContext = context;
		mScroller = new Scroller(context, new DecelerateInterpolator());
		// XListView need the scroll event, and it will dispatch the event to
		// user's listener (as a proxy).
		super.setOnScrollListener(this);

		// init header view
		mHeaderView = new XListViewHeader(context);
		mHeaderViewContent = (RelativeLayout) mHeaderView
				.findViewById(R.id.xlistview_header_content);
		addHeaderView(mHeaderView);

		// init footer view
		mFooterView = new XListViewFooter(context);

		// init header height
		mHeaderView.getViewTreeObserver().addOnGlobalLayoutListener(
				new OnGlobalLayoutListener() {
					@SuppressWarnings("deprecation")
					@Override
					public void onGlobalLayout() {
						mHeaderViewHeight = mHeaderViewContent.getHeight();
						getViewTreeObserver()
								.removeGlobalOnLayoutListener(this);
					}
				});
		// 补充修改
		disablePullLoad();
		disablePullRefreash();
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
		// make sure XListViewFooter is the last footer view, and only add once.
		if (mIsFooterReady == false) {
			// if not inflate screen ,footerview not add
			if (getAdapter() != null) {
				if (getLastVisiblePosition() != (getAdapter().getCount() - 1)) {
					mIsFooterReady = true;
					addFooterView(mFooterView);
				}
			}
		}
	}

	@Override
	public void setAdapter(ListAdapter adapter) {
		// Log.e("测试调用", "XListView setAdapter");
		mAdapter = adapter;
		super.setAdapter(adapter);
	}

	public void setPullRefreshListener(IXListViewRefreshListener refreshListener) {
		mEnablePullRefresh = true;
		mHeaderViewContent.setVisibility(View.VISIBLE);
		this.mOnRefresh = refreshListener;
	}

	// 刷新完成(有时间显示)
	public void setPullRefreshComplete(String time) {
		if (mPullRefreshing == true) {
			mPullRefreshing = false;
			resetHeaderHeight();
		}
	}

	// 刷新完成(无时间显示,或显示上次的时间)
	public void setPullRefreshComplete() {
		if (mPullRefreshing == true) {
			mPullRefreshing = false;
			resetHeaderHeight();
		}
	}

	// 禁止刷新
	public void disablePullRefreash() {
		mEnablePullRefresh = false;
		mHeaderViewContent.setVisibility(View.INVISIBLE);
	}

	// 添加上拉加载监听
	public void setPullLoadListener(IXListViewLoadMore loadMoreListener) {
		mPullLoading = false;
		setPullLoadNormal();
		this.mLoadMore = loadMoreListener;
		// 可点击加载更多
		mFooterView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startLoadMore();
			}
		});
	}

	// 显示正常情况
	public void setPullLoadNormal() {
		mFooterView.show();
		mEnablePullLoad = true;
		mFooterView.setState(XListViewFooter.STATE_NORMAL);
	}

	// 上拉加载完成
	public void setPullLoadComplete() {
		if (mPullLoading == true) {
			mFooterView.show();
			mPullLoading = false;
			mFooterView.setState(XListViewFooter.STATE_NORMAL);
		}
	}

	// 显示没有更多了
	public void setPullLoadNoMore() {
		mFooterView.show();
		mEnablePullLoad = false;
		mFooterView.setState(XListViewFooter.STATE_NO_MORE);
	}

	// 禁止上拉（不显示上拉）
	public void disablePullLoad() {
		mFooterView.hide();
		mEnablePullLoad = false;
		mFooterView.setOnClickListener(null);
	}

	// 隐藏上拉加载
	public void hidePullLoad() {
		mEnablePullLoad = false;
		mFooterView.hide();
	}

	// 显示上拉加载
	public void showPullLoad() {
		mEnablePullLoad = true;
		mFooterView.show();
	}

	// 正在加载
	private void startLoadMore() {
		if (mEnablePullLoad
				&& mFooterView.getBottomMargin() > PULL_LOAD_MORE_DELTA
				&& !mPullLoading) {
			mPullLoading = true;
			mFooterView.setState(XListViewFooter.STATE_LOADING);
			if (mLoadMore != null) {
				mLoadMore.onLoadMore();
			}
		}
	}

	public void setColor(int color) {
		mFooterView.setColor(color);
		mHeaderView.setColor(color);
	}

	protected void invokeOnScrolling() {
		if (mScrollListener instanceof IXScrollListener) {
			IXScrollListener l = (IXScrollListener) mScrollListener;
			l.onXScrolling(this);
		}
	}

	protected void updateHeaderHeight(float delta) {
		mHeaderView.setVisiableHeight((int) delta
				+ mHeaderView.getVisiableHeight());
		if (mEnablePullRefresh && !mPullRefreshing) { // 未处于刷新状态,更新箭头
			if (mHeaderView.getVisiableHeight() > mHeaderViewHeight) {
				mHeaderView.setState(XListViewHeader.STATE_READY);
			} else {
				mHeaderView.setState(XListViewHeader.STATE_NORMAL);
				if (delta > 2.0) {
					if(null != mUpdateStateView){
						mUpdateStateView.setVisibility(View.GONE);
					}
				}
			}
		}
		setSelection(0); // scroll to top each time

	}

	public void hideFooter() {
		if (mFooterView != null) {
			mFooterView.setVisibility(View.GONE);
		}
	}

	public void showFooter() {
		if (mFooterView != null) {
			mFooterView.setVisibility(View.VISIBLE);
		}
	}

	/**
	 * reset header view's height.
	 */
	protected void resetHeaderHeight() {
		int height = mHeaderView.getVisiableHeight();
		if (height == 0) // not visible.
			return;
		// refreshing and header isn't shown fully. do nothing.
		if (mPullRefreshing && height <= mHeaderViewHeight) {
			return;
		}
		int finalHeight = 0; // default: scroll back to dismiss header.
		// is refreshing, just scroll back to show all the header.
		if (mPullRefreshing && height > mHeaderViewHeight) {
			finalHeight = mHeaderViewHeight;
		}
		mScrollBack = SCROLLBACK_HEADER;
		mScroller.startScroll(0, height, 0, finalHeight - height,
				SCROLL_DURATION);
		// trigger computeScroll
		invalidate();
	}

	protected void resetHeaderHeight(int disy) {
		int height = mHeaderView.getVisiableHeight();
		if (height == 0) // not visible.
			return;
		// refreshing and header isn't shown fully. do nothing.
		if (mPullRefreshing && height <= mHeaderViewHeight) {
			return;
		}
		int finalHeight = 0; // default: scroll back to dismiss header.
		// is refreshing, just scroll back to show all the header.
		if (mPullRefreshing && height > mHeaderViewHeight) {
			finalHeight = mHeaderViewHeight;
		}
		mScrollBack = SCROLLBACK_HEADER;
		mScroller.startScroll(0, height, 0, finalHeight - height + 100,
				SCROLL_DURATION);
		// trigger computeScroll
		invalidate();
	}

	protected void updateFooterHeight(float delta) {
		int height = mFooterView.getBottomMargin() + (int) delta;
		if (mEnablePullLoad && !mPullLoading) {
			if (height > PULL_LOAD_MORE_DELTA) { // height enough to invoke load
				// more.
				mFooterView.setState(XListViewFooter.STATE_READY);
			} else {
				mFooterView.setState(XListViewFooter.STATE_NORMAL);
			}
		}
		mFooterView.setBottomMargin(height);

		// setSelection(mTotalItemCount - 1); // scroll to bottom
	}

	protected void resetFooterHeight() {
		int bottomMargin = mFooterView.getBottomMargin();
		if (bottomMargin > 0) {
			mScrollBack = SCROLLBACK_FOOTER;
			mScroller.startScroll(0, bottomMargin, 0, -bottomMargin,
					SCROLL_DURATION);
			invalidate();
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {

		if (mLastY == -1) {
			mLastY = ev.getRawY();
		}

		switch (ev.getAction()) {
			case MotionEvent.ACTION_DOWN:
				mLastY = ev.getRawY();
				break;
			case MotionEvent.ACTION_MOVE:
				final float deltaY = ev.getRawY() - mLastY;
				mLastY = ev.getRawY();

				if (getFirstVisiblePosition() == 0
						&& (mHeaderView.getVisiableHeight() > 0 || deltaY > 0)
						&& !mPullRefreshing) {
					// the first item is showing, header has shown or pull down.
					if (mEnablePullRefresh) {
						updateHeaderHeight(deltaY / OFFSET_RADIO);
						invokeOnScrolling();
					}

				} else if (getLastVisiblePosition() == mTotalItemCount - 1
						&& (mFooterView.getBottomMargin() > 0 || deltaY < 0)
						&& !mPullLoading) {
					// last item, already pulled up or want to pull up.
					if (mEnablePullLoad) {
						updateFooterHeight(-deltaY / OFFSET_RADIO);
					}
				}
				break;
			default:
				mLastY = -1; // reset
				if (getFirstVisiblePosition() == 0) {
					// invoke refresh
					startOnRefresh();
					resetHeaderHeight();
				} else if (getLastVisiblePosition() == mTotalItemCount - 1) {
					// invoke load more.
					startLoadMore();
					resetFooterHeight();
				}
				break;
		}
		return super.onTouchEvent(ev);
	}

	protected void startOnRefresh() {
		if (mEnablePullRefresh
				&& mHeaderView.getVisiableHeight() > mHeaderViewHeight
				&& !mPullRefreshing) {
			mPullRefreshing = true;
			mHeaderView.setState(XListViewHeader.STATE_REFRESHING);
			if (mOnRefresh != null) {
				mOnRefresh.onRefresh();
			}
		}
	}

	// 加载成功后修改文字为加载成功
	public void endOnRefresh() {
		if (mEnablePullRefresh) { // 刷新成功
			mHeaderView.setState(XListViewHeader.STATE_REFRESHED);
		}
	}

	@Override
	public void computeScroll() {
		if (mScroller.computeScrollOffset()) {
			if (mScrollBack == SCROLLBACK_HEADER) {
				mHeaderView.setVisiableHeight(mScroller.getCurrY());
			} else {
				mFooterView.setBottomMargin(mScroller.getCurrY());
			}
			postInvalidate();
			invokeOnScrolling();
		}
		super.computeScroll();
	}

	@Override
	public void setOnScrollListener(OnScrollListener l) {
		mScrollListener = l;
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if (mScrollListener != null) {
			mScrollListener.onScrollStateChanged(view, scrollState);
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
						 int visibleItemCount, int totalItemCount) {
		// send to user's listener
		mTotalItemCount = totalItemCount;
		if (mScrollListener != null) {
			mScrollListener.onScroll(view, firstVisibleItem, visibleItemCount,
					totalItemCount);
		}
	}

	public View getUpdateStateView() {
		return mUpdateStateView;
	}

	public void setUpdateStateView(View mUpdateStateView) {
		this.mUpdateStateView = mUpdateStateView;
	}

}