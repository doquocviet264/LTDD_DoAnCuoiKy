package com.example.appdoan.Fragment.quanlythuchi_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.quanlythuchi_app.Fragment.Chart.ChartViewAdapter;
import com.example.quanlythuchi_app.Helper.LoadingDialog;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class StatisticalChartActivity extends AppCompatActivity {

    LoadingDialog loadingDialog = new LoadingDialog(StatisticalChartActivity.this);
    BottomNavigationView navbar;

    private TabLayout tabChart;

    private ViewPager2 viewPager2;

    private ChartViewAdapter chartViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistical_chart);

        navbar = findViewById(R.id.navbar);
        navbar.setSelectedItemId(R.id.report);
        tabChart = findViewById(R.id.tabLayoutChart);
        viewPager2 = findViewById(R.id.viewPagerChart);

        chartViewAdapter = new ChartViewAdapter(this);
        viewPager2.setAdapter(chartViewAdapter);

        new TabLayoutMediator(tabChart, viewPager2, (tab, position) -> {
            switch (position)
            {
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


        navbar.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.report:
                        return true;
                    case R.id.home:
                        loadingDialog.startLoadingDialog();
                        loadingDialog.setDialogDuration(2000);
                        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.card:
                        loadingDialog.startLoadingDialog();
                        loadingDialog.setDialogDuration(2000);
                        startActivity(new Intent(getApplicationContext(), CardIntroductionActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.setting:
                        loadingDialog.startLoadingDialog();
                        loadingDialog.setDialogDuration(2000);
                        startActivity(new Intent(getApplicationContext(), SettingActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                }
                return false;
            }
        });
    }
}