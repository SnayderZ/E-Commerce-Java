package com.proyecto.e_commerce_java.presentation.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.textfield.TextInputEditText;
import com.proyecto.e_commerce_java.R;
import com.proyecto.e_commerce_java.presentation.home.HomeActivity;
import com.proyecto.e_commerce_java.presentation.register.RegisterActivity;

public class LoginActivity extends AppCompatActivity {
    private static final String SESSION_PREFERENCES = "session_preferences";
    private static final String KEY_TOKEN = "token";
    private static final String KEY_FIRST_NAME = "profile_first_name";
    private static final String KEY_LAST_NAME = "profile_last_name";
    private static final String KEY_EMAIL = "profile_email";
    private static final String KEY_PROFILE_PHOTO = "profile_photo";

    private LoginViewModel viewModel;
    private TextInputEditText emailInput;
    private TextInputEditText passwordInput;
    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);
        loginButton = findViewById(R.id.loginButton);
        TextView createAccountText = findViewById(R.id.createAccountText);
        createAccountText.setOnClickListener(view ->
                startActivity(new Intent(this, RegisterActivity.class)));

        viewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        loginButton.setOnClickListener(view -> {
            String email = getInputText(emailInput);
            String password = getInputText(passwordInput);

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, R.string.login_required_fields, Toast.LENGTH_SHORT).show();
                return;
            }

            viewModel.login(email, password);
        });

        viewModel.getUserLiveData().observe(this, user -> {
            saveSession(user.getToken(), user.getFirstName(), user.getLastName(), user.getEmail(), user.getProfilePhoto());
            Intent intent = new Intent(this, HomeActivity.class);
            intent.putExtra(HomeActivity.EXTRA_TOKEN, user.getToken());
            intent.putExtra(HomeActivity.EXTRA_FIRST_NAME, user.getFirstName());
            intent.putExtra(HomeActivity.EXTRA_LAST_NAME, user.getLastName());
            intent.putExtra(HomeActivity.EXTRA_EMAIL, user.getEmail());
            intent.putExtra(HomeActivity.EXTRA_PROFILE_PHOTO, user.getProfilePhoto());
            startActivity(intent);
        });


        viewModel.getErrorLiveData().observe(this, error ->
                Toast.makeText(this, error, Toast.LENGTH_SHORT).show());

        viewModel.getLoadingLiveData().observe(this, loading ->
                loginButton.setEnabled(!loading));
    }

    private String getInputText(TextInputEditText input) {
        return input.getText() == null ? "" : input.getText().toString().trim();
    }

    private void saveSession(String token, String firstName, String lastName, String email, String profilePhoto) {
        SharedPreferences.Editor editor = getSharedPreferences(SESSION_PREFERENCES, MODE_PRIVATE).edit();
        editor.putString(KEY_TOKEN, safeValue(token));
        editor.putString(KEY_FIRST_NAME, safeValue(firstName));
        editor.putString(KEY_LAST_NAME, safeValue(lastName));
        editor.putString(KEY_EMAIL, safeValue(email));
        editor.putString(KEY_PROFILE_PHOTO, safeValue(profilePhoto));
        editor.apply();
    }

    private String safeValue(String value) {
        return value == null ? "" : value;
    }
}
