package com.example.appdoan;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.appdoan.Adapter.ChartViewAdapter;
import com.example.appdoan.Helper.LoadingDialog;
import com.example.appdoan.databinding.FragmentStatisticalChartBinding;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class StatisticalChartFragment extends Fragment {

    private LoadingDialog loadingDialog;
    private FragmentStatisticalChartBinding binding;
    private TabLayout tabChart;
    private ViewPager2 viewPager2;
    private ChartViewAdapter chartViewAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentStatisticalChartBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        loadingDialog = new LoadingDialog(requireActivity()); // Đảm bảo sử dụng Activity thay vì Fragment

        tabChart = binding.tabLayoutChart;
        viewPager2 = binding.viewPagerChart;

        // Sử dụng requireActivity() để truyền vào ChartViewAdapter, đây là Activity chứa Fragment
        chartViewAdapter = new ChartViewAdapter(requireActivity());
        viewPager2.setAdapter(chartViewAdapter);

        new TabLayoutMediator(tabChart, viewPager2, (tab, position) -> {
            switch (position) {
                case 0:
                    tab.setText("Hàng tháng");
                    break;
                case 1:
                    tab.setText("Theo thẻ");
                    break;
                case 2:
                    tab.setText("Theo danh mục");
                    break;
            }
        }).attach();

        return view;
    }

    private void startActivityWithLoading(Class<?> cls) {
        if (getActivity() == null) return;

        loadingDialog.startLoadingDialog();
        loadingDialog.setDialogDuration(2000);
        Intent intent = new Intent(getActivity(), cls);
        startActivity(intent);
    }
}
