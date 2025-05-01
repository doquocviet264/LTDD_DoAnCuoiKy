package com.example.appdoan;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.appdoan.API.HTTPRequest;
import com.example.appdoan.API.HTTPService;
import com.example.appdoan.Activity.GoalActivity;
import com.example.appdoan.Container.Request.GoalRequest;
import com.example.appdoan.Container.Response.ApiResponse;
import com.example.appdoan.databinding.ActivityGoalCreateBinding;

import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GoalCreateActivity extends AppCompatActivity {
    private ActivityGoalCreateBinding binding;
    private String dateTemp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGoalCreateBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupDatePicker();
        setupEvents();
    }

    private void setupDatePicker() {
        binding.edtGoalDeadline.setInputType(InputType.TYPE_NULL);
        binding.edtGoalDeadline.setOnClickListener(v -> {
            binding.edtGoalDeadline.clearFocus();

            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DATE);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, selectedYear, selectedMonth, selectedDay) -> {
                String dayString = (selectedDay < 10) ? "0" + selectedDay : String.valueOf(selectedDay);
                String monthString = ((selectedMonth + 1) < 10) ? "0" + (selectedMonth + 1) : String.valueOf(selectedMonth + 1);

                binding.edtGoalDeadline.setText(dayString + "-" + monthString + "-" + selectedYear);
                dateTemp = selectedYear + "-" + monthString + "-" + dayString;
            }, year, month, day);

            datePickerDialog.show();
        });
    }

    private void setupEvents() {
        binding.btnBackGoalCreate.setOnClickListener(v -> onBackPressed());

        binding.btnGoalCreate.setOnClickListener(v -> {
            String goalName = binding.edtGoalName.getText().toString();
            String goalAmount = binding.edtGoalAmount.getText().toString();
            String goalBalance = binding.edtGoalBalance.getText().toString();
            String goalDeadline = binding.edtGoalDeadline.getText().toString();

            if (goalName.isEmpty() || goalAmount.isEmpty() || goalBalance.isEmpty() || goalDeadline.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            long goalAmountNum = Long.parseLong(goalAmount);
            long goalBalanceNum = Long.parseLong(goalBalance);

            GoalRequest goalRequest = new GoalRequest(goalName, goalBalanceNum, goalAmountNum, dateTemp);

            HTTPRequest httpRequest = HTTPService.getInstance().create(HTTPRequest.class);
            Call<ApiResponse<Object>> call = httpRequest.addGoal(goalRequest);

            call.enqueue(new Callback<ApiResponse<Object>>() {
                @Override
                public void onResponse(Call<ApiResponse<Object>> call, Response<ApiResponse<Object>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        ApiResponse<Object> apiResponse = response.body();
                        if (apiResponse.getStatus() == 200) {
                            Toast.makeText(GoalCreateActivity.this, "Thêm mục tiêu thành công!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(GoalCreateActivity.this, GoalActivity.class));
                            finish();
                        } else {
                            Toast.makeText(GoalCreateActivity.this, apiResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<ApiResponse<Object>> call, Throwable t) {
                    Toast.makeText(GoalCreateActivity.this, "Lỗi kết nối!", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}
