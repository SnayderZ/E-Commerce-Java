package com.proyecto.e_commerce_java.domain.UseCases;

import com.proyecto.e_commerce_java.domain.repositories.ProductoRepository;

public class ObtenerProductosUseCase {
    private final ProductoRepository productoRepository;

    public ObtenerProductosUseCase(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    public void execute(String token, int pageNumber, int pageSize, ProductoRepository.ProductosCallback callback) {
        productoRepository.fetchProductos(token, pageNumber, pageSize, callback);
    }
}
