package com.proyecto.e_commerce_java.domain.UseCases;

import com.proyecto.e_commerce_java.domain.repositories.UserRepository;

public class UpdateProfileUseCase {
    private final UserRepository userRepository;

    public UpdateProfileUseCase(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void execute(
            String token,
            String firstName,
            String lastName,
            String email,
            String password,
            String profilePhoto,
            UserRepository.UpdateProfileCallback callback
    ) {
        userRepository.updateProfile(token, firstName, lastName, email, password, profilePhoto, callback);
    }
}
