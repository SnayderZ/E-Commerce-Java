package com.proyecto.e_commerce_java.presentation.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.proyecto.e_commerce_java.data.remote.ServicesApiClient.ApiService;
import com.proyecto.e_commerce_java.data.remote.ServicesApiClient.RetrofitClient;
import com.proyecto.e_commerce_java.data.repositories.Product.ProductRepositoryImpl;
import com.proyecto.e_commerce_java.domain.Entities.Product;
import com.proyecto.e_commerce_java.domain.UseCases.CartUseCase;
import com.proyecto.e_commerce_java.domain.UseCases.GetProductsUseCase;
import com.proyecto.e_commerce_java.domain.repositories.ProductRepository;

import java.util.List;

public class HomeViewModel extends ViewModel {
    private static final int DEFAULT_PAGE_NUMBER = 1;
    private static final int DEFAULT_PAGE_SIZE = 10;

    private final CartUseCase cartUseCase;
    private final GetProductsUseCase getProductsUseCase;
    private final MutableLiveData<List<Product>> apiProductsLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> errorLiveData = new MutableLiveData<>();

    public HomeViewModel() {
        ApiService apiService = RetrofitClient.getApiService();
        ProductRepository productRepository = new ProductRepositoryImpl(apiService);
        cartUseCase = new CartUseCase(productRepository);
        getProductsUseCase = new GetProductsUseCase(productRepository);
    }

    public void loadProducts(String token) {
        getProductsUseCase.execute(token, DEFAULT_PAGE_NUMBER, DEFAULT_PAGE_SIZE, new ProductRepository.ProductsCallback() {
            @Override
            public void onSuccess(List<Product> products) {
                apiProductsLiveData.setValue(products);
            }

            @Override
            public void onError(String error) {
                errorLiveData.setValue(error);
            }
        });
    }

    public void addProduct(Product product) {
        cartUseCase.addProduct(product);
    }

    public LiveData<List<Product>> getApiProductsLiveData() {
        return apiProductsLiveData;
    }

    public LiveData<String> getErrorLiveData() {
        return errorLiveData;
    }
}
