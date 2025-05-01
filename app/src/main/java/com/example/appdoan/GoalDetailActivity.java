package com.example.appdoan;

import android.os.Bundle;


import androidx.appcompat.app.AppCompatActivity;
import android.widget.Toast;

import com.example.appdoan.API.HTTPRequest;
import com.example.appdoan.API.HTTPService;
import com.example.appdoan.Container.Response.ApiResponse;
import com.example.appdoan.Model.GoalModel;
import com.example.appdoan.Utils.Format;
import com.example.appdoan.databinding.ActivityGoalDetailBinding;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class GoalDetailActivity extends AppCompatActivity {

    private ActivityGoalDetailBinding binding;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGoalDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        position = getIntent().getIntExtra("position", 0);

        Retrofit retrofit = HTTPService.getInstance();
        HTTPRequest httpRequest = retrofit.create(HTTPRequest.class);

        Call<ApiResponse<Object>> call = httpRequest.getAllGoal();
        call.enqueue(new Callback<ApiResponse<Object>>() {
            @Override
            public void onResponse(Call<ApiResponse<Object>> call, Response<ApiResponse<Object>> response) {
                ApiResponse<Object> apiResponse = response.body();
                Gson gson = new Gson();
                String jsonData = gson.toJson(apiResponse.getData());
                List<GoalModel> modalList = gson.fromJson(jsonData, new TypeToken<List<GoalModel>>() {}.getType());

                GoalModel goal = modalList.get(position);
                Double amount = goal.getAmount().doubleValue();
                Double balance = goal.getBalance().doubleValue();
                int progress = (int) ((balance / amount) * 100);
                String deadline = Format.formatDate(goal.getDeadline());
                long status = goal.getStatus();

                binding.tvGoalNameDetail.setText(goal.getName());
                binding.tvGoalAmountDetail.setText(Format.formatNumber(goal.getAmount()));
                binding.tvGoalBalanceDetail.setText(Format.formatNumber(goal.getBalance()));
                binding.tvGoalDeadlineDetail.setText(deadline);
                binding.pbGoalDetail.setProgress(progress);

                if (status == 3L) {
                    binding.tvGoalStatusDetail.setText("Quá hạn");
                    binding.tvGoalStatusDetail.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
                } else if (status == 2L) {
                    binding.tvGoalStatusDetail.setText("Hoàn thành");
                    binding.tvGoalStatusDetail.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
                } else {
                    binding.tvGoalStatusDetail.setText("Trong hạn");
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Object>> call, Throwable t) {
                Toast.makeText(GoalDetailActivity.this, "Lỗi kết nối", Toast.LENGTH_SHORT).show();
            }
        });

        binding.btnBackGoalDetail.setOnClickListener(v -> onBackPressed());
    }
}
