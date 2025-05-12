package com.example.appdoan.Fragment.quanlythuchi_app.Activity.User;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quanlythuchi_app.API.HTTPRequest;
import com.example.quanlythuchi_app.API.HTTPService;
import com.example.quanlythuchi_app.Container.Request.UpdateProfileRequest;
import com.example.quanlythuchi_app.Container.Response.ApiResponse;
import com.example.quanlythuchi_app.Container.Response.RegisterUserResponse;
import com.example.quanlythuchi_app.R;
import com.example.quanlythuchi_app.SettingActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class UpdateProfileActivity extends AppCompatActivity {

    EditText edtFirstname, edtLastname, edtAvatar;

    Button btnEditInfo;

    ImageView btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);

        btnBack = findViewById(R.id.btnBack);
        edtFirstname = findViewById(R.id.firstname);
        edtLastname = findViewById(R.id.lastname);
        edtAvatar = findViewById(R.id.avatar);
        btnEditInfo = findViewById(R.id.btnUpdateProfile);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        // lấy thông tin user set vào các text
        Retrofit retrofit = HTTPService.getInstance();
        HTTPRequest httpRequest = retrofit.create(HTTPRequest.class);
//        SharedPreferences sharedPreferences = getSharedPreferences("MyApp", Context.MODE_PRIVATE);
//        String token = sharedPreferences.getString("token", "");
        Call<RegisterUserResponse> call = httpRequest.getProfile();
        call.enqueue(new Callback<RegisterUserResponse>() {
            @Override
            public void onResponse(Call<RegisterUserResponse> call, Response<RegisterUserResponse> response) {
                if(response.isSuccessful())
                {
                    RegisterUserResponse registerUserResponse = response.body();
                    if(registerUserResponse.isStatus() == true)
                    {
                        edtFirstname.setText(registerUserResponse.getUserInfoModel().getFirstname());
                        edtLastname.setText(registerUserResponse.getUserInfoModel().getLastname());
                        edtAvatar.setText(registerUserResponse.getUserInfoModel().getAvatar());

                    }
                }
            }

            @Override
            public void onFailure(Call<RegisterUserResponse> call, Throwable t) {
                Toast.makeText(UpdateProfileActivity.this, "Lỗi kết nối!", Toast.LENGTH_SHORT).show();
            }
        });

        btnEditInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Retrofit retrofit = HTTPService.getInstance();
                HTTPRequest httpRequest = retrofit.create(HTTPRequest.class);

                String firstName = edtFirstname.getText().toString();
                String lastName = edtLastname.getText().toString();
                String avatar = edtAvatar.getText().toString();

                if(firstName.isEmpty() || lastName.isEmpty() || avatar.isEmpty())
                {
                    Toast.makeText(UpdateProfileActivity.this, "Vui lòng điền đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                }
                else {
                    UpdateProfileRequest updateProfileRequest = new UpdateProfileRequest(firstName, lastName, avatar);
                    Call<ApiResponse<Object>> call = httpRequest.updateProfile(updateProfileRequest);
                    call.enqueue(new Callback<ApiResponse<Object>>() {
                        @Override
                        public void onResponse(Call<ApiResponse<Object>> call, Response<ApiResponse<Object>> response) {
                            if(response.isSuccessful())
                            {
                                ApiResponse<Object> apiResponse = response.body();
                                if(apiResponse.getStatus() == 200)
                                {
                                    Toast.makeText(UpdateProfileActivity.this, "Chỉnh sửa thông tin thành công!", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(UpdateProfileActivity.this, SettingActivity.class);
                                    startActivity(intent);
                                }
                                else {
                                    Toast.makeText(UpdateProfileActivity.this, "Không thành công. Vui lòng kiểm tra lại!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                        @Override
                        public void onFailure(Call<ApiResponse<Object>> call, Throwable t) {
                            Toast.makeText(UpdateProfileActivity.this, "Lỗi kết nối!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }
}