package com.example.appdoan.Fragment.quanlythuchi_app.Activity.Budget;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlythuchi_app.API.HTTPRequest;
import com.example.quanlythuchi_app.API.HTTPService;
import com.example.quanlythuchi_app.Adapter.BudgetAdapter;
import com.example.quanlythuchi_app.Container.Response.ApiResponse;
import com.example.quanlythuchi_app.HomeActivity;
import com.example.quanlythuchi_app.Model.BudgetModel;
import com.example.quanlythuchi_app.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class BudgetIntroductionActivity extends AppCompatActivity {
    ImageButton btnBack,btnAdd;
    SearchView svBudget;
    RecyclerView rvBudget;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget_introduction);
        setControl();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvBudget.setLayoutManager(linearLayoutManager);

        DividerItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        rvBudget.addItemDecoration(itemDecoration);

        setEvent();
        getBudget();
    }

    private void setControl() {
        btnBack = findViewById(R.id.btnBackBuget);
        btnAdd =findViewById(R.id.btnAddBudget);
        svBudget=findViewById(R.id.svBudget);
        rvBudget=findViewById(R.id.rvBudget);
    }
    private void setEvent() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BudgetIntroductionActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(BudgetIntroductionActivity.this, BudgetCreateActivity.class);
                startActivity(intent);
            }
        });
    }
    private void getBudget() {
        Retrofit retrofit = HTTPService.getInstance();
        HTTPRequest httpRequest = retrofit.create(HTTPRequest.class);

        Call<ApiResponse<Object>> call = httpRequest.getAllBudget();
        call.enqueue(new Callback<ApiResponse<Object>>() {
            @Override
            public void onResponse(Call<ApiResponse<Object>> call, Response<ApiResponse<Object>> response) {
                ApiResponse<Object> apiResponse = response.body();
                Gson gson = new Gson();
                String jsonData = gson.toJson(apiResponse.getData());

                List<BudgetModel> modalList = gson.fromJson(jsonData, new TypeToken<List<BudgetModel>>(){}.getType());
                BudgetAdapter budgetAdapter = new BudgetAdapter(BudgetIntroductionActivity.this, modalList);
                rvBudget.setAdapter(budgetAdapter);
                List<BudgetModel> filteredItems = new ArrayList<>();

                svBudget.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        filteredItems.clear();
                        for(BudgetModel model : modalList){
                            if(model.getCategoryModel().getName().toLowerCase().contains(newText.toLowerCase())){
                                filteredItems.add(model);
                            }
                        }
                        BudgetAdapter budgetAdapter1 = new BudgetAdapter(BudgetIntroductionActivity.this,filteredItems);
                        rvBudget.setAdapter(budgetAdapter1);
                        return true;
                    }
                });

            }
            @Override
            public void onFailure(Call<ApiResponse<Object>> call, Throwable t) {

                Toast.makeText(BudgetIntroductionActivity.this, "Lỗi kết nối", Toast.LENGTH_SHORT).show();
            }
        });
    }
}