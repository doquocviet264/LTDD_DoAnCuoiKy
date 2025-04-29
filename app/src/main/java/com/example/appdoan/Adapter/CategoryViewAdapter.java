package com.example.appdoan.Adapter;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.appdoan.Fragment.CategoryExpenseFragment;
import com.example.appdoan.Fragment.CategoryIncomeFragment;


public class CategoryViewAdapter extends FragmentStateAdapter {

    private static final Fragment[] fragments = {
            new CategoryIncomeFragment(),
            new CategoryExpenseFragment()
    };

    public CategoryViewAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return fragments[position];
    }

    @Override
    public int getItemCount() {
        return fragments.length; // Sử dụng độ dài của mảng fragments để trả về số lượng tab
    }

}

