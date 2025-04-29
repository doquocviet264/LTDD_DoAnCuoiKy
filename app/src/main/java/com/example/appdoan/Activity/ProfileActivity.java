package com.example.appdoan.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.appdoan.API.HTTPRequest;
import com.example.appdoan.API.HTTPService;
import com.example.appdoan.Container.Request.UpdateProfileRequest;
import com.example.appdoan.Container.Response.ApiResponse;
import com.example.appdoan.Container.Response.RegisterUserResponse;
import com.example.appdoan.R;
import com.example.appdoan.databinding.ActivityProfileBinding;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class ProfileActivity extends AppCompatActivity {

    private ActivityProfileBinding binding;
    private HTTPRequest httpRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initHttpRequest();
        loadUserProfile();
        setupListeners();
    }

    private void initHttpRequest() {
        Retrofit retrofit = HTTPService.getInstance();
        httpRequest = retrofit.create(HTTPRequest.class);
    }

    private void loadUserProfile() {
        httpRequest.getProfile().enqueue(new Callback<RegisterUserResponse>() {
            @Override
            public void onResponse(Call<RegisterUserResponse> call, Response<RegisterUserResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    RegisterUserResponse userResponse = response.body();
                    if (userResponse.isStatus()) {
                        binding.firstname.setText(userResponse.getUserInfoModel().getFirstname());
                        binding.lastname.setText(userResponse.getUserInfoModel().getLastname());
                        binding.avatar.setText(userResponse.getUserInfoModel().getAvatar());
                    }
                } else {
                    showToast("Không thể lấy thông tin người dùng!");
                }
            }

            @Override
            public void onFailure(Call<RegisterUserResponse> call, Throwable t) {
                showToast("Lỗi kết nối!");
            }
        });
    }

    private void setupListeners() {
        binding.btnBack.setOnClickListener(v -> onBackPressed());

        binding.btnUpdateProfile.setOnClickListener(v -> updateProfile());
    }

    private void updateProfile() {
        String firstName = binding.firstname.getText().toString().trim();
        String lastName = binding.lastname.getText().toString().trim();
        String avatar = binding.avatar.getText().toString().trim();

        if (firstName.isEmpty() || lastName.isEmpty() || avatar.isEmpty()) {
            showToast("Vui lòng điền đầy đủ thông tin!");
            return;
        }

        UpdateProfileRequest request = new UpdateProfileRequest(firstName, lastName, avatar);
        httpRequest.updateProfile(request).enqueue(new Callback<ApiResponse<Object>>() {
            @Override
            public void onResponse(Call<ApiResponse<Object>> call, Response<ApiResponse<Object>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<Object> apiResponse = response.body();
                    if (apiResponse.getStatus() == 200) {
                        showToast("Chỉnh sửa thông tin thành công!");
                        startActivity(new Intent(ProfileActivity.this, SettingFragment.class));
                        finish();
                    } else {
                        showToast("Không thành công. Vui lòng kiểm tra lại!");
                    }
                } else {
                    showToast("Đã có lỗi xảy ra!");
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Object>> call, Throwable t) {
                showToast("Lỗi kết nối!");
            }
        });
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
