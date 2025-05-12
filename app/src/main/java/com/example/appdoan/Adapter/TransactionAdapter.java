package com.example.appdoan.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appdoan.Model.TransactionModel;
import com.example.appdoan.R;
import com.example.appdoan.TransactionInformationActivity;
import com.example.appdoan.Utils.Format;
import com.example.appdoan.databinding.ItemTransactionBinding;

import java.util.List;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder> {

    private final List<TransactionModel> mListTransaction;
    private final Context context;

    public TransactionAdapter(List<TransactionModel> mListTransaction, Context context) {
        this.mListTransaction = mListTransaction;
        this.context = context;
    }

    @NonNull
    @Override
    public TransactionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemTransactionBinding binding = ItemTransactionBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new TransactionViewHolder(binding, context); // truyền context vào ViewHolder
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionViewHolder holder, int position) {
        TransactionModel transactionModel = mListTransaction.get(position);
        if (transactionModel == null) {
            return;
        }
        holder.bind(transactionModel);
    }

    @Override
    public int getItemCount() {
        return mListTransaction != null ? mListTransaction.size() : 0;
    }

    public static class TransactionViewHolder extends RecyclerView.ViewHolder {

        private final ItemTransactionBinding binding;
        private final Context context; // thêm biến context

        public TransactionViewHolder(@NonNull ItemTransactionBinding binding, Context context) {
            super(binding.getRoot());
            this.binding = binding;
            this.context = context;
        }

        public void bind(TransactionModel transactionModel) {
            binding.transactionName.setText(transactionModel.getName());
            binding.transactionCategory.setText(transactionModel.getCategoryModel() != null ? transactionModel.getCategoryModel().getName() : "");
            binding.transactionAmount.setText(Format.formatNumber(transactionModel.getAmount()) + " VND");
            binding.transactionDate.setText(Format.formatDate(transactionModel.getTransactiondate()));
            binding.transactionLayout.setOnClickListener(v -> {
                Intent intent = new Intent(context, TransactionInformationActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("transaction_object", transactionModel);
                intent.putExtras(bundle);
                context.startActivity(intent);
            });
        }
    }
}
