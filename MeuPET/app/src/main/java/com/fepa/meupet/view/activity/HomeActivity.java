package com.fepa.meupet.view.activity;

import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.fepa.meupet.R;
import com.fepa.meupet.control.adapter.BottomBarAdapter;
import com.fepa.meupet.control.general.CustomSwipePager;
import com.fepa.meupet.control.general.HomeHandler;

public class HomeActivity extends AppCompatActivity {

    private HomeHandler handler;

//    @Override
//    public void onRestoreInstanceState(Bundle savedInstanceState, PersistableBundle persistentState) {
//        super.onRestoreInstanceState(savedInstanceState, persistentState);
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        CustomSwipePager viewPager = this.findViewById(R.id.viewPager);
        AHBottomNavigation bottomNavigation = this.findViewById(R.id.bottom_navigation);
        BottomBarAdapter pagerAdapter = new BottomBarAdapter(getSupportFragmentManager());

        this.handler = new HomeHandler(this);

        // manages viewpager and pageradapter
        this.handler.setupViewPagerAndPagerAdapter(viewPager, pagerAdapter);

        // manages bottom navigation
        this.handler.setupBottomNavigation(bottomNavigation);

        // TODO: MAKE IT DYNAMIC
        this.handler.createNotification("1", 2);

        // handles bottom navigation tab click
        bottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {
                // changes activity title based on the tab selected
                String[] nameArray = getResources().getStringArray(R.array.bnav_tab_name);
                getSupportActionBar().setTitle(nameArray[position]);

                // swipes fragment based on the tab selected
                handler.changeSelectedTab( position, wasSelected);

                handler.removeNotification(position);
                return true;
            }
        });
    }
}
