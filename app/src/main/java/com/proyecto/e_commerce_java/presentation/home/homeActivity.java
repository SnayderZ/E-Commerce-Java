package com.proyecto.e_commerce_java.presentation.home;

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

    private HomeViewModel viewModel;
    private TextView productosApiText;
    private Button cargarProductosButton;
    private Button agregarPrimerProductoButton;
    private List<Producto> productosApi = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_home);

        bindViews();
        configureViewModel();
        configureActions();
        configureBottomNavigation();
    }

    private void bindViews() {
        productosApiText = findViewById(R.id.productosApiText);
        cargarProductosButton = findViewById(R.id.cargarProductosButton);
        agregarPrimerProductoButton = findViewById(R.id.agregarPrimerProductoButton);
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
        cargarProductosButton.setOnClickListener(view -> viewModel.cargarProductos());
        agregarPrimerProductoButton.setOnClickListener(view -> agregarPrimerProducto());
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
