package com.proyecto.e_commerce_java.presentation.cart;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.proyecto.e_commerce_java.R;
import com.proyecto.e_commerce_java.presentation.navigation.BottomNavigationHelper;

import java.util.Locale;

public class CartActivity extends AppCompatActivity {
    private CartViewModel viewModel;
    private CartAdapter cartAdapter;
    private RecyclerView cartRecyclerView;
    private TextView totalText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        cartRecyclerView = findViewById(R.id.cartRecyclerView);
        totalText = findViewById(R.id.totalText);
        viewModel = new ViewModelProvider(this).get(CartViewModel.class);

        configureCartList();
        configureViewModel();

        BottomNavigationView bottomNavigation = findViewById(R.id.bottomNavigation);
        BottomNavigationHelper.setup(this, bottomNavigation, R.id.nav_cart);
    }

    @Override
    protected void onResume() {
        super.onResume();
        viewModel.loadCart();
    }

    private void configureCartList() {
        cartAdapter = new CartAdapter(new CartAdapter.OnCartActionListener() {
            @Override
            public void onIncrease(com.proyecto.e_commerce_java.domain.Entities.CartItem item) {
                viewModel.increaseQuantity(item);
            }

            @Override
            public void onDecrease(com.proyecto.e_commerce_java.domain.Entities.CartItem item) {
                viewModel.decreaseQuantity(item);
            }

            @Override
            public void onRemove(com.proyecto.e_commerce_java.domain.Entities.CartItem item) {
                viewModel.removeItem(item);
            }
        });

        cartRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        cartRecyclerView.setAdapter(cartAdapter);
    }

    private void configureViewModel() {
        viewModel.getCartLiveData().observe(this, cartItems ->
                cartAdapter.submitList(cartItems));

        viewModel.getTotalLiveData().observe(this, total ->
                totalText.setText(String.format(Locale.US, getString(R.string.total_format), total)));
    }
}
