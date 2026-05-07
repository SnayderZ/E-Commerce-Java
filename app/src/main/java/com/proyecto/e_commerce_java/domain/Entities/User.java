package com.proyecto.e_commerce_java.domain.Entities;

public class User {

    private String token;

    public User(String token) {

        this.token = token;
    }



    public String getToken() {
        return token;
    }
}
