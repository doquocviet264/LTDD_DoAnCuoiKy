package com.example.appdoan.Fragment.quanlythuchi_app.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlythuchi_app.Activity.Category.UpdateCategoryActivity;
import com.example.quanlythuchi_app.Helper.Format;
import com.example.quanlythuchi_app.Model.CategoryModel;
import com.example.quanlythuchi_app.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    Context context;
    List<CategoryModel> mListCategory;

    List<CategoryModel> filterList;

    public CategoryAdapter(Context context, List<CategoryModel> mListCategory) {
        this.context = context;
        this.mListCategory = mListCategory;
        this.filterList = new ArrayList<>(mListCategory);
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_category, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        CategoryModel categoryModel = mListCategory.get(position);
        holder.categoryColor.getBackground().setColorFilter(Color.parseColor(categoryModel.getColor()), PorterDuff.Mode.SRC);
        holder.categoryName.setText(Format.truncate_string(categoryModel.getName(), 70, "...", true));
        holder.categoryDescription.setText(Format.truncate_string(categoryModel.getDescription(), 70, "...", true));
        holder.categoryLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickGoToUpdate(categoryModel);
            }
        });
    }

    private void onClickGoToUpdate(CategoryModel categoryModel)
    {
        Intent intent = new Intent(context, UpdateCategoryActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("category_object", categoryModel);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        if(mListCategory != null)
        {
            return mListCategory.size();
        }
        return 0;
    }

    public static class CategoryViewHolder extends RecyclerView.ViewHolder{
        LinearLayout categoryLayout;
        ImageButton categoryColor;
        TextView categoryName, categoryDescription;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryLayout = itemView.findViewById(R.id.cat_layout);
            categoryColor = itemView.findViewById(R.id.cat_color);
            categoryName = itemView.findViewById(R.id.cat_name);
            categoryDescription = itemView.findViewById(R.id.cat_desc);

        }
    }

    public void filter(String query)
    {
        mListCategory.clear();
        if(query.isEmpty())
        {
            mListCategory.addAll(filterList);
        }
        else {
            query = query.toLowerCase(Locale.getDefault());
            for(CategoryModel item : filterList)
            {
                if(item.getName().toLowerCase(Locale.getDefault()).contains(query))
                {
                    mListCategory.add(item);
                }
            }
        }
        notifyDataSetChanged();
    }
}
