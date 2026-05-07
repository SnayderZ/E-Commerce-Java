package com.proyecto.e_commerce_java.domain.UseCases;

import com.proyecto.e_commerce_java.domain.Entities.Producto;
import com.proyecto.e_commerce_java.domain.repositories.ProductoRepository;

import java.util.List;

public class CarritoUseCase {
    private final ProductoRepository productoRepository;

    public CarritoUseCase(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    public void agregarProducto(Producto producto) {
        productoRepository.addProducto(producto);
    }

    public List<Producto> obtenerProductosCarrito() {
        return productoRepository.getProductosCarrito();
    }

    public double calcularTotal() {
        double total = 0;

        for (Producto producto : productoRepository.getProductosCarrito()) {
            total += producto.getPrecio() * producto.getCantidad();
        }

        return total;
    }
}
