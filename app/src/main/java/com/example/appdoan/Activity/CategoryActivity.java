package com.example.appdoan.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.appdoan.Adapter.CategoryViewAdapter;
import com.example.appdoan.CategoryAddActivity;
import com.example.appdoan.Fragment.CategoryExpenseFragment;
import com.example.appdoan.Fragment.CategoryIncomeFragment;
import com.example.appdoan.databinding.ActivityCategoryBinding;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class CategoryActivity extends AppCompatActivity {

    private ActivityCategoryBinding binding;
    private CategoryViewAdapter categoryViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCategoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupViewPager();
        setupTabLayout();
        setupListeners();
    }

    private void setupViewPager() {
        categoryViewAdapter = new CategoryViewAdapter(this);
        binding.viewPagerCategory.setAdapter(categoryViewAdapter);
    }

    private void setupTabLayout() {
        new TabLayoutMediator(binding.tabLayoutCategory, binding.viewPagerCategory,
                (tab, position) -> tab.setText(position == 0 ? "Thu nhập" : "Chi tiêu")
        ).attach();
    }

    private void setupListeners() {
        binding.btnBack.setOnClickListener(view -> onBackPressed()); // Trở lại trang trước

        binding.btnCategoryCreate.setOnClickListener(view -> {
            Intent intent = new Intent(this, CategoryAddActivity.class);
            startActivity(intent);
        });


        binding.searchView.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String text) {
                int currentPage = binding.tabLayoutCategory.getSelectedTabPosition();
                if (currentPage == 0) {
                    // Thu nhập
                    CategoryIncomeFragment incomeFragment = (CategoryIncomeFragment) categoryViewAdapter.createFragment(currentPage);
                    incomeFragment.filterCategoryIncome(text);
                } else if (currentPage == 1) {
                    // Chi tiêu
                    CategoryExpenseFragment expenseFragment = (CategoryExpenseFragment) categoryViewAdapter.createFragment(currentPage);
                    expenseFragment.filterCategoryExpense(text);
                }
                return true;
            }
        });
        EditText searchEditText = binding.searchView.findViewById(androidx.appcompat.R.id.search_src_text);
        searchEditText.setTextColor(Color.WHITE); // Màu chữ nhập vào
        searchEditText.setHintTextColor(Color.LTGRAY); // Màu gợi ý "Search here..."

        // (Tuỳ chọn) Đổi màu icon search và close cho đồng bộ nếu nền tối
        ImageView searchIcon = binding.searchView.findViewById(androidx.appcompat.R.id.search_mag_icon);
        ImageView closeIcon = binding.searchView.findViewById(androidx.appcompat.R.id.search_close_btn);
        searchIcon.setColorFilter(Color.WHITE);
        closeIcon.setColorFilter(Color.WHITE);
    }
}
