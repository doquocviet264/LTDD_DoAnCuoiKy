package com.example.appdoan.Fragment.quanlythuchi_app.Activity.Goal;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quanlythuchi_app.API.HTTPRequest;
import com.example.quanlythuchi_app.API.HTTPService;
import com.example.quanlythuchi_app.Activity.Goal.GoalIntroductionActivity;
import com.example.quanlythuchi_app.Container.Request.GoalRequest;
import com.example.quanlythuchi_app.Container.Response.ApiResponse;
import com.example.quanlythuchi_app.Model.GoalModel;
import com.example.quanlythuchi_app.R;
import com.example.quanlythuchi_app.Utils.FormatDate;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.text.ParseException;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class GoalUpdateActivity extends AppCompatActivity {
    TextView edtGoalNameUpdate,edtGoalAmountUpdate,edtGoalBalanceUpdate,edtGoalDeadlineUpdate;
    ImageButton btnBackGoalUpdate;
    Button btnGoalSaveUpdate,btnDeleteGoal;
    String dateTemp;
    int position;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goal_update);

        setControl();
        setEvent();
    }

    private void setControl() {
        edtGoalNameUpdate=findViewById(R.id.edtGoalNameUpadte);
        edtGoalAmountUpdate=findViewById(R.id.edtGoalAmountUpdate);
        edtGoalBalanceUpdate=findViewById(R.id.edtGoalBalanceUpdate);
        edtGoalDeadlineUpdate=findViewById(R.id.edtGoalDeadlineUpdate);
        edtGoalDeadlineUpdate.setInputType(InputType.TYPE_NULL);
        btnBackGoalUpdate=findViewById(R.id.btnBackGoalDetail);
        btnGoalSaveUpdate=findViewById(R.id.btnGoalSaveUpdate);
        btnDeleteGoal=findViewById(R.id.btnDeleteGoal);
        Intent intent = getIntent();
        position=intent.getIntExtra("position",0);
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

                Double amount = Double.valueOf(modalList.get(position).getAmount());
                Double balance= Double.valueOf(modalList.get(position).getBalance());
                int progress = (int) ((balance/amount)*100);
                String deadline=null;
                try {
                    deadline = FormatDate.ChangeFormatToDMY(modalList.get(position).getDeadline());
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }

                edtGoalNameUpdate.setText(modalList.get(position).getName());
                edtGoalAmountUpdate.setText(modalList.get(position).getAmount().toString());
                edtGoalBalanceUpdate.setText(modalList.get(position).getBalance().toString());
                edtGoalDeadlineUpdate.setText(deadline);

                btnGoalSaveUpdate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Retrofit retrofit = HTTPService.getInstance();
                        HTTPRequest httpRequest = retrofit.create(HTTPRequest.class);

                        String goalName,goalAmount,goalBalance,goalDeadline;
                        goalName=edtGoalNameUpdate.getText().toString();
                        goalAmount=edtGoalAmountUpdate.getText().toString();
                        goalBalance=edtGoalBalanceUpdate.getText().toString();
                        goalDeadline=edtGoalDeadlineUpdate.getText().toString();
                        if(goalName.isEmpty()||goalAmount.isEmpty()||goalBalance.isEmpty()||goalDeadline.isEmpty()){
                            Toast.makeText(GoalUpdateActivity.this, "Vui lòng nhập đủ thông tin", Toast.LENGTH_SHORT).show();
                        }else {
                            Long goalBalanceNumb = Long.valueOf(goalBalance);
                            Long goalAmountNumb = Long.valueOf(goalAmount);
                            String formattedDate = null;
                            try {
                                formattedDate = FormatDate.ChangeFormatToYDM(goalDeadline);
                            } catch (ParseException e) {
                                throw new RuntimeException(e);
                            }

                            GoalRequest goalRequest = new GoalRequest(goalName, goalBalanceNumb, goalAmountNumb,formattedDate);
                            Call<ApiResponse<Object>> call = httpRequest.updateGoal(goalRequest, modalList.get(position).getId());
                            call.enqueue(new Callback<ApiResponse<Object>>() {
                                @Override
                                public void onResponse(Call<ApiResponse<Object>> call, Response<ApiResponse<Object>> response) {
                                    if(response.isSuccessful())
                                    {
                                        ApiResponse<Object> apiResponse = response.body();
                                        if(apiResponse.getStatus() == 200)
                                        {
                                            Toast.makeText(GoalUpdateActivity.this, "Cập nhật mục tiêu thành công!", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(GoalUpdateActivity.this, GoalIntroductionActivity.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                        else {
                                            Toast.makeText(GoalUpdateActivity.this, apiResponse.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                                @Override
                                public void onFailure(Call<ApiResponse<Object>> call, Throwable t) {
                                    Toast.makeText(GoalUpdateActivity.this, "Lỗi kết nối!", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                });
                btnDeleteGoal.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(GoalUpdateActivity.this);
                        builder.setMessage("Bạn có thực sự muốn xóa mục tiêu?")
                                .setCancelable(false)
                                .setPositiveButton("Có", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        Retrofit retrofit = HTTPService.getInstance();
                                        HTTPRequest httpRequest = retrofit.create(HTTPRequest.class);
//                                SharedPreferences sharedPreferences = getSharedPreferences("MyApp", Context.MODE_PRIVATE);
//                                String token = sharedPreferences.getString("token", "");
                                        Call<ApiResponse<Object>> call = httpRequest.deleteGoal( modalList.get(position).getId());
                                        call.enqueue(new Callback<ApiResponse<Object>>() {
                                            @Override
                                            public void onResponse(Call<ApiResponse<Object>> call, Response<ApiResponse<Object>> response) {
                                                if(response.isSuccessful())
                                                {
                                                    ApiResponse<Object> apiResponse = response.body();
                                                    if(apiResponse.getStatus() == 101)
                                                    {
                                                        Toast.makeText(GoalUpdateActivity.this, "Không thể xóa mục tiêu!", Toast.LENGTH_SHORT).show();
                                                    }
                                                    else {
                                                        Toast.makeText(GoalUpdateActivity.this, "Xóa mục tiêu thành công!", Toast.LENGTH_SHORT).show();
                                                        Intent intent = new Intent(GoalUpdateActivity.this, GoalIntroductionActivity.class);
                                                        startActivity(intent);
                                                        finish();
                                                    }
                                                }
                                                else {
                                                    Toast.makeText(GoalUpdateActivity.this, "Thất bại! Vui lòng kiểm tra lại!", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                            @Override
                                            public void onFailure(Call<ApiResponse<Object>> call, Throwable t) {
                                                Toast.makeText(GoalUpdateActivity.this, "Lỗi kết nối!", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                })
                                .setNegativeButton("Không", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                        AlertDialog alert = builder.create();
                        alert.show();
                    }

                });
            }

            @Override
            public void onFailure(Call<ApiResponse<Object>> call, Throwable t) {

                Toast.makeText(GoalUpdateActivity.this, "Lỗi kết nối", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void setEvent() {
        btnBackGoalUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();

            }
        });
        edtGoalDeadlineUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtGoalDeadlineUpdate.clearFocus();
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month=calendar.get(Calendar.MONTH);
                int day=calendar.get(Calendar.DATE);

                DatePickerDialog datePickerDialog = new DatePickerDialog(GoalUpdateActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String dayString =(dayOfMonth<10)?"0"+dayOfMonth:String.valueOf(dayOfMonth);
                        String monthString = ((month + 1) < 10) ? "0" + (month + 1) : String.valueOf(month + 1);
                        edtGoalDeadlineUpdate.setText(dayString + "-" + monthString + "-" + year);
                        dateTemp=year + "-" + monthString + "-" + dayString;
                    }},year,month,day);
                datePickerDialog.show();
            }
        });

    }
}