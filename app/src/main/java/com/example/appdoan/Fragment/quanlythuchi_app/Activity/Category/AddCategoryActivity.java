package com.example.appdoan.Fragment.quanlythuchi_app.Activity.Category;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quanlythuchi_app.API.HTTPRequest;
import com.example.quanlythuchi_app.API.HTTPService;
import com.example.quanlythuchi_app.Container.Request.CategoryRequest;
import com.example.quanlythuchi_app.Container.Response.ApiResponse;
import com.example.quanlythuchi_app.Helper.Format;
import com.example.quanlythuchi_app.R;
import com.skydoves.colorpickerview.ColorPickerDialog;
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class AddCategoryActivity extends AppCompatActivity {

    TextView categoryName, categoryDescription;

    ImageButton categoryColor, btnBack;

    Spinner spnCategoryType;

    Button addCategory;

    String categoryTypeText, categoryNameText, categoryDesText, categoryColorText = "#00ffff";

    Long categoryType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_category);

        categoryName = findViewById(R.id.cate_name);
        categoryColor = findViewById(R.id.cat_color);
        spnCategoryType = findViewById(R.id.cat_type);
        categoryDescription = findViewById(R.id.cate_desc);
        addCategory = findViewById(R.id.categoryCreate);
        btnBack = findViewById(R.id.btnBack);

        setSpinnerTypeCategory();
        setData();
        setEvent();
        createCategory();
    }

    private void createCategory() {
        addCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Retrofit retrofit = HTTPService.getInstance();
                HTTPRequest httpRequest = retrofit.create(HTTPRequest.class);
                categoryNameText = categoryName.getText().toString();
                categoryDesText = categoryDescription.getText().toString();
                if(categoryNameText.isEmpty() || categoryDesText.isEmpty())
                {
                    Toast.makeText(AddCategoryActivity.this, "Vui lòng điền đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                }
                else {
                    CategoryRequest categoryRequest = new CategoryRequest(categoryDesText, categoryNameText, categoryColorText, categoryType);
                    Call<ApiResponse<Object>> call = httpRequest.addCategory(categoryRequest);
                    call.enqueue(new Callback<ApiResponse<Object>>() {
                        @Override
                        public void onResponse(Call<ApiResponse<Object>> call, Response<ApiResponse<Object>> response) {
                            if(response.isSuccessful())
                            {
                                ApiResponse apiResponse = response.body();
                                if(apiResponse.getStatus() == 101)
                                {
                                    Toast.makeText(AddCategoryActivity.this, apiResponse.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    Toast.makeText(AddCategoryActivity.this, apiResponse.getMessage(), Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(AddCategoryActivity.this, CategoryIntroductionActivity.class);
                                    startActivity(intent);
                                }
                            }
                            else{
                                Toast.makeText(AddCategoryActivity.this, "Thêm danh mục không thành công. Vui lòng thử lại!", Toast.LENGTH_SHORT).show();
                            }
                        }
                        @Override
                        public void onFailure(Call<ApiResponse<Object>> call, Throwable t) {
                            Toast.makeText(AddCategoryActivity.this, "Lỗi kết nối!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    private void setSpinnerTypeCategory() {
        List<String> listCategoryType = new ArrayList<>();
        listCategoryType.add("Chi tiêu");
        listCategoryType.add("Thu nhập");
        ArrayAdapter<String> categoryTypeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, listCategoryType);
        categoryTypeAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spnCategoryType.setAdapter(categoryTypeAdapter);
    }

    private void setData() {
        categoryColor.getBackground().setColorFilter(Color.parseColor("#00ffff"), PorterDuff.Mode.SRC);
    }

    private void setEvent() {
        spnCategoryType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                categoryTypeText = (String) adapterView.getItemAtPosition(i);
                if(categoryTypeText == "Chi tiêu")
                {
                    categoryType = 2L;
                }
                else {
                    categoryType = 1L;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        categoryColor.setOnClickListener(view -> {
            new ColorPickerDialog.Builder(AddCategoryActivity.this)
                    .setTitle("Chọn màu cho danh mục")
                    .setPositiveButton(getString(R.string.ok),
                            (ColorEnvelopeListener) (envelope, fromUser) -> {
                                categoryColorText = Format.getRealColor(envelope.getHexCode());
                                categoryColor.getBackground().setColorFilter(envelope.getColor(), PorterDuff.Mode.SRC);
                            })
                    .setNegativeButton(getString(R.string.cancel),
                            (dialogInterface, i) -> dialogInterface.dismiss())
                    .setBottomSpace(12)
                    .show();
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}