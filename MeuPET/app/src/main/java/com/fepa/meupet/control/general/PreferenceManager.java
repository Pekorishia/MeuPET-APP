package com.fepa.meupet.control.general;

import android.content.Context;
import android.content.SharedPreferences;

import com.fepa.meupet.model.environment.constants.GeneralConfig;

public class PreferenceManager {
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;

    public PreferenceManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(GeneralConfig.Preferences.PREFERENCE_NAME, GeneralConfig.Preferences.MODE);
        editor = pref.edit();
    }

    public void setFirstTimeLaunch(boolean isFirstTime) {
        editor.putBoolean(GeneralConfig.Preferences.IS_FIRST_LAUNCH, isFirstTime);
        editor.commit();
    }

    public boolean isFirstTimeLaunch() {
        return pref.getBoolean(GeneralConfig.Preferences.IS_FIRST_LAUNCH, true);
    }

}