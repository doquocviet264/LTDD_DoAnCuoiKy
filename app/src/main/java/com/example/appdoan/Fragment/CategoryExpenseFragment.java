package com.example.appdoan.Fragment;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.appdoan.API.HTTPRequest;
import com.example.appdoan.API.HTTPService;
import com.example.appdoan.Adapter.CategoryAdapter;
import com.example.appdoan.Model.CategoryModel;
import com.example.appdoan.R;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CategoryExpenseFragment extends Fragment {

    private String currentQuery = "";
    private CategoryAdapter adapter;
    private RecyclerView lvCategory;
    private List<CategoryModel> mListCategory;
    private SwipeRefreshLayout swipeRefreshLayout;
    private HTTPRequest httpRequest;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category_expense, container, false);

        lvCategory = view.findViewById(R.id.lvCategory);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        mListCategory = new ArrayList<>();
        adapter = new CategoryAdapter(getContext(), mListCategory);

        lvCategory.setLayoutManager(new LinearLayoutManager(getContext()));
        lvCategory.setHasFixedSize(true);
        lvCategory.setAdapter(adapter);

        Retrofit retrofit = HTTPService.getInstance();
        httpRequest = retrofit.create(HTTPRequest.class);

        getExpenseCategory();

        swipeRefreshLayout.setOnRefreshListener(this::refreshData);

        return view;
    }

    private void refreshData() {
        filterCategoryExpense(currentQuery);
        swipeRefreshLayout.setRefreshing(false);
    }

    private void getExpenseCategory() {
        Retrofit retrofit = HTTPService.getInstance();
        HTTPRequest httpRequest = retrofit.create(HTTPRequest.class);
        Call<List<CategoryModel>> call = httpRequest.expenseCategory();
        call.enqueue(new Callback<List<CategoryModel>>() {
            @Override
            public void onResponse(Call<List<CategoryModel>> call, Response<List<CategoryModel>> response) {
                if(response.isSuccessful())
                {
                    mListCategory = response.body();
                    lvCategory.setLayoutManager(new LinearLayoutManager(getContext()));
                    lvCategory.setHasFixedSize(true);
                    adapter = new CategoryAdapter(getContext(), mListCategory);
                    lvCategory.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onFailure(Call<List<CategoryModel>> call, Throwable t) {
                Toast.makeText(requireContext(), "Lỗi kết nối!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void filterCategoryExpense(String query) {
        currentQuery = query;
        adapter.filter(currentQuery);
    }
}

