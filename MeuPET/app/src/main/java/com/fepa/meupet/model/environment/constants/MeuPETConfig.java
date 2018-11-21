package com.fepa.meupet.model.environment.constants;

import android.graphics.Color;

public final class MeuPETConfig {

    // disable instantiation
    private MeuPETConfig(){}

    // splash
    public static final int SPLASH_TIME = 3000;

    // login
    public static final int REQUEST_CODE = 0;

    // home
    public static final int START_BOTTOM_NAV_TAB = 1;

    // Pet
    public static final boolean MALE = false;
    public static final boolean FEMALE = true;

    // List
    public static final int ITEM_SELECTED_COLOR = Color.rgb(226, 226, 226);

    // Error
    public static final String INVALID_INSTANCE_EXCEPTION = "Invalid Instance Excaption";
}
