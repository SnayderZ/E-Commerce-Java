package com.proyecto.e_commerce_java.presentation.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.proyecto.e_commerce_java.data.remote.ServicesApiClient.ApiService;
import com.proyecto.e_commerce_java.data.remote.ServicesApiClient.RetrofitClient;
import com.proyecto.e_commerce_java.data.repositories.Product.ProductRepositoryImpl;
import com.proyecto.e_commerce_java.domain.Entities.Producto;
import com.proyecto.e_commerce_java.domain.UseCases.CarritoUseCase;
import com.proyecto.e_commerce_java.domain.repositories.ProductoRepository;

import java.util.List;

public class HomeViewModel extends ViewModel {
    private final CarritoUseCase carritoUseCase;
    private final MutableLiveData<List<Producto>> productosApiLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> errorLiveData = new MutableLiveData<>();

    public HomeViewModel() {
        ApiService apiService = RetrofitClient.getApiService();
        ProductoRepository productoRepository = new ProductRepositoryImpl(apiService);
        carritoUseCase = new CarritoUseCase(productoRepository);
    }

    public void cargarProductos() {
        carritoUseCase.obtenerProductosApi(new ProductoRepository.ProductosCallback() {
            @Override
            public void onSuccess(List<Producto> productos) {
                productosApiLiveData.setValue(productos);
            }

            @Override
            public void onError(String error) {
                errorLiveData.setValue(error);
            }
        });
    }

    public void agregarProducto(Producto producto) {
        carritoUseCase.agregarProducto(producto);
    }

    public LiveData<List<Producto>> getProductosApiLiveData() {
        return productosApiLiveData;
    }

    public LiveData<String> getErrorLiveData() {
        return errorLiveData;
    }
}
