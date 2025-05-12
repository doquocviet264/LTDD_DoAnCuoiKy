package com.example.appdoan.Fragment.quanlythuchi_app.Fragment.Statistical;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlythuchi_app.API.HTTPRequest;
import com.example.quanlythuchi_app.API.HTTPService;
import com.example.quanlythuchi_app.Adapter.TransactionAdapter;
import com.example.quanlythuchi_app.Container.Response.ApiResponse;
import com.example.quanlythuchi_app.Helper.Format;
import com.example.quanlythuchi_app.Model.TransactionModel;
import com.example.quanlythuchi_app.R;
import com.example.quanlythuchi_app.Utils.FormatDate;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class DateStatisticalFragment extends Fragment {
    TransactionAdapter adapter;
    private EditText edtCategoryFrom,edtCategoryTo;
    private TextView tvCategoryInfoChi,tvCategoryInfoThu;
    private Button btnFilter,btnUnFilter;
    private RecyclerView rvAllStat;
    private List<TransactionModel> listTransaction;

    @Nullable
    @Override
    public View onCreateView(@Nullable LayoutInflater inflater,@Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_date_statistical, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        edtCategoryFrom=view.findViewById(R.id.edtCategoryFrom);
        edtCategoryTo=view.findViewById(R.id.edtCategoryTo);
        btnFilter=view.findViewById(R.id.btnFilterCategoryStat);
        rvAllStat=view.findViewById(R.id.rvAllStat);
        tvCategoryInfoChi=view.findViewById(R.id.tvCategoryInfoChi);
        tvCategoryInfoThu=view.findViewById(R.id.tvCategoryInfoThu);
        btnUnFilter=view.findViewById(R.id.btnUnFilterCategoryStat);
        btnUnFilter.setVisibility(View.GONE);
        edtCategoryFrom.setInputType(InputType.TYPE_NULL);
        edtCategoryTo.setInputType(InputType.TYPE_NULL);

        edtCategoryFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DATE);

                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String dayString = (dayOfMonth < 10) ? "0" + dayOfMonth : String.valueOf(dayOfMonth);
                        String monthString = ((month + 1) < 10) ? "0" + (month + 1) : String.valueOf(month + 1);
                        edtCategoryFrom.setText(dayString + "-" + monthString + "-" + year);

                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });
        edtCategoryTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtCategoryTo.clearFocus();
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DATE);

                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String dayString = (dayOfMonth < 10) ? "0" + dayOfMonth : String.valueOf(dayOfMonth);
                        String monthString = ((month + 1) < 10) ? "0" + (month + 1) : String.valueOf(month + 1);
                        edtCategoryTo.setText(dayString + "-" + monthString + "-" + year);

                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });
        listTransaction=new ArrayList<>();
        Retrofit retrofit = HTTPService.getInstance();
        HTTPRequest httpRequest=retrofit.create(HTTPRequest.class);
        Call<ApiResponse<Object>> callTran = httpRequest.getAll();
        callTran.enqueue(new Callback<ApiResponse<Object>>() {
            @Override
            public void onResponse(Call<ApiResponse<Object>> call, Response<ApiResponse<Object>> response) {
                ApiResponse<Object> apiResponse = response.body();
                Gson gson = new Gson();
                String jsonData = gson.toJson(apiResponse.getData());
                listTransaction = gson.fromJson(jsonData, new TypeToken<List<TransactionModel>>(){}.getType());

                Long thu1 = 0L,chi = 0L;
                for(TransactionModel model:listTransaction){
                    if(model.getType()==1L) thu1+=model.getAmount();
                    if(model.getType()==2L) chi+=model.getAmount();
                }
                tvCategoryInfoChi.setText(Format.formatNumber(chi));
                tvCategoryInfoThu.setText(Format.formatNumber(thu1));
                rvAllStat.setLayoutManager(new LinearLayoutManager(getContext()));
                rvAllStat.setHasFixedSize(true);
                adapter = new TransactionAdapter(listTransaction, getContext());
                rvAllStat.setAdapter(adapter);
                adapter.notifyDataSetChanged();




            }

            @Override
            public void onFailure(Call<ApiResponse<Object>> call, Throwable t) {

            }
        });
        btnUnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnFilter.setVisibility(View.VISIBLE);
                btnUnFilter.setVisibility(View.GONE);
                Call<ApiResponse<Object>> callTran = httpRequest.getAll();
                callTran.enqueue(new Callback<ApiResponse<Object>>() {
                    @Override
                    public void onResponse(Call<ApiResponse<Object>> call, Response<ApiResponse<Object>> response) {
                        ApiResponse<Object> apiResponse = response.body();
                        Gson gson = new Gson();
                        String jsonData = gson.toJson(apiResponse.getData());
                        listTransaction = gson.fromJson(jsonData, new TypeToken<List<TransactionModel>>(){}.getType());

                        Long thu1 = 0L,chi = 0L;
                        for(TransactionModel model:listTransaction){
                            if(model.getType()==1L) thu1+=model.getAmount();
                            if(model.getType()==2L) chi+=model.getAmount();
                        }
                        edtCategoryFrom.setText("");
                        edtCategoryTo.setText("");
                        tvCategoryInfoChi.setText(Format.formatNumber(chi));
                        tvCategoryInfoThu.setText(Format.formatNumber(thu1));
                        rvAllStat.setLayoutManager(new LinearLayoutManager(getContext()));
                        rvAllStat.setHasFixedSize(true);
                        adapter = new TransactionAdapter(listTransaction, getContext());
                        rvAllStat.setAdapter(adapter);
                        adapter.notifyDataSetChanged();




                    }

                    @Override
                    public void onFailure(Call<ApiResponse<Object>> call, Throwable t) {

                    }
                });
            }
        });
        btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(edtCategoryFrom.getText().toString().isEmpty()||edtCategoryTo.getText().toString().isEmpty()){
                    Toast.makeText(getContext(), "Nhập đủ thông tin", Toast.LENGTH_SHORT).show();
                }else {
                    btnFilter.setVisibility(View.GONE);
                    btnUnFilter.setVisibility(View.VISIBLE);
                    String from ,to;
                    try {
                        from = FormatDate.ChangeFormatToYDM(edtCategoryFrom.getText().toString());
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                    try {
                        to =FormatDate.ChangeFormatToYDM(edtCategoryTo.getText().toString());
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                    Call<ApiResponse<Object>> expenseFilter =httpRequest.getTotalExpenseInTime(from,to);
                    expenseFilter.enqueue(new Callback<ApiResponse<Object>>() {
                        @Override
                        public void onResponse(Call<ApiResponse<Object>> call, Response<ApiResponse<Object>> response) {
                            if (response.isSuccessful()){
                                ApiResponse<Object> apiResponse=response.body();
                                if(apiResponse.getStatus()==200){

                                    tvCategoryInfoChi.setText(apiResponse.getData().toString());

                                }else {
                                    Toast.makeText(getContext(), apiResponse.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<ApiResponse<Object>> call, Throwable t) {

                        }
                    });
                    Call<ApiResponse<Object>> incomeFilter = httpRequest.getTotalIncomeInTime(from,to);
                    incomeFilter.enqueue(new Callback<ApiResponse<Object>>() {
                        @Override
                        public void onResponse(Call<ApiResponse<Object>> call, Response<ApiResponse<Object>> response) {
                            if(response.isSuccessful()){
                                ApiResponse<Object> apiResponse = response.body();
                                if(apiResponse.getStatus()==200){

                                    tvCategoryInfoThu.setText(apiResponse.getData().toString());
                                }else {
                                    Toast.makeText(getContext(), apiResponse.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<ApiResponse<Object>> call, Throwable t) {

                        }
                    });
                    Call<ApiResponse<Object>> callFromTo = httpRequest.getTransactionFromTo(from,to);
                    callFromTo.enqueue(new Callback<ApiResponse<Object>>() {
                        @Override
                        public void onResponse(Call<ApiResponse<Object>> call, Response<ApiResponse<Object>> response) {
                            if(response.isSuccessful()){
                                ApiResponse apiResponse = response.body();
                                if(apiResponse.getStatus()==200){
                                    Gson gson = new Gson();
                                    String jsonData = gson.toJson(apiResponse.getData());
                                    listTransaction = gson.fromJson(jsonData, new TypeToken<List<TransactionModel>>(){}.getType());

                                    rvAllStat.setLayoutManager(new LinearLayoutManager(getContext()));
                                    rvAllStat.setHasFixedSize(true);
                                    adapter = new TransactionAdapter(listTransaction, getContext());
                                    rvAllStat.setAdapter(adapter);
                                    adapter.notifyDataSetChanged();
                                }else {
                                    Toast.makeText(getContext(), apiResponse.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<ApiResponse<Object>> call, Throwable t) {

                        }
                    });
                }
            }
        });
    }

}