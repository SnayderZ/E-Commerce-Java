package com.proyecto.e_commerce_java.data.remote.ServicesApiClient;

import com.proyecto.e_commerce_java.data.remote.LoginApi.LoginRequest;
import com.proyecto.e_commerce_java.data.remote.LoginApi.LoginResponse;
import com.proyecto.e_commerce_java.data.remote.ProductApi.ProductsResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiService {
    @POST("user/Login")
    Call<LoginResponse> login(@Body LoginRequest request);

    @GET("products")
    Call<ProductsResponse> getProducts(
            @Header("Authorization") String token,
            @Query("pageNumber") int pageNumber,
            @Query("pageSize") int pageSize
    );
}
