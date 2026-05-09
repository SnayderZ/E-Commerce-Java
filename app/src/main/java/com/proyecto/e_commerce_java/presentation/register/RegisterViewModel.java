package com.proyecto.e_commerce_java.presentation.register;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.proyecto.e_commerce_java.data.remote.ServicesApiClient.ApiService;
import com.proyecto.e_commerce_java.data.remote.ServicesApiClient.RetrofitClient;
import com.proyecto.e_commerce_java.data.repositories.User.UserRepositoryImpl;
import com.proyecto.e_commerce_java.domain.UseCases.RegisterUseCase;
import com.proyecto.e_commerce_java.domain.repositories.UserRepository;

public class RegisterViewModel extends ViewModel {
    private final RegisterUseCase registerUseCase;
    private final MutableLiveData<Boolean> successLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> errorLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> loadingLiveData = new MutableLiveData<>(false);

    public RegisterViewModel() {
        ApiService apiService = RetrofitClient.getApiService();
        UserRepository userRepository = new UserRepositoryImpl(apiService);
        registerUseCase = new RegisterUseCase(userRepository);
    }

    public void register(String firstName, String lastName, String email, String password, String profilePhoto) {
        loadingLiveData.setValue(true);
        registerUseCase.execute(firstName, lastName, email, password, profilePhoto, new UserRepository.RegisterCallback() {
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
