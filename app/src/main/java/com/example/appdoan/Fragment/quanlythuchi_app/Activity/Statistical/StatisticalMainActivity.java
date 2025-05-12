package com.example.appdoan.Fragment.quanlythuchi_app.Activity.Statistical;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.quanlythuchi_app.Adapter.StatViewAdapter;
import com.example.quanlythuchi_app.Fragment.Statistical.CardStatisticalFragment;
import com.example.quanlythuchi_app.Fragment.Statistical.CategoryStatisticalFragment;
import com.example.quanlythuchi_app.Fragment.Statistical.DateStatisticalFragment;
import com.example.quanlythuchi_app.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class StatisticalMainActivity extends AppCompatActivity {
    private TabLayout tabLayout;


    private ViewPager2 viewPager2;

    private StatViewAdapter statViewAdapter;

    private ImageButton btnBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistical_main);
        tabLayout = StatisticalMainActivity.this.findViewById(R.id.tabLayoutStat);
        viewPager2 = StatisticalMainActivity.this.findViewById(R.id.viewPagerStat);
        btnBack=StatisticalMainActivity.this.findViewById(R.id.btnBack);
        statViewAdapter = new StatViewAdapter(this);
        viewPager2.setAdapter(statViewAdapter);

        new TabLayoutMediator(tabLayout, viewPager2, (tab, position) -> {
            switch (position)
            {
                case 0:
                    tab.setText("Tùy chỉnh");
                    break;
                case 1:
                    tab.setText("Theo danh mục");
                    break;
                case 2:
                    tab.setText("Theo thẻ");
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
                        DateStatisticalFragment allStat = new DateStatisticalFragment();
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragmentAllStat, allStat)
                                .commit();
                        break;
                    case 1:
                        CategoryStatisticalFragment categoryStat = new CategoryStatisticalFragment();
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragmentCategoryStat, categoryStat)
                                .commit();
                        break;
                    case 2:
                        CardStatisticalFragment cardStat = new CardStatisticalFragment();
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentCardStat,cardStat).commit();
                        break;

                }
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


    }
}