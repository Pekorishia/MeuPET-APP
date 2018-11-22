package com.fepa.meupet.control.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class SliderAdapter extends PagerAdapter {
    private LayoutInflater layoutInflater;

    private Activity activity;
    private int[] layouts;

    public SliderAdapter(Activity activity, final int[] layouts) {
        this.activity = activity;
        this.layouts = layouts;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        this.layoutInflater = (LayoutInflater) this.activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = this.layoutInflater.inflate(this.layouts[position], container, false);
        container.addView(view);

        return view;
    }

    @Override
    public int getCount() {
        return this.layouts.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object obj) {
        return view == obj;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View view = (View) object;
        container.removeView(view);
    }
}
