package com.proyecto.e_commerce_java.domain.Entities;

public class User {

    private final String token;
    private final String firstName;
    private final String lastName;
    private final String email;
    private final String profilePhoto;

    public User(String token, String firstName, String lastName, String email, String profilePhoto) {
        this.token = token;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.profilePhoto = profilePhoto;
    }

    public String getToken() {
        return token;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getProfilePhoto() {
        return profilePhoto;
    }

}
