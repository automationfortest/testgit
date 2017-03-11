package slzjandroid.slzjapplication.weights.listview.xlistview;

import android.view.View;

/**
 * you can listen ListView.OnScrollListener or this one. it will invoke
 * onXScrolling when header/footer scroll back.
 */
public interface IXScrollListener {
	/**
	 * @param view
	 */
	public void onXScrolling(View view);
}
