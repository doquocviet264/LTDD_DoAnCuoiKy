package com.example.appdoan.API;

import com.example.appdoan.Container.Request.CategoryRequest;
import com.example.appdoan.Container.Request.GoalRequest;
import com.example.appdoan.Container.Request.LoginRequest;
import com.example.appdoan.Container.Request.RegisterRequest;
import com.example.appdoan.Container.Request.UpdateProfileRequest;
import com.example.appdoan.Container.Response.ApiResponse;
import com.example.appdoan.Container.Response.LoginResponse;
import com.example.appdoan.Container.Response.RegisterUserResponse;
import com.example.appdoan.Model.CategoryModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface HTTPRequest {
    // user
    @POST("api/auth/login")
    Call<ApiResponse<LoginResponse>> login(@Body LoginRequest loginRequest);

    @POST("api/auth/register")
    Call<RegisterUserResponse> register(@Body RegisterRequest registerUserRequest);

    @GET("api/user/profile")
    Call<RegisterUserResponse> getProfile();

    @PUT("api/user/profile")
    Call<ApiResponse<Object>> updateProfile( @Body UpdateProfileRequest updateProfileRequest);

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
    // ======== goal
    @GET("api/goal/all")
    Call<ApiResponse<Object>> getAllGoal();

    @POST("api/goal/add")
    Call<ApiResponse<Object>> addGoal(@Body GoalRequest goalRequest);

    @PUT("api/goal/update/{id}")
    Call<ApiResponse<Object>> updateGoal(@Body GoalRequest goalRequest,@Path("id") Long id);

    @DELETE("api/goal/delete/{id}")
    Call<ApiResponse<Object>> deleteGoal(@Path("id") Long id);

}
