package com.example.appdoan.Fragment.quanlythuchi_app.Activity.Goal;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quanlythuchi_app.API.HTTPRequest;
import com.example.quanlythuchi_app.API.HTTPService;
import com.example.quanlythuchi_app.Container.Response.ApiResponse;
import com.example.quanlythuchi_app.Helper.Format;
import com.example.quanlythuchi_app.Model.GoalModel;
import com.example.quanlythuchi_app.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class GoalDetailActivity extends AppCompatActivity {
    TextView tvGoalNameDetail,tvGoalAmountDetail,tvGoalStatusDetail,tvGoalBalanceDetail,tvGoalDeadlineDetail,tvDeadlineNoti;
    ImageButton btnBackGoalDetail;
    ProgressBar pbGoalDetail;
    int position;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goal_detail);
        setControl();
        Intent intent = getIntent();
        position=intent.getIntExtra("position",0);
        Retrofit retrofit = HTTPService.getInstance();
        HTTPRequest httpRequest = retrofit.create(HTTPRequest.class);

        Call<ApiResponse<Object>> call = httpRequest.getAllGoal();
        call.enqueue(new Callback<ApiResponse<Object>>() {
            @Override
            public void onResponse(Call<ApiResponse<Object>> call, Response<ApiResponse<Object>> response) {
                ApiResponse<Object> apiResponse = response.body(); // Đối tượng ApiResponse<Object> của bạn
                Gson gson = new Gson();
                String jsonData = gson.toJson(apiResponse.getData());

                List<GoalModel> modalList = gson.fromJson(jsonData, new TypeToken<List<GoalModel>>(){}.getType());

                Double amount = Double.valueOf(modalList.get(position).getAmount());
                Double balance= Double.valueOf(modalList.get(position).getBalance());
                int progress = (int) ((balance/amount)*100);
                String deadline = Format.formatDate(modalList.get(position).getDeadline());

                Long status = modalList.get(position).getStatus();
                tvGoalNameDetail.setText(modalList.get(position).getName());
                tvGoalAmountDetail.setText(Format.formatNumber(modalList.get(position).getAmount()));
                tvGoalBalanceDetail.setText(Format.formatNumber(modalList.get(position).getBalance()));
                tvGoalDeadlineDetail.setText(deadline);
                pbGoalDetail.setProgress(progress);
                pbGoalDetail.setProgress(progress);
                if(status==3L){
                    tvGoalStatusDetail.setText("Quá hạn");
                    tvGoalStatusDetail.setTextColor(Color.RED);
                }
                else if(status==2L){
                    tvGoalStatusDetail.setText("Hoàn thành");
                    tvGoalStatusDetail.setTextColor(Color.GREEN);
                }else {
                    tvGoalStatusDetail.setText("Trong hạn");
                }



            }

            @Override
            public void onFailure(Call<ApiResponse<Object>> call, Throwable t) {

                Toast.makeText(GoalDetailActivity.this, "Lỗi kết nối", Toast.LENGTH_SHORT).show();
            }
        });
        btnBackGoalDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void setControl() {
        tvGoalNameDetail=findViewById(R.id.tvGoalNameDetail);
        tvGoalAmountDetail=findViewById(R.id.tvGoalAmountDetail);
        tvGoalBalanceDetail=findViewById(R.id.tvGoalBalanceDetail);
        tvGoalDeadlineDetail=findViewById(R.id.tvGoalDeadlineDetail);
        btnBackGoalDetail=findViewById(R.id.btnBackGoalDetail);
        pbGoalDetail=findViewById(R.id.pbGoalDetail);
        tvDeadlineNoti=findViewById(R.id.tvDeadlineNoti);
        tvGoalStatusDetail=findViewById(R.id.tvGoalStatusDetail);

    }
}