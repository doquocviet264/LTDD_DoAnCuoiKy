package com.example.appdoan.Fragment.quanlythuchi_app.Activity.Budget;

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
import com.example.quanlythuchi_app.Model.BudgetModel;
import com.example.quanlythuchi_app.Model.CategoryModel;
import com.example.quanlythuchi_app.R;
import com.example.quanlythuchi_app.Utils.FormatDate;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.text.ParseException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class BudgetDetailActivity extends AppCompatActivity {
    TextView tvBudgetAmountDetail,tvbudgetBalanceDetail,tvBudgetMonthDetail,tvBudgetDescriptionDetail,tvBudgetCategoryDetail;
    ProgressBar progressBar;
    ImageButton btnBack;
    Long budgetId;
    Long categoryId;
    BudgetModel budgetModel=null;
    CategoryModel categoryModel=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget_detail);
        Intent intent = getIntent();
        budgetId=intent.getLongExtra("budgetId",0L);
        categoryId=intent.getLongExtra("categoryId",0L);
        setControl();
        //nhận budget
        Retrofit retrofit = HTTPService.getInstance();
        HTTPRequest httpRequest =retrofit.create(HTTPRequest.class);

        Call<ApiResponse<Object>> call = httpRequest.getBudget(budgetId);
        call.enqueue(new Callback<ApiResponse<Object>>() {
            @Override
            public void onResponse(Call<ApiResponse<Object>> call, Response<ApiResponse<Object>> response) {
                ApiResponse<Object> apiResponse = response.body(); // Đối tượng ApiResponse<Object> của bạn
                Gson gson = new Gson();
                String jsonData = gson.toJson(apiResponse.getData());

                budgetModel = gson.fromJson(jsonData, new TypeToken<BudgetModel>(){}.getType());

                tvBudgetDescriptionDetail.setText(budgetModel.getDescription().toString());
                tvBudgetCategoryDetail.setText(budgetModel.getCategoryModel().getName());
                String from,to;
                try {
                    from = FormatDate.ChangeFormatToDMY(budgetModel.getFromdate());
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
                try {
                    to = FormatDate.ChangeFormatToDMY(budgetModel.getTodate());
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
                tvBudgetMonthDetail.setText(from+" đến "+to);
                //lay tong so tien trong budget(balance)
                Call<ApiResponse<Object>> call1 = httpRequest.totalByCategoryInMonth(budgetModel.getCategoryModel().getId());
                call1.enqueue(new Callback<ApiResponse<Object>>() {
                    @Override
                    public void onResponse(Call<ApiResponse<Object>> call, Response<ApiResponse<Object>> response) {
                        if(response.isSuccessful())
                        {
                            ApiResponse<Object> apiResponse = response.body();
                            if(apiResponse.getStatus() == 200)
                            {
                                Double amount = Double.valueOf(budgetModel.getAmount());
                                Double totalInMonth = (Double) apiResponse.getData();
                                String totalInMonthInt = Format.formatNumber(totalInMonth.longValue());
                                int progress = (int) ((totalInMonth/amount)*100);
                                progressBar.setProgress(progress);
                                tvbudgetBalanceDetail.setText(totalInMonthInt);
                                if(totalInMonth>amount){
                                    tvBudgetAmountDetail.setTextColor(Color.RED);
                                    tvBudgetAmountDetail.setText(Format.formatNumber(budgetModel.getAmount()) +" - Vượt ngân sách");
                                }else if(totalInMonth==amount){
                                    tvBudgetAmountDetail.setTextColor(Color.GREEN);
                                    tvBudgetAmountDetail.setText(Format.formatNumber(budgetModel.getAmount()) +" - Hoàn thành");
                                }else {
                                    tvBudgetAmountDetail.setText(Format.formatNumber(budgetModel.getAmount()));
                                }

                            }else if(apiResponse.getStatus()==101){

                                tvbudgetBalanceDetail.setText("0");
                                progressBar.setProgress(0);
                                tvBudgetAmountDetail.setText(Format.formatNumber(budgetModel.getAmount()));
                            }
                            else {
                                Toast.makeText(BudgetDetailActivity.this, apiResponse.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse<Object>> call, Throwable t) {
                        Toast.makeText(BudgetDetailActivity.this, "Lỗi kết nối!", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onFailure(Call<ApiResponse<Object>> call, Throwable t) {
                Toast.makeText(BudgetDetailActivity.this, "Lỗi kết nối!", Toast.LENGTH_SHORT).show();
            }
        });


        setEvent();
    }

    private void setControl() {
        tvBudgetCategoryDetail=findViewById(R.id.tvBudgetCategotyDetail);
        tvbudgetBalanceDetail=findViewById(R.id.tvBudgetBalanceDetail);
        tvBudgetAmountDetail=findViewById(R.id.tvBudgetAmountDetail);
        tvBudgetMonthDetail=findViewById(R.id.tvBudgetMonthDetail);
        tvBudgetDescriptionDetail=findViewById(R.id.tvBudgetDescriptionDetail);
        progressBar=findViewById(R.id.pbBudgetDetail);
        btnBack=findViewById(R.id.btnBackBudgetDetail);

    }
    private void setEvent() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}