package com.proyecto.e_commerce_java.presentation.register;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.proyecto.e_commerce_java.R;

public class RegisterActivity extends AppCompatActivity {
    private TextInputEditText firstNameInput;
    private TextInputEditText lastNameInput;
    private TextInputEditText emailInput;
    private TextInputEditText passwordInput;
    private TextInputEditText confirmPasswordInput;
    private LinearLayout profilePhotoOptionsContainer;
    private Button createAccountButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        firstNameInput = findViewById(R.id.registerFirstNameInput);
        lastNameInput = findViewById(R.id.registerLastNameInput);
        emailInput = findViewById(R.id.registerEmailInput);
        passwordInput = findViewById(R.id.registerPasswordInput);
        confirmPasswordInput = findViewById(R.id.registerConfirmPasswordInput);
        profilePhotoOptionsContainer = findViewById(R.id.profilePhotoOptionsContainer);

        Button choosePhotoButton = findViewById(R.id.chooseProfilePhotoButton);
        Button skipPhotoButton = findViewById(R.id.skipProfilePhotoButton);
        createAccountButton = findViewById(R.id.createAccountButton);

        choosePhotoButton.setOnClickListener(view ->
                completeRegistration(R.string.profile_photo_placeholder));

        skipPhotoButton.setOnClickListener(view ->
                completeRegistration(R.string.profile_photo_skipped));

        createAccountButton.setOnClickListener(view -> validateForm());
    }

    private void validateForm() {
        String password = getText(passwordInput);
        String confirmPassword = getText(confirmPasswordInput);

        if (getText(firstNameInput).isEmpty()
                || getText(lastNameInput).isEmpty()
                || getText(emailInput).isEmpty()
                || password.isEmpty()
                || confirmPassword.isEmpty()) {
            Toast.makeText(this, R.string.complete_required_fields, Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, R.string.passwords_do_not_match, Toast.LENGTH_SHORT).show();
            return;
        }

        showProfilePhotoOptions();
    }

    private void showProfilePhotoOptions() {
        createAccountButton.setEnabled(false);
        createAccountButton.setText(R.string.account_data_loaded);
        profilePhotoOptionsContainer.setVisibility(View.VISIBLE);
        Toast.makeText(this, R.string.register_placeholder_success, Toast.LENGTH_SHORT).show();
    }

    private void completeRegistration(int messageResId) {
        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show();
        finish();
    }

    private String getText(TextInputEditText input) {
        return input.getText() == null ? "" : input.getText().toString().trim();
    }
}
