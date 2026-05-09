package com.proyecto.e_commerce_java.presentation.profile;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.proyecto.e_commerce_java.R;
import com.proyecto.e_commerce_java.presentation.login.LoginActivity;
import com.proyecto.e_commerce_java.presentation.navigation.BottomNavigationHelper;

public class ProfileActivity extends AppCompatActivity {
    private static final String SETTINGS_NAME = "app_settings";
    private static final String DARK_MODE_KEY = "dark_mode_enabled";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        setupActions();
        setupBottomNavigation();
    }

    private void setupActions() {
        Button saveProfileButton = findViewById(R.id.saveProfileButton);
        Button recentPurchasesButton = findViewById(R.id.recentPurchasesButton);
        Button purchaseHistoryButton = findViewById(R.id.purchaseHistoryButton);
        Button logoutButton = findViewById(R.id.logoutButton);
        Switch darkModeSwitch = findViewById(R.id.darkModeSwitch);

        SharedPreferences preferences = getSharedPreferences(SETTINGS_NAME, MODE_PRIVATE);
        darkModeSwitch.setChecked(preferences.getBoolean(DARK_MODE_KEY, false));

        saveProfileButton.setOnClickListener(view ->
                Toast.makeText(this, R.string.profile_saved_placeholder, Toast.LENGTH_SHORT).show());

        recentPurchasesButton.setOnClickListener(view ->
                Toast.makeText(this, R.string.recent_purchases_placeholder, Toast.LENGTH_SHORT).show());

        purchaseHistoryButton.setOnClickListener(view ->
                Toast.makeText(this, R.string.purchase_history_placeholder, Toast.LENGTH_SHORT).show());

        darkModeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            preferences.edit().putBoolean(DARK_MODE_KEY, isChecked).apply();
            AppCompatDelegate.setDefaultNightMode(
                    isChecked ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO
            );
        });

        logoutButton.setOnClickListener(view -> logout());
    }

    private void logout() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private void setupBottomNavigation() {
        BottomNavigationView bottomNavigation = findViewById(R.id.bottomNavigation);
        BottomNavigationHelper.setup(this, bottomNavigation, R.id.nav_account);
    }
}
