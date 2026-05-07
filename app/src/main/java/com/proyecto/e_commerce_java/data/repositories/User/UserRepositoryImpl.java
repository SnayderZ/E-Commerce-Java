package com.proyecto.e_commerce_java.data.repositories.User;

import com.proyecto.e_commerce_java.data.remote.ServicesApiClient.ApiService;
import com.proyecto.e_commerce_java.data.remote.LoginApi.LoginRequest;
import com.proyecto.e_commerce_java.data.remote.LoginApi.LoginResponse;
import com.proyecto.e_commerce_java.domain.Entities.User;
import com.proyecto.e_commerce_java.domain.repositories.UserRepository;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserRepositoryImpl implements UserRepository {
    private final ApiService apiService;

    public UserRepositoryImpl(ApiService apiService) {
        this.apiService = apiService;
    }

    @Override
    public void login(String email, String password, LoginCallback callback) {
        LoginRequest request = new LoginRequest(email, password);

        apiService.login(request).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    User user = response.body().toDomain();
                    if (user.getToken() == null || user.getToken().trim().isEmpty()) {
                        callback.onError("El servidor no devolvio un token de autenticacion");
                        return;
                    }

                    callback.onSuccess(user);
                } else {
                    callback.onError("Credenciales invalidas");
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable throwable) {
                callback.onError(throwable.getMessage());
            }
        });
    }
}
