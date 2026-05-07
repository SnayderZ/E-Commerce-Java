package com.proyecto.e_commerce_java.domain.Entities;

import java.util.ArrayList;
import java.util.List;

public class ShoppingCart {
    private final List<Producto> productos = new ArrayList<>();

    public void addProducto(Producto producto) {
        productos.add(producto);
    }

    public List<Producto> getProductos() {
        return productos;
    }

    public double calcularTotal() {
        double total = 0;

        for (Producto producto : productos) {
            total += producto.getPrecio() * producto.getCantidad();
        }

        return total;
    }
}
