package com.example.appdoan.API;

import com.example.appdoan.Container.Request.LoginRequest;
import com.example.appdoan.Container.Request.RegisterRequest;
import com.example.appdoan.Container.Response.ApiResponse;
import com.example.appdoan.Container.Response.LoginResponse;
import com.example.appdoan.Container.Response.RegisterResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public interface HTTPRequest {
    // user
    @POST("api/auth/login")
    Call<ApiResponse<LoginResponse>> login(@Body LoginRequest loginRequest);

    @POST("api/auth/register")
    Call<RegisterResponse> register(@Body RegisterRequest registerUserRequest);
//
//    @GET("api/user/profile")
//    Call<RegisterUserResponse> getProfile();
//
//    @PUT("api/user/profile")
//    Call<ApiResponse<Object>> updateProfile( @Body UpdateProfileRequest updateProfileRequest);
}
