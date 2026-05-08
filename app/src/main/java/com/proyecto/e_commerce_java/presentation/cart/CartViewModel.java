package com.proyecto.e_commerce_java.presentation.cart;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.proyecto.e_commerce_java.data.remote.ServicesApiClient.ApiService;
import com.proyecto.e_commerce_java.data.remote.ServicesApiClient.RetrofitClient;
import com.proyecto.e_commerce_java.data.repositories.Product.ProductRepositoryImpl;
import com.proyecto.e_commerce_java.domain.Entities.Product;
import com.proyecto.e_commerce_java.domain.UseCases.CartUseCase;
import com.proyecto.e_commerce_java.domain.repositories.ProductRepository;

import java.util.List;

public class CartViewModel extends ViewModel {
    private final CartUseCase cartUseCase;
    private final MutableLiveData<List<Product>> cartLiveData = new MutableLiveData<>();
    private final MutableLiveData<Double> totalLiveData = new MutableLiveData<>(0.0);

    public CartViewModel() {
        ApiService apiService = RetrofitClient.getApiService();
        ProductRepository productRepository = new ProductRepositoryImpl(apiService);
        cartUseCase = new CartUseCase(productRepository);
    }

    public void loadCart() {
        cartLiveData.setValue(cartUseCase.getCartProducts());
        totalLiveData.setValue(cartUseCase.calculateTotal());
    }

    public LiveData<List<Product>> getCartLiveData() {
        return cartLiveData;
    }

    public LiveData<Double> getTotalLiveData() {
        return totalLiveData;
    }
}
