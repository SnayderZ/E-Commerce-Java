package com.proyecto.e_commerce_java.domain.UseCases;

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

    public List<Product> getCartProducts() {
        return productRepository.getCartProducts();
    }

    public double calculateTotal() {
        double total = 0;

        for (Product product : productRepository.getCartProducts()) {
            total += product.getPrice() * product.getStock();
        }

        return total;
    }
}
