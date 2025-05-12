package com.example.appdoan.Fragment.quanlythuchi_app;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.quanlythuchi_app.API.HTTPRequest;
import com.example.quanlythuchi_app.API.HTTPService;
import com.example.quanlythuchi_app.Activity.Budget.BudgetIntroductionActivity;
import com.example.quanlythuchi_app.Activity.Category.CategoryIntroductionActivity;
import com.example.quanlythuchi_app.Activity.Goal.GoalIntroductionActivity;
import com.example.quanlythuchi_app.Activity.User.UpdateProfileActivity;
import com.example.quanlythuchi_app.Container.Response.RegisterUserResponse;
import com.example.quanlythuchi_app.Helper.LoadingDialog;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SettingActivity extends AppCompatActivity {

    LoadingDialog loadingDialog = new LoadingDialog(SettingActivity.this);

    BottomNavigationView navbar;
    ImageButton logOut;

    TextView fullName, email;

    ImageView imageAvatar;

    LinearLayout profile, resetPass, category, budget, goal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        navbar = findViewById(R.id.navbar);
        navbar.setSelectedItemId(R.id.setting);
        logOut = findViewById(R.id.btnLogout);
        fullName = findViewById(R.id.fullName);
        email = findViewById(R.id.email);
        imageAvatar = findViewById(R.id.ivAvatar);
        profile = findViewById(R.id.profile);
        resetPass = findViewById(R.id.resetPassword);
        category = findViewById(R.id.category);
        budget = findViewById(R.id.budget);
        goal = findViewById(R.id.goal);

        navbar.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.setting:
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
                    case R.id.home:
                        loadingDialog.startLoadingDialog();
                        loadingDialog.setDialogDuration(2000);
                        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                }
                return false;
            }
        });

        // lấy thông tin full name và email
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
                        fullName.setText(registerUserResponse.getUserInfoModel().getFirstname() + " " + registerUserResponse.getUserInfoModel().getLastname());
                        email.setText(registerUserResponse.getUserInfoModel().getAccountModel().getEmail());

                        Glide.with(SettingActivity.this)
                                .load(registerUserResponse.getUserInfoModel().getAvatar())
                                .placeholder(R.drawable.user1)
                                .error(R.drawable.user1)
                                .into(imageAvatar);
                    }
                }
            }
            public void onFailure(Call<RegisterUserResponse> call, Throwable t) {
                Toast.makeText(SettingActivity.this, "Lỗi kết nối!", Toast.LENGTH_SHORT).show();
            }
        });

        // chuyển qua giao diện chỉnh sửa thông tin
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingActivity.this, UpdateProfileActivity.class);
                startActivity(intent);
            }
        });

        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SettingActivity.this);
                builder.setMessage("Bạn có thực sự muốn đăng xuất?")
                        .setCancelable(false)
                        .setPositiveButton("Có", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                SharedPreferences sharedPreferences = getSharedPreferences("MyApp", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.remove("token");
                                editor.apply();
                                Intent intent = new Intent(SettingActivity.this, LoginActivity.class);
                                startActivity(intent);
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

        resetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingActivity.this, ResetPasswordActivity.class);
                startActivity(intent);
            }
        });

        category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingActivity.this, CategoryIntroductionActivity.class);
                startActivity(intent);
            }
        });

        budget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingActivity.this, BudgetIntroductionActivity.class);
                startActivity(intent);
            }
        });

        goal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingActivity.this, GoalIntroductionActivity.class);
                startActivity(intent);
            }
        });
    }
}