package com.proyecto.e_commerce_java.data.remote.LoginApi;

import com.google.gson.annotations.SerializedName;
import com.proyecto.e_commerce_java.domain.Entities.User;

public class LoginResponse {
    @SerializedName("email")
    private String email;

    @SerializedName("nombre")
    private String nombre;

    @SerializedName(value = "token", alternate = {"accessToken", "jwtToken", "jwt", "bearerToken"})
    private String token;

    public User toDomain() {
        return new User(token);
    }
}
