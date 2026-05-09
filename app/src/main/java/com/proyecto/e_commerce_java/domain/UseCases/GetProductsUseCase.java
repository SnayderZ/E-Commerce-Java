package com.proyecto.e_commerce_java.domain.UseCases;

import com.proyecto.e_commerce_java.domain.repositories.ProductRepository;

public class GetProductsUseCase {
    private final ProductRepository productRepository;

    public GetProductsUseCase(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public void execute(
            String token,
            int pageNumber,
            int pageSize,
            ProductRepository.ProductsCallback callback
    ) {
        productRepository.fetchProducts(token, pageNumber, pageSize, callback);
    }

    public void execute(
            String token,
            int pageNumber,
            int pageSize,
            String search,
            ProductRepository.ProductsCallback callback
    ) {
        productRepository.fetchProducts(token, pageNumber, pageSize, search, callback);
    }
}
