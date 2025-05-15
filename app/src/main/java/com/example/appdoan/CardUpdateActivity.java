package com.example.appdoan;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.appdoan.API.HTTPRequest;
import com.example.appdoan.API.HTTPService;
import com.example.appdoan.Container.Request.CardRequest;
import com.example.appdoan.Container.Response.ApiResponse;
import com.example.appdoan.Model.CardModel;
import com.example.appdoan.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CardUpdateActivity extends AppCompatActivity {

    ImageButton btnBack, btnCardDelete;

    EditText edtCardNumberUpdate, edtCardBalanceUpdate, edtCardNameUpdate, edtCardDescriptionUpdate;

    Button btnCardUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_update);

        btnBack = findViewById(R.id.btnBack);
        edtCardNumberUpdate = findViewById(R.id.cardNumberUpdate);
        edtCardBalanceUpdate = findViewById(R.id.cardBalanceUpdate);
        edtCardNameUpdate = findViewById(R.id.cardNameUpdate);
        edtCardDescriptionUpdate = findViewById(R.id.cardDescriptionUpdate);
        btnCardUpdate = findViewById(R.id.btnCardUpdate);
        btnCardDelete = findViewById(R.id.btnDeleteCard);

        Bundle bundle = getIntent().getExtras();
        if(bundle == null)
        {
            return;
        }
        CardModel cardModel = (CardModel) bundle.get("card_object");
        edtCardNumberUpdate.setText(cardModel.getCardnumber());
        edtCardNameUpdate.setText(cardModel.getName());
        String cardBalanceValue = String.valueOf(cardModel.getBalance());
        edtCardBalanceUpdate.setInputType(InputType.TYPE_CLASS_NUMBER);
        edtCardBalanceUpdate.setText(cardBalanceValue);
        edtCardDescriptionUpdate.setText(cardModel.getDescription());

        btnCardUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Retrofit retrofit = HTTPService.getInstance();
                HTTPRequest httpRequest = retrofit.create(HTTPRequest.class);

                String cardNumberUpdate = edtCardNumberUpdate.getText().toString();
                String cardNameUpdate = edtCardNameUpdate.getText().toString();
                String cardBalanceTextUpdate = edtCardBalanceUpdate.getText().toString();
                String cardDescriptionUpdate = edtCardDescriptionUpdate.getText().toString();
                if(cardNumberUpdate.isEmpty() || cardNameUpdate.isEmpty() || cardBalanceTextUpdate.isEmpty() || cardDescriptionUpdate.isEmpty())
                {
                    Toast.makeText(CardUpdateActivity.this, "Vui lòng điền đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                }
                else {
                    Long cardBalanceUpdate = Long.valueOf(cardBalanceTextUpdate);
                    CardRequest cardRequest = new CardRequest(cardNameUpdate, cardBalanceUpdate, cardNumberUpdate, cardDescriptionUpdate);
                    Call<ApiResponse<Object>> call = httpRequest.updateCard(cardRequest, cardModel.getId());
                    call.enqueue(new Callback<ApiResponse<Object>>() {
                        @Override
                        public void onResponse(Call<ApiResponse<Object>> call, Response<ApiResponse<Object>> response) {
                            if(response.isSuccessful())
                            {
                                ApiResponse<Object> apiResponse = response.body();
                                if(apiResponse.getStatus() == 200)
                                {
                                    Toast.makeText(CardUpdateActivity.this, "Chỉnh sửa thẻ ngân hàng thành công!", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                                else {
                                    Toast.makeText(CardUpdateActivity.this, "Chỉnh sửa thất bại! Vui lòng thử lại!", Toast.LENGTH_SHORT).show();
                                }
                            }
                            else {
                                Toast.makeText(CardUpdateActivity.this, "Thất bại! Vui lòng thử lại!", Toast.LENGTH_SHORT).show();
                            }
                        }
                        @Override
                        public void onFailure(Call<ApiResponse<Object>> call, Throwable t) {
                            Toast.makeText(CardUpdateActivity.this, "Lỗi kết nối!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        btnCardDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(CardUpdateActivity.this);
                builder.setMessage("Bạn có thực sự muốn xóa thẻ?")
                        .setCancelable(false)
                        .setPositiveButton("Có", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Retrofit retrofit = HTTPService.getInstance();
                                HTTPRequest httpRequest = retrofit.create(HTTPRequest.class);
                                SharedPreferences sharedPreferences = getSharedPreferences("MyApp", Context.MODE_PRIVATE);
                                String token = sharedPreferences.getString("token", "");
                                Call<ApiResponse<Object>> call = httpRequest.deleteCard(cardModel.getId());
                                call.enqueue(new Callback<ApiResponse<Object>>() {
                                    @Override
                                    public void onResponse(Call<ApiResponse<Object>> call, Response<ApiResponse<Object>> response) {
                                        if(response.isSuccessful())
                                        {
                                            ApiResponse<Object> apiResponse = response.body();
                                            if(apiResponse.getStatus() == 101)
                                            {
                                                Toast.makeText(CardUpdateActivity.this, "Không thể xóa thẻ do có giao dịch liên quan!", Toast.LENGTH_SHORT).show();
                                            }
                                            else {
                                                Toast.makeText(CardUpdateActivity.this, "Xóa thẻ ngân hàng thành công!", Toast.LENGTH_SHORT).show();
                                                finish();
                                            }
                                        }
                                        else {
                                            Toast.makeText(CardUpdateActivity.this, "Thất bại! Vui lòng kiểm tra lại!", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                    @Override
                                    public void onFailure(Call<ApiResponse<Object>> call, Throwable t) {
                                        Toast.makeText(CardUpdateActivity.this, "Lỗi kết nối!", Toast.LENGTH_SHORT).show();
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
                onBackPressed();
            }
        });
    }
}