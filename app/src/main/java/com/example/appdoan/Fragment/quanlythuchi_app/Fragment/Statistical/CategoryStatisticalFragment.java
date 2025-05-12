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
import android.widget.LinearLayout;
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
import com.example.quanlythuchi_app.Model.BudgetModel;
import com.example.quanlythuchi_app.Model.CategoryModel;
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


public class CategoryStatisticalFragment extends Fragment {

    private String currentQuery = "";
    TransactionAdapter adapter;
    private RecyclerView rvAllTran;
    private TextView statCategoryName,tvIncomeorExpense,statCategoryIncomeorExpense,statCategoryBudget;
    private Spinner edtCategoryStat,edtCategotyType;
    List<String> categoryType = new ArrayList<>();
    private EditText edtDayFilterFrom,edtDayFilterTo;
    private Button btnFilter;
    private Button btnCategoryStatFilter,btnCategoryStatSearch;
    private LinearLayout filterDay;
    boolean isVisible = false;
    String categorySelected,categoryTypeSelected;
    Long amount,categoryId;
    Long bugetId;
    private CategoryModel categoryModel;
    private BudgetModel budgetModel;
    private List<TransactionModel> listTransaction;
    private List<CategoryModel> listCategoryModel;
    private List<BudgetModel> budgetModel1;

    @Nullable
    @Override
    public View onCreateView(@Nullable LayoutInflater inflater,@Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category_statistical, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvAllTran = view.findViewById(R.id.rvAllTran);
        //khoi tao layout
        edtCategoryStat = view.findViewById(R.id.edtCategoryStat);
        edtCategotyType = view.findViewById(R.id.edtCategoryTypeStat);
        btnFilter=view.findViewById(R.id.btnFilter);
        edtDayFilterFrom = view.findViewById(R.id.edtDayFilterFrom);
        edtDayFilterTo = view.findViewById(R.id.edtDayFilterTo);
        edtDayFilterFrom.setInputType(InputType.TYPE_NULL);
        edtDayFilterTo.setInputType(InputType.TYPE_NULL);

        categoryType.add("Loại");
        categoryType.add("Thu");
        categoryType.add("Chi");
        ArrayAdapter<String> listCategoryType = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, categoryType);
        listCategoryType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        edtCategotyType.setAdapter(listCategoryType);

        edtDayFilterFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtDayFilterFrom.clearFocus();
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DATE);

                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String dayString = (dayOfMonth < 10) ? "0" + dayOfMonth : String.valueOf(dayOfMonth);
                        String monthString = ((month + 1) < 10) ? "0" + (month + 1) : String.valueOf(month + 1);
                        edtDayFilterFrom.setText(dayString + "-" + monthString + "-" + year);

                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });
        edtDayFilterTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtDayFilterTo.clearFocus();
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DATE);

                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String dayString = (dayOfMonth < 10) ? "0" + dayOfMonth : String.valueOf(dayOfMonth);
                        String monthString = ((month + 1) < 10) ? "0" + (month + 1) : String.valueOf(month + 1);
                        edtDayFilterTo.setText(dayString + "-" + monthString + "-" + year);

                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });
        listTransaction = new ArrayList<>();

        listCategoryModel=new ArrayList<>();
        List<String> listCategoryNameExpense = new ArrayList<>();
        List<Long> listCategoryIdExpense = new ArrayList<>();
        List<String> listCategoryName = new ArrayList<>();
        List<Long> listCategoryId = new ArrayList<>();
        List<Long> listBudgetCategoryId = new ArrayList<>();
        List<Long> listBudgetId = new ArrayList<>();

        //khoi tao spinner category
        Retrofit retrofit = HTTPService.getInstance();
        HTTPRequest httpRequest = retrofit.create(HTTPRequest.class);
        Call <ApiResponse<Object>> callListTransaction = httpRequest.getAll();
        callListTransaction.enqueue(new Callback<ApiResponse<Object>>() {
            @Override
            public void onResponse(Call<ApiResponse<Object>> call, Response<ApiResponse<Object>> response) {

                ApiResponse<Object> apiResponse = response.body();// Đối tượng ApiResponse<Object> của bạn
                Gson gson = new Gson();
                String jsonData = gson.toJson(apiResponse.getData());
                listTransaction = gson.fromJson(jsonData, new TypeToken<List<TransactionModel>>(){}.getType());

                rvAllTran.setLayoutManager(new LinearLayoutManager(getContext()));
                rvAllTran.setHasFixedSize(true);
                adapter = new TransactionAdapter(listTransaction, getContext());
                rvAllTran.setAdapter(adapter);
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onFailure(Call<ApiResponse<Object>> call, Throwable t) {

            }
        });
        Call<List<CategoryModel>> callListCategoryModel = httpRequest.getCategory();
        callListCategoryModel.enqueue(new Callback<List<CategoryModel>>() {
            @Override
            public void onResponse(Call<List<CategoryModel>> call, Response<List<CategoryModel>> response) {
                listCategoryModel = response.body();
                List<String> listTypeDefaultName = new ArrayList<>();
                boolean btnFilterText = true;
                List<String> listTypeThu= new ArrayList<>();
                List<String> listTypeChi = new ArrayList<>();
                List<String> listCategoryName = new ArrayList<>();

                for(CategoryModel modal:listCategoryModel){
                    listCategoryName.add(modal.getName());
                }
                List<CategoryModel> listCategoryThu = new ArrayList<>();
                List<CategoryModel> listCategoryChi = new ArrayList<>();
                listTypeDefaultName.add("Danh mục");
                ArrayAdapter<String> listCategoryThuAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, listTypeDefaultName);
                listCategoryThuAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                edtCategoryStat.setAdapter(listCategoryThuAdapter);
                edtCategotyType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        categoryTypeSelected = (String) parent.getItemAtPosition(position);

                        if (categoryTypeSelected=="Loại"){
                            Call <ApiResponse<Object>> callListTransaction = httpRequest.getAll();
                            callListTransaction.enqueue(new Callback<ApiResponse<Object>>() {
                                @Override
                                public void onResponse(Call<ApiResponse<Object>> call, Response<ApiResponse<Object>> response) {

                                    ApiResponse<Object> apiResponse = response.body();// Đối tượng ApiResponse<Object> của bạn
                                    Gson gson = new Gson();
                                    String jsonData = gson.toJson(apiResponse.getData());
                                    listTransaction = gson.fromJson(jsonData, new TypeToken<List<TransactionModel>>(){}.getType());

                                    rvAllTran.setLayoutManager(new LinearLayoutManager(getContext()));
                                    rvAllTran.setHasFixedSize(true);
                                    adapter = new TransactionAdapter(listTransaction, getContext());
                                    rvAllTran.setAdapter(adapter);
                                    adapter.notifyDataSetChanged();
                                }

                                @Override
                                public void onFailure(Call<ApiResponse<Object>> call, Throwable t) {

                                }
                            });
                            edtDayFilterFrom.setText("");
                            edtDayFilterTo.setText("");
                            ArrayAdapter<String> listCategoryDefaultAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, listTypeDefaultName);
                            listCategoryDefaultAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            edtCategoryStat.setAdapter(listCategoryDefaultAdapter);
                        }
                        if(categoryTypeSelected=="Thu"){
                            for(CategoryModel model:listCategoryModel){
                                if(model.getType()==1L){
                                    listTypeThu.add(model.getName());
                                    listCategoryThu.add(model);
                                }

                            }
                            ArrayAdapter<String> listCategoryThuAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, listTypeThu);
                            listCategoryThuAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            edtCategoryStat.setAdapter(listCategoryThuAdapter);

                        }
                        if (categoryTypeSelected == "Chi") {
                            for(CategoryModel model:listCategoryModel){
                                if(model.getType()==2L){
                                    listTypeChi.add(model.getName());
                                    listCategoryChi.add(model);
                                }
                            }
                            ArrayAdapter<String> listCategoryChiAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, listTypeChi);
                            listCategoryChiAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            edtCategoryStat.setAdapter(listCategoryChiAdapter);
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                edtCategoryStat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        categorySelected = (String) parent.getItemAtPosition(position);
                        if(categorySelected=="Danh mục"){

                            btnFilter.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    edtDayFilterFrom.setText("");
                                    edtDayFilterTo.setText("");
                                    Toast.makeText(getContext(), "Nhập các lựa chọn cụ thể", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                        for(CategoryModel model : listCategoryThu){
                            if(model.getName().equals(categorySelected)){
                                Call <ApiResponse<Object>> callListTransactionThu = httpRequest.getAll();
                                callListTransactionThu.enqueue(new Callback<ApiResponse<Object>>() {
                                    @Override
                                    public void onResponse(Call<ApiResponse<Object>> call, Response<ApiResponse<Object>> response) {
                                        List<TransactionModel> transactionModelThu = new ArrayList<>();
                                        ApiResponse<Object> apiResponse = response.body();// Đối tượng ApiResponse<Object> của bạn
                                        Gson gson = new Gson();
                                        String jsonData = gson.toJson(apiResponse.getData());
                                        listTransaction = gson.fromJson(jsonData, new TypeToken<List<TransactionModel>>(){}.getType());
                                        for(TransactionModel model1:listTransaction) {
                                            if (model1.getCategoryModel().getId() == model.getId()) {
                                                transactionModelThu.add(model1);
                                            }
                                        }
                                        btnFilter.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                if(!edtDayFilterFrom.getText().toString().isEmpty()&&!edtDayFilterTo.getText().toString().isEmpty()){
                                                    String from,to;
                                                    try {
                                                        from = FormatDate.ChangeFormatToYDM(edtDayFilterFrom.getText().toString());
                                                    } catch (ParseException e) {
                                                        throw new RuntimeException(e);
                                                    }
                                                    try {
                                                        to = FormatDate.ChangeFormatToYDM(edtDayFilterTo.getText().toString());
                                                    } catch (ParseException e) {
                                                        throw new RuntimeException(e);
                                                    }
                                                    Call <ApiResponse<Object>> callListTranByCategoryAndDate = httpRequest.getAllByCategory(model.getId(),from,to);
                                                    callListTranByCategoryAndDate.enqueue(new Callback<ApiResponse<Object>>() {
                                                        @Override
                                                        public void onResponse(Call<ApiResponse<Object>> call, Response<ApiResponse<Object>> response) {
                                                            ApiResponse<Object> apiResponse = response.body();// Đối tượng ApiResponse<Object> của bạn
                                                            Gson gson = new Gson();
                                                            String jsonData = gson.toJson(apiResponse.getData());
                                                            listTransaction = gson.fromJson(jsonData, new TypeToken<List<TransactionModel>>(){}.getType());
                                                            rvAllTran.setLayoutManager(new LinearLayoutManager(getContext()));
                                                            rvAllTran.setHasFixedSize(true);
                                                            adapter = new TransactionAdapter(listTransaction, getContext());
                                                            rvAllTran.setAdapter(adapter);
                                                            adapter.notifyDataSetChanged();
                                                        }

                                                        @Override
                                                        public void onFailure(Call<ApiResponse<Object>> call, Throwable t) {

                                                        }
                                                    });
                                                    edtDayFilterFrom.setText("");
                                                    edtDayFilterTo.setText("");

                                                } else if(edtDayFilterFrom.getText().toString().isEmpty()&&edtDayFilterTo.getText().toString().isEmpty()){
                                                    rvAllTran.setLayoutManager(new LinearLayoutManager(getContext()));
                                                    rvAllTran.setHasFixedSize(true);
                                                    adapter = new TransactionAdapter( transactionModelThu, getContext());
                                                    rvAllTran.setAdapter(adapter);
                                                    adapter.notifyDataSetChanged();
                                                }else {
                                                    Toast.makeText(getContext(), "Nhập đầy đủ thông tin ngày tháng", Toast.LENGTH_SHORT).show();
                                                    edtDayFilterFrom.setText("");
                                                    edtDayFilterTo.setText("");
                                                }




                                            }
                                        });
                                    }

                                    @Override
                                    public void onFailure(Call<ApiResponse<Object>> call, Throwable t) {

                                    }
                                });
                            }
                        }
                        for(CategoryModel model : listCategoryChi){
                            if(model.getName().equals(categorySelected)){
                                Call <ApiResponse<Object>> callListTransactionThu = httpRequest.getAll();
                                callListTransactionThu.enqueue(new Callback<ApiResponse<Object>>() {
                                    @Override
                                    public void onResponse(Call<ApiResponse<Object>> call, Response<ApiResponse<Object>> response) {
                                        List<TransactionModel> transactionModelChi = new ArrayList<>();
                                        ApiResponse<Object> apiResponse = response.body();// Đối tượng ApiResponse<Object> của bạn
                                        Gson gson = new Gson();
                                        String jsonData = gson.toJson(apiResponse.getData());
                                        listTransaction = gson.fromJson(jsonData, new TypeToken<List<TransactionModel>>(){}.getType());
                                        for(TransactionModel model1:listTransaction){
                                            if(model1.getCategoryModel().getId()==model.getId()){
                                                transactionModelChi.add(model1);
                                            }
                                        }
                                        btnFilter.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {

                                                if(!edtDayFilterFrom.getText().toString().isEmpty()&&!edtDayFilterTo.getText().toString().isEmpty()){
                                                    String from,to;
                                                    try {
                                                        from = FormatDate.ChangeFormatToYDM(edtDayFilterFrom.getText().toString());
                                                    } catch (ParseException e) {
                                                        throw new RuntimeException(e);
                                                    }
                                                    try {
                                                        to = FormatDate.ChangeFormatToYDM(edtDayFilterTo.getText().toString());
                                                    } catch (ParseException e) {
                                                        throw new RuntimeException(e);
                                                    }
                                                    Call <ApiResponse<Object>> callListTranByCategoryAndDate = httpRequest.getAllByCategory(model.getId(),from,to);
                                                    callListTranByCategoryAndDate.enqueue(new Callback<ApiResponse<Object>>() {
                                                        @Override
                                                        public void onResponse(Call<ApiResponse<Object>> call, Response<ApiResponse<Object>> response) {
                                                            ApiResponse<Object> apiResponse = response.body();// Đối tượng ApiResponse<Object> của bạn
                                                            Gson gson = new Gson();
                                                            String jsonData = gson.toJson(apiResponse.getData());
                                                            listTransaction = gson.fromJson(jsonData, new TypeToken<List<TransactionModel>>(){}.getType());
                                                            rvAllTran.setLayoutManager(new LinearLayoutManager(getContext()));
                                                            rvAllTran.setHasFixedSize(true);
                                                            adapter = new TransactionAdapter(listTransaction, getContext());
                                                            rvAllTran.setAdapter(adapter);
                                                            adapter.notifyDataSetChanged();
                                                        }

                                                        @Override
                                                        public void onFailure(Call<ApiResponse<Object>> call, Throwable t) {

                                                        }
                                                    });
                                                    edtDayFilterFrom.setText("");
                                                    edtDayFilterTo.setText("");

                                                } else if(edtDayFilterFrom.getText().toString().isEmpty()&&edtDayFilterTo.getText().toString().isEmpty()){
                                                    rvAllTran.setLayoutManager(new LinearLayoutManager(getContext()));
                                                    rvAllTran.setHasFixedSize(true);
                                                    adapter = new TransactionAdapter(transactionModelChi, getContext());
                                                    rvAllTran.setAdapter(adapter);
                                                    adapter.notifyDataSetChanged();
                                                }else {
                                                    Toast.makeText(getContext(), "Nhập đầy đủ thông tin ngày tháng", Toast.LENGTH_SHORT).show();
                                                    edtDayFilterFrom.setText("");
                                                    edtDayFilterTo.setText("");
                                                }



                                            }
                                        });
                                    }

                                    @Override
                                    public void onFailure(Call<ApiResponse<Object>> call, Throwable t) {

                                    }
                                });
                            }
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }

            @Override
            public void onFailure(Call<List<CategoryModel>> call, Throwable t) {

            }
        });

    }


}