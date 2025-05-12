package com.example.appdoan.Fragment.quanlythuchi_app.Activity.Transaction;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
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
import com.example.quanlythuchi_app.Model.CategoryModel;
import com.example.quanlythuchi_app.Model.TransactionModel;
import com.example.quanlythuchi_app.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class TransactionUpdateActivity extends AppCompatActivity {
    List<CategoryModel> mListCategoryExpense, mListCategoryIncome;
    List<String> mListCategoryIncomeName, mListCategoryExpenseName;
    Long categoryIdSelected, cardIdSelected;
    List<Long> mListCategoryIncomeId, mListCategoryExpenseId;

    EditText tranNameUpdate, tranAmountUpdate, tranLocationUpdate, tranDesUpdate, tranDateUpdate;
    Spinner spnCategoryUpdate;
    Button btnTranUpdate;
    ImageButton btnBack, btnDeleteTransaction;

    // request
    String tranNameUpdateText, tranLocationUpdateText, tranDesUpdateText, tranDateUpdateText;
    Long tranAmountUpdateText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_update);
        btnBack = findViewById(R.id.btnBack);
        btnTranUpdate = findViewById(R.id.btnTransactionUpdate);
        btnDeleteTransaction = findViewById(R.id.btnDeleteTransaction);
        tranNameUpdate = findViewById(R.id.transactionNameUpdate);
        tranAmountUpdate = findViewById(R.id.transactionAmountUpdate);
        tranLocationUpdate = findViewById(R.id.transactionLocationUpdate);
        tranDesUpdate = findViewById(R.id.transactionDescriptionUpdate);
        tranDateUpdate = findViewById(R.id.transactionDateUpdate);
        spnCategoryUpdate = findViewById(R.id.categoryTranUpdate);

        setSpinner();
        setData();
        setEvent();
    }

    private void setData() {
        Bundle bundle = getIntent().getExtras();
        if(bundle == null)
        {
            return;
        }
        TransactionModel transactionModel = (TransactionModel) bundle.get("transaction_object_update");
        tranNameUpdate.setText(transactionModel.getName());
        tranAmountUpdate.setText(transactionModel.getAmount().toString());
        tranLocationUpdate.setText(transactionModel.getLocation());
        tranDesUpdate.setText(transactionModel.getDescription());
        tranDateUpdate.setText(Format.formatDate(transactionModel.getTransactiondate()));
    }

    private void setSpinner() {
        Retrofit retrofit = HTTPService.getInstance();
        HTTPRequest httpRequest = retrofit.create(HTTPRequest.class);

        // kiểm tra xem loại của giao dịch đang chỉnh sửa là gì
        Bundle bundle = getIntent().getExtras();
        if(bundle == null)
        {
            return;
        }
        TransactionModel transactionModel = (TransactionModel) bundle.get("transaction_object_update");
        // nếu là giao dịch thu nhập
        if(transactionModel.getType() == 1L)
        {
            Call<List<CategoryModel>> call = httpRequest.incomeCategory();
            call.enqueue(new Callback<List<CategoryModel>>() {
                @Override
                public void onResponse(Call<List<CategoryModel>> call, Response<List<CategoryModel>> response) {
                    if(response.isSuccessful())
                    {
                        mListCategoryIncome = response.body();
                        mListCategoryIncomeName = new ArrayList<>();
                        mListCategoryIncomeId = new ArrayList<>();
                        for(CategoryModel item : mListCategoryIncome)
                        {
                            mListCategoryIncomeName.add(item.getName());
                            mListCategoryIncomeId.add(item.getId());
                        }
                        ArrayAdapter<String> categoryName = new ArrayAdapter<>(TransactionUpdateActivity.this, android.R.layout.simple_spinner_item, mListCategoryIncomeName);
                        categoryName.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                        spnCategoryUpdate.setAdapter(categoryName);

                        spnCategoryUpdate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                categoryIdSelected = mListCategoryIncomeId.get(i);
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
        }

        // nếu là giao dịch chi tiêu
        else
        {
            Call<List<CategoryModel>> call = httpRequest.expenseCategory();
            call.enqueue(new Callback<List<CategoryModel>>() {
                @Override
                public void onResponse(Call<List<CategoryModel>> call, Response<List<CategoryModel>> response) {
                    if(response.isSuccessful())
                    {
                        mListCategoryExpense = response.body();
                        mListCategoryExpenseName = new ArrayList<>();
                        mListCategoryExpenseId = new ArrayList<>();
                        for(CategoryModel item : mListCategoryExpense)
                        {
                            mListCategoryExpenseName.add(item.getName());
                            mListCategoryExpenseId.add(item.getId());
                        }
                        ArrayAdapter<String> categoryName = new ArrayAdapter<>(TransactionUpdateActivity.this, android.R.layout.simple_spinner_item, mListCategoryExpenseName);
                        categoryName.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                        spnCategoryUpdate.setAdapter(categoryName);

                        spnCategoryUpdate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                categoryIdSelected = mListCategoryExpenseId.get(i);
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
        }
    }

    private void setEvent() {

        tranDateUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(TransactionUpdateActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                        String dayString = (dayOfMonth < 10) ? "0" + dayOfMonth : String.valueOf(dayOfMonth);
                        String monthString = ((monthOfYear + 1) < 10) ? "0" + (monthOfYear + 1) : String.valueOf(monthOfYear + 1);
                        tranDateUpdate.setText(dayString + "/" + monthString + "/" + year);
                    }}, year, month, day);
                datePickerDialog.show();
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnTranUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = getIntent().getExtras();
                if(bundle == null)
                {
                    return;
                }
                TransactionModel transactionModel = (TransactionModel) bundle.get("transaction_object_update");
                tranNameUpdateText = tranNameUpdate.getText().toString();
                if(tranAmountUpdate.getText().toString().isEmpty())
                {
                    tranAmountUpdateText = 0L;
                }
                else {
                    tranAmountUpdateText = Long.parseLong(tranAmountUpdate.getText().toString());
                }
                tranLocationUpdateText = tranLocationUpdate.getText().toString();
                tranDesUpdateText = tranDesUpdate.getText().toString();
                tranDateUpdateText = tranDateUpdate.getText().toString();
                if(tranNameUpdateText.isEmpty() || tranAmountUpdateText <= 0 || tranLocationUpdateText.isEmpty() || tranDesUpdateText.isEmpty() || tranDateUpdateText.isEmpty())
                {
                    Toast.makeText(TransactionUpdateActivity.this, "Vui lòng điền đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                }
                else{
                    TransactionRequest transactionRequest = new TransactionRequest(tranNameUpdateText, tranAmountUpdateText, tranLocationUpdateText, Format.formatDateRequest(tranDateUpdateText), tranDesUpdateText);
                    Retrofit retrofit = HTTPService.getInstance();
                    HTTPRequest httpRequest = retrofit.create(HTTPRequest.class);
                    Call<ApiResponse<Object>> call = httpRequest.updateTransaction(categoryIdSelected,transactionModel.getId() , transactionRequest);
                    call.enqueue(new Callback<ApiResponse<Object>>() {
                        @Override
                        public void onResponse(Call<ApiResponse<Object>> call, Response<ApiResponse<Object>> response) {
                            if(response.isSuccessful())
                            {
                                ApiResponse apiResponse = response.body();
                                if(apiResponse.getStatus() == 100)
                                {
                                    Toast.makeText(TransactionUpdateActivity.this, "Số tiền trong thẻ không đủ để thực hiện giao dịch!", Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    Toast.makeText(TransactionUpdateActivity.this, "Sửa giao dịch thành công!", Toast.LENGTH_SHORT).show();
                                    if(transactionModel.getType() == 1L)
                                    {
                                        Intent intent = new Intent(TransactionUpdateActivity.this, TransactionIncomeActivity.class);
                                        startActivity(intent);
                                    }
                                    else
                                    {
                                        Intent intent = new Intent(TransactionUpdateActivity.this, TransactionExpenseActivity.class);
                                        startActivity(intent);
                                    }
                                }
                            }
                            else
                            {
                                Toast.makeText(TransactionUpdateActivity.this, "Sửa giao dịch không thành công!", Toast.LENGTH_SHORT).show();
                            }
                        }
                        @Override
                        public void onFailure(Call<ApiResponse<Object>> call, Throwable t) {
                            Toast.makeText(TransactionUpdateActivity.this, "Lỗi kết nối!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        btnDeleteTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(TransactionUpdateActivity.this);
                builder.setMessage("Bạn có thực sự muốn xóa giao dịch?")
                        .setCancelable(false)
                        .setPositiveButton("Có", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Bundle bundle = getIntent().getExtras();
                                if(bundle == null)
                                {
                                    return;
                                }
                                TransactionModel transactionModel = (TransactionModel) bundle.get("transaction_object_update");
                                Retrofit retrofit = HTTPService.getInstance();
                                HTTPRequest httpRequest = retrofit.create(HTTPRequest.class);
                                Call<ApiResponse<Object>> call = httpRequest.deleteTranscation(transactionModel.getId());
                                call.enqueue(new Callback<ApiResponse<Object>>() {
                                    @Override
                                    public void onResponse(Call<ApiResponse<Object>> call, Response<ApiResponse<Object>> response) {
                                        if(response.isSuccessful())
                                        {
                                            ApiResponse apiResponse = response.body();
                                            if(apiResponse.getStatus()  == 200)
                                            {
                                                Toast.makeText(TransactionUpdateActivity.this, apiResponse.getMessage(), Toast.LENGTH_SHORT).show();
                                                if(transactionModel.getType() == 1L)
                                                {
                                                    Intent intent = new Intent(TransactionUpdateActivity.this, TransactionIncomeActivity.class);
                                                    startActivity(intent);
                                                }
                                                else
                                                {
                                                    Intent intent = new Intent(TransactionUpdateActivity.this, TransactionExpenseActivity.class);
                                                    startActivity(intent);
                                                }
                                            }
                                            else {
                                                Toast.makeText(TransactionUpdateActivity.this, "Xóa không thành công. Vui lòng thử lại!", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<ApiResponse<Object>> call, Throwable t) {
                                        Toast.makeText(TransactionUpdateActivity.this, "Lỗi kết nối!", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        })
                        .setNegativeButton("Không", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
    }

}