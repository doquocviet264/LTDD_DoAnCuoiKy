package com.example.appdoan;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.text.InputType;
import android.widget.Toast;


import com.example.appdoan.API.HTTPRequest;
import com.example.appdoan.API.HTTPService;
import com.example.appdoan.Activity.GoalActivity;
import com.example.appdoan.Container.Request.GoalRequest;
import com.example.appdoan.Container.Response.ApiResponse;
import com.example.appdoan.Model.GoalModel;
import com.example.appdoan.Utils.FormatDate;
import com.example.appdoan.databinding.ActivityGoalUpdateBinding;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.text.ParseException;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class GoalUpdateActivity extends AppCompatActivity {
    private ActivityGoalUpdateBinding binding;
    private int position;
    private String dateTemp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGoalUpdateBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setEvent();
        loadGoalData();
    }

    private void loadGoalData() {
        position = getIntent().getIntExtra("position", 0);
        Retrofit retrofit = HTTPService.getInstance();
        HTTPRequest httpRequest = retrofit.create(HTTPRequest.class);

        httpRequest.getAllGoal().enqueue(new Callback<ApiResponse<Object>>() {
            @Override
            public void onResponse(Call<ApiResponse<Object>> call, Response<ApiResponse<Object>> response) {
                ApiResponse<Object> apiResponse = response.body();
                Gson gson = new Gson();
                List<GoalModel> modalList = gson.fromJson(
                        gson.toJson(apiResponse.getData()), new TypeToken<List<GoalModel>>() {}.getType());

                GoalModel goal = modalList.get(position);
                try {
                    binding.edtGoalNameUpadte.setText(goal.getName());
                    binding.edtGoalAmountUpdate.setText(goal.getAmount().toString());
                    binding.edtGoalBalanceUpdate.setText(goal.getBalance().toString());
                    binding.edtGoalDeadlineUpdate.setText(FormatDate.ChangeFormatToDMY(goal.getDeadline()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                binding.btnGoalSaveUpdate.setOnClickListener(v -> updateGoal(goal));
                binding.btnDelete.setOnClickListener(v -> confirmDeleteGoal(goal));
            }

            @Override
            public void onFailure(Call<ApiResponse<Object>> call, Throwable t) {
                Toast.makeText(GoalUpdateActivity.this, "Lỗi kết nối", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateGoal(GoalModel goal) {
        String name = binding.edtGoalNameUpadte.getText().toString();
        String amountStr = binding.edtGoalAmountUpdate.getText().toString();
        String balanceStr = binding.edtGoalBalanceUpdate.getText().toString();
        String deadlineStr = binding.edtGoalDeadlineUpdate.getText().toString();

        if (name.isEmpty() || amountStr.isEmpty() || balanceStr.isEmpty() || deadlineStr.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            long amount = Long.parseLong(amountStr);
            long balance = Long.parseLong(balanceStr);
            String formattedDate = FormatDate.ChangeFormatToYDM(deadlineStr);

            GoalRequest request = new GoalRequest(name, balance, amount, formattedDate);
            Retrofit retrofit = HTTPService.getInstance();
            HTTPRequest httpRequest = retrofit.create(HTTPRequest.class);

            httpRequest.updateGoal(request, goal.getId()).enqueue(new Callback<ApiResponse<Object>>() {
                @Override
                public void onResponse(Call<ApiResponse<Object>> call, Response<ApiResponse<Object>> response) {
                    if (response.isSuccessful() && response.body().getStatus() == 200) {
                        Toast.makeText(GoalUpdateActivity.this, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(GoalUpdateActivity.this, GoalActivity.class));
                        finish();
                    } else {
                        Toast.makeText(GoalUpdateActivity.this, "Thất bại: " + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ApiResponse<Object>> call, Throwable t) {
                    Toast.makeText(GoalUpdateActivity.this, "Lỗi kết nối!", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            Toast.makeText(this, "Dữ liệu không hợp lệ", Toast.LENGTH_SHORT).show();
        }
    }

    private void confirmDeleteGoal(GoalModel goal) {
        new AlertDialog.Builder(this)
                .setMessage("Bạn có thực sự muốn xóa mục tiêu?")
                .setPositiveButton("Có", (dialog, which) -> deleteGoal(goal))
                .setNegativeButton("Không", null)
                .show();
    }

    private void deleteGoal(GoalModel goal) {
        Retrofit retrofit = HTTPService.getInstance();
        HTTPRequest httpRequest = retrofit.create(HTTPRequest.class);

        httpRequest.deleteGoal(goal.getId()).enqueue(new Callback<ApiResponse<Object>>() {
            @Override
            public void onResponse(Call<ApiResponse<Object>> call, Response<ApiResponse<Object>> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus() == 101) {
                        Toast.makeText(GoalUpdateActivity.this, "Không thể xóa mục tiêu!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(GoalUpdateActivity.this, "Xóa thành công!", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                } else {
                    Toast.makeText(GoalUpdateActivity.this, "Thất bại!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Object>> call, Throwable t) {
                Toast.makeText(GoalUpdateActivity.this, "Lỗi kết nối!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setEvent() {
        binding.btnBackGoalDetail.setOnClickListener(v -> onBackPressed());

        binding.edtGoalDeadlineUpdate.setInputType(InputType.TYPE_NULL);
        binding.edtGoalDeadlineUpdate.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog dialog = new DatePickerDialog(this, (view, y, m, d) -> {
                String dayStr = (d < 10) ? "0" + d : String.valueOf(d);
                String monthStr = ((m + 1) < 10) ? "0" + (m + 1) : String.valueOf(m + 1);
                binding.edtGoalDeadlineUpdate.setText(dayStr + "-" + monthStr + "-" + y);
                dateTemp = y + "-" + monthStr + "-" + dayStr;
            }, year, month, day);
            dialog.show();
        });
    }
}
