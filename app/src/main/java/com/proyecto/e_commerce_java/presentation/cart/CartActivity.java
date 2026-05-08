package com.proyecto.e_commerce_java.presentation.cart;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.proyecto.e_commerce_java.R;
import com.proyecto.e_commerce_java.domain.Entities.Product;
import com.proyecto.e_commerce_java.presentation.navigation.BottomNavigationHelper;

import java.util.List;
import java.util.Locale;

public class CartActivity extends AppCompatActivity {
    private CartViewModel viewModel;
    private TextView cartText;
    private TextView totalText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        cartText = findViewById(R.id.cartText);
        totalText = findViewById(R.id.totalText);
        viewModel = new ViewModelProvider(this).get(CartViewModel.class);

        viewModel.getCartLiveData().observe(this, products ->
                cartText.setText(formatProducts(products)));
        viewModel.getTotalLiveData().observe(this, total ->
                totalText.setText(String.format(Locale.US, getString(R.string.total_format), total)));

        BottomNavigationView bottomNavigation = findViewById(R.id.bottomNavigation);
        BottomNavigationHelper.setup(this, bottomNavigation, R.id.nav_cart);
    }

    @Override
    protected void onResume() {
        super.onResume();
        viewModel.loadCart();
    }

    private String formatProducts(List<Product> products) {
        if (products == null || products.isEmpty()) {
            return getString(R.string.no_products);
        }

        StringBuilder builder = new StringBuilder();
        for (Product product : products) {
            double subtotal = product.getPrice() * product.getStock();
            builder.append(product.getName())
                    .append("\n")
                    .append(getString(R.string.stock_label))
                    .append(": ")
                    .append(product.getStock())
                    .append("   ")
                    .append(getString(R.string.price_label))
                    .append(": $")
                    .append(String.format(Locale.US, "%.2f", product.getPrice()))
                    .append("\n")
                    .append(getString(R.string.subtotal_label))
                    .append(": $")
                    .append(String.format(Locale.US, "%.2f", subtotal))
                    .append("\n\n");
        }

        return builder.toString().trim();
    }
}
