package com.fepa.meupet.control.general;

import android.app.Activity;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Handler;
import android.support.annotation.ColorRes;
import android.support.v4.content.ContextCompat;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.aurelhubert.ahbottomnavigation.notification.AHNotification;
import com.fepa.meupet.R;
import com.fepa.meupet.control.adapter.CustomPagerAdapter;
import com.fepa.meupet.model.environment.constants.GeneralConfig;
import com.fepa.meupet.model.environment.enums.SwipeDirection;
import com.fepa.meupet.view.fragment.CalendarFragment;
import com.fepa.meupet.view.fragment.LostPetsFragment;
import com.fepa.meupet.view.fragment.PetFragment;
import com.fepa.meupet.view.fragment.SearchMapFragment;
import com.fepa.meupet.view.fragment.SettingsFragment;

public class HomeHandler {

    private Activity activity;
    private CustomSwipePager viewPager;
    private CustomPagerAdapter pagerAdapter;
    private AHBottomNavigation bottomNavigation;

    public HomeHandler(Activity activity){
        this.activity = activity;
    }

    public void createNotification(final String text, final int tabNumber) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                AHNotification notification = new AHNotification.Builder()
                        .setText(text)
                        .setBackgroundColor(fetchColor(R.color.notification_bg))
                        .setTextColor(fetchColor(R.color.notification_color))
                        .build();
                // Adding notification to last item.
                bottomNavigation.setNotification(notification, tabNumber);
            }
        }, 1000);
    }

    public void removeNotification(int position){
           this.bottomNavigation.setNotification(new AHNotification(), position);
    }

    public void changeSelectedTab(int position, boolean wasSelected){
        // swipes fragment based on the tab selected
        if (!wasSelected)
            this.viewPager.setCurrentItem(position);
    }

    public void setupViewPagerAndPagerAdapter(CustomSwipePager viewPager, CustomPagerAdapter pagerAdapter) {
        this.viewPager = viewPager;

        // disables swiping
        this.viewPager.setAllowedSwipeDirection(SwipeDirection.none);

        // manages pagerAdapter
        this.setupPagerAdapter(pagerAdapter);

        // attaches the pageAdapter as the viewPager adapter
        this.viewPager.setAdapter(this.pagerAdapter);

        viewPager.setCurrentItem(GeneralConfig.START_BOTTOM_NAV_TAB);
    }

    public void setupBottomNavigation(AHBottomNavigation bottomNavigation){
        this.bottomNavigation = bottomNavigation;

        this.setupBottomNavStyle();
        this.setupBottomNavBehaviors();
        this.addBottomNavigationItems();

        // sets the home screen start item
        this.bottomNavigation.setCurrentItem(GeneralConfig.START_BOTTOM_NAV_TAB);
    }

    private void setupPagerAdapter(CustomPagerAdapter pagerAdapter){
        this.pagerAdapter = pagerAdapter;

        this.pagerAdapter.addFragments(new SearchMapFragment());
        this.pagerAdapter.addFragments(new PetFragment());
        this.pagerAdapter.addFragments(new CalendarFragment());
        this.pagerAdapter.addFragments(new LostPetsFragment());
        this.pagerAdapter.addFragments(new SettingsFragment());
    }

    private void setupBottomNavStyle() {
        // sets the bottom nav background color
        this.bottomNavigation.setDefaultBackgroundColor(fetchColor(R.color.bnav_background));

        // sets the color of the selected tab to highlight it
        this.bottomNavigation.setAccentColor(fetchColor(R.color.bnav_selected_tab));

        // sets the color of the other tabs
        this.bottomNavigation.setInactiveColor(fetchColor(R.color.bnav_non_selected_tab));

        //  displays item title always (for selected and non-selected ted)
        this.bottomNavigation.setTitleState(AHBottomNavigation.TitleState.ALWAYS_SHOW);
    }

    private void setupBottomNavBehaviors() {
        // makes the bottom nav stays over the Overview Buttons instead of behind them
        this.bottomNavigation.setTranslucentNavigationEnabled(true);
    }

    private void addBottomNavigationItems() {
        // gets access to resources
        Resources res = this.activity.getResources();

        // gets the string array that contains te names and drawable paths
        String[] tabNames = res.getStringArray(R.array.bnav_tab_name);
        TypedArray tabImages = res.obtainTypedArray(R.array.bnav_tab_drawable);

        // for every string array item creates a bottom nav item
        for(int i = 0; i < tabNames.length; i++){
            AHBottomNavigationItem item = new AHBottomNavigationItem(
                    tabNames[i],
                    tabImages.getResourceId(i, -1)
            );

            // populates the bottom nav
            this.bottomNavigation.addItem(item);
        }

        // clears away the allocated space for the typed array
        tabImages.recycle();
    }

    private int fetchColor(@ColorRes int color) {
        return ContextCompat.getColor(this.activity, color);
    }
}
