package com.example.appdoan.Fragment.quanlythuchi_app.Activity.Goal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quanlythuchi_app.API.HTTPRequest;
import com.example.quanlythuchi_app.API.HTTPService;
import com.example.quanlythuchi_app.Activity.Goal.GoalIntroductionActivity;
import com.example.quanlythuchi_app.Container.Request.GoalRequest;
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

public class GoalDepositActivity extends AppCompatActivity {
    EditText edtGoalDeposit;
    TextView tvMaxAdd;
    Button btnAddDeposit;

    ImageButton btnBack;
    int position;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goal_deposit);
        btnAddDeposit = GoalDepositActivity.this.findViewById(R.id.btnAddDeposit);
        edtGoalDeposit=GoalDepositActivity.this.findViewById(R.id.edtGoalDeposit);
        btnBack = GoalDepositActivity.this.findViewById(R.id.btnBackGoalUpdateDeposit);
        tvMaxAdd = GoalDepositActivity.this.findViewById(R.id.tvMaxAdd);
        Intent intent = getIntent();
        position=intent.getIntExtra("position",0);
        Retrofit retrofit = HTTPService.getInstance();
        HTTPRequest httpRequest = retrofit.create(HTTPRequest.class);
//

        Call<ApiResponse<Object>> call = httpRequest.getAllGoal();
        call.enqueue(new Callback<ApiResponse<Object>>() {
            @Override
            public void onResponse(Call<ApiResponse<Object>> call, Response<ApiResponse<Object>> response) {
                ApiResponse<Object> apiResponse = response.body(); // Đối tượng ApiResponse<Object> của bạn
                Gson gson = new Gson();
                String jsonData = gson.toJson(apiResponse.getData());

                List<GoalModel> modalList = gson.fromJson(jsonData, new TypeToken<List<GoalModel>>(){}.getType());

                Long balance = modalList.get(position).getBalance();
                String name =modalList.get(position).getName();
                Long amount = modalList.get(position).getAmount();
                String date = modalList.get(position).getDeadline();
                tvMaxAdd.setText(Format.formatNumber(amount-balance));
                btnBack.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });
                btnAddDeposit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String deposit;
                        deposit=edtGoalDeposit.getText().toString();
                        if(deposit.isEmpty()){
                            Toast.makeText(GoalDepositActivity.this, "Vui lòng số tiền thêm vào", Toast.LENGTH_SHORT).show();
                        }else {
                            Long goalDeposit = Long.valueOf(deposit);
                            GoalRequest goalRequest = new GoalRequest(name, balance+goalDeposit, amount,date);
                            Call<ApiResponse<Object>> call = httpRequest.updateGoal(goalRequest, modalList.get(position).getId());
                            call.enqueue(new Callback<ApiResponse<Object>>() {
                                @Override
                                public void onResponse(Call<ApiResponse<Object>> call, Response<ApiResponse<Object>> response) {
                                    if(response.isSuccessful())
                                    {
                                        ApiResponse<Object> apiResponse = response.body();
                                        if(apiResponse.getStatus() == 200)
                                        {
                                            Toast.makeText(GoalDepositActivity.this, "Cập nhật mục tiêu thành công!", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(GoalDepositActivity.this, GoalIntroductionActivity.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                        else {
                                            Toast.makeText(GoalDepositActivity.this, apiResponse.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                                @Override
                                public void onFailure(Call<ApiResponse<Object>> call, Throwable t) {
                                    Toast.makeText(GoalDepositActivity.this, "Lỗi kết nối!", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                });
            }

            @Override
            public void onFailure(Call<ApiResponse<Object>> call, Throwable t) {

                Toast.makeText(GoalDepositActivity.this, "Lỗi kết nối", Toast.LENGTH_SHORT).show();
            }
        });


    }
}