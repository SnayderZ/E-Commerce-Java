package com.proyecto.e_commerce_java.domain.UseCases;

import com.proyecto.e_commerce_java.domain.repositories.UserRepository;

public class LoginUseCase {
    private final UserRepository userRepository;

    public LoginUseCase(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void execute(String email, String password, UserRepository.LoginCallback callback) {
        userRepository.login(email, password, callback);
    }
}
