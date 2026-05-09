package com.proyecto.e_commerce_java.presentation.home;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
    private EditText searchInput;
    private CheckBox nameFilterCheck;
    private CheckBox priceFilterCheck;
    private RecyclerView productsRecyclerView;
    private ProductAdapter productAdapter;
    private String token;
    private List<Product> allProducts = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_home);
        token = resolveToken();

        bindViews();
        configureProductsList();
        configureViewModel();
        configureSearch();
        configureBottomNavigation();

        if (savedInstanceState == null) {
            loadProducts();
        }
    }

    private void bindViews() {
        searchInput = findViewById(R.id.searchInput);
        nameFilterCheck = findViewById(R.id.nameFilterCheck);
        priceFilterCheck = findViewById(R.id.priceFilterCheck);
        productsRecyclerView = findViewById(R.id.productsRecyclerView);
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

    private void configureProductsList() {
        productAdapter = new ProductAdapter(product -> {
            viewModel.addProduct(product);
            Toast.makeText(this, R.string.product_added, Toast.LENGTH_SHORT).show();
        });

        productsRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        productsRecyclerView.setAdapter(productAdapter);
    }

    private void configureViewModel() {
        viewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        viewModel.getProductsLiveData().observe(this, products -> {
            allProducts = products == null ? new ArrayList<>() : products;
            filterProducts();
        });

        viewModel.getErrorLiveData().observe(this, error -> {
            if (error != null && !error.trim().isEmpty()) {
                Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void configureSearch() {
        searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence text, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence text, int start, int before, int count) {
                filterProducts();
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        nameFilterCheck.setOnCheckedChangeListener((buttonView, isChecked) -> filterProducts());
        priceFilterCheck.setOnCheckedChangeListener((buttonView, isChecked) -> filterProducts());
    }

    private void filterProducts() {
        String query = searchInput.getText().toString().trim().toLowerCase(Locale.ROOT);
        boolean filterByName = nameFilterCheck.isChecked();
        boolean filterByPrice = priceFilterCheck.isChecked();

        if (query.isEmpty()) {
            productAdapter.submitList(allProducts);
            return;
        }

        List<Product> filteredProducts = new ArrayList<>();

        for (Product product : allProducts) {
            boolean matches = false;

            if (filterByName) {
                matches = product.getName().toLowerCase(Locale.ROOT).contains(query);
            }

            if (filterByPrice) {
                String priceText = String.format(Locale.US, "%.2f", product.getPrice());
                matches = matches || priceText.contains(query);
            }

            if (!filterByName && !filterByPrice) {
                matches = product.getName().toLowerCase(Locale.ROOT).contains(query);
            }

            if (matches) {
                filteredProducts.add(product);
            }
        }

        productAdapter.submitList(filteredProducts);
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
}
