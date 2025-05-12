package com.example.appdoan.Fragment.quanlythuchi_app;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlythuchi_app.API.HTTPRequest;
import com.example.quanlythuchi_app.API.HTTPService;
import com.example.quanlythuchi_app.Activity.Budget.BudgetIntroductionActivity;
import com.example.quanlythuchi_app.Activity.Category.CategoryIntroductionActivity;
import com.example.quanlythuchi_app.Activity.Goal.GoalIntroductionActivity;
import com.example.quanlythuchi_app.Activity.Statistical.StatisticalMainActivity;
import com.example.quanlythuchi_app.Activity.Transaction.TransactionExpenseActivity;
import com.example.quanlythuchi_app.Activity.Transaction.TransactionIncomeActivity;
import com.example.quanlythuchi_app.Adapter.NotificationAdapter;
import com.example.quanlythuchi_app.Adapter.TransactionAdapter;
import com.example.quanlythuchi_app.Container.Response.ApiResponse;
import com.example.quanlythuchi_app.Container.Response.RegisterUserResponse;
import com.example.quanlythuchi_app.Helper.LoadingDialog;
import com.example.quanlythuchi_app.Model.NotificationModel;
import com.example.quanlythuchi_app.Model.TransactionModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class HomeActivity extends AppCompatActivity {

    LoadingDialog loadingDialog = new LoadingDialog(HomeActivity.this);

    List<NotificationModel> mListNotification;

    Spinner spnTranFilter;
    BottomNavigationView navbar;
    LinearLayout income, category, expense, budget, goal, statistical;
    TextView profileName, notificationCount;
    RecyclerView rvTransaction;

    ImageView btnNotification;

    private List<TransactionModel> mListTransaction;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        navbar = findViewById(R.id.navbar);
        navbar.setSelectedItemId(R.id.home);
        income = findViewById(R.id.incomeLayout);
        category = findViewById(R.id.categoryLayout);
        expense = findViewById(R.id.expenseLayout);
        budget = findViewById(R.id.budgetLayout);
        goal = findViewById(R.id.goalLayout);
        statistical = findViewById(R.id.statisticalLayout);
        profileName = findViewById(R.id.profileName);
        notificationCount = findViewById(R.id.notificationCount);
        spnTranFilter = findViewById(R.id.tranFilter);
        rvTransaction = findViewById(R.id.listTransactionRecently);
        btnNotification = findViewById(R.id.notification);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvTransaction.setLayoutManager(linearLayoutManager);

        DividerItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        rvTransaction.addItemDecoration(itemDecoration);

        setSpinnerTranFilter();
        mListTransaction = new ArrayList<>();

        setEvent();

        Retrofit retrofit = HTTPService.getInstance();
        HTTPRequest httpRequest = retrofit.create(HTTPRequest.class);
        Call<RegisterUserResponse> call = httpRequest.getProfile();
        call.enqueue(new Callback<RegisterUserResponse>() {
            @Override
            public void onResponse(Call<RegisterUserResponse> call, Response<RegisterUserResponse> response) {
                if(response.isSuccessful())
                {
                    RegisterUserResponse registerUserResponse = response.body();
                    if(registerUserResponse.isStatus() == true)
                    {
                        profileName.setText(registerUserResponse.getUserInfoModel().getFirstname() + " " + registerUserResponse.getUserInfoModel().getLastname());
                    }
                }
            }

            @Override
            public void onFailure(Call<RegisterUserResponse> call, Throwable t) {
            }
        });

        navbar.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId())
                {
                    case R.id.home:
                        return true;
                    case R.id.report:
                        loadingDialog.startLoadingDialog();
                        loadingDialog.setDialogDuration(2000);
                        startActivity(new Intent(getApplicationContext(), StatisticalChartActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.card:
                        loadingDialog.startLoadingDialog();
                        loadingDialog.setDialogDuration(2000);
                        startActivity(new Intent(getApplicationContext(), CardIntroductionActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.setting:
                        loadingDialog.startLoadingDialog();
                        loadingDialog.setDialogDuration(2000);
                        startActivity(new Intent(getApplicationContext(), SettingActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                }
                return false;
            }
        });

        income.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, TransactionIncomeActivity.class);
                startActivity(intent);
            }
        });

        category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, CategoryIntroductionActivity.class);
                startActivity(intent);
            }
        });

        expense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, TransactionExpenseActivity.class);
                startActivity(intent);
            }
        });

        budget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, BudgetIntroductionActivity.class);
                startActivity(intent);
            }
        });

        goal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, GoalIntroductionActivity.class);
                startActivity(intent);
            }
        });

        statistical.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, StatisticalMainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setSpinnerTranFilter() {
        List<String> listCategoryType = new ArrayList<>();
        listCategoryType.add("Trong tháng");
        listCategoryType.add("Trong tuần");
        listCategoryType.add("Trong ngày");
        ArrayAdapter<String> categoryTypeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, listCategoryType);
        categoryTypeAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spnTranFilter.setAdapter(categoryTypeAdapter);
    }

    private void setEvent()
    {
        spnTranFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Retrofit retrofit = HTTPService.getInstance();
                HTTPRequest httpRequest = retrofit.create(HTTPRequest.class);
                String selectedItem = adapterView.getItemAtPosition(i).toString();
                if(selectedItem.equals("Trong tháng"))
                {
                    Call<ApiResponse<Object>> call = httpRequest.getAllTransactionInCurrentMonth();
                    call.enqueue(new Callback<ApiResponse<Object>>() {
                        @Override
                        public void onResponse(Call<ApiResponse<Object>> call, Response<ApiResponse<Object>> response) {
                            ApiResponse<Object> apiResponse = response.body();
                            if(apiResponse.getStatus() == 101)
                            {
                                Toast.makeText(HomeActivity.this, "Chưa có giao dịch để hiển thị!", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Gson gson = new Gson();
                                String jsonData = gson.toJson(apiResponse.getData());
                                mListTransaction = gson.fromJson(jsonData, new TypeToken<List<TransactionModel>>(){}.getType());
                                Collections.reverse(mListTransaction);
                                TransactionAdapter transactionAdapter = new TransactionAdapter(mListTransaction, HomeActivity.this);
                                rvTransaction.setAdapter(transactionAdapter);
                            }
                        }
                        @Override
                        public void onFailure(Call<ApiResponse<Object>> call, Throwable t) {
                        }
                    });
                }
                else if(selectedItem.equals("Trong tuần"))
                {
                    Call<ApiResponse<Object>> call = httpRequest.getAllTransactionOfWeek();
                    call.enqueue(new Callback<ApiResponse<Object>>() {
                        @Override
                        public void onResponse(Call<ApiResponse<Object>> call, Response<ApiResponse<Object>> response) {
                            ApiResponse<Object> apiResponse = response.body();
                            if(apiResponse.getStatus() == 101)
                            {
                                Toast.makeText(HomeActivity.this, "Chưa có giao dịch để hiển thị!", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Gson gson = new Gson();
                                String jsonData = gson.toJson(apiResponse.getData());
                                mListTransaction = gson.fromJson(jsonData, new TypeToken<List<TransactionModel>>(){}.getType());

                                Collections.reverse(mListTransaction);
                                TransactionAdapter transactionAdapter = new TransactionAdapter(mListTransaction, HomeActivity.this);
                                rvTransaction.setAdapter(transactionAdapter);
                            }
                        }
                        @Override
                        public void onFailure(Call<ApiResponse<Object>> call, Throwable t) {
                        }
                    });
                }
                else {
                    Call<ApiResponse<Object>> call = httpRequest.getAllTransactionToday();
                    call.enqueue(new Callback<ApiResponse<Object>>() {
                        @Override
                        public void onResponse(Call<ApiResponse<Object>> call, Response<ApiResponse<Object>> response) {
                            ApiResponse<Object> apiResponse = response.body();
                            if(apiResponse.getStatus() == 101)
                            {
                                Toast.makeText(HomeActivity.this, "Chưa có giao dịch để hiển thị!", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Gson gson = new Gson();
                                String jsonData = gson.toJson(apiResponse.getData());
                                mListTransaction = gson.fromJson(jsonData, new TypeToken<List<TransactionModel>>(){}.getType());
                                Collections.reverse(mListTransaction);
                                TransactionAdapter transactionAdapter = new TransactionAdapter(mListTransaction, HomeActivity.this);
                                rvTransaction.setAdapter(transactionAdapter);
                            }
                        }
                        @Override
                        public void onFailure(Call<ApiResponse<Object>> call, Throwable t) {
                        }
                    });
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        btnNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(HomeActivity.this);
                dialog.setContentView(R.layout.activity_notification_dialog);
                dialog.setTitle("Thông báo");

                // Set dialog width and height
                Window dialogWindow = dialog.getWindow();
                if (dialogWindow != null) {
                    WindowManager.LayoutParams layoutParams = dialogWindow.getAttributes();
                    layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT; // Set width to match parent
                    layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT; // Set height to wrap content
                    dialogWindow.setAttributes(layoutParams);
                    dialogWindow.setGravity(Gravity.TOP);
                }

                Retrofit retrofit = HTTPService.getInstance();
                HTTPRequest httpRequest = retrofit.create(HTTPRequest.class);
                Call<ApiResponse<Object>> call = httpRequest.listNotification();
                call.enqueue(new Callback<ApiResponse<Object>>() {
                    @Override
                    public void onResponse(Call<ApiResponse<Object>> call, Response<ApiResponse<Object>> response) {
                        if(response.isSuccessful())
                        {
                            ApiResponse apiResponse = response.body();
                            if(apiResponse.getStatus() == 101)
                            {
                                Toast.makeText(HomeActivity.this, apiResponse.getMessage(), Toast.LENGTH_SHORT).show();
                                notificationCount.setText("0");
                            }
                            else
                            {
                                Gson gson = new Gson();
                                String jsonData = gson.toJson(apiResponse.getData());
                                mListNotification = gson.fromJson(jsonData, new TypeToken<List<NotificationModel>>(){}.getType());
                                int count = mListNotification.size();
                                String countText = String.valueOf(count);
                                notificationCount.setText(countText);
                                RecyclerView rcvNotification = dialog.findViewById(R.id.notificationRecyclerView);
                                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(HomeActivity.this);
                                rcvNotification.setLayoutManager(linearLayoutManager);

                                DividerItemDecoration itemDecoration = new DividerItemDecoration(HomeActivity.this, DividerItemDecoration.VERTICAL);
                                rcvNotification.addItemDecoration(itemDecoration);
                                NotificationAdapter notificationAdapter = new NotificationAdapter(HomeActivity.this, mListNotification);
                                rcvNotification.setAdapter(notificationAdapter);
                                dialog.show();
                            }
                        }
                    }
                    @Override
                    public void onFailure(Call<ApiResponse<Object>> call, Throwable t) {
                    }
                });
            }
        });


    }
}