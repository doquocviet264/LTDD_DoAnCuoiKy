package com.example.appdoan.Fragment.quanlythuchi_app.Activity.Transaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quanlythuchi_app.Helper.Format;
import com.example.quanlythuchi_app.Model.TransactionModel;
import com.example.quanlythuchi_app.R;

public class TransactionInformationActivity extends AppCompatActivity {
    ImageButton btnBack;
    Button btnEditTran;
    TextView tranName, tranDate, tranAmount, tranCard, tranCategory, tranLocation, tranDes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_information);

        btnBack = findViewById(R.id.btnBack);
        btnEditTran = findViewById(R.id.updateTransaction);
        tranName = findViewById(R.id.transactionInforName);
        tranDate = findViewById(R.id.transactionInforDate);
        tranAmount = findViewById(R.id.transactionInforAmount);
        tranCard = findViewById(R.id.transactionInforCard);
        tranCategory = findViewById(R.id.transactionInforCategory);
        tranLocation = findViewById(R.id.transactionInforLocation);
        tranDes = findViewById(R.id.transactionInforDescription);

        setData();
        setEvent();
    }

    private void setEvent() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnEditTran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickGoToUpdate();
            }
        });
    }

    private void onClickGoToUpdate()
    {
        Bundle bundle = getIntent().getExtras();
        if(bundle == null)
        {
            return;
        }
        TransactionModel transactionModel = (TransactionModel) bundle.get("transaction_object");
        Intent intent = new Intent(TransactionInformationActivity.this, TransactionUpdateActivity.class);
        Bundle bundle1 = new Bundle();
        bundle1.putSerializable("transaction_object_update", transactionModel);
        intent.putExtras(bundle1);
        startActivity(intent);
    }
    private void setData() {
        Bundle bundle = getIntent().getExtras();
        if(bundle == null)
        {
            return;
        }
        TransactionModel transactionModel = (TransactionModel) bundle.get("transaction_object");
        tranName.setText(transactionModel.getName());
        tranDate.setText(Format.formatDate(transactionModel.getTransactiondate()));
        tranAmount.setText(Format.formatNumber(transactionModel.getAmount()) + " VND");
        tranCard.setText(transactionModel.getCardModel().getName());
        tranCategory.setText(transactionModel.getCategoryModel().getName());
        tranLocation.setText(transactionModel.getLocation());
        tranDes.setText(transactionModel.getDescription());
    }
}