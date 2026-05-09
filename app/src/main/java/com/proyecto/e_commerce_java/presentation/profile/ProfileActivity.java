package com.proyecto.e_commerce_java.presentation.profile;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Base64;
import android.widget.ImageView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.proyecto.e_commerce_java.R;
import com.proyecto.e_commerce_java.presentation.login.LoginActivity;
import com.proyecto.e_commerce_java.presentation.navigation.BottomNavigationHelper;

public class ProfileActivity extends AppCompatActivity {
    private static final String SETTINGS_NAME = "app_settings";
    private static final String DARK_MODE_KEY = "dark_mode_enabled";
    private static final String SESSION_PREFERENCES = "session_preferences";
    private static final String KEY_TOKEN = "token";
    private static final String KEY_FIRST_NAME = "profile_first_name";
    private static final String KEY_LAST_NAME = "profile_last_name";
    private static final String KEY_EMAIL = "profile_email";
    private static final String KEY_PROFILE_PHOTO = "profile_photo";

    private ProfileViewModel viewModel;
    private EditText firstNameInput;
    private EditText lastNameInput;
    private EditText profileEmailInput;
    private EditText profilePasswordInput;
    private Button editProfileButton;
    private Button saveProfileButton;
    private Button cancelEditProfileButton;
    private String token;
    private ImageView profilePhotoImage;
    private Button editProfilePhotoButton;
    private ActivityResultLauncher<String> profilePhotoPicker;
    private String selectedProfilePhoto;
    private String originalFirstName = "";
    private String originalLastName = "";
    private String originalEmail = "";
    private String originalProfilePhoto = "";
    private boolean editingProfile;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        viewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        token = getSessionPreferences().getString(KEY_TOKEN, "");

