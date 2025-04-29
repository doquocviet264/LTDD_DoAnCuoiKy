package com.example.appdoan.API;

import okhttp3.Interceptor;
import androidx.annotation.NonNull;
import com.example.appdoan.Helper.MySharedPreferences;
import java.io.IOException;


import okhttp3.Request;
import okhttp3.Response;
public class MyInterceptor implements Interceptor {

    @NonNull
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request originalRequest = chain.request();

        MySharedPreferences mySharedPreferences = MySharedPreferences.getInstance();
        String token = mySharedPreferences.getToken();
        Request request = originalRequest.newBuilder()
                .addHeader("Authorization", "Bearer" + token).build();
        return chain.proceed(request);
    }
}
