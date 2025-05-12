package com.example.appdoan.Fragment.quanlythuchi_app.Activity.Category;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import com.example.quanlythuchi_app.Model.CategoryModel;
import com.example.quanlythuchi_app.R;
import com.skydoves.colorpickerview.ColorPickerDialog;
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class UpdateCategoryActivity extends AppCompatActivity {
    TextView cat_name, cat_des;
    ImageButton cat_color, btnBack, btnDeleteCategory;
    Spinner cat_type;
    Button btnUpdateCategory;

    String catName_update_text, catColor_update_text, catDes_update_text, catType_update_text;

    Long catType_update;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_category);

        cat_name = findViewById(R.id.cate_name_update);
        cat_des = findViewById(R.id.cate_desc_update);
        cat_color = findViewById(R.id.cate_color_update);
        cat_type = findViewById(R.id.cate_type_update);
        btnBack = findViewById(R.id.btnBack);
        btnDeleteCategory = findViewById(R.id.btnDeleteCategory);
        btnUpdateCategory = findViewById(R.id.categoryUpdate);

        setData();
        setEvent();
        updateCategory();
    }

    private void updateCategory() {
        btnUpdateCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Retrofit retrofit = HTTPService.getInstance();
                HTTPRequest httpRequest = retrofit.create(HTTPRequest.class);
                catName_update_text = cat_name.getText().toString();
                catDes_update_text = cat_des.getText().toString();
                if(catDes_update_text.isEmpty() || catName_update_text.isEmpty())
                {
                    Toast.makeText(UpdateCategoryActivity.this, "Vui lòng điền đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                }
                else {
                    Bundle bundle = getIntent().getExtras();
                    if(bundle == null)
                    {
                        return;
                    }
                    CategoryModel categoryModel = (CategoryModel) bundle.get("category_object");
                    CategoryRequest categoryRequest = new CategoryRequest(catDes_update_text, catName_update_text, catColor_update_text, catType_update);
                    Call<ApiResponse<Object>> call = httpRequest.updateCategory(categoryModel.getId(), categoryRequest);
                    call.enqueue(new Callback<ApiResponse<Object>>() {
                        @Override
                        public void onResponse(Call<ApiResponse<Object>> call, Response<ApiResponse<Object>> response) {
                            if(response.isSuccessful())
                            {
                                ApiResponse apiResponse = response.body();
                                if(apiResponse.getStatus() == 101)
                                {
                                    Toast.makeText(UpdateCategoryActivity.this, "Đã tồn tại danh mục với thể loại chi tiêu!", Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    Toast.makeText(UpdateCategoryActivity.this, "Chỉnh sửa danh mục thành công!", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(UpdateCategoryActivity.this, CategoryIntroductionActivity.class);
                                    startActivity(intent);
                                }
                            }
                            else {
                                Toast.makeText(UpdateCategoryActivity.this, "Không thành công! Vui lòng thử lại!", Toast.LENGTH_SHORT).show();
                            }
                        }
                        @Override
                        public void onFailure(Call<ApiResponse<Object>> call, Throwable t) {
                            Toast.makeText(UpdateCategoryActivity.this, "Lỗi kết nối!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }
        });
    }

    private void setEvent() {
        cat_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                catType_update_text = (String) adapterView.getItemAtPosition(i);
                if(catType_update_text == "Chi tiêu")
                {
                    catType_update = 2L;
                }
                else {
                    catType_update = 1L;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        cat_color.setOnClickListener(view -> {
            new ColorPickerDialog.Builder(UpdateCategoryActivity.this)
                    .setTitle("Chọn màu cho danh mục")
                    .setPositiveButton(getString(R.string.ok),
                            (ColorEnvelopeListener) (envelope, fromUser) -> {
                                catColor_update_text = Format.getRealColor(envelope.getHexCode());
                                cat_color.getBackground().setColorFilter(envelope.getColor(), PorterDuff.Mode.SRC);
                            })
                    .setNegativeButton(getString(R.string.cancel),
                            (dialogInterface, i) -> dialogInterface.dismiss())
                    .setBottomSpace(12)
                    .show();
        });

        btnDeleteCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(UpdateCategoryActivity.this);
                builder.setMessage("Bạn có thực sự muốn xóa thẻ?")
                        .setCancelable(false)
                        .setPositiveButton("Có", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Bundle bundle = getIntent().getExtras();
                                if(bundle == null)
                                {
                                    return;
                                }
                                CategoryModel categoryModel = (CategoryModel) bundle.get("category_object");
                                Retrofit retrofit = HTTPService.getInstance();
                                HTTPRequest httpRequest = retrofit.create(HTTPRequest.class);
                                Call<ApiResponse<Object>> call = httpRequest.deleteCategory(categoryModel.getId());
                                call.enqueue(new Callback<ApiResponse<Object>>() {
                                    @Override
                                    public void onResponse(Call<ApiResponse<Object>> call, Response<ApiResponse<Object>> response) {
                                        if(response.isSuccessful())
                                        {
                                            ApiResponse apiResponse = response.body();
                                            if(apiResponse.getStatus() == 101)
                                            {
                                                Toast.makeText(UpdateCategoryActivity.this, apiResponse.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                            else {
                                                Toast.makeText(UpdateCategoryActivity.this, "Xóa danh mục thành công!", Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(UpdateCategoryActivity.this, CategoryIntroductionActivity.class);
                                                startActivity(intent);
                                            }
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<ApiResponse<Object>> call, Throwable t) {
                                        Toast.makeText(UpdateCategoryActivity.this, "Lỗi kết nối!", Toast.LENGTH_SHORT).show();
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

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UpdateCategoryActivity.this, CategoryIntroductionActivity.class);
                startActivity(intent);
            }
        });
    }
    private void setData()
    {
        Bundle bundle = getIntent().getExtras();
        if(bundle == null)
        {
            return;
        }
        CategoryModel categoryModel = (CategoryModel) bundle.get("category_object");
        cat_name.setText(categoryModel.getName());
        cat_des.setText(categoryModel.getDescription());
        List<String> listCategoryType = new ArrayList<>();
        if(categoryModel.getType() == 1)
        {
            listCategoryType.add("Thu nhập");
            listCategoryType.add("Chi tiêu");
            ArrayAdapter<String> categoryTypeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, listCategoryType);
            categoryTypeAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
            cat_type.setAdapter(categoryTypeAdapter);
        }
        else {
            listCategoryType.add("Chi tiêu");
            listCategoryType.add("Thu nhập");
            ArrayAdapter<String> categoryTypeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, listCategoryType);
            categoryTypeAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
            cat_type.setAdapter(categoryTypeAdapter);
        }
        cat_color.getBackground().setColorFilter(Color.parseColor(categoryModel.getColor()), PorterDuff.Mode.SRC);
        catColor_update_text = categoryModel.getColor();
    }
}