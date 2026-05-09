package com.proyecto.e_commerce_java.presentation.profile;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.proyecto.e_commerce_java.data.remote.ServicesApiClient.ApiService;
import com.proyecto.e_commerce_java.data.remote.ServicesApiClient.RetrofitClient;
import com.proyecto.e_commerce_java.data.repositories.User.UserRepositoryImpl;
import com.proyecto.e_commerce_java.domain.UseCases.UpdateProfileUseCase;
import com.proyecto.e_commerce_java.domain.repositories.UserRepository;

public class ProfileViewModel extends ViewModel {
    private final UpdateProfileUseCase updateProfileUseCase;
    private final MutableLiveData<Boolean> successLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> errorLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> loadingLiveData = new MutableLiveData<>(false);

    public ProfileViewModel() {
        ApiService apiService = RetrofitClient.getApiService();
        UserRepository userRepository = new UserRepositoryImpl(apiService);
        updateProfileUseCase = new UpdateProfileUseCase(userRepository);
    }

    public void updateProfile(
            String token,
            String firstName,
            String lastName,
            String email,
            String password,
            String profilePhoto
    ) {
        loadingLiveData.setValue(true);
        updateProfileUseCase.execute(token, firstName, lastName, email, password, profilePhoto, new UserRepository.UpdateProfileCallback() {
            @Override
            public void onSuccess() {
                loadingLiveData.setValue(false);
                successLiveData.setValue(true);
            }

            @Override
            public void onError(String error) {
                loadingLiveData.setValue(false);
                errorLiveData.setValue(error);
            }
        });
    }

    public LiveData<Boolean> getSuccessLiveData() {
        return successLiveData;
    }

    public LiveData<String> getErrorLiveData() {
        return errorLiveData;
    }

    public LiveData<Boolean> getLoadingLiveData() {
        return loadingLiveData;
    }
}
