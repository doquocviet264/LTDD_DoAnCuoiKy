package com.example.appdoan.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.appdoan.Fragment.IncomeFragment;
import com.example.appdoan.Fragment.ExpenseFragment;

public class CategoryAdapter extends FragmentStateAdapter {

    public CategoryAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        // Tạo Fragment cho từng tab
        if (position == 0) {
            return new IncomeFragment(); // Tab thu nhập
        } else {
            return new ExpenseFragment(); // Tab chi tiêu
        }
    }

    @Override
    public int getItemCount() {
        return 2; // Chỉ có 2 tab
    }
}
