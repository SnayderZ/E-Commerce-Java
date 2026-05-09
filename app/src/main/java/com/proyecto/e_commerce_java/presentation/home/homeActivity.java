package com.proyecto.e_commerce_java.presentation.home;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Bundle;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
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

public class HomeActivity extends AppCompatActivity {
    public static final String EXTRA_TOKEN = "token";
    public static final String EXTRA_FIRST_NAME = "first_name";
    public static final String EXTRA_LAST_NAME = "last_name";
    public static final String EXTRA_EMAIL = "email";
    public static final String EXTRA_PROFILE_PHOTO = "profile_photo";

    private static final String SESSION_PREFERENCES = "session_preferences";
    private static final String KEY_TOKEN = "token";
    private static final String KEY_FIRST_NAME = "profile_first_name";
    private static final String KEY_LAST_NAME = "profile_last_name";
    private static final String KEY_EMAIL = "profile_email";
    private static final String KEY_PROFILE_PHOTO = "profile_photo";
    private static final long SEARCH_DELAY_MILLIS = 350L;

    private HomeViewModel viewModel;
    private EditText searchInput;
    private CheckBox nameFilterCheck;
    private CheckBox priceFilterCheck;
    private RecyclerView productsRecyclerView;
    private ProductAdapter productAdapter;
    private String token;
    private final Handler searchHandler = new Handler(Looper.getMainLooper());
    private final Runnable searchRunnable = this::searchProductsFromBackend;

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

        persistSessionProfileData();
        String profilePhoto = resolveProfilePhoto();
        loadProfilePhoto(profilePhoto);
    }
    @Override
    protected void onResume() {
        super.onResume();

        String profilePhoto = getSharedPreferences(SESSION_PREFERENCES, MODE_PRIVATE)
                .getString(KEY_PROFILE_PHOTO, "");

        loadProfilePhoto(profilePhoto);
    }


    private void loadProfilePhoto(String profilePhoto) {
        ImageView profileImage = findViewById(R.id.profileImage);

        if (profilePhoto == null || profilePhoto.trim().isEmpty()) {
            profileImage.setImageResource(R.drawable.ic_launcher_background);
            return;
        }

        try {
            byte[] imageBytes = Base64.decode(profilePhoto, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
            profileImage.setImageBitmap(bitmap);
        } catch (Exception exception) {
            profileImage.setImageResource(R.drawable.ic_launcher_background);
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

    private String resolveProfilePhoto() {
        SharedPreferences preferences = getSharedPreferences(SESSION_PREFERENCES, MODE_PRIVATE);
        String intentProfilePhoto = getIntent().getStringExtra(EXTRA_PROFILE_PHOTO);

        if (intentProfilePhoto != null && !intentProfilePhoto.trim().isEmpty()) {
            preferences.edit().putString(KEY_PROFILE_PHOTO, intentProfilePhoto).apply();
            return intentProfilePhoto;
        }

        return preferences.getString(KEY_PROFILE_PHOTO, "");
    }

    private void persistSessionProfileData() {
        SharedPreferences.Editor editor = getSharedPreferences(SESSION_PREFERENCES, MODE_PRIVATE).edit();
        putExtraIfPresent(editor, EXTRA_FIRST_NAME, KEY_FIRST_NAME);
        putExtraIfPresent(editor, EXTRA_LAST_NAME, KEY_LAST_NAME);
        putExtraIfPresent(editor, EXTRA_EMAIL, KEY_EMAIL);
        editor.apply();
    }

    private void putExtraIfPresent(SharedPreferences.Editor editor, String extraKey, String preferenceKey) {
        String value = getIntent().getStringExtra(extraKey);
        if (value != null && !value.trim().isEmpty()) {
            editor.putString(preferenceKey, value);
        }
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
            List<Product> currentProducts = products == null ? new ArrayList<>() : products;
            productAdapter.submitList(currentProducts);
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
                scheduleProductSearch();
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        nameFilterCheck.setOnCheckedChangeListener((buttonView, isChecked) -> searchProductsFromBackend());
        priceFilterCheck.setOnCheckedChangeListener((buttonView, isChecked) -> searchProductsFromBackend());
    }

    private void scheduleProductSearch() {
        searchHandler.removeCallbacks(searchRunnable);
        searchHandler.postDelayed(searchRunnable, SEARCH_DELAY_MILLIS);
    }

    private void searchProductsFromBackend() {
        if (token == null || token.trim().isEmpty()) {
            Toast.makeText(this, R.string.no_session_token, Toast.LENGTH_SHORT).show();
            return;
        }

        String search = buildBackendSearchQuery();
        if (search == null) {
            viewModel.loadProducts(token);
            return;
        }

        viewModel.searchProducts(token, search);
    }

    private String buildBackendSearchQuery() {
        String query = searchInput.getText().toString().trim();
        if (query.isEmpty()) {
            return null;
        }

        boolean filterByName = nameFilterCheck.isChecked();
        boolean filterByPrice = priceFilterCheck.isChecked();

        if (filterByPrice && !filterByName) {
            return "price:" + query;
        }

        return "name:" + query;
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
