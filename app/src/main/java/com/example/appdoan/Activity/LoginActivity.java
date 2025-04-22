package com.example.appdoan.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.appdoan.R;

public class LoginActivity extends AppCompatActivity {
    private EditText usernameInput, passwordInput;
    private Button loginBtn;
    private TextView registerText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login); // Liên kết với file XML layout

        // Ánh xạ view từ XML sang Java
        usernameInput = findViewById(R.id.username_input);
        passwordInput = findViewById(R.id.password_input);
        loginBtn = findViewById(R.id.login_btn);
        registerText = findViewById(R.id.register_text);
        // Xử lý sự kiện click nút đăng nhập
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleLogin();
            }
        });

        // Xử lý click text đăng ký
        registerText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }
    private void handleLogin() {
        // Lấy dữ liệu từ ô nhập
        String username = usernameInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();

        // Kiểm tra tên đăng nhập
        if (username.isEmpty()) {
            usernameInput.setError("Vui lòng nhập tên đăng nhập");
            usernameInput.requestFocus();
            return;
        }

        // Kiểm tra mật khẩu
        if (password.isEmpty()) {
            passwordInput.setError("Vui lòng nhập mật khẩu");
            passwordInput.requestFocus();
            return;
        }

        // Thông báo đăng nhập thành công (tạm thời)
        Toast.makeText(this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();

        // Sau này bạn có thể thêm code để chuyển sang màn hình chính:
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
        // finish();
    }
}
