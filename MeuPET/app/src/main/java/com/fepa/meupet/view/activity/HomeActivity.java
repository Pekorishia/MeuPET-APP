package com.fepa.meupet.view.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Handler;
import android.support.annotation.ColorRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.aurelhubert.ahbottomnavigation.notification.AHNotification;
import com.fepa.meupet.R;
import com.fepa.meupet.control.adapter.CustomPagerAdapter;
import com.fepa.meupet.control.general.CustomSwipePager;
import com.fepa.meupet.model.environment.constants.GeneralConfig;
import com.fepa.meupet.model.environment.enums.BottomTabs;
import com.fepa.meupet.model.environment.enums.SwipeDirection;
import com.fepa.meupet.view.fragment.CalendarFragment;
import com.fepa.meupet.view.fragment.LostPetsFragment;
import com.fepa.meupet.view.fragment.PetListFragment;
import com.fepa.meupet.view.fragment.SearchMapFragment;
import com.fepa.meupet.view.fragment.SettingsFragment;

import java.util.Map;

public class HomeActivity extends AppCompatActivity {

    private int[] notificationCount;

    private BroadcastReceiver receiver;
    private CustomSwipePager viewPager;
    private CustomPagerAdapter pagerAdapter;
    private AHBottomNavigation bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        this.viewPager = this.findViewById(R.id.viewPager);
        this.bottomNavigation = this.findViewById(R.id.bottom_navigation);
        this.pagerAdapter = new CustomPagerAdapter(getSupportFragmentManager());

        this.setupNotificationCount();

        // manages viewpager and pageradapter
        this.setupViewPagerAndPagerAdapter();

        // manages bottom navigation
        this.setupBottomNavigation();

        // handles bottom navigation tab click
        this.bottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {
                // changes activity title based on the tab selected
                String[] nameArray = getResources().getStringArray(R.array.bnav_tab_name);
                getSupportActionBar().setTitle(nameArray[position]);

                // swipes fragment based on the tab selected
                changeSelectedTab( position, wasSelected);

                removeNotification(position);
                return true;
            }
        });

        this.receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.hasExtra(GeneralConfig.Notifications.NOTIFICATION_BUNDLE)){
                    int value = ++notificationCount[BottomTabs.CALENDAR.getValue()];
                    createNotification(String.valueOf(value), BottomTabs.CALENDAR.getValue());
                }
            }
        };
    }

    private void setupNotificationCount(){
        this.notificationCount = new int[GeneralConfig.BOTTOM_NAV_SIZE];

        for (int i = 0; i < GeneralConfig.BOTTOM_NAV_SIZE; i++) {
            this.notificationCount[i] = 0;
        }
    }

    private void setupViewPagerAndPagerAdapter() {
        // disables swiping
        this.viewPager.setAllowedSwipeDirection(SwipeDirection.none);

        // manages pagerAdapter
        this.setupPagerAdapter();

        // attaches the pageAdapter as the viewPager adapter
        this.viewPager.setAdapter(this.pagerAdapter);

        viewPager.setCurrentItem(GeneralConfig.START_BOTTOM_NAV_TAB);
    }

    private void setupBottomNavigation(){
        this.setupBottomNavStyle();
        this.setupBottomNavBehaviors();
        this.addBottomNavigationItems();

        // sets the home screen start item
        this.bottomNavigation.setCurrentItem(GeneralConfig.START_BOTTOM_NAV_TAB);
    }

    private void setupPagerAdapter(){
        this.pagerAdapter.addFragments(new SearchMapFragment());
        this.pagerAdapter.addFragments(new PetListFragment());
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
        Resources res = this.getResources();

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


    private void changeSelectedTab(int position, boolean wasSelected){
        // swipes fragment based on the tab selected
        if (!wasSelected)
            this.viewPager.setCurrentItem(position);
    }

    private void createNotification(final String text, final int tabNumber) {
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

    private void removeNotification(int position){
        this.notificationCount[position] = 0;
        this.bottomNavigation.setNotification(new AHNotification(), position);
    }


    private int fetchColor(@ColorRes int color) {
        return ContextCompat.getColor(this, color);
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter();
        filter.addAction(GeneralConfig.Notifications.BROADCAST_NOTIFICATION);
        registerReceiver(this.receiver, filter);
    }

    @Override
    protected void onDestroy() {
        if (this.receiver != null){
            unregisterReceiver(this.receiver);
        }
        super.onDestroy();
    }
}
