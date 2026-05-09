package com.proyecto.e_commerce_java.data.repositories.User;

import com.proyecto.e_commerce_java.data.remote.ProfileApi.UpdateProfileRequest;
import com.proyecto.e_commerce_java.data.remote.ServicesApiClient.ApiService;
import com.proyecto.e_commerce_java.data.remote.LoginApi.LoginRequest;
import com.proyecto.e_commerce_java.data.remote.LoginApi.LoginResponse;
import com.proyecto.e_commerce_java.data.remote.RegisterApi.RegisterRequest;
import com.proyecto.e_commerce_java.domain.Entities.User;
import com.proyecto.e_commerce_java.domain.repositories.UserRepository;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.Locale;

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

    @Override
    public void register(String firstName, String lastName, String email, String password, String profilePhoto, RegisterCallback callback) {
        RegisterRequest request = new RegisterRequest(email, password, firstName, lastName, profilePhoto);

        apiService.register(request).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess();
                } else {
                    callback.onError("No se pudo crear la cuenta: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable throwable) {
                callback.onError(throwable.getMessage());
            }
        });
    }

    @Override
    public void updateProfile(
            String token,
            String firstName,
            String lastName,
            String email,
            String password,
            String profilePhoto,
            UpdateProfileCallback callback
    ) {
        UpdateProfileRequest request = new UpdateProfileRequest(
                firstName,
                lastName,
                email,
                password,
                profilePhoto
        );

        apiService.updateProfile(formatBearerToken(token), request)
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            callback.onSuccess();
                        } else {
                            callback.onError("No se pudo actualizar el perfil: " + response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable throwable) {
                        callback.onError(throwable.getMessage());
                    }
                });
    }

    private String formatBearerToken(String token) {
        if (token == null || token.trim().isEmpty()) {
            return "";
        }

        String cleanToken = token.trim();
        if (cleanToken.toLowerCase(Locale.ROOT).startsWith("bearer ")) {
            return cleanToken;
        }

        return "Bearer " + cleanToken;
    }

}
