package com.example.appdoan.Fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.appdoan.API.HTTPRequest;
import com.example.appdoan.API.HTTPService;
import com.example.appdoan.Container.Response.ApiResponse;
import com.example.appdoan.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LineChartTotalInYearFragment extends Fragment {

    private LineChart lineChartIncome, lineChartExpense;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_line_chart_total_in_year, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        lineChartIncome = view.findViewById(R.id.lineChartIncome);
        lineChartExpense = view.findViewById(R.id.lineChartExpense);

        setupLineChartIncome();
        setupLineChartExpense();
    }

    private void setupLineChartExpense() {
        Retrofit retrofit = HTTPService.getInstance();
        HTTPRequest httpRequest = retrofit.create(HTTPRequest.class);
        Call<ApiResponse<Object>> call = httpRequest.getTotalExpenseInYear();
        call.enqueue(new Callback<ApiResponse<Object>>() {
            @Override
            public void onResponse(Call<ApiResponse<Object>> call, Response<ApiResponse<Object>> response) {
                if(response.isSuccessful())
                {
                    ApiResponse apiResponse = response.body();
                    List<Entry> entries = new ArrayList<>();
                    List<?> dataList = (List<?>) apiResponse.getData();
                    for (int i = 0; i < dataList.size(); i++) {
                        Object data = dataList.get(i);
                        if (data instanceof Double) {
                            Double doubleValue = (Double) data;
                            Long longValue = doubleValue.longValue();
                            entries.add(new Entry(i, longValue));
                        }
                    }
                    LineDataSet dataSet = new LineDataSet(entries, "Chi tiêu");
                    // cấu hình trục x
                    dataSet.setColor(Color.RED);
                    dataSet.setLineWidth(2f);
                    dataSet.setCircleColor(Color.GREEN);
                    dataSet.setCircleRadius(3f);

                    LineData lineData = new LineData(dataSet);
                    XAxis xAxis = lineChartIncome.getXAxis();
                    String[] monthNames = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
                    xAxis = lineChartExpense.getXAxis();
                    xAxis.setLabelCount(12, true); // chỉ định số lượng nhãn trên trục x
                    xAxis.setValueFormatter(new ValueFormatter() {
                        @Override
                        public String getFormattedValue(float value) {
                            int monthIndex = (int) value;
                            return monthNames[monthIndex];
                        }
                    });
                    xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                    lineChartExpense.getDescription().setEnabled(false);
                    lineChartExpense.getAxisRight().setEnabled(false);
                    lineChartExpense.animateY(2000);
                    lineChartExpense.setData(lineData);
                    lineChartExpense.invalidate();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Object>> call, Throwable t) {
                Toast.makeText(getActivity(), "Lỗi kết nối!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupLineChartIncome() {
        Retrofit retrofit = HTTPService.getInstance();
        HTTPRequest httpRequest = retrofit.create(HTTPRequest.class);
        Call<ApiResponse<Object>> call = httpRequest.getTotalIncomeInYear();
        call.enqueue(new Callback<ApiResponse<Object>>() {
            @Override
            public void onResponse(Call<ApiResponse<Object>> call, Response<ApiResponse<Object>> response) {
                if(response.isSuccessful())
                {
                    ApiResponse apiResponse = response.body();
                    List<Entry> entries = new ArrayList<>();
                    List<?> dataList = (List<?>) apiResponse.getData();
                    for (int i = 0; i < dataList.size(); i++) {
                        Object data = dataList.get(i);
                        if (data instanceof Double) {
                            Double doubleValue = (Double) data;
                            Long longValue = doubleValue.longValue();
                            entries.add(new Entry(i, longValue));
                        }
                    }
                    LineDataSet dataSet = new LineDataSet(entries, "Thu nhập");

                    // cấu hình trục x
                    dataSet.setColor(Color.BLUE);
                    dataSet.setLineWidth(2f);
                    dataSet.setCircleColor(Color.GREEN);
                    dataSet.setCircleRadius(3f);

                    LineData lineData = new LineData(dataSet);
                    // cấu hình trục X
                    XAxis xAxis = lineChartIncome.getXAxis();
                    String[] monthNames = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
                    xAxis = lineChartIncome.getXAxis();
                    xAxis.setLabelCount(12, true); // chỉ định số lượng nhãn trên trục x
                    xAxis.setValueFormatter(new ValueFormatter() {
                        @Override
                        public String getFormattedValue(float value) {
                            int monthIndex = (int) value;
                            return monthNames[monthIndex];
                        }
                    });
                    xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                    lineChartIncome.getDescription().setEnabled(false);
                    lineChartIncome.getAxisRight().setEnabled(false);
                    lineChartIncome.animateY(2000);
                    lineChartIncome.setData(lineData);
                    lineChartIncome.invalidate();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Object>> call, Throwable t) {
                Toast.makeText(getActivity(), "Lỗi kết nối!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}