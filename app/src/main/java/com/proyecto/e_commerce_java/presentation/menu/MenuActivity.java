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

        findViewById(R.id.categoriesOption).setOnClickListener(view -> showComingSoon());
        findViewById(R.id.settingsOption).setOnClickListener(view -> showComingSoon());

        BottomNavigationView bottomNavigation = findViewById(R.id.bottomNavigation);
        BottomNavigationHelper.setup(this, bottomNavigation, R.id.nav_menu);
    }

    private void showComingSoon() {
        Toast.makeText(this, R.string.proximamente, Toast.LENGTH_SHORT).show();
    }
}
