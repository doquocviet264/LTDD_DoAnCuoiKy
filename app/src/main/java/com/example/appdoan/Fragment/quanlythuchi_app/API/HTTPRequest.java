package com.example.appdoan.Fragment.quanlythuchi_app.API;

import com.example.quanlythuchi_app.Container.Request.BudgetRequest;
import com.example.quanlythuchi_app.Container.Request.CardRequest;
import com.example.quanlythuchi_app.Container.Request.CategoryRequest;
import com.example.quanlythuchi_app.Container.Request.GoalRequest;
import com.example.quanlythuchi_app.Container.Request.LoginRequest;
import com.example.quanlythuchi_app.Container.Request.RegisterUserRequest;
import com.example.quanlythuchi_app.Container.Request.TransactionRequest;
import com.example.quanlythuchi_app.Container.Request.UpdateProfileRequest;
import com.example.quanlythuchi_app.Container.Response.ApiResponse;
import com.example.quanlythuchi_app.Container.Response.LoginResponse;
import com.example.quanlythuchi_app.Container.Response.RegisterUserResponse;
import com.example.quanlythuchi_app.Model.CardModel;
import com.example.quanlythuchi_app.Model.CategoryModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface HTTPRequest {

    //  ============= user
    @POST("api/auth/login")
    Call<ApiResponse<LoginResponse>> login(@Body LoginRequest loginRequest);

    @POST("api/auth/register")
    Call<RegisterUserResponse> register(@Body RegisterUserRequest registerUserRequest);

    @GET("api/user/profile")
    Call<RegisterUserResponse> getProfile();

    @PUT("api/user/profile")
    Call<ApiResponse<Object>> updateProfile( @Body UpdateProfileRequest updateProfileRequest);

    // ============== card
    @GET("api/card/all")
    Call<List<CardModel>> getAllCard();

    @POST("api/card/add")
    Call<ApiResponse<Object>> addCard(@Body CardRequest cardRequest);

    @PUT("api/card/update/{id}")
    Call<ApiResponse<Object>> updateCard(@Body CardRequest cardRequest, @Path("id") Long id);

    @DELETE("api/card/delete/{id}")
    Call<ApiResponse<Object>> deleteCard(@Path("id") Long id);

    // ================ category
    @GET("api/category/income")
    Call<List<CategoryModel>> incomeCategory();

    @GET("api/category/expense")
    Call<List<CategoryModel>> expenseCategory();

    @DELETE("api/category/delete/{id}")
    Call<ApiResponse<Object>> deleteCategory(@Path("id") Long id);
    @POST("api/category/add")
    Call<ApiResponse<Object>> addCategory(@Body CategoryRequest categoryRequest);

    @PUT("api/category/update/{id}")
    Call<ApiResponse<Object>> updateCategory(@Path("id") Long id, @Body CategoryRequest categoryRequest);

    // ============== transaction
    @GET("api/transaction/income")
    Call<ApiResponse<Object>> getIncomeTransaction();

    @GET("api/transaction/expense")
    Call<ApiResponse<Object>> getExpenseTransaction();

    @GET("api/transaction/alltransactionintoday")
    Call<ApiResponse<Object>> getAllTransactionToday();

    @GET("api/transaction/allofweek")
    Call<ApiResponse<Object>> getAllTransactionOfWeek();

    @GET("api/transaction/allincurrentmonth")
    Call<ApiResponse<Object>> getAllTransactionInCurrentMonth();

    @POST("api/transaction/add/income/{idCategory}/{idCard}")
    Call<ApiResponse<Object>> addTransactionIncome(@Path("idCategory") Long idCategory, @Path("idCard") Long idCard, @Body TransactionRequest transactionRequest);

    @POST("api/transaction/add/expense/{idCategory}/{idCard}")
    Call<ApiResponse<Object>> addTransactionExpense(@Path("idCategory") Long idCategory, @Path("idCard") Long idCard, @Body TransactionRequest transactionRequest);

    @PUT("api/transaction/update/{idCategory}/{idTran}")
    Call<ApiResponse<Object>> updateTransaction(@Path("idCategory") Long idCategory, @Path("idTran") Long idTran, @Body TransactionRequest transactionRequest);

    @DELETE("api/transaction/delete/{idTran}")
    Call<ApiResponse<Object>> deleteTranscation(@Path("idTran") Long idTran);

    // lấy dữ liệu để vẽ biểu đồ
    @GET("api/transaction/totalincomeinyear")
    Call<ApiResponse<Object>> getTotalIncomeInYear();

    @GET("api/transaction/totalexpenseinyear")
    Call<ApiResponse<Object>> getTotalExpenseInYear();

    @GET("api/transaction/listtotalincomebycard")
    Call<ApiResponse<Object>> listTotalIncomeByCard();

    @GET("api/transaction/listtotalexpensebycard")
    Call<ApiResponse<Object>> listTotalExpenseByCard();

    @GET("api/transaction/totalbycategoryincomeinmonth")
    Call<ApiResponse<Object>> listTotalByCategoryIncome();

    @GET("api/transaction/totalbycategoryexpenseinmonth")
    Call<ApiResponse<Object>> listTotalByCategoryExpense();

    // ======= notification
    @GET("api/notification/all")
    Call<ApiResponse<Object>> listNotification();

    @DELETE("api/notification/delete/{id}")
    Call<ApiResponse<Object>> deleteNotification(@Path("id") Long idNoti);


    // ======== goal
    @GET("api/goal/all")
    Call<ApiResponse<Object>> getAllGoal();

    @POST("api/goal/add")
    Call<ApiResponse<Object>> addGoal(@Body GoalRequest goalRequest);

    @PUT("api/goal/update/{id}")
    Call<ApiResponse<Object>> updateGoal(@Body GoalRequest goalRequest,@Path("id") Long id);

    @DELETE("api/goal/delete/{id}")
    Call<ApiResponse<Object>> deleteGoal(@Path("id") Long id);

    // =============== budget
    @GET("api/budget/all")
    Call<ApiResponse<Object>> getAllBudget();

    @POST("api/budget/add/{idCategory}")
    Call<ApiResponse<Object>> addBudget(@Path("idCategory") Long idCategory, @Body BudgetRequest budgetRequest);

    @GET("api/category/all")
    Call<List<CategoryModel>> getCategory();

    @GET("api/budget/{id}")
    Call<ApiResponse<Object>> getBudget(@Path("id") Long id);

    @PUT("api/budget/update/{id}")
    Call<ApiResponse<Object>> updateBudget(@Path("id") Long id,@Body BudgetRequest budgetRequest);


    @DELETE("api/budget/delete/{id}")
    Call<ApiResponse<Object>> deleteBudget(@Path("id") Long id);


    // ================= thống kê giao dịch
    @GET("api/transaction/totalbycategory/{idCategory}")
    Call<ApiResponse<Object>> totalByCategoryInMonth(@Path("idCategory") Long id);
    //hom nay
    @GET("api/transaction/allincomeintoday")
    Call<ApiResponse<Object>> getAllIncomeToday();

    @GET("api/transaction/allexpensetoday")
    Call<ApiResponse<Object>> getAllExpenseToday();

    @GET("api/transaction/gettotalincometoday")
    Call<ApiResponse<Object>> getTotalIncomeToday();

    @GET("api/transaction/gettotalexpensetoday")
    Call<ApiResponse<Object>> getTotalExpenseToday();

    //theo tuan
    @GET("api/transaction/allofweek")
    Call<ApiResponse<Object>> getAllOfWeek();


    @GET("api/transaction/totalincomebyweek")
    Call<ApiResponse<Object>> getTotalIncomeByWeek();

    @GET("api/transaction/totalexpensebyweek")
    Call<ApiResponse<Object>> getTotalExpenseByWeek();

    //theo thang
    // tổng thu nhập và chi tiêu theo từng tháng
    @GET("api/transaction/totalincomebymonth/{year}/{month}")
    Call<ApiResponse<Object>> getTotalIncomeByMonth(@Path("year") int year, @Path("month") int month);

    @GET("api/transaction/totalexpensebymonth/{year}/{month}")
    Call<ApiResponse<Object>> getTotalExpenseByMonth(@Path("year") int year, @Path("month") int month);

    @GET("api/transaction/totalincome/currentmonth")
    Call<ApiResponse<Object>> getTotalIncomeByCurrentMonth();

    @GET("api/transaction/totalexpense/currentmonth")
    Call<ApiResponse<Object>> getTotalIncByCurrentMonth();

    @GET("api/transaction/totalincome/previousmonth")
    Call<ApiResponse<Object>> getTotalIncomeInPreviousMonth();

    @GET("api/transaction/totalexpense/previousmonth")
    Call<ApiResponse<Object>> getTotalExpenseInPreviousMonth();
    @GET("api/transaction/totalbycategory/{idCategory}/{date1}/{date2}")
    Call<ApiResponse<Object>> totalByCategoryInMonthFilter(@Path("idCategory") Long id, @Path("date1") String fromDate, @Path("date2") String toDate);

    @GET("api/transaction/category/{idCategory}/{fromDate}/{toDate}")
    Call<ApiResponse<Object>> getAllByCategory(@Path("idCategory") Long idCategory, @Path("fromDate") String fromDate, @Path("toDate") String toDate);
    @GET("api/transaction/all")
    Call<ApiResponse<Object>> getAll();
    // lấy tổng tiền giao dịch theo danh mục trong tháng hiện tại để kiểm tra ngân sách
    @GET("api/transaction/from/{date1}/to/{date2}")
    Call<ApiResponse<Object>> getTransactionFromTo(@Path("date1") String date1, @Path("date2") String date2);
    @GET("api/transaction/totalincome/from/{date1}/to/{date2}")
    Call<ApiResponse<Object>> getTotalIncomeInTime(@Path("date1") String fromDate, @Path("date2") String toDate);


    @GET("api/transaction/totalexpense/from/{date1}/to/{date2}")
    Call<ApiResponse<Object>> getTotalExpenseInTime(@Path("date1") String fromDate, @Path("date2") String toDate);

    @GET("api/transaction/all/card/{id}/{fromDate}/{toDate}")
    Call<ApiResponse<Object>> getAllIncomeByCard(@Path("id") Long id, @Path("fromDate") String fromDate, @Path("toDate") String toDate);


    // tổng thu nhập và chi tiêu của thẻ trong tháng
    @GET("api/transaction/totalincome/card/{idCard}")
    Call<ApiResponse<Object>> getTotalIncomeByCardInMonth(@Path("idCard") Long idCard);

    @GET("api/transaction/totalexpense/card/{idCard}")
    Call<ApiResponse<Object>> getTotalExpenseByCardInMonth(@Path("idCard") Long idCard);


    // tổng thu nhập và chi tiêu theo thẻ trong khoảng thời gian
    @GET(value = "/totalincome/card/{id}/from/{date1}/to/{date2}")
    Call<ApiResponse<Object>> getTotalIncomeByCard(@Path("id") Long idCard, @Path("date1") String fromDate, @Path("date2") String toDate);


    @GET(value = "/totalexpense/card/{id}/from/{date1}/to/{date2}")
    Call<ApiResponse<Object>> getTotalExpenseByCard(@Path("id") Long idCard, @Path("date1") String fromDate, @Path("date2") String toDate);
}
