package com.proyecto.e_commerce_java.data.remote.LoginApi;

import com.proyecto.e_commerce_java.domain.Entities.User;

public class LoginResponse {
    private String email;
    private String nombre;
    private String token;

    public User toDomain() {
        return new User(token);
    }
}
