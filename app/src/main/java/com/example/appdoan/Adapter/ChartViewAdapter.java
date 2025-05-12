package com.example.appdoan.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.appdoan.Fragment.BarChartCardFragment;
import com.example.appdoan.Fragment.LineChartTotalInYearFragment;
import com.example.appdoan.Fragment.PieChartCategoryFragment;

public class ChartViewAdapter extends FragmentStateAdapter {
    private FragmentActivity fragmentActivity;

    public ChartViewAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
        this.fragmentActivity = fragmentActivity;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position)
        {
            case 0:
                return new LineChartTotalInYearFragment();
            case 1:
                return new BarChartCardFragment();
            case 2:
                return new PieChartCategoryFragment();
            default:
                return new LineChartTotalInYearFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}

