package slzjandroid.slzjapplication.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import java.util.ArrayList;

import slzjandroid.slzjapplication.R;
import slzjandroid.slzjapplication.adapter.GuideViewPagerAdapter;
import slzjandroid.slzjapplication.helper.SPLoginUser;

public class GuideActivity extends FragmentActivity {
    private ArrayList<View> guideViews;
    private GuideViewPagerAdapter guideViewPagerAdapter;

    private ImageView[] guide_dot_iv;
    private ViewPager guide_viewpager;
    private View guideView1, guideView2, guideView3, guideView4;
    private boolean falg;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            //  getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        initViews();
        initListeners();
        initValues();

    }

    private void initViews() {
        guide_dot_iv = new ImageView[4];
        guide_dot_iv[0] = (ImageView) findViewById(R.id.guide_dot1_iv);
        guide_dot_iv[1] = (ImageView) findViewById(R.id.guide_dot2_iv);
        guide_dot_iv[2] = (ImageView) findViewById(R.id.guide_dot3_iv);
        guide_dot_iv[3] = (ImageView) findViewById(R.id.guide_dot4_iv);


        guide_viewpager = (ViewPager) findViewById(R.id.guide_viewpager);

        guideView1 = LayoutInflater.from(this).inflate(R.layout.activity_guide_view1, null);
        guideView2 = LayoutInflater.from(this).inflate(R.layout.activity_guide_view2, null);

        guideView3 = LayoutInflater.from(this).inflate(R.layout.activity_guide_view3, null);
        guideView4 = LayoutInflater.from(this).inflate(R.layout.activity_guide_view4, null);
    }

    private void initListeners() {
        guide_viewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                selectPage(position);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
                if (arg0 == guide_dot_iv.length - 1 && !falg) {
                    falg = true;
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            startActivity(new Intent(GuideActivity.this, LoginActivity.class));
                            GuideActivity.this.finish();
                        }
                    }, 800);

                }
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });
    }

    private void initValues() {
        guideViews = new ArrayList<>();
        guideViews.add(guideView1);
        guideViews.add(guideView2);
        guideViews.add(guideView3);
        guideViews.add(guideView4);
        guideViewPagerAdapter = new GuideViewPagerAdapter(guideViews);
        guide_viewpager.setAdapter(guideViewPagerAdapter);
    }

    /**
     * 浮点显示控制
     *
     * @param current
     */
    private void selectPage(int current) {
        for (int i = 0; i < guide_dot_iv.length; i++) {
            guide_dot_iv[current].setImageResource(R.mipmap.img_slide_sel);
            if (current != i) {
                guide_dot_iv[i].setImageResource(R.mipmap.img_slide_u);
            }
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        SPLoginUser.setTag(GuideActivity.this, "0");
    }
}
