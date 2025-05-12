package com.example.appdoan.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.appdoan.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // layout chính chứa container

        // Hiển thị HomeFragment mặc định
        if (savedInstanceState == null) {
            loadFragment(new HomeFragment());
        }

        // Xử lý sự kiện cho các LinearLayout dưới thanh bottom bar
        setupNavigation();
    }

    // Hàm xử lý chuyển fragment
    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
    }

    // Hàm gán sự kiện click cho từng nút bottom
    private void setupNavigation() {
        LinearLayout bottomBtn1 = findViewById(R.id.bottomBtn1);
        LinearLayout bottomBtn2 = findViewById(R.id.bottomBtn2);
        LinearLayout bottomBtn3 = findViewById(R.id.bottomBtn3);
        LinearLayout bottomBtn4 = findViewById(R.id.bottomBtn4);

        bottomBtn1.setOnClickListener(v -> loadFragment(new HomeFragment()));
//        bottomBtn2.setOnClickListener(v -> loadFragment(new AnotherFragment()));
        bottomBtn3.setOnClickListener(v -> loadFragment(new CardFragment()));
        bottomBtn4.setOnClickListener(v -> loadFragment(new SettingFragment()));
    }
}
