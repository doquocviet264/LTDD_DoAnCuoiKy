package com.example.appdoan.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.appdoan.CategoryUpdateActivity;
import com.example.appdoan.Utils.Format;
import com.example.appdoan.Model.CategoryModel;
import com.example.appdoan.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    private final Context context;
    private List<CategoryModel> mListCategory;
    private final List<CategoryModel> filterList;
    private final View.OnClickListener categoryClickListener;

    public CategoryAdapter(Context context, List<CategoryModel> mListCategory) {
        this.context = context;
        this.mListCategory = mListCategory;
        this.filterList = new ArrayList<>(mListCategory);

        // Tái sử dụng OnClickListener thay vì tạo mới mỗi lần
        categoryClickListener = view -> {
            CategoryModel categoryModel = (CategoryModel) view.getTag();
            onClickGoToUpdate(categoryModel);
        };
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Tạo view cho item
        View view = LayoutInflater.from(context).inflate(R.layout.item_category, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        CategoryModel categoryModel = mListCategory.get(position);

        // Set màu sắc cho danh mục
        holder.categoryColor.getBackground().setColorFilter(Color.parseColor(categoryModel.getColor()), PorterDuff.Mode.SRC);

        // Cắt ngắn tên và mô tả
        String truncatedName = Format.truncate_string(categoryModel.getName(), 70, "...", true);
        String truncatedDesc = Format.truncate_string(categoryModel.getDescription(), 70, "...", true);

        // Set tên và mô tả cho category
        holder.categoryName.setText(truncatedName);
        holder.categoryDescription.setText(truncatedDesc);

        // Đặt listener cho việc nhấn vào category
        holder.categoryLayout.setTag(categoryModel);
        holder.categoryLayout.setOnClickListener(categoryClickListener);
    }

    private void onClickGoToUpdate(CategoryModel categoryModel) {
        // Chuyển đến Activity cập nhật danh mục
        Intent intent = new Intent(context, CategoryUpdateActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("category_object", categoryModel);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        // Trả về số lượng item trong danh sách
        return mListCategory != null ? mListCategory.size() : 0;
    }

    public static class CategoryViewHolder extends RecyclerView.ViewHolder {
        LinearLayout categoryLayout;
        ImageButton categoryColor;
        TextView categoryName, categoryDescription;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            // Khởi tạo các view của item
            categoryLayout = itemView.findViewById(R.id.cat_layout);
            categoryColor = itemView.findViewById(R.id.cat_color);
            categoryName = itemView.findViewById(R.id.cat_name);
            categoryDescription = itemView.findViewById(R.id.cat_desc);
        }
    }

    public void filter(String query) {
        // Lọc danh mục theo từ khóa tìm kiếm
        mListCategory.clear();
        if (TextUtils.isEmpty(query)) {
            // Nếu không có từ khóa, hiển thị toàn bộ danh mục
            mListCategory.addAll(filterList);
        } else {
            // Lọc danh mục theo tên
            query = query.toLowerCase(Locale.getDefault());
            for (CategoryModel item : filterList) {
                if (item.getName().toLowerCase(Locale.getDefault()).contains(query)) {
                    mListCategory.add(item);
                }
            }
        }
        // Cập nhật lại RecyclerView
        notifyDataSetChanged();
    }
}
