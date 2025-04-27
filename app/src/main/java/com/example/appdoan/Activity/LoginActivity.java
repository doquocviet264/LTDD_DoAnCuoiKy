package com.example.appdoan.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.appdoan.API.HTTPRequest;
import com.example.appdoan.API.HTTPService;
import com.example.appdoan.Container.Request.LoginRequest;
import com.example.appdoan.Container.Response.ApiResponse;
import com.example.appdoan.Container.Response.LoginResponse;
import com.example.appdoan.Helper.LoadingDialog;
import com.example.appdoan.Helper.MySharedPreferences;
import com.example.appdoan.R;
import com.example.appdoan.databinding.ActivityLoginBinding;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding binding;
    private LoadingDialog loadingDialog;
    private HTTPRequest httpRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init();
        initListeners();
    }

    private void init() {
        MySharedPreferences.init(getApplicationContext());
        loadingDialog = new LoadingDialog(this);
        Retrofit retrofit = HTTPService.getInstance();
        httpRequest = retrofit.create(HTTPRequest.class);
    }

    private void initListeners() {

//        binding.forgotpass.setOnClickListener(view ->
//                startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class))
//        );

        binding.txtResgiter.setOnClickListener(view ->
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class))
        );

        binding.btnLogin.setOnClickListener(view -> handleLogin());
    }

    private void handleLogin() {
        String username = binding.edtUsername.getText().toString().trim();
        String password = binding.edtPassword.getText().toString().trim();

        if (username.isEmpty() || password.isEmpty()) {
            showToast("Tên đăng nhập hoặc mật khẩu không được để trống!");
            return;
        }

        loadingDialog.startLoadingDialog();
        loadingDialog.setDialogDuration(2000);

        LoginRequest loginRequest = new LoginRequest(username, password);
        Call<ApiResponse<LoginResponse>> call = httpRequest.login(loginRequest);

        call.enqueue(new Callback<ApiResponse<LoginResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<LoginResponse>> call, Response<ApiResponse<LoginResponse>> response) {
                loadingDialog.dismissDialog();
                if (response.isSuccessful() && response.body() != null) {
                    handleLoginResponse(response.body());
                } else {
                    showToast("Đăng nhập thất bại. Vui lòng kiểm tra lại!");
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<LoginResponse>> call, Throwable t) {
                loadingDialog.dismissDialog();
                showToast("Lỗi kết nối!");
            }
        });
    }

    private void handleLoginResponse(ApiResponse<LoginResponse> apiResponse) {
        String message = apiResponse.getData().getMessage();
        if (message != null) {
            switch (message) {
                case "Tài khoản đã bị khóa! Hãy liên hệ với chúng tôi!":
                    showToast("Tài khoản đã bị khóa. Hãy liên lạc với chúng tôi!");
                    break;
                case "Tài khoản không hợp lệ. Vui lòng thử lại!":
                case "Tài khoản mật khẩu không hợp lệ. Vui lòng thử lại":
                    showToast("Tài khoản hoặc mật khẩu không chính xác. Vui lòng kiểm tra lại!");
                    break;
                default:
                    showToast(message);
            }
        } else if (apiResponse.getData().isStatus()) {
            showToast("Đăng nhập thành công!");
            MySharedPreferences.getInstance().putToken(apiResponse.getData().getAccessToken());
            startActivity(new Intent(this, MainActivity.class));
            finish(); // kết thúc LoginActivity để user không back lại
        }
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