        configureProfilePhotoPicker();
        bindViews();
        loadStoredProfile();
        setupActions();
        configureViewModel();
        setEditMode(false);
        setupBottomNavigation();
    }

    private void bindViews() {
        profilePhotoImage = findViewById(R.id.profilePhotoImage);
        editProfilePhotoButton = findViewById(R.id.editProfilePhotoButton);
        firstNameInput = findViewById(R.id.firstNameInput);
        lastNameInput = findViewById(R.id.lastNameInput);
        profileEmailInput = findViewById(R.id.profileEmailInput);
        profilePasswordInput = findViewById(R.id.profilePasswordInput);
        editProfileButton = findViewById(R.id.editProfileButton);
        saveProfileButton = findViewById(R.id.saveProfileButton);
        cancelEditProfileButton = findViewById(R.id.cancelEditProfileButton);
    }

    private void setupActions() {
        Button recentPurchasesButton = findViewById(R.id.recentPurchasesButton);
        Button purchaseHistoryButton = findViewById(R.id.purchaseHistoryButton);
        Button logoutButton = findViewById(R.id.logoutButton);
        SwitchCompat darkModeSwitch = findViewById(R.id.darkModeSwitch);

        SharedPreferences preferences = getSharedPreferences(SETTINGS_NAME, MODE_PRIVATE);
        darkModeSwitch.setChecked(preferences.getBoolean(DARK_MODE_KEY, false));

        editProfileButton.setOnClickListener(view -> setEditMode(true));

        cancelEditProfileButton.setOnClickListener(view -> {
            loadStoredProfile();
            setEditMode(false);
        });

        saveProfileButton.setOnClickListener(view -> saveProfile());
        editProfilePhotoButton.setOnClickListener(view ->
                profilePhotoPicker.launch("image/*"));


        recentPurchasesButton.setOnClickListener(view ->
                Toast.makeText(this, R.string.recent_purchases_placeholder, Toast.LENGTH_SHORT).show());

        purchaseHistoryButton.setOnClickListener(view ->
                Toast.makeText(this, R.string.purchase_history_placeholder, Toast.LENGTH_SHORT).show());

        darkModeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            preferences.edit().putBoolean(DARK_MODE_KEY, isChecked).apply();

            AppCompatDelegate.setDefaultNightMode(
                    isChecked
                            ? AppCompatDelegate.MODE_NIGHT_YES
                            : AppCompatDelegate.MODE_NIGHT_NO
            );
        });

        logoutButton.setOnClickListener(view -> logout());
    }
    @Override
    protected void onResume() {
        super.onResume();

        if (firstNameInput != null && !editingProfile) {
            loadStoredProfile();
        }
    }


    private void configureViewModel() {
        viewModel.getSuccessLiveData().observe(this, success -> {
            if (!Boolean.TRUE.equals(success)) {
                return;
            }

            saveProfileLocally();
            profilePasswordInput.setText("");
            setEditMode(false);
            Toast.makeText(this, R.string.profile_updated, Toast.LENGTH_SHORT).show();
        });

        viewModel.getErrorLiveData().observe(this, error -> {
            String message = error == null || error.trim().isEmpty()
                    ? getString(R.string.profile_update_error)
                    : error;
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        });

        viewModel.getLoadingLiveData().observe(this, loading -> {
            boolean isLoading = Boolean.TRUE.equals(loading);
            editProfileButton.setEnabled(!isLoading);
            saveProfileButton.setEnabled(!isLoading);
            cancelEditProfileButton.setEnabled(!isLoading);
        });
    }

    private void saveProfile() {
        if (token == null || token.trim().isEmpty()) {
            Toast.makeText(this, R.string.no_session_token, Toast.LENGTH_SHORT).show();
            return;
        }

        String firstName = getChangedValue(getText(firstNameInput), originalFirstName);
        String lastName = getChangedValue(getText(lastNameInput), originalLastName);
        String email = getChangedValue(getText(profileEmailInput), originalEmail);
        String password = getText(profilePasswordInput).isEmpty()
                ? null
                : getText(profilePasswordInput);
        String profilePhoto = getChangedValue(selectedProfilePhoto, originalProfilePhoto);

        if (firstName == null
                && lastName == null
                && email == null
                && password == null
                && profilePhoto == null) {
            Toast.makeText(this, R.string.no_profile_changes, Toast.LENGTH_SHORT).show();
            return;
        }

        viewModel.updateProfile(token, firstName, lastName, email, password, profilePhoto);
    }

    private void saveProfileLocally() {
        getSessionPreferences().edit()
                .putString(KEY_FIRST_NAME, getText(firstNameInput))
                .putString(KEY_LAST_NAME, getText(lastNameInput))
                .putString(KEY_EMAIL, getText(profileEmailInput))
                .putString(KEY_PROFILE_PHOTO, selectedProfilePhoto == null ? "" : selectedProfilePhoto)
                .apply();
        originalFirstName = getText(firstNameInput);
        originalLastName = getText(lastNameInput);
        originalEmail = getText(profileEmailInput);
        originalProfilePhoto = selectedProfilePhoto == null ? "" : selectedProfilePhoto;
    }

    private void loadStoredProfile() {
        SharedPreferences preferences = getSessionPreferences();

        originalFirstName = preferences.getString(KEY_FIRST_NAME, "");
        originalLastName = preferences.getString(KEY_LAST_NAME, "");
        originalEmail = preferences.getString(KEY_EMAIL, "");
        originalProfilePhoto = preferences.getString(KEY_PROFILE_PHOTO, "");

        firstNameInput.setText(preferences.getString(KEY_FIRST_NAME, ""));
        lastNameInput.setText(preferences.getString(KEY_LAST_NAME, ""));
        profileEmailInput.setText(preferences.getString(KEY_EMAIL, ""));
        profilePasswordInput.setText("");

        selectedProfilePhoto = preferences.getString(KEY_PROFILE_PHOTO, "");
        loadProfilePhoto(selectedProfilePhoto);

    }
    private String getChangedValue(String currentValue, String originalValue) {
        if (currentValue == null) {
            return null;
        }

        String cleanCurrent = currentValue.trim();
        String cleanOriginal = originalValue == null ? "" : originalValue.trim();

        if (cleanCurrent.equals(cleanOriginal)) {
            return null;
        }

        return cleanCurrent;
    }

    private void setEditMode(boolean editing) {
        editingProfile = editing;
        editProfilePhotoButton.setVisibility(editing ? View.VISIBLE : View.GONE);
        firstNameInput.setEnabled(editing);
        lastNameInput.setEnabled(editing);
        profileEmailInput.setEnabled(editing);
        profilePasswordInput.setEnabled(editing);
        editProfileButton.setVisibility(editing ? View.GONE : View.VISIBLE);
        saveProfileButton.setVisibility(editing ? View.VISIBLE : View.GONE);
        cancelEditProfileButton.setVisibility(editing ? View.VISIBLE : View.GONE);
    }

    private String getText(EditText input) {
        return input.getText() == null ? "" : input.getText().toString().trim();
    }

    private SharedPreferences getSessionPreferences() {
        return getSharedPreferences(SESSION_PREFERENCES, MODE_PRIVATE);
    }

    private void logout() {
        getSessionPreferences().edit().clear().apply();

        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private void setupBottomNavigation() {
        BottomNavigationView bottomNavigation = findViewById(R.id.bottomNavigation);
        BottomNavigationHelper.setup(this, bottomNavigation, R.id.nav_account);
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

            selectedProfilePhoto = encodedPhoto;
            loadProfilePhoto(selectedProfilePhoto);
            Toast.makeText(this, R.string.profile_photo_updated, Toast.LENGTH_SHORT).show();
        });
    }

    private String encodeProfilePhoto(Uri imageUri) {
        try (InputStream inputStream = getContentResolver().openInputStream(imageUri)) {
            if (inputStream == null) {
                return null;
            }

            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            if (bitmap == null) {
                return null;
            }

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 75, outputStream);
            return Base64.encodeToString(outputStream.toByteArray(), Base64.NO_WRAP);
        } catch (Exception exception) {
            return null;
        }
    }

    private void loadProfilePhoto(String profilePhoto) {
        if (profilePhoto == null || profilePhoto.trim().isEmpty()) {
            profilePhotoImage.setImageResource(R.drawable.ic_launcher_background);
            return;
        }

        try {
            byte[] imageBytes = Base64.decode(profilePhoto, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
            profilePhotoImage.setImageBitmap(bitmap);
        } catch (Exception exception) {
            profilePhotoImage.setImageResource(R.drawable.ic_launcher_background);
        }
    }

}
