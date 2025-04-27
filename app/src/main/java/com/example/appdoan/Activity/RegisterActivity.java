package com.example.appdoan.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;



import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import com.example.appdoan.API.HTTPRequest;
import com.example.appdoan.API.HTTPService;
import com.example.appdoan.Container.Request.RegisterRequest;
import com.example.appdoan.Container.Response.RegisterResponse;
import com.example.appdoan.databinding.ActivityRegisterBinding;


public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater()); // Inflate the layout
        setContentView(binding.getRoot()); // Set content view using the binding object

        Retrofit retrofit = HTTPService.getInstance();
        HTTPRequest httpRequest = retrofit.create(HTTPRequest.class);



        // Set Login click listener
        binding.txtLogin.setOnClickListener(view -> {
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
        });

        // Register button click listener
        binding.btnRegister.setOnClickListener(view -> {
            String email = binding.edtEmail.getText().toString();
            String firstname = binding.edtFirstname.getText().toString();
            String lastname = binding.edtLastname.getText().toString();
            String username = binding.edtUsername.getText().toString();
            String password = binding.edtPassword.getText().toString();
            String repassword = binding.edtRepassword.getText().toString();

            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(firstname) || TextUtils.isEmpty(lastname)
                    || TextUtils.isEmpty(username) || TextUtils.isEmpty(password) || TextUtils.isEmpty(repassword)) {
                showToast("Vui lòng điền đầy đủ thông tin!");
            } else if (!password.equals(repassword)) {
                showToast("Nhập lại mật khẩu không trùng khớp!");
            } else if (!isValidEmail(email)) {
                showToast("Địa chỉ email không hợp lệ!");
            } else {
                RegisterRequest registerUserRequest = new RegisterRequest(username, password, firstname, lastname, email, "USER", "");
                Call<RegisterResponse> call = httpRequest.register(registerUserRequest);

                call.enqueue(new Callback<RegisterResponse>() {
                    @Override
                    public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                        if (response.isSuccessful()) {
                            RegisterResponse registerUserResponse = response.body();
                            if ("Tài khoản đã tồn tại. Vui lòng nhập lại!".equals(registerUserResponse.getMessage())) {
                                showToast("Username đã tồn tại. Vui lòng nhập lại!");
                            } else if ("Email đã tồn tại. Vui lòng nhập lại!".equals(registerUserResponse.getMessage())) {
                                showToast("Địa chỉ email đã được sử dụng!");
                            } else {
                                showToast("Đăng kí tài khoản thành công!");
                                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                            }
                        } else {
                            showToast("Đăng ký tài khoản thất bại. Vui lòng kiểm tra lại!");
                        }
                    }

                    @Override
                    public void onFailure(Call<RegisterResponse> call, Throwable t) {
                        showToast("Lỗi kết nối!");
                    }
                });
            }
        });
    }

    private void showToast(String message) {
        Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    // Kiểm tra định dạng email hợp lệ
    private boolean isValidEmail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}
