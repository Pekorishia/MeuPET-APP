package com.fepa.meupet.view.activity;

import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Handler;
import android.support.annotation.ColorRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.aurelhubert.ahbottomnavigation.notification.AHNotification;
import com.fepa.meupet.R;
import com.fepa.meupet.control.adapter.BottomBarAdapter;
import com.fepa.meupet.control.viewpager.CustomSwipePager;
import com.fepa.meupet.model.environment.constants.GeneralConfig;
import com.fepa.meupet.model.environment.enums.SwipeDirection;
import com.fepa.meupet.view.fragment.CalendarFragment;
import com.fepa.meupet.view.fragment.LostPetsFragment;
import com.fepa.meupet.view.fragment.PetListFragment;
import com.fepa.meupet.view.fragment.SearchMapFragment;
import com.fepa.meupet.view.fragment.SettingsFragment;

public class HomeActivity extends AppCompatActivity {

    // TODO: detach notification handler from home
    private Boolean notificationVisible = false;

    private Toolbar toolbar;
    private CustomSwipePager viewPager;
    private BottomBarAdapter pagerAdapter;
    private AHBottomNavigation bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // manages viewpager
        this.setupViewPager();

        // manages bottom navigation
        this.setupBottomNavigation();

        // TODO: detach notification handler from home
        this.createFakeNotification();

        // handles bottom navigation tab click
        bottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {
                // changes activity title based on the tab selected
                String[] nameArray = getResources().getStringArray(R.array.bnav_tab_name);
                getSupportActionBar().setTitle(nameArray[position]);

                // swipes fragment based on the tab selected
                if (!wasSelected)
                    viewPager.setCurrentItem(position);

                // TODO: detach notification handler from home
                // remove notification badge
                int lastItemPos = bottomNavigation.getItemsCount() - 1;
                if (notificationVisible && position == lastItemPos)
                    bottomNavigation.setNotification(new AHNotification(), lastItemPos);
                return true;
            }
        });
    }

    private void setupBottomNavigation(){
        this.bottomNavigation = this.findViewById(R.id.bottom_navigation);

        this.setupBottomNavStyle();
        this.setupBottomNavBehaviors();
        this.addBottomNavigationItems();

        // sets the home screen start item
        this.bottomNavigation.setCurrentItem(GeneralConfig.START_BOTTOM_NAV_TAB);
    }

    public void setupBottomNavBehaviors() {
        // makes the bottom nav stays over the Overview Buttons instead of behind them
        bottomNavigation.setTranslucentNavigationEnabled(true);
    }

    private void setupBottomNavStyle() {
        // sets the bottom nav background color
        bottomNavigation.setDefaultBackgroundColor(fetchColor(R.color.bnav_background));

        // sets the color of the selected tab to highlight it
        bottomNavigation.setAccentColor(fetchColor(R.color.bnav_selected_tab));

        // sets the color of the other tabs
        bottomNavigation.setInactiveColor(fetchColor(R.color.bnav_non_selected_tab));

        //  displays item title always (for selected and non-selected ted)
        bottomNavigation.setTitleState(AHBottomNavigation.TitleState.ALWAYS_SHOW);
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
            bottomNavigation.addItem(item);
        }

        // clears away the allocated space for the typed array
        tabImages.recycle();
    }

    private void setupViewPager() {
        this.viewPager = this.findViewById(R.id.viewPager);

        // disables swiping
        this.viewPager.setAllowedSwipeDirection(SwipeDirection.none);

        // manages pagerAdapter
        this.setupPagerAdapter();

        // attaches the pageAdapter as the viewPager adapter
        this.viewPager.setAdapter(this.pagerAdapter);

        viewPager.setCurrentItem(GeneralConfig.START_BOTTOM_NAV_TAB);
    }

    private void setupPagerAdapter(){
        this.pagerAdapter = new BottomBarAdapter(getSupportFragmentManager());

        this.pagerAdapter.addFragments(new SearchMapFragment());
        this.pagerAdapter.addFragments(new PetListFragment());
        this.pagerAdapter.addFragments(new CalendarFragment());
        this.pagerAdapter.addFragments(new LostPetsFragment());
        this.pagerAdapter.addFragments(new SettingsFragment());
    }

    // TODO: detach notification handler from home
    private void createFakeNotification() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                AHNotification notification = new AHNotification.Builder()
                        .setText("1")
                        .setBackgroundColor(Color.YELLOW)
                        .setTextColor(Color.BLACK)
                        .build();
                // Adding notification to last item.
                bottomNavigation.setNotification(notification, bottomNavigation.getItemsCount() - 1);
                notificationVisible = true;
            }
        }, 1000);
    }

    private int fetchColor(@ColorRes int color) {
        return ContextCompat.getColor(this, color);
    }
}
