package com.example.appdoan.Fragment.quanlythuchi_app.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlythuchi_app.API.HTTPRequest;
import com.example.quanlythuchi_app.API.HTTPService;
import com.example.quanlythuchi_app.Activity.Budget.BudgetDetailActivity;
import com.example.quanlythuchi_app.Activity.Budget.BudgetUpdateActivity;
import com.example.quanlythuchi_app.Container.Response.ApiResponse;
import com.example.quanlythuchi_app.Helper.Format;
import com.example.quanlythuchi_app.Model.BudgetModel;
import com.example.quanlythuchi_app.Model.CategoryModel;
import com.example.quanlythuchi_app.R;
import com.example.quanlythuchi_app.Utils.FormatDate;

import java.text.ParseException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class BudgetAdapter extends RecyclerView.Adapter<BudgetAdapter.BudgetViewHolder> {
    private List<BudgetModel> listBudget;
    private Context context;

    public BudgetAdapter(Context context,List<BudgetModel> listBudget) {
        this.listBudget = listBudget;
        this.context = context;
    }

    @NonNull
    @Override
    public BudgetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_budget, parent, false);
        return new BudgetViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BudgetViewHolder holder, int position) {
        final BudgetModel budgetModel = listBudget.get(position);
        final CategoryModel categoryModel = listBudget.get(position).getCategoryModel();
        holder.tvBudgetCategory.setText(budgetModel.getCategoryModel().getName());
        String test = budgetModel.getFromdate();
        String from = null;
        String to = null;
        try {
            from = FormatDate.ChangeFormatToDMY(budgetModel.getFromdate());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        try {
            to = FormatDate.ChangeFormatToDMY(budgetModel.getTodate());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        holder.tvBudgetFrom.setText(from);
        holder.tvBudgetTo.setText(to);

        holder.tvBudgetDescription.setText("Mô tả: "+budgetModel.getDescription());
        holder.layoutItemBudget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickGo(budgetModel.getId(),budgetModel.getCategoryModel().getId());
            }
        });

        Retrofit retrofit = HTTPService.getInstance();
        HTTPRequest httpRequest = retrofit.create(HTTPRequest.class);
        Long idCategory = budgetModel.getCategoryModel().getId();
        Call<ApiResponse<Object>> call = httpRequest.totalByCategoryInMonth(idCategory);
        call.enqueue(new Callback<ApiResponse<Object>>() {
            @Override
            public void onResponse(Call<ApiResponse<Object>> call, Response<ApiResponse<Object>> response) {
                if(response.isSuccessful())
                {
                    ApiResponse<Object> apiResponse = response.body();
                    if(apiResponse.getStatus() == 200)
                    {
                        Double amount = Double.valueOf(budgetModel.getAmount());
                        Double totalInMonth = (Double) apiResponse.getData();
                        String totalInMonthInt = Format.formatNumber(totalInMonth.longValue());
                        int progress = (int) ((totalInMonth/amount)*100);
                        holder.pbBudgetProgress.setProgress(progress);
                        holder.tvTotalInMonth.setText("Chi :"+totalInMonthInt);
                        if(totalInMonth>amount){
                            holder.tvBudgetAmount.setTextColor(Color.RED);
                            holder.tvBudgetAmount.setText("Ngân sách: "+ Format.formatNumber(budgetModel.getAmount()) +" - Vượt ngân sách");
                        }else if(totalInMonth==amount){
                            holder.tvBudgetAmount.setTextColor(Color.GREEN);
                            holder.tvBudgetAmount.setText("Ngân sách: "+ Format.formatNumber(budgetModel.getAmount()) +" - Hoàn thành");
                        }else {
                            holder.tvBudgetAmount.setText("Ngân sách: "+ Format.formatNumber(budgetModel.getAmount()));
                        }

                    }else if(apiResponse.getStatus()==101){
                        Double amount = Double.valueOf(budgetModel.getAmount());
                        Double totalInMonth = 0D;
                        int progress = (int) (totalInMonth/amount)*100;
                        holder.tvTotalInMonth.setText("Chi: 0");
                        holder.pbBudgetProgress.setProgress(progress);
                        holder.tvBudgetAmount.setText("Ngân sách: "+ Format.formatNumber(budgetModel.getAmount()));
                    }
                    else {
                        Toast.makeText(context, "Lỗi truy vấn tiến trình", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Object>> call, Throwable t) {
                Toast.makeText(context, "Lỗi kết nối!", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void onClickGo(Long budgetId,Long categoryId){
        String[] option = {"Sửa","Chi tiết"};
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Chọn một tùy chọn").setItems(option, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String selected = option[which];
                switch (selected){
                    case "Sửa":
                        Intent intent = new Intent(context, BudgetUpdateActivity.class);
                        intent.putExtra("budgetId",budgetId);
                        context.startActivity(intent);
                        break;
                    case "Chi tiết":
                        Intent intent1 = new Intent(context, BudgetDetailActivity.class);
                        intent1.putExtra("budgetId",budgetId);
                        intent1.putExtra("categoryId",categoryId);
                        context.startActivity(intent1);
                        break;
                }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    private void handleSelect(String s){

    }
    @Override
    public int getItemCount() {
        return listBudget==null?0:listBudget.size();
    }
    public static class BudgetViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvTotalInMonth,tvBudgetCategory, tvBudgetAmount, tvBudgetFrom,tvBudgetTo,tvBudgetDescription;
        private final ProgressBar pbBudgetProgress;
        private final LinearLayout layoutItemBudget;
        public BudgetViewHolder(@NonNull View view) {
            super(view);
            tvTotalInMonth=view.findViewById(R.id.tvTotalInMonth);
            tvBudgetCategory =view.findViewById(R.id.tvBudgetCategory);
            tvBudgetAmount=view.findViewById(R.id.tvBudgetAmount);
            pbBudgetProgress=view.findViewById(R.id.pbBudgetProgress);
            tvBudgetFrom=view.findViewById(R.id.tvBudgetFrom);
            tvBudgetTo=view.findViewById(R.id.tvBudgetTo);
            tvBudgetDescription=view.findViewById(R.id.tvBudgetDescription);
            layoutItemBudget=view.findViewById(R.id.layoutItemBudget);
        }
    }
}
