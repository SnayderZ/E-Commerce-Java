package com.proyecto.e_commerce_java.domain.UseCases;

import com.proyecto.e_commerce_java.domain.repositories.UserRepository;

public class RegisterUseCase {
    private final UserRepository userRepository;

    public RegisterUseCase(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void execute(
            String firstName,
            String lastName,
            String email,
            String password,
            String profilePhoto,
            UserRepository.RegisterCallback callback
    ) {
        userRepository.register(firstName, lastName, email, password, profilePhoto, callback);
    }
}
