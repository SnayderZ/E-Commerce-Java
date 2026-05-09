package com.proyecto.e_commerce_java.domain.repositories;

import com.proyecto.e_commerce_java.domain.Entities.CartItem;
import com.proyecto.e_commerce_java.domain.Entities.Product;

import java.util.List;

public interface ProductRepository {
    List<CartItem> getCartItems();

    void addProduct(Product product);
    void increaseCartItemQuantity(int productId);
    void decreaseCartItemQuantity(int productId);
    void removeCartItem(int productId);

    void fetchProducts(
            String token,
            int pageNumber,
            int pageSize,
            ProductsCallback callback
    );

    void fetchProducts(
            String token,
            int pageNumber,
            int pageSize,
            String search,
            ProductsCallback callback
    );

    interface ProductsCallback {
        void onSuccess(List<Product> products);

        void onError(String error);
    }
}
