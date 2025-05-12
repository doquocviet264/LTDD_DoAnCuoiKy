package com.example.appdoan.Fragment.quanlythuchi_app.Activity.Category;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.quanlythuchi_app.Adapter.CategoryViewAdapter;
import com.example.quanlythuchi_app.Fragment.Category.CategoryExpenseFragment;
import com.example.quanlythuchi_app.Fragment.Category.CategoryIncomeFragment;
import com.example.quanlythuchi_app.HomeActivity;
import com.example.quanlythuchi_app.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class CategoryIntroductionActivity extends AppCompatActivity {
    private TabLayout tabLayout;

    private SearchView searchView;

    private ViewPager2 viewPager2;

    private CategoryViewAdapter categoryViewAdapter;

    private ImageButton btnBack, btnAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_introduction);

        tabLayout = findViewById(R.id.tabLayoutCategory);
        viewPager2 = findViewById(R.id.viewPagerCategory);
        btnBack = findViewById(R.id.btnBack);
        btnAdd = findViewById(R.id.btnCategoryCreate);
        searchView = findViewById(R.id.searchView);

        categoryViewAdapter = new CategoryViewAdapter(this);
        viewPager2.setAdapter(categoryViewAdapter);

        new TabLayoutMediator(tabLayout, viewPager2, (tab, position) -> {
            switch (position)
            {
                case 0:
                    tab.setText("Thu nhập");
                    break;
                case 1:
                    tab.setText("Chi tiêu");
                    break;
            }
        }).attach();

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                if (position == 0 || position == 1) {
                }
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });


        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                switch (position)
                {
                    case 0:
                        CategoryIncomeFragment incomeFragment = new CategoryIncomeFragment();
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragmentContainerIncome, incomeFragment)
                                .commit();
                        break;
                    case 1:
                        CategoryExpenseFragment expenseFragment = new CategoryExpenseFragment();
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragmentContainerExpense, expenseFragment)
                                .commit();
                        break;
                }
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CategoryIntroductionActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CategoryIntroductionActivity.this, AddCategoryActivity.class);
                startActivity(intent);
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String svText) {
                int currentPage = tabLayout.getSelectedTabPosition();
                Fragment currentFragment = categoryViewAdapter.getCurrentFragment(currentPage);
                if(currentFragment instanceof CategoryIncomeFragment)
                {
                    CategoryIncomeFragment incomeFragment = (CategoryIncomeFragment) currentFragment;
                    incomeFragment.filterCategoryIncome(svText);
                }
                else if(currentFragment instanceof CategoryExpenseFragment)
                {
                    CategoryExpenseFragment expenseFragment = (CategoryExpenseFragment) currentFragment;
                    expenseFragment.filterCategoryExpense(svText);
                }
                return true;
            }
        });
    }

}