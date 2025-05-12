package com.example.appdoan;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.widget.SearchView;
import android.widget.Toast;

import com.example.appdoan.API.HTTPRequest;
import com.example.appdoan.API.HTTPService;
import com.example.appdoan.Activity.MainActivity;
import com.example.appdoan.Activity.TransactionIncomeCreateActivity;
import com.example.appdoan.Adapter.TransactionAdapter;
import com.example.appdoan.Container.Response.ApiResponse;
import com.example.appdoan.Model.TransactionModel;
import com.example.appdoan.databinding.ActivityTransactionIncomeBinding;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class TransactionIncomeActivity extends AppCompatActivity {

    private ActivityTransactionIncomeBinding binding;
    private RecyclerView rvTranIncomeList;
    private List<TransactionModel> mListTranIncome;
    private List<TransactionModel> filterList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTransactionIncomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        rvTranIncomeList = binding.tranIncomeRecycleView;
        mListTranIncome = new ArrayList<>();
        filterList = new ArrayList<>();

        // Cấu hình RecyclerView
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvTranIncomeList.setLayoutManager(linearLayoutManager);
        rvTranIncomeList.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        // Lấy dữ liệu
        getListTransactionIncome();

        // Thiết lập sự kiện
        binding.btnBack.setOnClickListener(v -> startActivity(new Intent(this, MainActivity.class)));
        binding.add.setOnClickListener(v -> startActivity(new Intent(this, TransactionIncomeCreateActivity.class)));
        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterListIncome(newText);
                return true;
            }
        });
    }

    private void getListTransactionIncome() {
        Retrofit retrofit = HTTPService.getInstance();
        HTTPRequest httpRequest = retrofit.create(HTTPRequest.class);
        Call<ApiResponse<Object>> call = httpRequest.getIncomeTransaction();
        call.enqueue(new Callback<ApiResponse<Object>>() {
            @Override
            public void onResponse(Call<ApiResponse<Object>> call, Response<ApiResponse<Object>> response) {
                ApiResponse<Object> apiResponse = response.body();
                if (apiResponse == null) {
                    Toast.makeText(TransactionIncomeActivity.this, "Lỗi dữ liệu", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (apiResponse.getStatus() == 101) {
                    Toast.makeText(TransactionIncomeActivity.this, "Chưa có giao dịch để hiển thị!", Toast.LENGTH_SHORT).show();
                } else {
                    Gson gson = new Gson();
                    String jsonData = gson.toJson(apiResponse.getData());
                    mListTranIncome = gson.fromJson(jsonData, new TypeToken<List<TransactionModel>>(){}.getType());
                    if (mListTranIncome != null) {
                        Collections.reverse(mListTranIncome);
                        updateAdapter(mListTranIncome);
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Object>> call, Throwable t) {
                Toast.makeText(TransactionIncomeActivity.this, "Lỗi kết nối", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void filterListIncome(String query) {
        filterList.clear();
        if (query.isEmpty()) {
            updateAdapter(mListTranIncome);
            return;
        }
        for (TransactionModel item : mListTranIncome) {
            if (item.getName() != null && item.getName().toLowerCase().contains(query.toLowerCase())) {
                filterList.add(item);
            }
        }
        updateAdapter(filterList);
    }

    private void updateAdapter(List<TransactionModel> list) {
        TransactionAdapter transactionAdapter = new TransactionAdapter(list, TransactionIncomeActivity.this);
        rvTranIncomeList.setAdapter(transactionAdapter);
    }
}