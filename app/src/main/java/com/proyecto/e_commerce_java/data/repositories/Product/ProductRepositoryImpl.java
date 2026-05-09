package com.proyecto.e_commerce_java.data.repositories.Product;

import com.proyecto.e_commerce_java.data.remote.ProductApi.ProductDto;
import com.proyecto.e_commerce_java.data.remote.ProductApi.ProductsResponse;
import com.proyecto.e_commerce_java.data.remote.ServicesApiClient.ApiService;
import com.proyecto.e_commerce_java.domain.Entities.CartItem;
import com.proyecto.e_commerce_java.domain.Entities.Product;
import com.proyecto.e_commerce_java.domain.repositories.ProductRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductRepositoryImpl implements ProductRepository {
    private static final List<CartItem> cartItems = new ArrayList<>();

    private final ApiService apiService;


    public ProductRepositoryImpl(ApiService apiService) {
        this.apiService = apiService;
    }

    @Override
    public List<CartItem> getCartItems(){
        return cartItems;
    }

    @Override
    public void addProduct(Product product) {

        for(CartItem item : cartItems){
            if (item.getProduct().getId() == product.getId()){
                item.increaseQuantity();
                return;
            }
        }
        cartItems.add(new CartItem(product,1));
    }

    @Override
    public void increaseCartItemQuantity(int productId) {
        for (CartItem item : cartItems){
            if(item.getProduct().getId() == productId){
                item.increaseQuantity();
                return;
            }
        }
    }

    @Override
    public void decreaseCartItemQuantity(int productId) {
        for (CartItem item : cartItems){
            if (item.getProduct().getId() == productId){
                item.decreaseQuantity();
                return;
            }
        }
    }

    @Override
    public void removeCartItem(int productId) {
        for(int index = 0; index < cartItems.size(); index++){
            if(cartItems.get(index).getProduct().getId() == productId){
                cartItems.remove(index);
                return;
            }
        }
    }

    @Override
    public void fetchProducts(String token, int pageNumber, int pageSize, ProductsCallback callback) {
        fetchProducts(token, pageNumber, pageSize, null, callback);
    }

    @Override
    public void fetchProducts(String token, int pageNumber, int pageSize, String search, ProductsCallback callback) {
        apiService.getProducts(formatBearerToken(token), pageNumber, pageSize, search)
                .enqueue(new Callback<ProductsResponse>() {
                    @Override
                    public void onResponse(Call<ProductsResponse> call, Response<ProductsResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            callback.onSuccess(mapProducts(response.body()));
                        } else {
                            callback.onError("Error " + response.code() + ": " + response.message());
                        }
                    }

                    @Override
                    public void onFailure(Call<ProductsResponse> call, Throwable throwable) {
                        callback.onError(throwable.getMessage());
                    }
                });
    }


    private String formatBearerToken(String token) {
        if (token == null || token.trim().isEmpty()) {
            return "";
        }

        String cleanToken = token.trim();
        if (cleanToken.toLowerCase(Locale.ROOT).startsWith("bearer ")) {
            return cleanToken;
        }

        return "Bearer " + cleanToken;
    }

    private List<Product> mapProducts(ProductsResponse response) {
        List<Product> products = new ArrayList<>();
        if (response.getItems() == null) {
            return products;
        }

        for (ProductDto productDto : response.getItems()) {
            if (!productDto.isActive()) {
                continue;
            }

            products.add(new Product(
                    productDto.getId(),
                    productDto.getName(),
                    productDto.getUnitPrice(),
                    productDto.getUnitsInStock(),
                    productDto.getImage()
            ));
        }

        return products;
    }
}
