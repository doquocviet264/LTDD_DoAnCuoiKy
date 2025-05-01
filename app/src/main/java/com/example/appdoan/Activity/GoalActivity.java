package com.example.appdoan.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.appdoan.API.HTTPRequest;
import com.example.appdoan.API.HTTPService;
import com.example.appdoan.Adapter.GoalAdapter;
import com.example.appdoan.Container.Response.ApiResponse;
import com.example.appdoan.GoalCreateActivity;
import com.example.appdoan.Model.GoalModel;
import com.example.appdoan.R;
import com.example.appdoan.databinding.ActivityGoalBinding;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class GoalActivity extends AppCompatActivity {

    private ActivityGoalBinding binding;
    private GoalAdapter goalAdapter;
    private List<GoalModel> goalList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGoalBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupRecyclerView();
        fetchGoals();
        setupEvents();
    }

    private void setupRecyclerView() {
        binding.rvGoal.setLayoutManager(new LinearLayoutManager(this));
        binding.rvGoal.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        goalAdapter = new GoalAdapter(this, goalList);
        binding.rvGoal.setAdapter(goalAdapter);
    }

    private void fetchGoals() {
        Retrofit retrofit = HTTPService.getInstance();
        HTTPRequest httpRequest = retrofit.create(HTTPRequest.class);

        httpRequest.getAllGoal().enqueue(new Callback<ApiResponse<Object>>() {
            @Override
            public void onResponse(Call<ApiResponse<Object>> call, Response<ApiResponse<Object>> response) {
                ApiResponse<Object> apiResponse = response.body();
                if (apiResponse != null && apiResponse.getData() != null) {
                    Gson gson = new Gson();
                    String jsonData = gson.toJson(apiResponse.getData());

                    List<GoalModel> modalList = gson.fromJson(jsonData, new TypeToken<List<GoalModel>>(){}.getType());
                    GoalAdapter goalAdapter = new GoalAdapter(GoalActivity.this, modalList);
                    binding.rvGoal.setAdapter(goalAdapter);
                    List<GoalModel> filteredItems = new ArrayList<>();

                    binding.svGoal.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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
                            GoalAdapter goalAdapter1 = new GoalAdapter(GoalActivity.this,filteredItems);
                            binding.rvGoal.setAdapter(goalAdapter1);
                            return true;
                        }
                    });
                } else {
                    Toast.makeText(GoalActivity.this, "Không có dữ liệu mục tiêu", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Object>> call, Throwable t) {
                Toast.makeText(GoalActivity.this, "Lỗi kết nối", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void setupEvents() {
        binding.btnAdd.setOnClickListener(v -> startActivity(new Intent(this, GoalCreateActivity.class)));

        binding.btnBackGoal.setOnClickListener(view -> onBackPressed());
        EditText searchEditText = binding.svGoal.findViewById(androidx.appcompat.R.id.search_src_text);
        searchEditText.setTextColor(Color.WHITE); // Màu chữ nhập vào
        searchEditText.setHintTextColor(Color.LTGRAY); // Màu gợi ý "Search here..."

        // (Tuỳ chọn) Đổi màu icon search và close cho đồng bộ nếu nền tối
        ImageView searchIcon = binding.svGoal.findViewById(androidx.appcompat.R.id.search_mag_icon);
        ImageView closeIcon = binding.svGoal.findViewById(androidx.appcompat.R.id.search_close_btn);
        searchIcon.setColorFilter(Color.WHITE);
        closeIcon.setColorFilter(Color.WHITE);
    }
}