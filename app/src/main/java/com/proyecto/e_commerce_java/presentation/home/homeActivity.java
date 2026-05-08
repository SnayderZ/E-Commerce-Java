package com.proyecto.e_commerce_java.presentation.home;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.proyecto.e_commerce_java.R;
import com.proyecto.e_commerce_java.domain.Entities.Product;
import com.proyecto.e_commerce_java.presentation.navigation.BottomNavigationHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class HomeActivity extends AppCompatActivity {
    public static final String EXTRA_TOKEN = "token";
    private static final String SESSION_PREFERENCES = "session_preferences";
    private static final String KEY_TOKEN = "token";

    private HomeViewModel viewModel;
    private TextView apiProductsText;
    private Button loadProductsButton;
    private Button addFirstProductButton;
    private String token;
    private List<Product> apiProducts = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_home);
        token = resolveToken();

        bindViews();
        configureViewModel();
        configureActions();
        configureBottomNavigation();

        if (savedInstanceState == null) {
            loadProducts();
        }
    }

    private void bindViews() {
        apiProductsText = findViewById(R.id.apiProductsText);
        loadProductsButton = findViewById(R.id.loadProductsButton);
        addFirstProductButton = findViewById(R.id.addFirstProductButton);
    }

    private String resolveToken() {
        SharedPreferences preferences = getSharedPreferences(SESSION_PREFERENCES, MODE_PRIVATE);
        String intentToken = getIntent().getStringExtra(EXTRA_TOKEN);

        if (intentToken != null && !intentToken.trim().isEmpty()) {
            preferences.edit().putString(KEY_TOKEN, intentToken).apply();
            return intentToken;
        }

        return preferences.getString(KEY_TOKEN, "");
    }

    private void configureViewModel() {
        viewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        viewModel.getApiProductsLiveData().observe(this, products -> {
            apiProducts = products == null ? new ArrayList<>() : products;
            apiProductsText.setText(formatProducts(apiProducts));
        });

        viewModel.getErrorLiveData().observe(this, error -> {
            if (error != null && !error.trim().isEmpty()) {
                Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void configureActions() {
        loadProductsButton.setOnClickListener(view -> loadProducts());
        addFirstProductButton.setOnClickListener(view -> addFirstProduct());
    }

    private void loadProducts() {
        if (token == null || token.trim().isEmpty()) {
            Toast.makeText(this, R.string.no_session_token, Toast.LENGTH_SHORT).show();
            return;
        }

        viewModel.loadProducts(token);
    }

    private void configureBottomNavigation() {
        BottomNavigationView bottomNavigation = findViewById(R.id.bottomNavigation);
        BottomNavigationHelper.setup(this, bottomNavigation, R.id.nav_home);
    }

    private void addFirstProduct() {
        if (apiProducts.isEmpty()) {
            Toast.makeText(this, R.string.load_products_first, Toast.LENGTH_SHORT).show();
            return;
        }

        viewModel.addProduct(apiProducts.get(0));
        Toast.makeText(this, R.string.product_added_to_cart, Toast.LENGTH_SHORT).show();
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
