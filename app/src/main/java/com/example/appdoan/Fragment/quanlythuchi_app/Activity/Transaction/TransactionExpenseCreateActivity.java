package com.example.appdoan.Fragment.quanlythuchi_app.Activity.Transaction;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quanlythuchi_app.API.HTTPRequest;
import com.example.quanlythuchi_app.API.HTTPService;
import com.example.quanlythuchi_app.Container.Request.TransactionRequest;
import com.example.quanlythuchi_app.Container.Response.ApiResponse;
import com.example.quanlythuchi_app.Helper.Format;
import com.example.quanlythuchi_app.Model.CardModel;
import com.example.quanlythuchi_app.Model.CategoryModel;
import com.example.quanlythuchi_app.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class TransactionExpenseCreateActivity extends AppCompatActivity {

    String categorySelected, cardSelected;
    // ===== TransactionRequest
    String tranNameRequest, tranLocationRequest, tranDateRequest, tranDesRequest;
    Long tranAmountRequest;

    // =============
    Long categoryIdSelected, cardIdSelected;
    List<CategoryModel> mListCategoryExpense;
    List<CardModel> mListCard;
    List<String> mListCategoryName, mListCardName;
    List<Long> mListCategoryId, mListCardId;
    ImageButton btnBack;
    Spinner spnCard, spnCategory;
    EditText edtTranName, edtTranAmount, edtTranLocation, edtTranDes, edtTranDate;
    Button btnTranCreate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_expense_create);
        btnBack = findViewById(R.id.btnBack);
        spnCard = findViewById(R.id.cardCreateExpense);
        spnCategory = findViewById(R.id.categoryCreateExpense);
        edtTranName = findViewById(R.id.transactionExpenseNameCreate);
        edtTranAmount = findViewById(R.id.transactionExpenseAmountCreate);
        edtTranLocation = findViewById(R.id.transactionExpenseLocationCreate);
        edtTranDes = findViewById(R.id.transactionExpenseDescriptionCreate);
        edtTranDate = findViewById(R.id.transactionExpenseDateCreate);
        btnTranCreate = findViewById(R.id.btnTransactionExpenseCreate);

        setEvent();
        setSpinner();

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void setSpinner() {
        Retrofit retrofit = HTTPService.getInstance();
        HTTPRequest httpRequest = retrofit.create(HTTPRequest.class);
        Call<List<CategoryModel>> call = httpRequest.expenseCategory();
        call.enqueue(new Callback<List<CategoryModel>>() {
            @Override
            public void onResponse(Call<List<CategoryModel>> call, Response<List<CategoryModel>> response) {
                if(response.isSuccessful())
                {
                    mListCategoryExpense = response.body();
                    mListCategoryName = new ArrayList<>();
                    mListCategoryId = new ArrayList<>();
                    for(CategoryModel item : mListCategoryExpense)
                    {
                        mListCategoryName.add(item.getName());
                        mListCategoryId.add(item.getId());
                    }
                    ArrayAdapter<String> categoryName = new ArrayAdapter<>(TransactionExpenseCreateActivity.this, android.R.layout.simple_spinner_item, mListCategoryName);
                    categoryName.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                    spnCategory.setAdapter(categoryName);

                    spnCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            categorySelected = (String) adapterView.getItemAtPosition(i);
                            categoryIdSelected = mListCategoryId.get(i);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {
                        }
                    });
                }
            }
            @Override
            public void onFailure(Call<List<CategoryModel>> call, Throwable t) {
            }
        });

        Call<List<CardModel>> call1 = httpRequest.getAllCard();
        call1.enqueue(new Callback<List<CardModel>>() {
            @Override
            public void onResponse(Call<List<CardModel>> call, Response<List<CardModel>> response) {
                if(response.isSuccessful())
                {
                    mListCard = response.body();
                    mListCardName = new ArrayList<>();
                    mListCardId = new ArrayList<>();
                    for(CardModel item : mListCard)
                    {
                        mListCardName.add(item.getName());
                        mListCardId.add(item.getId());
                    }
                    ArrayAdapter<String> cardName = new ArrayAdapter<>(TransactionExpenseCreateActivity.this, android.R.layout.simple_spinner_item, mListCardName);
                    cardName.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                    spnCard.setAdapter(cardName);

                    spnCard.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            cardSelected = (String) adapterView.getItemAtPosition(i);
                            cardIdSelected = mListCardId.get(i);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });
                }
            }
            @Override
            public void onFailure(Call<List<CardModel>> call, Throwable t) {
            }
        });
    }

    private void setEvent() {
        edtTranDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(TransactionExpenseCreateActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                        String dayString = (dayOfMonth < 10) ? "0" + dayOfMonth : String.valueOf(dayOfMonth);
                        String monthString = ((monthOfYear + 1) < 10) ? "0" + (monthOfYear + 1) : String.valueOf(monthOfYear + 1);
                        edtTranDate.setText(dayString + "/" + monthString + "/" + year);
                    }}, year, month, day);
                datePickerDialog.show();
            }
        });

        btnTranCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tranNameRequest = edtTranName.getText().toString();
                if(edtTranAmount.getText().toString().isEmpty())
                {
                    tranAmountRequest = 0L;
                }
                else {
                    tranAmountRequest = Long.parseLong(edtTranAmount.getText().toString());
                }
                tranLocationRequest = edtTranLocation.getText().toString();
                tranDesRequest = edtTranDes.getText().toString();
                tranDateRequest = edtTranDate.getText().toString();
                if(tranNameRequest.isEmpty() || tranAmountRequest <= 0 || tranLocationRequest.isEmpty() || tranDesRequest.isEmpty() || tranDateRequest.isEmpty())
                {
                    Toast.makeText(TransactionExpenseCreateActivity.this, "Vui lòng điền đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                }
                else {
                    TransactionRequest transactionRequest = new TransactionRequest(tranNameRequest, tranAmountRequest, tranLocationRequest, Format.formatDateRequest(tranDateRequest), tranDesRequest);
                    Retrofit retrofit = HTTPService.getInstance();
                    HTTPRequest httpRequest = retrofit.create(HTTPRequest.class);
                    Call<ApiResponse<Object>> call = httpRequest.addTransactionExpense(categoryIdSelected, cardIdSelected, transactionRequest);
                    call.enqueue(new Callback<ApiResponse<Object>>() {
                        @Override
                        public void onResponse(Call<ApiResponse<Object>> call, Response<ApiResponse<Object>> response) {
                            if(response.isSuccessful())
                            {
                                ApiResponse apiResponse = response.body();
                                if(apiResponse.getStatus() == 100)
                                {
                                    Toast.makeText(TransactionExpenseCreateActivity.this, apiResponse.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    Toast.makeText(TransactionExpenseCreateActivity.this, "Thêm giao dịch thành công!", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(TransactionExpenseCreateActivity.this, TransactionExpenseActivity.class);
                                    startActivity(intent);
                                }
                            }
                            else
                            {
                                Toast.makeText(TransactionExpenseCreateActivity.this, "Thêm giao dịch không thành công!", Toast.LENGTH_SHORT).show();
                            }
                        }
                        @Override
                        public void onFailure(Call<ApiResponse<Object>> call, Throwable t) {
                            Toast.makeText(TransactionExpenseCreateActivity.this, "Lỗi kết nối!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }
}