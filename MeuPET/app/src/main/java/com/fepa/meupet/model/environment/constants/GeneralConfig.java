package com.fepa.meupet.model.environment.constants;

import android.graphics.Color;

public final class GeneralConfig {

    // disable instantiation
    private GeneralConfig(){}

    // splash
    public static final int SPLASH_TIME = 3500;

    // login
    public static final int REQUEST_CODE = 0;

    // home
    public static final int START_BOTTOM_NAV_TAB = 1;

    // Pet
    public static final boolean MALE = false;
    public static final boolean FEMALE = true;

    // Preference Mode
    public static final int MODE = 0;
    public static final String PREFERENCE_NAME = "WelcomeSlider";
    public static final String IS_FIRST_LAUNCH = "IsFirstTimeLaunch";

    // List
    public static final int ITEM_SELECTED_COLOR = Color.rgb(226, 226, 226);

    // Error
    public static final String INVALID_INSTANCE_EXCEPTION = "Invalid Instance Excaption";
}
