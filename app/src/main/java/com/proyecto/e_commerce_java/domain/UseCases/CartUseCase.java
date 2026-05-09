package com.proyecto.e_commerce_java.domain.UseCases;

import com.proyecto.e_commerce_java.domain.Entities.CartItem;
import com.proyecto.e_commerce_java.domain.Entities.Product;
import com.proyecto.e_commerce_java.domain.repositories.ProductRepository;

import java.util.List;

public class CartUseCase {
    private final ProductRepository productRepository;

    public CartUseCase(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public void addProduct(Product product) {
        productRepository.addProduct(product);
    }

    public List<CartItem> getCartItems(){

        return productRepository.getCartItems();
    }
    public void increaseQuantity(int productId){
        productRepository.increaseCartItemQuantity(productId);
    }
    public void decreaseQuantity(int productId){
        productRepository.decreaseCartItemQuantity(productId);
    }
    public void removeItem(int productId){
        productRepository.removeCartItem(productId);
    }

    public double calculateTotal() {
        double total = 0;

        for (CartItem item : productRepository.getCartItems()) {
            total += item.getSubTotal();
        }

        return total;
    }
}
