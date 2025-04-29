package com.example.appdoan.Activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.appdoan.API.HTTPRequest;
import com.example.appdoan.API.HTTPService;
import com.example.appdoan.Container.Response.RegisterUserResponse;
import com.example.appdoan.Helper.LoadingDialog;
import com.example.appdoan.R;
import com.example.appdoan.databinding.FragmentSettingBinding;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SettingFragment extends Fragment {

    private FragmentSettingBinding binding;
    private LoadingDialog loadingDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSettingBinding.inflate(inflater, container, false);

        loadingDialog = new LoadingDialog(requireActivity());

        loadUserProfile();
        setupListeners();

        return binding.getRoot();
    }

    private void loadUserProfile() {
        Retrofit retrofit = HTTPService.getInstance();
        HTTPRequest httpRequest = retrofit.create(HTTPRequest.class);
        Call<RegisterUserResponse> call = httpRequest.getProfile();

        call.enqueue(new Callback<RegisterUserResponse>() {
            @Override
            public void onResponse(Call<RegisterUserResponse> call, Response<RegisterUserResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    RegisterUserResponse registerUserResponse = response.body();
                    if (registerUserResponse.isStatus()) {
                        binding.fullName.setText(registerUserResponse.getUserInfoModel().getFirstname() + " " + registerUserResponse.getUserInfoModel().getLastname());
                        binding.email.setText(registerUserResponse.getUserInfoModel().getAccountModel().getEmail());

                        Glide.with(requireContext())
                                .load(registerUserResponse.getUserInfoModel().getAvatar())
                                .placeholder(R.drawable.profile)
                                .error(R.drawable.profile)
                                .into(binding.ivAvatar);
                    }
                }
            }

            @Override
            public void onFailure(Call<RegisterUserResponse> call, Throwable t) {
                Toast.makeText(requireContext(), "Lỗi kết nối!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupListeners() {
        binding.profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateTo(ProfileActivity.class);
            }
        });

        binding.resetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateTo(ResetPasswordActivity.class);
            }
        });

        binding.category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateTo(CategoryActivity.class);
            }
        });

        binding.budget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateTo(BudgetActivity.class);
            }
        });

        binding.goal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateTo(GoalActivity.class);
            }
        });

        binding.btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLogoutDialog();
            }
        });
    }

    private void navigateTo(Class<?> targetActivity) {
        Intent intent = new Intent(requireContext(), targetActivity);
        startActivity(intent);
    }

    private void showLogoutDialog() {
        new AlertDialog.Builder(requireContext())
                .setMessage("Bạn có thực sự muốn đăng xuất?")
                .setCancelable(false)
                .setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("MyApp", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.remove("token");
                        editor.apply();
                        navigateTo(LoginActivity.class);
                    }
                })
                .setNegativeButton("Không", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                })
                .create()
                .show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null; // Tránh leak memory
    }
}
