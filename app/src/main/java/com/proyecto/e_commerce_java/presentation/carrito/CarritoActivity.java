package com.proyecto.e_commerce_java.presentation.carrito;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.proyecto.e_commerce_java.R;
import com.proyecto.e_commerce_java.domain.Entities.Producto;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CarritoActivity extends AppCompatActivity {
    public static final String EXTRA_TOKEN = "token";

    private CarritoViewModel viewModel;
    private TextView productosApiText;
    private TextView carritoText;
    private TextView totalText;
    private Button cargarProductosButton;
    private Button agregarPrimerProductoButton;
    private List<Producto> productosApi = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carrito);

        productosApiText = findViewById(R.id.productosApiText);
        carritoText = findViewById(R.id.carritoText);
        totalText = findViewById(R.id.totalText);
        cargarProductosButton = findViewById(R.id.cargarProductosButton);
        agregarPrimerProductoButton = findViewById(R.id.agregarPrimerProductoButton);
        viewModel = new ViewModelProvider(this).get(CarritoViewModel.class);

        cargarProductosButton.setOnClickListener(view -> viewModel.cargarProductos());
        agregarPrimerProductoButton.setOnClickListener(view -> agregarPrimerProducto());

        viewModel.getProductosApiLiveData().observe(this, productos -> {
            productosApi = productos;
            productosApiText.setText(formatProductos(productos));
        });

        viewModel.getCarritoLiveData().observe(this, productos ->
                carritoText.setText(formatProductos(productos)));

        viewModel.getTotalLiveData().observe(this, total ->
                totalText.setText(String.format(Locale.US, "Total: $%.2f", total)));

        viewModel.getErrorLiveData().observe(this, error ->
                Toast.makeText(this, error, Toast.LENGTH_SHORT).show());

        viewModel.actualizarCarrito();
    }

    private void agregarPrimerProducto() {
        if (productosApi.isEmpty()) {
            Toast.makeText(this, "Primero cargue productos desde la API", Toast.LENGTH_SHORT).show();
            return;
        }

        viewModel.agregarProducto(productosApi.get(0));
    }

    private String formatProductos(List<Producto> productos) {
        if (productos == null || productos.isEmpty()) {
            return "Sin productos";
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

        return builder.toString();
    }
}
