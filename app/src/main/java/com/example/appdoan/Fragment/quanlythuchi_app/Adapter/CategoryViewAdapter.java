package com.example.appdoan.Fragment.quanlythuchi_app.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.quanlythuchi_app.Fragment.Category.CategoryExpenseFragment;
import com.example.quanlythuchi_app.Fragment.Category.CategoryIncomeFragment;

public class CategoryViewAdapter extends FragmentStateAdapter {
    private FragmentActivity fragmentActivity;
    public CategoryViewAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
        this.fragmentActivity = fragmentActivity;
    }
    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position)
        {
            case 0:
                return new CategoryIncomeFragment();
            case 1:
                return new CategoryExpenseFragment();
            default:
                return new CategoryIncomeFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }

    public Fragment getCurrentFragment(int position)
    {
        return fragmentActivity.getSupportFragmentManager().findFragmentByTag("f" + position);
    }
}
