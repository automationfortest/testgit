package slzjandroid.slzjapplication.fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.umeng.analytics.MobclickAgent;

import slzjandroid.slzjapplication.utils.CommonUtils;

/**
 *
 */


public abstract class BaseFragment extends Fragment {
    protected String channelId, url, rfu, tp, pid, uid;
    public String TAG = "";
    private View rootView;
    protected Context mContext;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mContext = getActivity();
        rootView = inflater.inflate(getLayoutId(), null);
        findViews();
        init();
        bindViews();
        TAG = getClass().getSimpleName();
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    protected abstract int getLayoutId();

    protected abstract void findViews();

    protected abstract void bindViews();

    protected abstract void init();

    public View findViewById(int resId) {

        if (rootView != null) {
            return rootView.findViewById(resId);
        }
        return null;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        umengCount(true);
    }

    @Override
    public void onPause() {
        super.onPause();
        umengCount(false);
    }

    protected String getDisplayTitle() {
        return "";
    }

    public void umengCount(boolean tag) {
        String displayTitle = getDisplayTitle();

        if (CommonUtils.hasText(displayTitle)) {
            if (tag) {
                MobclickAgent.onPageStart(displayTitle);
            } else {
                MobclickAgent.onPageEnd(displayTitle);
            }
        } else {
            if (tag) {
                MobclickAgent.onPageStart(getClass().getSimpleName());
            } else {
                MobclickAgent.onPageEnd(getClass().getSimpleName());
            }
        }
    }
}
