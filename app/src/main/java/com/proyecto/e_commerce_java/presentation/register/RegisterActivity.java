package com.proyecto.e_commerce_java.presentation.register;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.textfield.TextInputEditText;
import com.proyecto.e_commerce_java.R;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class RegisterActivity extends AppCompatActivity {
    private TextInputEditText firstNameInput;
    private TextInputEditText lastNameInput;
    private TextInputEditText emailInput;
    private TextInputEditText passwordInput;
    private TextInputEditText confirmPasswordInput;
    private LinearLayout profilePhotoOptionsContainer;
    private Button createAccountButton;
    private RegisterViewModel viewModel;
    private ActivityResultLauncher<String> profilePhotoPicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        viewModel = new ViewModelProvider(this).get(RegisterViewModel.class);
        configureProfilePhotoPicker();
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
                profilePhotoPicker.launch("image/*"));

        skipPhotoButton.setOnClickListener(view ->
                registerUser(null));

        createAccountButton.setOnClickListener(view -> validateForm());
        configureObservers();
    }

    private void configureProfilePhotoPicker() {
        profilePhotoPicker = registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
            if (uri == null) {
                return;
            }

            String encodedPhoto = encodeProfilePhoto(uri);
            if (encodedPhoto == null) {
                Toast.makeText(this, R.string.profile_photo_error, Toast.LENGTH_SHORT).show();
                return;
            }

            registerUser(encodedPhoto);
        });
    }

    private void configureObservers() {
        viewModel.getSuccessLiveData().observe(this, success -> {
            if (Boolean.TRUE.equals(success)) {
                Toast.makeText(this, R.string.register_success, Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        viewModel.getErrorLiveData().observe(this, error -> {
            if (error != null && !error.trim().isEmpty()) {
                Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
            }
        });

        viewModel.getLoadingLiveData().observe(this, loading -> {
            boolean isLoading = Boolean.TRUE.equals(loading);
            createAccountButton.setEnabled(!isLoading && profilePhotoOptionsContainer.getVisibility() != View.VISIBLE);
        });
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

    private void registerUser(String profilePhoto) {
        viewModel.register(
                getText(firstNameInput),
                getText(lastNameInput),
                getText(emailInput),
                getText(passwordInput),
                profilePhoto
        );
    }

    private String getText(TextInputEditText input) {
        return input.getText() == null ? "" : input.getText().toString().trim();
    }

    private String encodeProfilePhoto(Uri imageUri) {
        try (InputStream inputStream = getContentResolver().openInputStream(imageUri)) {
            if (inputStream == null) {
                return null;
            }

            Bitmap originalBitmap = BitmapFactory.decodeStream(inputStream);
            if (originalBitmap == null) {
                return null;
            }

            Bitmap resizedBitmap = resizeBitmap(originalBitmap, 512);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 80, outputStream);

            return Base64.encodeToString(outputStream.toByteArray(), Base64.NO_WRAP);
        } catch (Exception exception) {
            return null;
        }
    }

    private Bitmap resizeBitmap(Bitmap bitmap, int maxSize) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        if (width <= maxSize && height <= maxSize) {
            return bitmap;
        }

        float ratio = Math.min((float) maxSize / width, (float) maxSize / height);
        int targetWidth = Math.round(width * ratio);
        int targetHeight = Math.round(height * ratio);

        return Bitmap.createScaledBitmap(bitmap, targetWidth, targetHeight, true);
    }
}
