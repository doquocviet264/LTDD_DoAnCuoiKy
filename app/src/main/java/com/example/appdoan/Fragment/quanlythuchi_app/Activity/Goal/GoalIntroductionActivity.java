package com.example.appdoan.Fragment.quanlythuchi_app.Activity.Goal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlythuchi_app.API.HTTPRequest;
import com.example.quanlythuchi_app.API.HTTPService;
import com.example.quanlythuchi_app.Adapter.GoalAdapter;
import com.example.quanlythuchi_app.Container.Response.ApiResponse;
import com.example.quanlythuchi_app.HomeActivity;
import com.example.quanlythuchi_app.Model.GoalModel;
import com.example.quanlythuchi_app.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class GoalIntroductionActivity extends AppCompatActivity {
    ImageButton btnBack,btnAdd;
    SearchView svGoal;
    RecyclerView rvGoal;
    String test;
    List<GoalModel> listGoal,listGoal1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goal_intruction);
        setControl();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvGoal.setLayoutManager(linearLayoutManager);

        DividerItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        rvGoal.addItemDecoration(itemDecoration);

        listGoal = new ArrayList<>();
        listGoal1=new ArrayList<>();
        getGoal();
        setEvent();
    }

    private void getGoal() {
        Retrofit retrofit = HTTPService.getInstance();
        HTTPRequest httpRequest = retrofit.create(HTTPRequest.class);

        Call<ApiResponse<Object>> call = httpRequest.getAllGoal();
        call.enqueue(new Callback<ApiResponse<Object>>() {
            @Override
            public void onResponse(Call<ApiResponse<Object>> call, Response<ApiResponse<Object>> response) {
                ApiResponse<Object> apiResponse = response.body();
                Gson gson = new Gson();
                String jsonData = gson.toJson(apiResponse.getData());

                List<GoalModel> modalList = gson.fromJson(jsonData, new TypeToken<List<GoalModel>>(){}.getType());
                GoalAdapter goalAdapter = new GoalAdapter(GoalIntroductionActivity.this, modalList);
                rvGoal.setAdapter(goalAdapter);
                List<GoalModel> filteredItems = new ArrayList<>();

                svGoal.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        filteredItems.clear();
                        for(GoalModel goalModel : modalList){
                            if(goalModel.getName().toLowerCase().contains(newText.toLowerCase())){
                                filteredItems.add(goalModel);
                            }
                        }
                        GoalAdapter goalAdapter1 = new GoalAdapter(GoalIntroductionActivity.this,filteredItems);
                        rvGoal.setAdapter(goalAdapter1);
                        return true;
                    }
                });
            }

            @Override
            public void onFailure(Call<ApiResponse<Object>> call, Throwable t) {

                Toast.makeText(GoalIntroductionActivity.this, "Lỗi kết nối", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setControl() {
        btnBack=findViewById(R.id.btnBackGoal);
        btnAdd=findViewById(R.id.btnAdd);
        svGoal=findViewById(R.id.svGoal);
        rvGoal=findViewById(R.id.rvGoal);
    }
    private void setEvent() {
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GoalIntroductionActivity.this, GoalCreateActivity.class);
                startActivity(intent);
            }
        });


        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GoalIntroductionActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });

    }

}