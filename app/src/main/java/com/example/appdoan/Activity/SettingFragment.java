package com.example.appdoan.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.appdoan.R;

public class SettingFragment extends Fragment {

    private TextView fullName, email;
    private ImageView ivAvatar;
    private ImageButton btnLogout;

    private LinearLayout profile, resetPassword, category, budget, goal;

    public SettingFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_setting, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Ánh xạ view
        fullName = view.findViewById(R.id.fullName);
        email = view.findViewById(R.id.email);
        ivAvatar = view.findViewById(R.id.ivAvatar);
        btnLogout = view.findViewById(R.id.btnLogout);

        profile = view.findViewById(R.id.profile);
        resetPassword = view.findViewById(R.id.resetPassword);
        category = view.findViewById(R.id.category);
        budget = view.findViewById(R.id.budget);
        goal = view.findViewById(R.id.goal);

        // Set thông tin người dùng giả lập
        fullName.setText("Nguyễn Văn A");
        email.setText("nguyenvana@example.com");

        // Xử lý nút logout
        btnLogout.setOnClickListener(v -> navigateTo(LoginActivity.class, true));

        // Gán sự kiện cho các layout điều hướng
        setupNavigation(profile, ProfileActivity.class);
        setupNavigation(resetPassword, ResetPasswordActivity.class);
//        setupNavigation(category, CategoryActivity.class);
        setupNavigation(budget, BudgetActivity.class);
//        setupNavigation(goal, GoalActivity.class);
    }

    private void setupNavigation(View view, Class<?> targetActivity) {
        view.setOnClickListener(v -> navigateTo(targetActivity, false));
    }

    private void navigateTo(Class<?> targetActivity, boolean finishCurrent) {
        Intent intent = new Intent(getActivity(), targetActivity);
        startActivity(intent);
        if (finishCurrent) {
            getActivity().finish();
        }
    }
}
