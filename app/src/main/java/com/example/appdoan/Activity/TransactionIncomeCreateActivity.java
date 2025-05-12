package com.example.appdoan.Activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;


import android.app.DatePickerDialog;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;


import com.example.appdoan.API.HTTPRequest;
import com.example.appdoan.API.HTTPService;
import com.example.appdoan.Container.Request.TransactionRequest;
import com.example.appdoan.Container.Response.ApiResponse;
import com.example.appdoan.Model.CardModel;
import com.example.appdoan.Model.CategoryModel;
import com.example.appdoan.TransactionIncomeActivity;
import com.example.appdoan.Utils.Format;
import com.example.appdoan.databinding.ActivityTransactionIncomeCreateBinding;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class TransactionIncomeCreateActivity extends AppCompatActivity {

    private ActivityTransactionIncomeCreateBinding binding;
    private String categorySelected, cardSelected;
    // TransactionRequest
    private String tranNameRequest, tranLocationRequest, tranDateRequest, tranDesRequest;
    private Long tranAmountRequest;
    // Data
    private Long categoryIdSelected, cardIdSelected;
    private List<CategoryModel> mListCategoryIncome;
    private List<CardModel> mListCard;
    private List<String> mListCategoryName, mListCardName;
    private List<Long> mListCategoryId, mListCardId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTransactionIncomeCreateBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mListCategoryName = new ArrayList<>();
        mListCardName = new ArrayList<>();
        mListCategoryId = new ArrayList<>();
        mListCardId = new ArrayList<>();

        setSpinner();
        setEvent();
    }

    private void setSpinner() {
        Retrofit retrofit = HTTPService.getInstance();
        HTTPRequest httpRequest = retrofit.create(HTTPRequest.class);

        // Lấy danh mục thu nhập
        Call<List<CategoryModel>> call = httpRequest.incomeCategory();
        call.enqueue(new Callback<List<CategoryModel>>() {
            @Override
            public void onResponse(Call<List<CategoryModel>> call, Response<List<CategoryModel>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    mListCategoryIncome = response.body();
                    mListCategoryName.clear();
                    mListCategoryId.clear();
                    for (CategoryModel item : mListCategoryIncome) {
                        mListCategoryName.add(item.getName());
                        mListCategoryId.add(item.getId());
                    }
                    setupSpinner(binding.categoryCreateIncome, mListCategoryName, mListCategoryId, (position) -> {
                        categorySelected = mListCategoryName.get(position);
                        categoryIdSelected = mListCategoryId.get(position);
                    });
                }
            }

            @Override
            public void onFailure(Call<List<CategoryModel>> call, Throwable t) {
                Toast.makeText(TransactionIncomeCreateActivity.this, "Lỗi kết nối khi lấy danh mục", Toast.LENGTH_SHORT).show();
            }
        });

        // Lấy danh sách thẻ
        Call<List<CardModel>> call1 = httpRequest.getAllCard();
        call1.enqueue(new Callback<List<CardModel>>() {
            @Override
            public void onResponse(Call<List<CardModel>> call, Response<List<CardModel>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    mListCard = response.body();
                    mListCardName.clear();
                    mListCardId.clear();
                    for (CardModel item : mListCard) {
                        mListCardName.add(item.getName());
                        mListCardId.add(item.getId());
                    }
                    setupSpinner(binding.cardCreateIncome, mListCardName, mListCardId, (position) -> {
                        cardSelected = mListCardName.get(position);
                        cardIdSelected = mListCardId.get(position);
                    });
                }
            }

            @Override
            public void onFailure(Call<List<CardModel>> call, Throwable t) {
                Toast.makeText(TransactionIncomeCreateActivity.this, "Lỗi kết nối khi lấy danh sách thẻ", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupSpinner(Spinner spinner, List<String> names, List<Long> ids, OnItemSelectedListener listener) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, names);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                listener.onItemSelected(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                listener.onItemSelected(-1); // Gọi với vị trí không hợp lệ để xử lý khi không chọn
            }
        });
    }

    private void setEvent() {
        binding.btnBack.setOnClickListener(v -> onBackPressed());

        binding.transactionIncomeDateCreate.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            new DatePickerDialog(this, (view1, year1, monthOfYear, dayOfMonth) -> {
                String dayString = dayOfMonth < 10 ? "0" + dayOfMonth : String.valueOf(dayOfMonth);
                String monthString = (monthOfYear + 1) < 10 ? "0" + (monthOfYear + 1) : String.valueOf(monthOfYear + 1);
                binding.transactionIncomeDateCreate.setText(dayString + "/" + monthString + "/" + year1);
            }, year, month, day).show();
        });

        binding.btnTransactionIncomeCreate.setOnClickListener(v -> {
            tranNameRequest = binding.transactionIncomeNameCreate.getText().toString();
            String amountText = binding.transactionIncomeAmountCreate.getText().toString();
            tranAmountRequest = amountText.isEmpty() ? 0L : Long.parseLong(amountText);
            tranLocationRequest = binding.transactionIncomeLocationCreate.getText().toString();
            tranDesRequest = binding.transactionIncomeDescriptionCreate.getText().toString();
            tranDateRequest = binding.transactionIncomeDateCreate.getText().toString();

            if (tranNameRequest.isEmpty() || tranAmountRequest <= 0 || tranLocationRequest.isEmpty() ||
                    tranDesRequest.isEmpty() || tranDateRequest.isEmpty() || categoryIdSelected == null || cardIdSelected == null) {
                Toast.makeText(this, "Vui lòng điền đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                return;
            }

            TransactionRequest transactionRequest = new TransactionRequest(tranNameRequest, tranAmountRequest,
                    tranLocationRequest, Format.formatDateRequest(tranDateRequest), tranDesRequest);
            Retrofit retrofit = HTTPService.getInstance();
            HTTPRequest httpRequest = retrofit.create(HTTPRequest.class);
            Call<ApiResponse<Object>> call = httpRequest.addTransactionIncome(categoryIdSelected, cardIdSelected, transactionRequest);
            call.enqueue(new Callback<ApiResponse<Object>>() {
                @Override
                public void onResponse(Call<ApiResponse<Object>> call, Response<ApiResponse<Object>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        ApiResponse apiResponse = response.body();
                        if (apiResponse.getStatus() == 101) {
                            Toast.makeText(TransactionIncomeCreateActivity.this, "Thêm giao dịch không thành công. Vui lòng thử lại!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(TransactionIncomeCreateActivity.this, "Thêm giao dịch thành công!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(TransactionIncomeCreateActivity.this, TransactionIncomeActivity.class));
                        }
                    } else {
                        Toast.makeText(TransactionIncomeCreateActivity.this, "Thêm giao dịch không thành công!", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ApiResponse<Object>> call, Throwable t) {
                    Toast.makeText(TransactionIncomeCreateActivity.this, "Lỗi kết nối!", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    // Interface để xử lý sự kiện chọn item trong Spinner
    private interface OnItemSelectedListener {
        void onItemSelected(int position);
    }
}
