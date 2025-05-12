package com.example.appdoan.Fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.appdoan.API.HTTPRequest;
import com.example.appdoan.API.HTTPService;
import com.example.appdoan.Container.Response.ApiResponse;
import com.example.appdoan.Model.CategoryModel;

import com.example.appdoan.R;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class PieChartCategoryFragment extends Fragment {
    private PieChart pieChartIncomeCategory, pieChartExpenseCategory;

    List<PieEntry> listIncomeCategoryEntry = new ArrayList<>();

    List<PieEntry> listExpenseCategoryEntry = new ArrayList<>();

    List<String> colorIncomeCategory = new ArrayList<>();
    List<String> nameIncomeCategory = new ArrayList<>();

    List<String> colorExpenseCategory = new ArrayList<>();
    List<String> nameExpenseCategory = new ArrayList<>();

    List<CategoryModel> mListCategoryIncome = new ArrayList<>();

    List<CategoryModel> mListCategoryExpense = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pie_chart_category, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        pieChartIncomeCategory = view.findViewById(R.id.pieChartCategoryIncome);
        pieChartExpenseCategory = view.findViewById(R.id.pieChartCategoryExpense);

        colorPieChartIncome();
        colorPieChartExpense();
    }

    private void setupPieChartExpense() {
        Retrofit retrofit = HTTPService.getInstance();
        HTTPRequest httpRequest = retrofit.create(HTTPRequest.class);
        Call<ApiResponse<Object>> call = httpRequest.listTotalByCategoryExpense();

        call.enqueue(new Callback<ApiResponse<Object>>() {
            @Override
            public void onResponse(Call<ApiResponse<Object>> call, Response<ApiResponse<Object>> response) {
                if(response.isSuccessful())
                {
                    ApiResponse apiResponse = response.body();
                    List<?> dataList = (List<?>) apiResponse.getData();
                    for (int i = 0; i < dataList.size(); i++) {
                        Object data = dataList.get(i);
                        if (data instanceof Double) {
                            Double doubleValue = (Double) data;
                            Long longValue = doubleValue.longValue();
                            listExpenseCategoryEntry.add(new PieEntry(longValue, nameExpenseCategory.get(i)));
                        }
                    }
                    int[] colors = new int[colorExpenseCategory.size()];
                    for (int i = 0; i < colorExpenseCategory.size(); i++) {
                        colors[i] = Color.parseColor(colorExpenseCategory.get(i)); // Chuyển đổi mã màu thành kiểu int
                    }
                    PieDataSet pieDataSet = new PieDataSet(listExpenseCategoryEntry, "");
                    pieDataSet.setColor(Color.BLUE);
                    pieDataSet.setColors(colors);
                    PieData pieData = new PieData(pieDataSet);

                    pieChartExpenseCategory.getDescription().setEnabled(false);
                    pieChartExpenseCategory.setDrawEntryLabels(false);
                    pieChartExpenseCategory.animateY(2000);
                    pieChartExpenseCategory.setData(pieData);
                    pieChartExpenseCategory.invalidate();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Object>> call, Throwable t) {

            }
        });
    }

    private void colorPieChartExpense()
    {
        Retrofit retrofit = HTTPService.getInstance();
        HTTPRequest httpRequest = retrofit.create(HTTPRequest.class);
        Call<List<CategoryModel>> call = httpRequest.expenseCategory();
        call.enqueue(new Callback<List<CategoryModel>>() {
            @Override
            public void onResponse(Call<List<CategoryModel>> call, Response<List<CategoryModel>> response) {
                if(response.isSuccessful())
                {
                    mListCategoryExpense = response.body();
                    for(CategoryModel item : mListCategoryExpense)
                    {
                        colorExpenseCategory.add(item.getColor());
                        nameExpenseCategory.add(item.getName());
                    }
                    setupPieChartExpense();
                }
            }
            @Override
            public void onFailure(Call<List<CategoryModel>> call, Throwable t) {
            }
        });
    }

    private void setupPieChartIncome() {
        Retrofit retrofit = HTTPService.getInstance();
        HTTPRequest httpRequest = retrofit.create(HTTPRequest.class);
        Call<ApiResponse<Object>> call = httpRequest.listTotalByCategoryIncome();
        call.enqueue(new Callback<ApiResponse<Object>>() {
            @Override
            public void onResponse(Call<ApiResponse<Object>> call, Response<ApiResponse<Object>> response) {
                if(response.isSuccessful())
                {
                    ApiResponse apiResponse = response.body();
                    List<?> dataList = (List<?>) apiResponse.getData();
                    for (int i = 0; i < dataList.size(); i++) {
                        Object data = dataList.get(i);
                        if (data instanceof Double) {
                            Double doubleValue = (Double) data;
                            Long longValue = doubleValue.longValue();
                            listIncomeCategoryEntry.add(new PieEntry(longValue, nameIncomeCategory.get(i)));
                        }
                    }
                    int[] colors = new int[colorIncomeCategory.size()];
                    for (int i = 0; i < colorIncomeCategory.size(); i++) {
                        colors[i] = Color.parseColor(colorIncomeCategory.get(i)); // Chuyển đổi mã màu thành kiểu int
                    }
                    PieDataSet pieDataSet = new PieDataSet(listIncomeCategoryEntry, "");
                    pieDataSet.setXValuePosition(PieDataSet.ValuePosition.INSIDE_SLICE);
                    pieDataSet.setColors(colors);
                    PieData pieData = new PieData(pieDataSet);
                    pieChartIncomeCategory.getDescription().setEnabled(false);
                    pieChartIncomeCategory.setDrawEntryLabels(false);
                    pieChartIncomeCategory.setDrawCenterText(true);
                    pieChartIncomeCategory.animateY(2000);
                    pieChartIncomeCategory.setData(pieData);
                    pieChartIncomeCategory.invalidate();
                }
            }
            @Override
            public void onFailure(Call<ApiResponse<Object>> call, Throwable t) {

            }
        });
    }

    private void colorPieChartIncome()
    {
        Retrofit retrofit = HTTPService.getInstance();
        HTTPRequest httpRequest = retrofit.create(HTTPRequest.class);
        Call<List<CategoryModel>> call = httpRequest.incomeCategory();
        call.enqueue(new Callback<List<CategoryModel>>() {
            @Override
            public void onResponse(Call<List<CategoryModel>> call, Response<List<CategoryModel>> response) {
                if(response.isSuccessful())
                {
                    mListCategoryIncome = response.body();
                    for(CategoryModel item : mListCategoryIncome)
                    {
                        colorIncomeCategory.add(item.getColor());
                        nameIncomeCategory.add(item.getName());
                    }
                    setupPieChartIncome();
                }
            }
            @Override
            public void onFailure(Call<List<CategoryModel>> call, Throwable t) {
            }
        });
    }

}