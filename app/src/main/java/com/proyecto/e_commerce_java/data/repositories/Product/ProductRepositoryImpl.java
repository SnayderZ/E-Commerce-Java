package com.proyecto.e_commerce_java.data.repositories.Product;

import com.proyecto.e_commerce_java.data.remote.ServicesApiClient.ApiService;
import com.proyecto.e_commerce_java.domain.Entities.Producto;
import com.proyecto.e_commerce_java.domain.repositories.ProductoRepository;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductRepositoryImpl implements ProductoRepository {
    private static final List<Producto> carrito = new ArrayList<>();

    private final ApiService apiService;

    public ProductRepositoryImpl(ApiService apiService) {
        this.apiService = apiService;
    }

    @Override
    public List<Producto> getProductos() {
        return carrito;
    }

    @Override
    public void addProducto(Producto producto) {
        carrito.add(producto);
    }

    @Override
    public void fetchProductos(ProductosCallback callback) {
        apiService.getProductos().enqueue(new Callback<List<Producto>>() {
            @Override
            public void onResponse(Call<List<Producto>> call, Response<List<Producto>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("No se pudieron obtener los productos");
                }
            }

            @Override
            public void onFailure(Call<List<Producto>> call, Throwable throwable) {
                callback.onError(throwable.getMessage());
            }
        });
    }
}
