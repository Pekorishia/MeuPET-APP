package com.fepa.meupet.model.environment.constants;

import android.graphics.Color;

import com.fepa.meupet.model.environment.enums.BottomTabs;

public final class GeneralConfig {

    // disable instantiation
    private GeneralConfig(){}

    public static final int RESULT_OK = 1;
    public static final int REQUEST_CODE = 0;

    // splash
    public static final int SPLASH_TIME = 3500;

    // login
    public static final String LOGIN_ERROR = "Erro ao logar";

    // Firebase database
    public static final String DB_PATH_PERSON = "miauBD/person/";
    public static final String DB_PATH_MAP = "miauBD/searchMap/";
    public static final String DB_PATH_COLLAR = "miauBD/pet/0100/module/collar/040/statistic/";

    // Register
    public static final String REGISTER_ERROR = "Erro ao cadastrar";

    // home
    public static final int START_BOTTOM_NAV_TAB = BottomTabs.PETS.getValue();
    public static final int BOTTOM_NAV_SIZE = 5;

    // List
    public static final int ITEM_SELECTED_COLOR = Color.rgb(226, 226, 226);

    // notification
    public final class Notifications {
        public static final String BROADCAST_NOTIFICATION = "br.ufrn.android.broadcast.NOTIFICATION";
        public static final String NOTIFICATION_BUNDLE = "notificationBundle";
    }

    // Pets
    public final class Pets {
        public static final int PET_ADD_REQUEST_CODE = 10;
        public static final String PET_BUNDLE = "petBundle";
        public static final int ACTIVITY_LVL_CHART = 0;
        public static final int EATING_HABITS_CHART = 1;
    }

    // Preference Mode
    public final class Preferences {
        public static final int MODE = 0;
        public static final String PREFERENCE_NAME = "WelcomeSlider";
        public static final String IS_FIRST_LAUNCH = "IsFirstTimeLaunch";
    }

    // Maps
    public final class Maps {
        public static final int SEARCH_MAP_REQUEST_CODE = 10;
        public static final String SEARCH_MAP_BUNDLE = "mapBundle";
    }
}
