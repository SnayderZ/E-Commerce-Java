package com.proyecto.e_commerce_java.data.remote.RegisterApi;

public class RegisterRequest {
    private final String email;
    private final String password;
    private final String firstName;
    private final String lastName;
    private final String profilePhoto;

    public RegisterRequest(String email, String password, String firstName, String lastName, String profilePhoto) {
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.profilePhoto = profilePhoto;
    }
}
