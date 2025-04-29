package com.example.appdoan;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.appdoan.API.HTTPRequest;
import com.example.appdoan.API.HTTPService;
import com.example.appdoan.Activity.CategoryActivity;
import com.example.appdoan.Container.Request.CategoryRequest;
import com.example.appdoan.Container.Response.ApiResponse;
import com.example.appdoan.Helper.Format;
import com.example.appdoan.Model.CategoryModel;
import com.example.appdoan.databinding.ActivityCategoryUpdateBinding;
import com.skydoves.colorpickerview.ColorPickerDialog;
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class CategoryUpdateActivity extends AppCompatActivity {

    private ActivityCategoryUpdateBinding binding;

    String catName_update_text, catColor_update_text, catDes_update_text, catType_update_text;
    Long catType_update;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCategoryUpdateBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setData();
        setEvent();
        updateCategory();
    }

    private void updateCategory() {
        binding.btnCategoryUpdate.setOnClickListener(view -> {
            Retrofit retrofit = HTTPService.getInstance();
            HTTPRequest httpRequest = retrofit.create(HTTPRequest.class);
            catName_update_text = binding.cateNameUpdate.getText().toString();
            catDes_update_text = binding.cateDescUpdate.getText().toString();

            if (catDes_update_text.isEmpty() || catName_update_text.isEmpty()) {
                Toast.makeText(this, "Vui lòng điền đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                return;
            }

            Bundle bundle = getIntent().getExtras();
            if (bundle == null) return;

            CategoryModel categoryModel = (CategoryModel) bundle.get("category_object");
            CategoryRequest categoryRequest = new CategoryRequest(catDes_update_text, catName_update_text, catColor_update_text, catType_update);
            Call<ApiResponse<Object>> call = httpRequest.updateCategory(categoryModel.getId(), categoryRequest);
            call.enqueue(new Callback<>() {
                @Override
                public void onResponse(Call<ApiResponse<Object>> call, Response<ApiResponse<Object>> response) {
                    if (response.isSuccessful()) {
                        ApiResponse apiResponse = response.body();
                        if (apiResponse.getStatus() == 101) {
                            Toast.makeText(CategoryUpdateActivity.this, apiResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(CategoryUpdateActivity.this, apiResponse.getMessage(), Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    } else {
                        Toast.makeText(CategoryUpdateActivity.this, "Không thành công! Vui lòng thử lại!", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ApiResponse<Object>> call, Throwable t) {
                    Toast.makeText(CategoryUpdateActivity.this, "Lỗi kết nối!", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    private void setEvent() {
        binding.cateTypeUpdate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                catType_update_text = (String) adapterView.getItemAtPosition(i);
                catType_update = "Chi tiêu".equals(catType_update_text) ? 2L : 1L;
            }

            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        binding.cateColorUpdate.setOnClickListener(view -> {
            new ColorPickerDialog.Builder(this)
                    .setTitle("Chọn màu cho danh mục")
                    .setPositiveButton("OK", (ColorEnvelopeListener) (envelope, fromUser) -> {
                        catColor_update_text = Format.getRealColor(envelope.getHexCode());
                        binding.cateColorUpdate.getBackground().setColorFilter(envelope.getColor(), PorterDuff.Mode.SRC);
                    })
                    .setNegativeButton("Cancel", (dialogInterface, i) -> dialogInterface.dismiss())
                    .setBottomSpace(12)
                    .show();
        });

        binding.btnDeleteCategory.setOnClickListener(view -> {
            new AlertDialog.Builder(this)
                    .setMessage("Bạn có thực sự muốn xóa thẻ?")
                    .setCancelable(false)
                    .setPositiveButton("Có", (dialog, id) -> deleteCategory())
                    .setNegativeButton("Không", (dialog, id) -> dialog.cancel())
                    .create().show();
        });
        binding.btnBack.setOnClickListener(view -> finish());
    }

    private void deleteCategory() {
        Bundle bundle = getIntent().getExtras();
        if (bundle == null) return;

        CategoryModel categoryModel = (CategoryModel) bundle.get("category_object");
        HTTPRequest httpRequest = HTTPService.getInstance().create(HTTPRequest.class);
        Call<ApiResponse<Object>> call = httpRequest.deleteCategory(categoryModel.getId());

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<ApiResponse<Object>> call, Response<ApiResponse<Object>> response) {
                if (response.isSuccessful()) {
                    ApiResponse apiResponse = response.body();
                    if (apiResponse.getStatus() == 101) {
                        Toast.makeText(CategoryUpdateActivity.this, apiResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(CategoryUpdateActivity.this, "Xóa danh mục thành công!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(CategoryUpdateActivity.this, CategoryActivity.class));
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Object>> call, Throwable t) {
                Toast.makeText(CategoryUpdateActivity.this, "Lỗi kết nối!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle == null) return;

        CategoryModel categoryModel = (CategoryModel) bundle.get("category_object");

        binding.cateNameUpdate.setText(categoryModel.getName());
        binding.cateDescUpdate.setText(categoryModel.getDescription());
        binding.cateColorUpdate.getBackground().setColorFilter(Color.parseColor(categoryModel.getColor()), PorterDuff.Mode.SRC);
        catColor_update_text = categoryModel.getColor();

        List<String> listCategoryType = new ArrayList<>();
        if (categoryModel.getType() == 1) {
            listCategoryType.add("Thu nhập");
            listCategoryType.add("Chi tiêu");
        } else {
            listCategoryType.add("Chi tiêu");
            listCategoryType.add("Thu nhập");
        }

        ArrayAdapter<String> categoryTypeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, listCategoryType);
        categoryTypeAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        binding.cateTypeUpdate.setAdapter(categoryTypeAdapter);
    }
}
