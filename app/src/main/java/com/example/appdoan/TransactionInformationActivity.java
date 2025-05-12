package com.example.appdoan;

import android.os.Bundle;


import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import com.example.appdoan.Model.TransactionModel;
import com.example.appdoan.Utils.Format;
import com.example.appdoan.databinding.ActivityTransactionInformationBinding;

public class TransactionInformationActivity extends AppCompatActivity {

    private ActivityTransactionInformationBinding binding;
    private TransactionModel transactionModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTransactionInformationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setData();
        setEvent();
    }

    private void setEvent() {
        binding.btnBack.setOnClickListener(v -> finish());
        binding.updateTransaction.setOnClickListener(v -> onClickGoToUpdate());
    }

    private void onClickGoToUpdate() {
        if (transactionModel == null) {
            return;
        }
        Intent intent = new Intent(this, TransactionUpdateActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("transaction_object_update", transactionModel);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void setData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle == null) {
            return;
        }
        transactionModel = (TransactionModel) bundle.getSerializable("transaction_object");
        if (transactionModel == null) {
            return;
        }
        binding.transactionInforName.setText(transactionModel.getName());
        binding.transactionInforDate.setText(Format.formatDate(transactionModel.getTransactiondate()));
        binding.transactionInforAmount.setText(Format.formatNumber(transactionModel.getAmount()) + " VND");
        binding.transactionInforCard.setText(transactionModel.getCardModel() != null ? transactionModel.getCardModel().getName() : "");
        binding.transactionInforCategory.setText(transactionModel.getCategoryModel() != null ? transactionModel.getCategoryModel().getName() : "");
        binding.transactionInforLocation.setText(transactionModel.getLocation());
        binding.transactionInforDescription.setText(transactionModel.getDescription());
    }
}