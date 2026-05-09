package com.proyecto.e_commerce_java.presentation.menu;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.proyecto.e_commerce_java.R;
import com.proyecto.e_commerce_java.presentation.navigation.BottomNavigationHelper;

public class MenuActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        setupMenuActions();

        BottomNavigationView bottomNavigation = findViewById(R.id.bottomNavigation);
        BottomNavigationHelper.setup(this, bottomNavigation, R.id.nav_menu);
    }

    private void setupMenuActions() {
        findViewById(R.id.categoriesOption).setOnClickListener(view -> showComingSoon());
        findViewById(R.id.offersOption).setOnClickListener(view -> showComingSoon());
        findViewById(R.id.favoritesOption).setOnClickListener(view -> showComingSoon());
        findViewById(R.id.addressesOption).setOnClickListener(view -> showComingSoon());
        findViewById(R.id.couponsOption).setOnClickListener(view -> showComingSoon());
        findViewById(R.id.supportOption).setOnClickListener(view -> showComingSoon());
    }

    private void showComingSoon() {
        Toast.makeText(this, R.string.coming_soon, Toast.LENGTH_SHORT).show();
    }
}
