package com.example.appdoan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.appdoan.API.HTTPRequest;
import com.example.appdoan.API.HTTPService;
import com.example.appdoan.Container.Request.CardRequest;
import com.example.appdoan.Container.Response.ApiResponse;
import com.example.appdoan.R;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CardCreateActivity extends AppCompatActivity {

    ImageButton btnBack;
    EditText edtCardNumber, edtCardBalance, edtCardName, edtCardDescription;
    Button btnCreateCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_create);

        btnBack = findViewById(R.id.btnBack);
        edtCardNumber = findViewById(R.id.cardNumber);
        edtCardBalance = findViewById(R.id.cardBalance);
        edtCardName = findViewById(R.id.cardName);
        edtCardDescription = findViewById(R.id.cardDescription);
        btnCreateCard = findViewById(R.id.cardCreate);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        btnCreateCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Retrofit retrofit = HTTPService.getInstance();
                HTTPRequest httpRequest = retrofit.create(HTTPRequest.class);
                String cardNumber = edtCardNumber.getText().toString();
                String cardName = edtCardName.getText().toString();
                String cardBalanceText = edtCardBalance.getText().toString();
                String cardDescription = edtCardDescription.getText().toString();
                if(cardNumber.isEmpty() || cardName.isEmpty() || cardBalanceText.isEmpty() || cardDescription.isEmpty())
                {
                    Toast.makeText(CardCreateActivity.this, "Vui lòng điền đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                }
                else {
                    Long cardBalance = Long.valueOf(cardBalanceText);
                    CardRequest cardRequest = new CardRequest(cardName, cardBalance, cardNumber, cardDescription);
                    Call<ApiResponse<Object>> call = httpRequest.addCard(cardRequest);
                    call.enqueue(new Callback<ApiResponse<Object>>() {
                        @Override
                        public void onResponse(Call<ApiResponse<Object>> call, Response<ApiResponse<Object>> response) {
                            if(response.isSuccessful())
                            {
                                ApiResponse<Object> apiResponse = response.body();
                                if(apiResponse.getStatus() == 200)
                                {
                                    Toast.makeText(CardCreateActivity.this, "Thêm thẻ ngân hàng thành công!", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                                else {
                                    Toast.makeText(CardCreateActivity.this, "Thêm thẻ ngân hàng không thành công. Vui lòng kiểm tra lại!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                        @Override
                        public void onFailure(Call<ApiResponse<Object>> call, Throwable t) {
                            Toast.makeText(CardCreateActivity.this, "Lỗi kết nối!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });


    }
}