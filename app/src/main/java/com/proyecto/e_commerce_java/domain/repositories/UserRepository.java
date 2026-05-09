package com.proyecto.e_commerce_java.domain.repositories;

import com.proyecto.e_commerce_java.domain.Entities.User;

public interface UserRepository {
    void login(String email, String password, LoginCallback callback);

    void register(String firstName, String lastName, String email, String password, String profilePhoto, RegisterCallback callback);

    interface LoginCallback {
        void onSuccess(User user);

        void onError(String error);
    }

    interface RegisterCallback {
        void onSuccess();

        void onError(String error);
    }
    void updateProfile(
            String token,
            String firstName,
            String lastName,
            String email,
            String password,
            String profilePhoto,
            UpdateProfileCallback callback
    );

    interface UpdateProfileCallback {
        void onSuccess();

        void onError(String error);
    }

}
