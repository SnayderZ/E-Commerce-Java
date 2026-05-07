package com.proyecto.e_commerce_java.presentation.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.proyecto.e_commerce_java.data.remote.ApiService;
import com.proyecto.e_commerce_java.data.remote.RetrofitClient;
import com.proyecto.e_commerce_java.data.repositories.UserRepositoryImpl;
import com.proyecto.e_commerce_java.domain.Entities.User;
import com.proyecto.e_commerce_java.domain.UseCases.LoginUseCase;
import com.proyecto.e_commerce_java.domain.repositories.UserRepository;

public class LoginViewModel extends ViewModel {
    private final LoginUseCase loginUseCase;
    private final MutableLiveData<User> userLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> errorLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> loadingLiveData = new MutableLiveData<>(false);

    public LoginViewModel() {
        ApiService apiService = RetrofitClient.getApiService();
        UserRepository userRepository = new UserRepositoryImpl(apiService);
        loginUseCase = new LoginUseCase(userRepository);
    }

    public void login(String email, String password) {
        loadingLiveData.setValue(true);

        loginUseCase.execute(email, password, new UserRepository.LoginCallback() {
            @Override
            public void onSuccess(User user) {
                loadingLiveData.setValue(false);
                userLiveData.setValue(user);
            }

            @Override
            public void onError(String error) {
                loadingLiveData.setValue(false);
                errorLiveData.setValue(error);
            }
        });
    }

    public LiveData<User> getUserLiveData() {
        return userLiveData;
    }

    public LiveData<String> getErrorLiveData() {
        return errorLiveData;
    }

    public LiveData<Boolean> getLoadingLiveData() {
        return loadingLiveData;
    }
}
