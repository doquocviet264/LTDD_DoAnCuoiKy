package com.example.appdoan.Fragment.quanlythuchi_app.Activity.Goal;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quanlythuchi_app.API.HTTPRequest;
import com.example.quanlythuchi_app.API.HTTPService;
import com.example.quanlythuchi_app.Activity.Goal.GoalIntroductionActivity;
import com.example.quanlythuchi_app.Container.Request.GoalRequest;
import com.example.quanlythuchi_app.Container.Response.ApiResponse;
import com.example.quanlythuchi_app.R;

import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class GoalCreateActivity extends AppCompatActivity {
    EditText edtGoalName,edtGoalAmount,edtGoalBalance,edtGoalDeadline;
    Button btnGoalCreate;
    ImageButton btnBackGoalCreate;
    String dateTemp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goal_create);
        setControl();
        setEvent();
    }
    private void setControl() {
        edtGoalName=findViewById(R.id.edtGoalName);
        edtGoalAmount=findViewById(R.id.edtGoalAmount);
        edtGoalBalance=findViewById(R.id.edtGoalBalance);
        edtGoalDeadline=findViewById(R.id.edtGoalDeadline);
        edtGoalDeadline.setInputType(InputType.TYPE_NULL);
        btnGoalCreate=findViewById(R.id.btnGoalCreate);
        btnBackGoalCreate=findViewById(R.id.btnBackGoalCreate);
    }
    private void setEvent() {
        btnBackGoalCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        edtGoalDeadline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtGoalDeadline.clearFocus();
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month=calendar.get(Calendar.MONTH);
                int day=calendar.get(Calendar.DATE);

                DatePickerDialog datePickerDialog = new DatePickerDialog(GoalCreateActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String dayString =(dayOfMonth<10)?"0"+dayOfMonth:String.valueOf(dayOfMonth);
                        String monthString = ((month + 1) < 10) ? "0" + (month + 1) : String.valueOf(month + 1);
                        edtGoalDeadline.setText(dayString + "-" + monthString + "-" + year);
                        dateTemp=year + "-" + monthString + "-" + dayString;
                    }},year,month,day);
                datePickerDialog.show();
            }
        });
        btnGoalCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Retrofit retrofit = HTTPService.getInstance();
                HTTPRequest httpRequest = retrofit.create(HTTPRequest.class);
//                SharedPreferences sharedPreferences = getSharedPreferences("MyApp", Context.MODE_PRIVATE);

                String goalName,goalAmount,goalBalance,goalDeadline;
                goalName=edtGoalName.getText().toString();
                goalAmount=edtGoalAmount.getText().toString();
                goalBalance=edtGoalBalance.getText().toString();
                goalDeadline=edtGoalDeadline.getText().toString();
                if(goalName.isEmpty()||goalAmount.isEmpty()||goalBalance.isEmpty()||goalDeadline.isEmpty()){
                    Toast.makeText(GoalCreateActivity.this, "Vui lòng nhập đủ thông tin", Toast.LENGTH_SHORT).show();
                }else {
                    Long goalBalanceNumb = Long.valueOf(goalBalance);
                    Long goalAmountNumb = Long.valueOf(goalAmount);
                    String goalDeadlineDate= dateTemp;

                    GoalRequest goalRequest = new GoalRequest(goalName, goalBalanceNumb, goalAmountNumb,goalDeadlineDate);
//                    String token = sharedPreferences.getString("token", "");
                    Call<ApiResponse<Object>> call = httpRequest.addGoal(goalRequest);
                    call.enqueue(new Callback<ApiResponse<Object>>() {
                        @Override
                        public void onResponse(Call<ApiResponse<Object>> call, Response<ApiResponse<Object>> response) {
                            if(response.isSuccessful())
                            {
                                ApiResponse<Object> apiResponse = response.body();
                                if(apiResponse.getStatus() == 200)
                                {
                                    Toast.makeText(GoalCreateActivity.this, "Thêm mục tiêu thành công!", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(GoalCreateActivity.this, GoalIntroductionActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                                else {
                                    Toast.makeText(GoalCreateActivity.this, apiResponse.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                        @Override
                        public void onFailure(Call<ApiResponse<Object>> call, Throwable t) {
                            Toast.makeText(GoalCreateActivity.this, "Lỗi kết nối!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }
}