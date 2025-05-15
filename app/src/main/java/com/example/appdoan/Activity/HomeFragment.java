package com.example.appdoan.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.bumptech.glide.Glide;
import com.example.appdoan.API.HTTPRequest;
import com.example.appdoan.API.HTTPService;
import com.example.appdoan.Adapter.CryptoWallerAdapter;
import com.example.appdoan.Container.Response.RegisterUserResponse;
import com.example.appdoan.Model.CryptoWallet;
import com.example.appdoan.R;
import com.example.appdoan.TransactionExpenseActivity;
import com.example.appdoan.TransactionIncomeActivity;
import com.example.appdoan.databinding.FragmentHomeBinding;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        loadUserProfile();

        // Xử lý sự kiện khi click vào nút btnExpense
        binding.btnExpense.setOnClickListener(v -> {
            // Chuyển sang màn hình nhập khoản chi
            Intent intent = new Intent(getActivity(), TransactionExpenseActivity.class);
            startActivity(intent);
        });

        binding.btnIncome.setOnClickListener(v -> {
            // Chuyển sang màn hình nhập khoản chi
            Intent intent = new Intent(getActivity(), TransactionIncomeActivity.class);
            startActivity(intent);
        });



        return view;
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


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
