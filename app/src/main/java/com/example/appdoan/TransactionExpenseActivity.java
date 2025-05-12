package com.example.appdoan;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;


import com.example.appdoan.API.HTTPRequest;
import com.example.appdoan.API.HTTPService;
import com.example.appdoan.Activity.MainActivity;
import com.example.appdoan.Activity.TransactionExpenseCreateActivity;
import com.example.appdoan.Adapter.TransactionAdapter;
import com.example.appdoan.Container.Response.ApiResponse;
import com.example.appdoan.Model.TransactionModel;
import com.example.appdoan.R;

import com.example.appdoan.databinding.ActivityTransactionExpenseBinding;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class TransactionExpenseActivity extends AppCompatActivity {

    private ActivityTransactionExpenseBinding binding;
    private RecyclerView rvTranExpenseList;
    private List<TransactionModel> mListTranExpense;
    private List<TransactionModel> filterList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTransactionExpenseBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        rvTranExpenseList = binding.tranIncomeRecycleView;
        mListTranExpense = new ArrayList<>();
        filterList = new ArrayList<>();

        // Cấu hình RecyclerView
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvTranExpenseList.setLayoutManager(linearLayoutManager);
        rvTranExpenseList.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        // Lấy dữ liệu
        getListTransactionExpense();

        // Thiết lập sự kiện
        setEvent();
    }

    private void setEvent() {
        binding.btnBack.setOnClickListener(v -> startActivity(new Intent(this, MainActivity.class)));
        binding.addTransactionExpense.setOnClickListener(v -> startActivity(new Intent(this, TransactionExpenseCreateActivity.class)));
        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterListExpense(newText);
                return true;
            }
        });
    }

    private void getListTransactionExpense() {
        Retrofit retrofit = HTTPService.getInstance();
        HTTPRequest httpRequest = retrofit.create(HTTPRequest.class);
        Call<ApiResponse<Object>> call = httpRequest.getExpenseTransaction();
        call.enqueue(new Callback<ApiResponse<Object>>() {
            @Override
            public void onResponse(Call<ApiResponse<Object>> call, Response<ApiResponse<Object>> response) {
                ApiResponse<Object> apiResponse = response.body();
                if (apiResponse == null) {
                    Toast.makeText(TransactionExpenseActivity.this, "Lỗi dữ liệu", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (apiResponse.getStatus() == 101) {
                    Toast.makeText(TransactionExpenseActivity.this, "Chưa có giao dịch để hiển thị!", Toast.LENGTH_SHORT).show();
                } else {
                    Gson gson = new Gson();
                    String jsonData = gson.toJson(apiResponse.getData());
                    mListTranExpense = gson.fromJson(jsonData, new TypeToken<List<TransactionModel>>(){}.getType());
                    if (mListTranExpense != null) {
                        Collections.reverse(mListTranExpense);
                        updateAdapter(mListTranExpense);
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Object>> call, Throwable t) {
                Toast.makeText(TransactionExpenseActivity.this, "Lỗi kết nối", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void filterListExpense(String query) {
        filterList.clear();
        if (query.isEmpty()) {
            updateAdapter(mListTranExpense);
            return;
        }
        for (TransactionModel item : mListTranExpense) {
            if (item.getName() != null && item.getName().toLowerCase().contains(query.toLowerCase())) {
                filterList.add(item);
            }
        }
        updateAdapter(filterList);
    }

    private void updateAdapter(List<TransactionModel> list) {
        TransactionAdapter transactionAdapter = new TransactionAdapter(list, TransactionExpenseActivity.this);
        rvTranExpenseList.setAdapter(transactionAdapter);
    }
}