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

public class TransactionExpenseActivity extends AppCompatActivity {

    ImageButton btnBack, btnCreateTranExpense;

    SearchView svExpense;

    private RecyclerView rvTranExpenseList;

    private List<TransactionModel> mListTranExpense;
    private List<TransactionModel> filterList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_expense);

        btnBack = findViewById(R.id.btnBack);
        btnCreateTranExpense = findViewById(R.id.addTransactionExpense);
        rvTranExpenseList = findViewById(R.id.tranIncomeRecycleView);
        svExpense = findViewById(R.id.searchViewTranExpense);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvTranExpenseList.setLayoutManager(linearLayoutManager);

        DividerItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        rvTranExpenseList.addItemDecoration(itemDecoration);

        mListTranExpense = new ArrayList<>();
        getListTransactionExpese();

        setEvent();
    }

    private void setEvent() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TransactionExpenseActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });

        btnCreateTranExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TransactionExpenseActivity.this, TransactionExpenseCreateActivity.class);
                startActivity(intent);
            }
        });

        svExpense.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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

    private void getListTransactionExpese() {
        Retrofit retrofit = HTTPService.getInstance();
        HTTPRequest httpRequest = retrofit.create(HTTPRequest.class);
        Call<ApiResponse<Object>> call = httpRequest.getExpenseTransaction();
        call.enqueue(new Callback<ApiResponse<Object>>() {
            @Override
            public void onResponse(Call<ApiResponse<Object>> call, Response<ApiResponse<Object>> response) {
                ApiResponse<Object> apiResponse = response.body();
                if(apiResponse.getStatus() == 101)
                {
                    Toast.makeText(TransactionExpenseActivity.this, "Chưa có giao dịch để hiển thị!", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Gson gson = new Gson();
                    String jsonData = gson.toJson(apiResponse.getData());
                    mListTranExpense = gson.fromJson(jsonData, new TypeToken<List<TransactionModel>>(){}.getType());
                    Collections.reverse(mListTranExpense);
                    TransactionAdapter transactionAdapter = new TransactionAdapter(mListTranExpense, TransactionExpenseActivity.this);
                    rvTranExpenseList.setAdapter(transactionAdapter);
                }
            }
            @Override
            public void onFailure(Call<ApiResponse<Object>> call, Throwable t) {

            }
        });
    }

    private void filterListExpense(String query)
    {
        filterList = new ArrayList<>();
        filterList.clear();
        for(TransactionModel item : mListTranExpense)
        {
            if(item.getName().toLowerCase().contains(query.toLowerCase()))
            {
                filterList.add(item);
            }
        }
        TransactionAdapter transactionAdapter = new TransactionAdapter(filterList, TransactionExpenseActivity.this);
        rvTranExpenseList.setAdapter(transactionAdapter);
    }
}