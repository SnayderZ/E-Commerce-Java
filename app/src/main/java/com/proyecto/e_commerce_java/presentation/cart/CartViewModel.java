package com.proyecto.e_commerce_java.presentation.cart;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.proyecto.e_commerce_java.data.remote.ServicesApiClient.ApiService;
import com.proyecto.e_commerce_java.data.remote.ServicesApiClient.RetrofitClient;
import com.proyecto.e_commerce_java.data.repositories.Product.ProductRepositoryImpl;
import com.proyecto.e_commerce_java.domain.Entities.CartItem;
import com.proyecto.e_commerce_java.domain.UseCases.CartUseCase;
import com.proyecto.e_commerce_java.domain.repositories.ProductRepository;

import java.util.List;

public class CartViewModel extends ViewModel {
    private final CartUseCase cartUseCase;
    private final MutableLiveData<List<CartItem>> cartLiveData = new MutableLiveData<>();
    private final MutableLiveData<Double> totalLiveData = new MutableLiveData<>(0.0);

    public CartViewModel() {
        ApiService apiService = RetrofitClient.getApiService();
        ProductRepository productRepository = new ProductRepositoryImpl(apiService);
        cartUseCase = new CartUseCase(productRepository);
    }

    public void loadCart() {
        cartLiveData.setValue(cartUseCase.getCartItems());
        totalLiveData.setValue(cartUseCase.calculateTotal());
    }

    public void increaseQuantity(CartItem item) {
        cartUseCase.increaseQuantity(item.getProduct().getId());
        loadCart();
    }

    public void decreaseQuantity(CartItem item) {
        cartUseCase.decreaseQuantity(item.getProduct().getId());
        loadCart();
    }

    public void removeItem(CartItem item) {
        cartUseCase.removeItem(item.getProduct().getId());
        loadCart();
    }

    public LiveData<List<CartItem>> getCartLiveData() {
        return cartLiveData;
    }

    public LiveData<Double> getTotalLiveData() {
        return totalLiveData;
    }
}
