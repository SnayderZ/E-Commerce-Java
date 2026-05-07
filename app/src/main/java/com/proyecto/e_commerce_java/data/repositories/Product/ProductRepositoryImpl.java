package com.proyecto.e_commerce_java.data.repositories.Product;

import com.proyecto.e_commerce_java.data.remote.ProductApi.ProductoDto;
import com.proyecto.e_commerce_java.data.remote.ProductApi.ProductosResponse;
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
    public List<Producto> getProductosCarrito() {
        return carrito;
    }

    @Override
    public void addProducto(Producto producto) {
        carrito.add(producto);
    }

    @Override
    public void fetchProductos(String token, int pageNumber, int pageSize, ProductosCallback callback) {
        apiService.getProductos(formatBearerToken(token), pageNumber, pageSize).enqueue(new Callback<ProductosResponse>() {
            @Override
            public void onResponse(Call<ProductosResponse> call, Response<ProductosResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(mapProductos(response.body()));
                } else {
                    callback.onError("Error " + response.code() + ": " + response.message());
                }
            }

            @Override
            public void onFailure(Call<ProductosResponse> call, Throwable throwable) {
                callback.onError(throwable.getMessage());
            }
        });
    }

    private String formatBearerToken(String token) {
        if (token == null || token.trim().isEmpty()) {
            return "";
        }

        String cleanToken = token.trim();
        if (cleanToken.toLowerCase().startsWith("bearer ")) {
            return cleanToken;
        }

        return "Bearer " + cleanToken;
    }

    private List<Producto> mapProductos(ProductosResponse response) {
        List<Producto> productos = new ArrayList<>();
        if (response.getItems() == null) {
            return productos;
        }

        for (ProductoDto productoDto : response.getItems()) {
            if (!productoDto.isEstado()) {
                continue;
            }

            productos.add(new Producto(
                    productoDto.getId(),
                    productoDto.getName(),
                    productoDto.getUnitPrice(),
                    productoDto.getUnitsInStock(),
                    productoDto.getImagen()
            ));
        }

        return productos;
    }
}
