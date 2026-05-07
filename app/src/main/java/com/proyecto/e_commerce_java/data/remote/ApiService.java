package com.proyecto.e_commerce_java.data.remote;

import com.proyecto.e_commerce_java.domain.Entities.Producto;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiService {
    @POST("auth/login")
    Call<LoginResponse> login(@Body LoginRequest request);

    @GET("productos")
    Call<List<Producto>> getProductos();
}
