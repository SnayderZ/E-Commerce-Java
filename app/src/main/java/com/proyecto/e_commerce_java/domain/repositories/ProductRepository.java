package com.proyecto.e_commerce_java.domain.repositories;

import com.proyecto.e_commerce_java.domain.Entities.Product;

import java.util.List;

public interface ProductRepository {
    List<Product> getCartProducts();

    void addProduct(Product product);

    void fetchProducts(String token, int pageNumber, int pageSize, ProductsCallback callback);

    interface ProductsCallback {
        void onSuccess(List<Product> products);

        void onError(String error);
    }
}
