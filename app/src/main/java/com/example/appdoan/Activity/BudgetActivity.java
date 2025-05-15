package com.example.appdoan.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appdoan.API.HTTPRequest;
import com.example.appdoan.API.HTTPService;
import com.example.appdoan.Adapter.BudgetAdapter;
import com.example.appdoan.BudgetCreateActivity;
import com.example.appdoan.Container.Response.ApiResponse;
import com.example.appdoan.Model.BudgetModel;
import com.example.appdoan.R;
import com.example.appdoan.databinding.ActivityBudgetBinding;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class BudgetActivity extends AppCompatActivity {
    private ActivityBudgetBinding binding;
    private ImageButton btnBack, btnAdd;
    private SearchView svBudget;
    private RecyclerView rvBudget;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBudgetBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setControl();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvBudget.setLayoutManager(linearLayoutManager);

        DividerItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        rvBudget.addItemDecoration(itemDecoration);

        setEvent();
        getBudget();
    }

    private void setControl() {
        btnBack = binding.btnBackBuget;
        btnAdd = binding.btnAddBudget;
        svBudget = binding.svBudget;
        rvBudget = binding.rvBudget;
    }

    private void setEvent() {
        btnBack.setOnClickListener(view -> onBackPressed());

        btnAdd.setOnClickListener(v -> {
            Intent intent = new Intent(BudgetActivity.this, BudgetCreateActivity.class);
            startActivity(intent);
        });

        EditText searchEditText = svBudget.findViewById(androidx.appcompat.R.id.search_src_text);
        searchEditText.setTextColor(Color.WHITE);
        searchEditText.setHintTextColor(Color.LTGRAY);

        ImageView searchIcon = svBudget.findViewById(androidx.appcompat.R.id.search_mag_icon);
        ImageView closeIcon = svBudget.findViewById(androidx.appcompat.R.id.search_close_btn);
        searchIcon.setColorFilter(Color.WHITE);
        closeIcon.setColorFilter(Color.WHITE);
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
                BudgetAdapter budgetAdapter = new BudgetAdapter(BudgetActivity.this, modalList);
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
                        for (BudgetModel model : modalList) {
                            if (model.getCategoryModel().getName().toLowerCase().contains(newText.toLowerCase())) {
                                filteredItems.add(model);
                            }
                        }
                        BudgetAdapter filteredAdapter = new BudgetAdapter(BudgetActivity.this, filteredItems);
                        rvBudget.setAdapter(filteredAdapter);
                        return true;
                    }
                });
            }

            @Override
            public void onFailure(Call<ApiResponse<Object>> call, Throwable t) {
                Toast.makeText(BudgetActivity.this, "Lỗi kết nối", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}
