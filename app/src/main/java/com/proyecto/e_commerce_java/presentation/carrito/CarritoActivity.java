package com.proyecto.e_commerce_java.presentation.carrito;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.proyecto.e_commerce_java.R;
import com.proyecto.e_commerce_java.domain.Entities.Producto;
import com.proyecto.e_commerce_java.presentation.navigation.BottomNavigationHelper;

import java.util.List;
import java.util.Locale;

public class CarritoActivity extends AppCompatActivity {
    private CarritoViewModel viewModel;
    private TextView carritoText;
    private TextView totalText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carrito);

        carritoText = findViewById(R.id.carritoText);
        totalText = findViewById(R.id.totalText);
        viewModel = new ViewModelProvider(this).get(CarritoViewModel.class);

        viewModel.getCarritoLiveData().observe(this, productos ->
                carritoText.setText(formatProductos(productos)));
        viewModel.getTotalLiveData().observe(this, total ->
                totalText.setText(String.format(Locale.US, "Total: $%.2f", total)));

        BottomNavigationView bottomNavigation = findViewById(R.id.bottomNavigation);
        BottomNavigationHelper.setup(this, bottomNavigation, R.id.nav_cart);
    }

    @Override
    protected void onResume() {
        super.onResume();
        viewModel.cargarCarrito();
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
