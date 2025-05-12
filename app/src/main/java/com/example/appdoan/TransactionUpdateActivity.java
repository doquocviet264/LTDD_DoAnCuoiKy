package com.example.appdoan;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.app.DatePickerDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;


import com.example.appdoan.API.HTTPRequest;
import com.example.appdoan.API.HTTPService;
import com.example.appdoan.Container.Request.TransactionRequest;
import com.example.appdoan.Container.Response.ApiResponse;
import com.example.appdoan.Model.CategoryModel;
import com.example.appdoan.Model.TransactionModel;
import com.example.appdoan.Utils.Format;
import com.example.appdoan.databinding.ActivityTransactionUpdateBinding;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class TransactionUpdateActivity extends AppCompatActivity {

    private ActivityTransactionUpdateBinding binding;
    private List<CategoryModel> mListCategoryExpense, mListCategoryIncome;
    private List<String> mListCategoryIncomeName, mListCategoryExpenseName;
    private List<Long> mListCategoryIncomeId, mListCategoryExpenseId;
    private Long categoryIdSelected, cardIdSelected;
    private TransactionModel transactionModel;

    // Request
    private String tranNameUpdateText, tranLocationUpdateText, tranDesUpdateText, tranDateUpdateText;
    private Long tranAmountUpdateText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTransactionUpdateBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mListCategoryIncomeName = new ArrayList<>();
        mListCategoryExpenseName = new ArrayList<>();
        mListCategoryIncomeId = new ArrayList<>();
        mListCategoryExpenseId = new ArrayList<>();

        setData();
        setSpinner();
        setEvent();
    }

    private void setData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle == null) {
            return;
        }
        transactionModel = (TransactionModel) bundle.getSerializable("transaction_object_update");
        if (transactionModel == null) {
            return;
        }
        binding.transactionNameUpdate.setText(transactionModel.getName());
        binding.transactionAmountUpdate.setText(transactionModel.getAmount().toString());
        binding.transactionLocationUpdate.setText(transactionModel.getLocation());
        binding.transactionDescriptionUpdate.setText(transactionModel.getDescription());
        binding.transactionDateUpdate.setText(Format.formatDate(transactionModel.getTransactiondate()));
    }

    private void setSpinner() {
        Retrofit retrofit = HTTPService.getInstance();
        HTTPRequest httpRequest = retrofit.create(HTTPRequest.class);

        if (transactionModel == null) {
            return;
        }

        // Giao dịch thu nhập
        if (transactionModel.getType() == 1L) {
            Call<List<CategoryModel>> call = httpRequest.incomeCategory();
            call.enqueue(new Callback<List<CategoryModel>>() {
                @Override
                public void onResponse(Call<List<CategoryModel>> call, Response<List<CategoryModel>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        mListCategoryIncome = response.body();
                        mListCategoryIncomeName.clear();
                        mListCategoryIncomeId.clear();
                        for (CategoryModel item : mListCategoryIncome) {
                            mListCategoryIncomeName.add(item.getName());
                            mListCategoryIncomeId.add(item.getId());
                        }
                        setupSpinner(mListCategoryIncomeName, mListCategoryIncomeId);
                    }
                }

                @Override
                public void onFailure(Call<List<CategoryModel>> call, Throwable t) {
                    Toast.makeText(TransactionUpdateActivity.this, "Lỗi kết nối khi lấy danh mục", Toast.LENGTH_SHORT).show();
                }
            });
        }
        // Giao dịch chi tiêu
        else {
            Call<List<CategoryModel>> call = httpRequest.expenseCategory();
            call.enqueue(new Callback<List<CategoryModel>>() {
                @Override
                public void onResponse(Call<List<CategoryModel>> call, Response<List<CategoryModel>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        mListCategoryExpense = response.body();
                        mListCategoryExpenseName.clear();
                        mListCategoryExpenseId.clear();
                        for (CategoryModel item : mListCategoryExpense) {
                            mListCategoryExpenseName.add(item.getName());
                            mListCategoryExpenseId.add(item.getId());
                        }
                        setupSpinner(mListCategoryExpenseName, mListCategoryExpenseId);
                    }
                }

                @Override
                public void onFailure(Call<List<CategoryModel>> call, Throwable t) {
                    Toast.makeText(TransactionUpdateActivity.this, "Lỗi kết nối khi lấy danh mục", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void setupSpinner(List<String> categoryNames, List<Long> categoryIds) {
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categoryNames);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        binding.categoryTranUpdate.setAdapter(categoryAdapter);
        binding.categoryTranUpdate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                categoryIdSelected = categoryIds.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                categoryIdSelected = null;
            }
        });
    }

    private void setEvent() {
        binding.transactionDateUpdate.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            new DatePickerDialog(this, (view, year1, monthOfYear, dayOfMonth) -> {
                String dayString = dayOfMonth < 10 ? "0" + dayOfMonth : String.valueOf(dayOfMonth);
                String monthString = (monthOfYear + 1) < 10 ? "0" + (monthOfYear + 1) : String.valueOf(monthOfYear + 1);
                binding.transactionDateUpdate.setText(dayString + "/" + monthString + "/" + year1);
            }, year, month, day).show();
        });

        binding.btnBack.setOnClickListener(v -> finish());

        binding.btnTransactionUpdate.setOnClickListener(v -> {
            if (transactionModel == null) {
                return;
            }
            tranNameUpdateText = binding.transactionNameUpdate.getText().toString();
            String amountText = binding.transactionAmountUpdate.getText().toString();
            tranAmountUpdateText = amountText.isEmpty() ? 0L : Long.parseLong(amountText);
            tranLocationUpdateText = binding.transactionLocationUpdate.getText().toString();
            tranDesUpdateText = binding.transactionDescriptionUpdate.getText().toString();
            tranDateUpdateText = binding.transactionDateUpdate.getText().toString();

            if (tranNameUpdateText.isEmpty() || tranAmountUpdateText <= 0 || tranLocationUpdateText.isEmpty() ||
                    tranDesUpdateText.isEmpty() || tranDateUpdateText.isEmpty() || categoryIdSelected == null) {
                Toast.makeText(this, "Vui lòng điền đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                return;
            }

            TransactionRequest transactionRequest = new TransactionRequest(tranNameUpdateText, tranAmountUpdateText,
                    tranLocationUpdateText, Format.formatDateRequest(tranDateUpdateText), tranDesUpdateText);
            Retrofit retrofit = HTTPService.getInstance();
            HTTPRequest httpRequest = retrofit.create(HTTPRequest.class);
            Call<ApiResponse<Object>> call = httpRequest.updateTransaction(categoryIdSelected, transactionModel.getId(), transactionRequest);
            call.enqueue(new Callback<ApiResponse<Object>>() {
                @Override
                public void onResponse(Call<ApiResponse<Object>> call, Response<ApiResponse<Object>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        ApiResponse apiResponse = response.body();
                        if (apiResponse.getStatus() == 100) {
                            Toast.makeText(TransactionUpdateActivity.this, "Số tiền trong thẻ không đủ để thực hiện giao dịch!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(TransactionUpdateActivity.this, "Sửa giao dịch thành công!", Toast.LENGTH_SHORT).show();
                            navigateToTransactionActivity();
                        }
                    } else {
                        Toast.makeText(TransactionUpdateActivity.this, "Sửa giao dịch không thành công!", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ApiResponse<Object>> call, Throwable t) {
                    Toast.makeText(TransactionUpdateActivity.this, "Lỗi kết nối!", Toast.LENGTH_SHORT).show();
                }
            });
        });

        binding.btnDeleteTransaction.setOnClickListener(v -> {
            if (transactionModel == null) {
                return;
            }
            new AlertDialog.Builder(this)
                    .setMessage("Bạn có thực sự muốn xóa giao dịch?")
                    .setCancelable(false)
                    .setPositiveButton("Có", (dialog, id) -> {
                        Retrofit retrofit = HTTPService.getInstance();
                        HTTPRequest httpRequest = retrofit.create(HTTPRequest.class);
                        Call<ApiResponse<Object>> call = httpRequest.deleteTranscation(transactionModel.getId());
                        call.enqueue(new Callback<ApiResponse<Object>>() {
                            @Override
                            public void onResponse(Call<ApiResponse<Object>> call, Response<ApiResponse<Object>> response) {
                                if (response.isSuccessful() && response.body() != null) {
                                    ApiResponse apiResponse = response.body();
                                    if (apiResponse.getStatus() == 200) {
                                        Toast.makeText(TransactionUpdateActivity.this, apiResponse.getMessage(), Toast.LENGTH_SHORT).show();
                                        navigateToTransactionActivity();
                                    } else {
                                        Toast.makeText(TransactionUpdateActivity.this, "Xóa không thành công. Vui lòng thử lại!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<ApiResponse<Object>> call, Throwable t) {
                                Toast.makeText(TransactionUpdateActivity.this, "Lỗi kết nối!", Toast.LENGTH_SHORT).show();
                            }
                        });
                    })
                    .setNegativeButton("Không", (dialog, id) -> dialog.cancel())
                    .show();
        });
    }

    private void navigateToTransactionActivity() {
        Class<?> targetActivity = transactionModel.getType() == 1L ? TransactionIncomeActivity.class : TransactionExpenseActivity.class;
        startActivity(new Intent(this, targetActivity));
    }
}