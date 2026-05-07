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
import com.proyecto.e_commerce_java.domain.Entities.Producto;
import com.proyecto.e_commerce_java.presentation.navigation.BottomNavigationHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class HomeActivity extends AppCompatActivity {
    public static final String EXTRA_TOKEN = "token";
    private static final String SESSION_PREFERENCES = "session_preferences";
    private static final String KEY_TOKEN = "token";

    private HomeViewModel viewModel;
    private TextView productosApiText;
    private Button cargarProductosButton;
    private Button agregarPrimerProductoButton;
    private String token;
    private List<Producto> productosApi = new ArrayList<>();

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
            cargarProductos();
        }
    }

    private void bindViews() {
        productosApiText = findViewById(R.id.productosApiText);
        cargarProductosButton = findViewById(R.id.cargarProductosButton);
        agregarPrimerProductoButton = findViewById(R.id.agregarPrimerProductoButton);
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

        viewModel.getProductosApiLiveData().observe(this, productos -> {
            productosApi = productos == null ? new ArrayList<>() : productos;
            productosApiText.setText(formatProductos(productosApi));
        });

        viewModel.getErrorLiveData().observe(this, error -> {
            if (error != null && !error.trim().isEmpty()) {
                Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void configureActions() {
        cargarProductosButton.setOnClickListener(view -> cargarProductos());
        agregarPrimerProductoButton.setOnClickListener(view -> agregarPrimerProducto());
    }

    private void cargarProductos() {
        if (token == null || token.trim().isEmpty()) {
            Toast.makeText(this, "No hay token de sesion para cargar productos", Toast.LENGTH_SHORT).show();
            return;
        }

        viewModel.cargarProductos(token);
    }

    private void configureBottomNavigation() {
        BottomNavigationView bottomNavigation = findViewById(R.id.bottomNavigation);
        BottomNavigationHelper.setup(this, bottomNavigation, R.id.nav_home);
    }

    private void agregarPrimerProducto() {
        if (productosApi.isEmpty()) {
            Toast.makeText(this, "Primero cargue productos desde la API", Toast.LENGTH_SHORT).show();
            return;
        }

        viewModel.agregarProducto(productosApi.get(0));
        Toast.makeText(this, "Producto agregado al carrito", Toast.LENGTH_SHORT).show();
    }

    private String formatProductos(List<Producto> productos) {
        if (productos == null || productos.isEmpty()) {
            return getString(R.string.sin_productos);
        }

        StringBuilder builder = new StringBuilder();
        for (Producto producto : productos) {
            double subtotal = producto.getPrecio() * producto.getCantidad();
            builder.append(producto.getNombre())
                    .append("\n")
                    .append("Cantidad: ")
                    .append(producto.getCantidad())
                    .append("   Precio: $")
                    .append(String.format(Locale.US, "%.2f", producto.getPrecio()))
                    .append("\n")
                    .append("Subtotal: $")
                    .append(String.format(Locale.US, "%.2f", subtotal))
                    .append("\n\n");
        }

        return builder.toString().trim();
    }
}
