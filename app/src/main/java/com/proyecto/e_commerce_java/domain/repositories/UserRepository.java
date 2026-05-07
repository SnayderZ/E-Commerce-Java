package com.proyecto.e_commerce_java.domain.repositories;

import com.proyecto.e_commerce_java.domain.Entities.User;

public interface UserRepository {
    void login(String email, String password, LoginCallback callback);

    interface LoginCallback {
        void onSuccess(User user);

        void onError(String error);
    }
}
