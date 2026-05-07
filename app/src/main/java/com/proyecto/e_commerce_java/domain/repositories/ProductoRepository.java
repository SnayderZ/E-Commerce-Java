package com.proyecto.e_commerce_java.domain.repositories;

import com.proyecto.e_commerce_java.domain.Entities.Producto;

import java.util.List;

public interface ProductoRepository {
    List<Producto> getProductosCarrito();

    void addProducto(Producto producto);

    void fetchProductos(String token, int pageNumber, int pageSize, ProductosCallback callback);

    interface ProductosCallback {
        void onSuccess(List<Producto> productos);

        void onError(String error);
    }
}
