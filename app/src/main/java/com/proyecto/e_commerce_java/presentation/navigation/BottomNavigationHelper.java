package com.proyecto.e_commerce_java.presentation.navigation;

import android.app.Activity;
import android.content.Intent;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.proyecto.e_commerce_java.R;
import com.proyecto.e_commerce_java.presentation.carrito.CarritoActivity;
import com.proyecto.e_commerce_java.presentation.home.HomeActivity;
import com.proyecto.e_commerce_java.presentation.menu.MenuActivity;
import com.proyecto.e_commerce_java.presentation.perfil.PerfilActivity;

public final class BottomNavigationHelper {
    private BottomNavigationHelper() {
    }

    public static void setup(Activity activity, BottomNavigationView navigationView, int selectedItemId) {
        navigationView.setSelectedItemId(selectedItemId);
        navigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == selectedItemId) {
                return true;
            }

            Class<?> destination = getDestination(itemId);
            if (destination == null) {
                return false;
            }

            Intent intent = new Intent(activity, destination);
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            activity.startActivity(intent);
            return true;
        });
    }

    private static Class<?> getDestination(int itemId) {
        if (itemId == R.id.nav_home) {
            return HomeActivity.class;
        }
        if (itemId == R.id.nav_cart) {
            return CarritoActivity.class;
        }
        if (itemId == R.id.nav_account) {
            return PerfilActivity.class;
        }
        if (itemId == R.id.nav_menu) {
            return MenuActivity.class;
        }

        return null;
    }
}
