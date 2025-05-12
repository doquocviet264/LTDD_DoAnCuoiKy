package com.example.appdoan.Fragment.quanlythuchi_app.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.quanlythuchi_app.Fragment.Statistical.CardStatisticalFragment;
import com.example.quanlythuchi_app.Fragment.Statistical.CategoryStatisticalFragment;
import com.example.quanlythuchi_app.Fragment.Statistical.DateStatisticalFragment;


public class StatViewAdapter extends FragmentStateAdapter {
    private FragmentActivity fragmentActivity;
    public StatViewAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
        this.fragmentActivity = fragmentActivity;
    }
    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position)
        {
            case 0:
                return new DateStatisticalFragment();
            case 1:
                return new CategoryStatisticalFragment();
            case 2:
                return new CardStatisticalFragment();
            default:
                return new DateStatisticalFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }

    public Fragment getCurrentFragment(int position)
    {
        return fragmentActivity.getSupportFragmentManager().findFragmentByTag("f" + position);
    }
}
