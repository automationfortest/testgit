package slzjandroid.slzjapplication.adapter;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by xuyifei on 16/5/13.
 */
public class GuideViewPagerAdapter extends PagerAdapter {
    private ArrayList<View> Views;

    public GuideViewPagerAdapter(ArrayList<View> views) {
        this.Views = views;
    }

    /**
     * 获取总界面数
     */
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return Views.size();
    }

    /**
     * 判断pager的一个view是否和instantiateItem方法返回的object有关联，并决定是否由对象生成界面
     */
    @Override
    public boolean isViewFromObject(View container, Object object) {
        // TODO Auto-generated method stub
        return container == object;
    }

    /**
     * PagerAdapter适配器选择哪个对象
     */
    @Override
    public Object instantiateItem(View container, int position) {
        // TODO Auto-generated method stub
        ((ViewPager) container).addView(Views.get(position));
        return Views.get(position);
    }

    /**
     * 从ViewGroup中移出当前View
     */
    @Override
    public void destroyItem(View container, int position, Object object) {
        // TODO Auto-generated method stub
        ((ViewPager) container).removeView((View) object);
    }


}
