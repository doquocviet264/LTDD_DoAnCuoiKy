package com.example.appdoan.Fragment.quanlythuchi_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlythuchi_app.API.HTTPRequest;
import com.example.quanlythuchi_app.API.HTTPService;
import com.example.quanlythuchi_app.Activity.Card.CardCreateActivity;
import com.example.quanlythuchi_app.Adapter.CardAdapter;
import com.example.quanlythuchi_app.Helper.LoadingDialog;
import com.example.quanlythuchi_app.Model.CardModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CardIntroductionActivity extends AppCompatActivity {

    LoadingDialog loadingDialog = new LoadingDialog(CardIntroductionActivity.this);
    ImageButton btnCreateCard;
    private RecyclerView rcvCardList;
    private List<CardModel> mListCard;
    BottomNavigationView navbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_introduction);

        navbar = findViewById(R.id.navbar);
        navbar.setSelectedItemId(R.id.card);
        rcvCardList = findViewById(R.id.cardRecycleView);
        btnCreateCard = findViewById(R.id.btnCardCreate);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rcvCardList.setLayoutManager(linearLayoutManager);

        DividerItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        rcvCardList.addItemDecoration(itemDecoration);

        mListCard = new ArrayList<>();
        getAllCard();

        navbar.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavi6ygationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.card:
                        return true;
                    case R.id.report:
                        loadingDialog.startLoadingDialog();
                        loadingDialog.setDialogDuration(2000);
                        startActivity(new Intent(getApplicationContext(), StatisticalChartActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.home:
                        loadingDialog.startLoadingDialog();
                        loadingDialog.setDialogDuration(2000);
                        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.setting:
                        loadingDialog.startLoadingDialog();
                        loadingDialog.setDialogDuration(2000);
                        startActivity(new Intent(getApplicationContext(), SettingActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                }
                return false;
            }
        });

        btnCreateCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CardIntroductionActivity.this, CardCreateActivity.class);
                startActivity(intent);
            }
        });
    }

    private void getAllCard()
    {
        Retrofit retrofit = HTTPService.getInstance();
        HTTPRequest httpRequest = retrofit.create(HTTPRequest.class);
        Call<List<CardModel>> call = httpRequest.getAllCard();
        call.enqueue(new Callback<List<CardModel>>() {
            @Override
            public void onResponse(Call<List<CardModel>> call, Response<List<CardModel>> response) {
                mListCard = response.body();
                CardAdapter cardAdapter = new CardAdapter(CardIntroductionActivity.this, mListCard);
                rcvCardList.setAdapter(cardAdapter);
            }
            @Override
            public void onFailure(Call<List<CardModel>> call, Throwable t) {
                Toast.makeText(CardIntroductionActivity.this, "Lỗi kết nối", Toast.LENGTH_SHORT).show();
            }
        });
    }
}