package com.proyecto.e_commerce_java.data.remote.LoginApi;

import com.google.gson.annotations.SerializedName;
import com.proyecto.e_commerce_java.domain.Entities.User;

public class LoginResponse {
    @SerializedName("email")
    private String email;

    @SerializedName(value = "firstName", alternate = {"name", "nombre"})
    private String firstName;

    @SerializedName(value = "lastName", alternate = {"apellido"})
    private String lastName;

    @SerializedName(value = "token", alternate = {"accessToken", "jwtToken", "jwt", "bearerToken"})
    private String token;

    @SerializedName("profilePhoto")
    private String profilePhoto;

    public User toDomain() {
        return new User(token, firstName, lastName, email, profilePhoto);
    }

}
