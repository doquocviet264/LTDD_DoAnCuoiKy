package com.example.appdoan.Activity;

import android.os.Bundle;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.appdoan.R;
import com.example.appdoan.Adapter.CategoryAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class CategoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_category);

        SearchView searchView = findViewById(R.id.searchView);
        EditText searchEditText = searchView.findViewById(androidx.appcompat.R.id.search_src_text);
        searchEditText.setTextColor(getResources().getColor(android.R.color.white)); // Màu chữ gõ vào
        searchEditText.setHintTextColor(getResources().getColor(android.R.color.darker_gray)); // Màu chữ gợi ý

        // Initialize TabLayout and ViewPager2
        TabLayout tabLayout = findViewById(R.id.tabLayoutCategory);
        ViewPager2 viewPager = findViewById(R.id.viewPagerCategory);

        // Set Adapter for ViewPager2
        CategoryAdapter categoryAdapter = new CategoryAdapter(this);
        viewPager.setAdapter(categoryAdapter);

        // Connect TabLayout with ViewPager2
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            if (position == 0) {
                tab.setText("Thu nhập");
            } else if (position == 1) {
                tab.setText("Chi tiêu");
            }
        }).attach();
    }
}
