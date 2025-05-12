package com.example.appdoan.Fragment.quanlythuchi_app.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlythuchi_app.Activity.Transaction.TransactionInformationActivity;
import com.example.quanlythuchi_app.Helper.Format;
import com.example.quanlythuchi_app.Model.TransactionModel;
import com.example.quanlythuchi_app.R;

import java.util.List;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder> {

    private final List<TransactionModel> mListTransaction;

    private Context context;

    public TransactionAdapter(List<TransactionModel> mListTransaction, Context context) {
        this.mListTransaction = mListTransaction;
        this.context = context;
    }
    @NonNull
    @Override
    public TransactionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_transaction, parent, false);
        return new TransactionViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull TransactionViewHolder holder, int position) {
        final TransactionModel transactionModel = mListTransaction.get(position);
        if(transactionModel == null)
        {
            return;
        }
        holder.tvTransactionName.setText(transactionModel.getName());
        holder.tvTransactionCategory.setText(transactionModel.getCategoryModel().getName());
        holder.tvTransactionAmount.setText(Format.formatNumber(transactionModel.getAmount()) + " VND");
        holder.tvTransactionDate.setText(Format.formatDate(transactionModel.getTransactiondate()));

        holder.layoutTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickGoToInformation(transactionModel);
            }
        });
    }

    private void onClickGoToInformation(TransactionModel transactionModel) {
        Intent intent = new Intent(context, TransactionInformationActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("transaction_object", transactionModel);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        if(mListTransaction != null)
        {
            return mListTransaction.size();
        }
        return 0;
    }

    public static class TransactionViewHolder extends RecyclerView.ViewHolder{

        private final RelativeLayout layoutTransaction;

        private final TextView tvTransactionName, tvTransactionCategory, tvTransactionAmount, tvTransactionDate;

        public TransactionViewHolder(@NonNull View itemView) {
            super(itemView);
            layoutTransaction = itemView.findViewById(R.id.transactionLayout);
            tvTransactionName = itemView.findViewById(R.id.transactionName);
            tvTransactionCategory = itemView.findViewById(R.id.transactionCategory);
            tvTransactionAmount = itemView.findViewById(R.id.transactionAmount);
            tvTransactionDate = itemView.findViewById(R.id.transactionDate);
        }
    }
}
