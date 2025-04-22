package com.example.appdoan.Activity;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.EditText;
import androidx.appcompat.widget.SearchView;


import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.appdoan.R;

public class BudgetActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_budget);
        SearchView searchView = findViewById(R.id.svBudget);

        // Lấy EditText bên trong SearchView để chỉnh màu chữ và màu hint
        EditText searchEditText = searchView.findViewById(androidx.appcompat.R.id.search_src_text);
        searchEditText.setTextColor(Color.WHITE); // Màu chữ gõ vào
        searchEditText.setHintTextColor(Color.GRAY); // Màu chữ gợi ý
    }
}