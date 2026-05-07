package com.proyecto.e_commerce_java.domain.Entities;

public class User {
    private String email;
    private String nombre;
    private String token;

    public User(String email, String nombre, String token) {
        this.email = email;
        this.nombre = nombre;
        this.token = token;
    }

    public String getEmail() {
        return email;
    }

    public String getNombre() {
        return nombre;
    }

    public String getToken() {
        return token;
    }
}
