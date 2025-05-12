package com.example.appdoan.Fragment.quanlythuchi_app;

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
import com.example.quanlythuchi_app.Container.Request.RegisterUserRequest;
import com.example.quanlythuchi_app.Container.Response.RegisterUserResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class RegisterActivity extends AppCompatActivity {

    EditText mail, firstName, lastName, userName, passWord, rePassWord;
    Button btnRegister;
    TextView tvLogin;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mail = findViewById(R.id.email);
        firstName = findViewById(R.id.firstname);
        lastName = findViewById(R.id.lastname);
        userName = findViewById(R.id.username);
        passWord = findViewById(R.id.password);
        rePassWord = findViewById(R.id.repassword);
        btnRegister = findViewById(R.id.register);
        tvLogin = findViewById(R.id.login);
        toolbar = findViewById(R.id.toolbar);

        Retrofit retrofit = HTTPService.getInstance();
        HTTPRequest httpRequest = retrofit.create(HTTPRequest.class);

        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = mail.getText().toString();
                String firstname = firstName.getText().toString();
                String lastname = lastName.getText().toString();
                String username = userName.getText().toString();
                String password = passWord.getText().toString();
                String repassword = rePassWord.getText().toString();

                if(email.isEmpty() || firstname.isEmpty() || lastname.isEmpty() || username.isEmpty() || password.isEmpty() || repassword.isEmpty())
                {
                    Toast.makeText(RegisterActivity.this, "Vui lòng điền đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                }
                else {
                    if(password.compareTo(repassword) != 0)
                    {
                        Toast.makeText(RegisterActivity.this, "Nhập lại mật khẩu không trùng khớp!", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        RegisterUserRequest registerUserRequest = new RegisterUserRequest(username, password, firstname, lastname, email, "USER", "");
                        Call<RegisterUserResponse> call = httpRequest.register(registerUserRequest);

                        call.enqueue(new Callback<RegisterUserResponse>() {
                            @Override
                            public void onResponse(Call<RegisterUserResponse> call, Response<RegisterUserResponse> response) {
                                if(response.isSuccessful())
                                {
                                    RegisterUserResponse registerUserResponse = response.body();
                                    if(registerUserResponse.getMessage().equals("Tài khoản đã tồn tại. Vui lòng nhập lại!"))
                                    {
                                        Toast.makeText(RegisterActivity.this, "Username đã tồn tại. Vui lòng nhập lại!", Toast.LENGTH_SHORT).show();
                                    }
                                    else if(registerUserResponse.getMessage().equals("Email đã tồn tại. Vui lòng nhập lại!"))
                                    {
                                        Toast.makeText(RegisterActivity.this, "Địa chỉ email đã được sử dụng!", Toast.LENGTH_SHORT).show();
                                    }
                                    else {
                                        Toast.makeText(RegisterActivity.this, "Đăng kí tài khoản thành công!", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                        startActivity(intent);
                                    }
                                }
                                else
                                {
                                    Toast.makeText(RegisterActivity.this, "Đăng ký tài khoản thất bại. Vui lòng kiếm tra lại!", Toast.LENGTH_SHORT).show();
                                }
                            }
                            @Override
                            public void onFailure(Call<RegisterUserResponse> call, Throwable t) {
                                Toast.makeText(RegisterActivity.this, "Lỗi kết nối !", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }
        });
    }
}