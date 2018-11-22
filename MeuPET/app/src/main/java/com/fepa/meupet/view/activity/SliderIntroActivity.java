package com.fepa.meupet.view.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fepa.meupet.R;
import com.fepa.meupet.control.adapter.SliderAdapter;
import com.fepa.meupet.control.general.PreferenceManager;

public class SliderIntroActivity extends AppCompatActivity {

    private SliderAdapter sliderAdapter;
    private PreferenceManager preferenceManager;
    private LinearLayout dotLayout;
    private ViewPager viewPager;
    private TextView[] dots;
    private Button btnSkip;
    private Button btnNext;

    private final int[] layouts = new int[]
            {
                R.layout.welcome_slide1,
                R.layout.welcome_slide2,
                R.layout.welcome_slide3,
                R.layout.welcome_slide4
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        preferenceManager = new PreferenceManager(this);
        if (!preferenceManager.isFirstTimeLaunch()) {
            this.launcNextScreen();
            finish();
        }

        setContentView(R.layout.activity_slider_intro);

        this.viewPager = this.findViewById(R.id.vpSlider);
        this.dotLayout = this.findViewById(R.id.llDots);
        this.btnSkip = this.findViewById(R.id.btSkipSlider);
        this.btnNext = this.findViewById(R.id.btNextSlide);

        this.addBottomdots(0);

        this.sliderAdapter = new SliderAdapter(this, layouts);

        this.viewPager.setAdapter(this.sliderAdapter);
        this.viewPager.addOnPageChangeListener(this.viewPagerPageChangeListener);

    }

    private void launcNextScreen() {
        this.preferenceManager.setFirstTimeLaunch(false);
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        finish();
    }

    private void addBottomdots(int currentPage) {
        int[] colorsActive = getResources().getIntArray(R.array.array_dot_active);
        int[] colorsInactive = getResources().getIntArray(R.array.array_dot_inactive);

        this.dots = new TextView[this.layouts.length];
        this.dotLayout.removeAllViews();

        for (int i = 0; i < this.dots.length; i++) {
            this.dots[i] = new TextView(this);
            this.dots[i].setTextSize(35);
            this.dots[i].setText(Html.fromHtml("&#8226;"));
            this.dots[i].setTextColor(colorsInactive[currentPage]);
            this.dotLayout.addView(this.dots[i]);
        }

        if (dots.length > 0)
            this.dots[currentPage].setTextColor(colorsActive[currentPage]);
    }

    private int getItem(int i) {
        return this.viewPager.getCurrentItem() + i;
    }

    public void skip(View view) {
        this.launcNextScreen();
    }

    public void nextSlide(View view) {
        int current = getItem(+1);
        if (current < this.layouts.length) {
            this.viewPager.setCurrentItem(current);
        } else {
            this.launcNextScreen();
        }
    }


    //  viewpager change listener
    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            addBottomdots(position);

            // changing the next button text 'NEXT' / 'GOT IT'
            if (position == layouts.length - 1) {
                // last page. make button text to GOT IT
                btnNext.setText(getString(R.string.start));
                btnSkip.setVisibility(View.GONE);
            } else {
                // still pages are left
                btnNext.setText(getString(R.string.next));
                btnSkip.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    };

}