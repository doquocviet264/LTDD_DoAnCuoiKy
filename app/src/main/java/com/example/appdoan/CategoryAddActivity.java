package com.example.appdoan;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.appdoan.API.HTTPRequest;
import com.example.appdoan.API.HTTPService;
import com.example.appdoan.Activity.CategoryActivity;
import com.example.appdoan.Container.Request.CategoryRequest;
import com.example.appdoan.Container.Response.ApiResponse;
import com.example.appdoan.Helper.Format;
import com.example.appdoan.databinding.ActivityCategoryAddBinding;
import com.skydoves.colorpickerview.ColorPickerDialog;
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CategoryAddActivity extends AppCompatActivity {

    private ActivityCategoryAddBinding binding;
    private String categoryTypeText, categoryNameText, categoryDesText, categoryColorText = "#00ffff";
    private Long categoryType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCategoryAddBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSpinnerTypeCategory();
        setData();
        setEvent();
        createCategory();
    }

    private void createCategory() {
        binding.categoryCreate.setOnClickListener(view -> {
            Retrofit retrofit = HTTPService.getInstance();
            HTTPRequest httpRequest = retrofit.create(HTTPRequest.class);
            categoryNameText = binding.cateName.getText().toString();
            categoryDesText = binding.cateDesc.getText().toString();
            if (categoryNameText.isEmpty() || categoryDesText.isEmpty()) {
                Toast.makeText(this, "Vui lòng điền đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
            } else {
                CategoryRequest categoryRequest = new CategoryRequest(categoryDesText, categoryNameText, categoryColorText, categoryType);
                Call<ApiResponse<Object>> call = httpRequest.addCategory(categoryRequest);
                call.enqueue(new Callback<ApiResponse<Object>>() {
                    @Override
                    public void onResponse(Call<ApiResponse<Object>> call, Response<ApiResponse<Object>> response) {
                        if (response.isSuccessful()) {
                            ApiResponse apiResponse = response.body();
                            if (apiResponse.getStatus() == 101) {
                                Toast.makeText(CategoryAddActivity.this, apiResponse.getMessage(), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(CategoryAddActivity.this, apiResponse.getMessage(), Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        } else {
                            Toast.makeText(CategoryAddActivity.this, "Thêm danh mục không thành công. Vui lòng thử lại!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse<Object>> call, Throwable t) {
                        Toast.makeText(CategoryAddActivity.this, "Lỗi kết nối!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void setSpinnerTypeCategory() {
        List<String> listCategoryType = new ArrayList<>();
        listCategoryType.add("Chi tiêu");
        listCategoryType.add("Thu nhập");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, listCategoryType);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        binding.catType.setAdapter(adapter);
    }

    private void setData() {
        binding.catcolor.getBackground().setColorFilter(Color.parseColor("#00ffff"), PorterDuff.Mode.SRC);
    }

    private void setEvent() {
        binding.catType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                categoryTypeText = (String) adapterView.getItemAtPosition(i);
                categoryType = categoryTypeText.equals("Chi tiêu") ? 2L : 1L;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        binding.catcolor.setOnClickListener(view -> new ColorPickerDialog.Builder(CategoryAddActivity.this)
                .setTitle("Chọn màu cho danh mục")
                .setPositiveButton("OK", (ColorEnvelopeListener) (envelope, fromUser) -> {
                    categoryColorText = Format.getRealColor(envelope.getHexCode());
                    binding.catcolor.getBackground().setColorFilter(envelope.getColor(), PorterDuff.Mode.SRC);
                })
                .setNegativeButton("Cancel", (dialogInterface, i) -> dialogInterface.dismiss())
                .setBottomSpace(12)
                .show());

        binding.btnBack.setOnClickListener(view -> finish());
    }
}