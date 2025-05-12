package com.example.appdoan.Fragment.quanlythuchi_app.Activity.Transaction;

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
import com.example.quanlythuchi_app.Adapter.TransactionAdapter;
import com.example.quanlythuchi_app.Container.Response.ApiResponse;
import com.example.quanlythuchi_app.HomeActivity;
import com.example.quanlythuchi_app.Model.TransactionModel;
import com.example.quanlythuchi_app.R;
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

    ImageButton btnBack, btnCreateTranIncome;

    private RecyclerView rvTranIncomeList;

    SearchView svTranIncome;

    private List<TransactionModel> mListTranIncome;

    private List<TransactionModel> filterList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_income);

        btnBack = findViewById(R.id.btnBack);
        btnCreateTranIncome = findViewById(R.id.addTransactionIncome);
        rvTranIncomeList = findViewById(R.id.tranIncomeRecycleView);
        svTranIncome = findViewById(R.id.searchViewTranIncome);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvTranIncomeList.setLayoutManager(linearLayoutManager);

        DividerItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        rvTranIncomeList.addItemDecoration(itemDecoration);

        mListTranIncome = new ArrayList<>();
        getListTransactionIncome();

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TransactionIncomeActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });

        btnCreateTranIncome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TransactionIncomeActivity.this, TransactionIncomeCreateActivity.class);
                startActivity(intent);
            }
        });

        svTranIncome.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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
                if(apiResponse.getStatus() == 101)
                {
                    Toast.makeText(TransactionIncomeActivity.this, "Chưa có giao dịch để hiển thị!", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Gson gson = new Gson();
                    String jsonData = gson.toJson(apiResponse.getData());
                    mListTranIncome = gson.fromJson(jsonData, new TypeToken<List<TransactionModel>>(){}.getType());
                    Collections.reverse(mListTranIncome);
                    TransactionAdapter transactionAdapter = new TransactionAdapter(mListTranIncome, TransactionIncomeActivity.this);
                    rvTranIncomeList.setAdapter(transactionAdapter);
                }
            }
            @Override
            public void onFailure(Call<ApiResponse<Object>> call, Throwable t) {

            }
        });
    }

    private void filterListIncome(String query)
    {
        filterList = new ArrayList<>();
        filterList.clear();
        for(TransactionModel item : mListTranIncome)
        {
            if(item.getName().toLowerCase().contains(query.toLowerCase()))
            {
                filterList.add(item);
            }
        }
        TransactionAdapter transactionAdapter = new TransactionAdapter(filterList, TransactionIncomeActivity.this);
        rvTranIncomeList.setAdapter(transactionAdapter);
    }
}