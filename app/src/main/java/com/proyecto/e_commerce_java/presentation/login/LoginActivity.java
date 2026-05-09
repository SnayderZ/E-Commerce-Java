package com.proyecto.e_commerce_java.presentation.login;

import android.content.Intent;
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
            Intent intent = new Intent(this, HomeActivity.class);
            intent.putExtra(HomeActivity.EXTRA_TOKEN, user.getToken());
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
}
