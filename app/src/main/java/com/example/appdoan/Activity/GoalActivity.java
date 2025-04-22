package com.example.appdoan.Activity;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.appcompat.widget.SearchView;
import com.example.appdoan.R;

public class GoalActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_goal);
        SearchView searchView = findViewById(R.id.svGoal);

        // Lấy EditText bên trong SearchView để chỉnh màu chữ và màu hint
        EditText searchEditText = searchView.findViewById(androidx.appcompat.R.id.search_src_text);
        searchEditText.setTextColor(Color.WHITE); // Màu chữ gõ vào
        searchEditText.setHintTextColor(Color.GRAY); // Màu chữ gợi ý
    }
}