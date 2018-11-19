package com.fepa.meupet.view.activity;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Handler;
import android.support.annotation.ColorRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.aurelhubert.ahbottomnavigation.notification.AHNotification;
import com.fepa.meupet.R;
import com.fepa.meupet.model.environment.constants.MeuPETConfig;
import com.fepa.meupet.model.environment.intarface.MeuPETInterface;
import com.fepa.meupet.view.fragment.PetListFragment;

public class HomeActivity extends AppCompatActivity implements MeuPETInterface {

    private final int[] tabColors = {
            R.color.bnav_tab1,
            R.color.bnav_tab2,
            R.color.bnav_tab3
    };

    private Toolbar toolbar;
    private AHBottomNavigation bottomNavigation;

    private Boolean notificationVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(
                AppCompatDelegate.MODE_NIGHT_AUTO);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        this.toolbar = this.findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final PetListFragment fragment = new PetListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("color", ContextCompat.getColor(this, tabColors[0]));
        fragment.setArguments(bundle);

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.frame, fragment, PetListFragment.TAG)
                .commit();

        this.bottomNavigation = this.findViewById(R.id.bottom_navigation);
        setupBottomNavBehaviors();
        setupBottomNavStyle();

        this.addBottomNavigationItems();

        this.createFakeNotification();

        // sets the home screen start item
        bottomNavigation.setCurrentItem(MeuPETConfig.START_BOTTOM_NAV_TAB);

        bottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {
                fragment.updateColor(ContextCompat.getColor(HomeActivity.this, tabColors[position]));

                String[] nameArray = getResources().getStringArray(R.array.bnav_tab_name);
                getSupportActionBar().setTitle(nameArray[position]);

                // remove notification badge
                int lastItemPos = bottomNavigation.getItemsCount() - 1;
                if (notificationVisible && position == lastItemPos)
                    bottomNavigation.setNotification(new AHNotification(), lastItemPos);
                return true;
            }
        });
    }

    public void setupBottomNavBehaviors() {
        bottomNavigation.setBehaviorTranslationEnabled(true);

        /*
        Before enabling this. Change MainActivity theme to MyTheme.TranslucentNavigation in
        AndroidManifest.
        Warning: Toolbar Clipping might occur. Solve this by wrapping it in a LinearLayout with a top
        View of 24dp (status bar size) height.
         */
        bottomNavigation.setTranslucentNavigationEnabled(true);
    }

    /**
     * Adds styling properties to {@link AHBottomNavigation}
     */
    private void setupBottomNavStyle() {
        /*
        Set Bottom Navigation colors. Accent color for active item,
        Inactive color when its view is disabled.
        Will not be visible if setColored(true) and default current item is set.
         */
        bottomNavigation.setDefaultBackgroundColor(Color.YELLOW);
        bottomNavigation.setAccentColor(fetchColor(R.color.bnav_tab2));
        bottomNavigation.setInactiveColor(fetchColor(R.color.bottomtab_item_resting));

        // Colors for selected (active) and non-selected items.
        bottomNavigation.setColoredModeColors(Color.WHITE,
                fetchColor(R.color.bottomtab_item_resting));


        //  Enables Reveal effect
        bottomNavigation.setColored(true);

        //  Displays item Title always (for selected and non-selected items)
        bottomNavigation.setTitleState(AHBottomNavigation.TitleState.ALWAYS_SHOW);
    }


    private void addBottomNavigationItems() {

        // gets access to resources
        Resources res = this.getResources();

        // gets the string array that contains te names and drawable paths
        String[] tabNames = res.getStringArray(R.array.bnav_tab_name);
        TypedArray tabImages = res.obtainTypedArray(R.array.bnav_tab_drawable);
//
//        // for every string array item creates a bottom nav item
//        for(int i = 0; i < tabNames.length; i++){
//            AHBottomNavigationItem item = new AHBottomNavigationItem(
//                    tabNames[i],
//                    tabImages.getResourceId(i, -1),
//                    tabColors[i]
//            );
//
//            // populates the bottom nav
//            bottomNavigation.addItem(item);
//        }
//
//        // clears away the allocated space for the typed array
//        tabImages.recycle();

        AHBottomNavigationItem item1 = new AHBottomNavigationItem(tabNames[0], tabImages.getResourceId(0,-1), tabColors[0]);
        @SuppressLint("ResourceType") AHBottomNavigationItem item2 = new AHBottomNavigationItem(tabNames[1], tabImages.getResourceId(1,-1), tabColors[1]);
        @SuppressLint("ResourceType") AHBottomNavigationItem item3 = new AHBottomNavigationItem(tabNames[2], tabImages.getResourceId(2,-1), tabColors[2]);

        bottomNavigation.addItem(item1);
        bottomNavigation.addItem(item2);
        bottomNavigation.addItem(item3);
    }

    /**
     * Simple facade to fetch color resource, so I avoid writing a huge line every time.
     *
     * @param color to fetch
     * @return int color value.
     */
    private int fetchColor(@ColorRes int color) {
        return ContextCompat.getColor(this, color);
    }

    @Override
    public void onPetListClick() {
        Toast.makeText(this, "Click", Toast.LENGTH_SHORT).show();
    }

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
}
