package com.example.appdoan.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.appdoan.R;

public class RegisterActivity extends AppCompatActivity {
    private EditText regUsernameInput, regEmailInput, regPasswordInput, regConfirmPasswordInput;
    private Button registerBtn;
    private TextView loginText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);

        // Ánh xạ view
        regUsernameInput = findViewById(R.id.reg_username_input);
        regEmailInput = findViewById(R.id.reg_email_input);
        regPasswordInput = findViewById(R.id.reg_password_input);
        regConfirmPasswordInput = findViewById(R.id.reg_confirm_password_input);
        registerBtn = findViewById(R.id.register_btn);
        loginText = findViewById(R.id.login_text);

        // Xử lý click nút đăng ký
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleRegister();
            }
        });

        // Xử lý click text đăng nhập
        loginText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish(); // Đóng màn hình đăng ký sau khi chuyển
            }
        });
    }

    private void handleRegister() {
        String username = regUsernameInput.getText().toString().trim();
        String email = regEmailInput.getText().toString().trim();
        String password = regPasswordInput.getText().toString().trim();
        String confirmPassword = regConfirmPasswordInput.getText().toString().trim();

        // Validate dữ liệu
        if (username.isEmpty()) {
            regUsernameInput.setError("Vui lòng nhập tên đăng nhập");
            return;
        }

        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            regEmailInput.setError("Email không hợp lệ");
            return;
        }

        if (password.isEmpty() || password.length() < 6) {
            regPasswordInput.setError("Mật khẩu phải có ít nhất 6 ký tự");
            return;
        }

        if (!password.equals(confirmPassword)) {
            regConfirmPasswordInput.setError("Mật khẩu không khớp");
            return;
        }

        // Nếu hợp lệ, xử lý đăng ký
        Toast.makeText(this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();

        // Sau khi đăng ký thành công có thể chuyển về màn hình đăng nhập
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
