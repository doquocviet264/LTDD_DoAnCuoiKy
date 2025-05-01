package com.example.appdoan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.appdoan.API.HTTPRequest;
import com.example.appdoan.API.HTTPService;
import com.example.appdoan.Container.Request.GoalRequest;
import com.example.appdoan.Container.Response.ApiResponse;
import com.example.appdoan.Model.GoalModel;
import com.example.appdoan.Utils.Format;
import com.example.appdoan.databinding.ActivityGoalDepositBinding;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class GoalDepositActivity extends AppCompatActivity {

    private ActivityGoalDepositBinding binding;
    private int position;
    private GoalModel selectedGoal;
    private HTTPRequest httpRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGoalDepositBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initAPI();
        loadGoalData();
        setEvent();
    }

    private void initAPI() {
        Retrofit retrofit = HTTPService.getInstance();
        httpRequest = retrofit.create(HTTPRequest.class);
        position = getIntent().getIntExtra("position", 0);
    }

    private void loadGoalData() {
        httpRequest.getAllGoal().enqueue(new Callback<ApiResponse<Object>>() {
            @Override
            public void onResponse(Call<ApiResponse<Object>> call, Response<ApiResponse<Object>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Gson gson = new Gson();
                    String jsonData = gson.toJson(response.body().getData());
                    List<GoalModel> goalList = gson.fromJson(jsonData, new TypeToken<List<GoalModel>>() {}.getType());

                    if (position < goalList.size()) {
                        selectedGoal = goalList.get(position);
                        long maxDeposit = selectedGoal.getAmount() - selectedGoal.getBalance();
                        binding.tvMaxAdd.setText(Format.formatNumber(maxDeposit));
                    } else {
                        Toast.makeText(GoalDepositActivity.this, "Vị trí mục tiêu không hợp lệ!", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                } else {
                    Toast.makeText(GoalDepositActivity.this, "Không thể tải dữ liệu!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Object>> call, Throwable t) {
                Toast.makeText(GoalDepositActivity.this, "Lỗi kết nối!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setEvent() {
        binding.btnBackGoalUpdateDeposit.setOnClickListener(v -> finish());

        binding.btnAddDeposit.setOnClickListener(v -> {
            String input = binding.edtGoalDeposit.getText().toString().trim();
            if (input.isEmpty()) {
                Toast.makeText(GoalDepositActivity.this, "Vui lòng nhập số tiền", Toast.LENGTH_SHORT).show();
                return;
            }

            long depositAmount;
            try {
                depositAmount = Long.parseLong(input);
            } catch (NumberFormatException e) {
                Toast.makeText(GoalDepositActivity.this, "Số tiền không hợp lệ", Toast.LENGTH_SHORT).show();
                return;
            }

            if (selectedGoal == null) {
                Toast.makeText(GoalDepositActivity.this, "Mục tiêu chưa sẵn sàng", Toast.LENGTH_SHORT).show();
                return;
            }

            long newBalance = selectedGoal.getBalance() + depositAmount;
            GoalRequest goalRequest = new GoalRequest(
                    selectedGoal.getName(),
                    newBalance,
                    selectedGoal.getAmount(),
                    selectedGoal.getDeadline()
            );

            httpRequest.updateGoal(goalRequest, selectedGoal.getId()).enqueue(new Callback<ApiResponse<Object>>() {
                @Override
                public void onResponse(Call<ApiResponse<Object>> call, Response<ApiResponse<Object>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        ApiResponse<Object> apiResponse = response.body();
                        if (apiResponse.getStatus() == 200) {
                            Toast.makeText(GoalDepositActivity.this, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(GoalDepositActivity.this, apiResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<ApiResponse<Object>> call, Throwable t) {
                    Toast.makeText(GoalDepositActivity.this, "Lỗi kết nối!", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}
