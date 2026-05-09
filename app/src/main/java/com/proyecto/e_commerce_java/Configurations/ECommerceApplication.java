package com.proyecto.e_commerce_java.Configurations;

import android.app.Application;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatDelegate;

public class ECommerceApplication extends Application {
    private static final String SETTINGS_NAME = "app_settings";
    private static final String DARK_MODE_KEY = "dark_mode_enabled";

    @Override
    public void onCreate() {
        super.onCreate();

        SharedPreferences preferences = getSharedPreferences(SETTINGS_NAME, MODE_PRIVATE);
        boolean darkModeEnabled = preferences.getBoolean(DARK_MODE_KEY, false);

        AppCompatDelegate.setDefaultNightMode(
                darkModeEnabled
                        ? AppCompatDelegate.MODE_NIGHT_YES
                        : AppCompatDelegate.MODE_NIGHT_NO
        );
    }
}