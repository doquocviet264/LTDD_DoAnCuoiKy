package com.example.appdoan.Fragment.quanlythuchi_app.Activity.Budget;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quanlythuchi_app.API.HTTPRequest;
import com.example.quanlythuchi_app.API.HTTPService;
import com.example.quanlythuchi_app.Container.Request.BudgetRequest;
import com.example.quanlythuchi_app.Container.Response.ApiResponse;
import com.example.quanlythuchi_app.Model.BudgetModel;
import com.example.quanlythuchi_app.Model.CategoryModel;
import com.example.quanlythuchi_app.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class BudgetUpdateActivity extends AppCompatActivity {
    EditText edtBudgetAmountUpdate,edtBudgetDescriptionUpdate;
    Button btnBudgetUpdate,btnDeleteBudget;
    ImageButton btnBackBudgetUpdate;
    Long budgetId;
    CategoryModel categoryModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget_update);
        setControl();
        Intent intent = getIntent();
        budgetId=intent.getLongExtra("budgetId",0L);

        Retrofit retrofit = HTTPService.getInstance();
        HTTPRequest httpRequest =retrofit.create(HTTPRequest.class);

        Call<ApiResponse<Object>> call = httpRequest.getBudget(budgetId);
        call.enqueue(new Callback<ApiResponse<Object>>() {
            @Override
            public void onResponse(Call<ApiResponse<Object>> call, Response<ApiResponse<Object>> response) {
                ApiResponse<Object> apiResponse = response.body(); // Đối tượng ApiResponse<Object> của bạn
                Gson gson = new Gson();
                String jsonData = gson.toJson(apiResponse.getData());

                BudgetModel modal = gson.fromJson(jsonData, new TypeToken<BudgetModel>(){}.getType());

                edtBudgetAmountUpdate.setText(modal.getAmount().toString());
                edtBudgetDescriptionUpdate.setText(modal.getDescription().toString());
                categoryModel=modal.getCategoryModel();
            }

            @Override
            public void onFailure(Call<ApiResponse<Object>> call, Throwable t) {

            }
        });

        btnBackBudgetUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnBudgetUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String amount = edtBudgetAmountUpdate.getText().toString();
                Long amountIn = Long.valueOf(amount);
                String description =edtBudgetDescriptionUpdate.getText().toString();
                Retrofit retrofit = HTTPService.getInstance();
                HTTPRequest httpRequest =retrofit.create(HTTPRequest.class);
                BudgetRequest budgetRequest = new BudgetRequest(amountIn,description);
                Call<ApiResponse<Object>> call1 = httpRequest.updateBudget(budgetId,budgetRequest);
                call1.enqueue(new Callback<ApiResponse<Object>>() {
                    @Override
                    public void onResponse(Call<ApiResponse<Object>> call, Response<ApiResponse<Object>> response) {
                        if(response.isSuccessful())
                        {
                            ApiResponse<Object> apiResponse = response.body();
                            if(apiResponse.getStatus() == 200)
                            {
                                Toast.makeText(BudgetUpdateActivity.this, "Sửa ngân sách thành công", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(BudgetUpdateActivity.this, BudgetIntroductionActivity.class);
                                startActivity(intent);
                                finish();
                            }
                            else {
                                Toast.makeText(BudgetUpdateActivity.this, apiResponse.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse<Object>> call, Throwable t) {
                        Toast.makeText(BudgetUpdateActivity.this, "Lỗi kết nối!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        btnDeleteBudget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Retrofit retrofit = HTTPService.getInstance();
                HTTPRequest httpRequest =retrofit.create(HTTPRequest.class);
                Call<ApiResponse<Object>> call = httpRequest.deleteBudget(budgetId);
                call.enqueue(new Callback<ApiResponse<Object>>() {
                    @Override
                    public void onResponse(Call<ApiResponse<Object>> call, Response<ApiResponse<Object>> response) {
                        if(response.isSuccessful())
                        {
                            ApiResponse<Object> apiResponse = response.body();
                            if(apiResponse.getStatus() == 200)
                            {
                                Toast.makeText(BudgetUpdateActivity.this, "Xóa ngân sách thành công", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(BudgetUpdateActivity.this, BudgetIntroductionActivity.class);
                                startActivity(intent);
                                finish();
                            }
                            else {
                                Toast.makeText(BudgetUpdateActivity.this, "Lỗi!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse<Object>> call, Throwable t) {

                    }
                });
            }
        });
    }

    private void setControl() {
        edtBudgetAmountUpdate=findViewById(R.id.edtBudgetAmountUpdate);
        edtBudgetDescriptionUpdate=findViewById(R.id.edtBudgetDescriptionUpdate);
        btnBudgetUpdate=findViewById(R.id.btnBudgetUpdate);
        btnBackBudgetUpdate=findViewById(R.id.btnBackBudgetUpdate);
        btnDeleteBudget=findViewById(R.id.btnDeleteBudget);
    }
}