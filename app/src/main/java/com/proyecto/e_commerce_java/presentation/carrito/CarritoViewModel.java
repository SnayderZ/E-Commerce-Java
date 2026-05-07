package com.proyecto.e_commerce_java.presentation.carrito;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.proyecto.e_commerce_java.data.remote.ApiService;
import com.proyecto.e_commerce_java.data.remote.RetrofitClient;
import com.proyecto.e_commerce_java.data.repositories.ProductRepositoryImpl;
import com.proyecto.e_commerce_java.domain.Entities.Producto;
import com.proyecto.e_commerce_java.domain.UseCases.CarritoUseCase;
import com.proyecto.e_commerce_java.domain.repositories.ProductoRepository;

import java.util.List;

public class CarritoViewModel extends ViewModel {
    private final CarritoUseCase carritoUseCase;
    private final MutableLiveData<List<Producto>> productosApiLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<Producto>> carritoLiveData = new MutableLiveData<>();
    private final MutableLiveData<Double> totalLiveData = new MutableLiveData<>(0.0);
    private final MutableLiveData<String> errorLiveData = new MutableLiveData<>();

    public CarritoViewModel() {
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
        actualizarCarrito();
    }

    public void actualizarCarrito() {
        carritoLiveData.setValue(carritoUseCase.obtenerProductosCarrito());
        totalLiveData.setValue(carritoUseCase.calcularTotal());
    }

    public LiveData<List<Producto>> getProductosApiLiveData() {
        return productosApiLiveData;
    }

    public LiveData<List<Producto>> getCarritoLiveData() {
        return carritoLiveData;
    }

    public LiveData<Double> getTotalLiveData() {
        return totalLiveData;
    }

    public LiveData<String> getErrorLiveData() {
        return errorLiveData;
    }
}
