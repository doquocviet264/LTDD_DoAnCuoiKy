package com.example.appdoan.Fragment.quanlythuchi_app;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.quanlythuchi_app.API.HTTPRequest;
import com.example.quanlythuchi_app.API.HTTPService;
import com.example.quanlythuchi_app.API.MySharedPreferences;
import com.example.quanlythuchi_app.Container.Request.LoginRequest;
import com.example.quanlythuchi_app.Container.Response.ApiResponse;
import com.example.quanlythuchi_app.Container.Response.LoginResponse;
import com.example.quanlythuchi_app.Helper.LoadingDialog;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LoginActivity extends AppCompatActivity {

    LoadingDialog loadingDialog = new LoadingDialog(LoginActivity.this);
    Toolbar exit;
    EditText user, pass;
    Button login;

    TextView tvForgotPass, tvRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        MySharedPreferences.init(getApplicationContext());

        exit = findViewById(R.id.toolbar);
        user = findViewById(R.id.username);
        pass = findViewById(R.id.password);
        login = findViewById(R.id.login);
        tvForgotPass = findViewById(R.id.forgotpass);
        tvRegister = findViewById(R.id.register);

        Retrofit retrofit = HTTPService.getInstance();
        HTTPRequest httpRequest = retrofit.create(HTTPRequest.class);

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showExitConfirmationDialog();
            }
        });

        tvForgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                startActivity(intent);
            }
        });

        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = user.getText().toString();
                String password = pass.getText().toString();
                loadingDialog.startLoadingDialog();
                loadingDialog.setDialogDuration(2000);
                if(username.isEmpty() || password.isEmpty())
                {
                    Toast.makeText(LoginActivity.this, "Tên đăng nhập hoặc mật khẩu không được để trống!", Toast.LENGTH_SHORT).show();
                }
                else {
                    LoginRequest loginRequest = new LoginRequest(username, password);
                    // gọi phương thức call api
                    Call<ApiResponse<LoginResponse>> call = httpRequest.login(loginRequest);
                    call.enqueue(new Callback<ApiResponse<LoginResponse>>() {
                        @Override
                        public void onResponse(Call<ApiResponse<LoginResponse>> call, Response<ApiResponse<LoginResponse>> response) {
                            if(response.isSuccessful())
                            {
                                ApiResponse<LoginResponse> apiResponse = response.body();
                                if(apiResponse.getData().getMessage() != null) {
                                    if (apiResponse.getData().getMessage().equals("Tài khoản đã bị khóa! Hãy liên hệ với chúng tôi!")) {
                                        Toast.makeText(LoginActivity.this, "Tài khoản đã bị khóa. Hãy liên lạc với chúng tôi!", Toast.LENGTH_SHORT).show();
                                    } else if (apiResponse.getData().getMessage().equals("Tài khoản không hợp lệ. Vui lòng thử lại!")) {
                                        Toast.makeText(LoginActivity.this, "Tài khoản hoặc mật khẩu không chính xác. Vui lòng kiểm tra lại!", Toast.LENGTH_SHORT).show();
                                    } else if (apiResponse.getData().getMessage().equals("Tài khoản mật khẩu không hợp lệ. Vui lòng thử lại")) {
                                        Toast.makeText(LoginActivity.this, "Tài khoản hoặc mật khẩu không chính xác. Vui lòng kiểm tra lại!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                else if(apiResponse.getData().isStatus() == true){
                                    Toast.makeText(LoginActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                                    String accessToken = apiResponse.getData().getAccessToken();
                                    MySharedPreferences mySharedPreferences = MySharedPreferences.getInstance();
                                    mySharedPreferences.putToken(accessToken);
                                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                    startActivity(intent);
                                }
                            }
                            else {
                                Toast.makeText(LoginActivity.this, "Đăng nhập thất bại. Vui lòng kiếm tra lại!", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ApiResponse<LoginResponse>> call, Throwable t) {
                            Toast.makeText(LoginActivity.this, "Lỗi kết nối!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }
    private void showExitConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Bạn có muốn thoát ứng dụng không?")
                .setCancelable(false)
                .setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                })
                .setNegativeButton("Không", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
}