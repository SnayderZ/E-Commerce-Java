package com.proyecto.e_commerce_java.data.remote.ProfileApi;

public class UpdateProfileRequest {
    private final String firstName;
    private final String lastName;
    private final String email;
    private final String password;
    private final String profilePhoto;

    public UpdateProfileRequest(
            String firstName,
            String lastName,
            String email,
            String password,
            String profilePhoto
    ) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.profilePhoto = profilePhoto;
    }
}
