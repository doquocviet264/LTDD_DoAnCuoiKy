package com.example.appdoan;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.appdoan.API.HTTPRequest;
import com.example.appdoan.API.HTTPService;
import com.example.appdoan.Activity.BudgetActivity;
import com.example.appdoan.Container.Request.BudgetRequest;
import com.example.appdoan.Container.Response.ApiResponse;
import com.example.appdoan.Model.CategoryModel;
import com.example.appdoan.R;
import java.util.ArrayList;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class BudgetCreateActivity extends AppCompatActivity {
    EditText edtBudgetAmount,edtBudgetDescription;
    Spinner edtBudgetCategory;
    Button btnBudgetCreate;
    ImageButton btnBackBudgetCreate;
    String categorySelected;
    String amount,description;
    Long categoryId;
    private List<CategoryModel> listCategory;
    private CategoryModel categoryModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget_create);
        setControl();
        List<String> listCategoryName = new ArrayList<>();
        List<Long> listCategoryId = new ArrayList<>();
        Retrofit retrofit = HTTPService.getInstance();
        HTTPRequest httpRequest =retrofit.create(HTTPRequest.class);

        Call<List<CategoryModel>> call = httpRequest.getCategory();
        call.enqueue(new Callback<List<CategoryModel>>() {
            @Override
            public void onResponse(Call<List<CategoryModel>> call, Response<List<CategoryModel>> response) {
                listCategory = response.body();
                for(CategoryModel modal : listCategory){
                    if(modal.getType().equals(2L)){
                        listCategoryName.add(modal.getName());
                        listCategoryId.add(modal.getId());
                    }
                }
                ArrayAdapter<String> listCategoryNameAdapter = new ArrayAdapter<>(BudgetCreateActivity.this, android.R.layout.simple_spinner_item,listCategoryName);
                listCategoryNameAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                edtBudgetCategory.setAdapter(listCategoryNameAdapter);

                edtBudgetCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        categorySelected = (String) parent.getItemAtPosition(position);
                        categoryId = listCategoryId.get(position);
                        for(CategoryModel model:listCategory){
                            if (model.getName().equals(categorySelected)){
                                categoryModel =model;
                            }
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                btnBudgetCreate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        amount =edtBudgetAmount.getText().toString();
                        description =edtBudgetDescription.getText().toString();

                        if(amount.isEmpty()){
                            Toast.makeText(BudgetCreateActivity.this, "Vui lòng nhập số tiền", Toast.LENGTH_SHORT).show();
                        }else{
                            Long amountRequest=Long.valueOf(amount);
                            Retrofit retrofit1 = HTTPService.getInstance();
                            HTTPRequest httpRequest1=retrofit1.create(HTTPRequest.class);
                            BudgetRequest budgetRequest = new BudgetRequest(amountRequest,description);

                            Call<ApiResponse<Object>> call = httpRequest1.addBudget(categoryId,budgetRequest);
                            call.enqueue(new Callback<ApiResponse<Object>>() {
                                @Override
                                public void onResponse(Call<ApiResponse<Object>> call, Response<ApiResponse<Object>> response) {
                                    if(response.isSuccessful())
                                    {
                                        ApiResponse<Object> apiResponse = response.body();
                                        if(apiResponse.getStatus() == 200)
                                        {
                                            Toast.makeText(BudgetCreateActivity.this, "Thêm ngân sách thành công", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(BudgetCreateActivity.this, BudgetActivity.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                        else {
                                            Toast.makeText(BudgetCreateActivity.this, apiResponse.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<ApiResponse<Object>> call, Throwable t) {
                                    Toast.makeText(BudgetCreateActivity.this, "Lỗi kết nối!", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                });
            }

            @Override
            public void onFailure(Call<List<CategoryModel>> call, Throwable t) {
                Log.e("test",t.getMessage());
                Toast.makeText(BudgetCreateActivity.this, "Lỗi kết nối", Toast.LENGTH_SHORT).show();
            }
        });

        btnBackBudgetCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void setControl() {
        edtBudgetAmount=findViewById(R.id.edtBudgetAmount);
        edtBudgetDescription=findViewById(R.id.edtBudgetDescription);
        edtBudgetCategory=findViewById(R.id.edtBudgetCategory);
        btnBudgetCreate=findViewById(R.id.btnBudgetCreate);
        btnBackBudgetCreate=findViewById(R.id.btnBackBudgetCreate);
    }
}