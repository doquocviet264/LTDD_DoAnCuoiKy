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
import com.example.appdoan.R;
import com.example.appdoan.Model.CardModel;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class BarChartCardFragment extends Fragment {
    private BarChart barChartCard;
    List<BarEntry> entriesIncome = new ArrayList<>();
    List<BarEntry> entriesExpense = new ArrayList<>();

    List<CardModel> mListCard = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bar_chart_card, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        barChartCard = view.findViewById(R.id.barChartCard);
        setupBarChart();
        getAllCard();
    }

    private void setupBarChart() {
        Retrofit retrofit = HTTPService.getInstance();
        HTTPRequest httpRequest = retrofit.create(HTTPRequest.class);
        Call<ApiResponse<Object>> call = httpRequest.listTotalIncomeByCard();
        call.enqueue(new Callback<ApiResponse<Object>>() {
            @Override
            public void onResponse(Call<ApiResponse<Object>> call, Response<ApiResponse<Object>> response) {
                if (response.isSuccessful()) {
                    ApiResponse apiResponse = response.body();
                    List<?> incomeData = (List<?>) apiResponse.getData();
                    for (int i = 0; i < incomeData.size(); i++) {
                        Object data = incomeData.get(i);
                        if (data instanceof Double) {
                            Double doubleValue = (Double) data;
                            Long longValue = doubleValue.longValue();
                            entriesIncome.add(new BarEntry(i, longValue));
                        }
                    }
                    createBarChart();
                }
            }
            @Override
            public void onFailure(Call<ApiResponse<Object>> call, Throwable t) {

            }
        });

        Call<ApiResponse<Object>> call1 = httpRequest.listTotalExpenseByCard();
        call1.enqueue(new Callback<ApiResponse<Object>>() {
            @Override
            public void onResponse(Call<ApiResponse<Object>> call, Response<ApiResponse<Object>> response) {
                if (response.isSuccessful()) {
                    ApiResponse apiResponse = response.body();
                    List<?> expenseData = (List<?>) apiResponse.getData();
                    for (int i = 0; i < expenseData.size(); i++) {
                        Object data = expenseData.get(i);
                        if (data instanceof Double) {
                            Double doubleValue = (Double) data;
                            Long longValue = doubleValue.longValue();
                            entriesExpense.add(new BarEntry(i, longValue));
                        }
                    }
                    createBarChart();
                }
            }
            @Override
            public void onFailure(Call<ApiResponse<Object>> call, Throwable t) {

            }
        });
    }

    private void createBarChart() {
        if (!entriesIncome.isEmpty() && !entriesExpense.isEmpty()) {
            // Khởi tạo dataset cho các cột biểu đồ với dữ liệu entriesIncome và entriesExpense
            BarDataSet dataSetIncome = new BarDataSet(entriesIncome, "Thu nhập");
            BarDataSet dataSetExpense = new BarDataSet(entriesExpense, "Chi tiêu");
            dataSetIncome.setBarBorderWidth(0.5f);
            dataSetExpense.setBarBorderWidth(0.5f);

            // Cấu hình các thuộc tính cho biểu đồ
            dataSetIncome.setColor(Color.BLUE);
            dataSetIncome.setValueTextColor(Color.BLACK);
            dataSetExpense.setColor(Color.RED);
            dataSetExpense.setValueTextColor(Color.BLACK);

            BarData barData = new BarData(dataSetIncome, dataSetExpense);
            barChartCard.setData(barData); // set dữ liệu

            // đổ dữ liệu vào nhãn x với tên các thẻ
            List<String> xAxisLabels = new ArrayList<>();
            for (CardModel card : mListCard) {
                xAxisLabels.add(card.getName());
            }
            XAxis xAxis = barChartCard.getXAxis();
            xAxis.setValueFormatter(new IndexAxisValueFormatter(xAxisLabels));
            xAxis.setCenterAxisLabels(true);
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis.setGranularity(1);
            xAxis.setGranularityEnabled(true);

            // Chia biểu đồ cột thành các nhóm
            barChartCard.setDragEnabled(true);
            barChartCard.setVisibleXRangeMaximum(3);

            barChartCard.setFitBars(true);
            barChartCard.getDescription().setEnabled(false);

            float barSpace = 0.1f;
            float groupSpace = 0.5f;
            barData.setBarWidth(0.15f); // Đặt độ rộng của các cột

            barChartCard.getXAxis().setAxisMinimum(0);
            barChartCard.getXAxis().setAxisMaximum(0+ barChartCard.getBarData().getGroupWidth(groupSpace, barSpace) * xAxisLabels.size());
            barChartCard.getAxisLeft().setAxisMinimum(0);
            barChartCard.getAxisRight().setEnabled(false);
            barChartCard.animateY(2000);
            barChartCard.groupBars(0, groupSpace, barSpace);
            barChartCard.invalidate();
        }
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
                createBarChart();
            }
            @Override
            public void onFailure(Call<List<CardModel>> call, Throwable t) {
            }
        });

    }
}