package com.example.appdoan.Fragment.quanlythuchi_app.Fragment.Statistical;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
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
import com.example.quanlythuchi_app.Model.CardModel;
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

public class CardStatisticalFragment extends Fragment {
    TransactionAdapter adapter;
    private EditText edtFrom,edtTo;
    private Spinner edtCardName;
    String cardSelected;
    private TextView tvCategoryInfoChi,tvCategoryInfoThu;
    private Button btnFilter,btnUnFilter;
    private RecyclerView rvList;
    private List<CardModel> cardModel;
    private List<TransactionModel> listTransaction;
    private List<TransactionModel> listTransactionTemp;
    private List<TransactionModel> tranByCard;

    @Nullable
    @Override
    public View onCreateView(@Nullable LayoutInflater inflater,@Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_card_statistical, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        edtFrom=view.findViewById(R.id.edtCardFrom);
        edtTo=view.findViewById(R.id.edtCardTo);
        edtCardName=(Spinner) view.findViewById(R.id.edtCardName);
        btnFilter=view.findViewById(R.id.btnFilterCardStat);
        rvList=view.findViewById(R.id.rvAllCard);
        btnUnFilter=view.findViewById(R.id.btnUnFilterCardStat);
        btnUnFilter.setVisibility(View.GONE);
        edtFrom.setInputType(InputType.TYPE_NULL);
        edtTo.setInputType(InputType.TYPE_NULL);

        edtFrom.setOnClickListener(new View.OnClickListener() {
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
                        edtFrom.setText(dayString + "-" + monthString + "-" + year);

                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });
        edtTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtTo.clearFocus();
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DATE);

                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String dayString = (dayOfMonth < 10) ? "0" + dayOfMonth : String.valueOf(dayOfMonth);
                        String monthString = ((month + 1) < 10) ? "0" + (month + 1) : String.valueOf(month + 1);
                        edtTo.setText(dayString + "-" + monthString + "-" + year);

                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });
        listTransaction=new ArrayList<>();
        listTransactionTemp=new ArrayList<>();
        tranByCard = new ArrayList<>();
        List<String>cardName= new ArrayList<>();
        List<Long>cardId= new ArrayList<>();
        Retrofit retrofit = HTTPService.getInstance();
        HTTPRequest httpRequest=retrofit.create(HTTPRequest.class);
        Call<List<CardModel>> callTran = httpRequest.getAllCard();
        callTran.enqueue(new Callback<List<CardModel>>() {
            @Override
            public void onResponse(Call<List<CardModel>> call, Response<List<CardModel>> response) {

                cardModel = response.body();

                List<String> cardN = new ArrayList<>();
                cardN.add("Thẻ");
                List<Long> cardId=new ArrayList<>();
                for(CardModel modal:cardModel){
                    cardN.add(modal.getName());
                    cardId.add(modal.getId());
                }
                ArrayAdapter<String> listCardName = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, cardN);
                listCardName.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                listCardName.notifyDataSetChanged ();
                edtCardName.setAdapter(listCardName);

                edtCardName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        cardSelected =(String) parent.getItemAtPosition(position);

                        Call<ApiResponse<Object>> callTran = httpRequest.getAll();
                        callTran.enqueue(new Callback<ApiResponse<Object>>() {
                            @Override
                            public void onResponse(Call<ApiResponse<Object>> call, Response<ApiResponse<Object>> response) {
                                ApiResponse<Object> apiResponse = response.body();
                                Gson gson = new Gson();
                                String jsonData = gson.toJson(apiResponse.getData());
                                listTransaction = gson.fromJson(jsonData, new TypeToken<List<TransactionModel>>(){}.getType());
                                listTransactionTemp=listTransaction;
                                tranByCard=new ArrayList<>();
                                for(TransactionModel model:listTransaction){
                                    if(model.getCardModel().getName().equals(cardSelected)){
                                        tranByCard.add(model);
                                    }
                                }

                                rvList.setLayoutManager(new LinearLayoutManager(getContext()));
                                rvList.setHasFixedSize(true);
                                adapter = new TransactionAdapter(tranByCard, getContext());
                                rvList.setAdapter(adapter);
                                adapter.notifyDataSetChanged();
                                btnUnFilter.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        edtCardName.setSelection(0);
                                    }
                                });
                                btnFilter.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if(edtFrom.getText().toString().isEmpty()&&edtTo.getText().toString().isEmpty()){

                                        }else if(!edtFrom.getText().toString().isEmpty()&&!edtTo.getText().toString().isEmpty()){
                                            String from ,to;
                                            try {
                                                from = FormatDate.ChangeFormatToYDM(edtFrom.getText().toString());
                                            } catch (ParseException e) {
                                                throw new RuntimeException(e);
                                            }
                                            try {
                                                to =FormatDate.ChangeFormatToYDM(edtTo.getText().toString());
                                            } catch (ParseException e) {
                                                throw new RuntimeException(e);
                                            }
                                            Long idCard = cardId.get(position-1);
                                            Call<ApiResponse<Object>> callAllIncomeByCard = httpRequest.getAllIncomeByCard(idCard,from,to);
                                            callAllIncomeByCard.enqueue(new Callback<ApiResponse<Object>>() {
                                                @Override
                                                public void onResponse(Call<ApiResponse<Object>> call, Response<ApiResponse<Object>> response) {
                                                    ApiResponse<Object> apiResponse = response.body();
                                                    Gson gson = new Gson();
                                                    String jsonData = gson.toJson(apiResponse.getData());
                                                    listTransaction = gson.fromJson(jsonData, new TypeToken<List<TransactionModel>>(){}.getType());
                                                    rvList.setLayoutManager(new LinearLayoutManager(getContext()));
                                                    rvList.setHasFixedSize(true);
                                                    adapter = new TransactionAdapter(listTransaction, getContext());
                                                    rvList.setAdapter(adapter);
                                                    adapter.notifyDataSetChanged();
                                                }

                                                @Override
                                                public void onFailure(Call<ApiResponse<Object>> call, Throwable t) {

                                                }
                                            });
                                            btnFilter.setVisibility(View.GONE);
                                            btnUnFilter.setVisibility(View.VISIBLE);
                                            edtFrom.setText("");
                                            edtTo.setText("");
                                        }else {
                                            Toast.makeText(getContext(), "Nhập đủ ngày tháng", Toast.LENGTH_SHORT).show();

                                            edtFrom.setText("");
                                            edtTo.setText("");
                                        }
                                    }
                                });
                            }

                            @Override
                            public void onFailure(Call<ApiResponse<Object>> call, Throwable t) {

                            }
                        });
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
            }

            @Override
            public void onFailure(Call<List<CardModel>> call, Throwable t) {

            }
        });
    }
}