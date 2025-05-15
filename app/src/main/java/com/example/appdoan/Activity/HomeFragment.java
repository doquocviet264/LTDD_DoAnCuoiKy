package com.example.appdoan.Activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.appdoan.API.HTTPRequest;
import com.example.appdoan.API.HTTPService;
import com.example.appdoan.Adapter.CryptoWallerAdapter;
import com.example.appdoan.Adapter.NotificationAdapter;
import com.example.appdoan.Adapter.TransactionAdapter;
import com.example.appdoan.Container.Response.ApiResponse;
import com.example.appdoan.Container.Response.RegisterUserResponse;
import com.example.appdoan.Model.CryptoWallet;
import com.example.appdoan.Model.NotificationModel;
import com.example.appdoan.Model.TransactionModel;
import com.example.appdoan.R;
import com.example.appdoan.TransactionExpenseActivity;
import com.example.appdoan.TransactionIncomeActivity;
import com.example.appdoan.databinding.FragmentHomeBinding;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private List<TransactionModel> mListTransaction;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        loadUserProfile();
//        loadTransactionSummary();
        loadTodayTransactions();

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
//        binding.btnNotification.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Dialog dialog = new Dialog(requireContext());
//                dialog.setContentView(R.layout.activity_notification_dialog);
//                dialog.setTitle("Thông báo");
//
//                // Set dialog width and height
//                Window dialogWindow = dialog.getWindow();
//                if (dialogWindow != null) {
//                    WindowManager.LayoutParams layoutParams = dialogWindow.getAttributes();
//                    layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT; // Set width to match parent
//                    layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT; // Set height to wrap content
//                    dialogWindow.setAttributes(layoutParams);
//                    dialogWindow.setGravity(Gravity.TOP);
//                }

//                Retrofit retrofit = HTTPService.getInstance();
//                HTTPRequest httpRequest = retrofit.create(HTTPRequest.class);
//                Call<ApiResponse<Object>> call = httpRequest.listNotification();
//                call.enqueue(new Callback<ApiResponse<Object>>() {
//                    @Override
//                    public void onResponse(Call<ApiResponse<Object>> call, Response<ApiResponse<Object>> response) {
//                        if(response.isSuccessful())
//                        {
//                            ApiResponse apiResponse = response.body();
//                            if(apiResponse.getStatus() == 101)
//                            {
//                                Toast.makeText(requireContext(), apiResponse.getMessage(), Toast.LENGTH_SHORT).show();
//                                notificationCount.setText("0");
//                            }
//                            else
//                            {
//                                Gson gson = new Gson();
//                                String jsonData = gson.toJson(apiResponse.getData());
//                                mListNotification = gson.fromJson(jsonData, new TypeToken<List<NotificationModel>>(){}.getType());
//                                int count = mListNotification.size();
//                                String countText = String.valueOf(count);
//                                notificationCount.setText(countText);
//                                RecyclerView rcvNotification = dialog.findViewById(R.id.notificationRecyclerView);
//                                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext());
//                                rcvNotification.setLayoutManager(linearLayoutManager);
//
//                                DividerItemDecoration itemDecoration = new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL);
//                                rcvNotification.addItemDecoration(itemDecoration);
//                                NotificationAdapter notificationAdapter = new NotificationAdapter(requireContext(), mListNotification);
//                                rcvNotification.setAdapter(notificationAdapter);
//                                dialog.show();
//                            }
//                        }
//                    }
//                    @Override
//                    public void onFailure(Call<ApiResponse<Object>> call, Throwable t) {
//                    }
//                });
//            }
    }
    private void loadTodayTransactions() {
        Retrofit retrofit = HTTPService.getInstance();
        HTTPRequest httpRequest = retrofit.create(HTTPRequest.class);
        Call<ApiResponse<Object>> call = httpRequest.getAllTransactionToday();
        call.enqueue(new Callback<ApiResponse<Object>>() {
            @Override
            public void onResponse(Call<ApiResponse<Object>> call, Response<ApiResponse<Object>> response) {
                ApiResponse<Object> apiResponse = response.body();
                if(apiResponse.getStatus() == 101)
                {
                    Toast.makeText(requireContext(),"Chưa có giao dịch để hiển thị!", Toast.LENGTH_SHORT).show();
                }
                else {
                    Gson gson = new Gson();
                    String jsonData = gson.toJson(apiResponse.getData());
                    mListTransaction = gson.fromJson(jsonData, new TypeToken<List<TransactionModel>>(){}.getType());
                    Collections.reverse(mListTransaction);
                    TransactionAdapter transactionAdapter = new TransactionAdapter(mListTransaction,  requireContext());
                    binding.list.setAdapter(transactionAdapter);
                }
            }
            @Override
            public void onFailure(Call<ApiResponse<Object>> call, Throwable t) {
            }
        });
    }

    private void loadTransactionSummary() {
        Retrofit retrofit = HTTPService.getInstance();
        HTTPRequest httpRequest = retrofit.create(HTTPRequest.class);

        // Gọi API tổng thu nhập tháng hiện tại
        Call<ApiResponse<Object>> incomeCall = httpRequest.getTotalIncomeByCurrentMonth();
        incomeCall.enqueue(new Callback<ApiResponse<Object>>() {
            @Override
            public void onResponse(Call<ApiResponse<Object>> call, Response<ApiResponse<Object>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Object data = response.body().getData();
                    binding.txtIncome.setText(data.toString() + " đ");
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Object>> call, Throwable t) {
                Toast.makeText(requireContext(), "Không thể lấy tổng thu nhập", Toast.LENGTH_SHORT).show();
            }
        });

        // Gọi API tổng chi tiêu tháng hiện tại
        Call<ApiResponse<Object>> expenseCall = httpRequest.getTotalIncByCurrentMonth();
        expenseCall.enqueue(new Callback<ApiResponse<Object>>() {
            @Override
            public void onResponse(Call<ApiResponse<Object>> call, Response<ApiResponse<Object>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Object data = response.body().getData();
                    binding.txtExpense.setText(data.toString() + " đ");
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Object>> call, Throwable t) {
                Toast.makeText(requireContext(), "Không thể lấy tổng chi tiêu", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
